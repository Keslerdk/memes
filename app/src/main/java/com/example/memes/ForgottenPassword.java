package com.example.memes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenPassword extends AppCompatActivity {

    private EditText userEmail;
    private Button resetBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        mAuth = FirebaseAuth.getInstance();

        userEmail = (EditText) findViewById(R.id.userEmail);
        resetBtn = (Button) findViewById(R.id.reset_btn);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString();
                resetEmailfunc(email);
            }
        });
    }

    private void resetEmailfunc(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("reset password", "Email sent.");
                            Toast.makeText(ForgottenPassword.this, "email sent", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(ForgottenPassword.this, "smth gone wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}