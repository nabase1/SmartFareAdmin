package com.example.smartfareadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfareadmin.R;
import com.example.smartfareadmin.RegisterDriver;
import com.example.smartfareadmin.dataObjects.DriverDeal;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Confirmed_drivers extends RecyclerView.Adapter<Confirmed_drivers.DriverViewHolder> {

    ArrayList<DriverDeal> driverDealArrayList;
    ArrayList<String> userIdArray;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    DriverDeal driverDeal;
    public String userId;

    public Confirmed_drivers(){

        driverDealArrayList = FirebaseUtils.driverDealArrayList;
        userIdArray = new ArrayList<String>();
        driverDeal = new DriverDeal();
         firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    DriverDeal dDeal = ds.getValue(DriverDeal.class);

                    if(dDeal.getStatus().equals("0")){
                        userId = ds.getKey();
                        Log.d("driver id", userId);
                        userIdArray.add(dataSnapshot.getKey());
                        driverDeal = ds.getValue(DriverDeal.class);
                        driverDeal.setId(ds.getKey());
                        driverDealArrayList.add(driverDeal);


                    }
                }

                notifyItemChanged(driverDealArrayList.size()-1);
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
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.driver_row, parent, false);

        return new DriverViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
         driverDeal = driverDealArrayList.get(position);
        holder.bind(driverDeal);
    }



    @Override
    public int getItemCount() {
        return driverDealArrayList.size();
    }


    public class DriverViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameText,phoneText,addressText,emailText,licenseText;
        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.dTextNum);
            addressText = (TextView) itemView.findViewById(R.id.ttxtTo);
            phoneText = (TextView) itemView.findViewById(R.id.dContact);
            emailText = (TextView) itemView.findViewById(R.id.ttxtFrom);
            licenseText = (TextView) itemView.findViewById(R.id.cPickupDate);

            itemView.setOnClickListener(this);

        }

        public void bind(DriverDeal driverDeal){
            nameText.setText(driverDeal.getName());
            phoneText.setText(driverDeal.getPhoneNumber());
            addressText.setText(driverDeal.getAddress());
            emailText.setText(driverDeal.getEmail());
            licenseText.setText(driverDeal.getDriverLicense());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            DriverDeal getDeals = driverDealArrayList.get(position);
            Intent intent = new Intent(v.getContext(), RegisterDriver.class);
            intent.putExtra("drivers", getDeals);
            intent.putExtra("userId", userIdArray.get(position));
            Log.d("uid",userIdArray.get(position));
            v.getContext().startActivity(intent);
        }
    }
}
