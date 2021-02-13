package com.example.memes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {


    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private EditText rname;
    private EditText ruserName;
    private EditText rEmail;
    private EditText rPassword;
    private EditText rPassword2;

    private Button signUp;
    private Button backtoEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //убрать верхнюю полоску с временем и зарядкой
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();



        rname = (EditText) findViewById(R.id.name);
        ruserName = (EditText) findViewById(R.id.userName);
        rEmail = (EditText) findViewById(R.id.rEmail);
        rPassword = (EditText) findViewById(R.id.rPassword);
        rPassword2 = (EditText) findViewById(R.id.rPassword2);

        signUp = (Button) findViewById(R.id.next_button);
        backtoEnter = (Button) findViewById(R.id.back_to_enter);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("users");

                String name = rname.getText().toString();
                String username = ruserName.getText().toString();
                String email = rEmail.getText().toString();
                String password = rPassword.getText().toString();
                //плохо работает.
                if (emptyField(rname) && emptyField(ruserName) && emptyField(rEmail) && emptyField(rPassword)) {
                    registration(email, password);
//                    addUser(name, username, email);
                }
            }
        });
        backtoEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void registration(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Registration.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registration.this, Verify.class);
                    int index = email.indexOf('@');
                    String intentmessage = "http://www."+email.substring(index);
                    intent.putExtra("url", intentmessage);
                    startActivity(intent);
                } else {
                    Toast.makeText(Registration.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUser(String name, String userName, String email) {
        UsersHelperClass helperClass = new UsersHelperClass(name, userName, email);
//        myRef.child(userName).setValue(helperClass);
        myRef.push().setValue(helperClass);
    }

    private boolean emptyField(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Field can't be empty!");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

}