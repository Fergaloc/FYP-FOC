package com.example.MyBleeds;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class bleedFragment extends Fragment {


    private View bleedView;
    private RecyclerView recyclerView;
    ArrayList<Bleed> arrayList;
    Context context;
    FirebaseAuth mAuth;
    String currentUserID;
    DatabaseReference bleedsRef;

    //Sets Dates for now and for 6 months
    String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    String SixDates = (LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));



    public bleedFragment(){

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceChange){

        bleedView = inflater.inflate(R.layout.my_health,container, false);

        recyclerView = (RecyclerView) bleedView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        bleedsRef = FirebaseDatabase.getInstance().getReference().child("bleeds").child(currentUserID);



        return bleedView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Bleed>().setQuery(bleedsRef, Bleed.class).build();

        FirebaseRecyclerAdapter<Bleed, BleedViewHolder> adapter =  new FirebaseRecyclerAdapter<Bleed, BleedViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final BleedViewHolder holder, int position, @NonNull Bleed model) {

                final String bleedID = getRef(position).getKey();

               bleedsRef.child(bleedID).orderByChild("BleedDate").startAt(SixDates).endAt(currentDate).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String bleedID = dataSnapshot.child("bleedIDID").getValue().toString();
                        final String bleedName = dataSnapshot.child("bleedName").getValue().toString();
                        String bleedDate = dataSnapshot.child("bleedDate").getValue().toString();
                        String bleedSeverity = dataSnapshot.child("bleedSeverity").getValue().toString();

                        holder.txtSeverity.setText(bleedSeverity);
                        holder.txtName.setText(bleedName);
                        holder.txtDate.setText(bleedDate);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }


            @NonNull
            @Override
            public BleedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bleedlist, parent, false);
               BleedViewHolder viewHolder = new BleedViewHolder(view);
                return viewHolder;

            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class BleedViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtSeverity, txtDate ;


        public BleedViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtbleedName);
            txtSeverity = itemView.findViewById(R.id.bleedSeverity);
            txtDate  = itemView.findViewById(R.id.txtBleedDate);

        }
    }


}
