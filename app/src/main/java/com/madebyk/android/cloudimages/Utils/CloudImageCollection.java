package com.madebyk.android.cloudimages.Utils;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.madebyk.android.cloudimages.data.image_info;

import java.util.ArrayList;
import java.util.List;

public class CloudImageCollection extends AppCompatActivity {
/*
    //class not used
*/
    private static final String TAG = "CloudImageCollection";

    public static List<image_info> getImages(){
        final List<image_info> images = new ArrayList<>();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads/"+mUser.getUid());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    image_info cloud_image_info = postSnapshot.getValue(image_info.class);
                    Log.d(TAG, "onDataChange: The image path is "+cloud_image_info.getImage_Path());
                    images.add(cloud_image_info);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: There was a"+databaseError.getMessage()+"error");
            }
        });
        return images;
    }
}
