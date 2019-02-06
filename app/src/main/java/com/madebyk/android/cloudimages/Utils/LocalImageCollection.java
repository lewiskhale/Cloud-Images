package com.madebyk.android.cloudimages.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.madebyk.android.cloudimages.data.image_info;

import java.util.ArrayList;
import java.util.List;

public class LocalImageCollection extends AppCompatActivity {

    public static ArrayList<image_info> readImages(Context context) {
        ArrayList<image_info> images = new ArrayList<>();
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_name
                , column_index_data_size, column_index_data_date_added
                , column_index_data_height, column_index_data_width
                , column_index_data_type;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        //Order of list
        String orderBy = MediaStore.Images.Media.DATE_ADDED;

        String[] projection = {MediaStore.MediaColumns.DATA
                , MediaStore.Images.Media.BUCKET_DISPLAY_NAME
                , MediaStore.MediaColumns.TITLE
                , MediaStore.MediaColumns.SIZE
                , MediaStore.MediaColumns.DATE_ADDED
                , MediaStore.MediaColumns.WIDTH
                , MediaStore.MediaColumns.HEIGHT
                , MediaStore.MediaColumns.MIME_TYPE};

        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_name = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE);
        column_index_data_size = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);
        column_index_data_date_added = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED);
        column_index_data_height = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT);
        column_index_data_width = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH);
        column_index_data_type = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE);

        while (cursor.moveToNext()) {
            String image_path = cursor.getString(column_index_data);
            String image_name = cursor.getString(column_index_name);
            String image_size = cursor.getString(column_index_data_size);
            String image_date_added = cursor.getString(column_index_data_date_added);
            String image_height = cursor.getString(column_index_data_height);
            String image_width = cursor.getString(column_index_data_width);
            String image_type = cursor.getString(column_index_data_type);

            images.add(new image_info(image_name, image_path, image_size, image_date_added, image_height+"x"+image_width, image_type));
        }
        return images;
    }

}
