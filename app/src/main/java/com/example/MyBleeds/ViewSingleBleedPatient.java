package com.example.MyBleeds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ViewSingleBleedPatient extends AppCompatActivity {

    public static final String BLEED_NAME = "bleedname";
    public static final String BLEED_ID = "bleedid";

    public static final String BLEED_SIDE = "bleedside";
    public static final String BLEED_SEVERITY = "bleedseverity";
    public static final String BLEED_CAUSE = "bleedcause";
    public static final String BLEED_DATE = "bleeddate";


    public static final String PATIENT_ID = "patientid";

    FirebaseAuth mAuth;

    TextView textViewDateBleed,textViewSeverityBleed, textViewLocationBleed, textViewCauseBleed, textViewSideBleed;

    TextView textViewShowLocation, textViewShowCause, textViewShowSide, textViewShowDate, textViewShowSeverity,textViewShowID;

    Button buttonReturn,buttonEdit;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_bleed);
        mAuth = FirebaseAuth.getInstance();


        textViewShowID = (TextView) findViewById(R.id.textViewShowID);
        textViewShowCause = (TextView) findViewById(R.id.textViewShowCause);
        textViewShowLocation = (TextView) findViewById(R.id.textViewShowLocation);
        textViewShowDate = (TextView) findViewById(R.id.textViewShowDate);
        textViewShowSeverity = (TextView) findViewById(R.id.textViewShowSeverity);
        textViewShowSide = (TextView) findViewById(R.id.textViewShowSide);

        buttonEdit = (Button) findViewById(R.id.editBleedButton) ;



        //gets bleed data from previous page and displays it
        Intent intent = getIntent();
        final String Bleedid = intent.getStringExtra(ViewBleeds.BLEED_ID);
        final String Bleedname = intent.getStringExtra(ViewBleeds.BLEED_NAME);
        final String Bleedseverity = intent.getStringExtra(ViewBleeds.BLEED_SEVERITY);
        final String Bleedside = intent.getStringExtra(ViewBleeds.BLEED_SIDE);
        final String Bleedcause = intent.getStringExtra(ViewBleeds.BLEED_CAUSE);
        final String Bleeddate = intent.getStringExtra(ViewBleeds.BLEED_DATE);




        textViewShowID.setText(Bleedid);
        textViewShowID.setVisibility(View.GONE);

        textViewShowCause.setText(Bleedcause);
        textViewShowLocation.setText(Bleedname);
        textViewShowDate.setText(Bleeddate);
        textViewShowSeverity.setText(Bleedseverity);
        textViewShowSide.setText(Bleedside);

        buttonReturn = (Button) findViewById(R.id.returnButton);



        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intent = new Intent(getApplicationContext(), ViewBleeds.class);

                intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

                startActivity(intent);
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editBleeds.class);

                intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

                intent.putExtra(BLEED_ID, Bleedid);

                intent.putExtra(BLEED_NAME, Bleedname);

                intent.putExtra(BLEED_SIDE, Bleedside);

                intent.putExtra(BLEED_SEVERITY, Bleedseverity);

                intent.putExtra(BLEED_CAUSE, Bleedcause);

                intent.putExtra(BLEED_DATE, Bleeddate);


                startActivity(intent);

            }
        });


    }
}