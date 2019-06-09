package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class dsIPFCalculatorActivity extends AppCompatActivity {

    private EditText bodyWeightInputBox;
    private Button calculateButton;
    private Spinner genderInputSpinner;
    private String genderResponse;
    private static final String flag = "com.example.weightliftingapp.calculatorFlag";
    public static final String bodyWeightResponseAppId = "com.example.weightliftingapp.bodyWeight";
    public static final String genderResponseAppId = "com.example.weightliftingapp.gender";
    public static final String ipfPointCalculatorId = "ipfPointCalculatorActivity";
    public static final String ipfDisplayTitle = "Your IPF Score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipfcalculator);

        this.CreateUIInputs();
    }

    private void CreateUIInputs() {
        this.calculateButton = findViewById(R.id.IPFCalculateButton);
        this.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenResultsActivity();
            }
        });
        this.bodyWeightInputBox = findViewById(R.id.BodyWeightInputBox);
        this.genderInputSpinner = findViewById(R.id.GenderInputSpinner);
        ArrayAdapter <Charsequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, R.layout.gender_spinner_custom_attributes);
        adapter.SetDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.genderInputSpinner.setAdapter(adapter);
    }

    private void OpenResultsActivity() {
        Intent intent = new Intent(this, ResultsActivity.class);
        // create a flag to help the results activity determine which calculator opened it
        intent.putExtra(flag, ipfPointCalculatorId);
        startActivity(intent);
    }
}
