package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.weightliftingapp.OneRepMax.PowerliftingPlatesValues;
import com.example.weightliftingapp.OneRepMax.StandardPlatesValues;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Quaternion;
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

    private float bufferPlateHeight = 0.005f;

    private ModelRenderable barGripSectionRenderable;
    private ModelRenderable barLeftSectionRenderable;
    private ModelRenderable barRightSectionRenderable;

    // renderable are the basic primitives and attributes that specify that object, so basically the features
    private ModelRenderable bufferPlateTemplate;
    private ModelRenderable modelRenderableTemplate;
    private ModelRenderable[] plateRenderables;

    // objects that are placed in the scene have to be attached to nodes
    private TransformableNode[] barNodes;
    private TransformableNode[] plateNodes;

    private static final String TAG = "AR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Toast.makeText(this, "we running", Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_arweight_results);

        // create template on a seperate thread and use it later on
        // and make copies of it, which will then be added to the scene
        MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Black)
                .thenAccept(
                        material -> {
                            ModelRenderable modelRenderableTemplate = ShapeFactory.makeCylinder(plateRadius, bufferPlateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                            MakeBufferPlateTemplateAvailable(modelRenderableTemplate);
                        });

        MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Red)
                .thenAccept(
                        material -> {
                            ModelRenderable modelRenderableTemplate = ShapeFactory.makeCylinder(plateRadius, plateHeight,  new Vector3(0.0f, 0.0f, 0.0f), material);
                            MakeModelRenderableTemplateAvailable(modelRenderableTemplate);

                            Intent intent = getIntent();
                            // get plate list from intent
                            double[] plateValues = intent.getDoubleArrayExtra(platesResponseId);
                            boolean plateFormat = intent.getBooleanExtra(plateFormatResponseId, false);

                            // initialize plate models once the template has been created so we do not
                            // run into concurrency issues
                            SetupPlateModels(plateValues, plateFormat);
                        });

        // initialize the barbell on a seperate thread
        MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Silver)
                .thenAccept(
                        material -> {
                            ModelRenderable barLeft = ShapeFactory.makeCylinder(0.04f, 0.445f,  new Vector3(0f, 0f, 0f), material);
                            SetupLeftSectionBarbell(barLeft);
                        });

        MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Silver)
                .thenAccept(
                        material -> {
                            ModelRenderable barRight = ShapeFactory.makeCylinder(0.04f, 0.445f,  new Vector3(0f, 0f, 0f), material);
                            SetupRightSectionBarbell(barRight);
                        });

        MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Silver)
                .thenAccept(
                        material -> {
                            ModelRenderable barGrip = ShapeFactory.makeCylinder(0.014f, 1.31f,  new Vector3(0f, 0f, 0f), material);
                            SetupGripSectionBarbell(barGrip);
                        });

        // get plates from One Rep max Activity, should be a list
        // that is a multiple of 2 for size [10,20,40,40,20,10]


        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.AR_Fragment);

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                // add bar and plates primitives to the scene
                // place the models in the scene according to an anchor point
                // that the user created via a touch gesture on the AR screen
                // render themin the scene
                CreateBarAndPlatesModel(anchorNode);
            }
        });
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
                        Toast.makeText(this, "Homie", Toast.LENGTH_LONG)
                                .show();
                        mUserRequestedInstall = false;
                        return;
                    default:
                        Toast.makeText(this, "Broooo", Toast.LENGTH_LONG)
                                .show();
                        break;

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

    private void MakeBufferPlateTemplateAvailable(ModelRenderable bufferTemplate) { bufferPlateTemplate = bufferTemplate; }

    private void MakeModelRenderableTemplateAvailable(ModelRenderable template) { modelRenderableTemplate = template; }

    private void SetupLeftSectionBarbell(ModelRenderable bar) {
        barLeftSectionRenderable = bar;
    }

    private void SetupGripSectionBarbell(ModelRenderable bar) {
        barGripSectionRenderable = bar;
    }

    private void SetupRightSectionBarbell(ModelRenderable bar) {
        barRightSectionRenderable = bar;
    }

    private void SetupPlateModels(double[] plateValues, boolean plateFormat) {
        plateRenderables = new ModelRenderable[plateValues.length];
        for (int i = 0; i < plateValues.length; i++) {
            if (plateFormat) {
                // user selected power lifting plates
                // figure out which plate it was
                // assign a color and size to these plates bases on real-world values

                if (plateValues[i] == PowerliftingPlatesValues.twentyFiveKilograms) {

                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Red);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == PowerliftingPlatesValues.twentyKilograms) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Blue);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == PowerliftingPlatesValues.fifteenKilograms) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Yellow);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == PowerliftingPlatesValues.tenKilograms) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Green);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == PowerliftingPlatesValues.fiveKilograms) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.White);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == PowerliftingPlatesValues.twoPointFiveKilograms) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Red);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == PowerliftingPlatesValues.twoKilograms) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Blue);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == PowerliftingPlatesValues.onePointFiveKilograms) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Yellow);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == PowerliftingPlatesValues.oneKilograms) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Green);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == PowerliftingPlatesValues.zeroPointFiveKilograms) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.White);
                    plateRenderables[i] = plateRenderable;

                } else {
                    // plate value does not match any one of the power lifting plate values
                    // throw error
                }

            } else {
                // user selected standard commercial plates
                // figure out which plate it was
                // assign a color and size to these plates bases on real-world values
                if (plateValues[i] == StandardPlatesValues.fourtyFivePounds) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Red);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == StandardPlatesValues.thirtyFivePounds) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Blue);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == StandardPlatesValues.twentyFivePounds) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Silver);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == StandardPlatesValues.fifteenPounds) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Yellow);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == StandardPlatesValues.tenPounds) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Green);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == StandardPlatesValues.fivePounds) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.Gray);
                    plateRenderables[i] = plateRenderable;

                } else if (plateValues[i] == StandardPlatesValues.twoPointFivePounds) {
                    ModelRenderable plateRenderable = modelRenderableTemplate.makeCopy();
                    plateRenderable.getMaterial().setFloat3(MaterialProperties.Color, PlateColorPalette.White);
                    plateRenderables[i] = plateRenderable;

                } else {
                    // plate value does not match any one of the standard plate values
                    // throw error
                }
            }

        }

    }

    private void CreateBarAndPlatesModel(AnchorNode anchorNode) {
        // to place renderable in the scene we attach them to a node
        // which is responsible for their transformations within the 3D space

        // Pose anchorPose = anchorNode.getAnchor().getPose();

        ModelRenderable[] barRenderables = {barLeftSectionRenderable, barGripSectionRenderable, barRightSectionRenderable};

        Log.d(TAG, "CreateBarAndPlateModelsWorks() works");
        barNodes = new TransformableNode[barRenderables.length];
        for (int i = 0; i < barRenderables.length; i++) {
            // place the bar renderable in the scene
            TransformableNode barNode = new TransformableNode(arFragment.getTransformationSystem());
            barNode.setParent(anchorNode);
            barNode.setRenderable(barRenderables[i]);
            barNode.select();
            // keep track of plateNode
            barNodes[i] = barNode;
        }

        barNodes[0].setLocalRotation(Quaternion.axisAngle(new Vector3(0, 0, 1), 90));
        barNodes[0].setLocalPosition(new Vector3(-0.8775f, plateRadius, 0f));
        barNodes[1].setLocalRotation(Quaternion.axisAngle(new Vector3(0, 0, 1), 90));
        barNodes[1].setLocalPosition(new Vector3(0f, plateRadius, 0f));
        barNodes[2].setLocalRotation(Quaternion.axisAngle(new Vector3(0, 0, 1), 90));
        barNodes[2].setLocalPosition(new Vector3(0.8775f, plateRadius, 0f));

        // number of total plates
        plateNodes = new TransformableNode[plateRenderables.length];
        float currentPosLeft = 0;
        float currentPosRight = 0;
        for (int i = 0; i < plateRenderables.length; i++) {
            TransformableNode plateNode = new TransformableNode(arFragment.getTransformationSystem());
            plateNode.setParent(anchorNode);
            plateNode.setRenderable(plateRenderables[i]);
            plateNode.select();

//            if (i != 0 && i != plateRenderables.length - 1) {
//                TransformableNode bufferPlateNode = new TransformableNode(arFragment.getTransformationSystem());
//                bufferPlateNode.setParent(anchorNode);
//                ModelRenderable bufferPlateRenderable = bufferPlateTemplate.makeCopy();
//                bufferPlateNode.setRenderable(bufferPlateRenderable);
//                bufferPlateNode.select();
//            }


            int halfwayPoint = plateNodes.length / 2;
            // set world position of node
            // make sure to apply rotation and scaling before any translation operations
            float offsetFromCenterOfBar;
            if (i < halfwayPoint) {
                // half of the grip bar is 0.655f
                offsetFromCenterOfBar = (0.01f + 0.655f + (((plateNodes.length / 2) - i - 0.5f) * plateHeight)) * -1;
                plateNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 0, 1), 90));
                plateNode.setLocalPosition(new Vector3( offsetFromCenterOfBar, plateRadius, 0f));

//                // set buffer plate
//                plateNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 0, 1), 90));
//                plateNode.setLocalPosition(new Vector3( offsetFromCenterOfBar, plateRadius, 0f));
            } else {
                offsetFromCenterOfBar = 0.01f + 0.655f + ((i - (plateNodes.length / 2) + 0.5f) * plateHeight);
                plateNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 0, 1), 90));
                plateNode.setLocalPosition(new Vector3( offsetFromCenterOfBar, plateRadius, 0f));
            }

            // keep track of plateNode
            plateNodes[i] = plateNode;
        }


    }
}
