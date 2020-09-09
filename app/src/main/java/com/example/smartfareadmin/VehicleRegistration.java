package com.example.smartfareadmin;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import com.example.smartfareadmin.dataObjects.VehicleData;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VehicleRegistration extends AppCompatActivity {

    private static final int PICTURE_CODE = 1 ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    VehicleData vehicleData;

    @BindView(R.id.v_tag_text)
    TextInputLayout tagText;

    @BindView(R.id.v_text_centralLock)
    TextInputLayout centralLockText;

    @BindView(R.id.v_text_chassis)
    TextInputLayout chassisText;

    @BindView(R.id.v_text_currentOwnership)
    TextInputLayout currentOwnershipText;

    @BindView(R.id.v_text_engineNumber)
    TextInputLayout engineNumberText;

    @BindView(R.id.v_text_engineType)
    TextInputLayout engineType;

    @BindView(R.id.v_text_exteriorColor)
    TextInputLayout exteriorColorText;

    @BindView(R.id.v_text_fuelLevel)
    TextInputLayout fuelLevelText;

    @BindView(R.id.v_text_fuelType)
    TextInputLayout fuelTypeText;

    @BindView(R.id.v_text_insuranceDate)
    TextInputLayout insuranceDateText;

    @BindView(R.id.v_text_insurancePolicy)
    TextInputLayout insurancePolicyText;

    @BindView(R.id.v_text_makeModel)
    TextInputLayout makeModelText;

    @BindView(R.id.v_text_interiorColor)
    TextInputLayout interiorColorText;

    @BindView(R.id.v_text_manufacturingDate)
    TextInputLayout manufacturingDateText;

    @BindView(R.id.v_text_regNumber)
    TextInputLayout regNumberText;

    @BindView(R.id.v_text_roadWorthDate)
    TextInputLayout roadWorthDateText;

    @BindView(R.id.v_text_steering)
    TextInputLayout steeringText;

    @BindView(R.id.v_text_trackingDevice)
    TextInputLayout trackingDeviceText;

    @BindView(R.id.v_text_transmission)
    TextInputLayout transmissionText;

    @BindView(R.id.v_text_tyresType)
    TextInputLayout tyresTypeText;

    @BindView(R.id.img_vehicle)
    ImageView img_vehicle;

    @BindView(R.id.search_item)
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_registration);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.mtoolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("SmartCab Gh");

        setSupportActionBar(toolbar);

        mSearchView.setVisibility(View.GONE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FirebaseUtils.openFirebaseUtils("vehicles details", this);
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;

        Intent intent = getIntent();
        VehicleData vObjects = (VehicleData) intent.getSerializableExtra("Vehicle");

        if(vObjects == null){
            vObjects = new VehicleData();
        }

        this.vehicleData = vObjects;

        tagText.getEditText().setText(vObjects.getTag());
        makeModelText.getEditText().setText(vObjects.getMakeModel());
        regNumberText.getEditText().setText(vObjects.getRegistrationNumber());
        currentOwnershipText.getEditText().setText(vObjects.getCurrentOwnership());
        transmissionText.getEditText().setText(vObjects.getTransmission());
        engineType.getEditText().setText(vObjects.getEngineType());
        engineNumberText.getEditText().setText(vObjects.getEngineNumber());
        tyresTypeText.getEditText().setText(vObjects.getTyresType());
        trackingDeviceText.getEditText().setText(vObjects.getTrackingDevice());
        steeringText.getEditText().setText(vObjects.getSteering());
        manufacturingDateText.getEditText().setText(vObjects.getManufacturingDate());
        interiorColorText.getEditText().setText(vObjects.getInteriorColor());
        exteriorColorText.getEditText().setText(vObjects.getExteriorColor());
        insurancePolicyText.getEditText().setText(vObjects.getInsurancePolicy());
        insuranceDateText.getEditText().setText(vObjects.getInsuranceDate());
        roadWorthDateText.getEditText().setText(vObjects.getRoadWorthyCertificateDate());
        fuelTypeText.getEditText().setText(vObjects.getFuelType());
        fuelLevelText.getEditText().setText(vObjects.getFuelLevel());
        centralLockText.getEditText().setText(vObjects.getCentralLock());
        chassisText.getEditText().setText(vObjects.getChassisNumber());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin, menu);

        MenuItem item = menu.findItem(R.id.action_add);
        item.setVisible(false);

        MenuItem itemRefresh = menu.findItem(R.id.action_refresh);
        itemRefresh.setVisible(false);

        MenuItem itemVerify = menu.findItem(R.id.action_verify);
        itemVerify.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_save){
            saveVehicleDetails();
        }
        if(item.getItemId() == R.id.action_delete){
            deleteVehicle();
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.imgAttachV)
    public void getImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICTURE_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICTURE_CODE && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Log.d("Uri", uri.toString());

            StorageReference sRef = FirebaseUtils.storageReference.child("vehicle_pictures").child(uri.getLastPathSegment());
            sRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            vehicleData.setImageUrl(url);
                            showImage(url);
                        }
                    });
                }
            });

        }else {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveVehicleDetails(){

        vehicleData.setTag(tagText.getEditText().getText().toString());
        vehicleData.setCentralLock(centralLockText.getEditText().getText().toString());
        vehicleData.setChassisNumber(chassisText.getEditText().getText().toString());
        vehicleData.setCurrentOwnership(currentOwnershipText.getEditText().getText().toString());
        vehicleData.setEngineNumber(engineNumberText.getEditText().getText().toString());
        vehicleData.setEngineType(engineType.getEditText().getText().toString());
        vehicleData.setExteriorColor(exteriorColorText.getEditText().getText().toString());
        vehicleData.setInteriorColor(interiorColorText.getEditText().getText().toString());
        vehicleData.setFuelLevel(fuelLevelText.getEditText().getText().toString());
        vehicleData.setFuelType(fuelTypeText.getEditText().getText().toString());
        vehicleData.setInsuranceDate(insuranceDateText.getEditText().getText().toString());
        vehicleData.setInsurancePolicy(insurancePolicyText.getEditText().getText().toString());
        vehicleData.setMakeModel(makeModelText.getEditText().getText().toString());
        vehicleData.setManufacturingDate(manufacturingDateText.getEditText().getText().toString());
        vehicleData.setRoadWorthyCertificateDate(roadWorthDateText.getEditText().getText().toString());
        vehicleData.setSteering(steeringText.getEditText().getText().toString());
        vehicleData.setTrackingDevice(trackingDeviceText.getEditText().getText().toString());
        vehicleData.setTyresType(tyresTypeText.getEditText().getText().toString());
        vehicleData.setTransmission(transmissionText.getEditText().getText().toString());
        vehicleData.setRegistrationNumber(regNumberText.getEditText().getText().toString());
        vehicleData.setStatus("0");

        if(vehicleData.getId() == null){
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);

            vehicleData.setRegistrationDate(formattedDate);
            databaseReference.push().setValue(vehicleData);

            Toast.makeText(this, "Vehicle Added", Toast.LENGTH_SHORT).show();
            clear();
        }
        else {
            vehicleData.setRegistrationDate(vehicleData.getRegistrationDate());
            databaseReference.child(vehicleData.getId()).setValue(vehicleData);
            onBackPressed();
            Toast.makeText(this, "Vehicle Updated", Toast.LENGTH_SHORT).show();
        }


    }

    public void clear(){
        tagText.getEditText().setText("");
        makeModelText.getEditText().setText("");
        regNumberText.getEditText().setText("");
        currentOwnershipText.getEditText().setText("");
        transmissionText.getEditText().setText("");
        engineType.getEditText().setText("");
        engineNumberText.getEditText().setText("");
        tyresTypeText.getEditText().setText("");
        trackingDeviceText.getEditText().setText("");
        steeringText.getEditText().setText("");
        manufacturingDateText.getEditText().setText("");
        interiorColorText.getEditText().setText("");
        exteriorColorText.getEditText().setText("");
        insurancePolicyText.getEditText().setText("");
        insuranceDateText.getEditText().setText("");
        roadWorthDateText.getEditText().setText("");
        fuelTypeText.getEditText().setText("");
        fuelLevelText.getEditText().setText("");
        centralLockText.getEditText().setText("");
        chassisText.getEditText().setText("");
    }

    private void deleteVehicle() {
        if(vehicleData.getId() == null){
            Toast.makeText(this, "Vehicle Not Available", Toast.LENGTH_SHORT).show();
        }else{
            databaseReference.child(vehicleData.getId()).removeValue();
            Toast.makeText(this, "Vehicle deleted", Toast.LENGTH_SHORT).show();
             onBackPressed();
        }

    }

    public void backToList(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","Vehicles");
        startActivity(intent);
        finish();
    }

    public void showImage(String url){
        if(url != null && url.isEmpty() == false){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(img_vehicle.getContext())
                    .load(url)
                    .resize(250, 250)
                    .centerCrop()
                    .into(img_vehicle);
            Log.d("uri not null", url);
        }

    }
}
