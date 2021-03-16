package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class PatientSettingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";

    //Constant for photo select.
    private static final int PICK_IMAGE_REQUEST = 1;



    EditText editTextName;
    Button buttonUpdate,buttonDOBPicker,btnSettingsReturn;
    Spinner spinnerRegion, SpinnerpatientSeverity,spinnerCenter;
    TextView textViewDOB;

    ImageView imgInfo;
    DatabaseReference databasePatients;

    DatabaseReference databasepatient,databaseReferenceCounty, databaseReferenceSeverity,databaseReferenceCCC,databaseReferenceParentID;
    DatabaseReference dataRefName,dataRefDate,dataRefCounty, datarefImg,datarefImgs;


    ListView listViewArtists;

    FirebaseAuth mAuth;

    List<Patient> patients;

    ImageView profilepic;
    private Uri mImageUri;
    private StorageReference mStorageRef,newStorage;
    private DatabaseReference mDatebaseRef;



    String imageURL;
    String newImageURL;

    Uri uri;

    FirebaseStorage storage;

    StorageReference storageReferencere;

    StorageReference imagereference;

    String useURL;

    Uri uriConvert;

    String imgCheck, imgChecks;

    Context context;

    BottomNavigationView itemSelectedListener;



    //Variables to get image content.

    String existingURL;

//Data for user.


    String county;
    String Severity;
    String CCC;
    String existingparentID;





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
            btnSettingsReturn = (Button) findViewById(R.id.btnSettingsReturn);
        SpinnerpatientSeverity = (Spinner) findViewById(R.id.SpinnerpatientSeverity);
        buttonDOBPicker = (Button) findViewById(R.id.buttonDOBPicker);
        textViewDOB = (TextView) findViewById(R.id.textViewDOB);
        profilepic = (ImageView) findViewById(R.id.profilepic);
            itemSelectedListener = (BottomNavigationView) findViewById(R.id.bottom_navigation);
            spinnerCenter = (Spinner) findViewById(R.id.spCCC);
            imgInfo = (ImageView) findViewById(R.id.imgInfo);
            mStorageRef = FirebaseStorage.getInstance().getReference();
            editTextName.setFocusable(false);

        context = getApplicationContext();
        final String uid = FirebaseAuth.getInstance().getUid();

        String id = FirebaseAuth.getInstance().getUid();


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

            btnSettingsReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PatientSettingsActivity.this.onBackPressed();
                    finish();
                }
            });

//Adds the users data to the drop downs
            //Uses created spinners and value event listeners
            //https://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position

            databaseReferenceCounty = FirebaseDatabase.getInstance().getReference().child("patients").child(id).child("patientRegion");
            databaseReferenceCounty.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){

                        county = dataSnapshot.getValue().toString();


                        String existingcounty = county;
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.genres, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerRegion.setAdapter(adapter);
                        if (existingcounty != null) {
                            int spinnerPosition = adapter.getPosition(existingcounty);
                            spinnerRegion.setSelection(spinnerPosition);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReferenceSeverity = FirebaseDatabase.getInstance().getReference().child("patients").child(id).child("patientSeverity");
            databaseReferenceSeverity.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){

                        Severity = dataSnapshot.getValue().toString();

                        String existingSeverity = Severity;
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.patientSeverity, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpinnerpatientSeverity.setAdapter(adapter);
                        if (existingSeverity != null) {
                            int spinnerPosition = adapter.getPosition(existingSeverity);
                            SpinnerpatientSeverity.setSelection(spinnerPosition);
                        }


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



            databaseReferenceCCC = FirebaseDatabase.getInstance().getReference().child("patients").child(id).child("doctorID");
            databaseReferenceCCC.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists()) {


                        if (dataSnapshot.getValue(String.class).equals("U32N7b9ZetXeQtBx9o9YIZBI7yB2")) {

                            CCC = "Cork University Hospital (CUH)";
                            String existingCCC = CCC;
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.CCC, android.R.layout.simple_spinner_item);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCenter.setAdapter(adapter);
                            if (existingCCC != null) {
                                int spinnerPosition = adapter.getPosition(existingCCC);
                                spinnerCenter.setSelection(spinnerPosition);
                            }

                        }

                        if (dataSnapshot.getValue(String.class).equals("WdLiqbz0xFVc3D9iSFcK0hHtka32")) {

                            CCC = "Children’s Health Ireland (CHI) at Crumlin";
                            String existingCCC = CCC;
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.CCC, android.R.layout.simple_spinner_item);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCenter.setAdapter(adapter);
                            if (existingCCC != null) {
                                int spinnerPosition = adapter.getPosition(existingCCC);
                                spinnerCenter.setSelection(spinnerPosition);
                            }

                        }

                        if (dataSnapshot.getValue(String.class).equals("MlkCgTNliecZ5jS7lxzHHmaWedo1")) {

                            CCC = "St. James’s Hospital in Dublin";
                            String existingCCC = CCC;
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.CCC, android.R.layout.simple_spinner_item);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCenter.setAdapter(adapter);
                            if (existingCCC != null) {
                                int spinnerPosition = adapter.getPosition(existingCCC);
                                spinnerCenter.setSelection(spinnerPosition);
                            }

                        }

                        if (dataSnapshot.getValue(String.class).equals("")) {

                            CCC = "";
                            String existingCCC = CCC;
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.CCC, android.R.layout.simple_spinner_item);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCenter.setAdapter(adapter);
                            if (existingCCC != null) {
                                int spinnerPosition = adapter.getPosition(existingCCC);
                                spinnerCenter.setSelection(spinnerPosition);
                            }

                        }

                    }

                    }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //Gets parentID that user has
            databaseReferenceParentID = FirebaseDatabase.getInstance().getReference().child("patients").child(id).child("parentID");
            databaseReferenceParentID.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){

                        existingparentID = dataSnapshot.getValue(String.class);

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


                    String  imageurl = imgCheck;
                    if(TextUtils.isEmpty(imgCheck))
                    {
                        imageurl = "";
                    }




                    String name = editTextName.getText().toString();
                    String region = spinnerRegion.getSelectedItem().toString();
                    String DOB = textViewDOB.getText().toString();
                    String severity = SpinnerpatientSeverity.getSelectedItem().toString();
                    String parentID = existingparentID;
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


            itemSelectedListener.setVisibility(View.GONE);

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
                            finish();
                            return true;

                        case R.id.ic_search:
                            String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Intent intent = new Intent(getApplicationContext(), ViewAllBleeds.class);

                            intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            finish();
                            return true;

                        case R.id.ic_account:

                            String patientnEWs = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Intent intentSetting = new Intent(getApplicationContext(), PatientAccountMenu.class);

                            intentSetting.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

                            startActivity(intentSetting);
                            overridePendingTransition(0,0);
                            finish();
                            return true;

                    }

                    return false;
                }
            });



            imgInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(PatientSettingsActivity.this).setTitle("Care Center Information").setMessage("Please select which Center you attend, if you wish to not share your data please select none. For more information please see the FAQ section.").show();


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
        if (mImageUri != null)
        {
            newStorage = mStorageRef.child("images/" + uid);
            newStorage.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return newStorage.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {

                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();

                         imageURL = downloadUri.toString();

                        String uploadId = uid;

                        mDatebaseRef.setValue(imageURL);

                    } else
                    {
                        Toast.makeText(PatientSettingsActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
    private boolean updatePatient(String name, String region, String DOB, String severity, String imageurl,String parentID,String doctorID){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patients").child(uid);

        Patient patient = new Patient( name , region, DOB, severity,imageurl,parentID,doctorID);

        //overide with new patient
        databaseReference.setValue(patient);

        return true;

    }




}
