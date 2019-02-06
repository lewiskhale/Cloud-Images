package com.madebyk.android.cloudimages.data;

import android.print.PrintAttributes;

import java.io.Serializable;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class image_info implements Serializable {

    private String Image_Name = null;
    private String Image_Path;
    private String Size;
    private String Dated_Added;
    private String Resolution;
    private String type;
    private String key;

    public image_info() {
    }

    public image_info(String image_Name, String image_Path) {
        Image_Name = image_Name;
        Image_Path = image_Path;
    }

    public image_info(String image_Name, String image_Path, String size, String date_Added, String resolution, String type) {
        Image_Name = image_Name;
        Image_Path = image_Path;
        Size = size;
        Dated_Added = date_Added;
        Resolution = resolution;
        this.type = type;
    }

    public void setImage_Name(String image_Name) {
        Image_Name = image_Name;
    }

    public void setImage_Path(String image_Path) {
        Image_Path = image_Path;
    }

    public String getImage_Name() {
        return Image_Name;
    }

    public String getImage_Path() {
        return Image_Path;
    }

    public String getSize() { return Size; }

    public String getDate_Added() { return Dated_Added; }

    public String getResolution() {
        return Resolution;
    }

    public String getType() { return type; }

    public void setKey(String key){
        this.key = key;
    }

    public String getKey(){
        return key;
    }
}
