package com.bourmier.projetcoiffeur;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FirebaseRemoteConfig config;
    private TextView descriptionText;
    private TextView localisationText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView firstName = view.findViewById(R.id.home_hello_message);

        SharedPreferences preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        firstName.setText(getString(R.string.hello, preferences.getString("username", "")));

        config = FirebaseRemoteConfig.getInstance();
        descriptionText = view.findViewById(R.id.home_description_message);
        localisationText = view.findViewById(R.id.home_localisation_message);

        Button addAppointment = view.findViewById(R.id.home_take_appointment_button);
        addAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity() instanceof  OnAddAppointmentActivityStart)

                    ((OnAddAppointmentActivityStart) getActivity()).startAddAppointmentActivity();
            }
        });

        initHomeText();
        updateHomeText();

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        FirebaseAnalytics.getInstance(getContext()).setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());
    }

    private void initHomeText () {


        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        config.setConfigSettingsAsync(configSettings);

        config.setDefaultsAsync(R.xml.remote_config_defaults);

        config.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {

                updateHomeText();
            }
        });
    }

    private void updateHomeText(){

        descriptionText.setText(config.getString("home_description"));
        localisationText.setText(config.getString("home_localisation"));
    }
}
