package com.example.smartfareadmin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartfareadmin.dataObjects.AssignVehicleData;
import com.example.smartfareadmin.dataObjects.Bookings;
import com.example.smartfareadmin.dataObjects.DriverDeal;
import com.example.smartfareadmin.dataObjects.TripDetailsData;
import com.example.smartfareadmin.dataObjects.VehicleData;
import com.example.smartfareadmin.notification.APIServices;
import com.example.smartfareadmin.notification.Client;
import com.example.smartfareadmin.notification.Data;
import com.example.smartfareadmin.notification.Response;
import com.example.smartfareadmin.notification.Sender;
import com.example.smartfareadmin.notification.Token;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class TripDetails extends AppCompatActivity {

    DriverDeal driverDeal;
    VehicleData vehicleData;
    Bookings bookings;
    TripDetailsData tripDetailsData;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String cUID,driverId,vehicleId,state,bookingid;
    String msg;
    AlertDialog alertDialog;
    APIServices apiServices;


    @BindView(R.id.dTextNum)
    TextView textName;

    @BindView(R.id.ttxtFrom)
    TextView regNum;

    @BindView(R.id.dContact)
    TextView driverContact;

    @BindView(R.id.ttxtTo)
    TextView vehicleExtColor;

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

    @BindView(R.id.ctextAmount)
    TextView txtAmount;

    @BindView(R.id.infoConatainer)
    ConstraintLayout infoContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_trip_details);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.mtoolbar);
        toolbar.setTitle("SmartCab Gh");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //create api services
        apiServices = Client.getRetrofit("https://fcm.googleapis.com/").create(APIServices.class);

        infoContainer.setVisibility(View.GONE);
        driverDeal = new DriverDeal();
        vehicleData = new VehicleData();
        tripDetailsData = new TripDetailsData();
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        bookingid = bundle.getString("bookingId");
        cUID = bundle.getString("userId");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Why Cancelling Request");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                msg = input.getText().toString();
                updateDriverDetails();
                dialog.dismiss();
                Toast.makeText(TripDetails.this, "Booking Cancelled!", Toast.LENGTH_SHORT).show();


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog = builder.create();


        Intent intent = getIntent();
        Bookings booking = (Bookings) intent.getSerializableExtra("Confirmed Bookings");



        if(booking == null){
            booking = new Bookings();
        }

        this.bookings = booking;

        utextName.setText(bookings.getName());
        utextFrom.setText(bookings.getFrom());
        utxtTo.setText(bookings.getTo());
        upickUpDate.setText(bookings.getPick_up_date());
        uPickUpTime.setText(bookings.getPick_up_time());
        txtAmount.setText("GHC " + bookings.getAmount());

        getTripData();

    }

    @OnClick(R.id.dContact)
    public void callNumber(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://"+driverContact.getText().toString().trim())));
    }

    @OnClick(R.id.cbtnCancel)
    public void btnCancel(){
        alertDialog.show();
    }

    public void getTripData(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("trip details");
        cUID = user.getUid();
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    TripDetailsData tripDetailsDatas = ds.getValue(TripDetailsData.class);
                    if(tripDetailsDatas != null){
                        if((tripDetailsDatas.getStatus()).equals("1") && (tripDetailsDatas.getUserId()).equals(cUID) && tripDetailsDatas.getBookingId().equals(bookingid)){
                            tripDetailsData = ds.getValue(TripDetailsData.class);
                            driverId = tripDetailsData.getDriverId();
                            tripDetailsData.setId(ds.getKey());
                            Log.d("trip details", tripDetailsData.getDriverId());
                            getAssignedVehicle();

                            return;
                        }else {
                            infoContainer.setVisibility(View.GONE);
                        }
                    }
                    else {
                        infoContainer.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUserInfo(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("bookings");
        cUID = user.getUid();
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("userIds", ds.getKey());
                    if(ds.getKey().equals(cUID)){
                        for(DataSnapshot ds1 : ds.getChildren()){
                            Log.d("booking id", ds1.getKey());
                            Bookings booking = ds1.getValue(Bookings.class);
                            booking.setId(ds1.getKey());
                            if((booking.getStatus()).equals("1") && booking.getId().equals(bookingid)){
                                bookings = ds1.getValue(Bookings.class);
                                bookings.setId(ds1.getKey());
                                utextName.setText(bookings.getName());
                                utextFrom.setText(bookings.getFrom());
                                utxtTo.setText(bookings.getTo());
                                upickUpDate.setText(bookings.getPick_up_date());
                                uPickUpTime.setText(bookings.getPick_up_time());
                                txtAmount.setText("GHC " + bookings.getAmount());




                            }
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAssignedVehicle(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("assigned vehicles");

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    AssignVehicleData assignVehicleData = ds.getValue(AssignVehicleData.class);
                    Log.d("ds key", ds.getKey());
                    if(assignVehicleData != null){
                        if((assignVehicleData.getStatus()).equals("1") && (assignVehicleData.getDriverId()).equals(driverId)){
                            Log.d("Assign vehicle", assignVehicleData.getVehicleId());
                            vehicleId = assignVehicleData.getVehicleId();
                            getDriverInfo();
                        }
                    }
                    else {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void getDriverInfo(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("drivers profile");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    if(dataSnapshot1.getKey().equals(driverId)){
                        for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){

                            driverDeal = dataSnapshot2.getValue(DriverDeal.class);
                            Log.d("driver Info", driverDeal.getDisplayName());
                            getVehicleInfo();
                        }
                    }
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
        textName.setText(driverDeal.getDisplayName());
        driverContact.setText(driverDeal.getPhoneNumber());
        regNum.setText(vehicleData.getRegistrationNumber());
        vehicleExtColor.setText(vehicleData.getExteriorColor());
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Confirmed Request");
        // intent.putExtra("state",);
        startActivity(intent);
        finish();


        super.onBackPressed();
    }

    public void updateTrip(){
        tripDetailsData.setStatus("-1");
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("trip details");
        dRef.child(tripDetailsData.getId()).setValue(tripDetailsData);
        // sendNotification("", "SmartCab Gh", driverDeal.getDisplayName() + " Just Completed His Trip!");
        updateBookings();


    }

    public void updateDriverDetails(){
        getDriverInfo();
        driverDeal.setStatus("0");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("drivers profile");
        mRef.child(driverId).child(driverDeal.getId()).setValue(driverDeal);
        updateTrip();

    }

    public void updateBookings(){
        bookings.setStatus("-1");
        bookings.setMsg(msg);
        databaseReference.child(user.getUid()).child(bookingid).setValue(bookings);
        sendNotification("","SmartCab GH", bookings.getName()+ " " + "Cancelled His Request");
        sendNotificationToFriver(driverId, "SmartCab GH", bookings.getName()+ " " + "Cancelled Your Trip");
        onBackPressed();

    }

    private void sendNotification(String userId, String title, String body) {

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("AdminTokens");
        // Query query = dRef.orderByKey().equalTo(userId);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(cUID, title, body,  userId, R.drawable.smart_logo_blue);
                    // Log.d("uidkey", ds.getKey());
                    Log.d("token", token.getToken());
                    Sender sender = new Sender(data, token.getToken());
                    apiServices.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(TripDetails.this, response.message(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotificationToFriver(String Uid, String title, String body) {

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = dRef.orderByKey().equalTo(Uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data("driver", title, body,  Uid, R.drawable.smart_logo_blue);

                    Sender sender = new Sender(data, token.getToken());
                    apiServices.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(TripDetails.this, response.message(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
