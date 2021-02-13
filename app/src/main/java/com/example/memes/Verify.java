package com.example.memes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Verify extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button Varific;
    private Button emailCheck;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        mAuth = FirebaseAuth.getInstance();

        Varific=(Button) findViewById(R.id.varific);
        next = (Button) findViewById(R.id.next);
        emailCheck = (Button) findViewById(R.id.navigateToEmail);

        Varific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToProfile();
            }
        });
        emailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getIntent().getStringExtra("url");
                Log.d("url", url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    public void sendEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(Verify.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Verify.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getToProfile(){
        reload();
        Log.d("verify", String.valueOf(mAuth.getCurrentUser()));
        Log.d("verify",String.valueOf(mAuth.getCurrentUser().isEmailVerified()));
        if (mAuth.getCurrentUser().isEmailVerified()) {
            Intent intent = new Intent(Verify.this, Profile.class );
            startActivity(intent);
        }
    }
    private void reload() {
        mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    updateUI(mAuth.getCurrentUser());
                    Toast.makeText(Verify.this,
                            "Reload successful!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("reload", "reload", task.getException());
                    Toast.makeText(Verify.this,
                            "Failed to reload user.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /*
    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
            mBinding.status.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mBinding.detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            mBinding.emailPasswordButtons.setVisibility(View.GONE);
            mBinding.emailPasswordFields.setVisibility(View.GONE);
            mBinding.signedInButtons.setVisibility(View.VISIBLE);

            if (user.isEmailVerified()) {
                mBinding.verifyEmailButton.setVisibility(View.GONE);
            } else {
                mBinding.verifyEmailButton.setVisibility(View.VISIBLE);
            }
        } else {
            mBinding.status.setText(R.string.signed_out);
            mBinding.detail.setText(null);

            mBinding.emailPasswordButtons.setVisibility(View.VISIBLE);
            mBinding.emailPasswordFields.setVisibility(View.VISIBLE);
            mBinding.signedInButtons.setVisibility(View.GONE);
        }
    } */

}