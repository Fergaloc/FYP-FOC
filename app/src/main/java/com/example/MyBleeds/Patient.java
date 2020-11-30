package com.example.MyBleeds;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Patient {
    private String patientName;
    private String patientRegion;
    private String patientDOB;
    private String patientSeverity;


    public Patient() {
        //this constructor is required
    }

    public Patient(String patientName, String patientRegion,String patientDOB, String patientSeverity) {
        this.patientName = patientName;
        this.patientRegion= patientRegion;
        this.patientDOB = patientDOB;
        this.patientSeverity = patientSeverity;
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



}