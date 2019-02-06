package com.madebyk.android.cloudimages.CloudImages;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.madebyk.android.cloudimages.Adapters.ImageAdapter;
import com.madebyk.android.cloudimages.R;
import com.madebyk.android.cloudimages.Utils.BottomNavigationViewHelper;
import com.madebyk.android.cloudimages.Utils.PermissionsUtil;
import com.madebyk.android.cloudimages.data.image_info;

import java.util.ArrayList;
import java.util.List;

public class CloudImagesActivity extends AppCompatActivity {

    private static final String TAG = "CloudImagesActivity";
    private Context mContext;
    private static final int ACTIVITY_NUM = 0;
    private static final int UNIQUE_REQUEST_CODE = 10101;
    private final List<image_info> list = new ArrayList<>();

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager manager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.dankMode);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        mContext = CloudImagesActivity.this;

        if(list.size() > 0){
            Toast.makeText(this, "There are images already", Toast.LENGTH_SHORT).show();
        }

        if (PermissionsUtil.StoragePermissionAccess(mContext) == false) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    UNIQUE_REQUEST_CODE);
        }
        else {
            setUpToolbar();
            setUpBottomNavigation();
            setUpRecyclerView();
        }
    }

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_id);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            manager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
        } else {
            manager = new StaggeredGridLayoutManager(3, OrientationHelper.HORIZONTAL);
        }
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        getList();
    }

    private void setUpBottomNavigation() {
        Log.d(TAG, "setUpBottomNavigation: setting up bottom navigation view");
        BottomNavigationViewEx view = (BottomNavigationViewEx) findViewById(R.id.images_bottom_navigation_view);
        BottomNavigationViewHelper.setupBottomNavigationView(view);
        BottomNavigationViewHelper.enableNavigation(CloudImagesActivity.this, view);
        Menu menu = view.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUM);
        item.setChecked(true);
        Log.d(TAG, "setUpBottomNavigation: Completed...");
    }

    private void setUpToolbar() {
        ImageView toolbar_image = (ImageView) findViewById(R.id.snippet_toolbar_image);
        TextView toolbar_text = (TextView) findViewById(R.id.snippet_toolbar_text);
        toolbar_image.setImageResource(R.drawable.ic_download_from_cloud);
        toolbar_text.setText(R.string.images_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.images_toolbar);
        setSupportActionBar(toolbar);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == UNIQUE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText((Activity) mContext, "Granted...Thanks!", Toast.LENGTH_SHORT).show();

                setUpToolbar();
                setUpBottomNavigation();
                setUpRecyclerView();
            }
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setMessage("Permissions will be needed in order for the application to function. Please grant permission.")
                            .setTitle("Permission Dialog");

                    dialog.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(
                                    (Activity) mContext
                                    , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                                    , UNIQUE_REQUEST_CODE);
                        }
                    });

                    dialog.setNegativeButton("Nope!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "Was not permitted.", Toast.LENGTH_SHORT).show();
                            finishAffinity();
                        }
                    });
                    dialog.show();
                }
            }
            else{
                finishAffinity();
            }
        }
    }

    private void getList(){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads/"+mUser.getUid()+"/");
        adapter = new ImageAdapter(CloudImagesActivity.this, list, 0);
        recyclerView.setAdapter(adapter);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    image_info cloud_image_info = postSnapshot.getValue(image_info.class);
//                    Toast.makeText(CloudImagesActivity.this, "The path is: "+cloud_image_info.getImage_Path(), Toast.LENGTH_SHORT).show();
                    cloud_image_info.setKey(postSnapshot.getKey());
                    list.add(cloud_image_info);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CloudImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
