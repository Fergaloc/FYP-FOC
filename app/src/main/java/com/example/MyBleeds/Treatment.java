package com.example.MyBleeds;

public class Treatment {

    private String treatmentID;
    private String treatmentReason;
    private String treatmentType;
    private String treatmentDate;


   public Treatment(){

   }


   public Treatment(String treatmentID,String treatmentReason, String treatmentType, String treatmentDate) {

       this.treatmentID=treatmentID;
       this.treatmentReason=treatmentReason;
       this.treatmentType=treatmentType;
       this.treatmentDate=treatmentDate;
   }

   public String getTreatmentID() {
       return treatmentID;
   }

    public String getTreatmentReason() {
        return treatmentReason;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public String getTreatmentDate() {
        return treatmentDate;
    }




}



