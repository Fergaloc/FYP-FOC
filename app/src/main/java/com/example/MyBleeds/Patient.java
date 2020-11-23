package com.example.MyBleeds;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Patient {
    private String patientName;
    private String patientRegion;


    public Patient() {
        //this constructor is required
    }

    public Patient(String patientName, String patientRegion) {
        this.patientName = patientName;
        this.patientRegion= patientRegion;

    }


    public String getPatientName() {
        return patientName;
    }

    public String getPatientRegion() {
        return patientRegion;
    }


}