package com.example.smartfareadmin.adapters;

import android.content.Context;
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
import com.example.smartfareadmin.dataObjects.driverBooking;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class DriversPersonalOngoingTripAdapter extends RecyclerView.Adapter<DriversPersonalOngoingTripAdapter.BookingViewHolder> implements Filterable {

    ArrayList<driverBooking> driverBookingsArrayList,mFilteredList,mBookings;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    driverBooking driverBooking;

    public DriversPersonalOngoingTripAdapter(){
        driverBooking = new driverBooking();
        driverBookingsArrayList = FirebaseUtils.driverBookingsArrayList;
        driverBooking = new driverBooking();
        driverBookingsArrayList = FirebaseUtils.driverBookingsArrayList;
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("key", dataSnapshot.getKey());
                    driverBooking = ds.getValue(driverBooking.class);
                    if((driverBooking.getStatus()).equals("1")){
                        driverBooking.setId(ds.getKey());
                        driverBookingsArrayList.add(driverBooking);

                        Collections.sort(driverBookingsArrayList, new Comparator<driverBooking>() {
                            @Override
                            public int compare(driverBooking o1, driverBooking o2) {
                                return Long.compare(Long.parseLong(o2.getDateTime().toString()),
                                        Long.parseLong(o1.getDateTime().toString()));
                            }
                        });

                        mBookings = driverBookingsArrayList;

                       // Collections.reverse(driverBookingsArrayList);
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

    @Override
    public Filter getFilter() {
        return null;
    }

    public Filter filterList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String searchString = constraint.toString();

            ArrayList<driverBooking> filter = new ArrayList<driverBooking>();
            if(searchString.isEmpty()){
                mFilteredList = mBookings;
            }else {
                for (driverBooking booking : mBookings){
                    if(booking.getClientName().toLowerCase().trim().contains(searchString)){
                        filter.add(booking);
                    }
                    else if(booking.getDriverName().toLowerCase().trim().contains(searchString)){
                        filter.add(booking);
                    }

                }
                mFilteredList = filter;
            }

            FilterResults results = new FilterResults();
            results.values = mFilteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            driverBookingsArrayList = (ArrayList<driverBooking>) results.values;
            notifyDataSetChanged();

        }

    };

    public class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPhone, textFrom,textTo, cname,textAmount,textDate;


        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

          textName = (TextView) itemView.findViewById(R.id.dTextNum);
          textPhone = (TextView) itemView.findViewById(R.id.dContact);
          textFrom = (TextView) itemView.findViewById(R.id.ttxtFrom);
          textTo = (TextView) itemView.findViewById(R.id.ttxtTo);
          cname = (TextView) itemView.findViewById(R.id.cTextNum);
          textAmount = (TextView) itemView.findViewById(R.id.ctextAmount);
          textDate = (TextView) itemView.findViewById(R.id.dateTextView);



        }

        public void bind(driverBooking driverBooking){

            Date date=new Date(driverBooking.getDateTime());
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            sfd.format(date);
            String dateTime = date.toString();

            textName.setText(driverBooking.getDriverName());
            textPhone.setText(driverBooking.getClientNumber());
            textFrom.setText(driverBooking.getFrom());
            textTo.setText(driverBooking.getTo());
            cname.setText(driverBooking.getClientName());
            textAmount.setText(driverBooking.getPrice());
            textDate.setText(dateTime);

        }

    }
}
