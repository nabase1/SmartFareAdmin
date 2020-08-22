package com.example.smartfareadmin.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.smartfareadmin.DriverConfirm;
import com.example.smartfareadmin.R;
import com.example.smartfareadmin.activities.Constants;
import com.example.smartfareadmin.activities.Login;
import com.example.smartfareadmin.dataObjects.AssignVehicleData;
import com.example.smartfareadmin.dataObjects.VehicleData;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private static final String TAG = "MapActivity";
    Location currentlocation;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    private boolean DrivweStatus = true;
    boolean mLocationPermissionGranted;
    AssignVehicleData assignVehicleData;
    VehicleData vehicleData;
    double toLat,toLng,fromLat,fromLng;
    String toLocationName, fromLocationName,currentName, driverId;
    List<LatLng> latlngPoints;
    LatLng startLatlng, fromLatLng, toLatlng,  endLatLng;
    BitmapDescriptor fromIcon;
    BitmapDescriptor destinationIcon;
    int totalDuration;
    private String distance,duration;
    List<Polyline> polylines = new ArrayList<Polyline>();
    FirebaseAuth mAuth;
    AutocompleteSupportFragment dto_autocomplete_fragment, dFrom_autocomplete_fragment;
    PlacesClient placesClient;
    Marker destMarker, initialMarker;
    MarkerOptions options;
    private String destName;

    @BindView(R.id.meterConstraintLayout)
    ConstraintLayout meterConstraint;

    @BindView(R.id.buttonPickUpRout)
    Button btnPickUpRoute;

    @BindView(R.id.buttonDestination)
    Button btnDest;

    @BindView(R.id.fareMeterSwitch)
    Switch fareMeterSwitch;

    @BindView(R.id.meterPriceTxt)
    TextView meterPriceText;

    @BindView(R.id.dEndtripbtn)
    Button dEndTripbtn;


    double totalWaiting = -1;
    final int incrementTime = 1000;
    double totalFare = 0;
    private volatile boolean stopThread = false;
    AlertDialog alertDialog;

    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SmartCab Gh");
        fromIcon = BitmapDescriptorFactory.fromResource(R.drawable.markersmartcab);
        destinationIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker2smartcab);


        mAuth = FirebaseAuth.getInstance();
        assignVehicleData = new AssignVehicleData();
        vehicleData = new VehicleData();
        latlngPoints = new ArrayList<LatLng>();
        driverId = mAuth.getCurrentUser().getUid();

        Bundle bundle = getIntent().getExtras();
        String drawRout = bundle.getString(Constants.DriverMap);
        if(drawRout.equals("nonRoute")){
            btnDest.setVisibility(View.GONE);
            btnPickUpRoute.setVisibility(View.GONE);
        }
        if(drawRout.equals("drawRout")){
            btnDest.setVisibility(View.GONE);
            btnPickUpRoute.setVisibility(View.VISIBLE);

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure You Want to End The Trip");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(DriverMap.this, DriverConfirm.class);
                intent.putExtra(Constants.TOTAL_FARE, totalFare);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();

      fareMeterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if(isChecked){
                  stopThread = false;
                  fareReader(incrementTime,meterPriceText,2,t);

              }
              else {
                  stopThread = true;
                  totalFare = Double.parseDouble(meterPriceText.getText().toString());
                  Log.d("waiting Fare: ", String.valueOf(totalFare));
              }
          }
      });

        String apiKey = "AIzaSyDYYmE1NW1InrVKG0xjEiowMWj1wEjo3Y8";

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(this);


        dto_autocomplete_fragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.dto_autocomplete_fragment);

        dFrom_autocomplete_fragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id. dFrom_autocomplete_fragment);


        //setting the hint to the textField
        dFrom_autocomplete_fragment.setHint("Current Location");
        dto_autocomplete_fragment.setHint("Where to?");



        // Specify the types of place data to return.
        dFrom_autocomplete_fragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        dto_autocomplete_fragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        //filters the google place search to only ghana
        dFrom_autocomplete_fragment.setCountry("GH");
        dto_autocomplete_fragment.setCountry("GH");

        dto_autocomplete_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Geolocate(place);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
    }

    @OnClick(R.id.buttonPickUpRout)
    public void getPickUpRout(){
        Bundle bundle = getIntent().getExtras();
        String drawRout = bundle.getString(Constants.DriverMap);

        if(drawRout.equals("drawRout")){

            toLat = bundle.getDouble(Constants.toLat);
            toLng = bundle.getDouble(Constants.toLng);
            fromLat = bundle.getDouble(Constants.FromLat);
            fromLng = bundle.getDouble(Constants.fromLng);

            if(currentlocation != null){
                fromCustomLoc(fromLatLng);
                meterConstraint.setVisibility(View.VISIBLE);
                btnDest.setVisibility(View.VISIBLE);
                btnPickUpRoute.setVisibility(View.GONE);
            }

        }

    }

    @OnClick(R.id.buttonDestination)
    public void getDestinationRout(){

        Bundle bundle = getIntent().getExtras();
        String drawRout = bundle.getString(Constants.DriverMap);
        if(drawRout.equals("drawRout")){

            toLat = bundle.getDouble(Constants.toLat);
            toLng = bundle.getDouble(Constants.toLng);
            fromLat = bundle.getDouble(Constants.FromLat);
            fromLng = bundle.getDouble(Constants.fromLng);

            if(currentlocation != null){
                toCustomLoc(toLatlng);
                btnDest.setVisibility(View.GONE);
                dEndTripbtn.setVisibility(View.VISIBLE);
            }

        }
    }

    @OnClick(R.id.dEndtripbtn)
    public void getEndTripDialog(){
        alertDialog.show();
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();

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
            logOut();
            return true;
        }

        if (id == R.id.item_refresh) {
                resetMap(mMap, currentlocation);
        }
        if (id == R.id.item_about) {
            Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.item_bookingInfo){
            Intent intent = new Intent(this, DriverConfirm.class);
            intent.putExtra(Constants.TOTAL_FARE, totalWaiting);
            finish();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(googleApiClient.isConnected()){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
        else {
            googleApiClient.connect();
        }

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

        startLatlng = new LatLng(location.getLatitude(), location.getLongitude());

        if(latlngPoints != null){
            latlngPoints.clear();
        }
        latlngPoints.add(startLatlng);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        mMap.addMarker(new MarkerOptions()
                .position(startLatlng)
                .icon(fromIcon)
                .snippet("current Location"));

        Log.d(TAG, "Latitude Longitude" + startLatlng.toString());


        try {
            currentName = new Geocoder(this).getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getAddressLine(0);
        } catch (IOException e) {
            Log.d(TAG, "error" + e.getMessage());
        }

        //String DriverId = mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Drivers Online");
        GeoFire geoFire = new GeoFire(databaseReference);
        geoFire.setLocation(driverId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if(error != null){
                    Log.d(TAG, "Geofier Error" + error.getMessage());
                }
                else {
                    Log.d(TAG, "no Error");
                }
            }
        });

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

    private void logOut(){
        DrivweStatus = false;
       // disconnectUser();
        mAuth.signOut();
        getAssignedVehicle();
//        Intent userIntent = new Intent(DriverMap.this, Login.class);
//        startActivity(userIntent);
//        finish();
    }

    private void disconnectUser(){

        String DriverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Drivers Online");
        GeoFire geoFire = new GeoFire(databaseReference);
        geoFire.removeLocation(DriverId);
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

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                BuildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
            else {
                mMap.setMyLocationEnabled(false);
            }
        }
    }

    public void getAssignedVehicle(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("assigned vehicles");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    AssignVehicleData assignVehicle = ds.getValue(AssignVehicleData.class);
                    if(assignVehicle != null){
                        if((assignVehicle.getStatus()).equals("1") && (assignVehicle.getDriverId()).equals(driverId)){
                            assignVehicleData = ds.getValue(AssignVehicleData.class);
                            assignVehicleData.setId(ds.getKey());
                            getVehicleInfo();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getVehicleInfo(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("vehicles details");

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> titleList = new ArrayList<String>();
                titleList.add("Select Vehicle");
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    vehicleData = dataSnapshot1.getValue(VehicleData.class);
                    if(vehicleData.getStatus().equals("1") && assignVehicleData.getVehicleId().equals(dataSnapshot1.getKey())){
                        vehicleData.setId(dataSnapshot1.getKey());
                        signOutVehicle();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DriverMap.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signOutVehicle(){
        if(assignVehicleData.getStatus().equals("1")){
            assignVehicleData.setStatus("0");
            DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.assigned_vehicles));
            dRef.child(mAuth.getCurrentUser().getUid()).child(getString(R.string.status)).setValue(assignVehicleData);
            updateVehicle();
        }

    }

    public void updateVehicle(){
        vehicleData.setStatus("0");
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("vehicles details");
        dRef.child(vehicleData.getId()).setValue(vehicleData);


    }

    public void fromCustomLoc(LatLng latLng){
        latLng = new LatLng(fromLat,fromLng);
        Log.d("Latlng", latLng.toString());
        latlngPoints.add(latLng);
/*
        try {
            fromLocationName = new Geocoder(this).getFromLocation(latLng.latitude, latLng.latitude, 1).get(0).getAddressLine(0);
        } catch (IOException e) {
            Log.d(TAG, "error" + e.getMessage());
        }
        */

          mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(fromLocationName)
                .icon(destinationIcon)
                .snippet("From Location"));

        drawRouteOnMap(startLatlng, latLng, latlngPoints);

        latlngPoints.remove(0);
    }

    public void toCustomLoc(LatLng latLng){
        toLatlng = new LatLng(toLat,toLng);

        latlngPoints.add(toLatlng);
/*
        try {
            toLocationName = new Geocoder(this).getFromLocation(toLatlng.latitude, toLatlng.latitude, 1).get(0).getAddressLine(0);
        } catch (IOException e) {
            Log.d(TAG, "error" + e.getMessage());
        }
*/
        mMap.addMarker(new MarkerOptions()
                .position(toLatlng)
                .title(toLocationName)
                .draggable(true)
                .icon(fromIcon)
                .snippet("To Location"));

        drawRouteOnMap(fromLatLng, toLatlng, latlngPoints);
    }

    //method to reset the map
    public void resetMap(GoogleMap map, Location location) {
        if(map != null){
            map.clear();

            latlngPoints.removeAll(latlngPoints);
            polylines.removeAll(polylines);
            meterConstraint.setVisibility(View.GONE);

            map.setMyLocationEnabled(true);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatlng));


            if (latlngPoints != null) {
                latlngPoints.clear();
            }
            latlngPoints.add(startLatlng);
/*
            try {
                currentName = new Geocoder(this).getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getAddressLine(0);
            } catch (IOException e) {
                Log.d(TAG, "error" + e.getMessage());
            }
*/

           mMap.addMarker(new MarkerOptions()
                    .position(startLatlng)
                    .title(currentName)
                    .draggable(true)
                    .snippet("current Location")
                    .icon(fromIcon));

            currentlocation.setLatitude(startLatlng.latitude);
            currentlocation.setLongitude(startLatlng.longitude);

        }else {

        }

    }

    /*=========================================fare reader=================================================*/
    public void fareReader(final int incrementalTime, TextView fareText, double farePerMin, Thread thread){

        thread=new Thread(){

            @Override
            public void run(){

                while(!stopThread){

                    try {
                        Thread.sleep(incrementalTime);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                totalFare +=farePerMin;
                                fareText.setText(String.format("%1$, .2f",totalFare));
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        thread.start();

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
           DriverMap.FetchUrl FetchUrl = new DriverMap.FetchUrl();

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

            DriverMap.ParserTask parserTask = new DriverMap.ParserTask();

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

                //calculatePrice();
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

        Geocoder geocoder = new Geocoder(DriverMap.this);

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

       // meterConstraint.setVisibility(View.VISIBLE);

        //distanceInKm = currentlocation.distanceTo(finalLocation) / 1000;//calculating the distance in KM


    }

    public void getPrice(){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("services");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
