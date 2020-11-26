package com.example.MyBleeds;

import java.util.Date;

public class Bleed {

            private String bleedID;
            private String bleedName;
            private int bleedRating;
            private String bleedSide;
            private String bleedSeverity;
            private String bleedCause;



            //contsructor to retrieve the values

    public Bleed(){


    }

    public Bleed(String bleedID, String bleedName, int bleedRating, String bleedSide, String bleedSeverity, String bleedCause) {
        this.bleedID = bleedID;
        this.bleedName  = bleedName;
        this.bleedRating = bleedRating;
        this.bleedSide = bleedSide;
        this.bleedSeverity = bleedSeverity;
        this.bleedCause = bleedCause;
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

    public String getBleedSide() { return  bleedSide; }

    public String getBleedSeverity() { return  bleedSeverity; }

    public String getBleedCause() { return  bleedCause; }

    }

