package com.example.MyBleeds;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class ViewSingleBleedPatient extends AppCompatActivity implements DeleteDialog.DeleteDialogListener {

    public static final String BLEED_NAME = "bleedname";
    public static final String BLEED_ID = "bleedid";

    public static final String BLEED_SIDE = "bleedside";
    public static final String BLEED_SEVERITY = "bleedseverity";
    public static final String BLEED_CAUSE = "bleedcause";
    public static final String BLEED_DATE = "bleeddate";


    public static final String PATIENT_ID = "patientid";

    FirebaseAuth mAuth;

    TextView textViewDateBleed,textViewSeverityBleed, textViewLocationBleed, textViewCauseBleed, textViewSideBleed;

    TextView textViewShowLocation, textViewShowCause, textViewShowSide, textViewShowDate, textViewShowSeverity,textViewShowID,txtPic;

    Button buttonReturn,buttonEdit,buttonDelete;

    DatabaseReference databaseTreatment,databaseImage;

    ListView listViewTreatment;

    ImageView imgPic;


    Query query;

    String bleedIntentID;

    List<Treatment> treatments;

    private String imgCheck;
    private Uri uriConvert,UriConverts;

    ImageView imgDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_bleed);
        mAuth = FirebaseAuth.getInstance();



        textViewShowCause = (TextView) findViewById(R.id.textViewShowCause);
        textViewShowLocation = (TextView) findViewById(R.id.textViewShowLocation);
        textViewShowDate = (TextView) findViewById(R.id.textViewShowDate);
        textViewShowSeverity = (TextView) findViewById(R.id.textViewShowSeverity);
        textViewShowSide = (TextView) findViewById(R.id.textViewShowSide);
        listViewTreatment =(ListView) findViewById(R.id.ListViewTreatmentEdit);
        buttonDelete = (Button) findViewById(R.id.btnDelete);




        //For image
        txtPic = (TextView) findViewById(R.id.txtPic);
        imgPic = (ImageView) findViewById(R.id.imgPic);
        txtPic.setVisibility(View.GONE);
        imgPic.setVisibility(View.GONE);


        buttonEdit = (Button) findViewById(R.id.editBleedButton) ;


        listViewTreatment.setFocusable(false);



        //gets bleed data from previous page and displays it
        Intent intent = getIntent();
        final String Bleedid = intent.getStringExtra(ViewBleeds.BLEED_ID);
        final String Bleedname = intent.getStringExtra(ViewBleeds.BLEED_NAME);
        final String Bleedseverity = intent.getStringExtra(ViewBleeds.BLEED_SEVERITY);
        final String Bleedside = intent.getStringExtra(ViewBleeds.BLEED_SIDE);
        final String Bleedcause = intent.getStringExtra(ViewBleeds.BLEED_CAUSE);
        final String Bleeddate = intent.getStringExtra(ViewBleeds.BLEED_DATE);







        databaseImage = FirebaseDatabase.getInstance().getReference().child("bleedImages").child(Bleedid).child(Bleedid).child("imageUrl");
        databaseImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                imgCheck = dataSnapshot.getValue(String.class);
                if(dataSnapshot.exists()){

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
                Intent intent = new Intent(getApplicationContext(), ViewAllBleeds.class);

                intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

                startActivity(intent);
            }
        });



        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDeleteDialog();


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

                TreatmentList treatmentListAdapter = new TreatmentList(ViewSingleBleedPatient.this, treatments);
                listViewTreatment.setAdapter(treatmentListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void openDialog() {

        // Create a custom dialog object
        final Dialog dialog = new Dialog(ViewSingleBleedPatient.this);
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


    public void openDeleteDialog(){

        //Opens Dialog and brings bleed id
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.show(getSupportFragmentManager(), "Delete Dialog");

        Intent intent = getIntent();
        String BleedID = intent.getStringExtra(ViewSingleBleedPatient.BLEED_ID);

        Bundle bundle = new Bundle();
        bundle.putString("BLEED_ID", BleedID);
       deleteDialog.setArguments(bundle);

    }


    @Override
    public void applyData() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}