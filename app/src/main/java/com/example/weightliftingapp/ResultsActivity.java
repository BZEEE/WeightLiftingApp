package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

import static com.example.weightliftingapp.RepMaxCalculator.*;
import static com.example.weightliftingapp.RepMaxAlgorithms.*;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        double liftResponse = intent.getDoubleExtra(MainActivity.liftResponseAppId, 0);
        int repetitionResponse = intent.getIntExtra(MainActivity.repetitionResponseAppId, 0);

        // run calculator using RepMaxCalculator
        // ...
        // make sure to return the convert the value to a String before assigning it to the TextView below
        double oneRepMaxValue = RepMaxCalculator.OneRepMaxCalculation(liftResponse, repetitionResponse, RepMaxAlgorithms.Epley);

        // set response value to the TextView of this activity
        TextView OneRepMaxResult = findViewById(R.id.one_rep_max_result_value);
        OneRepMaxResult.setText(String.format(Locale.CANADA, "%1$.1f", oneRepMaxValue));


    }
}
