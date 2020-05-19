package com.example.smartfareadmin.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfareadmin.R;
import com.example.smartfareadmin.dataObjects.driverBooking;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;


public class MeterCompletedTripAdapter extends RecyclerView.Adapter<MeterCompletedTripAdapter.BookingViewHolder> {

    ArrayList<driverBooking> driverBookingsArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    driverBooking driverBooking;

    public MeterCompletedTripAdapter(){
        driverBooking = new driverBooking();
        driverBookingsArrayList = FirebaseUtils.driverBookingsArrayList;
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;
        databaseReference.orderByChild("dateTime");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("key", dataSnapshot.getKey());
                    driverBooking = ds.getValue(driverBooking.class);
                    if((driverBooking.getStatus()).equals("0")){
                        driverBooking.setId(ds.getKey());
                        driverBookingsArrayList.add(driverBooking);

                        Collections.reverse(driverBookingsArrayList);
                    }

                }
                notifyItemChanged(driverBookingsArrayList.size()-1);

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

        databaseReference.addChildEventListener(childEventListener);
    }


    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemview = LayoutInflater.from(context)
                .inflate(R.layout.driver_trip_row, parent, false);


        return new BookingViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        driverBooking = driverBookingsArrayList.get(position);
        holder.bind(driverBooking);
    }


    @Override
    public int getItemCount() {
        return driverBookingsArrayList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPhone, textFrom,textTo, cname,textAmount;


        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

          textName = (TextView) itemView.findViewById(R.id.dTextNum);
          textPhone = (TextView) itemView.findViewById(R.id.dContact);
          textFrom = (TextView) itemView.findViewById(R.id.ttxtFrom);
          textTo = (TextView) itemView.findViewById(R.id.ttxtTo);
          cname = (TextView) itemView.findViewById(R.id.cTextNum);
          textAmount = (TextView) itemView.findViewById(R.id.ctextAmount);



        }

        public void bind(driverBooking driverBooking){

            textName.setText(driverBooking.getDriverName());
            textPhone.setText(driverBooking.getClientNumber());
            textFrom.setText(driverBooking.getFrom());
            textTo.setText(driverBooking.getTo());
            cname.setText(driverBooking.getClientName());
            textAmount.setText(driverBooking.getPrice());

        }

    }
}
