package com.example.diyhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlaceOrderPageBuyer extends AppCompatActivity {

    RecyclerView productsReviewRecycler;
    String prodID;
    String sellerID;
    ArrayList<AllProductsList> list;
    PlaceOrderPageBuyerAdapter adapter;
    Button placeOrderButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_page_buyer);

        productsReviewRecycler = findViewById(R.id.placeOrderRV);
        productsReviewRecycler.setHasFixedSize(true);
        productsReviewRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        placeOrderButton = findViewById(R.id.proceedWithPaymentButton);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            prodID = extras.getString("ProductID");
            sellerID = extras.getString("SellerID");
        }
        list = new ArrayList<>();

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaceOrderPageBuyer.this, PaymentPageBuyer.class);
                startActivity(intent);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    AllProductsList allProductsList = snapshot.getValue(AllProductsList.class);
                    if(allProductsList.getProductID().equalsIgnoreCase(prodID))
                    list.add(allProductsList);
                }
                adapter = new PlaceOrderPageBuyerAdapter(PlaceOrderPageBuyer.this, list);
                productsReviewRecycler.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}