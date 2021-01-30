package com.example.memes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText Rlogin;
    private EditText Rpassword;
    private Button SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        Rlogin = (EditText) findViewById(R.id.Remail);
        Rpassword = (EditText) findViewById(R.id.RPassword);

        SignUp = (Button) findViewById(R.id.signUp);


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration(Rlogin.getText().toString(), Rpassword.getText().toString());
            }
        });

    }

    public void registration(String email, String password) {
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


}