package com.madebyk.android.cloudimages.RegistrationAndSignIn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.madebyk.android.cloudimages.CloudImages.CloudImagesActivity;
import com.madebyk.android.cloudimages.R;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    //sharedPrefs
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASS = "password";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_REMEMBER = "remember";

    //Member vars
    private EditText username, email, password;
    private Button registerButton;
    private TextView tv_register_to_signin;
    private CheckBox checkBox;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.dankMode);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        FirebaseApp.initializeApp(RegistrationActivity.this);

        setViews();
        checkPrefs();
        ToSignInScreen();
    }

    private void setViews() {
        mAuth = FirebaseAuth.getInstance();
        username = (EditText) findViewById(R.id.username_reg);
        email = (EditText) findViewById(R.id.email_reg);
        password = (EditText) findViewById(R.id.passwd_reg);
        registerButton = (Button) findViewById(R.id.registration_button);
        tv_register_to_signin = (TextView) findViewById(R.id.tv_SignIn);
        checkBox = (CheckBox) findViewById(R.id.remember_me_checkbox);
        progressBar = (ProgressBar) findViewById(R.id.login_toolbar_progressbar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(RegistrationActivity.this, "You're in! ;)", Toast.LENGTH_SHORT).show();
            updateUI(currentUser);
        }
        else {
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    String UserEmail = email.getText().toString();
                    String UserPassword = password.getText().toString();
                    if (!(UserEmail.isEmpty() && UserPassword.isEmpty())) {
                        mAuth.createUserWithEmailAndPassword(UserEmail, UserPassword)
                                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                            sendVerification();

                                            if (currentUser.isEmailVerified()) {
                                                // Successfully registered user
                                                Toast.makeText(RegistrationActivity.this, "Account successfully created", Toast.LENGTH_SHORT).show();
                                                //updateUI(currentUser);
                                                untilVerified();
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(RegistrationActivity.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        updateUI(null);
                                    }
                                });
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(RegistrationActivity.this, CloudImagesActivity.class);
            startActivity(intent);
            managePrefs();
            finish();
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void untilVerified() {
        while (!currentUser.isEmailVerified()) {
            currentUser.reload();
        }
        updateUI(currentUser);
    }

    private void ToSignInScreen() {
        Log.d(TAG, "ToSignInScreen: Starting the switch");
        tv_register_to_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Log.d(TAG, "ToSignInScreen: Switch complete...");
    }

    private void checkPrefs() {
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean(PREF_REMEMBER, false)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    private void managePrefs() {
        if (checkBox.isChecked()) {
            editor.putString(PREF_USERNAME, username.getText().toString().trim());
            editor.putString(PREF_EMAIL, email.getText().toString().trim());
            editor.putBoolean(PREF_REMEMBER, true);
            editor.putString(PREF_PASS, password.getText().toString());
            editor.apply();
        } else {
            editor.remove(PREF_USERNAME);
            editor.remove(PREF_EMAIL);
            editor.remove(PREF_PASS);
            editor.putBoolean(PREF_REMEMBER, false);
            editor.apply();
        }
    }

    private void sendVerification() {
        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistrationActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
