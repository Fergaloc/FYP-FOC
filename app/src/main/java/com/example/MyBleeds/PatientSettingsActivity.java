package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PatientSettingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";


    EditText editTextName;
    Button buttonUpdate, buttonHome, buttonLogOut,buttonDOBPicker;
    Spinner spinnerRegion, SpinnerpatientSeverity;
    TextView textViewDOB;

    DatabaseReference databasePatients;

    DatabaseReference databasepatient;
    DatabaseReference dataRefName,dataRefDate,dataRefCounty;


    ListView listViewArtists;

    FirebaseAuth mAuth;

    List<Patient> patients;

    ImageView profilepic;

    Uri uri;

    FirebaseStorage storage;

    StorageReference storageReferencere;

    StorageReference imagereference;





        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_settings);
        mAuth = FirebaseAuth.getInstance();

        databasepatient = FirebaseDatabase.getInstance().getReference("patients").child("U32N7b9ZetXeQtBx9o9YIZBI7yB2");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextFirstName);
        spinnerRegion = (Spinner) findViewById(R.id.spinnerRegion);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdatePatient);
        buttonHome = (Button) findViewById(R.id.buttonHome);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        SpinnerpatientSeverity = (Spinner) findViewById(R.id.SpinnerpatientSeverity);
        buttonDOBPicker = (Button) findViewById(R.id.buttonDOBPicker);
        textViewDOB = (TextView) findViewById(R.id.textViewDOB);
        profilepic = (ImageView) findViewById(R.id.profilepic);


        String uid = FirebaseAuth.getInstance().getUid();

        dataRefName = databasepatient.child(uid).child("patientName");

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
                    String imageurl =  storageReferencere.child("images/" + profilePicID).toString();

                    if(TextUtils.isEmpty(name)){
                        editTextName.setError("Name Required");
                        return;
                    }

                    updatePatient(name, region, DOB, severity, imageurl);


                }

            });

        //Bringing user to home page, doesnt save data
        buttonHome.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Intent intentSettings = new Intent(getApplicationContext(), Patient_HomeActivity.class);

            intentSettings.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

            startActivity(intentSettings);

        }

    });


        //Logs out user and send them to the Log-in page.
            buttonLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(PatientSettingsActivity.this, LogInActivity.class));
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
    private boolean updatePatient(String name, String region, String DOB, String severity, String imageurl){

        String docID = "U32N7b9ZetXeQtBx9o9YIZBI7yB2";

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patients").child(docID).child(uid);

        Patient patient = new Patient( name , region, DOB, severity,imageurl);

        //overide with new patient

        databaseReference.setValue(patient);

        return true;

    }

}
