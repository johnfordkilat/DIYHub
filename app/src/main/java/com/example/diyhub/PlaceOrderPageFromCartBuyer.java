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

public class PlaceOrderPageFromCartBuyer extends AppCompatActivity {

    ImageView backButton;
    RecyclerView productsReviewRecycler;
    String prodID;
    String sellerID;
    ArrayList<CartPageList> list;
    PlaceOrderPageOrderFromCartAdapter adapter;
    Button placeOrderButton;
    String shopName;
    double price;
    int quantity;
    ArrayList<String> stringList;
    ArrayAdapter adapterString;
    String prodName;
    String variations;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_page_from_cart_buyer);
        backButton  = findViewById(R.id.backButtonProceedPaymentPageCart);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        productsReviewRecycler = findViewById(R.id.placeOrderRVCart);
        productsReviewRecycler.setHasFixedSize(true);
        productsReviewRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        placeOrderButton = findViewById(R.id.proceedWithPaymentButtonCart);
        backButton = findViewById(R.id.backButtonProceedPaymentPageCart);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            prodID = extras.getString("ProductIDCart");
            sellerID = extras.getString("SellerIDCart");
            quantity = extras.getInt("ProductQuantityCart");
            shopName = extras.getString("ShopNameCart");
            price = extras.getDouble("ProductPriceCart");
            variations = extras.getString("VariationsCart");

        }
        list = new ArrayList<>();
        stringList = new ArrayList<>();

        Toast.makeText(this, "SellerID"+sellerID, Toast.LENGTH_SHORT).show();
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlaceOrderPageFromCartBuyer.this, PaymentPageBuyer.class);
                intent.putExtra("SellerID",sellerID);
                intent.putExtra("TotalPayment", totalPayment);
                intent.putExtra("OrderProductName", productName);
                intent.putExtra("OrderQuantity", orderQuantity);
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
                startActivity(intent);


            }
        });





        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ShoppingCart").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    CartPageList allProductsList = snapshot.getValue(CartPageList.class);
                        list.add(allProductsList);
                }
                adapter = new PlaceOrderPageOrderFromCartAdapter(PlaceOrderPageFromCartBuyer.this, list);
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
                new IntentFilter("ProceedWithPaymentDataCart"));
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