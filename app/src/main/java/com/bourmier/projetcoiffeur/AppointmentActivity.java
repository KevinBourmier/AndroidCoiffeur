package com.bourmier.projetcoiffeur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.bourmier.projetcoiffeur.validator.Validator;
import com.bourmier.projetcoiffeur.validator.ValidatorRule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.DatePickerDialog.*;

public class AppointmentActivity extends AppCompatActivity {

    private TextInputEditText dateInputText;
    private TextInputEditText timeInputText;
    private TextInputEditText nameInputText;
    private TextInputEditText hairdresserInputText;

    private Validator validator;
    private Appointment appointment;

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

        appointment = new Appointment();

        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        nameInputText = findViewById(R.id.appointment_name_field);
        TextInputLayout nameInputTextLayout = findViewById(R.id.appointment_name_field_layout);
        nameInputText.setText(preferences.getString("username", ""));

        dateInputText = findViewById(R.id.appointment_date_field);
        TextInputLayout dateInputTextLayout = findViewById(R.id.appointment_date_field_layout);
        dateInputText.setOnClickListener(dateInputTextListener);

        timeInputText = findViewById(R.id.appointment_time_field);
        TextInputLayout timeInputTextLayout = findViewById(R.id.appointment_time_field_layout);
        timeInputText.setOnClickListener(timeInputTextListener);

        hairdresserInputText = findViewById(R.id.appointment_hairdresser_field);
        TextInputLayout hairdresserInputTextLayout = findViewById(R.id.appointment_hairdresser_field_layout);

        validator = new Validator(this);
        validator.addField(nameInputText, nameInputTextLayout);
        validator.addField(hairdresserInputText, hairdresserInputTextLayout);
        validator.addField(dateInputText, dateInputTextLayout, dateRule);
        validator.addField(timeInputText, timeInputTextLayout, timeRule);

        Button sendFormButton = findViewById(R.id.appointment_validate_button);
        sendFormButton.setOnClickListener(formListener);
    }

    final View.OnClickListener formListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            final View tempView = view;
            boolean validate = validator.validate();

            if(!validate){

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

                try{
                    Date appointmentDate = dateFormat.parse(dateInputText.getText().toString() + " " + timeInputText.getText().toString());

                    appointment.addAppointmnent(
                        nameInputText.getText().toString(),
                        hairdresserInputText.getText().toString(),
                            new Timestamp(appointmentDate),
                        new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Intent result = new Intent();
                                result.putExtra("id", documentReference.getId());
                                setResult(RESULT_OK, result);
                                finish();
                            }
                        },
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Snackbar.make(tempView, R.string.error_db_save, Snackbar.LENGTH_LONG).show();
                            }
                        }
                    );

                }catch (Exception e){

                    Snackbar.make(view, getString(R.string.error_occured), Snackbar.LENGTH_LONG).show();
                }
            }
        }
    };

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

    final ValidatorRule dateRule =  new ValidatorRule() {
        @Override
        public boolean validate(TextInputEditText textInput, TextInputLayout inputLayout) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateFormat.setLenient(false);

            try {
                Date appointmentDate = dateFormat.parse(textInput.getText().toString());

                if(appointmentDate.before(new Date()))

                    inputLayout.setError(getString(R.string.error_future_date));

            }catch (Exception e){

                inputLayout.setError(getString(R.string.error_date_format));
                return true;
            }

            return false;
        }
    };

    final  ValidatorRule timeRule =  new ValidatorRule() {
        @Override
        public boolean validate(TextInputEditText textInput, TextInputLayout inputLayout) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            dateFormat.setLenient(false);

            try {
                Date appointmentDate = dateFormat.parse(textInput.getText().toString());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(appointmentDate.getTime());

                int hour = calendar.get(Calendar.HOUR_OF_DAY);

                if(hour > 19 || hour < 10){

                    inputLayout.setError(getString(R.string.error_shop_close));
                    return true;
                }

            }catch (Exception e){

                inputLayout.setError(getString(R.string.error_date_format));
                return true;
            }

            return false;
        }
    };
}