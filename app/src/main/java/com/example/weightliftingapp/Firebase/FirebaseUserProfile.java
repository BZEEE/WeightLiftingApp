package com.example.weightliftingapp.Firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.GetTokenResult;

import android.net.Uri;


public final class FirebaseUserProfile {
    private static FirebaseUser user = FirebaseAuthenticationManager.GetCurrentUser();
    private static final String TAG = "FirebaseUserProfile";

    public static String GetUserDisplayName() {
        if (user != null) {
            return user.getDisplayName();
        } else {
            return null;
        }
    }

    public static String GetUserEmail() {
        if (user != null) {
            return user.getEmail();
        } else {
            return null;
        }
    }

    public static Uri GetUserPhotoUrl() {
        if (user != null) {
            return user.getPhotoUrl();
        } else {
            return null;
        }
    }

    public static String GetUserUid() {
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    public static String GetUserPhoneNumber() {
        if (user != null) {
            return user.getPhoneNumber();
        } else {
            return null;
        }
    }

    public static String GetProviderId() {
        if (user != null) {
            return user.getProviderId();
        } else {
            return null;
        }
    }

    public static FirebaseUserMetadata GetUserMetaData() {
        if (user != null) {
            return user.getMetadata();
        } else {
            return null;
        }
    }

    public static Task<GetTokenResult> GetUserIdToken() {
        if (user != null) {
            return user.getIdToken(true);
        } else {
            return null;
        }
    }

}
