package com.madebyk.android.cloudimages.RegistrationAndSignIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.madebyk.android.cloudimages.Images_Home.ImagesActivity;
import com.madebyk.android.cloudimages.R;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    //Member vars
    private EditText username, email, password;
    private Button registerButton;
    private TextView tv_register_to_signin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        FirebaseApp.initializeApp(RegistrationActivity.this);

        mAuth = FirebaseAuth.getInstance();
        username = (EditText) findViewById(R.id.username_reg);
        email = (EditText) findViewById(R.id.email_reg);
        password = (EditText) findViewById(R.id.passwd_reg);
        registerButton = (Button) findViewById(R.id.registration_button);
        tv_register_to_signin = (TextView) findViewById(R.id.tv_SignIn);
        ToSignInScreen();
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart: Starting the onStart()");
        //Check to see if the user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.d(TAG, "onStart: current user is not null: "+currentUser.getDisplayName());
            updateUI(currentUser);
        }
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserEmail = email.getText().toString();
                String UserPassword = password.getText().toString();
                if(!(UserEmail.isEmpty() && UserPassword.isEmpty())){
                    mAuth.createUserWithEmailAndPassword(UserEmail, UserPassword)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        // Successfully registered user
                                        Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(RegistrationActivity.this, "Account successfully created",Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    }
                                    else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                }
            }
        });
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            Intent intent = new Intent(RegistrationActivity.this, ImagesActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            //Do stuffs
            Toast.makeText(this, "Cant find account?!", Toast.LENGTH_SHORT).show();
        }
    }

    private void ToSignInScreen(){
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
}
