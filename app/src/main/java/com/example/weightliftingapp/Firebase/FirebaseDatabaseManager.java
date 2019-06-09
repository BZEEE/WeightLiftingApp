package com.example.weightliftingapp.Firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public final class FirebaseDatabaseManager {
    private static FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    private static final String TAG = "FirebaseDatabaseManager";


    public static boolean UsernameExistsInDatabase(String username) {
        DatabaseReference ref = fDatabase.getReference("Users/"+username);
        return (ref != null);
    }

    public static void writeToDatabase(String location, String message) {
        DatabaseReference refLocation = fDatabase.getReference(location);
        refLocation.setValue(message);

    }

    public static void readFromDatabase(DatabaseReference refLocation) {
        // Read from the database
        refLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
