package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class newPatientSettingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";

    //Constant for photo select.
    private static final int PICK_IMAGE_REQUEST = 1;


    EditText editTextName;
    Button buttonUpdate, buttonHome, buttonLogOut,buttonDOBPicker;
    Spinner spinnerRegion, SpinnerpatientSeverity,spinnerCenter;
    TextView textViewDOB;

    DatabaseReference databasePatients;

    DatabaseReference databasepatient;
    DatabaseReference dataRefName,dataRefDate,datarefImg,databaseReference;


    ListView listViewArtists;
    ImageView imgInfo;
    FirebaseAuth mAuth;

    List<Patient> patients;

    ImageView profilepic;

    Uri uri;

    FirebaseStorage storage;

    String imageURL;

    StorageReference storageReferencere;

    StorageReference imagereference;

    String useURL;

    Uri uriConvert;

    String imgCheck,userCheck;

    Context context;


    private Uri mImageUri;
    private StorageReference mStorageRef,newStorage;
    private DatabaseReference mDatebaseRef;



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
     //   buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        SpinnerpatientSeverity = (Spinner) findViewById(R.id.SpinnerpatientSeverity);
        spinnerCenter = (Spinner) findViewById(R.id.spCCC);
        buttonDOBPicker = (Button) findViewById(R.id.buttonDOBPicker);
        textViewDOB = (TextView) findViewById(R.id.textViewDOB);
        profilepic = (ImageView) findViewById(R.id.profilepic);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imgInfo = (ImageView) findViewById(R.id.imgInfo);

        itemSelectedListener = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        itemSelectedListener.setVisibility(View.GONE);





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


                String  imageurl = imgCheck;
                if(TextUtils.isEmpty(imgCheck))
                {
                    imageurl = "";
                }




                String parentID = "";

                String doctorID = "";

                //if statement to determine the doctor id based on hospital
                if(spinnerCenter.getSelectedItem().toString().equals("Cork University Hospital (CUH)")){
                    doctorID = "U32N7b9ZetXeQtBx9o9YIZBI7yB2";
                }
                if  (spinnerCenter.getSelectedItem().toString().equals("Children’s Health Ireland (CHI) at Crumlin")){
                    doctorID = "WdLiqbz0xFVc3D9iSFcK0hHtka32";
                };
                if(spinnerCenter.getSelectedItem().toString().equals("St. James’s Hospital in Dublin")){
                    doctorID = "MlkCgTNliecZ5jS7lxzHHmaWedo1";
                }
                if(spinnerCenter.getSelectedItem().toString().equals("")){
                    doctorID = "";

                }


                if(TextUtils.isEmpty(name)){
                    editTextName.setError("Name Required");
                    return;
                }

                updatePatient(name, region, DOB, severity, imageurl,parentID, doctorID);
                itemSelectedListener.setVisibility(View.VISIBLE);
                Toast.makeText(newPatientSettingsActivity.this, "Profile Created", Toast.LENGTH_SHORT).show();



            }

        });




        //Logs out user and send them to the Log-in page.
      //  buttonLogOut.setOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View v) {
      //          FirebaseAuth.getInstance().signOut();
       //         finish();
       //         startActivity(new Intent(newPatientSettingsActivity.this, LogInActivity.class));
      //      }
      //  });


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
                        Intent intent = new Intent(getApplicationContext(), ViewBleeds.class);

                        intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.ic_account:

                        break;
                }

                return false;
            }
        });




        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(newPatientSettingsActivity.this).setTitle("Care Center Information").setMessage("Please select which Center you attend, if you wish to not share your data please select none. For more information please see the FAQ section.").show();


            }
        });


    }

    //https://www.youtube.com/watch?v=CQ5qcJetYAI
    //method to fetch image from gallery
    private void choosePicture() {
        //https://www.youtube.com/watch?v=gqIWrNitbbk
        //Image chooser.
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode== PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            mImageUri = data.getData();
            Glide.with(this).load(mImageUri).into(profilepic);
            uploadPicture();
        }
    }

    //Provided by Android to upload file to Firebase
    private void uploadPicture() {

        final String uid = FirebaseAuth.getInstance().getUid();


        mDatebaseRef = FirebaseDatabase.getInstance().getReference("patients").child(uid).child("imageURL");
        //Code to upload image to firebase storage and firebase Database.
        //https://www.youtube.com/watch?v=lPfQN-Sfnjw&t=1034s
        if (mImageUri != null) {
            newStorage = mStorageRef.child("images/" + uid);
            newStorage.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return newStorage.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {

                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        imageURL = downloadUri.toString();

                        String uploadId = uid;

                        mDatebaseRef.setValue(imageURL);

                    } else {
                        Toast.makeText(newPatientSettingsActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

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
    private boolean updatePatient(String name, String region, String DOB, String severity, String imageurl, String parentID,String doctorID){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patients").child(uid);

        Patient patient = new Patient( name , region, DOB, severity,imageurl, parentID,doctorID);

        //overide with new patient

        databaseReference.setValue(patient);

        return true;

    }


}
