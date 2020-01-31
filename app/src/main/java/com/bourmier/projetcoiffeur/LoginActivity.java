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

public class LoginActivity extends AppCompatActivity {

    EditText username;
    Button login;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CheckBox saveLoginCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        username = (EditText)findViewById(R.id.username);
        login = (Button)findViewById(R.id.login);
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        saveLoginCheck = (CheckBox)findViewById(R.id.saveLoginCheck);
        editor = sharedPreferences.edit();

        Intent mainActivity = new Intent(this, MainActivity.class);

        //System.out.println("VOICI LE NOM DU CLIENT " + sharedPreferences.getString("username", username.getText().toString()));

        if(!sharedPreferences.getString("username", username.getText().toString()).equals(""))
            startActivity(mainActivity);

        //if(sharedPreferences != null)
           // startActivity(mainActivity);

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

        if(editor != null){
            startActivity(mainActivity);

                editor.putBoolean("saveLogin", true);
                editor.putString("username", user);
                editor.commit();

        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

}
