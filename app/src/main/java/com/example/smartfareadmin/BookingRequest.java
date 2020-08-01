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
    }

    @OnClick(R.id.btn_confirmed_vehicles)
    public void confirmedBookbtn(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Confirmed Request");
        startActivity(intent);
    }


    @OnClick(R.id.btn_canceled_bookings)
    public void BookCancelled(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Cancelled Bookings");
        startActivity(intent);
    }


}
