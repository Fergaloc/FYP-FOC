package com.example.MyBleeds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ViewSingleBleed extends AppCompatActivity {

    public static final String BLEED_NAME = "bleedname";

    public static final String BLEED_SIDE = "bleedside";
    public static final String BLEED_SEVERITY = "bleedseverity";
    public static final String BLEED_CAUSE = "bleedcause";
    public static final String BLEED_DATE = "bleeddate";


    public static final String DOCTOR_ID = "doctorid";

    FirebaseAuth mAuth;

    TextView textViewDateBleed,textViewSeverityBleed, textViewLocationBleed, textViewCauseBleed, textViewSideBleed;

    TextView textViewShowLocation, textViewShowCause, textViewShowSide, textViewShowDate, textViewShowSeverity;

    Button buttonReturn;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_bleed);
        mAuth = FirebaseAuth.getInstance();

        textViewShowCause = (TextView) findViewById(R.id.textViewShowCause);
        textViewShowLocation = (TextView) findViewById(R.id.textViewShowLocation);
        textViewShowDate = (TextView) findViewById(R.id.textViewShowDate);
        textViewShowSeverity = (TextView) findViewById(R.id.textViewShowSeverity);
        textViewShowSide = (TextView) findViewById(R.id.textViewShowSide);



        //gets bleed data from previous page and displays it
        Intent intent = getIntent();
        String Bleedname = intent.getStringExtra(ViewPatientBleeds.BLEED_NAME);
        String Bleedseverity = intent.getStringExtra(ViewPatientBleeds.BLEED_SEVERITY);
        String Bleedside = intent.getStringExtra(ViewPatientBleeds.BLEED_SIDE);
        String Bleedcause = intent.getStringExtra(ViewPatientBleeds.BLEED_CAUSE);
        String Bleeddate = intent.getStringExtra(ViewPatientBleeds.BLEED_DATE);



        textViewShowCause.setText(Bleedcause);
        textViewShowLocation.setText(Bleedname);
        textViewShowDate.setText(Bleeddate);
        textViewShowSeverity.setText(Bleedseverity);
        textViewShowSide.setText(Bleedside);

        buttonReturn = (Button) findViewById(R.id.returnButton);

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doctor = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intent = new Intent(getApplicationContext(), ViewMyPatients.class);

                intent.putExtra(DOCTOR_ID, mAuth.getCurrentUser().getUid());

                startActivity(intent);
            }
        });



    }
}