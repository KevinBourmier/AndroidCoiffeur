package com.bourmier.projetcoiffeur;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    Button login;
    SharedPreferences sharedPreferences;
    CheckBox saveLoginCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        username = findViewById(R.id.username);
        login = findViewById(R.id.login);
        saveLoginCheck = findViewById(R.id.saveLoginCheck);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();

            }
        });

        username.setText(sharedPreferences.getString("username", null));
    }

    public void Login(){
        Intent mainActivity = new Intent(this, MainActivity.class);

        String user = username.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(editor != null){
            editor.putBoolean("saveLogin", true);
            editor.putString("username", user);
            editor.putString("uuid", UUID.randomUUID().toString());
            editor.apply();
            finish();
            startActivity(mainActivity);

        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

}
