package com.bourmier.projetcoiffeur;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

public class PersonFragment extends Fragment {

    ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person, container, false);

        ListView listView = v.findViewById(R.id.listPerson);
        TextView emptyList = v.findViewById(R.id.ability_list_empty_text);
        progressBar = v.findViewById(R.id.ability_list_loading_spinner);
        listView.setEmptyView(emptyList);

        readPerson(listView);

        return v;
    }


    private void readPerson(ListView listView) {
        final ArrayList<DocumentChange> list = new ArrayList<>();
        final PersonArrayAdapter adapter = new PersonArrayAdapter(this.getContext(), list);
        listView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("benefit")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                    progressBar.setVisibility(View.GONE);

                    if (e == null) {
                        adapter.addAll(snapshot.getDocumentChanges());

                        adapter.sort(new Comparator<DocumentChange>() {
                            @Override
                            public int compare(DocumentChange documentChange, DocumentChange t1) {
                                Long price1 = (Long) documentChange.getDocument().get("price");
                                Long price2 = (Long) t1.getDocument().get("price");

                                return price1.compareTo(price2);
                            }
                        });
                    }
                }
            });
    }

    @Override
    public void onResume() {

        super.onResume();

        FirebaseAnalytics.getInstance(getContext()).setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());
    }
}
