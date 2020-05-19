package com.example.smartfareadmin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.smartfareadmin.AdminActivity;
import com.example.smartfareadmin.ChooseVehicle;
import com.example.smartfareadmin.R;
import com.example.smartfareadmin.RegisterDriver;
import com.example.smartfareadmin.UserProfile;
import com.example.smartfareadmin.dataObjects.AssignVehicleData;
import com.example.smartfareadmin.dataObjects.CodeObjects;
import com.example.smartfareadmin.fragments.DriverMap;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jgabrielfreitas.core.BlurImageView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;
    private String work;
    private String stat = "";
    String driverId = "";
    private long backPressedTime;
    List<AuthUI.IdpConfig> providers;
    CodeObjects codeObjects;
    AssignVehicleData assignVehicleData;


    @BindView(R.id.workConstraint)
    ConstraintLayout workConstraint;

    @BindView(R.id.textCode)
    TextInputLayout textCode;


    @BindView(R.id.btnConfirm)
    Button btnConfirmCode;

    @BindView(R.id.btnCustomer)
    Button btnCustomer;

    @BindView(R.id.btnDriver)
    Button btnDriver;

    @BindView(R.id.BlurImageView)
    BlurImageView blurImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        blurImageView.setBlur(2);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUtils.openFirebaseUtils("user profile", this);
        mfirebaseDatabase = FirebaseUtils.firebaseDatabase;
        mdatabaseReference = FirebaseUtils.databaseReference;
        assignVehicleData = new AssignVehicleData();

        btnDriver.setEnabled(true);
        textCode.setVisibility(View.GONE);
        btnConfirmCode.setVisibility(View.GONE);
        workConstraint.setVisibility(View.INVISIBLE);
        //slider(workConstraint);

        displaySignInButtons();

        getAssignedVehicle();

    }


    private void displaySignInButtons() {

        //arrays with the providers of authentication type
        providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build()

        );


        startActivityForResult(
               AuthUI.getInstance()
                       .createSignInIntentBuilder()
                       .setAvailableProviders(providers)
                       .setTheme(R.style.AppTheme)
                       .build(),Constants.My_Code
       );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.My_Code){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            //if successfully signed in
            if(resultCode == RESULT_OK && work == "Customer"){
                //get user

                 mdatabaseReference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                       if(dataSnapshot.child(user.getUid()).exists()){
                           Intent mapIntent = new Intent(Login.this, Services.class);
                           startActivity(mapIntent);
                           finish();
                       }

                       else {
                           Intent profileIntent = new Intent(Login.this, UserProfile.class);
                           startActivity(profileIntent);
                           finish();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("error", databaseError.getMessage());
                   }
               });

            }else if(resultCode == RESULT_OK && work == "Driver"){

                getAssignedVehicle();

                DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("drivers profile");

                dRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        FirebaseUser user = mAuth.getCurrentUser();

                        if(dataSnapshot.child(user.getUid()).exists()){
                          if(driverId.equals("")){
                              Intent intent = new Intent(Login.this, ChooseVehicle.class);
                              startActivity(intent);
                              finish();
                          }else{
                              if(driverId.equals(user.getUid())){

                                  if(stat.equals("1")){
                                      Intent mapIntent = new Intent(Login.this, DriverMap.class);
                                      mapIntent.putExtra(Constants.DriverMap, "nonRoute");
                                      startActivity(mapIntent);
                                      finish();
                                  }else {
                                      Intent intent = new Intent(Login.this, ChooseVehicle.class);
                                      startActivity(intent);
                                      finish();
                                  }
                              }else{
                                  Intent intent = new Intent(Login.this, ChooseVehicle.class);
                                  startActivity(intent);
                                  finish();
                              }
                          }

                        }

                        else {

                            textCode.setVisibility(View.VISIBLE);
                            btnConfirmCode.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("error", databaseError.getMessage());
                    }
                });



            }else if(resultCode == RESULT_OK){
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
            }
            else {
                //when user cancels sign in process
                if(response == null){
                    Toast.makeText(this, "Sign In Cancelled", Toast.LENGTH_SHORT).show();
                }

                    //when no internet connection
                if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                if(response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                    Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
                }


                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            this.finish();
            return;
        }else {
            Toast.makeText(this, "Press Again To Exit", Toast.LENGTH_SHORT).show();
        }

       backPressedTime = System.currentTimeMillis();
    }

    @OnClick(R.id.btnDriver)
    public void driverBtn(){
        work = "Driver";
        verifyDriver();

        Log.d(TAG, "Driver");
    }

    @OnClick(R.id.btnCustomer)
    public void customerBtn(){
        work = "Customer";
        workConstraint.setVisibility(View.INVISIBLE);
        displaySignInButtons();

        Log.d(TAG,work);
    }

    @OnClick(R.id.btnConfirm)
    public void btnconfirmCode(){
        confirmCode();
    }

    @OnClick({R.id.buttonAdmin})
    public void adminbtn(){
        work = "admin";

        displaySignInButtons();

    }

    public void verifyDriver(){
        displaySignInButtons();
    }


    public void confirmCode(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("drivers code");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String code = textCode.getEditText().getText().toString();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    codeObjects = ds.getValue(CodeObjects.class);
                    Log.d("codekey", ds.getKey());

                    Log.d("code", codeObjects.getCode());
                    if(codeObjects.getCode().equals(code) && codeObjects.getStatus().equals("0")){
                        codeObjects.setStatus("1");
                        myRef.child(ds.getKey()).setValue(codeObjects);
                       // displaySignInButtons();

                        Intent driverIntent = new Intent(Login.this, RegisterDriver.class);
                        driverIntent.putExtra("userId", "");
                        startActivity(driverIntent);
                        finish();
                        break;

                    }else {
                        textCode.getEditText().setError("Invalid Code");
                        textCode.getEditText().setFocusable(true);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void slider(ConstraintLayout constraintLayout){

        TranslateAnimation animate;
            constraintLayout.setVisibility(View.VISIBLE);
            animate = new TranslateAnimation(
                    0,
                    0,
                    constraintLayout.getHeight(),
                    0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            constraintLayout.startAnimation(animate);

    }

    public void getAssignedVehicle(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("assigned vehicles");
        FirebaseUser user = mAuth.getCurrentUser();
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                     assignVehicleData = ds.getValue(AssignVehicleData.class);
                    Log.d("ds key", ds.getKey());
                    if(assignVehicleData != null){
                            driverId = assignVehicleData.getDriverId();
                            stat = assignVehicleData.getStatus();
                            Log.d("Assign vehicle", assignVehicleData.getDriverId());
                        Log.d("driver ID", stat);


                    }
                    else {
                        driverId = "";
                        Log.d("driverId",driverId);
                        Log.d("driverId","fuck off");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
