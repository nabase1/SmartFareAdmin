package com.example.smartfareadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfareadmin.R;
import com.example.smartfareadmin.TripDetails;
import com.example.smartfareadmin.dataObjects.Bookings;
import com.example.smartfareadmin.dataObjects.driverBooking;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ConfirmedBooking extends RecyclerView.Adapter<ConfirmedBooking.BookingViewHolder> implements Filterable {

    ArrayList<Bookings> bookingsArrayList,mFilteredList,mBookings;
    ArrayList<String> userIdArray;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    Bookings bookings;
    public String userId;


    public ConfirmedBooking(){
        bookings = new Bookings();
        userIdArray = new ArrayList<String>();
        bookingsArrayList = FirebaseUtils.bookingsArrayList;
        mFilteredList = new ArrayList<Bookings>();
        mBookings = new ArrayList<Bookings>();
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                userId = dataSnapshot.getKey();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("userIds", dataSnapshot.getKey());

                    bookings = ds.getValue(Bookings.class);
                    if((bookings.getStatus()).equals("1")){
                        bookings.setId(ds.getKey());
                        bookingsArrayList.add(bookings);

                        Collections.sort(bookingsArrayList, new Comparator<Bookings>() {
                            @Override
                            public int compare(Bookings o1, Bookings o2) {
                                return Long.compare(Long.parseLong(o2.getDateTime().toString()),
                                        Long.parseLong(o1.getDateTime().toString()));
                            }
                        });

                        mBookings = bookingsArrayList;

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

    @Override
    public Filter getFilter() {
        return null;
    }

    public Filter filterList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String searchString = constraint.toString();

            ArrayList<Bookings> filter = new ArrayList<Bookings>();
            if(searchString.isEmpty()){
                mFilteredList = mBookings;
            }else {
                for (Bookings booking : mBookings){
                    if(booking.getName().toLowerCase().trim().contains(searchString) ||
                            booking.getServiceType().toLowerCase().trim().contains(searchString) ||
                            booking.getPick_up_date().trim().toLowerCase().contains(searchString) ||
                            booking.getPick_up_time().trim().toLowerCase().contains(searchString)){
                        filter.add(booking);
                    }
//                    else if(booking.getServiceType().toLowerCase().trim().contains(searchString)){
//                        filter.add(booking);
//                    }
//                    else if(booking.getPick_up_date().trim().toLowerCase().contains(searchString) ){
//                        filter.add(booking);
//                    }

                }
                mFilteredList = filter;
            }

            FilterResults results = new FilterResults();
            results.values = mFilteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            bookingsArrayList = (ArrayList<Bookings>) results.values;
            notifyDataSetChanged();

        }

    };

    public class BookingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textName, textServiceType,textPickDate, textPickTime;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

          textName = (TextView) itemView.findViewById(R.id.dTextNum);
          textServiceType = (TextView) itemView.findViewById(R.id.ttxtServiceName);
          textPickDate = (TextView) itemView.findViewById(R.id.cPickupDate);
          textPickTime = (TextView) itemView.findViewById(R.id.cPickUpTime);


          itemView.setOnClickListener(this);

        }

        public void bind(Bookings bookings){
             Log.d("confirmed booking", userId);
            textName.setText(bookings.getName());
            textServiceType.setText(bookings.getServiceType());
            textPickDate.setText(bookings.getPick_up_date());
            textPickTime.setText(bookings.getPick_up_time());

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("click",String.valueOf(position));
            //userId = userIdArray.get(position);
            Bookings getDeals = bookingsArrayList.get(position);
            Intent intent = new Intent(v.getContext(), TripDetails.class);
            intent.putExtra("Confirmed Bookings", getDeals);
           // intent.putExtra("userId", userIdArray.get(position));
           // intent.putExtra("bookingId", bookingidArrayList.get(position));
          //  Log.d("uid",userIdArray.get(position));
            v.getContext().startActivity(intent);
        }
    }
}
