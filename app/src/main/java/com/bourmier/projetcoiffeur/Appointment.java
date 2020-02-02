package com.bourmier.projetcoiffeur;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Appointment {

    private FirebaseFirestore firestore;

    public Appointment(){

        firestore = FirebaseFirestore.getInstance();
    }

    public void addAppointmnent(String name, String uuid, String hairdresser, Timestamp date, OnSuccessListener<DocumentReference> successListener, OnFailureListener failureListener){

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("uuid", uuid);
        data.put("hairdresser", hairdresser);
        data.put("date", date);

        firestore.collection("appointment")
            .add(data)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener);
    }
}
