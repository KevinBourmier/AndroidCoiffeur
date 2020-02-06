package com.bourmier.projetcoiffeur.validator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bourmier.projetcoiffeur.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class AppointmentArrayAdapter extends ArrayAdapter<DocumentChange> {

    private Context context;
    private ArrayList<DocumentChange> list;

    public AppointmentArrayAdapter(Context context, ArrayList<DocumentChange> element){
        super(context, -1, element);

        this.context = context;
        this.list = element;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.appointment_list_item, parent, false);

        TextView haidresser = rowView.findViewById(R.id.list_appointment_hairdresser_name);
        haidresser.setText(list.get(position).getDocument().get("hairdresser").toString());

        TextView date = rowView.findViewById(R.id.list_appointment_date);
        date.setText("02/02/2020");

        return rowView;
    }
}
