package com.example.MyBleeds;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Patient {
    private String patientName;
    private String patientRegion;
    private String patientDOB;
    private String patientSeverity;
    private String imageURL;


    public Patient() {
        //this constructor is required
    }

    public Patient(String patientName, String patientRegion,String patientDOB, String patientSeverity, String imageURL) {
        this.patientName = patientName;
        this.patientRegion= patientRegion;
        this.patientDOB = patientDOB;
        this.patientSeverity = patientSeverity;
        this.imageURL = imageURL;
    }


    public String getPatientName() {
        return patientName;
    }

    public String getPatientRegion() {
        return patientRegion;
    }

    public String getPatientDOB() {
        return patientDOB;
    }

    public String getPatientSeverity() {
        return patientSeverity;
    }

    public String getImageURL() {
        return imageURL;
    }


    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }



}