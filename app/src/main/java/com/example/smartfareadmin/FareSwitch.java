package com.example.smartfareadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FareSwitch extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

   @BindView(R.id.per_km_switch)
    Switch per_km_switch;

   @BindView(R.id.per_min_switch)
   Switch per_min_switch;

   @BindView(R.id.textStatus)
    TextView textStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_switch);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.mtoolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("SmartCab Gh");

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FirebaseUtils.openFirebaseUtils("price switch",this);

        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;

        getPriceStatus();

        CompoundButton.OnCheckedChangeListener multipleLIsteners = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.getId() == R.id.per_min_switch){
                    if(isChecked){
                        if(per_km_switch.isChecked()){
                            per_km_switch.setChecked(false);
                            databaseReference.child("per km").setValue("0");
                            databaseReference.child("per min").setValue("1");
                            databaseReference.child("both").setValue("0");

                            Toast.makeText(FareSwitch.this, "Switched to Minute Pricing", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                if(buttonView.getId() == R.id.per_km_switch){
                    if(isChecked){
                        if(per_min_switch.isChecked()){
                            per_min_switch.setChecked(false);
                            databaseReference.child("per min").setValue("0");
                            databaseReference.child("per km").setValue("1");
                            databaseReference.child("both").setValue("0");

                            Toast.makeText(FareSwitch.this, "Switched to Kilometer pricing", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }
        };

        per_km_switch.setOnCheckedChangeListener(multipleLIsteners);
        per_min_switch.setOnCheckedChangeListener(multipleLIsteners);

    }


    public void getPriceStatus() {
        if (per_min_switch.isChecked() || per_km_switch.isChecked()) {

        }
        else if(per_min_switch.isChecked() && per_km_switch.isChecked()) {
        }
        else
         {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String perMin = dataSnapshot.child("per min").getValue().toString();
                    String perKM = dataSnapshot.child("per km").getValue().toString();
                    String both = dataSnapshot.child("both").getValue().toString();


                    if (perKM.equals("1")) {
                        per_km_switch.setChecked(true);
                        textStatus.setText("Kilometer pricing activated");
                    }
                    if (perMin.equals("1")) {
                        per_min_switch.setChecked(true);
                        textStatus.setText("Minutes Pricing activated");
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(FareSwitch.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
