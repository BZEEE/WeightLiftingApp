package com.example.weightliftingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    public static final String usernameAppId = "com.example.weightliftingapp.username";
    public static final String passwordAppId = "com.example.weightliftingapp.password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.CreateUIInputs();
    }

    private void CreateUIInputs() {
        loginButton = findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenUserProfileActivity();
            }
        });
    }

    private void OpenUserProfileActivity() {
        EditText usernameInputBox = findViewById(R.id.UsernameInputBox);
        EditText passwordInputBox = findViewById(R.id.PasswordInputBox);
        String usernameResponse = usernameInputBox.getText().toString();
        String passwordResponse = passwordInputBox.getText().toString();


        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(usernameAppId, usernameResponse);
        intent.putExtra(passwordAppId, passwordResponse);
        startActivity(intent);
    }
}
