package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

//Code taken from youtube tutorial - https://www.youtube.com/watch?v=2yepe4GYa90&list=PLk7v1Z2rk4hj6SDHf_YybDeVhUT9MXaj1&index=6

public class MainActivity extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";


    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerGenres;

    DatabaseReference databasePatients;

    ListView listViewArtists;

    FirebaseAuth mAuth;

    List<Patient> patients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        databasePatients = FirebaseDatabase.getInstance().getReference("patients");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        buttonAdd = (Button) findViewById(R.id.buttonAddArtist);

        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

        patients = new ArrayList<>();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtist();

            }
        });

         // will open seleceted patient when clicked
        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick (AdapterView <?> adapterView, View view,int i, long l){
        Patient patient = patients.get(i);

        Intent intent = new Intent(getApplicationContext(), AddBleedActivity.class);

        intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
        intent.putExtra(PATIENT_NAME, patient.getPatientName());

        startActivity(intent);

        }
    });

      listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
              Patient patient = patients.get(i);

              showUpdateDialog(mAuth.getCurrentUser().getUid(), patient.getPatientName());

              return false;
          }
      });

}

    @Override
    protected void onStart() {
        super.onStart();

        //Checks to see if user is logged in.
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LogInActivity.class));
        }
        databasePatients.addValueEventListener(new ValueEventListener() {
            @Override
            //once artist is added, it will fetch all artists
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                patients.clear();

                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
                    Patient patient = artistSnapshot.getValue(Patient.class);
                    patients.add(patient);
                }

                PatientList adapter = new PatientList(MainActivity.this, patients);
                listViewArtists.setAdapter(adapter);
            }
            @Override
            //if theres an error
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Bring us to updater after Long click
    private void showUpdateDialog(final String patientid, String patientName){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);

        dialogBuilder.setView(dialogView);


        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button updateButton = (Button) dialogView.findViewById(R.id.buttonUpdate);
        final Spinner spinnerGenres = (Spinner) dialogView.findViewById(R.id.spinnerGenres);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle("Updating Patient " + patientName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String region = spinnerGenres.getSelectedItem().toString();

                if(TextUtils.isEmpty(name)){
                    editTextName.setError("Name Required");
                    return;
                }

                updatePatient(name, region );

                alertDialog.dismiss();

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(patientid);
                
            }
        });

    }

    private void deleteArtist(String uid) {

        DatabaseReference drPatient = FirebaseDatabase.getInstance().getReference("patients").child(uid);

        DatabaseReference  drBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(uid);

        drPatient.removeValue();
        drBleeds.removeValue();

        Toast.makeText(this, "Patient is deleted", Toast.LENGTH_LONG).show();
    }

    private boolean updatePatient(String name, String region){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patients").child(uid);

        Patient patient = new Patient( name , region);

        //overide with new patient

        databaseReference.setValue(patient);

        return true;

    }


    private void addArtist(){
            String name = editTextName.getText().toString().trim();
            String region = spinnerGenres.getSelectedItem().toString();

            if(!TextUtils.isEmpty(name)){

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Patient patient = new Patient(name, region);

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                DatabaseReference usersRef = rootRef.child("patients");

                usersRef.child(uid).setValue(patient);


                Toast.makeText(this, "Patient Added", Toast.LENGTH_LONG).show();
        }else{
                Toast.makeText(this, "You should enter a name", Toast.LENGTH_LONG).show();
        }
}
        }
