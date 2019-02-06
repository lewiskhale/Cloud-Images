package com.madebyk.android.cloudimages.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.madebyk.android.cloudimages.R;
import com.madebyk.android.cloudimages.data.image_info;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Handler;

public class LocalFullImageAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<image_info> list_of_images;
    private View mDecorView;

    private StorageReference StorageRef = FirebaseStorage.getInstance().getReference("uploads");
    private DatabaseReference DatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    //member variables
    private ImageView share_button;
    private ImageView like_button;
    private ImageView delete_button;
    private ImageView upload_button;
    private ImageView info_button;
    private ProgressBar progressBar;
    private ImageView back_arrow;

    public LocalFullImageAdapter(Context context, ArrayList<image_info> images, View decorView) {
        mContext = context;
        list_of_images = images;
        mDecorView = decorView;
        hideSystemUI();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return list_of_images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.images_fullscreen_image, null);

        findButtons(view);
        button_functionality(position);

        ImageView fullscreenImage = view.findViewById(R.id.image_fullscreen_image);

        Glide
                .with(mContext)
                .load(list_of_images.get(position).getImage_Path())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_hourglass)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(fullscreenImage);
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (container.getParent() != null) {
            ((ViewPager) container).removeView((View) object);
        }
    }

    private void hideSystemUI() {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void findButtons(View view) {

        upload_button = (ImageView) view.findViewById(R.id.fullscreen_image_nav_bar_upload_or_download);
        upload_button.setImageResource(R.drawable.ic_cloud_upload);
        share_button = (ImageView) view.findViewById(R.id.fullscreen_image_nav_bar_share);
        like_button = (ImageView) view.findViewById(R.id.fullscreen_image_nav_bar_like);
        delete_button = (ImageView) view.findViewById(R.id.fullscreen_image_nav_bar_remove);
        info_button = (ImageView) view.findViewById(R.id.fullscreen_image_nav_bar_info);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_circular);
        back_arrow = (ImageView) view.findViewById(R.id.fullscreen_image_toolbar_back_arrow);
        progressBar.setVisibility(View.GONE);
    }

    private void button_functionality(final int position) {
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(list_of_images.get(position));
            }
        });

        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Temporary
                Toast.makeText(mContext,
                        "The image name is: " + list_of_images.get(position).getImage_Name() +
                                "\n" + "The image path is: " + list_of_images.get(position).getImage_Path()
                                + "\n" + "The image resolution is: " + list_of_images.get(position).getResolution()
                                + "\n" + "The image size is: " + list_of_images.get(position).getSize()                  //Fix the size issue
                                + "\n" + "The date added was: " + list_of_images.get(position).getDate_Added()        //Fix the date added
                        , Toast.LENGTH_LONG).show();
            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });
    }

    private void uploadImage(image_info imageFile) {
        progressBar.setVisibility(View.VISIBLE);
        final String image_name = imageFile.getImage_Name();
        final String image_type = imageFile.getType();
        Uri imageURI = Uri.fromFile(new File(imageFile.getImage_Path()));
        final String image_size = imageFile.getSize();
        final String date_Added = imageFile.getDate_Added();
        final String image_res = imageFile.getResolution();

        final StorageReference fileRef = StorageRef.child(mUser.getUid() + "/" + System.currentTimeMillis() + "." + image_type);
        UploadTask uploadTask = fileRef.putFile(imageURI);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {
                    //Handler handler = new Handler();

                    Toast.makeText(mContext, "Image successfully uploaded", Toast.LENGTH_SHORT).show();
                    String downloadURL = task.getResult().toString();

                    image_info mImage_info = new image_info(image_name, downloadURL, image_size, date_Added, image_res, image_type);
                    String uploadId = DatabaseRef.push().getKey();
                    DatabaseRef.child(mUser.getUid() + "/" + uploadId).setValue(mImage_info);
                } else {
                    Toast.makeText(mContext, "Image upload is unsuccessful", Toast.LENGTH_SHORT).show();
                    //progressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = 10.00 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
            }
        });
    }
}