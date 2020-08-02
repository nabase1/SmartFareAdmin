package com.example.smartfareadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }



    public void SwitchFare(View view){
        Intent intent = new Intent(this, FareSwitch.class);
        startActivity(intent);
    }

    public void getPrices(View view){
        Intent intent = new Intent(this, PriceControl.class);
        startActivity(intent);
    }

    public void backPressed(View view){
        onBackPressed();
    }
}