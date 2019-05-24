package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

import static com.example.weightliftingapp.RepMaxCalculatorActivity.*;
import static com.example.weightliftingapp.WilksCalculatorActivity.*;



public class ResultsActivity extends AppCompatActivity {
    private static final String calculatorFlagId = "com.example.weightliftingapp.calculatorFlag";
    private String checkFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);



        Intent intent = getIntent();
        // this flag is referenced from RepMaxCalculatorActivity.java
        this.checkFlag = intent.getStringExtra(flag);

        switch (this.checkFlag)  {
            case repMaxCalculatorId:
                double liftResponse = intent.getDoubleExtra(MainActivity.liftResponseAppId, 0);
                int repetitionResponse = intent.getIntExtra(MainActivity.repetitionResponseAppId, 0);
                // run calculator using RepMaxCalculator
                // ...
                // make sure to return the convert the value to a String before assigning it to the TextView below
                RepMaxCalculator calculator = new RepMaxCalculator();
                double oneRepMaxValue = calculator.Calculate(liftResponse, repetitionResponse, RepMaxAlgorithms.Epley);
                // set response value to the TextView of this activity
                TextView result = findViewById(R.id.results_value);
                result.setText(String.format(Locale.CANADA, "%1$.1f", oneRepMaxValue));
                break;
            case wilksCalculatorId:
                break;
//            case ipfPointCalculatorId:
//                break;
            default:
                // throw error if calculator doesnt exist
                break;


        }

    }
}
