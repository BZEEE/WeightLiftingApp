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

        this.CreateUIInputs();
    }

    private void CreateUIInputs() {
        this.goToOneRepMaxCalculatorButton = findViewById(R.id.GoToOneRepMaxCalculatorButton);
        this.goToOneRepMaxCalculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenOneRepMaxCalculatorActivity();
            }
        });

        this.goToWilksCalculatorButton = findViewById(R.id.GoToWilksCalculatorButton);
        this.goToWilksCalculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenWilksCalculatorActivity();
            }
        });

        this.goToIPFCalculatorButton = findViewById(R.id.GoToIPFCalculatorButton);
        this.goToIPFCalculatorButton.setOnClickListener(new View.OnClickListener() {
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
