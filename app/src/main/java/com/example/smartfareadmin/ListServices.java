package com.example.smartfareadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfareadmin.adapters.BookingCancelledAdapter;
import com.example.smartfareadmin.adapters.CompletedTripAdapter;
import com.example.smartfareadmin.adapters.ConfirmedBooking;
import com.example.smartfareadmin.adapters.Confirmed_drivers;
import com.example.smartfareadmin.adapters.DealAdapter;
import com.example.smartfareadmin.adapters.DriversAdapter;
import com.example.smartfareadmin.adapters.DriversOnline;
import com.example.smartfareadmin.adapters.MeterCompletedTripAdapter;
import com.example.smartfareadmin.adapters.DriversPersonalOngoingTripAdapter;
import com.example.smartfareadmin.adapters.PendingBookingAdapter;
import com.example.smartfareadmin.adapters.VehicleAdapter;
import com.example.smartfareadmin.dataObjects.DriverLocation;
import com.example.smartfareadmin.utils.FirebaseUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListServices extends AppCompatActivity {

    private String choice;
    private boolean stopThread = false;
    @BindView(R.id.textViewHead)
    TextView textViewHead;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mDealLinearManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_services);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.mtoolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("SmartCab Gh");

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        choice = bundle.getString("choice");

        initialize();

        loadListView(choice);
       Thread thread=new Thread(){

            @Override
            public void run(){

                while(!stopThread){

                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

    }

    public void loadListView(String option){

        if(option.equals("Pending Request")){
            FirebaseUtils.openFirebaseUtils("bookings",this);
            final PendingBookingAdapter adapter = new PendingBookingAdapter();
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);

        }

        if(option.equals("services")){
            FirebaseUtils.openFirebaseUtils("services",this);
            final DealAdapter adapter = new DealAdapter();
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);

        }

        if(option.equals("Confirmed Request")){
            FirebaseUtils.openFirebaseUtils("bookings",this);
            final ConfirmedBooking adapter = new ConfirmedBooking();
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);

        }

        if(option.equals("Cancelled Bookings")){
            FirebaseUtils.openFirebaseUtils("bookings",this);
            final BookingCancelledAdapter adapter = new BookingCancelledAdapter();
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);

        }

        if(option.equals("Completed Trips")){
            FirebaseUtils.openFirebaseUtils("bookings",this);
            final CompletedTripAdapter adapter = new CompletedTripAdapter();
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);

        }

        if(option.equals("Pending Drivers")){
            FirebaseUtils.openFirebaseUtils("drivers profile", this);
            final DriversAdapter adapter = new DriversAdapter();
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);


        }

        if(option.equals("Confirmed Drivers")){
            FirebaseUtils.openFirebaseUtils("drivers profile", this);
            final Confirmed_drivers adapter = new Confirmed_drivers();
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);


        }

        if(option.equals("Vehicles")){
            FirebaseUtils.openFirebaseUtils("vehicles details", this);
            final VehicleAdapter adapter = new VehicleAdapter();
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);


        }

        if(option.equals("Meter Trips")){
            FirebaseUtils.openFirebaseUtils("Meter Trip", this);
            final MeterCompletedTripAdapter adapter = new MeterCompletedTripAdapter();
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);
        }

        if(option.equals("Ongoing Meter Trips")){
            FirebaseUtils.openFirebaseUtils("Meter Trip", this);
            final DriversPersonalOngoingTripAdapter adapter = new DriversPersonalOngoingTripAdapter();
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);


        }

        if(option.equals("Drivers Online")){

            FirebaseUtils.openFirebaseUtils("drivers online", this);
            final DriversOnline adapter = new DriversOnline();
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);
        }
    }

    public void initialize(){
        textViewHead.setText(choice);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
        mDealLinearManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.admin, menu);
        if(choice.equals("services") || choice.equals("Vehicles")){
            MenuItem mainMenu = menu.findItem(R.id.main_menu);
            mainMenu.setVisible(false);

            MenuItem refreshItem = menu.findItem(R.id.action_refresh);
            refreshItem.setVisible(false);

        }

        if(choice.equals("Pending Request") || choice.equals("Confirmed Request")
                || choice.equals("Cancelled Bookings") || choice.equals("Completed Trips")
                || choice.equals("Confirmed Drivers")  || choice.equals("Pending Drivers")
                || choice.equals("Meter Trips") || choice.equals("Ongoing Meter Trips")
                || choice.equals("Drivers Online")){
            MenuItem mainMenu = menu.findItem(R.id.main_menu);
            mainMenu.setVisible(false);

            MenuItem addItem = menu.findItem(R.id.action_add);
            addItem.setVisible(false);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

           if(choice.equals("services") && item.getItemId() == R.id.action_add){
               Intent intent =  new Intent(this, AddService.class);
               startActivity(intent);

       }

        if(choice.equals("Vehicles") && item.getItemId() == R.id.action_add){
            Intent intent =  new Intent(this, VehicleRegistration.class);
            startActivity(intent);

        }

        if(choice.equals("Pending Request") || choice.equals("Confirmed Request")
                || choice.equals("Cancelled Bookings") || choice.equals("Completed Trips")
                || choice.equals("Confirmed Drivers")  || choice.equals("Pending Drivers") ){
           loadListView(choice);
        }

        return super.onOptionsItemSelected(item);

    }
}
