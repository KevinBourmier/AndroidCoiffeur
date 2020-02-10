package com.bourmier.projetcoiffeur;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bourmier.projetcoiffeur.validator.AppointmentArrayAdapter;
import com.bourmier.projetcoiffeur.validator.PersonArrayAdapter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


import com.google.firebase.analytics.FirebaseAnalytics;

public class PersonFragment extends Fragment {

    private TextView textView;
    private ListView listView;
    private ArrayList<StringBuilder> list;
    private ArrayAdapter adapter;
    private TextView emptyList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person, container, false);

        listView = v.findViewById(R.id.listPerson);

        readPerson(listView);

        return v;
    }


    private void readPerson(ListView listView) {
        final ArrayList<DocumentChange> list = new ArrayList<>();
        final PersonArrayAdapter adapter = new PersonArrayAdapter(this.getContext(), list);
        listView.setAdapter(adapter);
        //System.out.println(tsLong);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("benefit")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("firestore", "Listen failed.", e);
                            return;
                        }
                        adapter.addAll(snapshot.getDocumentChanges());

                        System.out.println("Adapter : " + adapter.getCount());


                        //Log.d("firestore", "Current data: " + snapshot.getDocuments());
                    }
                });
    }

    @Override
    public void onResume() {

        super.onResume();

        FirebaseAnalytics.getInstance(getContext()).setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());
    }
}
