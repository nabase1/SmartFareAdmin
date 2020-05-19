package com.example.smartfareadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfareadmin.CompletedTripDetails;
import com.example.smartfareadmin.R;
import com.example.smartfareadmin.dataObjects.Bookings;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;


public class CompletedTripAdapter extends RecyclerView.Adapter<CompletedTripAdapter.BookingViewHolder> {

    ArrayList<Bookings> bookingsArrayList;
    ArrayList<String> bookingidArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    Bookings bookings;

    public CompletedTripAdapter(){
        bookings = new Bookings();
        bookingsArrayList = FirebaseUtils.bookingsArrayList;
        bookingidArrayList = new ArrayList<String>();
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    bookings = ds.getValue(Bookings.class);
                    if((bookings.getStatus()).equals("2")){
                        bookings.setId(ds.getKey());
                        bookingsArrayList.add(bookings);
                        bookingidArrayList.add(ds.getKey());

                        Collections.reverse(bookingsArrayList);
                        Collections.reverse(bookingidArrayList);
                    }

                }
                notifyItemChanged(bookingsArrayList.size()-1);

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
                .inflate(R.layout.booking_request_row, parent, false);


        return new BookingViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        bookings = bookingsArrayList.get(position);
        holder.bind(bookings);
    }


    @Override
    public int getItemCount() {
        return bookingsArrayList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textName,textServiceName,textPickDate, textPickTime;


        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = (TextView) itemView.findViewById(R.id.dTextNum);
            textPickDate = (TextView) itemView.findViewById(R.id.cPickupDate);
            textPickTime = (TextView) itemView.findViewById(R.id.cPickUpTime);
            textServiceName = (TextView) itemView.findViewById(R.id.ttxtServiceName);

            itemView.setOnClickListener(this);



        }

        public void bind(Bookings bookings){
         // Log.d("booking", bookings.getName());

            textName.setText(bookings.getName());
            textPickDate.setText(bookings.getPick_up_date());
            textPickTime.setText(bookings.getPick_up_time());
            textServiceName.setText(bookings.getServiceType());

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("click",String.valueOf(position));
            //userId = userIdArray.get(position);
            Bookings getDeals = bookingsArrayList.get(position);
            Intent intent = new Intent(v.getContext(), CompletedTripDetails.class);
            intent.putExtra("Completed Bookings", getDeals);
            intent.putExtra("bookingId", bookingidArrayList.get(position));
            intent.putExtra("completed", "1");
            Log.d("bid",bookingidArrayList.get(position));
            v.getContext().startActivity(intent);
        }
    }
}
