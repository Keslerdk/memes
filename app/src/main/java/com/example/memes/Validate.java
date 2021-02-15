package com.example.memes;

import com.google.android.material.textfield.TextInputLayout;

public class Validate {
    public Boolean validEmail(TextInputLayout email) {
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

    public boolean validUsername(TextInputLayout username) {
        String val = username.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            username.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            username.setError("White Spaces are not allowed");
            return false;
        }
        else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validname(TextInputLayout name) {
        String val = name.getEditText().getText().toString();
        if (val.isEmpty()) {
            name.setError("field cant be empty");
            return false;
        } else if (val.length() >= 15) {
            name.setError("Name too long");
            return false;
        } else {
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

    public boolean validPasswordReg(TextInputLayout password) {
        String val = password.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public boolean equalsPassword (TextInputLayout password1, TextInputLayout password2){
        String val1 = password1.getEditText().getText().toString();
        String val2 = password2.getEditText().getText().toString();

        if (!val1.equals(val2)) {
            password1.setError("try again");
            password2.setError("try again");
            return false;
        }
        else {
            password1.setError(null);
            password2.setError(null);
            password1.setErrorEnabled(false);
            password2.setErrorEnabled(false);
            return true;
        }
    }
}
