package com.bourmier.projetcoiffeur;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite, container, false);

        listView = (ListView)v.findViewById(R.id.listAppointment);
        list = new ArrayList<StringBuilder>();

        readAppointment();

        return v;

    }


    private void readAppointment() {
        SharedPreferences preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final long tsLong = currentTimeMillis()/1000;
        //System.out.println(tsLong);
        final String idUser = preferences.getString("uuid", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appointment")
                .whereEqualTo("uuid", idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StringBuilder data = new StringBuilder();
                            Date date;
                            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM, Locale.FRANCE);
                            String strDate;



                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Timestamp t = (Timestamp) document.getData().get("date");
                                //System.out.println("Voici la date : " + t.getSeconds());
                                date = t.toDate();
                                strDate = dateFormat.format(date);
                                if(t.getSeconds() >= tsLong) {
                                    data.append("Nom du coiffeur : ").append(document.getData().get("hairdresser").toString())
                                            .append("\nDate : ")
                                            .append(strDate)
                                            .append("\n\n");
                                }



                            }
                            adapter = new ArrayAdapter(getActivity(), R.layout.row, R.id.coiffeur, list);
                            list.add(data);
                            listView.setAdapter(adapter);

                        }

                    }
                });
    }

}