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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlaceOrderFromCartBuyerTwo extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView backButton;
    RecyclerView productsReviewRecycler;
    String prodID;
    String sellerID;
    ArrayList<CartPageList> list;
    PlaceOrderPageOrderFromCartTwoAdapter adapter;
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
    TextView subTotal, shipping, addFees, numOfItems,totalPaymentTxt;
    int totalNumOfCarts;
    double totalPaymentTxtInside;
    double subTotalInside;
    double shippingInside;
    double addFeesInside;
    int numOfItemsInside;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_from_cart_buyer_two);

        backButton  = findViewById(R.id.backButtonProceedPaymentPageCartTwo);
        productsReviewRecycler = findViewById(R.id.placeOrderRVCartTwo);
        productsReviewRecycler.setHasFixedSize(true);
        productsReviewRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        placeOrderButton = findViewById(R.id.proceedWithPaymentButtonCartTwo);
        backButton = findViewById(R.id.backButtonProceedPaymentPageCartTwo);
        subTotal = findViewById(R.id.merchSubtotalTxtTwo);
        shipping = findViewById(R.id.shippingSubTotalTxtTwo);
        addFees = findViewById(R.id.additionalFeesTxtTwo);
        numOfItems = findViewById(R.id.totalNumOfItemsTxtTwo);
        totalPaymentTxt = findViewById(R.id.totalPaymentTxtTwo);

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

                Intent intent = new Intent(PlaceOrderFromCartBuyerTwo.this, PaymentPageBuyer.class);
                intent.putExtra("SellerID",sellerID);
                intent.putExtra("TotalPayment", totalPaymentTxtInside);
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
                int i = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    CartPageList allProductsList = snapshot.getValue(CartPageList.class);
                    list.add(allProductsList);
                    totalPaymentTxtInside = totalPaymentTxtInside + list.get(i).getTotalPrice();
                    subTotalInside = subTotalInside + list.get(i).getProductPrice();
                    numOfItemsInside = numOfItemsInside + list.get(i).getProductQuantity();
                    shipping.setText("₱"+allProductsList.getProductShippingFee());
                    addFees.setText("₱"+allProductsList.getProductAdditionalFee());
                    i++;
                }
                totalPaymentTxt.setText("₱"+""+totalPaymentTxtInside);
                subTotal.setText("₱"+subTotalInside);
                numOfItems.setText(String.valueOf(numOfItemsInside));
                adapter = new PlaceOrderPageOrderFromCartTwoAdapter(PlaceOrderFromCartBuyerTwo.this, list);
                productsReviewRecycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getNumOfCarts();




        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-message".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("ProceedWithPaymentDataCartTwo"));



    }

    private void getNumOfCarts()
    {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ShoppingCart");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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