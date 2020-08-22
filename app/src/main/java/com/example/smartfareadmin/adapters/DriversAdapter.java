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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DriversAdapter extends RecyclerView.Adapter<DriversAdapter.DriverViewHolder> {

    ArrayList<DriverDeal> driverDealArrayList;
    ArrayList<String> userIdArray;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ValueEventListener mValueEventListener;
    DriverDeal driverDeal;
    public String userId;

    public DriversAdapter(){

        driverDealArrayList = FirebaseUtils.driverDealArrayList;
        userIdArray = new ArrayList<String>();
        driverDeal = new DriverDeal();
         firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    DriverDeal dDeal = ds.getValue(DriverDeal.class);

                    if(dDeal.getStatus().equals("-1")){

                        driverDeal = ds.getValue(DriverDeal.class);
                        driverDealArrayList.add(driverDeal);

                        Collections.reverse(driverDealArrayList);
                    }
                }

                notifyItemChanged(driverDealArrayList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.addListenerForSingleValueEvent(mValueEventListener);

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
            v.getContext().startActivity(intent);
        }
    }
}
