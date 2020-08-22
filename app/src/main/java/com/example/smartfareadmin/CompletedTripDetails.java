package com.example.smartfareadmin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.smartfareadmin.dataObjects.AssignVehicleData;
import com.example.smartfareadmin.dataObjects.Bookings;
import com.example.smartfareadmin.dataObjects.DriverDeal;
import com.example.smartfareadmin.dataObjects.TripDetailsData;
import com.example.smartfareadmin.dataObjects.VehicleData;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedTripDetails extends AppCompatActivity {

    DriverDeal driverDeal;
    VehicleData vehicleData;
    Bookings bookings;
    TripDetailsData tripDetailsData;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String cUID,driverId,vehicleId,bookingId,stat;

    @BindView(R.id.dTextNum)
    TextView textName;

    @BindView(R.id.dContact)
    TextView driverContact;

    @BindView(R.id.uTextName)
    TextView utextName;

    @BindView(R.id.uPickupDate)
    TextView upickUpDate;

    @BindView(R.id.uPickUpTime)
    TextView uPickUpTime;

    @BindView(R.id.utxtFrom)
    TextView utextFrom;

    @BindView(R.id.utxtTo)
    TextView utxtTo;

    @BindView(R.id.utextAmount)
    TextView textAmount;

    @BindView(R.id.txtDriverInfo)
    TextView textDriverInfo;

    @BindView(R.id.txtNameTitle)
    TextView textNameTitle;

    @BindView(R.id.txtPhonetitle)
    TextView textPhoneTitle;

    @BindView(R.id.texttitleHead)
    TextView textTitle;

    @BindView(R.id.infoConatainer)
    ConstraintLayout infoContainer;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_completed_trip_details);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("SmartCah Gh");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        infoContainer.setVisibility(View.GONE);
        driverDeal = new DriverDeal();
        vehicleData = new VehicleData();
        tripDetailsData = new TripDetailsData();
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mProgressBar.setVisibility(View.VISIBLE);
         Bundle bundle = getIntent().getExtras();
         stat = bundle.getString("completed");


        Intent intent = getIntent();
//        Bookings bookingsId = (Bookings) intent.getSerializableExtra("bookingId");
//        bookingId = bookingsId.getId();
//        Log.d("booking id", bookingId);
        Bookings booking = (Bookings) intent.getSerializableExtra("Completed Bookings");


        if(booking == null){
            booking = new Bookings();
        }

        this.bookings = booking;
        bookingId = bookings.getId();

        utextName.setText(bookings.getName());
        utextFrom.setText(bookings.getFrom());
        utxtTo.setText(bookings.getTo());
        upickUpDate.setText(bookings.getPick_up_date());
        uPickUpTime.setText(bookings.getPick_up_time());
     //   textAmount.setText("GHC " + bookings.getAmount());

        if(stat.equals("1")){
            textTitle.setText(R.string.completed_trip_details);
            getTripData();
        }
        else {
            textNameTitle.setText(R.string.reason);
            textName.setText(bookings.getMsg());

            textNameTitle.setVisibility(View.VISIBLE);
            textName.setVisibility(View.VISIBLE);

            infoContainer.setVisibility(View.VISIBLE);

            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void getTripData(){

        textDriverInfo.setVisibility(View.VISIBLE);
        textNameTitle.setVisibility(View.VISIBLE);
        textName.setVisibility(View.VISIBLE);
        textPhoneTitle.setVisibility(View.VISIBLE);
        driverContact.setVisibility(View.VISIBLE);

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("trip details");

        Query query = dRef.orderByChild(bookingId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    for(DataSnapshot dataSnapshot : dataSnapshot1.getChildren()){
                        tripDetailsData = dataSnapshot.getValue(TripDetailsData.class);
                        if((tripDetailsData.getStatus()).equals("0") && tripDetailsData.getBookingId().equals(bookingId)){
                            driverId = tripDetailsData.getDriverId();
                            Log.d("trip details", tripDetailsData.getDriverId());
                            textAmount.setText("GHC " + tripDetailsData.getTotalFare());
                            // getAssignedVehicle();
                            getDriverInfo();

                            return;
                        }else {
                            infoContainer.setVisibility(View.GONE);
                            Log.d("no tip","trip doesnt exist");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getDriverInfo(){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.drivers_profile));

        Query query = mRef.orderByKey().equalTo(driverId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    driverDeal = dataSnapshot1.getValue(DriverDeal.class);

                    bindText();
                    infoContainer.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getVehicleInfo(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("vehicles details");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    if(dataSnapshot1.getKey().equals(vehicleId)){
                        vehicleData = dataSnapshot1.getValue(VehicleData.class);
                        Log.d("driver info", vehicleData.getRegistrationNumber());
                       // getUserInfo();
                        bindText();
                        infoContainer.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void bindText(){
        textName.setText(driverDeal.getName());
        driverContact.setText(driverDeal.getPhoneNumber());

    }
}
