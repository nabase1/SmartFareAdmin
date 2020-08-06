package com.example.smartfareadmin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.smartfareadmin.dataObjects.Bookings;
import com.example.smartfareadmin.dataObjects.DriverDeal;
import com.example.smartfareadmin.dataObjects.TripDetailsData;
import com.example.smartfareadmin.notification.APIServices;
import com.example.smartfareadmin.notification.Client;
import com.example.smartfareadmin.notification.Data;
import com.example.smartfareadmin.notification.Response;
import com.example.smartfareadmin.notification.Sender;
import com.example.smartfareadmin.notification.Token;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class ManageBookings extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    Bookings mBookings;
    DriverDeal dDeal;
    TripDetailsData tripDetailsData;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    private String status;
    private String driverId, userBookingId;
    public static ArrayList<DriverDeal> array;
    public static ArrayList<String> driverIdArray;
    AlertDialog alertDialog;
    APIServices apiServices;
    Boolean notify = false;
    String mUid;
    String msg, bookingid;


    @BindView(R.id.dTextNum)
    TextView textName;

    @BindView(R.id.dContact)
    TextView textPhone;

    @BindView(R.id.ttxtFrom)
    TextView textFrom;

    @BindView(R.id.ttxtTo)
    TextView textTo;

    @BindView(R.id.ctextAmount)
    EditText textAmount;

    @BindView(R.id.cPickupDate)
    EditText textPickDate;

    @BindView(R.id.cPickUpTime)
    EditText textPickTime;

    @BindView(R.id.cDistance)
    TextView textDistance;

    @BindView(R.id.dTextServiceType)
    TextView txtserviceType;

    @BindView(R.id.spinnerAssign)
    Spinner spinnerAssign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);

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

        FirebaseUtils.openFirebaseUtils("bookings",this);

        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;
        mAuth = FirebaseAuth.getInstance();
        array = new ArrayList<DriverDeal>();
        driverIdArray = new ArrayList<String>();
        tripDetailsData = new TripDetailsData();

        mUid = mAuth.getCurrentUser().getUid();

        //create api services
        apiServices = Client.getRetrofit("https://fcm.googleapis.com/").create(APIServices.class);

        populateSpinner(databaseReference);
        spinnerAssign.setOnItemSelectedListener(this);

//        Bundle bundle = getIntent().getExtras();
//        userBookingId = bundle.getString("userId");

        Intent intent = getIntent();
        Bookings bookings = (Bookings) intent.getSerializableExtra("Pending Bookings");

        if(bookings == null){
            bookings = new Bookings();
        }
        this.mBookings = bookings;
        userBookingId = mBookings.getUid();

        bookingInformation();

        Log.d("id", bookings.getId());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Why Cancelling Trip");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                msg = input.getText().toString();
                notify = true;
                status = "-1";
                UpdatedBooking();
                onBackPressed();
                dialog.dismiss();
                Toast.makeText(ManageBookings.this, "Booking Cancelled!", Toast.LENGTH_SHORT).show();


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog = builder.create();
    }

    private void bookingInformation() {
        bookingid = mBookings.getId();
        textName.setText(mBookings.getName());
        txtserviceType.setText(mBookings.getServiceType());
        textPhone.setText(mBookings.getPhoneNumber());
        textFrom.setText(mBookings.getFrom());
        textTo.setText(mBookings.getTo());
        textPickDate.setText(mBookings.getPick_up_date());
        textPickTime.setText(mBookings.getPick_up_time());
        textDistance.setText(mBookings.getDistance());
        textAmount.setText(mBookings.getAmount());
    }

    @OnClick(R.id.cbtnDone)
    public void CancelBooking(){
       alertDialog.show();
    }

    @OnClick(R.id.dContact)
    public void callNumber(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://"+ textPhone.getText().toString().trim())));
    }

    @OnClick(R.id.buttonStart)
    public void getButtonClick(){
        notify = true;
        status = "1";
        UpdatedBooking();

    }


    public void showDateDialog() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
    }

    @OnClick(R.id.cPickupDate)
    public void showDate() {
        showDateDialog();
    }

    public void updateDate(String date) {
        textPickDate.setText(date);

    }

    public void showTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, min, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @OnClick(R.id.cPickUpTime)
    public void showTime() {
        showTimeDialog();
    }

    public void updateTime(String time) {
        textPickTime.setText(time);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + " : " + minute;
        updateTime(time);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int rMonth = month +1;
        String date = dayOfMonth + " / " + rMonth + " / " + year;
        updateDate(date);
    }

    public void UpdatedBooking(){
        mBookings.setPick_up_date( textPickDate.getText().toString());
        mBookings.setPick_up_time(textPickTime.getText().toString());
        mBookings.setAmount(textAmount.getText().toString());
        mBookings.setStatus(status);
        if(status.equals("1")){
            if(driverId.equals("")){
                Toast.makeText(this, "Assign Driver To Trip", Toast.LENGTH_SHORT).show();
            }else {
                if(mBookings.getId() == null){
                    Toast.makeText(this, "null point exception", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("id", mBookings.getId());
                    databaseReference.child(userBookingId).child(mBookings.getId()).setValue(mBookings);
                    updateDriverDetails();
                }
            }
        }else {
            Log.d("Cancelled status", status);
            mBookings.setMsg(msg);
            databaseReference.child(userBookingId).child(mBookings.getId()).setValue(mBookings);
            if (notify) {

                sendNotification("cancelled",userBookingId,"SmartCab GH","Request Cancelled:" + msg);

            }
            notify = false;
        }

    }

//    public void backToList(){
//        Intent intent = new Intent(this, ListServices.class);
//        intent.putExtra("choice","Pending Request");
//        startActivity(intent);
//        finish();
//    }

    //method to fetch data from firebase to populate  spinner
    public void populateSpinner(DatabaseReference databaseReference){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("drivers profile");

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> titleList = new ArrayList<String>();
                titleList.add("Select Driver");
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    for(DataSnapshot ds: dataSnapshot1.getChildren()){
                        DriverDeal dDeals = ds.getValue(DriverDeal.class);
                        if(dDeals.getStatus().equals("1")){
                            String driverId = dataSnapshot1.getKey();
                            driverIdArray.add(driverId);
                            dDeals.setId(ds.getKey());
                            String driverName = dDeals.getDisplayName();
                            array.add(dDeals);
                            titleList.add(driverName);
                        }
                    }

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ManageBookings.this, android.R.layout.simple_spinner_item, titleList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAssign.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ManageBookings.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int item = parent.getSelectedItemPosition();
        Log.d("item pos", String.valueOf(item));

        if(parent.getSelectedItem().equals("Select Driver")){
                driverId = "";
        }else {
            int itemReduce = item - 1;
            dDeal = array.get(itemReduce);

            driverId = driverIdArray.get(itemReduce);
            Log.d("dName", dDeal.getDisplayName());
            Log.d("driverId", driverId);
            Log.d("userBookingId", userBookingId);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void saveTripDetails(){

        tripDetailsData.setDriverId(driverId);
        tripDetailsData.setUserId(userBookingId);
        tripDetailsData.setStatus("1");
        tripDetailsData.setBookingId(bookingid);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("trip details");
        mRef.push().setValue(tripDetailsData);

        if (notify) {

            sendNotification("confirm",userBookingId,"SmartCab GH",  "Your Booking has been confirmed");
            sendNotificationToDriver("request",driverId, "SmartCab GH", "You Have A New Request!");
        }
        notify = false;

       onBackPressed();

    }

    private void sendNotification(String user, String userBookingId, String title, String body) {

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = dRef.orderByKey().equalTo(userBookingId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(user, title, body,  userBookingId, R.drawable.ic_stat_smartcap_logo);

                    Sender sender = new Sender(data, token.getToken());
                    apiServices.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(ManageBookings.this, response.message(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotificationToDriver(String user, String userBookingId, String title, String body) {

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = dRef.orderByKey().equalTo(userBookingId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(user, title, body,  userBookingId, R.drawable.ic_stat_smartcap_logo);

                    Sender sender = new Sender(data, token.getToken());
                    apiServices.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(ManageBookings.this, response.message(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateDriverDetails(){
        dDeal.setStatus("2");
        if(dDeal.getId() != null){
            DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("drivers profile");
            dRef.child(driverId).child(dDeal.getId()).setValue(dDeal);
            saveTripDetails();

        }else {
            Log.d("empty id", dDeal.getId());
        }


    }


}
