package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateUIInputs();
    }

    private void CreateUIInputs() {
        calculateButton = (Button) findViewById(R.id.CalculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenResultsActivity();
            }
        });
    }

    private void OpenResultsActivity() {
        EditText liftInputBox = (EditText) findViewById(R.id.LiftInputBox);
        EditText repetitionsInputBox = (EditText) findViewById(R.id.RepetitionsInputBox);

        Intent intent = new Intent(this, ResultsActivity.class);
        startActivity(intent);
    }
}
