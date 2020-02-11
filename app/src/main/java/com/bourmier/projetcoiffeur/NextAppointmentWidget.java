package com.bourmier.projetcoiffeur;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class NextAppointmentWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String hairdresser, String date, boolean error) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.next_appointment_widget);

        views.setViewVisibility(R.id.widget_loading_spinner, View.GONE);

        if(!error){

            views.setViewVisibility(R.id.widget_no_appointment, View.GONE);
            views.setTextViewText(R.id.appwidget_text, hairdresser);
            views.setTextViewText(R.id.appwidget_date, date);
        }else{

            views.setViewVisibility(R.id.widget_no_appointment, View.VISIBLE);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        SharedPreferences preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        final String idUser = preferences.getString("uuid", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("appointment")
            .whereEqualTo("uuid", idUser)
            .whereGreaterThan("date", Timestamp.now())
            .orderBy("date")
            .limit(1)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {


                    if (task.isSuccessful()) {

                        if(task.getResult().getDocuments().size() != 0){

                            QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                            Date newDate = ((Timestamp) document.getData().get("date")).toDate();
                            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM, Locale.FRANCE);
                            String strDate = context.getString(R.string.dateHair, dateFormat.format(newDate));
                            String hairdresser = context.getString(R.string.hairdresserName, document.getData().get("hairdresser").toString());

                            updateAllWidget(appWidgetIds, context, appWidgetManager, hairdresser, strDate, false);

                        }else{

                            updateAllWidget(appWidgetIds, context, appWidgetManager, null, null, true);
                        }
                    }
                    else {

                        updateAllWidget(appWidgetIds, context, appWidgetManager, null, null, true);
                    }
                }
            });
    }

    private void updateAllWidget(int[] appWidgetIds, Context context, AppWidgetManager appWidgetManager, String hairdresser, String date, Boolean error){

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, hairdresser, date, error);
        }
    }
}

