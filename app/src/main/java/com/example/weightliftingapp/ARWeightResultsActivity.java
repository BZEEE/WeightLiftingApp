package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Switch;
import android.widget.Toast;

import com.example.weightliftingapp.OneRepMax.PowerliftingPlatesValues;
import com.example.weightliftingapp.OneRepMax.StandardPlatesValues;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;

import static com.example.weightliftingapp.ResultsActivity.plateFormatResponseId;
import static com.example.weightliftingapp.ResultsActivity.platesResponseId;

public class ARWeightResultsActivity extends AppCompatActivity {
    // Set to true ensures requestInstall() triggers installation if necessary.
    private boolean mUserRequestedInstall = true;
    private Session mSession;

    private ArFragment arFragment;

    private float plateRadius = 0.225f; // in metres
    private float plateHeight = 0.03f;

    private ModelRenderable barGripSectionRenderable;
    private ModelRenderable barLeftSectionRenderable;
    private ModelRenderable barRightSectionRenderable;
    private ModelRenderable[] plateRenderables;
    private ArrayList tempContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arweight_results);

        // get plates from One Rep max Activity, should be a list
        // that is a multiple of 2 for size [10,20,40,40,20,10]
        Intent intent = getIntent();
        // get plate list from intent
        ArrayList plateValues = intent.getIntegerArrayListExtra(platesResponseId);
        boolean plateFormat = intent.getBooleanExtra(plateFormatResponseId, false);

        // initialize the models
        SetupBarModel();
        SetupPlateModels(plateValues, plateFormat);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.weights_AR_fragment);

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                // add bar and plates primitives to the scene
                // place the models in the scene according to an anchor point
                // that the user created via a touch gesture on the AR screen
                CreateBarAndPlatesModel(anchorNode);
            }
        });

//
//
//        ModelRenderable.builder()
//            .setSource(this, R.raw.andy)
//            .build()
//            .thenAccept(renderable -> andyRenderable = renderable)
//            .exceptionally(
//                throwable -> {
//                    Log.e(TAG, "Unable to load Renderable.", throwable);
//                    return null;
//                });

        // andy renderable is directlyattached to root scene node
//        Node node = new Node();
//        node.setParent(arFragment.getArSceneView().getScene());
//        node.setRenderable(andyRenderable);


//        https://developers.google.com/ar/develop/java/sceneform/create-renderables
//        create shapes at runtime using ShapeFactory or MaterialFactory

//        whether to use ArFragment vs SceneView


    }

    @Override
    protected void onResume() {
        super.onResume();

        // ARCore requires camera permission to operate.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this);
            return;
        }

        // Make sure ARCore is installed and up to date.
        try {
            if (mSession == null) {
                switch (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                    case INSTALLED:
                        // Success, create the AR session.
                        mSession = new Session(this);
                        break;
                    case INSTALL_REQUESTED:
                        // Ensures next invocation of requestInstall() will either return
                        // INSTALLED or throw an exception.
                        mUserRequestedInstall = false;
                        return;
                }
            }
        } catch (UnavailableUserDeclinedInstallationException e) {
            // Display an appropriate message to the user and return gracefully.
            Toast.makeText(this, "TODO: handle exception " + e, Toast.LENGTH_LONG)
                    .show();
            return;
        } catch (Exception e) {  // Current catch statements.
            return;  // mSession is still null.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                    .show();
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this);
            }
            finish();
        }
    }

    private void SetupBarModel() {
    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Silver)
        .thenAccept(
                material -> {
                    barLeftSectionRenderable = ShapeFactory.makeCylinder(0.025f, 0.445f,  new Vector3(0.0f, -0.8775f, 0.25f), material);
                });

    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Silver)
        .thenAccept(
            material -> {
                barRightSectionRenderable = ShapeFactory.makeCylinder(0.025f, 0.445f,  new Vector3(0.0f, 0.8775f, 0.25f), material);
            });

    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Silver)
        .thenAccept(
            material -> {
                barGripSectionRenderable = ShapeFactory.makeCylinder(0.014f, 1.31f,  new Vector3(0.0f, 0.0f, 0.25f), material);
            });
    }

    private void SetupPlateModels(ArrayList plateValues, boolean plateFormat) {
        int plateCount = plateValues.size();
        for (int i = 0; i < plateCount / 2; i++) {
            if (plateFormat) {
                // user selected power lifting plates
                // figure out which plate it was
                // assign a color and size to these plates bases on real-world values

                if ((double) plateValues.get(i) == PowerliftingPlatesValues.twentyFiveKilograms) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Red).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == PowerliftingPlatesValues.twentyKilograms) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Blue).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == PowerliftingPlatesValues.fifteenKilograms) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Yellow).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == PowerliftingPlatesValues.tenKilograms) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Green).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == PowerliftingPlatesValues.fiveKilograms) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.White).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == PowerliftingPlatesValues.twoPointFiveKilograms) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Red).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == PowerliftingPlatesValues.twoKilograms) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Blue).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == PowerliftingPlatesValues.onePointFiveKilograms) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Yellow).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == PowerliftingPlatesValues.oneKilograms) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Green).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == PowerliftingPlatesValues.zeroPointFiveKilograms) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.White).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else {
                    // plate value does not match any one of the power lifting plate values
                    // throw error
                }

            } else {
                // user selected standard commercial plates
                // figure out which plate it was
                // assign a color and size to these plates bases on real-world values

                if ((double) plateValues.get(i) == StandardPlatesValues.fourtyFivePounds) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Red).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });

                } else if ((double) plateValues.get(i) == StandardPlatesValues.thirtyFivePounds) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Blue).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == StandardPlatesValues.twentyFivePounds) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Silver).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == StandardPlatesValues.fifteenPounds) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Yellow).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == StandardPlatesValues.tenPounds) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Green).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == StandardPlatesValues.fivePounds) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Gray).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);

                            });
                } else if ((double) plateValues.get(i) == StandardPlatesValues.twoPointFivePounds) {
                    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.White).thenAccept(
                            material -> {
                                // change color of model renderable based on plate weight
                                ModelRenderable plateRenderable = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                                // adjust global position of plate relative to the bar
                                // when we add a plate to one end of the bar
                                // we also have to add it to the other end to balance it out
                                // add model renderable to the renderables array
                                tempContainer.add(plateRenderable);
                            });
                } else {
                    // plate value does not match any one of the standard plate values
                    // throw error
                }
            }
        }

        plateRenderables = new ModelRenderable[plateCount * 2];
        for (int i = 0; i < plateCount / 2; i++) {
            plateRenderables[plateCount] = (ModelRenderable) tempContainer.get(i);
            plateRenderables[plateCount + 1 + i] = (ModelRenderable) tempContainer.get(i);
        }
    }

    private void CreateBarAndPlatesModel(AnchorNode anchorNode) {
        // place and transform the bar in the scene
        TransformableNode barNode = new TransformableNode(arFragment.getTransformationSystem());
        barNode.setParent(anchorNode);
        barNode.setRenderable(barGripSectionRenderable);
        barNode.select();

        //  place and transform the plates in the scene relative to the bar

    }
}
