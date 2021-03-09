package com.example.MyBleeds;


//codinginflow.com/tutorials/android/firebase-storage-upload-and-retrieve-images/part-3-image-upload

public class PatientUpload {
    private String uImageURL;


    public PatientUpload(){
        //Empty for Firebase logic
    }


    public PatientUpload(String ImageURL){

        uImageURL= ImageURL;
    }

    public  String getImageUrl() {
        return uImageURL;
    }
    public void setImageUrl(String imageUrl) {
        uImageURL = imageUrl;
    }


}


