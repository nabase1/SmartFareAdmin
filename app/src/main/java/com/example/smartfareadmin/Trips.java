package com.example.smartfareadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Trips extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
    }

    public void DriversCompletedTrip(View view){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Meter Trips");
        startActivity(intent);
    }

    public void DriversOngoingTrip(View view){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Ongoing Meter Trips");
        startActivity(intent);
    }

    public void CompletedTrips(View view){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Completed Trips");
        startActivity(intent);
    }

    public void backPress(View view){
        onBackPressed();
    }
}