package com.example.smartfareadmin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.smartfareadmin.AdminActivity;
import com.example.smartfareadmin.R;
import com.example.smartfareadmin.dataObjects.AssignVehicleData;
import com.example.smartfareadmin.dataObjects.CodeObjects;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
          if(resultCode == RESULT_OK){
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



    @OnClick({R.id.buttonAdmin})
    public void adminbtn(){
        displaySignInButtons();

    }

    public void verifyDriver(){
        displaySignInButtons();
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



}
