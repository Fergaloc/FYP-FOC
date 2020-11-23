package com.example.MyBleeds;

public class Bleed {

            private String bleedID;
            private String bleedName;
            private int bleedRating;

            //contsructor to retrieve the values

    public Bleed(){


    }

    public Bleed(String bleedID, String bleedName, int bleedRating) {
        this.bleedID = bleedID;
        this.bleedName  = bleedName;
        this.bleedRating = bleedRating;
    }

    public String getBleedIDID() {
        return bleedID;
    }

    public String getBleedName() {
        return bleedName;
    }

    public int getBleedRating() {
        return bleedRating;
    }
}
