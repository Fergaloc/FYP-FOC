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
    private String parentID;



    public Patient() {

    }

    public Patient(String patientName, String patientRegion,String patientDOB, String patientSeverity, String imageURL,String parentID) {
        this.patientName = patientName;
        this.patientRegion= patientRegion;
        this.patientDOB = patientDOB;
        this.patientSeverity = patientSeverity;
        this.imageURL = imageURL;
        this.parentID = parentID;
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

    public String getParentID() {
        return parentID;
    }




    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setPatientDOB(String patientDOB) {
        this.patientDOB = patientDOB;
    }

    public void setPatientSeverity(String patientSeverity) {
        this.patientSeverity = patientSeverity;
    }

    public void setPatientRegion(String patientRegion) {
        this.patientRegion = patientRegion;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }
}

