package com.example.weightliftingapp.IPF;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.weightliftingapp.R;
import com.example.weightliftingapp.ResultsActivity;

public class IPFCalculatorActivity extends AppCompatActivity {

    private EditText bodyWeightInputBox;
    private EditText totalInputBox;
    private Button calculateButton;
    private Spinner genderInputSpinner;
    private static final String flag = "com.example.weightliftingapp.calculatorFlag";
    public static final String totalResponseIPFAppId = "com.example.weightliftingapp.totalIPF";
    public static final String bodyWeightResponseIPFAppId = "com.example.weightliftingapp.bodyWeightIPF";
    public static final String genderResponseIPFAppId = "com.example.weightliftingapp.genderIPF";
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
        this.totalInputBox = findViewById(R.id.TotalIPFInputBox);
        this.bodyWeightInputBox = findViewById(R.id.BodyWeightIPFInputBox);
        this.genderInputSpinner = findViewById(R.id.GenderIPFInputSpinner);
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, R.layout.gender_spinner_custom_attributes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.genderInputSpinner.setAdapter(adapter);
    }

    private void OpenResultsActivity() {
        double totalResponse = Double.parseDouble(this.totalInputBox.getText().toString());
        double bodyWeightResponse = Double.parseDouble(this.bodyWeightInputBox.getText().toString());
        String genderResponse = this.genderInputSpinner.getSelectedItem().toString();

        Intent intent = new Intent(this, ResultsActivity.class);
        // create a flag to help the results activity determine which calculator opened it
        intent.putExtra(flag, ipfPointCalculatorId);
        intent.putExtra(totalResponseIPFAppId, totalResponse);
        intent.putExtra(bodyWeightResponseIPFAppId, bodyWeightResponse);
        intent.putExtra(genderResponseIPFAppId, genderResponse);
        startActivity(intent);
    }
}
