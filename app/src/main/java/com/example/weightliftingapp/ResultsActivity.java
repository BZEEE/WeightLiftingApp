package com.example.weightliftingapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weightliftingapp.IPF.IPFCalculator;
import com.example.weightliftingapp.OneRepMax.RepMaxAlgorithms;
import com.example.weightliftingapp.OneRepMax.RepMaxCalculator;
import com.example.weightliftingapp.Wilks.WilksCalculator;
import com.example.weightliftingapp.Wilks.WilksCalculatorActivity;
import com.google.ar.core.ArCoreApk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;

import static com.example.weightliftingapp.IPF.IPFCalculatorActivity.bodyWeightResponseIPFAppId;
import static com.example.weightliftingapp.IPF.IPFCalculatorActivity.genderResponseIPFAppId;
import static com.example.weightliftingapp.IPF.IPFCalculatorActivity.ipfDisplayTitle;
import static com.example.weightliftingapp.IPF.IPFCalculatorActivity.ipfPointCalculatorId;
import static com.example.weightliftingapp.IPF.IPFCalculatorActivity.totalResponseIPFAppId;
import static com.example.weightliftingapp.OneRepMax.RepMaxCalculatorActivity.*;
import static com.example.weightliftingapp.Wilks.WilksCalculatorActivity.*;


public class ResultsActivity extends AppCompatActivity {
    private String checkFlag;
    private TextView calculatorTitle;
    private TextView calculatorResponse;
    private Button augmentedRealityButton;
    private Switch standardVersusPowerliftingSwitch;
    private RelativeLayout standardVersusPowerliftingSwitchLayout;
    public static final String plateFormatResponseId = "com.example.weightliftingapp.ARPlatesFormat";
    public static final String platesResponseId = "com.example.weightliftingapp.ARPlates";

    private double MIN_OPENGL_VERSION = 3.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        Intent intent = getIntent();
        // this flag is referenced from RepMaxCalculatorActivity.java
        this.checkFlag = intent.getStringExtra(flag);

        // get result UI so we can pass calculations to it
        this.calculatorTitle = findViewById(R.id.results_title);
        this.calculatorResponse = findViewById(R.id.results_value);

        // place bar for user through AR, requires API version 24 or higher for ARCore library
        // default Arcore uses sceneform which might suffice for our purposes
        //  Opengl 3.0 is requires by the emulator
        this.augmentedRealityButton = findViewById(R.id.goToARWeightsButton);
        this.standardVersusPowerliftingSwitch = findViewById(R.id.StandardVersusPowerliftingSwitch);
        this.standardVersusPowerliftingSwitchLayout = findViewById(R.id.StandardVersusPowerliftingSwitchLayout);


        // determine which calculator activity started the results activity
        switch (this.checkFlag) {
            case repMaxCalculatorId:

                double liftResponse = intent.getDoubleExtra(liftResponseAppId, 0);
                int repetitionResponse = intent.getIntExtra(repetitionResponseAppId, 0);
                // run calculator using RepMaxCalculator

                // make sure to return the convert the value to a String before assigning it to the TextView below
                RepMaxCalculator repMaxCalculator = new RepMaxCalculator();
                double oneRepMaxValue = repMaxCalculator.Calculate(liftResponse, repetitionResponse, RepMaxAlgorithms.Epley);

                // set response value to the TextView of this activity
                this.calculatorTitle.setText(oneRepMaxDisplayTitle);
                this.calculatorResponse.setText(String.format(Locale.CANADA, "%1$.2f", oneRepMaxValue));

                // check if device enables ArCore
                DeviceEnablesArCore(repMaxCalculator, oneRepMaxValue);

                break;

            case wilksCalculatorId:
                // disable AR since it is not needed for Wilks
                DisableARButton();
                DisablePlatesSwitchLayout();

                double bodyWeightWilksResponse = intent.getDoubleExtra(bodyWeightResponseWilksAppId, 0);
                String genderWilksResponse = intent.getStringExtra(genderResponseWilksAppId);
                // run calculator using WilksCalculator
                // make sure to return the convert the value to a String before assigning it to the TextView below
                WilksCalculator wilksCalculator = new WilksCalculator();
                double wilksValue = wilksCalculator.Calculate(bodyWeightWilksResponse, genderWilksResponse);

                // set response value to the TextView of this activity
                this.calculatorTitle.setText(wilksDisplayTitle);
                this.calculatorResponse.setText(String.format(Locale.CANADA, "%1$.3f", wilksValue));
                break;

            case ipfPointCalculatorId:
                // disable AR since it is not needed for IPF
                DisableARButton();
                DisablePlatesSwitchLayout();

                double totalIPFResponse = intent.getDoubleExtra(totalResponseIPFAppId, 0);
                double bodyWeightIPFResponse = intent.getDoubleExtra(bodyWeightResponseIPFAppId, 0);
                String genderIPFResponse = intent.getStringExtra(genderResponseIPFAppId);

                IPFCalculator ipfPointCalculator = new IPFCalculator();
                double IPFpoints = ipfPointCalculator.Calculate(totalIPFResponse, bodyWeightIPFResponse, genderIPFResponse);
                // set response value to the TextView of this activity
                this.calculatorTitle.setText(ipfDisplayTitle);
                this.calculatorResponse.setText(String.format(Locale.CANADA, "%1$.3f", IPFpoints));
                break;

            default:
                // throw error if calculator doesnt exist
                break;
        }

    }

    //use plate functions to output correct images after using the greedy algorithm
    private void display2dGraphics(double oneRepMaxValue){
        // call the ischecked function for powerlifting or standard plates
        if (standardVersusPowerliftingSwitch.isChecked()) {
        //            // user wants power lifting plates
            double[] weight = RepMaxCalculator.GetPlatesFromOneRepMax(oneRepMaxValue, true);
            int  left = weight.length/2 - 1;
            int right = weight.length/2;
            for (int i = 0; i < weight.length/2; i++) {
                if (i == 0){
                    // set canvas and draw the barbell image on that canvas (5 rectangles, displayed adjacent to each other
                    // create
                    private Canvas mCanvas;
                    private Paint mPaint = new Paint();
                    private Bitmap mBitmap;
                    private ImageView mImageView;

                    //display barbell graphic on canvas


                }

                double graphic_display_left = weight[left];
                double graphic_display_right = weight[right];
                //after i > 1, start displaying images relative to each plate.

                // display android images through here
                // a bunch

                ImageView image = findViewById(R.id.empty_barbell_graphic);
                if (graphic_display_left == 25 && graphic_display_right == 25) {
                    // draw the 25 kg plate with canvas
                    Rect twentyfivekg = new Rect();
                    twentyfivekg.set(0,0,0,0);
                    // canvas not initiated
                }
                else if (graphic_display_left == 20 && graphic_display_right == 20){
                    // draw the 20 kg plate using canvas
                    Rect twentykg = new Rect();
                    twentykg.set(0,0,0,0);

                }
                else if (graphic_display_left == 15 && graphic_display_right == 15){
                    // draw the 15 kg plate using canvas
                    Rect fifteenkg = new Rect();
                    fifteenkg.set(0,0,0,0);
                }
                else if (graphic_display_left == 10 && graphic_display_right == 10){
                    // draw the 10 kg plate using canvas
                    Rect tenkg = new Rect();
                    tenkg.set(0,0,0,0);
                }
                else if (graphic_display_left == 5 && graphic_display_right == 5){
                    // draw the 5 kg plate using canvas
                    Rect fivekg = new Rect();
                    fivekg.set(0,0,0,0);
                }
                else if (graphic_display_left == 2.5 && graphic_display_right == 2.5) {
                    // draw the 2.5 kg plate using canvas
                    Rect twohalfkg = new Rect();
                    twohalfkg.set(0,0,0,0);
                }
                else if (graphic_display_left == 1.25 && graphic_display_right == 1.25){
                    // draw the 1.25 kg plate using canvas on the corresponding screen part
                    Rect onequarterkg = new Rect();
                    onequarterkg.set(0,0,0,0);

                }


                right = right + 1;
                left = left + 1;
            }
            // dynamically display images in java


        }
        else {

        }
        //        }
        // add once acquiring the array, display corresponding images
    }

    private void DeviceEnablesArCore(RepMaxCalculator repMaxCalculator, double oneRepMaxValue) {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if (availability.isTransient()) {
            // Re-query at 5Hz while compatibility is checked in the background.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // recursive call to re-check if the devices support AR capabilities
                    // delay for 200 milliseconds, then re-query the check
                    DeviceEnablesArCore(repMaxCalculator, oneRepMaxValue);
                }
            }, 200);
        }

        if (availability.isSupported() && CheckIsSupportedDeviceOrFinish(this)) {
            // AR is supported
            EnablePlatesSwitchLayout();
            EnableARButton(repMaxCalculator, oneRepMaxValue);
        } else {
            // Unsupported or unknown.
            DisablePlatesSwitchLayout();
            DisableARButton();
        }
    }

    public boolean CheckIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString = ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        return true;
    }

    private void EnableARButton(RepMaxCalculator repMaxCalculator, double oneRepMaxValue) {
        augmentedRealityButton.setVisibility(View.VISIBLE);
        augmentedRealityButton.setEnabled(true);
        // set on click listener since the button is visible
        augmentedRealityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToWeightsARView(repMaxCalculator, oneRepMaxValue);
            }
        });
    }

    private void DisableARButton() {
        augmentedRealityButton.setVisibility(View.INVISIBLE);
        augmentedRealityButton.setEnabled(false);
    }

    private void EnablePlatesSwitchLayout() {
        standardVersusPowerliftingSwitchLayout.setVisibility(View.VISIBLE);
        standardVersusPowerliftingSwitchLayout.setEnabled(true);
    }

    private void DisablePlatesSwitchLayout() {
        standardVersusPowerliftingSwitchLayout.setVisibility(View.INVISIBLE);
        standardVersusPowerliftingSwitchLayout.setEnabled(false);
    }

    private void GoToWeightsARView(RepMaxCalculator repMaxCalculator, double oneRepMaxValue) {
        Intent intent = new Intent(this, ARWeightResultsActivity.class);

        if (standardVersusPowerliftingSwitch.isChecked()) {
            // user wants power lifting plates
            intent.putExtra(plateFormatResponseId, true);
            double[] plates = repMaxCalculator.GetPlatesFromOneRepMax(oneRepMaxValue, true);
            intent.putExtra(platesResponseId, plates);
        } else {
            // user wants standard plates
            intent.putExtra(plateFormatResponseId, false);
            double[] plates = repMaxCalculator.GetPlatesFromOneRepMax(oneRepMaxValue, false);
            intent.putExtra(platesResponseId, plates);
        }
        startActivity(intent);
    }
}
