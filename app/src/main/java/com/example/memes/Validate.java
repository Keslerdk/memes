package com.example.memes;

import com.google.android.material.textfield.TextInputLayout;

public class Validate {
    public Boolean validEmail (TextInputLayout email){
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }
//    public boolean validUsername (String username) {
//
//    }
    public boolean validname (TextInputLayout name) {
        String val = name.getEditText().getText().toString();
        if (val.isEmpty()) {
            name.setError("field cant be empty");
            return false;
        }
        else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }
    public Boolean validPasswordEntry(TextInputLayout password) {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cant be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}
