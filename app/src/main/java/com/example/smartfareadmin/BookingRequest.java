package com.example.smartfareadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookingRequest extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_request);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btxtArrowBack)
    public void backPress(){
        onBackPressed();
    }

    @OnClick(R.id.btn_pending_bookings)
    public void pendingBookbtn(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Pending Request");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_confirmed_vehicles)
    public void confirmedBookbtn(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Confirmed Request");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_completed_trips)
    public void CompletedTrips(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Completed Trips");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_canceled_bookings)
    public void BookCancelled(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Cancelled Bookings");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_completed_drivers_trips)
    public void DriversCompletedTrip(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Meter Trips");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_ongoing_drivers_trip)
    public void DriversOngoingTrip(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Ongoing Meter Trips");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AdminActivity.class);
        finish();
        startActivity(intent);
    }
}
