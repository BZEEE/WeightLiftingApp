package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class WilksCalculatorActivity extends AppCompatActivity {
    private EditText bodyWeightInputBox;
    private Button calculateButton;
    private Spinner genderInputSpinner;
    private String genderResponse;
    private static final String flag = "com.example.weightliftingapp.calculatorFlag";
    public static final String bodyWeightResponseAppId = "com.example.weightliftingapp.bodyWeight";
    public static final String genderResponseAppId = "com.example.weightliftingapp.gender";
    public static final String wilksDisplayTitle = "Your Wilks Score";
    public static final String wilksCalculatorId = "WilksCalculatorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wilks_calculator);

        this.CreateUIInputs();
    }

    private void CreateUIInputs() {
        this.calculateButton = findViewById(R.id.WilksCalculateButton);
        this.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenResultsActivity();
            }
        });

        this.bodyWeightInputBox = findViewById(R.id.BodyWeightInputBox);

        this.genderInputSpinner = findViewById(R.id.GenderInputSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, R.layout.gender_spinner_custom_attributes);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.genderInputSpinner.setAdapter(adapter);
    }

    private void OpenResultsActivity() {
        double bodyWeightResponse = Double.parseDouble(this.bodyWeightInputBox.getText().toString());
        this.genderResponse = this.genderInputSpinner.getSelectedItem().toString();

        Intent intent = new Intent(this, ResultsActivity.class);
        // create a flag to help the results activity determine which calculator opened it
        intent.putExtra(flag, wilksCalculatorId);
        intent.putExtra(bodyWeightResponseAppId, bodyWeightResponse);
         intent.putExtra(genderResponseAppId, genderResponse);
        startActivity(intent);
    }
}
