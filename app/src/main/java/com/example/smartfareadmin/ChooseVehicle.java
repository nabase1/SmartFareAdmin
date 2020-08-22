package com.example.smartfareadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfareadmin.activities.Constants;
import com.example.smartfareadmin.activities.Login;
import com.example.smartfareadmin.dataObjects.AssignVehicleData;
import com.example.smartfareadmin.dataObjects.DriverDeal;
import com.example.smartfareadmin.dataObjects.VehicleData;
import com.example.smartfareadmin.fragments.DriverMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseVehicle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.spinnerVehicle)
    Spinner spinnerVehicle;

    VehicleData vehicleData;
    public static ArrayList<VehicleData> array;
    AssignVehicleData assignVehicleData;
    DriverDeal driverDeal;
    FirebaseAuth mAuth;
    FirebaseUser user;

    String driverId, vehicleId, cDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_vehicle);
        ButterKnife.bind(this);

        array = new ArrayList<VehicleData>();
        vehicleData = new VehicleData();
        assignVehicleData = new AssignVehicleData();
        driverDeal = new DriverDeal();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        driverId = user.getUid();

        spinnerVehicle.setOnItemSelectedListener(this);

        populateSpinner();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.buttonAssign)
    public void getButtonClick(){
        if(vehicleId.equals("")){
            Toast.makeText(this, "No Vehicle Selected!, Please Seclect Vehicle", Toast.LENGTH_SHORT).show();
        }else {
            updateVehicle();
        }

    }

    @OnClick(R.id.buttonCancel)
    public void getButtonCancel(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int item = parent.getSelectedItemPosition();
        if(parent.getSelectedItem().equals("Select Vehicle")){
            vehicleId = "";
        }else {
            int itemReduce = item - 1;
            vehicleData = array.get(itemReduce);

            vehicleData.setStatus("1");
            vehicleId = vehicleData.getId();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //method to fetch data from firebase to populate  spinner
    public void populateSpinner(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("vehicles details");

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> titleList = new ArrayList<String>();
                titleList.add("Select Vehicle");
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    VehicleData vDeals = dataSnapshot1.getValue(VehicleData.class);
                    if(vDeals.getStatus().equals("0")){
                        vDeals.setId(dataSnapshot1.getKey());
                        String vehicle = vDeals.getRegistrationNumber();
                        array.add(vDeals);
                        titleList.add(vehicle);
                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChooseVehicle.this, android.R.layout.simple_spinner_item, titleList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerVehicle.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChooseVehicle.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }



//    public void getDriverInfo(){
//        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("drivers profile");
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    if(dataSnapshot1.getKey().equals(driverId)){
//                        for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
//
//                            driverDeal = dataSnapshot2.getValue(DriverDeal.class);
//                            driverDeal.setId(dataSnapshot2.getKey());
//
//                        }
//
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    public void updateVehicle(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("vehicles details");
        if(vehicleData.getId() != null){
            dRef.child(vehicleData.getId()).setValue(vehicleData, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error != null){
                        Toast.makeText(ChooseVehicle.this, "There is a problem!, Please Wait", Toast.LENGTH_SHORT).show();
                    }else {
                        saveAssignVehicle();
                    }
                }
            });

        }
        else {
            Toast.makeText(this, "Vehicle is null!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDriver(){
        driverDeal.setStatus("1");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("drivers profile");
        mRef.child(driverId).setValue(driverDeal);

    }

    public void saveAssignVehicle(){

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df =  new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
        String formattedDate = df.format(c);

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("assigned vehicles");
        assignVehicleData.setDriverId(driverId);
        assignVehicleData.setVehicleId(vehicleId);
        assignVehicleData.setStatus("1");
        assignVehicleData.setcDate(formattedDate);

        dRef.child(driverId).push().setValue(assignVehicleData);

        updateDriver();

        Intent intent = new Intent(this, DriverMap.class);
        intent.putExtra(Constants.DriverMap, "nonRoute");
        startActivity(intent);
        finish();

    }
}
