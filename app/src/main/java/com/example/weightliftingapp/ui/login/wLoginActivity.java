package com.example.weightliftingapp.ui.login;

import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.*;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weightliftingapp.Firebase.FirebaseAuthenticationManager;
import com.example.weightliftingapp.Firebase.FirebaseDatabaseManager;
import com.example.weightliftingapp.R;
import com.example.weightliftingapp.UserProfileActivity;

public class wLoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    public static final String usernameAppId = "com.example.weightliftingapp.username";
    public static final String passwordAppId = "com.example.weightliftingapp.password";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final TextView RegisterTextView = findViewById(R.id.RegisterTextView);
        final TextView loginErrorTextView = findViewById(R.id.LoginErrorTextView);
        final TextView LoginTextView = findViewById(R.id.LoginTextView);
        final Switch RegisterOrLoginSwitch = findViewById(R.id.RegisterOrLoginSwitch);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    // login failed
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    // login may have succeeded, so now we check authentication with google firebase
                    String email = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    if (RegisterOrLoginSwitch.isChecked()) {
                        // user wants to login
                        FirebaseAuthenticationManager.SignInExistingUser(email, password);

                    } else {
                        // user wants to register
                        if (FirebaseDatabaseManager.UsernameExistsInDatabase(email)) {
                            // if the email exists in the database, inform user, has to be a unique email/username
                            // display error message
                            loginErrorTextView.setText(R.string.username_already_exists_tag);

                        } else {
                            // else it is a new user that we have to authenticate
                            loginErrorTextView.setText("");
                            FirebaseAuthenticationManager.CreateNewUserAccount(email, password);
                        }
                    }

                    // check to see if authentication with firebase failed
                    if (FirebaseAuthenticationManager.GetCurrentUser() != null) {
                        // to make sure the GetCurrentUser() function in FirebaseAuth receives a fresh token every time
                        FirebaseAuthenticationManager.AttachAuthStateListener();
                        // Go to user profile activity
                        goToUserProfileActivity();
                        // update UI with user view model
                        updateUiWithUser(loginResult.getSuccess());
                    }
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        RegisterOrLoginSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // user wants to login
                    RegisterTextView.setTextColor(getResources().getColor(R.color.fadedGrey, getBaseContext().getTheme()));
                    LoginTextView.setTextColor(getResources().getColor(R.color.TealBlue, getBaseContext().getTheme()));
                    loginButton.setText(R.string.action_login);
                } else {
                    // user wants to register
                    RegisterTextView.setTextColor(getResources().getColor(R.color.TealBlue, getBaseContext().getTheme()));
                    LoginTextView.setTextColor(getResources().getColor(R.color.fadedGrey, getBaseContext().getTheme()));
                    loginButton.setText(R.string.action_register);
                }
            }
        });
    }

    private void goToUserProfileActivity() {
        // go to user profile activity
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
