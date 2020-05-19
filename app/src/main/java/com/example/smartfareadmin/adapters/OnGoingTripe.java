package com.example.smartfareadmin.adapters;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.smartfareadmin.dataObjects.Bookings;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class OnGoingTripe {

    ArrayList<Bookings> bookingsArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    Bookings bookings;


    public OnGoingTripe() {
        bookings = new Bookings();
        bookingsArrayList = FirebaseUtils.bookingsArrayList;
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;


        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    bookings = ds.getValue(Bookings.class);
                    if((bookings.getStatus()).equals("1")){
                        bookings.setId(ds.getKey());
                        bookingsArrayList.add(bookings);

                        Collections.reverse(bookingsArrayList);
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }


}
