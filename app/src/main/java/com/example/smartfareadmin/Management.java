package com.example.smartfareadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    }


    @OnClick(R.id.btn_manage_vehicles)
    public void getVehicle(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Vehicles");
        finish();
    }

    public void driverCode(View view){
        Intent intent = new Intent(this, GenerateCode.class);
        startActivity(intent);
        finish();
    }


}
