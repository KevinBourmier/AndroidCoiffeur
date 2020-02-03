package com.bourmier.projetcoiffeur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent activityIntent;

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        if(sharedPreferences.getString("username", null) != null){

            FirebaseAnalytics.getInstance(this).setUserId(sharedPreferences.getString("uuid", ""));
            activityIntent = new Intent(this, MainActivity.class);
        }else{

            activityIntent = new Intent(this, LoginActivity.class);
        }

        startActivity(activityIntent);
        finish();
    }
}
