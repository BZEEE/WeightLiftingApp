package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WilksCalculatorActivity extends AppCompatActivity {

    private Button calculateButton;
    private EditText bodyWeightInputBox;
    private EditText genderInputBox;
    private static final String bodyWeightResponseAppId = "com.example.weightliftingapp.liftResponse";
    private static final String genderResponseAppId = "com.example.weightliftingapp.repetitionResponse";
    private static final String flag = "com.example.weightliftingapp.calculatorFlag";
    public static final String wilksCalculatorId = "WilksCalculatorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wilks_calculator);

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
        this.bodyWeightInputBox = findViewById(R.id.BodyWeightInputBox);
        this.genderInputBox = findViewById(R.id.GenderInputBox);
        double bodyWeightResponse = Double.parseDouble(this.bodyWeightInputBox.getText().toString());
        int genderResponse = Integer.parseInt(this.genderInputBox.getText().toString());


        Intent intent = new Intent(this, ResultsActivity.class);
        // create a flag to help the results activity determine which calculator opened it
        intent.putExtra(flag, wilksCalculatorId);
        intent.putExtra(bodyWeightResponseAppId, bodyWeightResponse);
        intent.putExtra(genderResponseAppId, genderResponse);
        startActivity(intent);
    }
}
