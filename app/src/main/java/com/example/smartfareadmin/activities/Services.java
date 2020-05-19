package com.example.smartfareadmin.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartfareadmin.KeyValues;
import com.example.smartfareadmin.R;
import com.example.smartfareadmin.TripDetails;
import com.example.smartfareadmin.dataObjects.SevicesDeal;
import com.example.smartfareadmin.fragments.MapsActivity;
import com.example.smartfareadmin.notification.Token;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Services extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static Double price_per_km;
    private static Double min_price;
    private static Double pricePerMin;
    private static Double basePrice;

    private  static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private String status;
    String mUid;

    public static ArrayList<SevicesDeal> array;

    @BindView(R.id.Servicesspinner)
    Spinner serviceSpinner;

    @BindView(R.id.headerText)
    TextView CardHeaderText;

    @BindView(R.id.bodyText)
    TextView CardBodyText;

    @BindView(R.id.btnMapView)
    Button btnMapView;

    @BindView(R.id.img_service)
    ImageView img_services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        FirebaseUtils.openFirebaseUtils("services", this);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.mtoolbar);
        toolbar.setTitle("SmartCab Gh");
        setSupportActionBar(toolbar);

        populateSpinner(FirebaseUtils.databaseReference);
        serviceSpinner.setOnItemSelectedListener(this);
        btnMapView.setVisibility(View.INVISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();

        array= new ArrayList<SevicesDeal>();

        checkUserStatus();
        ///update token
        updateToken(FirebaseInstanceId.getInstance().getToken());

        btnMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Status clicked", status);
                if(status.equals("1")){
                    Intent MapsIntent = new Intent(Services.this, MapsActivity.class);
                    MapsIntent.putExtra(KeyValues.getPRICE(), price_per_km);
                    MapsIntent.putExtra("minPrice", min_price);
                    MapsIntent.putExtra("pricePerMin", pricePerMin);
                    MapsIntent.putExtra("basePrice", basePrice);
                    startActivity(MapsIntent);
                    finish();
                }

                if(status.equals("0")){
                    Toast.makeText(Services.this, "this services is not activated yet!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        Intent LoginIntent = new Intent(Services.this, Login.class);
                        startActivity(LoginIntent);
                        finish();
                    }
                });

    }

    public void updateToken(String token){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Token mtoken = new Token(token);

        dRef.child(mUid).setValue(mtoken);
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

     //method to fetch data from firebase to populate  spinner
    public void populateSpinner(DatabaseReference databaseReference){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> titleList = new ArrayList<String>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    SevicesDeal sdeals = dataSnapshot1.getValue(SevicesDeal.class);
                    sdeals.setId(dataSnapshot1.getKey());
                    String services = sdeals.getName();
                    array.add(sdeals);
                    titleList.add(services);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Services.this, android.R.layout.simple_spinner_item, titleList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                serviceSpinner.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Services.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);

        MenuItem refreshItem = menu.findItem(R.id.item_refresh);
        refreshItem.setVisible(false);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.item_logout){
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            Intent LoginIntent = new Intent(Services.this, Login.class);
                            startActivity(LoginIntent);
                            finish();
                        }
                    });

            return true;
        }
        if(id == R.id.item_about ){
            Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.item_bookingInfo){
            Intent intent = new Intent(this, TripDetails.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // String selected = parent.getItemAtPosition(position).toString();
        int item = parent.getSelectedItemPosition();

            SevicesDeal deals = array.get(item);
            String name = deals.getName();
            String desc = deals.getDescription();
            String kmPrice = deals.getPrice_per_km();
            String minPrice = deals.getMin_price();
            String Price_Per_Min  = deals.getPrice_per_min();
            String base_price = deals.getBase_price();
            status = deals.getStatus();

            Log.d("Status", status);

            if(status.equals("1")){
                price_per_km = Double.parseDouble(kmPrice);
                min_price = Double.parseDouble(minPrice);
                pricePerMin = Double.parseDouble(Price_Per_Min);
                basePrice = Double.parseDouble(base_price);
                showImage(deals.getImageUrl());

                Log.d("Price per Min", Price_Per_Min);
            }

            price_per_km = Double.parseDouble(kmPrice);
            Log.d("Price Per Km", kmPrice);
            CardHeaderText.setText(name.toUpperCase());
            CardBodyText.setText(desc);
            showImage(deals.getImageUrl());

            btnMapView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showImage(String url){
        if(url != null && url.isEmpty() == false){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(img_services.getContext())
                    .load(url)
                    .resize(150, 150)
                    .centerCrop()
                    .into(img_services);
            Log.d("uri not null", url);
        }

    }

}
