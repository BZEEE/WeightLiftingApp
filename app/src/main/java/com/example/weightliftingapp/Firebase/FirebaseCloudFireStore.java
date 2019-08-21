package com.example.weightliftingapp.Firebase;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

// firestore stores data in Documents, which are stored in Collections
// so Collection contains Documents
// cloud fire store is a noSQL document oriented database
public class FirebaseCloudFireStore {
    // Access a Cloud Firestore instance from your Activity
    private static FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private static final String TAG = "FirebaseCloudFireStore";

    private static Map<String, Object> CreateDataContainer() {
        return new HashMap<>();
    }

    private static void CreateDocument(String documentId) {
        fireStore.document(documentId);
    }

    private static void CreateCollection(String collectionId) {
        fireStore.collection(collectionId);
    }

    private static DocumentReference GetDocument(String documentId) {
        return fireStore.document(documentId);
    }

    private static DocumentReference GetCollection(String collectionId, String documentId) {
        return fireStore.collection(collectionId).document(documentId);
    }

    private static void AddDataContainerToDocument(Map<String, Object> dataContainer, String collectionId, String documentId) {
        fireStore.collection(collectionId).document(documentId)
                .set(dataContainer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing data container to document", e);
                    }
                });
    }

    private static void AddDocumentToCollection(String collectionId, String documentId) {
        // Add a new document with a generated ID
        fireStore.collection(collectionId)
                .add(fireStore.document(documentId))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document to collection", e);
                    }
                });
    }

    private static void WriteBatchData(DocumentReference documentRef, String key, Object value) {
        WriteBatch batch = fireStore.batch();
        batch.update(documentRef, key, value);
    }

    private static void WriteBatchData(DocumentReference documentRef, Map<String, Object> dataContainer) {
        WriteBatch batch = fireStore.batch();
        batch.update(documentRef, dataContainer);
    }

    private static void DeleteDocument(DocumentReference documentRef) {
        WriteBatch batch = fireStore.batch();
        batch.delete(documentRef);
    }

    private static void DeleteItemField(DocumentReference documentRef) {
        Map<String,Object> updates = new HashMap<>();
        updates.put("capital", FieldValue.delete());
    }
}

