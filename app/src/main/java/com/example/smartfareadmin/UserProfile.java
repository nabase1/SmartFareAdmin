package com.example.smartfareadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartfareadmin.activities.Login;
import com.example.smartfareadmin.activities.Services;
import com.example.smartfareadmin.dataObjects.GetUserData;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfile extends AppCompatActivity {

    @BindView(R.id.user_name_text)
    TextInputLayout user_name_text;

    @BindView(R.id.user_contact_text)
    TextInputLayout user_contact;

    @BindView(R.id.verification_text)
    EditText verification_code_text;

    @BindView(R.id.sendVerificationButton)
    Button sendVerificationBtn;

    @BindView(R.id.verifyButton)
    Button verifyBtn;

    @BindView(R.id.ccp)
    CountryCodePicker countryCodePicker;

    ProgressDialog progressBar;
    private String uid;
    GetUserData uData;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String phoneNumber;
    private String  mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);


        progressBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUtils.openFirebaseUtils("user profile",this);
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;

        getUserProfile();


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                saveProfile();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(UserProfile.this, "Invalid Phone Number!", Toast.LENGTH_SHORT).show();

                user_name_text.setVisibility(View.GONE);
                countryCodePicker.setVisibility(View.VISIBLE);
                user_contact.setVisibility(View.VISIBLE);
                sendVerificationBtn.setVisibility(View.VISIBLE);

                verification_code_text.setVisibility(View.GONE);
                verifyBtn.setVisibility(View.GONE);
                progressBar.dismiss();

            }

            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Toast.makeText(UserProfile.this, "Verification Code Sent", Toast.LENGTH_SHORT).show();
                user_name_text.setVisibility(View.GONE);
                user_contact.setVisibility(View.GONE);
                countryCodePicker.setVisibility(View.GONE);
                sendVerificationBtn.setVisibility(View.GONE);

                verification_code_text.setVisibility(View.VISIBLE);
                verifyBtn.setVisibility(View.VISIBLE);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };

        Intent intent = getIntent();
        GetUserData getData = (GetUserData) intent.getSerializableExtra("user profile");

        if (getData == null) {
            getData = new GetUserData();
        }

        this.uData = getData;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.sendVerificationButton)
    public void saveUserProfile(){
        verifyPhoneNumber();
    }

    @OnClick(R.id.verifyButton)
    public void verifyNumber(){
        verifyCode();
    }


    public void getUserProfile(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            uid = user.getUid();
            String userName = user.getDisplayName();
            String userContact = user.getPhoneNumber();

            user_name_text.getEditText().setText(userName);
            user_contact.getEditText().setText(userContact);
        }
    }

    public void saveProfile(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            uData.setUsername(user_name_text.getEditText().getText().toString());
            uData.setUserPhone(phoneNumber);


            if ((user_name_text.getEditText().getText()).toString().equals("")) {
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
            }
            else if(user_contact.getEditText().getText().toString().equals("")){
                Toast.makeText(this, "Please Enter Your Contact", Toast.LENGTH_SHORT).show();
            }
            else {
                if(uData.getId() == null){
                    databaseReference.child(uid).push().setValue(uData);
                    progressBar.dismiss();
                    Intent mapIntent = new Intent(UserProfile.this, Services.class);
                    startActivity(mapIntent);
                    finish();

                }
                else {
                    databaseReference.child(uid).child(uData.getId()).setValue(uData);
                    Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
                }

            }
        }


    }

    public void verifyPhoneNumber(){

        progressBar.setTitle("Phone Verification");
        progressBar.setMessage("Please Wait While We Verify Contact");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        String countryCode = countryCodePicker.getFullNumberWithPlus();
        String number = user_contact.getEditText().getText().toString();

        phoneNumber =  countryCode + number;

        if ((user_name_text.getEditText().getText()).toString().equals("")) {
            user_name_text.getEditText().setError("User Name is required");
            user_name_text.getEditText().setFocusable(true);
            progressBar.dismiss();
        }
        else if(user_contact.getEditText().getText().toString().equals("")){
           user_contact.getEditText().setError("Phone Number is required");
           user_contact.setFocusable(true);
           progressBar.dismiss();
        }
        else {
            user_name_text.setVisibility(View.GONE);
            user_contact.setVisibility(View.GONE);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    callbacks);        // OnVerificationStateChangedCallbacks
        }

    }

    public void verifyCode(){

        progressBar.setTitle("Code Verification");
        progressBar.setMessage("Please Wait While We Verify the Code");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        String code = verification_code_text.getText().toString();
        verification_code_text.setVisibility(View.GONE);
        verifyBtn.setVisibility(View.GONE);
        if(TextUtils.isEmpty(code)){
            verification_code_text.setError("Verification Code is required");
            progressBar.dismiss();
        }else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential( mVerificationId, code);
            saveProfile();
        }



    }

}
