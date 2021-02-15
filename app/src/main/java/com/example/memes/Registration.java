package com.example.memes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {


    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private TextInputLayout rname;
    private TextInputLayout ruserName;
    private TextInputLayout rEmail;
    private TextInputLayout rPassword1;
    private TextInputLayout rPassword2;

    private Button signUp;
    private Button backtoEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //убрать верхнюю полоску с временем и зарядкой
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();


        rname = (TextInputLayout) findViewById(R.id.reg_name);
        ruserName = (TextInputLayout) findViewById(R.id.reg_userName);
        rEmail = (TextInputLayout) findViewById(R.id.reg_email);
        rPassword1 = (TextInputLayout) findViewById(R.id.reg_password1);
        rPassword2 = (TextInputLayout) findViewById(R.id.reg_assword2);

        signUp = (Button) findViewById(R.id.next_button);
        backtoEnter = (Button) findViewById(R.id.back_to_enter);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("users");

                String name = rname.getEditText().getText().toString();
                String username = ruserName.getEditText().getText().toString();
                String email = rEmail.getEditText().getText().toString();
                String password1 = rPassword1.getEditText().getText().toString();
                //плохо работает.
                Validate valid = new Validate();
                boolean nameform = valid.validname(rname);
                boolean usernameform = valid.validUsername(ruserName);
                boolean emailform = valid.validEmail(rEmail);
                boolean passwordform = valid.validPasswordReg(rPassword1);
                boolean twoPswrdsform = valid.equalsPassword(rPassword1, rPassword2);

                if (nameform && usernameform && emailform && passwordform && twoPswrdsform)
                    registration(name, username, email, password1);
//                    addUser(name, username, email);

            }
        });
        backtoEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void registration(String name, String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Registration.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    addUser(name, username, email);
                    Intent intent = new Intent(Registration.this, Verify.class);
//                    int index = email.indexOf('@');
//                    String intentmessage = "http://www." + email.substring(index);
//                    intent.putExtra("url", intentmessage);
                    startActivity(intent);
                } else {
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthUserCollisionException e) {
                        Toast.makeText(Registration.this, "Пользователь с таким email уже существует", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void addUser(String name, String userName, String email) {
        UsersHelperClass helperClass = new UsersHelperClass(name, userName, email);
//        myRef.child(userName).setValue(helperClass);
        myRef.child(userName).setValue(helperClass);
    }

//    private boolean uniqUser (TextInputLayout username) {
//        String val = username.getEditText().getText().toString();
//
//    }

}