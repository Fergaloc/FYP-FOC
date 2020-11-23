package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddBleedActivity extends AppCompatActivity {


    TextView textViewArtistsName;
    EditText editTextBleedLocation;
    SeekBar seekBarRating;
    ListView listViewTracks;
    Button buttonAddTrack;


    DatabaseReference databaseBleeds;

    List<Bleed> bleeds;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        //gets button from xml
        textViewArtistsName = (TextView) findViewById(R.id.textViewArtistName);
        editTextBleedLocation = (EditText) findViewById(R.id.editTextName);
        seekBarRating = (SeekBar) findViewById((R.id.seekBarRating));
        listViewTracks = (ListView) findViewById(R.id.listViewTracks);
        buttonAddTrack = (Button) findViewById(R.id.buttonAddTrack);


        //to get artist name + id, it displays the artists name on the track page.

        Intent intent = getIntent();

        bleeds = new ArrayList<>();

        String id = intent.getStringExtra(PatientSettingsActivity.PATIENT_ID);
        String name = intent.getStringExtra(PatientSettingsActivity.PATIENT_NAME);

        textViewArtistsName.setText(name);

        //create new node in tracks node, the tracks id will match with the artist id
        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(id);

        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBleed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // code to detect changes in values

        databaseBleeds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bleeds.clear();
                for(DataSnapshot trackSnapshot: dataSnapshot.getChildren()){
                    Bleed bleed = trackSnapshot.getValue(Bleed.class);
                    bleeds.add(bleed);
                }
                BleedList bleedListAdapter = new BleedList(AddBleedActivity.this, bleeds);
                listViewTracks.setAdapter(bleedListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Code taken from https://www.youtube.com/watch?v=EM2x33g4syY&list=PLk7v1Z2rk4hj6SDHf_YybDeVhUT9MXaj1
    //methods were related to music tracks, must change to project specific name.
    private void saveBleed(){

        String bleedLocation  = editTextBleedLocation.getText().toString().trim();
        int rating = seekBarRating.getProgress();

        if(!TextUtils.isEmpty(bleedLocation )){
            String id = databaseBleeds.push().getKey();

            Bleed bleed = new Bleed(id,bleedLocation , rating);
            databaseBleeds.child(id).setValue(bleed);

            Toast.makeText(this,"Bleed saved successfully", Toast.LENGTH_LONG).show();
            editTextBleedLocation.setText("");
        }else{
            Toast.makeText(this,"Bleed Location should not be empty", Toast.LENGTH_LONG).show();
        }
        }


    }
