package com.madebyk.android.cloudimages.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.madebyk.android.cloudimages.R;
import com.madebyk.android.cloudimages.RegistrationAndSignIn.RegistrationActivity;
import com.madebyk.android.cloudimages.RegistrationAndSignIn.SignInActivity;

import java.util.ArrayList;

public class AccountSettings extends AppCompatActivity {

    private static final String TAG = "AccountSettings";

    //Set the firebase stuff

    //member vars
    ImageView toolbar_image;
    TextView toolbar_text;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.AppTheme);
        }
        else{
            setTheme(R.style.dankMode);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Log.d(TAG, "onCreate: account settings started");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        setUpToolbar();
        return_to_settings();
        setUpListView();
    }

    private void setUpToolbar() {
        Log.d(TAG, "setUpToolbar: setting up toolbar");
        toolbar_image = (ImageView) findViewById(R.id.snippet_toolbar_image);
        toolbar_text = (TextView) findViewById(R.id.snippet_toolbar_text);
        toolbar_image.setImageResource(R.drawable.ic_arrow_back_arrow);
        toolbar_text.setText(mUser.getProviderId());
        toolbar_text.setTextSize(20f);
        Toolbar toolbar = (Toolbar) findViewById(R.id.images_toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "setUpToolbar: completed setting up toolbar");
    }

    private void return_to_settings(){
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpListView() {

        ArrayList<String> account_options = new ArrayList<>();
        account_options.add("Edit Profile");
        account_options.add("Remove Account");
        account_options.add("Sign Out");

        ListView listView = (ListView) findViewById(R.id.account_settings_listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AccountSettings.this, android.R.layout.simple_list_item_1, account_options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //Does nothing
                        break;
                    case 1:
                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(AccountSettings.this);
                        dialog1.setTitle("Confirmation")
                                .setMessage("Are you sure you want to remove account and it's contents. Changes cannot be reverted")
                                .setPositiveButton("Yes, I am sure", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                    }
                                })
                                .setNegativeButton("No I am not sure", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Do nothing
                                    }
                                });
                        break;
                    case 2:
                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(AccountSettings.this);
                        dialog2.setTitle("Confirmation")
                                .setMessage("Are you sure you want to sign out of account?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(AccountSettings.this, SignInActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finishAffinity();
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Does nothing
                                    }
                                }).show();
                        break;
                }
            }
        });
    }
}