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
import java.util.ArrayList;


public class PersonArrayAdapter extends ArrayAdapter<DocumentChange> {

    private Context context;
    private ArrayList<DocumentChange> list;


    public PersonArrayAdapter(Context context, ArrayList<DocumentChange> element){
        super(context, -1, element);
        this.context = context;
        this.list = element;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.ability_list_item, parent, false);

        TextView benefits = rowView.findViewById(R.id.list_ability_name);
        benefits.setText(list.get(position).getDocument().get("benefit").toString());

        TextView prices = rowView.findViewById(R.id.list_ability_price);
        prices.setText((list.get(position).getDocument().get("price").toString())+"€");

        return rowView;
    }
}
