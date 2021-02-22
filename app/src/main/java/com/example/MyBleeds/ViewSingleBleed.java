package com.example.MyBleeds;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewSingleBleed extends AppCompatActivity {

    public static final String BLEED_NAME = "bleedname";
    public static final String BLEED_ID = "bleedID";
    public static final String BLEED_SIDE = "bleedside";
    public static final String BLEED_SEVERITY = "bleedseverity";
    public static final String BLEED_CAUSE = "bleedcause";
    public static final String BLEED_DATE = "bleeddate";
    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientID";


    public static final String DOCTOR_ID = "doctorid";

    FirebaseAuth mAuth;

    TextView textViewDateBleed,textViewSeverityBleed, textViewLocationBleed, textViewCauseBleed, textViewSideBleed;

    TextView textViewShowLocation, textViewShowCause, textViewShowSide, textViewShowDate, textViewShowSeverity,txtPic;

    Button buttonReturn,buttonedit,buttonDelete;

    DatabaseReference databaseTreatment,databaseImage;

    ListView listViewTreatment;

    ImageView imgPic;

    Query query;

    String bleedIntentID;

    List<Treatment> treatments;

    private String imgCheck;
    private Uri uriConvert;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_bleed);
        mAuth = FirebaseAuth.getInstance();

        buttonDelete = (Button) findViewById(R.id.btnDelete);

        buttonDelete.setVisibility(View.GONE);


        textViewShowCause = (TextView) findViewById(R.id.textViewShowCause);
        textViewShowLocation = (TextView) findViewById(R.id.textViewShowLocation);
        textViewShowDate = (TextView) findViewById(R.id.textViewShowDate);
        textViewShowSeverity = (TextView) findViewById(R.id.textViewShowSeverity);
        textViewShowSide = (TextView) findViewById(R.id.textViewShowSide);
        listViewTreatment =(ListView) findViewById(R.id.ListViewTreatmentEdit);
        buttonedit = (Button) findViewById(R.id.editBleedButton);



        //For image
        txtPic = (TextView) findViewById(R.id.txtPic);
        imgPic = (ImageView) findViewById(R.id.imgPic);
        txtPic.setVisibility(View.GONE);
        imgPic.setVisibility(View.GONE);

        listViewTreatment.setFocusable(false);

        //Hide edit bleed button for doctors
        buttonedit.setVisibility(View.GONE);

        //gets bleed data from previous page and displays it
        Intent intent = getIntent();
        String Bleedid = intent.getStringExtra(ViewPatientBleeds.BLEED_ID);
        String Bleedname = intent.getStringExtra(ViewPatientBleeds.BLEED_NAME);
        String Bleedseverity = intent.getStringExtra(ViewPatientBleeds.BLEED_SEVERITY);
        String Bleedside = intent.getStringExtra(ViewPatientBleeds.BLEED_SIDE);
        String Bleedcause = intent.getStringExtra(ViewPatientBleeds.BLEED_CAUSE);
        String Bleeddate = intent.getStringExtra(ViewPatientBleeds.BLEED_DATE);
        String PatientName = intent.getStringExtra(ViewPatientBleeds.PATIENT_NAME);
        String PatientID = intent.getStringExtra(ViewPatientBleeds.PATIENT_ID);



        databaseImage = FirebaseDatabase.getInstance().getReference().child("bleedImages").child(Bleedid).child(Bleedid).child("imageUrl");
        databaseImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                imgCheck = dataSnapshot.getValue(String.class);
                if (dataSnapshot.exists()) {

                    uriConvert = Uri.parse(imgCheck);
                    Glide.with(getApplicationContext())
                            .load(uriConvert)
                            .into(imgPic);

                    txtPic.setVisibility(View.VISIBLE);
                    imgPic.setVisibility(View.VISIBLE);


                    imgPic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            openDialog();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



       // name = PatientName;
       // ID = PatientID;


        textViewShowCause.setText(Bleedcause);
        textViewShowLocation.setText(Bleedname);
        textViewShowDate.setText(Bleeddate);
        textViewShowSeverity.setText(Bleedseverity);
        textViewShowSide.setText(Bleedside);

        buttonReturn = (Button) findViewById(R.id.returnButton);


        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String doctor = FirebaseAuth.getInstance().getCurrentUser().getUid();
              //  Intent intent = new Intent(getApplicationContext(), ViewMyPatients.class);
               // intent.putExtra(DOCTOR_ID, mAuth.getCurrentUser().getUid());
               // startActivity(intent);

                ViewSingleBleed.this.onBackPressed();

            }
        });



        //Find treatment reference for bleed and querying location for our data.
        databaseTreatment = FirebaseDatabase.getInstance().getReference("treatment").child(Bleedid);
        query = databaseTreatment.child(Bleedid);
        treatments = new ArrayList<>();

        databaseTreatment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                treatments.clear();
                for(DataSnapshot treatmentSnapshot: dataSnapshot.getChildren()){
                    Treatment treatment = treatmentSnapshot.getValue(Treatment.class);
                    treatments.add(treatment);
                }

                TreatmentList treatmentListAdapter = new TreatmentList(ViewSingleBleed.this, treatments);
                listViewTreatment.setAdapter(treatmentListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public void openDialog() {

        // Create a custom dialog object
        final Dialog dialog = new Dialog(ViewSingleBleed.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.bleeddialog);
        // Set dialog title
        dialog.setTitle("Bleed");

        // set values for custom dialog components - text, image or button

        ImageView image = dialog.findViewById(R.id.dialog_imageview);
        Glide.with(getApplicationContext())
                .load(uriConvert)
                .into(image);

        dialog.show();


    }
}