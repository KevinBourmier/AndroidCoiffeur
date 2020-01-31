package com.bourmier.projetcoiffeur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        TextView firstName = (TextView) findViewById(R.id.firstName);

        String s = logSharedPreferences(this);
        firstName.setText(s);


    }

    public static String logSharedPreferences(final Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        String user = "";
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue().toString();
            Log.d("map values", key + ": " + value);
            if(key.equals("username"))
                user = value;
        }
        return user;
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_fav:
                    selectedFragment = new FavouriteFragment();
                    break;
                case R.id.nav_person:
                    selectedFragment = new PersonFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            return true;
        }
    };




}
