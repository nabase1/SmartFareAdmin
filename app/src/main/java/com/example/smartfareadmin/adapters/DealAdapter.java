package com.example.smartfareadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfareadmin.AddService;
import com.example.smartfareadmin.R;
import com.example.smartfareadmin.dataObjects.SevicesDeal;
import com.example.smartfareadmin.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder>{

    ArrayList<SevicesDeal> array;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ImageView imageView;

    public DealAdapter(){

        array = FirebaseUtils.arrayList;

        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;


        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SevicesDeal sevicesDeal = dataSnapshot.getValue(SevicesDeal.class);
                sevicesDeal.setId(dataSnapshot.getKey());
                array.add(sevicesDeal);
                notifyItemChanged(array.size()-1);
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
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View itemview = LayoutInflater.from(context)
                .inflate(R.layout.recyler_row, parent, false);

        return new DealViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
            SevicesDeal deal = array.get(position);
            holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }



    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView textViewTitle,tvDesc,tvPrice;
        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.tdtitle);
            tvDesc = (TextView) itemView.findViewById(R.id.tddesc);
            tvPrice = (TextView) itemView.findViewById(R.id.tdprice);
            imageView = (ImageView) itemView.findViewById(R.id.imgView);

            itemView.setOnClickListener(this);


        }

        public void bind(SevicesDeal deal){
            textViewTitle.setText(deal.getName());
            tvDesc.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice_per_km());
            showImage(deal.getImageUrl());

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("click",String.valueOf(position));

            SevicesDeal getDeals = array.get(position);
            Intent intent = new Intent(v.getContext(), AddService.class);
            intent.putExtra("services", getDeals);
            v.getContext().startActivity(intent);
        }

        public void showImage(String url){
            if(url != null && url.isEmpty() == false){
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                Picasso.with(imageView.getContext())
                        .load(url)
                        .resize(100, 100)
                        .centerCrop()
                        .into(imageView);
                Log.d("uri not null", url);
            }

        }
    }

}
