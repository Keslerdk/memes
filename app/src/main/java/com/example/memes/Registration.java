package com.example.memes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
    private Button Varific;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        Rlogin=(EditText) findViewById(R.id.Remail);
        Rpassword=(EditText) findViewById(R.id.RPassword);

        SignUp=(Button) findViewById(R.id.signUp);
        Varific=(Button) findViewById(R.id.varific);
        Varific.setVisibility(View.GONE);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration(Rlogin.getText().toString(), Rpassword.getText().toString());
            }
        });
        Varific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    public void registration(String email, String password) {
        Varific.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                    Toast.makeText(Registration.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(Registration.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(Registration.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Registration.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}