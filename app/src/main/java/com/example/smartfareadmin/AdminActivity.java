package com.example.smartfareadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.smartfareadmin.activities.Constants;
import com.example.smartfareadmin.activities.Login;
import com.example.smartfareadmin.fragments.AdminMaps;
import com.example.smartfareadmin.notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminActivity extends AppCompatActivity {

    String mUid;
    private  static FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();

        checkUserStatus();
        updateToken(FirebaseInstanceId.getInstance().getToken());

    }


    @OnClick(R.id.btn_booking_request)
    public void Bookbtn(){
        Intent intent = new Intent(this,   BookingRequest.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_management)
    public void managementbtn(){
        Intent intent = new Intent(this,   Management.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_trackVehicles)
    public void getTrackButton(){
        Intent intent = new Intent(this, AdminMaps.class);
        startActivity(intent);
    }


    public void getTrips(View view){
        Intent intent = new Intent(this,   Trips.class);
        startActivity(intent);
    }

    public void getDrivers(View view){
        Intent intent = new Intent(this,   Drivers.class);
        startActivity(intent);
    }

    public void getSettings(View view){
        Intent intent = new Intent(this,   Settings.class);
        startActivity(intent);
    }


    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){

            mUid = user.getUid();

            //save uid of current user
            SharedPreferences sp = getSharedPreferences(Constants.SP_USER,MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Constants.USER_ID, mUid);
            editor.apply();

        }
    }

    public void updateToken(String token){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("AdminTokens");
        Token mtoken = new Token(token);

        dRef.child(mUid).setValue(mtoken);
    }

}
