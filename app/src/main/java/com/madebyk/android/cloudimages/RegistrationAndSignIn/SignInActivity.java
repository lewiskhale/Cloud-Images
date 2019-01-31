package com.madebyk.android.cloudimages.RegistrationAndSignIn;

import android.app.Activity;
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

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    //Member vars
    private EditText email, password;
    private Button signInButton;
    private TextView tv_signIn_to_reg;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FirebaseApp.initializeApp(SignInActivity.this);

        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.email_sign_in);
        password = (EditText) findViewById(R.id.passwd_sign_in);
        signInButton = (Button) findViewById(R.id.sign_in_button);
        ToRegisterScreen();
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
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                }
            }
        });

    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(SignInActivity.this, ImagesActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Cant find the account?!", Toast.LENGTH_SHORT).show();
        }
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
    //close other activities and log in
}
