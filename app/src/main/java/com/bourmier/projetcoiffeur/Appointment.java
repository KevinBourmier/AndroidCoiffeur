package com.bourmier.projetcoiffeur;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import static android.app.DatePickerDialog.*;

public class Appointment extends AppCompatActivity {

    TextInputEditText dateInputText;
    TextInputEditText timeInputText;
    TextInputEditText nameInputText;

    final Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        nameInputText = findViewById(R.id.appointment_name_field);
        nameInputText.setText(preferences.getString("username", ""));

        dateInputText = findViewById(R.id.appointment_date_field);
        dateInputText.setOnClickListener(dateInputTextListener);

        timeInputText = findViewById(R.id.appointment_time_field);
        timeInputText.setOnClickListener(timeInputTextListener);
    }

    final View.OnClickListener dateInputTextListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            DatePickerDialog dialog = new DatePickerDialog(view.getContext(), dateSetListener, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            dialog.show();
        }
    };

    final View.OnClickListener timeInputTextListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            TimePickerDialog dialog = new TimePickerDialog(view.getContext(), timeSetListener, hour, minute, true);
            dialog.show();
        }
    };

    final DatePickerDialog.OnDateSetListener dateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            dateInputText.setText(getString(R.string.dateFormat, i2, i1 + 1, i));
        }
    };

    final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

            timeInputText.setText(getString(R.string.timeFormat, i, i1));
        }
    };
}
