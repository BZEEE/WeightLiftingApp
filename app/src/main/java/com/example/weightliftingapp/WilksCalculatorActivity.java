package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WilksCalculatorActivity extends AppCompatActivity {
    private Button calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wilks_calculator);
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
//        this.bodyWeightInputBox = findViewById(R.id.BodyWeightInputBox);
//        // this.genderInputSpinner = findViewById(R.id.GenderInputSpinner);
//        double bodyWeightResponse = Double.parseDouble(this.bodyWeightInputBox.getText().toString());
//        // String genderResponse = this.genderInputSpinner.getSelectedItem().toString();

        Intent intent = new Intent(this, ResultsActivity.class);
        // create a flag to help the results activity determine which calculator opened it
//        intent.putExtra(flag, wilksCalculatorId);
//        intent.putExtra(bodyWeightResponseAppId, bodyWeightResponse);
        // intent.putExtra(genderResponseAppId, genderResponse);
        startActivity(intent);
    }
}
