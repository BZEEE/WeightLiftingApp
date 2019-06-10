package com.example.weightliftingapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.weightliftingapp.IPF.IPFCalculator;
import com.example.weightliftingapp.OneRepMax.RepMaxAlgorithms;
import com.example.weightliftingapp.OneRepMax.RepMaxCalculator;
import com.example.weightliftingapp.Wilks.WilksCalculator;
import com.example.weightliftingapp.Wilks.WilksCalculatorActivity;
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



        // determine which calculator activity started the results activity
        switch (this.checkFlag)  {
            case repMaxCalculatorId:

                // check if device enables ArCore
                DeviceEnablesArCore();

                double liftResponse = intent.getDoubleExtra(liftResponseAppId, 0);
                int repetitionResponse = intent.getIntExtra(repetitionResponseAppId, 0);
                // run calculator using RepMaxCalculator

                // make sure to return the convert the value to a String before assigning it to the TextView below
                RepMaxCalculator repMaxCalculator = new RepMaxCalculator();
                double oneRepMaxValue = repMaxCalculator.Calculate(liftResponse, repetitionResponse, RepMaxAlgorithms.Epley);

                // set response value to the TextView of this activity
                this.calculatorTitle.setText(oneRepMaxDisplayTitle);
                this.calculatorResponse.setText(String.format(Locale.CANADA, "%1$.2f", oneRepMaxValue));
                break;

            case wilksCalculatorId:
                // disable AR since it is not needed for Wilks
                DisableARButton();
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

                double totalIPFResponse = intent.getDoubleExtra(totalResponseIPFAppId, 0);
                double bodyWeightIPFResponse = intent.getDoubleExtra(bodyWeightResponseIPFAppId, 0);
                String genderIPFResponse = intent.getStringExtra(genderResponseIPFAppId);

                IPFCalculator ipfPointCalculator = new IPFCalculator();
                double IPFpoints = ipfPointCalculator.Calculate(totalIPFResponse, bodyWeightIPFResponse, genderIPFResponse);
                // set response value to the TextView of this activity
                this.calculatorTitle.setText(ipfDisplayTitle);
                this.calculatorResponse.setText(String.format(Locale.CANADA,"%1$.3f", IPFpoints));
                break;

            default:
                // throw error if calculator doesnt exist
                break;
        }

    }

    private void DeviceEnablesArCore() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
//        switch (availability) {
//            case
//        }

        if (availability.isTransient()) {
            // Re-query at 5Hz while compatibility is checked in the background.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // recursive call to re-check if the devices support AR capabilities
                    // delay for 200 milliseconds, then re-query the check
                    DeviceEnablesArCore();
                }
            }, 200);
        }
        if (availability.isSupported()) {
            // AR is supported
            EnableARButton();
        } else {
            // Unsupported or unknown.
            DisableARButton();
        }
    }

    private void EnableARButton() {
        augmentedRealityButton.setVisibility(View.VISIBLE);
        augmentedRealityButton.setEnabled(true);
        // set on click listener since the button is visible
        augmentedRealityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToWeightsARView();
            }
        });
    }

    private void DisableARButton() {
        augmentedRealityButton.setVisibility(View.INVISIBLE);
        augmentedRealityButton.setEnabled(false);
    }

    private void GoToWeightsARView() {
        Intent intent = new Intent(this, ARWeightResultsActivity.class);
        startActivity(intent);
    }
}
