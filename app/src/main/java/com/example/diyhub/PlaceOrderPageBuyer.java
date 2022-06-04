package com.example.diyhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
    String shopName;
    double price;
    int quantity;
    ArrayList<String> stringList;
    ArrayAdapter adapterString;
    String prodName;
    String variations;
    ImageView backButton;
    double totalPayment;
    String productName;
    int orderQuantity;
    String productImage;
    String orderType;
    String itemCode;
    String buyerName;
    String paymentStatus;
    String bookingAddress;
    String orderStatus;
    String shopName1;
    double productPrice;
    double shippingFee;
    double additionalFee;
    String category1;
    String category2;
    String category3;
    String customSpecsTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_page_buyer);

        productsReviewRecycler = findViewById(R.id.placeOrderRV);
        productsReviewRecycler.setHasFixedSize(true);
        productsReviewRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        placeOrderButton = findViewById(R.id.proceedWithPaymentButton);
        backButton = findViewById(R.id.backButtonProceedPaymentPage);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            prodID = extras.getString("ProductID");
            sellerID = extras.getString("SellerID");
            quantity = extras.getInt("ProductQuantity");
            shopName = extras.getString("ShopName");
            price = extras.getDouble("ProductPrice");
            variations = extras.getString("Variations");
            category1 = extras.getString("Category1");
            category2 = extras.getString("Category2");
            category3 = extras.getString("Category3");
            customSpecsTxt = extras.getString("CustomSpecsTxt");


        }
        list = new ArrayList<>();
        stringList = new ArrayList<>();


        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlaceOrderPageBuyer.this, PaymentPageBuyer.class);
                intent.putExtra("SellerID",sellerID);
                intent.putExtra("TotalPayment", totalPayment);
                intent.putExtra("OrderProductName", productName);
                intent.putExtra("OrderQuantity", quantity);
                intent.putExtra("OrderProductImage", productImage);
                intent.putExtra("OrderType", orderType);
                intent.putExtra("ItemCode", itemCode);
                intent.putExtra("BuyerName", buyerName);
                intent.putExtra("PaymentStatus", paymentStatus);
                intent.putExtra("BookingAddress", bookingAddress);
                intent.putExtra("OrderStatus", orderStatus);
                intent.putExtra("ShopName", shopName);
                intent.putExtra("OrderProductPrice", productPrice);
                intent.putExtra("OrderShippingFee", shippingFee);
                intent.putExtra("OrderAdditionalFee", additionalFee);
                intent.putExtra("Category1", category1);
                intent.putExtra("Category2", category2);
                intent.putExtra("Category3", category3);
                intent.putExtra("CustomSpecsTxt", customSpecsTxt);
                startActivity(intent);


            }
        });

        stringList.add(0,String.valueOf(quantity));
        stringList.add(1, variations);



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
                adapter = new PlaceOrderPageBuyerAdapter(PlaceOrderPageBuyer.this, list, stringList);
                productsReviewRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-message".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("ProceedWithPaymentData"));




    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            productName = intent.getStringExtra("OrderProductName");
            orderQuantity = intent.getIntExtra("OrderQuantity",0);
            productImage = intent.getStringExtra("OrderProductImage");
            orderType = intent.getStringExtra("OrderType");
            itemCode = intent.getStringExtra("ItemCode");
            buyerName = intent.getStringExtra("BuyerName");
            paymentStatus = intent.getStringExtra("PaymentStatus");
            bookingAddress = intent.getStringExtra("BookingAddress");
            orderStatus = intent.getStringExtra("OrderStatus");
            shopName = intent.getStringExtra("ShopName");
            productPrice = intent.getDoubleExtra("OrderProductPrice",0);
            shippingFee = intent.getDoubleExtra("OrderShippingFee",0);
            additionalFee = intent.getDoubleExtra("OrderAdditionalFee",0);
            totalPayment = intent.getDoubleExtra("OrderTotalPayment",0);
        }
    };
}