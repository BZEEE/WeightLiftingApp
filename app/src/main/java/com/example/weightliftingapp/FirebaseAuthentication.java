package com.example.weightliftingapp;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

// might be able to put authentication logic right in the activity logic
// https://firebase.google.com/docs/auth/android/manage-users

//public class FirebaseAuthentication {
//    private FirebaseAuth mAuth;
//
//    public FirebaseAuthentication() {
//        mAuth = FirebaseAuth.getInstance();
//    }
//
//    public FirebaseUser GetCurrentUsser() {
//        if (mAuth != null) {
//            return mAuth.getCurrentUser();
//        } else {
//            throw new Error();
//        }
//
//    }
//
//    public void CreateNewUserAccount(email, password) {
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }
//
//}
