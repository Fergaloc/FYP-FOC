package com.example.MyBleeds;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Parent {
    private String parentName;
    private String parentID;



    public Parent() {

    }

    public Parent(String parentName, String parentID) {
        this.parentName = parentName;
        this.parentID = parentID;
    }


    public String getParentName() {
        return parentName;
    }

    public String getParentID() {
        return parentID;
    }



    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }}


