package com.example.smartfareadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Management extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.mtxtArrowBack)
    public void backPress(){
        onBackPressed();
    }

    @OnClick(R.id.btn_services)
    public void Servicesbtn(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","services");
        startActivity(intent);
        finish();

    }

    @OnClick(R.id.btn_drivers)
    public void ManageDrivers(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Pending Drivers");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_drivers_confirm)
    public void ConfirmedDrivers(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Confirmed Drivers");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_manage_vehicles)
    public void getVehicle(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Vehicles");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_manage_prices)
    public void getPrices(){
        Intent intent = new Intent(this, PriceControl.class);
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
