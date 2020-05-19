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
            textViewHead.setText(choice);
            FirebaseUtils.openFirebaseUtils("bookings",this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
            final PendingBookingAdapter adapter = new PendingBookingAdapter();
            recyclerView.setAdapter(adapter);
            LinearLayoutManager dealLinearManager =
                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
            recyclerView.setLayoutManager(dealLinearManager);

        }

        if(option.equals("services")){
            textViewHead.setText(choice);
            FirebaseUtils.openFirebaseUtils("services",this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
            final DealAdapter adapter = new DealAdapter();
            recyclerView.setAdapter(adapter);
            LinearLayoutManager dealLinearManager =
                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
            recyclerView.setLayoutManager(dealLinearManager);

        }

        if(option.equals("Confirmed Request")){
            textViewHead.setText(choice);
            FirebaseUtils.openFirebaseUtils("bookings",this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
            final ConfirmedBooking adapter = new ConfirmedBooking();
            recyclerView.setAdapter(adapter);
            LinearLayoutManager dealLinearManager =
                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
            recyclerView.setLayoutManager(dealLinearManager);

        }

        if(option.equals("Cancelled Bookings")){
            textViewHead.setText(choice);
            FirebaseUtils.openFirebaseUtils("bookings",this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
            final BookingCancelledAdapter adapter = new BookingCancelledAdapter();
            recyclerView.setAdapter(adapter);
            LinearLayoutManager dealLinearManager =
                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
            recyclerView.setLayoutManager(dealLinearManager);

        }

        if(option.equals("Completed Trips")){
            textViewHead.setText(choice);
            FirebaseUtils.openFirebaseUtils("bookings",this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
            final CompletedTripAdapter adapter = new CompletedTripAdapter();
            recyclerView.setAdapter(adapter);
            LinearLayoutManager dealLinearManager =
                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
            recyclerView.setLayoutManager(dealLinearManager);

        }

        if(option.equals("Pending Drivers")){
            textViewHead.setText(choice);
            FirebaseUtils.openFirebaseUtils("drivers profile", this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
            final DriversAdapter adapter = new DriversAdapter();
            recyclerView.setAdapter(adapter);
            LinearLayoutManager dealLinearManager =
                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
            recyclerView.setLayoutManager(dealLinearManager);


        }

        if(option.equals("Confirmed Drivers")){
            textViewHead.setText(choice);
            FirebaseUtils.openFirebaseUtils("drivers profile", this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
            final Confirmed_drivers adapter = new Confirmed_drivers();
            recyclerView.setAdapter(adapter);
            LinearLayoutManager dealLinearManager =
                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
            recyclerView.setLayoutManager(dealLinearManager);


        }

        if(option.equals("Vehicles")){
            textViewHead.setText(choice);
            FirebaseUtils.openFirebaseUtils("vehicles details", this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
            final VehicleAdapter adapter = new VehicleAdapter();
            recyclerView.setAdapter(adapter);
            LinearLayoutManager dealLinearManager =
                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
            recyclerView.setLayoutManager(dealLinearManager);


        }

        if(option.equals("Meter Trips")){
            textViewHead.setText(choice);
            FirebaseUtils.openFirebaseUtils("Meter Trip", this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
            final MeterCompletedTripAdapter adapter = new MeterCompletedTripAdapter();
            recyclerView.setAdapter(adapter);
            LinearLayoutManager dealLinearManager =
                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
            recyclerView.setLayoutManager(dealLinearManager);
        }

        if(option.equals("Ongoing Meter Trips")){
            textViewHead.setText(choice);
            FirebaseUtils.openFirebaseUtils("Meter Trip", this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
            final DriversPersonalOngoingTripAdapter adapter = new DriversPersonalOngoingTripAdapter();
            recyclerView.setAdapter(adapter);
            LinearLayoutManager dealLinearManager =
                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
            recyclerView.setLayoutManager(dealLinearManager);


        }

        if(option.equals("Drivers Online")){
            textViewHead.setText(choice);
            FirebaseUtils.openFirebaseUtils("drivers online", this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
            final DriversOnline adapter = new DriversOnline();
            recyclerView.setAdapter(adapter);
            LinearLayoutManager dealLinearManager =
                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
            recyclerView.setLayoutManager(dealLinearManager);


        }
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
    public void onBackPressed() {
        if(choice.equals("Confirmed Request") || choice.equals("Cancelled Bookings")
                || choice.equals("Completed Trips") || choice.equals("Pending Request")
                || choice.equals("Meter Trips") || choice.equals("Ongoing Meter Trips")){
            Intent intent = new Intent(this,BookingRequest.class);
            finish();
            startActivity(intent);
        }
        else if(choice.equals("Pending Drivers") || choice.equals("Vehicles") || choice.equals("services") || choice.equals("Confirmed Drivers") ){
            Intent intent = new Intent(this,Management.class);
            finish();
            startActivity(intent);
        }else if(choice.equals("Drivers Online")){
            Intent intent = new Intent(this,AdminActivity.class);
            finish();
            startActivity(intent);
        }else {
            Intent intent = new Intent(this,AdminActivity.class);
            finish();
            startActivity(intent);
        }



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

           if(choice.equals("services") && item.getItemId() == R.id.action_add){
               Intent intent =  new Intent(this, AddService.class);
               startActivity(intent);
               finish();

       }

        if(choice.equals("Vehicles") && item.getItemId() == R.id.action_add){
            Intent intent =  new Intent(this, VehicleRegistration.class);
            startActivity(intent);
            finish();

        }

        if(choice.equals("Pending Request") || choice.equals("Confirmed Request")
                || choice.equals("Cancelled Bookings") || choice.equals("Completed Trips")
                || choice.equals("Confirmed Drivers")  || choice.equals("Pending Drivers") ){
           loadListView(choice);
        }

        return super.onOptionsItemSelected(item);

    }
}
