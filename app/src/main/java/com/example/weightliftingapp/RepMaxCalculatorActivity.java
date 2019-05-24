package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RepMaxCalculatorActivity extends AppCompatActivity {

    private Button calculateButton;
    private EditText liftInputBox;
    private EditText repetitionsInputBox;
    public static final String liftResponseAppId = "com.example.weightliftingapp.liftResponse";
    public static final String repetitionResponseAppId = "com.example.weightliftingapp.repetitionResponse";
    public static final String flag = "com.example.weightliftingapp.calculatorFlag";
    public static final String repMaxCalculatorId = "RepMaxCalculatorActivity";
    public static final String oneRepMaxDisplayTitle = "One Rep Max";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_max_calculator);

        CreateUIInputs();
    }

    private void CreateUIInputs() {
        this.calculateButton = findViewById(R.id.OneRepMaxCalculateButton);
        this.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenResultsActivity();
            }
        });
    }

    private void OpenResultsActivity() {
        this.liftInputBox = findViewById(R.id.LiftInputBox);
        this.repetitionsInputBox = findViewById(R.id.RepetitionsInputBox);
        double liftResponse = Double.parseDouble(this.liftInputBox.getText().toString());
        int repetitionResponse = Integer.parseInt(this.repetitionsInputBox.getText().toString());


        Intent intent = new Intent(this, ResultsActivity.class);
        // create a flag to help the results activity determine which calculator opened it
        intent.putExtra(flag, repMaxCalculatorId);
        intent.putExtra(liftResponseAppId, liftResponse);
        intent.putExtra(repetitionResponseAppId, repetitionResponse);
        startActivity(intent);
    }
}
