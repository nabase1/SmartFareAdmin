package com.example.smartfareadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfareadmin.adapters.BookingCancelledAdapter;
import com.example.smartfareadmin.adapters.CompletedTripAdapter;
import com.example.smartfareadmin.adapters.ConfirmedBooking;
import com.example.smartfareadmin.adapters.Confirmed_drivers;
import com.example.smartfareadmin.adapters.DealAdapter;
import com.example.smartfareadmin.adapters.DisabledDrivers;
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

public class ListServices extends AppCompatActivity{

    private String choice;
    @BindView(R.id.textViewHead)
    TextView textViewHead;

    @BindView(R.id.progressBar2)
    ProgressBar mProgressBar;

    @BindView(R.id.search_item)
    SearchView mSearchView;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mDealLinearManager;
    private PendingBookingAdapter mPendingBookingAdapter;
    private DealAdapter mDealAdapter;
    private ConfirmedBooking mConfirmedBooking;
    private BookingCancelledAdapter mBookingCancelledAdapter;
    private CompletedTripAdapter mCompletedTripAdapter;
    private DriversAdapter mDriversAdapter;
    private Confirmed_drivers mConfirmedDrivers;
    private VehicleAdapter mVehicleAdapter;
    private MeterCompletedTripAdapter mMeterCompletedTripAdapter;
    private DriversPersonalOngoingTripAdapter mDriversPersonalOngoingTripAdapter;
    private DriversOnline mDriversOnline;
    private DisabledDrivers mDisabledDrivers;

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

        mProgressBar.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        choice = bundle.getString("choice");

        initializeSearch(choice);
        initialize();

        loadListView(choice);


    }


    public void loadListView(String option){

        if(option.equals("Pending Request")){
            FirebaseUtils.openFirebaseUtils("bookings",this);
            mPendingBookingAdapter = new PendingBookingAdapter();
            mRecyclerView.setAdapter(mPendingBookingAdapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);

        }

        if(option.equals("services")){
            FirebaseUtils.openFirebaseUtils("services",this);
            mDealAdapter = new DealAdapter();
            mRecyclerView.setAdapter(mDealAdapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);

        }

        if(option.equals("Confirmed Request")){
            FirebaseUtils.openFirebaseUtils("bookings",this);
            mConfirmedBooking = new ConfirmedBooking();
            mRecyclerView.setAdapter(mConfirmedBooking);
            mRecyclerView.setLayoutManager(mDealLinearManager);

        }

        if(option.equals("Cancelled Bookings")){
            FirebaseUtils.openFirebaseUtils("bookings",this);
            mBookingCancelledAdapter = new BookingCancelledAdapter();
            mRecyclerView.setAdapter(mBookingCancelledAdapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);

        }

        if(option.equals("Completed Trips")){
            FirebaseUtils.openFirebaseUtils("bookings",this);
            mCompletedTripAdapter = new CompletedTripAdapter();
            mRecyclerView.setAdapter(mCompletedTripAdapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);

        }

        if(option.equals("Pending Drivers")){
            FirebaseUtils.openFirebaseUtils("drivers profile", this);
            mDriversAdapter = new DriversAdapter();
            mRecyclerView.setAdapter(mDriversAdapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);


        }

        if(option.equals("Confirmed Drivers")){
            FirebaseUtils.openFirebaseUtils("drivers profile", this);
            mConfirmedDrivers = new Confirmed_drivers();
            mRecyclerView.setAdapter(mConfirmedDrivers);
            mRecyclerView.setLayoutManager(mDealLinearManager);
        }

        if(option.equals("Deactivated Drivers")){
            FirebaseUtils.openFirebaseUtils("drivers profile", this);
            mDisabledDrivers = new DisabledDrivers();
            mRecyclerView.setAdapter(mDisabledDrivers);
            mRecyclerView.setLayoutManager(mDealLinearManager);
        }

        if(option.equals("Vehicles")){
            FirebaseUtils.openFirebaseUtils("vehicles details", this);
            mVehicleAdapter = new VehicleAdapter();
            mRecyclerView.setAdapter(mVehicleAdapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);


        }

        if(option.equals("Meter Trips")){
            FirebaseUtils.openFirebaseUtils("Meter Trip", this);
            mMeterCompletedTripAdapter = new MeterCompletedTripAdapter();
            mRecyclerView.setAdapter(mMeterCompletedTripAdapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);
        }

        if(option.equals("Ongoing Meter Trips")){
            FirebaseUtils.openFirebaseUtils("Meter Trip", this);
            mDriversPersonalOngoingTripAdapter = new DriversPersonalOngoingTripAdapter();
            mRecyclerView.setAdapter(mDriversPersonalOngoingTripAdapter);
            mRecyclerView.setLayoutManager(mDealLinearManager);


        }

        if(option.equals("Drivers Online")){

            FirebaseUtils.openFirebaseUtils("drivers online", this);
            mDriversOnline = new DriversOnline();
            mRecyclerView.setAdapter(mDriversOnline);
            mRecyclerView.setLayoutManager(mDealLinearManager);
        }

        mProgressBar.setVisibility(View.GONE);

    }

    public void initialize(){
        textViewHead.setText(choice);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerdeals);
        mDealLinearManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false );
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    public void refreshList(){

        if(choice.equals("Pending Request")){
            mPendingBookingAdapter.notifyDataSetChanged();
        }

        if(choice.equals("services")){
            mDealAdapter.notifyDataSetChanged();
        }

        if(choice.equals("Confirmed Request")){
            mConfirmedBooking.notifyDataSetChanged();
        }

        if(choice.equals("Cancelled Bookings")){
            mBookingCancelledAdapter.notifyDataSetChanged();
        }

        if(choice.equals("Completed Trips")){
            mCompletedTripAdapter.notifyDataSetChanged();
        }

        if(choice.equals("Pending Drivers")){
            mDriversAdapter.notifyDataSetChanged();
        }
        if(choice.equals("Deactivated Drivers")){
            mDisabledDrivers.notifyDataSetChanged();
        }

        if(choice.equals("Confirmed Drivers")){
            mConfirmedDrivers.notifyDataSetChanged();
        }

        if(choice.equals("Vehicles")){
            mVehicleAdapter.notifyDataSetChanged();
        }

        if(choice.equals("Meter Trips")){
            mMeterCompletedTripAdapter.notifyDataSetChanged();
        }

        if(choice.equals("Ongoing Meter Trips")){
            mDriversPersonalOngoingTripAdapter.notifyDataSetChanged();
        }

        if(choice.equals("Drivers Online")){
            mDriversOnline.notifyDataSetChanged();
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
                || choice.equals("Drivers Online") || choice.equals("Deactivated Drivers")){
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

        if(item.getItemId() == R.id.action_refresh){
            refreshList();
        }

        return super.onOptionsItemSelected(item);

    }

    //listing to search view

    public void initializeSearch(String option){

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (option.equals("Pending Request")) {
                    mPendingBookingAdapter = new PendingBookingAdapter();
                }

                if (option.equals("services")) {
                    //filter recyclerView when query is submitted
                   mDealAdapter.filterList.filter(query);
                }

                if (option.equals("Confirmed Request")) {
                    mConfirmedBooking = new ConfirmedBooking();
                }

                if (option.equals("Cancelled Bookings")) {
                    //filter recyclerView when query is submitted
                    mBookingCancelledAdapter.filterList.filter(query);
                }

                if (option.equals("Completed Trips")) {
                    mCompletedTripAdapter = new CompletedTripAdapter();
                }

                if (option.equals("Pending Drivers")) {
                    mDriversAdapter = new DriversAdapter();
                }

                if (option.equals("Confirmed Drivers")) {
                    mConfirmedDrivers = new Confirmed_drivers();
                }

                if (option.equals("Deactivated Drivers")) {
                    mDisabledDrivers = new DisabledDrivers();
                }

                if (option.equals("Vehicles")) {
                    mVehicleAdapter = new VehicleAdapter();

                }

                if (option.equals("Meter Trips")) {
                    //filter recyclerView when query is submitted
                    mMeterCompletedTripAdapter.filterList.filter(query);
                }

                if (option.equals("Ongoing Meter Trips")) {

                }

                return  false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (option.equals("Pending Request")) {
                    mPendingBookingAdapter = new PendingBookingAdapter();
                }

                if (option.equals("services")) {
                    //filter recylerView when query is changed
                    mDealAdapter.filterList.filter(newText);
                }

                if (option.equals("Confirmed Request")) {
                    mConfirmedBooking = new ConfirmedBooking();
                }

                if (option.equals("Cancelled Bookings")) {
                    //filter recylerView when query is changed
                    mBookingCancelledAdapter.filterList.filter(newText);
                }

                if (option.equals("Completed Trips")) {
                    mCompletedTripAdapter = new CompletedTripAdapter();
                }

                if (option.equals("Pending Drivers")) {
                    mDriversAdapter = new DriversAdapter();
                }

                if (option.equals("Confirmed Drivers")) {
                    mConfirmedDrivers = new Confirmed_drivers();
                }

                if (option.equals("Deactivated Drivers")) {
                    mDisabledDrivers = new DisabledDrivers();
                }

                if (option.equals("Vehicles")) {
                    mVehicleAdapter = new VehicleAdapter();

                }

                if (option.equals("Meter Trips")) {
                    //filter recylerView when query is changed
                    mMeterCompletedTripAdapter.filterList.filter(newText);
                }

                if (option.equals("Ongoing Meter Trips")) {

                }


                return false;
            }
        });
    }

}
