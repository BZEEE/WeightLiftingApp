package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class WilksCalculatorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button calculateButton;
    private EditText bodyWeightInputBox;
    private Spinner genderInputSpinner;
    private static final String flag = "com.example.weightliftingapp.calculatorFlag";
    public static final String bodyWeightResponseAppId = "com.example.weightliftingapp.liftResponse";
    public static final String genderResponseAppId = "com.example.weightliftingapp.repetitionResponse";
    public static final String wilksCalculatorId = "WilksCalculatorActivity";
    public static final String wilksDisplayTitle = "Wilks Calculation";


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

        this.genderInputSpinner = findViewById(R.id.GenderInputSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderInputSpinner.setAdapter(adapter);
        genderInputSpinner.setOnItemSelectedListener(this);

    }

    private void OpenResultsActivity() {
        this.bodyWeightInputBox = findViewById(R.id.BodyWeightInputBox);
        this.genderInputSpinner = findViewById(R.id.GenderInputSpinner);
        double bodyWeightResponse = Double.parseDouble(this.bodyWeightInputBox.getText().toString());
        String genderResponse = genderInputSpinner.getSelectedItem().toString();

        Intent intent = new Intent(this, ResultsActivity.class);
        // create a flag to help the results activity determine which calculator opened it
        intent.putExtra(flag, wilksCalculatorId);
        intent.putExtra(bodyWeightResponseAppId, bodyWeightResponse);
        intent.putExtra(genderResponseAppId, genderResponse);
        startActivity(intent);
    }

    // have to implement these methods because of onItemSelectedListener interface
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        //parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        // if no spinner item is selected, do nothing for now
    }

}
