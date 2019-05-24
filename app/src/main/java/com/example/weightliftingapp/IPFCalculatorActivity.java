package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IPFCalculatorActivity extends AppCompatActivity {

    private Button calculateButton;
    private static final String flag = "com.example.weightliftingapp.calculatorFlag";
    public static final String ipfPointCalculatorId = "ipfPointCalculatorActivity";
    public static final String ipfDisplayTitle = "IPF Calculation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipfcalculator);

        CreateUIInputs();
    }

    private void CreateUIInputs() {
        this.calculateButton = findViewById(R.id.IPFCalculateButton);
        this.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenResultsActivity();
            }
        });
    }

    private void OpenResultsActivity() {
        Intent intent = new Intent(this, ResultsActivity.class);
        // create a flag to help the results activity determine which calculator opened it
        intent.putExtra(flag, ipfPointCalculatorId);
        startActivity(intent);
    }
}
