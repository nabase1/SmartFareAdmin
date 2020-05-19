package com.example.smartfareadmin.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfareadmin.R;
import com.example.smartfareadmin.dataObjects.DriverLocation;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class DriversOnline extends RecyclerView.Adapter<DriversOnline.DriversOnlineViewHolder> {

    ArrayList<DriverLocation> driversOnlineArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;


    public DriversOnline(){
        driversOnlineArrayList = FirebaseUtils.driversOnlineArrayList;
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;


        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Log.d("key", dataSnapshot.getKey());
                    DriverLocation driversOnline = dataSnapshot.getValue(DriverLocation.class);
                    driversOnline.setId(dataSnapshot.getKey());

                    driversOnlineArrayList.add(driversOnline);


                    notifyItemChanged(driversOnlineArrayList.size()-1);
                Log.d("veh name", driversOnlineArrayList.get(0).getVehicle());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addChildEventListener(childEventListener);
    }


    @NonNull
    @Override
    public DriversOnlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemview = LayoutInflater.from(context)
                .inflate(R.layout.drivers_online_row, parent, false);


        return new DriversOnlineViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull DriversOnlineViewHolder holder, int position) {
       DriverLocation driversOnline = driversOnlineArrayList.get(position);
       holder.bind(driversOnline);
    }


    @Override
    public int getItemCount() {
        return driversOnlineArrayList.size();
    }

    public class DriversOnlineViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textVehicle, textLocation;


        public DriversOnlineViewHolder(@NonNull View itemView) {
            super(itemView);

          textName = (TextView) itemView.findViewById(R.id.dTextNam);
          textVehicle = (TextView) itemView.findViewById(R.id.dVehicle);
          textLocation= (TextView) itemView.findViewById(R.id.ttxtLocation);
        }

        public void bind(DriverLocation OnlineDrivers){
            textName.setText(OnlineDrivers.getName());
            textVehicle.setText(OnlineDrivers.getVehicle());
            textLocation.setText(OnlineDrivers.getLocationName());
        }

    }
}
