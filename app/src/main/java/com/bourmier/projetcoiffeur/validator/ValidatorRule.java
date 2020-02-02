package com.bourmier.projetcoiffeur.validator;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public interface ValidatorRule {

    boolean validate(TextInputEditText textInput, TextInputLayout inputLayout);
}
