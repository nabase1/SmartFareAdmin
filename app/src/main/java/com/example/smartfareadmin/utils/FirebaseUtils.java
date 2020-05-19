package com.example.smartfareadmin.utils;

import android.app.Activity;

import com.example.smartfareadmin.adapters.DriversOnline;
import com.example.smartfareadmin.dataObjects.Bookings;
import com.example.smartfareadmin.dataObjects.DriverDeal;
import com.example.smartfareadmin.dataObjects.DriverLocation;
import com.example.smartfareadmin.dataObjects.SevicesDeal;
import com.example.smartfareadmin.dataObjects.VehicleData;
import com.example.smartfareadmin.dataObjects.driverBooking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FirebaseUtils {

    private static final int My_Code = 1403;
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
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static Activity caller;

    private FirebaseUtils(){}


    public static void openFirebaseUtils(String ref, Activity callerActivity){
        if(firebaseUtils == null){
            firebaseUtils = new FirebaseUtils();
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();

            caller = callerActivity;

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



}
