package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CalculatorSelectionActivity extends AppCompatActivity {

    private Button goToOneRepMaxCalculatorButton;
    private Button goToWilksCalculatorButton;
    private Button goToIPFCalculatorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_selection);

        CreateUIInputs();
    }

    private void CreateUIInputs() {
        goToOneRepMaxCalculatorButton = findViewById(R.id.GoToOneRepMaxCalculatorButton);
        goToOneRepMaxCalculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenOneRepMaxCalculatorActivity();
            }
        });

        goToWilksCalculatorButton = findViewById(R.id.GoToWilksCalculatorButton);
        goToWilksCalculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenWilksCalculatorActivity();
            }
        });

        goToIPFCalculatorButton = findViewById(R.id.GoToIPFCalculatorButton);
        goToIPFCalculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenIPFCalculatorActivity();
            }
        });
    }

    private void OpenOneRepMaxCalculatorActivity() {
        Intent intent = new Intent(this, RepMaxCalculatorActivity.class);
        startActivity(intent);
    }

    private void OpenWilksCalculatorActivity() {
        Intent intent = new Intent(this, WilksCalculatorActivity.class);
        startActivity(intent);
    }

    private void OpenIPFCalculatorActivity() {
        Intent intent = new Intent(this, IPFCalculatorActivity.class);
        startActivity(intent);
    }
}
