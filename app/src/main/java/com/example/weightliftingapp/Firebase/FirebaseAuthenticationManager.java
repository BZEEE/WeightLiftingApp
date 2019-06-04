package com.example.weightliftingapp.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public final class FirebaseAuthenticationManager {
    private static FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static FirebaseUser currentUser = null;
    private static final String TAG = "FirebaseAuthManager";


    public static FirebaseAuth GetFirebaseAuthenticationInstance() {
        return fAuth;
    }

    public static FirebaseUser GetCurrentUser() {
        return currentUser;
    }

    public static void SetCurrentUser(FirebaseUser user) {
        currentUser = user;
    }

    public static void AttachAuthStateListener() {
        // get a callback every time the underlying token state changes.
        // This can be useful to react to edge cases like if the user was deleted
        // on another device and the local token has not refreshed. In this case,
        // you may get a valid user getCurrentUser but subsequent calls to
        // authenticated resources will fail.
        fAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = fAuth.getCurrentUser();
            }
        });
    }

    public static void CreateNewUserAccount(String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = fAuth.getCurrentUser();
                            SetCurrentUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            SetCurrentUser(null);

                        }
                    }
                });
    }

    public static void SignInExistingUser(String email, String password) {
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInExistingUserWithEmail:success");
                            FirebaseUser user = fAuth.getCurrentUser();
                            SetCurrentUser(user);
                        } else {
                            Log.w(TAG, "signInExistingUserWithEmail:failure", task.getException());
                            SetCurrentUser(null);
                        }
                    }
                });
    }

    public static void resetUsername(String email) {
        // reset user's password
        // provide toast message saying password was changed
        // perform null checks to see if a user is signed in
        FirebaseUser user = fAuth.getCurrentUser();

        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        } else {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });
    }

    private static void resetPassword(String password) {
        // reset user's username
        // provide toast message saying username was changed
        // perform null checks to see if a user is signed in
        FirebaseUser user = fAuth.getCurrentUser();

        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        } else {
                            Log.d(TAG, "User password not updated.");
                        }
                    }
                });
    }

    private static void DeleteUserAccount() {
        FirebaseUser user = fAuth.getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User Account Deleted");
                        }
                    }
                });
    }

    private static void ReAuthenticateUserAccount(String email, String password) {
        FirebaseUser user = fAuth.getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                    }
                });
    }
}
