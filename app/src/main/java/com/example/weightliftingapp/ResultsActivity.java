package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        double liftResponse = intent.getDoubleExtra(MainActivity.liftResponseAppId, 0);
        int repetitionResponse = intent.getIntExtra(MainActivity.repetitionResponseAppId, 0);

    }
}
