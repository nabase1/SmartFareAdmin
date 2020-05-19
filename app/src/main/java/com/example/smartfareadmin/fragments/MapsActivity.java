package com.example.smartfareadmin.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.smartfareadmin.KeyValues;
import com.example.smartfareadmin.R;
import com.example.smartfareadmin.TripDetails;
import com.example.smartfareadmin.activities.Constants;
import com.example.smartfareadmin.activities.Login;
import com.example.smartfareadmin.activities.Services;
import com.example.smartfareadmin.adapters.PlaceAutocompleteAdapter;
import com.example.smartfareadmin.dataObjects.Bookings;
import com.example.smartfareadmin.notification.APIServices;
import com.example.smartfareadmin.notification.Client;
import com.example.smartfareadmin.notification.Data;
import com.example.smartfareadmin.notification.Response;
import com.example.smartfareadmin.notification.Sender;
import com.example.smartfareadmin.notification.Token;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static final String TAG = "MapActivity";
    private GoogleMap mMap;
    Location currentlocation;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    private boolean userStatus = true;
    private static PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private static LatLngBounds latLngBounds;
    private float distanceInKm;
    private String destName;
    private double sprice;
    int totalDuration;
    private String distance,duration;
    private double toLat,toLng,fromLat,fromLng;
    private String snippest = "Drag to change location";
    boolean open = false;
    int stat = 1;
    Marker destMarker, initialMarker;
    MarkerOptions options;
    private String locationName;
    LatLng startLatlng, endLatLng, statingPoint, currentPoint;
    List<LatLng> latlngPoints;
    List<Polyline> polylines = new ArrayList<Polyline>();

    FirebaseAuth mAuth;
    private long backPressedTime;
    AutocompleteSupportFragment autocompleteFragment, fromAutocompleteFragment;
    PlacesClient placesClient;
    com.google.android.libraries.places.api.model.Place lplace;
    Bookings bDeals;
    BitmapDescriptor fromIcon;
    AlertDialog alertDialog;

    APIServices apiServices;
    Boolean notify = false;
    String mUid;


    @BindView(R.id.meterConstraintLayout)
    ConstraintLayout constraintLayoutInfo;

    @BindView(R.id.ttxtFrom)
    TextView textFrom;

    @BindView(R.id.textDest)
    TextView textDest;

    @BindView(R.id.textprice)
    TextView textPrice;

    @BindView(R.id.hideLayoutbtn)
    Button hideLayoutbtn;

    @BindView(R.id.dateTxt)
    EditText dateText;

    @BindView(R.id.timeTxt)
    EditText timeText;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);
        fromIcon = BitmapDescriptorFactory.fromResource(R.drawable.markersmartcab);

        FirebaseUtils.openFirebaseUtils("bookings", this);
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;
        mAuth = FirebaseAuth.getInstance();

        mUid = mAuth.getCurrentUser().getUid();

        //create api services
        apiServices = Client.getRetrofit("https://fcm.googleapis.com/").create(APIServices.class);


        constraintLayoutInfo.setVisibility(View.INVISIBLE);
        latlngPoints = new ArrayList<LatLng>();
        latLngBounds = new LatLngBounds(
                new LatLng(-47, -160), new LatLng(71, 136));

        currentlocation = new Location("");

        hideLayoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slider(constraintLayoutInfo, open);
            }
        });

        String apiKey = "AIzaSyDYYmE1NW1InrVKG0xjEiowMWj1wEjo3Y8";

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(this);


        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        fromAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.fromautocomplete_fragment);


        //setting the hint to the textField
        fromAutocompleteFragment.setHint("Current Location");
        fromAutocompleteFragment.setText(locationName);
        autocompleteFragment.setHint("Where to?");



        // Specify the types of place data to return.
        fromAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        //filters the google place search to only ghana
        fromAutocompleteFragment.setCountry("GH");
        autocompleteFragment.setCountry("GH");

        //event listener for the place selected
        fromAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mMap.clear();

                stat = 0;
                startLatlng = place.getLatLng();
                locationName = place.getName();
                Log.d("location Address", place.getAddress());

                currentPoint = place.getLatLng();
                Log.d("startLatlng", startLatlng.toString() + " " + locationName);
                fromLat = startLatlng.latitude;
                fromLng = startLatlng.longitude;

                mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatlng));

                if (latlngPoints != null) {
                    latlngPoints.clear();
                }
                latlngPoints.add(startLatlng);

                if(initialMarker != null){
                    initialMarker.remove();
                }

                initialMarker = mMap.addMarker(new MarkerOptions()
                        .position(startLatlng)
                        .title(locationName)
                        .draggable(true)
                        .snippet(snippest)
                        .icon(fromIcon));

                currentlocation.setLatitude(startLatlng.latitude);
                currentlocation.setLongitude(startLatlng.longitude);

                if (destMarker != null) {
                    Geolocate(lplace);
                }

            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(MapsActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                lplace = place;
                Geolocate(place);
                //Toast.makeText(MapsActivity.this, place.getName() +" " + place.getLatLng(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(MapsActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        Intent intent = getIntent();
        Bookings bookings = (Bookings) intent.getSerializableExtra("services");


        if (bookings == null) {
            bookings = new Bookings();
        }

        this.bDeals = bookings;

        // creating dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thank you For Booking");
        builder.setMessage("Confirmation Message Will be Sent To You Soon");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //creating dialog object
        alertDialog = builder.create();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.btnBook)
    public void onClickBooking(View view) {
        saveBookings();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            Intent LoginIntent = new Intent(MapsActivity.this, Login.class);
                            startActivity(LoginIntent);
                            finish();
                        }
                    });
            return true;
        }

        if (id == R.id.item_refresh) {
            stat = 1;
            resetMap(mMap, currentlocation);
        }
        if (id == R.id.item_about) {
            Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(id == R.id.item_bookingInfo){
            Intent intent = new Intent(this, TripDetails.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent ServiceIntent = new Intent(MapsActivity.this, Services.class);
        startActivity(ServiceIntent);
        finish();

    }

    @OnClick(R.id.dateTxt)
    public void showDate() {
        showDateDialog();
    }

    @OnClick(R.id.timeTxt)
    public void showTime() {
        showTimeDialog();
    }

    public void showDateDialog() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int rMonth = month +1;
        String date = dayOfMonth + " / " + rMonth + " / " + year;
        updateDate(date);
    }


    public void updateDate(String date) {
        dateText.setText(date);

    }

    public void showTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, min, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + " : " + minute;
        updateTime(time);
    }

    public void updateTime(String time) {
        timeText.setText(time);
    }


    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    //method to reset the map
    public void resetMap(GoogleMap map, Location location) {
       if(map != null){
           map.clear();
           textFrom.setText("");
           textDest.setText("");
           textPrice.setText("");
           autocompleteFragment.setText("");
           fromAutocompleteFragment.setText("");
           bDeals.setName("");
           bDeals.setDistance("");
           bDeals.setAmount("");
           bDeals.setTo("");
           bDeals.setFrom("");
           latlngPoints.removeAll(latlngPoints);
           destMarker = null;
           polylines.removeAll(polylines);


           if (open) {
               slider(constraintLayoutInfo, open);
           }

           if(stat == 1){
               startLatlng = statingPoint;

               map.setMyLocationEnabled(true);
           }else {
               startLatlng = currentPoint;
           }

           mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatlng));




           if (latlngPoints != null) {
               latlngPoints.clear();
           }
           latlngPoints.add(startLatlng);

           try {
               locationName = new Geocoder(this).getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getAddressLine(0);
               //locationName = new Geocoder(MapsActivity.this).getFromLocation(startLatlng.latitude, startLatlng.longitude, 1).get(0).getAddressLine(0);
           } catch (IOException e) {
               Log.d(TAG, "error" + e.getMessage());
           }

           if (initialMarker != null) {
               initialMarker.remove();
           }

           initialMarker = mMap.addMarker(new MarkerOptions()
                   .position(startLatlng)
                   .title(locationName)
                   .draggable(true)
                   .snippet(snippest)
                   .icon(fromIcon));

           currentlocation.setLatitude(startLatlng.latitude);
           currentlocation.setLongitude(startLatlng.longitude);

           if (destMarker != null) {
               Log.d("dest marker", "its not null" + destMarker.getTitle());
           } else {
               Log.d("empty marker", "its null");
           }

       }else {
           toast("Refreshing Map");
       }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void saveBookings() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

       DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("user profile");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = "";
                String phone = "";
                String userId = user.getUid();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    Log.d("User key", key);
                    if(key.equals(userId)){
                        for(DataSnapshot subKey : ds.getChildren()){
                            String ukey = subKey.getKey();
                            Log.d("sub key", ukey);
                            name = subKey.child("username").getValue().toString();
                            phone = subKey.child("userPhone").getValue().toString();
                            Log.d("values", name + " " + phone);
                        }

                        Log.d("email details", user.getDisplayName() + " " + user.getPhoneNumber());
                        bDeals.setName(name);
                        bDeals.setPhoneNumber(phone);
                        bDeals.setFrom(textFrom.getText().toString());
                        bDeals.setTo(textDest.getText().toString());
                        bDeals.setPick_up_date( dateText.getText().toString());
                        bDeals.setPick_up_time(timeText.getText().toString());
                        bDeals.setAmount(String.valueOf(sprice));
                        bDeals.setStatus("0");
                        bDeals.setDistance(String.valueOf(distanceInKm));
                        bDeals.setToLatitude(String.valueOf(toLat));
                        bDeals.setToLongitude(String.valueOf(toLng));
                        bDeals.setFromLatitude(String.valueOf(fromLat));
                        bDeals.setFromLongitude(String.valueOf(fromLng));

                        if(TextUtils.isEmpty(dateText.getText()) || TextUtils.isEmpty(timeText.getText())){
                            toast("Please Select Date or Time");
                        }
                        else if((textFrom.getText().toString()).equals("") || (textDest.getText().toString()).equals("")){
                            toast("Destination or From is empty!");
                        }

                        else if(bDeals.getName().equals("")){
                            toast("Name or Contact Informatiin is empty!");
                        }
                        else if(bDeals.getPhoneNumber().equals("")){
                            toast("No Contact is found");
                        }
                        else {
                            if(bDeals.getId() == null){
                                databaseReference.child(userId).push().setValue(bDeals);
                                resetMap(mMap, currentlocation);
                                alertDialog.show();
                            }
                            else {
                                databaseReference.child(userId).child(bDeals.getId()).setValue(bDeals);
                                toast("Booking Updated");
                            }
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }
        else {
           toast("Account Info not found!");
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        getLocationPermission();

        if (mMap != null) {
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                    mMap.clear();
                    stat = 0;
                    startLatlng = marker.getPosition();
                    currentPoint = marker.getPosition();
                    fromLat = startLatlng.latitude;
                    fromLng = startLatlng.longitude;

                    if (latlngPoints != null) {
                        latlngPoints.clear();
                    }
                    latlngPoints.add(startLatlng);

                    try {
                        locationName = new Geocoder(MapsActivity.this).getFromLocation(startLatlng.latitude, startLatlng.longitude, 1).get(0).getAddressLine(0);
                    } catch (IOException e) {
                        Log.d(TAG, "error" + e.getMessage());
                    }

                    if (initialMarker != null) {
                        initialMarker.remove();
                    }

                    initialMarker = mMap.addMarker(new MarkerOptions()
                            .position(startLatlng)
                            .title(locationName)
                            .draggable(true)
                            .snippet(snippest)
                            .icon(fromIcon));

                    currentlocation.setLatitude(startLatlng.latitude);
                    currentlocation.setLongitude(startLatlng.longitude);

                    if (destMarker != null) {
                        Geolocate(lplace);
                    }

                }
            });
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        currentlocation = location;

        statingPoint = new LatLng(location.getLatitude(), location.getLongitude());
        startLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        fromLat = startLatlng.latitude;
        fromLng = startLatlng.longitude;

        if (latlngPoints != null) {
            latlngPoints.clear();
        }
        latlngPoints.add(startLatlng);

        try {
            locationName = new Geocoder(this).getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getAddressLine(0);
        } catch (IOException e) {
            Log.d(TAG, "error" + e.getMessage());
        }

        initialMarker = mMap.addMarker(new MarkerOptions()
                .position(startLatlng)
                .title(locationName)
                .draggable(true)
                .snippet(snippest)
                .icon(fromIcon));

        currentlocation.setLatitude(location.getLatitude());
        currentlocation.setLongitude(location.getLongitude());

        Log.d(TAG, "curent location " + location);


        //stop location update
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        }


    }


    protected synchronized void BuildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            BuildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                BuildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
            }

        }
    }

    /*
        private void hiddeKeys(){
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    */
    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

    }

    private void slider(ConstraintLayout constraintLayout, boolean opened) {

        TranslateAnimation animate;
        if (!open) {
            constraintLayout.setVisibility(View.VISIBLE);
            animate = new TranslateAnimation(
                    0,
                    0,
                    constraintLayout.getHeight(),
                    0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            constraintLayout.startAnimation(animate);

        } else {
            constraintLayout.setVisibility(View.INVISIBLE);
            animate = new TranslateAnimation(
                    0,
                    0,
                    0,
                    constraintLayout.getHeight());
            animate.setDuration(500);
            animate.setFillAfter(true);
            constraintLayout.startAnimation(animate);

        }

        open = !open;


    }



    /*===================================drawing route=====================================================*/

    public void drawRouteOnMap(LatLng origin, LatLng dest, List<LatLng> MarkerPoints) {
        // Checks, whether start and end locations are captured
        if (MarkerPoints.size() >= 2) {
            origin = MarkerPoints.get(0);
            dest = MarkerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getUrl(origin, dest);
            Log.d("onMapClick", url.toString());
            FetchUrl FetchUrl = new FetchUrl();

            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }


    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        //api key
        String apikey = "AIzaSyCqQRerngEVjoC5jsgGC3oI5TQiATARfKs";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + apikey;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions lineOptions = null;
            int HoursToMins = 0;
            int minutes = 0;
            int days = 0;
            points.clear();
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                //latlngPoints = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the latlngPoints in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if(j==0){ // Get distance from the list
                        distance = (String)point.get("distance");

                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        String timeItems [] = {duration};

                       //Converting time from hours into minutes
                        for(int d = 0; d < timeItems.length; d++){
                            if(timeItems[d].contains(" days")){
                                days = Integer.valueOf(timeItems[d].substring(0, timeItems[d].indexOf(" days")));
                                Log.d("days", String.valueOf(days));
                            }

                            if(timeItems[d].contains(" hours")){
                                int hours = Integer.valueOf(timeItems[d].substring(0, timeItems[d].indexOf(" hours")));
                                HoursToMins = hours * 60;
                                Log.d("HoursToMins", String.valueOf(HoursToMins));
                            }
                            if(timeItems[d].contains(" hour")){
                                int hour = Integer.valueOf(timeItems[d].substring(0, timeItems[d].indexOf(" hour")));
                                HoursToMins = hour * 60;
                                Log.d("HoursToMins", String.valueOf(HoursToMins));
                            }

                            if(timeItems[d].contains(" mins")){
                                if(timeItems[d].indexOf(" mins") <= 3){
                                    minutes = Integer.valueOf(timeItems[d].substring(0, timeItems[d].indexOf(" mins")));
                                    Log.d("items less than 3", String.valueOf(minutes));
                                }
                                else {
                                    if(timeItems[d].contains("hours ")){
                                        minutes = Integer.valueOf(timeItems[d].substring(timeItems[d].indexOf("hours ") + 6,timeItems[d].indexOf(" mins")));
                                        Log.d("minutes", String.valueOf(minutes));
                                    }

                                    if(timeItems[d].contains("hour ")){
                                        minutes = Integer.valueOf(timeItems[d].substring(timeItems[d].indexOf("hour ") + 5,timeItems[d].indexOf(" mins")));
                                        Log.d("minutes", String.valueOf(minutes));
                                    }

                                }

                            }
                        }
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                totalDuration = HoursToMins + minutes;
                Log.d("Total Time In Mins", String.valueOf(totalDuration));

                // Adding all the latlngPoints in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                polylines.add(mMap.addPolyline(lineOptions));

                calculatePrice();
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    //method to search for location
    private void Geolocate(com.google.android.libraries.places.api.model.Place place) {

        if(polylines != null){
         resetMap(mMap, currentlocation);
        }

        Log.d(TAG, "geocoding...");

        Geocoder geocoder = new Geocoder(MapsActivity.this);

        endLatLng = place.getLatLng();
        destName = place.getName();
        toLat = endLatLng.latitude;
        toLng = endLatLng.longitude;

        if(latlngPoints.size() > 2){
            latlngPoints.remove(1);
        }
        latlngPoints.add(endLatLng);
        if (destMarker != null) {
            destMarker.remove();
        }

        BitmapDescriptor destinationIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker2smartcab);

        options = new MarkerOptions();

        options.position(endLatLng)
                .title(place.getName())
                .icon(destinationIcon);

        destMarker = mMap.addMarker(options);

        Location finalLocation = new Location("");
        finalLocation.setLatitude(endLatLng.latitude);
        finalLocation.setLongitude(endLatLng.longitude);

        //method to draw route callled hers///
        drawRouteOnMap(startLatlng, endLatLng, latlngPoints);

        distanceInKm = currentlocation.distanceTo(finalLocation) / 1000;//calculating the distance in KM


    }

    //calculating price
    public void calculatePrice(){
        Bundle bundle = getIntent().getExtras();
        double priceperKm = bundle.getDouble(KeyValues.getPRICE());
        double minPrice = bundle.getDouble("minPrice");
        double pricePerMin = bundle.getDouble("pricePerMin");


            FirebaseUtils.openFirebaseUtils("price switch", this);
            DatabaseReference myRef = FirebaseUtils.databaseReference;

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double price = 0;
                    String per_km = dataSnapshot.child("per km").getValue().toString();
                    String per_min = dataSnapshot.child("per min").getValue().toString();
                    String both = dataSnapshot.child("both").getValue().toString();

                    if(per_km.equals("1")){
                        if(distanceInKm < 1){
                            price = minPrice;
                            Log.d("price",String.valueOf(price));
                        }
                        else {
                            price = Math.round(distanceInKm * priceperKm);
                            Log.d("price in km",String.valueOf(price));
                        }
                    }

                    if(per_min.equals("1")){
                        Log.d("Price per min", String.valueOf(pricePerMin));
                        price = Math.round(totalDuration * pricePerMin);
                        Log.d("price in mins",String.valueOf(price));
                    }

                    textFrom.setText(locationName);
                    textDest.setText(destName);
                    textPrice.setText("GHc " + String.format("%.2f", price));
                    sprice = price;

                    Log.d("distance & time: ", distance + " " + duration );
                    Log.d("distance in Km", String.valueOf(distanceInKm) );
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
            Log.d("distance & time: ", distance + " " + duration );

            if(!open){
                slider(constraintLayoutInfo, open);
            }

    }


    // Draw polyline on map
    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.RED);
        polyOptions.width(5);
        polyOptions.addAll(list);

        List<Polyline> polylines = new ArrayList<Polyline>();
        if (polylines != null) {
            polylines.clear();
        }
        polylines.add(mMap.addPolyline(polyOptions));
        //mMap.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }

        //  final LatLng
    }


    private void sendNotification(String userId, String title, String body) {

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("AdminTokens");
        Query query = dRef.orderByKey().equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(mUid, title, body,  userId, R.drawable.smart_cab_logo);

                    Sender sender = new Sender(data, token.getToken());
                    apiServices.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(MapsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
