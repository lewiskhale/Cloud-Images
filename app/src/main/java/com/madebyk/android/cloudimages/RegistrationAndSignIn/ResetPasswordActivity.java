package com.madebyk.android.cloudimages.RegistrationAndSignIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.madebyk.android.cloudimages.R;

public class ResetPasswordActivity extends AppCompatActivity {

    //mem vars
    private EditText et_email;
    private Button sendEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        et_email = (EditText) findViewById(R.id.pass_reset_email);
        sendEmail = (Button) findViewById(R.id.pass_reset_button);
        progressBar = (ProgressBar) findViewById(R.id.login_toolbar_progressbar);
        progressBar.setVisibility(View.GONE);
        resetPassword();
    }

    private void resetPassword() {
            sendEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!et_email.getText().toString().isEmpty()) {
                        progressBar.setVisibility(View.VISIBLE);
                        String email = et_email.getText().toString();
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ResetPasswordActivity.this, "Email to reset password has been sent. Please check your emails.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ResetPasswordActivity.this, SignInActivity.class));
                                        finishAffinity();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(ResetPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else {
                        Toast.makeText(ResetPasswordActivity.this, "Please insert a valid email address", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
