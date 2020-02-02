package com.bourmier.projetcoiffeur.validator;

import android.content.Context;

import com.bourmier.projetcoiffeur.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

public class Validator {

    private ArrayList<HashMap<String, Object>> toValidate;
    private Context androidContext;
    private ValidatorRule baseRule = new ValidatorRule(){

        @Override
        public boolean validate(TextInputEditText textInput, TextInputLayout inputLayout) {

            if(textInput.getText().toString().isEmpty()){

                inputLayout.setError(androidContext.getString(R.string.error_set_value));
                return true;
            }

            return false;
        }
    };

    public Validator(Context context){

        androidContext = context;
        toValidate = new ArrayList<>();
    }

    public void addField(TextInputEditText inputText, TextInputLayout inputLayout){

        addField(inputText, inputLayout, null);
    }

    public void addField(TextInputEditText inputText, TextInputLayout inputLayout, @Nullable ValidatorRule extraRule){

        HashMap<String, Object> data = new HashMap<>();
        data.put("inputText", inputText);
        data.put("inputLayout", inputLayout);
        data.put("extraRule", extraRule);

        toValidate.add(data);
    }

    public boolean validate(){

        boolean error = false;

        for (HashMap<String, Object> conf: toValidate){

            boolean tempError = baseRule.validate((TextInputEditText) conf.get("inputText"), (TextInputLayout) conf.get("inputLayout"));

            if(!tempError && conf.get("extraRule") != null)

                tempError |= ((ValidatorRule) conf.get("extraRule")).validate((TextInputEditText) conf.get("inputText"), (TextInputLayout) conf.get("inputLayout"));

            if(!tempError)

                ((TextInputLayout) conf.get("inputLayout")).setError(null);


            if(tempError)
                error = true;
        }

        return error;
    }
}
