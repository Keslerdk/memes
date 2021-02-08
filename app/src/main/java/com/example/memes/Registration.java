package com.example.memes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef.setValue("First data storage");

        rname = (EditText) findViewById(R.id.name);
        ruserName = (EditText) findViewById(R.id.userName);
        rEmail = (EditText) findViewById(R.id.rEmail);
        rPassword = (EditText) findViewById(R.id.rPassword);
        rPassword2 = (EditText) findViewById(R.id.rPassword2);

        signUp = (Button) findViewById(R.id.next_button);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = rname.getText().toString();
                String username = ruserName.getText().toString();
                String email = rEmail.getText().toString();
                String password = rPassword.getText().toString();
                //плохо работает.
                if (emptyField(rname) && emptyField(ruserName) && emptyField(rEmail) && emptyField(rPassword)) {
                    registration(email, password);
                    addUser(name, username, email, password);
                }
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
                    startActivity(intent);
                } else {
                    Toast.makeText(Registration.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUser(String name, String userName, String email, String password) {
        UsersHelperClass helperClass = new UsersHelperClass(name, userName, email, password);
        myRef.child(userName).setValue(helperClass);
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