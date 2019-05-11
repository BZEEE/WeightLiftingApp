package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        double liftResponse = intent.getDoubleExtra(MainActivity.liftResponseAppId, 0);
        int repetitionResponse = intent.getIntExtra(MainActivity.repetitionResponseAppId, 0);

        // run calculaton using RepMaxCalculator
        // ...
        // make sure to return the convert the value to a String before assigning it to the TextView below
        // String oneRepMaxValue = RepMaxCalculator.Calculate().ToString();

        // set response value to the TextView of this activity
        TextView OneRepMaxResult = (TextView) findViewById(R.id.one_rep_max_result_value);
        // OneRepMaxResult.setText(oneRepMaxValue);

    }
}
