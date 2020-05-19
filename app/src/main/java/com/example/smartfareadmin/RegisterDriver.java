package com.example.smartfareadmin;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartfareadmin.activities.Login;
import com.example.smartfareadmin.dataObjects.AssignVehicleData;
import com.example.smartfareadmin.dataObjects.DriverDeal;
import com.example.smartfareadmin.dataObjects.VehicleData;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterDriver extends AppCompatActivity {

    @BindView(R.id.user_name_text)
    TextInputLayout name_text;

    @BindView(R.id.phone_text)
    TextInputLayout phone_text;

    @BindView(R.id.email_text)
    TextInputLayout email_text;

    @BindView(R.id.address_text)
    TextInputLayout address_text;

    @BindView(R.id.display_text)
    TextInputLayout display_text;

    @BindView(R.id.driverLicense_text)
    TextInputLayout driverLicenseNum;

    @BindView(R.id.licenseExpire_text)
    TextInputLayout licenseExpire;

    @BindView(R.id.img_driver)
    ImageView img_driver;
    String status;



    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    DriverDeal driverDeal;
    VehicleData vehicleData;
    private static final int picture_code = 1;
    public static ArrayList<VehicleData> array;
    String driverId, vehicleId, dEmail, dName;
    AssignVehicleData assignVehicleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);

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


        FirebaseUtils.openFirebaseUtils("drivers profile", this);

        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;
        firebaseAuth = FirebaseAuth.getInstance();
        array = new ArrayList<VehicleData>();
        vehicleData = new VehicleData();
        assignVehicleData = new AssignVehicleData();


       // populateSpinner(databaseReference);

        Bundle bundle = getIntent().getExtras();
        driverId = bundle.getString("userId");




        Intent intent = getIntent();
        DriverDeal dDeals = (DriverDeal) intent.getSerializableExtra("drivers");

        if(dDeals == null){
            dDeals = new DriverDeal();
        }
        this.driverDeal = dDeals;

        if(driverId.equals("")){
            FirebaseUser user = firebaseAuth.getCurrentUser();

            name_text.getEditText().setText(user.getDisplayName());
            phone_text.getEditText().setText(dDeals.getPhoneNumber());
            email_text.getEditText().setText(user.getEmail());
            address_text.getEditText().setText(dDeals.getAddress());
            display_text.getEditText().setText(user.getPhoneNumber());
            driverLicenseNum.getEditText().setText(dDeals.getDriverLicense());
            licenseExpire.getEditText().setText(dDeals.getLicenseExpireDate());
            showImage(dDeals.getImageUrl());
        }else {

            name_text.getEditText().setText(dDeals.getName());
            phone_text.getEditText().setText(dDeals.getPhoneNumber());
            email_text.getEditText().setText(dDeals.getEmail());
            address_text.getEditText().setText(dDeals.getAddress());
            display_text.getEditText().setText(dDeals.getDisplayName());
            driverLicenseNum.getEditText().setText(dDeals.getDriverLicense());
            licenseExpire.getEditText().setText(dDeals.getLicenseExpireDate());
            showImage(dDeals.getImageUrl());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.admin, menu);

        MenuItem itemAdd = menu.findItem(R.id.action_add);
        itemAdd.setVisible(false);

        if(driverId.equals("")){
            MenuItem itemDelete = menu.findItem(R.id.action_delete);
            itemDelete.setVisible(false);

            MenuItem itemVerify = menu.findItem(R.id.action_verify);
            itemVerify.setVisible(false);

        }else {

            MenuItem itemSave = menu.findItem(R.id.action_save);
            itemSave.setVisible(false);

            MenuItem itemDelete = menu.findItem(R.id.action_delete);
            itemDelete.setVisible(true);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_save){
            status = "-1";
            saveDriver();
        }

        if(id == R.id.action_delete){
            deleteDriver();
        }

        if(id == R.id.action_verify){
            status = "0";
            saveDriver();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.imgAttach)
    public void imgAttach(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"select Image"), picture_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == picture_code && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Log.d("Uri", uri.toString());

            StorageReference sRef = FirebaseUtils.storageReference.child("drivers_pictures").child(uri.getLastPathSegment());
            sRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            driverDeal.setImageUrl(url);
                            showImage(url);
                       }
                   });
                }
            });

        }else {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveDriver(){

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){

            if(name_text.getEditText().getText().toString().equals("")){

                name_text.getEditText().setError("Required!");
                name_text.getEditText().setFocusable(true);
            }
            else if(phone_text.getEditText().getText().toString().equals("")){
                phone_text.getEditText().setError("Required!");
                phone_text.getEditText().setFocusable(true);
            }
            else if(email_text.getEditText().getText().toString().equals("")){
                email_text.getEditText().setError("Required!");
                email_text.getEditText().setFocusable(true);
            }
            else if(address_text.getEditText().getText().toString().equals("")){
                address_text.getEditText().setError("Required!");
                address_text.getEditText().setFocusable(true);
            }
            else if(display_text.getEditText().getText().toString().equals("")){
                display_text.getEditText().setError("Required!");
                display_text.getEditText().setFocusable(true);
            }
            else if(driverLicenseNum.getEditText().getText().toString().equals("")){
                driverLicenseNum.getEditText().setError("Required!");
                driverLicenseNum.getEditText().setFocusable(true);
            }
            else if(licenseExpire.getEditText().getText().toString().equals("")){
                licenseExpire.getEditText().setError("Required!");
                licenseExpire.getEditText().setFocusable(true);
            }
            else {
                driverDeal.setName(name_text.getEditText().getText().toString());
                driverDeal.setPhoneNumber(phone_text.getEditText().getText().toString());
                driverDeal.setEmail(email_text.getEditText().getText().toString());
                driverDeal.setAddress(address_text.getEditText().getText().toString());
                driverDeal.setDisplayName(display_text.getEditText().getText().toString());
                driverDeal.setDriverLicense(driverLicenseNum.getEditText().getText().toString());
                driverDeal.setLicenseExpireDate(licenseExpire.getEditText().getText().toString());
                driverDeal.setStatus(status);

                if(driverId.equals("")){
                    driverId = user.getUid();

                        if(driverDeal.getId() == null){

                            Date c = Calendar.getInstance().getTime();
                            System.out.println("Current time => " + c);

                            SimpleDateFormat df =  new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
                            String formattedDate = df.format(c);

                            driverDeal.setRegistrationDate(formattedDate);

                            databaseReference.child(user.getUid()).push().setValue(driverDeal);
                            Toast.makeText(this, "Profile Information Added, Thank You", Toast.LENGTH_SHORT).show();
                            clearFields();
                            firebaseAuth.signOut();
                            Intent intent = new Intent(this, Login.class);
                            startActivity(intent);
                            finish();
                        }else {

                            driverDeal.setRegistrationDate(driverDeal.getRegistrationDate());
                            databaseReference.child(user.getUid()).child(driverDeal.getId()).setValue(driverDeal);

                        }
                }
                else{
                    driverDeal.setRegistrationDate(driverDeal.getRegistrationDate());
                    databaseReference.child(driverId).child(driverDeal.getId()).setValue(driverDeal);
                    Toast.makeText(this, "Drivers Profile Information Updated!", Toast.LENGTH_SHORT).show();
                    backToList();
                }

            }

        }
            else {
                Toast.makeText(this, "User Account not found", Toast.LENGTH_SHORT).show();
            }
    }

    public void backToList(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Pending Drivers");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void deleteDriver(){
        if(driverDeal.getId() == null){
            Toast.makeText(this, "No Driver is selected", Toast.LENGTH_SHORT).show();
        }
        else {
            driverDeal.setStatus("-1");
            databaseReference.child(driverId).removeValue();
            Toast.makeText(this, "Driver Deleted!", Toast.LENGTH_SHORT).show();
            backToList();
        }
    }

    public void clearFields(){
        name_text.getEditText().setText("");
        phone_text.getEditText().setText("");
        email_text.getEditText().setText("");
        address_text.getEditText().setText("");
        display_text.getEditText().setText("");
        driverLicenseNum.getEditText().setText("");
        licenseExpire.getEditText().setText("");
    }

    //method to fetch data from firebase to populate  spinner
    public void populateSpinner(DatabaseReference databaseReference){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("vehicles details");

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> titleList = new ArrayList<String>();
                titleList.add("Select Vehicle");
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    VehicleData vDeals = dataSnapshot1.getValue(VehicleData.class);
                    if(vDeals.getStatus().equals("0")){
                        vDeals.setId(dataSnapshot1.getKey());
                        String vehicle = vDeals.getRegistrationNumber();
                        array.add(vDeals);
                        titleList.add(vehicle);
                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RegisterDriver.this, android.R.layout.simple_spinner_item, titleList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
               // vehicleNumber.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterDriver.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void updateVehicle(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("vehicles details");
        if(vehicleData.getId() != null){
            dRef.child(vehicleData.getId()).setValue(vehicleData);
            saveAssignVehicle();
        }
        else {
            Toast.makeText(this, "Vehicle is null!", Toast.LENGTH_SHORT).show();
        }
    }


    public void saveAssignVehicle(){

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df =  new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
        String formattedDate = df.format(c);

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("assigned vehicles");
        assignVehicleData.setDriverId(driverId);
        assignVehicleData.setVehicleId(vehicleId);
        assignVehicleData.setStatus("1");
        assignVehicleData.setcDate(formattedDate);

        dRef.push().setValue(assignVehicleData);
        Log.d("assignVehicle","ok saved");
    }


    public void showImage(String url){
        if(url != null && url.isEmpty() == false){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(img_driver.getContext())
                    .load(url)
                    .resize(250, 250)
                    .centerCrop()
                    .into(img_driver);
            Log.d("uri not null", url);
        }

    }


}
