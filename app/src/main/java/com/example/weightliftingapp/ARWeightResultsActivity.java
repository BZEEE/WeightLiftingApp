package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class ARWeightResultsActivity extends AppCompatActivity {
    // Set to true ensures requestInstall() triggers installation if necessary.
    private boolean mUserRequestedInstall = true;
    private Session mSession;

    private ModelRenderable barRenderable;
    private ArFragment arFragment;

    private ModelRenderable[] plateRenderables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arweight_results);

        // get plates from One Rep max Activity, should be a list
        // that is a multiple of 2 for size [10,20,40,40,20,10]
        Intent intent = getIntent();
        // get plate list from intent
        int[] plateValues = intent.getIntArrayExtra(platesResponseId);


        SetupBarModel();
        SetupPlateModels(plateValues);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.weights_AR_fragment);

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                // add bar and plates primitives to the scene
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
    MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Red)
        .thenAccept(
            material -> {
                barRenderable = ShapeFactory.makeCylinder(0.1f, 2.0f,  new Vector3(0.0f, 0.15f, 0.0f), material); });
    }

    private void SetupPlateModels(int[] plateValues) {
        for (int i = 0; i < plateValues.length / 2; i++) {
            MaterialFactory.makeOpaqueWithColor(this, PlateColorPalette.Red).thenAccept(
                    material -> {
                        // change color of model renderable based on plate weight
                        ModelRenderable plateRenderable = ShapeFactory.makeCylinder(0.1f, 2.0f,  new Vector3(0.0f, 0.15f, 0.0f), material);
                        // adjust global position of plate relative to the bar
                    });
        }
    }

    private void CreateBarAndPlatesModel(AnchorNode anchorNode) {
        TransformableNode barNode = new TransformableNode(arFragment.getTransformationSystem());
        barNode.setParent(anchorNode);
        barNode.setRenderable(barRenderable);
        barNode.select();
    }
}
