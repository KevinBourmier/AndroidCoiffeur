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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bourmier.projetcoiffeur.validator.AppointmentArrayAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;

public class FavouriteFragment extends Fragment {

    private TextView textView;
    private ListView listView;
    private ArrayList<StringBuilder> list;
    private ArrayAdapter adapter;
    private TextView emptyList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite, container, false);

        listView = v.findViewById(R.id.listAppointment);
        emptyList = v.findViewById(R.id.emptyList);

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
                //.orderBy("date")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                 @Override
                 public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                     if (e != null) {
                         Log.w("firestore", "Listen failed.", e);
                         return;
                     }
                     adapter.addAll(snapshot.getDocumentChanges());

                     System.out.println("Adapter : " + adapter.getCount());
                     if(adapter.getCount() == 0){
                         emptyList.setText("Vous n'avez pas de rendez vous !");
                     } else if (adapter.getCount() > 0){
                         emptyList.setText(" ");
                     }


                     //Log.d("firestore", "Current data: " + snapshot.getDocuments());
                 }
            });
//        db.collection("appointment")
//                .whereEqualTo("uuid", idUser)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            StringBuilder data = new StringBuilder();
//                            Date date;
//                            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM, Locale.FRANCE);
//                            String strDate;
//
//
//                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                                Timestamp t = (Timestamp) document.getData().get("date");
//                                //System.out.println("Voici la date : " + t.getSeconds());
//                                date = t.toDate();
//                                strDate = dateFormat.format(date);
//                                if(t.getSeconds() >= tsLong) {
//                                    data.append("Nom du coiffeur : ").append(document.getData().get("hairdresser").toString())
//                                            .append("\nDate : ")
//                                            .append(strDate)
//                                            .append("\n\n");
//                                }
//
//
//
//                            }
//                            adapter = new ArrayAdapter(getActivity(), R.layout.row, R.id.coiffeur, list);
//                            list.add(data);
//                            listView.setAdapter(adapter);
//
//                        }
//
//                    }
//                });
    }

}