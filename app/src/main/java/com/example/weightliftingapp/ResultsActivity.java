package com.example.weightliftingapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weightliftingapp.IPF.IPFCalculator;
import com.example.weightliftingapp.OneRepMax.RepMaxAlgorithms;
import com.example.weightliftingapp.OneRepMax.RepMaxCalculator;
import com.example.weightliftingapp.Wilks.WilksCalculator;
import com.google.ar.core.ArCoreApk;

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
            // barbell drawn to Ohio Powerbar Specs.
            // this entire section should be done before the loop is made.
            private Canvas mCanvas;
            private Paint mPaint = new Paint();
            private Bitmap mBitmap;
            private ImageView mImageView;

            private int black;
            private int barbellcolor;
            private int collarstopcolor;
            private int collarcolor;

            //display barbell graphic on canvas
            private Rect barbell = new Rect();
            private Rect collarstop = new Rect();
            private Rect collar = new Rect();

            black = ResourcesCompat.getColor(getResources(),R.color.black,null);
            barbellcolor = ResourcesCompat.getColor(getResources(),R.color.barbellgray,null);
            collarstopcolor = ResourcesCompat.getColor(getResources(),R.color.barbellgray,null);
            collarcolor = ResourcesCompat.getColor(getResources(),R.color.barbellgray,null);

            mPaint.setColor(barbellcolor);

            mImageView = (ImageView) findViewById(R.id.render_weight);
            //draw the barbell through java
            int vWidth = mImageView.getWidth();
            int vHeight = mImageView.getHeight();
            //create the bitmap to map the canvas to then draw on
            mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
            mImageView.setImageBitmap(mBitmap);
            mCanvas = new Canvas(mBitmap);

            //set the background color of the canvas to black
            mCanvas.drawColor(black);
            //center the midsection of the barbell in the center of the view
            mCanvas.drawRect(88,123,312,127,mPaint);
            //draw the collarstops

            //draw left sleevestop
            mCanvas.drawRect(82,116,87,134,mPaint);
            //draw right sleevestop
            mCanvas.drawRect(313,116,318,134,mPaint);

            //draw the collars
            //draw the left sleeve
            mCanvas.drawRect(10,121,81,129,mPaint);
            //draw the right sleeve
            mCanvas.drawRect(319,121,390,129,mPaint);
            //create a counter that initializes at collarstop left and right, and decrements/increments at the width of each weight.
            int counterleft = 81;
            int counterright = 319;

            for (int i = 0; i < weight.length/2; i++) {
                //initiate a "left" tracker that increments by the plate width. for every increment
                double graphic_display_left = weight[left];
                double graphic_display_right = weight[right];
                //after i > 1, start displaying images relative to each plate.
                if (graphic_display_left == 25 && graphic_display_right == 25) {
                    // draw the 25 kg plate with canvas
                    private Rect twentyfiveleft;
                    private Rect twentyfiveright;

                    private int red;
                    red = ResourcesCompat.getColor(getResources(),R.color.powerliftingred,null);

                    mPaint.setColor(red);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-4,87,counterleft,163,2,2,mPaint);
                    counterleft = counterleft - 5;
                    // canvas not initiated
                    mCanvas.drawRoundRect(counterright,87,counterright+4,163,2,2,mPaint);
                    counterright = counterright+5;
                }
                else if (graphic_display_left == 20 && graphic_display_right == 20){
                    // draw the 20 kg plate using canvas
                    private Rect twentyleft;
                    private Rect twentyright;

                    private int blue;

                    blue = ResourcesCompat.getColor(getResources(),R.color.powerliftingblue,null);

                    mPaint.setColor(blue);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-3,87,counterleft,163,2,2,mPaint);
                    counterleft = counterleft-4;

                    //draw the right plate
                    mCanvas.drawRoundRect(counterright,87,counterright+3,163,2,2,mPaint);
                    counterright = counterright + 4;

                }
                else if (graphic_display_left == 15 && graphic_display_right == 15){
                    // draw the 15 kg plate using canvas
                    private Rect fifteenleft;
                    private Rect fifteenright;

                    private int yellow;

                    yellow = ResourcesCompat.getColor(getResources(),R.color.powerliftingyellow,null);

                    mPaint.setColor(yellow);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-3,91,counterleft,159,2,2,mPaint);
                    counterleft = counterleft-4;

                    //draw the right plate
                    mCanvas.drawRoundRect(counterright,91,counterright+3,159,2,2,mPaint);
                    counterright = counterright + 4;
                }
                else if (graphic_display_left == 10 && graphic_display_right == 10){
                    // draw the 10 kg plate using canvas
                    private Rect tenleft;
                    private Rect tenright;

                    private int green;

                    green = ResourcesCompat.getColor(getResources(),R.color.powerliftinggreen,null);

                    mPaint.setColor(green);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-3,97,counterleft,153,2,2,mPaint);
                    counterleft = counterleft-4;

                    //draw the right plate
                    mCanvas.drawRoundRect(counterright,97,counterright+3,153,2,2,mPaint);
                    counterright = counterright + 4;
                }
                else if (graphic_display_left == 5 && graphic_display_right == 5){
                    // draw the 5 kg plate using canvas
                    private Rect fiveleft;
                    private Rect fiveright;

                    private int white;

                    white = ResourcesCompat.getColor(getResources(),R.color.powerliftingfivegray,null);

                    mPaint.setColor(white);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-4,106,counterleft,144,2,2,mPaint);
                    counterleft = counterleft-5;

                    //draw the right plate
                    mCanvas.drawRoundRect(counterright,106,counterright+4,144,2,2,mPaint);
                    counterright = counterright + 5;

                }
                else if (graphic_display_left == 2.5 && graphic_display_right == 2.5) {
                    // draw the 2.5 kg plate using canvas
                    private Rect twohalfleft;
                    private Rect twohalfright;

                    private int gray;

                    gray = ResourcesCompat.getColor(getResources(),R.color.powerliftingtwohalfgray,null);

                    mPaint.setColor(gray);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-2,109,counterleft,141,2,2,mPaint);
                    counterleft = counterleft-3;

                    //draw the right plate
                    mCanvas.drawRoundRect(counterright,109,counterright+2,141,2,2,mPaint);
                    counterright = counterright + 3;
                }
                else if (graphic_display_left == 1.25 && graphic_display_right == 1.25){
                    // draw the 1.25 kg plate using canvas on the corresponding screen part
                    private Rect onequarterleft;
                    private Rect onequarterright;

                    private int gray;

                    gray = ResourcesCompat.getColor(getResources(),R.color.powerliftingonequartergray,null);

                    mPaint.setColor(gray);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRect(counterleft-2,112,counterleft,138,mPaint);
                    counterleft = counterleft-3;

                    //draw the right plate
                    mCanvas.drawRect(counterright,112,counterright+2,138,mPaint);
                    counterright = counterright + 3;

                }


                right = right + 1;
                left = left - 1;
            }
            // dynamically display images in java


        }
        else {
            // the user prompts standard plates
            double[] weight = RepMaxCalculator.GetPlatesFromOneRepMax(oneRepMaxValue, false);
            int  left = weight.length/2 - 1;
            int right = weight.length/2;
            // barbell drawn to Ohio Powerbar Specs.
            // this entire section should be done before the loop is made.
            private Canvas mCanvas;
            private Paint mPaint = new Paint();
            private Bitmap mBitmap;
            private ImageView mImageView;

            private int black;
            private int barbellcolor;
            private int collarstopcolor;
            private int collarcolor;

            //display barbell graphic on canvas
            private Rect barbell = new Rect();
            private Rect collarstop = new Rect();
            private Rect collar = new Rect();

            black = ResourcesCompat.getColor(getResources(),R.color.black,null);
            barbellcolor = ResourcesCompat.getColor(getResources(),R.color.barbellgray,null);
            collarstopcolor = ResourcesCompat.getColor(getResources(),R.color.barbellgray,null);
            collarcolor = ResourcesCompat.getColor(getResources(),R.color.barbellgray,null);

            mPaint.setColor(barbellcolor);

            mImageView = (ImageView) findViewById(R.id.render_weight);
            //draw the barbell through java
            int vWidth = mImageView.getWidth();
            int vHeight = mImageView.getHeight();
            //create the bitmap to map the canvas to then draw on
            mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
            mImageView.setImageBitmap(mBitmap);
            mCanvas = new Canvas(mBitmap);

            //set the background color of the canvas to black
            mCanvas.drawColor(black);
            //center the midsection of the barbell in the center of the view
            mCanvas.drawRect(88,123,312,127,mPaint);
            //draw the collarstops

            //draw left sleevestop
            mCanvas.drawRect(82,116,87,134,mPaint);
            //draw right sleevestop
            mCanvas.drawRect(313,116,318,134,mPaint);

            //draw the collars
            //draw the left sleeve
            mCanvas.drawRect(10,121,81,129,mPaint);
            //draw the right sleeve
            mCanvas.drawRect(319,121,390,129,mPaint);
            int counterleft = 81;
            int counterright = 319;
            for (int i = 0; i < weight.length/2; i++) {
                //initiate a "left" tracker that increments by the plate width. for every increment
                double graphic_display_left = weight[left];
                double graphic_display_right = weight[right];
                //after i > 1, start displaying images relative to each plate.
                if (graphic_display_left == 20 && graphic_display_right == 20) {
                    // draw the 45 lb plate with canvas
                    private Rect fortyfiveleft;
                    private Rect fortyfiveright;

                    private int gray;
                    gray = ResourcesCompat.getColor(getResources(),R.color.fadedGrey,null);

                    mPaint.setColor(gray);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-8,87,counterleft,163,2,2,mPaint);
                    counterleft = counterleft - 9;
                    // canvas not initiated
                    mCanvas.drawRoundRect(counterright,87,counterright+8,163,2,2,mPaint);
                    counterright = counterright+9;
                }
                /*else if (graphic_display_left == 20 && graphic_display_right == 20){
                    // draw the 35 lb plate using canvas
                    private Rect twentyleft;
                    private Rect twentyright;

                    private int blue;

                    blue = ResourcesCompat.getColor(getResources(),R.color.powerliftingblue,null);

                    mPaint.setColor(blue);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-3,87,counterleft,163,2,2,mPaint);
                    counterleft = counterleft-4;

                    //draw the right plate
                    mCanvas.drawRoundRect(counterright,87,counterright+3,163,2,2,mPaint);
                    counterright = counterright + 4;

                }*/
                else if (graphic_display_left == 15 && graphic_display_right == 15){
                    // draw the 35 lb plate using canvas
                    private Rect fifteenleft;
                    private Rect fifteenright;

                    private int gray;

                    gray = ResourcesCompat.getColor(getResources(),R.color.fadedGrey,null);

                    mPaint.setColor(gray);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-7,91,counterleft,159,2,2,mPaint);
                    counterleft = counterleft-8;

                    //draw the right plate
                    mCanvas.drawRoundRect(counterright,91,counterright+7,159,2,2,mPaint);
                    counterright = counterright + 8;
                }
                else if (graphic_display_left == 10 && graphic_display_right == 10){
                    // draw the 25 lb plate using canvas
                    private Rect tenleft;
                    private Rect tenright;

                    private int gray;

                    gray = ResourcesCompat.getColor(getResources(),R.color.fadedGrey,null);

                    mPaint.setColor(gray);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-5,97,counterleft,153,2,2,mPaint);
                    counterleft = counterleft-6;

                    //draw the right plate
                    mCanvas.drawRoundRect(counterright,97,counterright+5,153,2,2,mPaint);
                    counterright = counterright + 6;
                }
                else if (graphic_display_left == 5 && graphic_display_right == 5){
                    // draw the 10 lb plate using canvas
                    private Rect fiveleft;
                    private Rect fiveright;

                    private int gray;

                    gray = ResourcesCompat.getColor(getResources(),R.color.fadedGrey,null);

                    mPaint.setColor(gray);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-4,106,counterleft,144,2,2,mPaint);
                    counterleft = counterleft-5;

                    //draw the right plate
                    mCanvas.drawRoundRect(counterright,106,counterright+4,144,2,2,mPaint);
                    counterright = counterright + 5;

                }
                else if (graphic_display_left == 2.5 && graphic_display_right == 2.5) {
                    // draw the 5 lb plate using canvas
                    private Rect twohalfleft;
                    private Rect twohalfright;

                    private int gray;

                    gray = ResourcesCompat.getColor(getResources(),R.color.fadedGrey,null);

                    mPaint.setColor(gray);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRoundRect(counterleft-2,109,counterleft,141,2,2,mPaint);
                    counterleft = counterleft-3;

                    //draw the right plate
                    mCanvas.drawRoundRect(counterright,109,counterright+2,141,2,2,mPaint);
                    counterright = counterright + 3;
                }
                else if (graphic_display_left == 1.25 && graphic_display_right == 1.25){
                    // draw the 2.5 lb plate using canvas on the corresponding screen part
                    private Rect twohalfleft;
                    private Rect twohalfright;

                    private int gray;

                    gray = ResourcesCompat.getColor(getResources(),R.color.fadedGrey,null);

                    mPaint.setColor(gray);

                    mImageView = (ImageView) findViewById(R.id.render_weight);

                    //draw the left plate
                    mCanvas.drawRect(counterleft-2,112,counterleft,138,mPaint);
                    counterleft = counterleft-3;

                    //draw the right plate
                    mCanvas.drawRect(counterright,112,counterright+2,138,mPaint);
                    counterright = counterright + 3;

                }


                right = right + 1;
                left = left - 1;
            }

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
