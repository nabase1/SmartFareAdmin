package com.example.smartfareadmin.utils;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.smartfareadmin.activities.Constants;
import com.example.smartfareadmin.adapters.DriversOnline;
import com.example.smartfareadmin.dataObjects.Bookings;
import com.example.smartfareadmin.dataObjects.DriverDeal;
import com.example.smartfareadmin.dataObjects.DriverLocation;
import com.example.smartfareadmin.dataObjects.SevicesDeal;
import com.example.smartfareadmin.dataObjects.VehicleData;
import com.example.smartfareadmin.dataObjects.driverBooking;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.smartfareadmin.activities.Constants.*;

public class FirebaseUtils {

    public static FirebaseDatabase firebaseDatabase;
    public  static DatabaseReference databaseReference;
    private static FirebaseUtils firebaseUtils;
    public static FirebaseStorage firebaseStorage;
    public static StorageReference storageReference;
    public static ArrayList<SevicesDeal> arrayList;
    public static ArrayList<Bookings> bookingsArrayList;
    public static ArrayList<DriverDeal> driverDealArrayList;
    public static ArrayList<VehicleData> vehicleDataArrayList;
    public static ArrayList<driverBooking> driverBookingsArrayList;
    public static ArrayList<DriverLocation> driversOnlineArrayList;
    private  static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener sAuthStateListener;
    private static Activity caller;

    private FirebaseUtils(){}

    public static void openFirebaseUtils(String ref, Activity callerActivity){
        if(firebaseUtils == null){
            firebaseUtils = new FirebaseUtils();
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;

            sAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser() == null){
                        firebaseUtils.signIn();
                    }
                }
            };

            connectStorage();
        }
        arrayList = new ArrayList<SevicesDeal>();
        bookingsArrayList = new ArrayList<Bookings>();
        driverDealArrayList = new ArrayList<DriverDeal>();
        vehicleDataArrayList = new ArrayList<VehicleData>();
        driverBookingsArrayList = new ArrayList<driverBooking>();
        driversOnlineArrayList = new ArrayList<DriverLocation>();
        databaseReference = firebaseDatabase.getReference().child(ref);

    }

    public static void connectStorage(){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build());



// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                My_Code);
    }

    public static void attachListener(){
        firebaseAuth.addAuthStateListener(sAuthStateListener);
    }

    public static void detachListener(){
        firebaseAuth.removeAuthStateListener(sAuthStateListener);
    }



}
