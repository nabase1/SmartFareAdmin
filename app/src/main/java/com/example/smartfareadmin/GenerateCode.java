package com.example.smartfareadmin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartfareadmin.dataObjects.CodeObjects;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GenerateCode extends AppCompatActivity {

    @BindView(R.id.codeText)
    EditText codeText;

    @BindView(R.id.buttonSaveCode)
    Button btnSaveCode;

    @BindView(R.id.buttonGenerate)
    Button btnGenerete;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    CodeObjects codeObjects;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_code);
        ButterKnife.bind(this);
        btnSaveCode.setVisibility(View.GONE);

        FirebaseUtils.openFirebaseUtils("drivers code",this);

        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;

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

        Intent intent = getIntent();
        CodeObjects cObjects = (CodeObjects) intent.getSerializableExtra("codes");

        if(cObjects == null){
            cObjects = new CodeObjects();
        }

        this.codeObjects = cObjects;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.buttonGenerate)
    public void generateUniqueNumber(){
        codeText.setText(getAlphaNumeric(5));
        //btnGenerete.setVisibility(View.GONE);
        btnSaveCode.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.buttonSaveCode)
    public void btnsaveCode(){
        saveCode();
    }

    public String getAlphaNumeric(int len) {

        char[] ch = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

        char[] c = new char[len];
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < len; i++) {
            c[i] = ch[random.nextInt(ch.length)];
        }

        return new String(c);
    }

    public void saveCode(){
        codeObjects.setCode(codeText.getText().toString());
        codeObjects.setStatus("0");
        if(TextUtils.isEmpty(codeText.getText().toString())){
            Toast.makeText(this, "Code not generated yet!", Toast.LENGTH_SHORT).show();
        }else {
            if(codeObjects.getId() == null){
                databaseReference.push().setValue(codeObjects);
                codeText.setText("");
            }
            else {
                Toast.makeText(this, "Code already exist, Generate different one", Toast.LENGTH_SHORT).show();
            }

        }


    }
}
