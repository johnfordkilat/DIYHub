package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.diyhub.R;
import com.example.diyhub.RecyclerViewAdapterShopsNear;
import com.example.diyhub.RecyclerViewAdapterShopsNearYou;
import com.example.diyhub.ShopNearYouList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllShopsNearYouListViewHomePage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ShopsList> slist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_shops_near_you_list_view_home_page);

        recyclerView = findViewById(R.id.shopsNearYouRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        slist = new ArrayList<>();

        getImagesShopsNearYou();
    }

    private void getImagesShopsNearYou(){

        DatabaseReference refNearYou1 = FirebaseDatabase.getInstance().getReference("Shops");
        refNearYou1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ShopsList listShops1 = snapshot.getValue(ShopsList.class);
                    double dist = Double.parseDouble(listShops1.getDistance());
                    if(dist <= 2.0)
                    {
                        slist.add(listShops1);
                    }
                }
                RecyclerViewAdapterShopsNearYou adapter = new RecyclerViewAdapterShopsNearYou(getApplicationContext(), slist);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}