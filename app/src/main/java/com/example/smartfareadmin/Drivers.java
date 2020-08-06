package com.example.smartfareadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Drivers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);
    }


    public void getOnlineDrivers(View view){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Drivers Online");
        startActivity(intent);
    }

    public void ManageDrivers(View view){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Pending Drivers");
        startActivity(intent);
    }

    public void ConfirmedDrivers(View view){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Confirmed Drivers");
        startActivity(intent);
    }


    public void backPress(View view){
        onBackPressed();
    }
}