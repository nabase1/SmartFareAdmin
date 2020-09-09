package com.example.smartfareadmin;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import com.example.smartfareadmin.dataObjects.SevicesDeal;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddService extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SevicesDeal deal;
    private static final int pictureCode  = 100;
    private String status;

    @BindView(R.id.user_name_text)
    TextInputLayout txt_name;

    @BindView(R.id.price_per_km_text)
    TextInputLayout txt_price_per_km;

    @BindView(R.id.user_contact_text)
    TextInputLayout txt_description;

    @BindView(R.id.price_base_text)
    TextInputLayout txt_base_price;

    @BindView(R.id.price_minimum_text)
    TextInputLayout txt_minimum_price;

    @BindView(R.id.price_per_min_text)
    TextInputLayout txt_price_per_min;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.driving_radiobtn)
    RadioButton driving_radiobtn;

    @BindView(R.id.nonDriving_radiobtn)
    RadioButton nonDriving_radiobtn;

    @BindView(R.id.img_btn)
    Button imag_btn;

    @BindView(R.id.img_view)
    ImageView imageView;

    @BindView(R.id.transition_container)
    ViewGroup tContainer;

    @BindView(R.id.search_item)
    SearchView mSearchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.mtoolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("SmartCab Gh");

        mSearchView.setVisibility(View.INVISIBLE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        FirebaseUtils.openFirebaseUtils("services",this);

        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;


        txt_base_price.setVisibility(View.GONE);
        txt_minimum_price.setVisibility(View.GONE);
        txt_price_per_min.setVisibility(View.GONE);
        txt_price_per_km.setVisibility(View.GONE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getRadioButton();
            }
        });

        Intent intent = getIntent();
        SevicesDeal sevicesDeal = (SevicesDeal) intent.getSerializableExtra("services");

        if(sevicesDeal == null){
           sevicesDeal = new SevicesDeal();
        }

        this.deal = sevicesDeal;

        txt_name.getEditText().setText(sevicesDeal.getName());
        txt_description.getEditText().setText(sevicesDeal.getDescription());
        txt_price_per_km.getEditText().setText(sevicesDeal.getPrice_per_km());
        status = sevicesDeal.getStatus();
        if(status != null){
            if(status.equals("1")){
                driving_radiobtn.setChecked(true);
            }
            if(status.equals("0")){
                nonDriving_radiobtn.setChecked(true);
            }
        }

        txt_price_per_min.getEditText().setText(sevicesDeal.getPrice_per_min());
        txt_minimum_price.getEditText().setText(sevicesDeal.getMin_price());
        txt_base_price.getEditText().setText(sevicesDeal.getBase_price());


        showImage(sevicesDeal.getImageUrl());


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getRadioButton(){

        if(driving_radiobtn.isChecked()){
            status = "1";

            TransitionManager.beginDelayedTransition(tContainer);

            txt_base_price.setVisibility(View.VISIBLE);
            txt_minimum_price.setVisibility(View.VISIBLE);
            txt_price_per_min.setVisibility(View.VISIBLE);
            txt_price_per_km.setVisibility(View.VISIBLE);
            txt_price_per_km.getEditText().setHint("Enter Price Per Km");
        }
        if(nonDriving_radiobtn.isChecked()){
            status = "0";
            TransitionManager.beginDelayedTransition(tContainer);
            txt_base_price.setVisibility(View.GONE);
            txt_minimum_price.setVisibility(View.GONE);
            txt_price_per_min.setVisibility(View.GONE);
            txt_price_per_km.setVisibility(View.VISIBLE);
            txt_price_per_km.getEditText().setHint("Enter Price");
        }

    }

    @OnClick(R.id.img_btn)
    public void getImage(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
       // intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent.createChooser(intent, "Select Picture"), pictureCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == pictureCode && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            StorageReference sRef = FirebaseUtils.storageReference.child("services_pictures").child(imageUri.getLastPathSegment());
            Log.d("image Path", sRef.toString());
            sRef.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                  sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                         String url =  uri.toString();
                          deal.setImageUrl(url);
                          Log.d("image uri", url);
                          showImage(url);
                      }
                  });

                }
            });

        }
        else {
            Log.d("code not match", "not match");
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","services");
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin, menu);
        MenuItem addItem = menu.findItem(R.id.action_add);
        addItem.setVisible(false);

        MenuItem itemRefresh = menu.findItem(R.id.action_refresh);
        itemRefresh.setVisible(false);

        MenuItem itemVerify = menu.findItem(R.id.action_verify);
        itemVerify.setVisible(false);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_save){
            saveServices();
        }


        if(id == R.id.action_delete){
            deleteService();

        }
        return super.onOptionsItemSelected(item);
    }

    public void saveServices(){
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if(selectedId == -1){
            Toast.makeText(this, "Please check any of the Radio Button!", Toast.LENGTH_SHORT).show();
            txt_base_price.setVisibility(View.INVISIBLE);
            txt_minimum_price.setVisibility(View.INVISIBLE);
            txt_price_per_min.setVisibility(View.INVISIBLE);
            txt_price_per_km.setVisibility(View.INVISIBLE);
        }

        deal.setName(txt_name.getEditText().getText().toString());
        deal.setDescription(txt_description.getEditText().getText().toString());
        deal.setPrice_per_km(txt_price_per_km.getEditText().getText().toString());
        deal.setStatus(status);
        deal.setPrice_per_min(txt_price_per_min.getEditText().getText().toString());
        deal.setMin_price(txt_minimum_price.getEditText().getText().toString());
        deal.setBase_price(txt_base_price.getEditText().getText().toString());


        if(deal.getId() == null){
            databaseReference.push().setValue(deal);
            clearFields();
            Toast.makeText(this, "Service added", Toast.LENGTH_SHORT).show();
            // backToList();
        }
        else {
            databaseReference.child(deal.getId()).setValue(deal);
            Toast.makeText(this, "Service Updated!", Toast.LENGTH_SHORT).show();
            backToList();
        }
    }

    public void deleteService(){
        if(deal.getId() == null){
            Toast.makeText(this, "Service not available", Toast.LENGTH_SHORT).show();
        }else {
            databaseReference.child(deal.getId()).removeValue();
            Toast.makeText(this, "Service deleted", Toast.LENGTH_SHORT).show();
            backToList();
        }
    }

    public void clearFields(){
        txt_price_per_km.getEditText().setText("");
        txt_description.getEditText().setText("");
        txt_name.getEditText().setText("");
        txt_minimum_price.getEditText().setText("");
        txt_base_price.getEditText().setText("");
        txt_price_per_min.getEditText().setText("");
        radioGroup.clearCheck();
    }

    public void backToList(){
        Intent intent = new Intent(this, ListServices.class);
        intent.putExtra("choice","services");
        startActivity(intent);
        finish();
    }

    public void showImage(String url){
        if(url != null && url.isEmpty() == false){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(url)
                    .resize(width, width*2/3)
                    .centerCrop()
                    .into(imageView);
            Log.d("uri not null", url);
        }

    }
}
