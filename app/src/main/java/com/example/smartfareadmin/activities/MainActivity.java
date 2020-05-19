package com.example.smartfareadmin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfareadmin.R;

public class MainActivity extends AppCompatActivity {
    private static int splash_time = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(() -> {
            Intent mapIntent = new Intent(MainActivity.this, Login.class);
            startActivity(mapIntent);
            finish();
                },splash_time);
    }

}
