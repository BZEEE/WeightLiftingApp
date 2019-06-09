package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

import static com.example.weightliftingapp.RepMaxCalculatorActivity.*;
import static com.example.weightliftingapp.WilksCalculatorActivity.*;
import static com.example.weightliftingapp.IPFCalculatorActivity.*;



public class ResultsActivity extends AppCompatActivity {
    private String checkFlag;
    private TextView calculatorTitle;
    private TextView calculatorResponse;

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

        // determine which calculator activity started the results activity
        switch (this.checkFlag)  {
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
                break;

            case wilksCalculatorId:
                double bodyWeightResponse = intent.getDoubleExtra(bodyWeightResponseAppId, 0);
                String genderResponse = intent.getStringExtra(genderResponseAppId);
                // run calculator using WilksCalculator
                // make sure to return the convert the value to a String before assigning it to the TextView below
                WilksCalculator wilksCalculator = new WilksCalculator();
                double wilksValue = wilksCalculator.Calculate(bodyWeightResponse, genderResponse);

                // set response value to the TextView of this activity
                this.calculatorTitle.setText(wilksDisplayTitle);
                this.calculatorResponse.setText(String.format(Locale.CANADA, "%1$.3f", wilksValue));
                break;

            case ipfPointCalculatorId:
                double bodyWeightResponse = intent.getDoubleExtra(bodyWeightResponseAppId, 0);
                String genderResponse = intent.getStringExtra(genderResponseAppId);

                ipfPointCalculator ipfPointCalculator = new IPFCalculator();
                double IPFpoints = ipfPointCalculator.Calculate(bodyWeightResponse,genderResponse);
                // set response value to the TextView of this activity
                this.calculatorTitle.setText(IPFDisplayTitle);
                this.calculatorResponse.setText(String.format(Locale.CANADA,"%1$.3f", IPFPoints));
                break;

            default:
                // throw error if calculator doesnt exist
                break;
        }

    }
}
