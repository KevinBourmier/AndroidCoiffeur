package com.bourmier.projetcoiffeur;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class NextAppointmentWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String hairdresser, String date) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.next_appointment_widget);
        views.setTextViewText(R.id.appwidget_text, hairdresser);
        views.setTextViewText(R.id.appwidget_date, date);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        SharedPreferences preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        final String idUser = preferences.getString("uuid", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        System.out.println("Je suis ici");

        db.collection("appointment")
                .whereEqualTo("uuid", idUser)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Date newDate;
                        DateFormat dateFormat;
                        String strDate = new String();

                        if (task.isSuccessful()) {

                            QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                            Timestamp date = (Timestamp) document.getData().get("date");
                            newDate = date.toDate();
                            dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM, Locale.FRANCE);
                            strDate += context.getString(R.string.dateHair) + " " + dateFormat.format(newDate);

                            String hairdresser = new String();
                            hairdresser += context.getString(R.string.hairdresserName) + " " + document.getData().get("hairdresser").toString();

                            for (int appWidgetId : appWidgetIds) {
                                updateAppWidget(context, appWidgetManager, appWidgetId, hairdresser, strDate);
                            }

                            Log.d("OK", document.getId() + " => " + document.getData());
                        }
                        else {
                            Log.w("Error", "Error getting documents.", task.getException());
                        }
                    }
                });

        // There may be multiple widgets active, so update all of them

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

