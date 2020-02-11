package com.bourmier.projetcoiffeur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bourmier.projetcoiffeur.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.type.Color;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;


public class AppointmentArrayAdapter extends ArrayAdapter<DocumentChange> {

    private Context context;
    private ArrayList<DocumentChange> list;
    Date newDate;
    DateFormat dateFormat;
    String strDate;

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
        haidresser.setTextSize(20);
        haidresser.setText(list.get(position).getDocument().get("hairdresser").toString());

        TextView date = rowView.findViewById(R.id.list_appointment_date);
        Timestamp t = (Timestamp) list.get(position).getDocument().get("date");
        newDate = t.toDate();
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM, Locale.getDefault());
        strDate = dateFormat.format(newDate);
        date.setText(strDate);

        return rowView;
    }
}
