package com.example.MyBleeds;


//codinginflow.com/tutorials/android/firebase-storage-upload-and-retrieve-images/part-3-image-upload

public class BleedUpload {
    private String mImageURL;


    public BleedUpload(){
        //Empty for Firebase logic
    }


    public BleedUpload(String ImageURL){

      mImageURL= ImageURL;
    }

    public String getImageUrl() {
        return mImageURL;
    }
    public void setImageUrl(String imageUrl) {
        mImageURL = imageUrl;
    }


}


