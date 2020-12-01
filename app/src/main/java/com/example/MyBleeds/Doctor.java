package com.example.MyBleeds;

public class Doctor {
    private String doctorName;
    private String doctorHospital;

    public Doctor() {
    }


    public Doctor(String doctorName, String doctorHospital){
        this.doctorName = doctorName;
        this.doctorHospital = doctorHospital;

    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDoctorHospital() {
        return doctorHospital;
    }

}

