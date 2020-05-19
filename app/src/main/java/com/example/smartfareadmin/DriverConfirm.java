package com.example.smartfareadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.smartfareadmin.activities.Constants;
import com.example.smartfareadmin.dataObjects.Bookings;
import com.example.smartfareadmin.dataObjects.DriverDeal;
import com.example.smartfareadmin.dataObjects.TripDetailsData;
import com.example.smartfareadmin.fragments.DriverMap;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverConfirm extends AppCompatActivity {

    Bookings bookings;
    DriverDeal driverDeal;
    TripDetailsData tripDetailsData;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private String bookingId,driverId,userBookId, cUID;

    @BindView(R.id.dTextNum)
    TextView textName;

    @BindView(R.id.dContact)
    TextView textPhone;

    @BindView(R.id.ttxtFrom)
    TextView textFrom;

    @BindView(R.id.ttxtTo)
    TextView textTo;

    @BindView(R.id.ctextAmount)
    TextView textAmount;

    @BindView(R.id.cPickupDate)
    TextView textPickDate;

    @BindView(R.id.cPickUpTime)
    TextView textPickTime;

    @BindView(R.id.cDistance)
    TextView textDistance;

    @BindView(R.id.cbtnDone)
    Button btnDone;

    @BindView(R.id.buttonViewRout)
    Button btnViewRout;

    @BindView(R.id.infoLayout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.texttitleHead)
    TextView textHead;

    @BindView(R.id.txtOthers)
    TextView txtOthers;

    @BindView(R.id.cTextWaitingTitle)
    TextView txtWaitingTitle;

    @BindView(R.id.ctextWaiting)
    TextView txtWaiting;

    @BindView(R.id.txtTotalFareTitle)
    TextView txtTotalFareTitle;

    @BindView(R.id.txtTotalFare)
    TextView txtTotalFare;

    @BindView(R.id.txtChange)
    TextView txtChange;

    @BindView(R.id.txtChangeTitle)
    TextView txtChangeTitle;

    @BindView(R.id.editTextAmount)
    EditText editTextAmount;

    double tWaitingFare,tFare;

    TextWatcher txtWatcher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_confirm);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("SmartCah Gh");

        FirebaseUtils.openFirebaseUtils("bookings",this);

        bookings = new Bookings();
        tripDetailsData = new TripDetailsData();
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        cUID = user.getUid();

        Log.d("user", cUID);

        getTripData();

        Bundle bundle = getIntent().getExtras();
        tWaitingFare =  bundle.getDouble(Constants.TOTAL_FARE);

        if(tWaitingFare == -1){
            txtOthers.setVisibility(View.GONE);
            txtWaitingTitle.setVisibility(View.GONE);
            txtWaiting.setVisibility(View.GONE);
            txtTotalFareTitle.setVisibility(View.GONE);
            txtTotalFare.setVisibility(View.GONE);
            editTextAmount.setVisibility(View.GONE);
            txtChangeTitle.setVisibility(View.GONE);
            txtChange.setVisibility(View.GONE);
            btnDone.setVisibility(View.GONE);

        }else {
            txtOthers.setVisibility(View.VISIBLE);
            txtWaitingTitle.setVisibility(View.VISIBLE);
            txtWaiting.setVisibility(View.VISIBLE);
            txtTotalFareTitle.setVisibility(View.VISIBLE);
            txtTotalFare.setVisibility(View.VISIBLE);
            editTextAmount.setVisibility(View.VISIBLE);
            txtChangeTitle.setVisibility(View.VISIBLE);
            txtChange.setVisibility(View.VISIBLE);
            btnDone.setVisibility(View.VISIBLE);
            btnViewRout.setVisibility(View.GONE);

        }

        txtWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   if(editTextAmount.getText().toString().trim().isEmpty()){
                       txtChange.setText("GHC " + "0.00");
                   }else {
                       getBalance();
                   }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

     editTextAmount.addTextChangedListener(txtWatcher);

    }

    private void getBalance() {

            double bal = Double.parseDouble(editTextAmount.getText().toString()) - tFare;

            txtChange.setText("GHC " + String.valueOf(bal));


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DriverMap.class);
        intent.putExtra(Constants.DriverMap, "nonRoute");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.cbtnDone)
    public void getComfirmation(){
        updateBookings();

    }


    @OnClick(R.id.buttonViewRout)
    public void getRoute(){
        Intent intent = new Intent(this, DriverMap.class);
        intent.putExtra(Constants.FromLat, Double.valueOf(bookings.getFromLatitude()) );
        intent.putExtra(Constants.fromLng, Double.valueOf(bookings.getFromLongitude()));
        intent.putExtra(Constants.toLat, Double.valueOf(bookings.getToLatitude()));
        intent.putExtra(Constants.toLng, Double.valueOf(bookings.getToLongitude()));
        intent.putExtra(Constants.DriverMap, "drawRout");
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.dContact})
    public void callNumber(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://"+ textPhone.getText().toString().trim())));
    }


////////////method to get trip details from firebase database///////////////////////////
    public void getTripData(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("trip details");
        cUID = user.getUid();
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    tripDetailsData = ds.getValue(TripDetailsData.class);
                    Log.d("ds key", ds.getKey());
                    if(tripDetailsData != null){
                        if((tripDetailsData.getStatus()).equals("1") && (tripDetailsData.getDriverId()).equals(cUID)){
                            tripDetailsData.setId(ds.getKey());
                            Log.d("trip data", tripDetailsData.getDriverId());
                            driverId = tripDetailsData.getDriverId();
                            userBookId = tripDetailsData.getUserId();
                            getBookingDetails();
                        }else {
                            constraintLayout.setVisibility(View.GONE);
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


/////method to get booking details from firebase database////////////////
    public void getBookingDetails(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Log.d("datasnapshot1 Key", dataSnapshot1.getKey());
                  if(dataSnapshot1.getKey().equals(userBookId)){

                      for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                          bookings = dataSnapshot2.getValue(Bookings.class);
                          if(bookings.getStatus().equals("1")){
                              bookings.setId(dataSnapshot2.getKey());
                              bookingId = bookings.getId();

                              Log.d("booking", bookings.getId());
                              Log.d("[path1", userBookId + " " + bookings.getId());
                              bind(bookings);

                              constraintLayout.setVisibility(View.VISIBLE);
                          }else {
                               constraintLayout.setVisibility(View.GONE);
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

    public void bind(Bookings bookings){
        textName.setText(bookings.getName());
        textAmount.setText("GHC " + bookings.getAmount());
        textDistance.setText(bookings.getDistance());
        textFrom.setText(bookings.getFrom());
        textTo.setText(bookings.getTo());
        textPickDate.setText(bookings.getPick_up_date());
        textPickTime.setText(bookings.getPick_up_time());
        textPhone.setText(bookings.getPhoneNumber());
        txtWaiting.setText("GHC " + String.valueOf(tWaitingFare));
        tFare = Double.parseDouble(bookings.getAmount()) + tWaitingFare;
        txtTotalFare.setText(" GHC " + String.valueOf(tFare));

    }

    public void updateBookings(){
        bookings.setStatus("2");
        Log.d("[path", userBookId + " " + bookings.getId());
        databaseReference.child(userBookId).child(bookingId).setValue(bookings);
        getDriverDetails();
    }

    public void getDriverDetails(){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("drivers profile");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                  if(driverId.equals(dataSnapshot1.getKey())){
                      for(DataSnapshot ds: dataSnapshot1.getChildren()){
                          driverDeal = ds.getValue(DriverDeal.class);
                          driverDeal.setId(ds.getKey());
                      }
                      updateDriverDetails();
                  }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateDriverDetails(){
        driverDeal.setStatus("0");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("drivers profile");
        mRef.child(driverId).child(driverDeal.getId()).setValue(driverDeal);
        updateTrip();

    }

    public void updateTrip(){
        tripDetailsData.setStatus("0");
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("trip details");
        dRef.child(tripDetailsData.getId()).setValue(tripDetailsData);
        textHead.setText("THANK YOU FOR SUCCESSFUL TRIP");

    }

}
