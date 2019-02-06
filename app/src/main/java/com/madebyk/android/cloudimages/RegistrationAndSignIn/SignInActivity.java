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
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.madebyk.android.cloudimages.CloudImages.CloudImagesActivity;
import com.madebyk.android.cloudimages.R;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    //sharedPrefs
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private static final String PREF_PASS = "password";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_REMEMBER = "remember";

    //Member vars
    private EditText email, password;
    private Button signInButton;
    private TextView tv_signIn_to_reg, tv_forgot_password;
    private CheckBox checkBox;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.AppTheme);
        }
        else{
            setTheme(R.style.dankMode);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FirebaseApp.initializeApp(SignInActivity.this);

        setViews();
        ToRegisterScreen();
        signIn();
        checkPrefs();
        resetPassword();
    }

    private void setViews(){
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.email_sign_in);
        password = (EditText) findViewById(R.id.passwd_sign_in);
        signInButton = (Button) findViewById(R.id.sign_in_button);
        checkBox = (CheckBox) findViewById(R.id.remember_me_checkbox);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        progressBar = (ProgressBar) findViewById(R.id.login_toolbar_progressbar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
            Toast.makeText(SignInActivity.this, "Account already active",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(SignInActivity.this, CloudImagesActivity.class);
            startActivity(intent);
            managePrefs();
            finish();
        }
        progressBar.setVisibility(View.GONE);
    }

    private void ToRegisterScreen(){
        tv_signIn_to_reg = (TextView) findViewById(R.id.tv_Back_to_reg);
        tv_signIn_to_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signIn(){
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String UserEmail = email.getText().toString();
                String UserPassword = password.getText().toString().trim();
                if (!(UserEmail.isEmpty() && UserPassword.isEmpty())) {
                    mAuth.signInWithEmailAndPassword(UserEmail, UserPassword)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        Toast.makeText(SignInActivity.this, "Successfully signed in",Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    }
                                }
                            })
                            .addOnFailureListener(SignInActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    updateUI(null);
                                }
                            });
                }
            }
        });
    }

    private void checkPrefs(){
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getBoolean(PREF_REMEMBER, false)){
            checkBox.setChecked(true);
        }
        else{
            checkBox.setChecked(false);
        }
        email.setText(sharedPreferences.getString(PREF_EMAIL, ""));
        password.setText(sharedPreferences.getString(PREF_PASS, ""));
    }

    private void managePrefs(){
        if(checkBox.isChecked()){
            editor.putString(PREF_EMAIL, email.getText().toString().trim());
            editor.putBoolean(PREF_REMEMBER, true);
            editor.putString(PREF_PASS, password.getText().toString());
            editor.apply();
        }
        else{
            editor.remove(PREF_EMAIL);
            editor.remove(PREF_PASS);
            editor.putBoolean(PREF_REMEMBER, false);
            editor.apply();
        }

    }

    private void resetPassword(){
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ResetPasswordActivity.class));
            }
        });
    }
}
