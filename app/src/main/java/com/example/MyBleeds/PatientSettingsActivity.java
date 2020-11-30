package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;

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

    ListView listViewArtists;

    FirebaseAuth mAuth;

    List<Patient> patients;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_settings);
        mAuth = FirebaseAuth.getInstance();

        databasePatients = FirebaseDatabase.getInstance().getReference("patients");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextFirstName);
        spinnerRegion = (Spinner) findViewById(R.id.spinnerRegion);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdatePatient);
        buttonHome = (Button) findViewById(R.id.buttonHome);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        SpinnerpatientSeverity = (Spinner) findViewById(R.id.SpinnerpatientSeverity);
        buttonDOBPicker = (Button) findViewById(R.id.buttonDOBPicker);
        textViewDOB = (TextView) findViewById(R.id.textViewDOB);

        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

        patients = new ArrayList<>();
            //updating a patient settings + ADD INTENT to bring to main page
            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = editTextName.getText().toString().trim();
                    String region = spinnerRegion.getSelectedItem().toString();
                    String DOB = textViewDOB.getText().toString();
                    String severity = SpinnerpatientSeverity.getSelectedItem().toString();

                    if(TextUtils.isEmpty(name)){
                        editTextName.setError("Name Required");
                        return;
                    }

                    updatePatient(name, region, DOB, severity);

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


            //https://www.youtube.com/watch?v=AdTzD96AhE0
            // code from an online tutorial that shows a date picker once the button is picked.
            buttonDOBPicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog();
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





    private boolean updatePatient(String name, String region, String DOB, String severity){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patients").child(uid);

        Patient patient = new Patient( name , region, DOB, severity);

        //overide with new patient

        databaseReference.setValue(patient);

        return true;

    }

}
