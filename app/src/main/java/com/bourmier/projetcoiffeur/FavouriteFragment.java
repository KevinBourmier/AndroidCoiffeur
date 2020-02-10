package com.bourmier.projetcoiffeur;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

public class FavouriteFragment extends Fragment {

    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite, container, false);

        ListView listView = v.findViewById(R.id.appointment_list_list);
        TextView emptyList = v.findViewById(R.id.appointment_list_empty_text);
        progressBar = v.findViewById(R.id.appointment_list_loading_spinner);
        listView.setEmptyView(emptyList);

        readAppointment(listView);

        return v;
    }

    private void readAppointment(ListView listView) {
        SharedPreferences preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final ArrayList<DocumentChange> list = new ArrayList<>();
        final AppointmentArrayAdapter adapter = new AppointmentArrayAdapter(this.getContext(), list);
        listView.setAdapter(adapter);
        //System.out.println(tsLong);
        final String idUser = preferences.getString("uuid", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("appointment")
            .whereEqualTo("uuid", idUser)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                 @Override
                 public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                     progressBar.setVisibility(View.GONE);

                     if (e != null) {
                         Log.w("firestore", "Listen failed.", e);
                         return;
                     }
                     adapter.addAll(snapshot.getDocumentChanges());

                     adapter.sort(new Comparator<DocumentChange>() {
                         @Override
                         public int compare(DocumentChange documentChange, DocumentChange t1) {
                             Timestamp timestamp1 = (Timestamp) documentChange.getDocument().get("date");
                             Timestamp timestamp2 = (Timestamp) t1.getDocument().get("date");

                             return timestamp1.compareTo(timestamp2);
                         }
                     });
                 }
            });
    }

    @Override
    public void onResume() {

        super.onResume();

        FirebaseAnalytics.getInstance(getContext()).setCurrentScreen(getActivity(), this.getClass().getSimpleName(),  this.getClass().getSimpleName());
    }
}
