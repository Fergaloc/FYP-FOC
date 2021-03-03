package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientSettingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";


    EditText editTextName;
    Button buttonUpdate,buttonDOBPicker;
    Spinner spinnerRegion, SpinnerpatientSeverity,spinnerCenter;
    TextView textViewDOB;

    DatabaseReference databasePatients;

    DatabaseReference databasepatient;
    DatabaseReference dataRefName,dataRefDate,dataRefCounty, datarefImg;


    ListView listViewArtists;

    FirebaseAuth mAuth;

    List<Patient> patients;

    ImageView profilepic;

    Uri uri;

    FirebaseStorage storage;

    StorageReference storageReferencere;

    StorageReference imagereference;

    String useURL;

    Uri uriConvert;

    String imgCheck;

    Context context;

    BottomNavigationView itemSelectedListener;






        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_settings);
        mAuth = FirebaseAuth.getInstance();

        databasepatient = FirebaseDatabase.getInstance().getReference("patients");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextFirstName);
        spinnerRegion = (Spinner) findViewById(R.id.spinnerRegion);
        buttonUpdate = (Button) findViewById(R.id.btnSaveuid);
        SpinnerpatientSeverity = (Spinner) findViewById(R.id.SpinnerpatientSeverity);
        buttonDOBPicker = (Button) findViewById(R.id.buttonDOBPicker);
        textViewDOB = (TextView) findViewById(R.id.textViewDOB);
        profilepic = (ImageView) findViewById(R.id.profilepic);
            itemSelectedListener = (BottomNavigationView) findViewById(R.id.bottom_navigation);
            spinnerCenter = (Spinner) findViewById(R.id.spCCC);

            editTextName.setFocusable(false);

        context = getApplicationContext();
        String uid = FirebaseAuth.getInstance().getUid();

        dataRefName = databasepatient.child(uid).child("patientName");

        datarefImg = FirebaseDatabase.getInstance().getReference("patients").child(uid).child("imageURL");

        //If statement, checks if user has a profile pic and displays if it does.
        datarefImg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imgCheck = dataSnapshot.getValue(String.class);

                if(dataSnapshot.exists()){

                    uriConvert = Uri.parse(imgCheck);

                    Glide.with(context)
                            .load(uriConvert)
                            .into(profilepic);
                } else{


                    profilepic.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Getting data and setting the text views data for it.
            dataRefName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.getValue().toString();
                    editTextName.setText(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            dataRefDate = databasepatient.child(uid).child("patientDOB");
            dataRefDate.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String date = dataSnapshot.getValue().toString();
                    textViewDOB.setText(date);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        storage = FirebaseStorage.getInstance();
        storageReferencere = storage.getReference();

        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

            String profilePicID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        patients = new ArrayList<>();
            //updating a patient settings + ADD INTENT to bring to main page
            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Sets name of image in storage as same as user ID
                    final String profilePicID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    String name = editTextName.getText().toString().trim();
                    String region = spinnerRegion.getSelectedItem().toString();
                    String DOB = textViewDOB.getText().toString();
                    String severity = SpinnerpatientSeverity.getSelectedItem().toString();
                   String imageurl =  useURL.toString();
                 //   String imageurl = uriConvert != null ? uriConvert.toString() : null;
                    String parentID = "";
                    String doctorID = "";



                    //if statement to determine the doctor id based on hospital
                    if(spinnerCenter.getSelectedItem().toString().equals("The Coagulation Centre in Cork University Hospital (CUH)")){
                        doctorID = "U32N7b9ZetXeQtBx9o9YIZBI7yB2";
                    }
                    if  (spinnerCenter.getSelectedItem().toString().equals("Children’s Health Ireland (CHI) at Crumlin")){
                        doctorID = "WdLiqbz0xFVc3D9iSFcK0hHtka32";
                    };



                    if(TextUtils.isEmpty(name)){
                        editTextName.setError("Name Required");
                        return;
                    }

                    updatePatient(name, region, DOB, severity, imageurl,parentID,doctorID);
                    Toast.makeText(PatientSettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();


                }

            });




            profilepic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choosePicture();
                }
            });


            //https://www.youtube.com/watch?v=AdTzD96AhE0
            // code from an online tutorial that shows a date picker once the button is picked.
            buttonDOBPicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog();
                }
            });


            //Bottom navigation switch case to decide location based upon selected item
            itemSelectedListener.setSelectedItemId(R.id.ic_account);

            itemSelectedListener.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.ic_home:
                            String patientnEW = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Intent intentSettings = new Intent(getApplicationContext(), Patient_HomeActivity.class);

                            intentSettings.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                            startActivity(intentSettings);
                            overridePendingTransition(0,0);
                            return true;

                        case R.id.ic_search:
                            String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Intent intent = new Intent(getApplicationContext(), ViewAllBleeds.class);

                            intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;

                        case R.id.ic_account:

                            String patientnEWs = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Intent intentSetting = new Intent(getApplicationContext(), PatientAccountMenu.class);

                            intentSetting.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

                            startActivity(intentSetting);
                            overridePendingTransition(0,0);
                            return true;

                    }

                    return false;
                }
            });












        }

//https://www.youtube.com/watch?v=CQ5qcJetYAI
    //method to fetch image from gallery
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK && data.getData()!=null){
            uri = data.getData();
            profilepic.setImageURI(uri);
            uploadPicture();
            getUrlAsync(useURL);

        }
    }

    //Provided by Android to upload file to Firebase
    private void uploadPicture() {

            final String profilePicID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final StorageReference riversRef = storageReferencere.child("images/" + profilePicID );

    UploadTask uploadTask = riversRef.putFile(uri);
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri downloadUrl)
                            {
                                String url = uri.toString();
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }



    //https://www.youtube.com/watch?v=AdTzD96AhE0
    // code from an online tutorial that shows a date picker once the button is picked.
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this ,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date =  dayOfMonth + "/" + (month+1) +  "/ "+ year;
        textViewDOB.setText(date);
    }

    //
    private boolean updatePatient(String name, String region, String DOB, String severity, String imageurl,String parentID,String doctorID){

        String docID = "U32N7b9ZetXeQtBx9o9YIZBI7yB2";

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patients").child(uid);

        Patient patient = new Patient( name , region, DOB, severity,imageurl,parentID,doctorID);

        //overide with new patient

        databaseReference.setValue(patient);

        return true;

    }

    // Calls the server to securely obtain an unguessable download Url
    private void getUrlAsync (String date){
        // Points to the root reference
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final String profilePicID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final StorageReference riversRef = storageReferencere.child("images/" + profilePicID );

        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                downloadUrl.toString();
                useURL = downloadUrl.toString();

            }
        });
    }

}
