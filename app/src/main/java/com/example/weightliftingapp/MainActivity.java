package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button goToCalculatorsButton;
    private Button goToLogInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.CreateUIInputs();
    }

    private void CreateUIInputs() {
        goToCalculatorsButton = findViewById(R.id.GoToCalculatorButton);
        goToCalculatorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCalculatorSelectionActivity();
            }
        });
        goToLogInButton = findViewById(R.id.GoToProfileButton);
        goToLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLogInActivity();
            }
        });
    }

    private void OpenCalculatorSelectionActivity() {
        Intent intent = new Intent(this, CalculatorSelectionActivity.class);
        startActivity(intent);
    }

    private void OpenLogInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
