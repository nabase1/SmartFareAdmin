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
import com.example.smartfareadmin.VehicleRegistration;
import com.example.smartfareadmin.dataObjects.VehicleData;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    ArrayList<VehicleData> vehicleDataArrayList;


    public VehicleAdapter(){

        vehicleDataArrayList = FirebaseUtils.vehicleDataArrayList;
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                VehicleData vehicleData = dataSnapshot.getValue(VehicleData.class);
                vehicleData.setId(dataSnapshot.getKey());
                vehicleDataArrayList.add(vehicleData);
                Log.d("vehicle Object", vehicleData.getEngineType());
                notifyItemChanged(vehicleDataArrayList.size()-1);
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
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.vehicle_row, parent, false);


        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
           VehicleData vehicleData = vehicleDataArrayList.get(position);

            Log.d("v Obj", vehicleData.getRegistrationNumber());
            holder.bind(vehicleData);

    }

    @Override
    public int getItemCount() {
        return vehicleDataArrayList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView regNum, engType, insPolicy,manufDate,ownership;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            regNum = (TextView) itemView.findViewById(R.id.dTextNum);
            engType = (TextView) itemView.findViewById(R.id.dContact);
            insPolicy = (TextView) itemView.findViewById(R.id.ttxtFrom);
            manufDate = (TextView) itemView.findViewById(R.id.ttxtTo);
            ownership = (TextView) itemView.findViewById(R.id.cPickupDate);

            itemView.setOnClickListener(this);
        }

        public void bind(VehicleData vehicleData){
            regNum.setText(vehicleData.getRegistrationNumber());
            engType.setText(vehicleData.getEngineType());
            insPolicy.setText(vehicleData.getInsurancePolicy());
            manufDate.setText(vehicleData.getManufacturingDate());
            ownership.setText(vehicleData.getCurrentOwnership());

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("click",String.valueOf(position));

            VehicleData getDeals = vehicleDataArrayList.get(position);
            Intent intent = new Intent(v.getContext(), VehicleRegistration.class);
            intent.putExtra("Vehicle", getDeals);
            v.getContext().startActivity(intent);
        }
    }
}
