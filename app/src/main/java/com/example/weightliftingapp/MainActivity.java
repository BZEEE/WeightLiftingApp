package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button goToCalculatorsButton;
    private Button goToProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateUIInputs();
    }

    private void CreateUIInputs() {
        goToCalculatorsButton = findViewById(R.id.GoToCalculatorButton);
        goToCalculatorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCalculatorSelectionActivity();
            }
        });
        goToProfileButton = findViewById(R.id.GoToProfileButton);
        goToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenUserProfileActivity();
            }
        });
    }

    private void OpenCalculatorSelectionActivity() {
        Intent intent = new Intent(this, CalculatorSelectionActivity.class);
        startActivity(intent);
    }

    private void OpenUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}
