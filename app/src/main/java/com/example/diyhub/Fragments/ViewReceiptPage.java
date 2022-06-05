package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.diyhub.PastTransaction.CancelledOrdersRecyclerAdapter;
import com.example.diyhub.PastTransaction.CompletedOrderRecyclerAdapter;
import com.example.diyhub.PastTransaction.ReturnRefundOrdersRecyclerAdapter;
import com.example.diyhub.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.StructuredQuery;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ViewReceiptPage extends AppCompatActivity {

    RecyclerView receiptRecycler;
    ProgressDialog progressDialog;
    private int[] tabIcons = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_products1,
            R.drawable.ic_orders1,
    };

    DatabaseReference referenceOrderRequest;
    ArrayList<OrdersList> ordersListsCompleted;
    ViewReceiptPageAdapter ordersAdapterCompleted;
    String prodID;
    TextView orderIDTxt,orderFromTxt,delAddressTxt,totalTxt;
    TextView subTotalTxt,delFeeTxt,taxTxt,total1Txt;
    TextView paymentOptionTxt,totalPaymentTxt;
    TextView additionalFeeTxt;

    private static final DecimalFormat df = new DecimalFormat("0.00");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipt_page);

        orderIDTxt = findViewById(R.id.orderIDTextReceiptPage);
        orderFromTxt = findViewById(R.id.orderFromTxtReceiptPage);
        delAddressTxt = findViewById(R.id.deliveryAddressTxtReceiptPage);
        totalTxt = findViewById(R.id.totalTxtReceiptPage);
        subTotalTxt = findViewById(R.id.prodPriceTxtReceiptPage);
        delFeeTxt = findViewById(R.id.deliveryFeeTxtReceiptPage);
        taxTxt = findViewById(R.id.taxTxtReceiptPage);
        total1Txt = findViewById(R.id.totalTxtReceiptPage1);
        paymentOptionTxt = findViewById(R.id.paymentOptionTxtReceiptPage);
        totalPaymentTxt = findViewById(R.id.totalPaymentTxtReceiptPage);
        additionalFeeTxt = findViewById(R.id.additionalFeeTxtReceiptPage);


        receiptRecycler = findViewById(R.id.receiptRecyclerBuyer);
        receiptRecycler.setHasFixedSize(true);
        receiptRecycler.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));
        receiptRecycler.addItemDecoration(dividerItemDecoration);


        Bundle extra = getIntent().getExtras();
        if(extra != null)
        {
            prodID = extra.getString("ProductID");
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
        {
            showData();
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BuyerPurchase").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        OrdersList list = snapshot.getValue(OrdersList.class);
                        if(list.getProductID().equalsIgnoreCase(prodID))
                        {
                            double subtotal = list.getOrderProductPrice() * list.getOrderQuantity();
                            double amount = subtotal + list.getOrderShippingFee() + list.getOrderAdditionalFee();
                            double percent = 1.12;
                            double incTxt = ((amount / percent) - amount ) * -1;



                            orderIDTxt.setText(list.getOrderID());
                            orderFromTxt.setText(list.getShopName());
                            delAddressTxt.setText(list.getBookingAddress());
                            totalTxt.setText("₱"+list.getOrderTotalPayment());
                            subTotalTxt.setText("₱"+subtotal);
                            delFeeTxt.setText("₱"+list.getOrderShippingFee());
                            taxTxt.setText("₱"+df.format(incTxt));
                            total1Txt.setText("₱"+list.getOrderTotalPayment());
                            paymentOptionTxt.setText(list.getPaymentOption());
                            totalPaymentTxt.setText("₱"+list.getOrderTotalPayment());
                            additionalFeeTxt.setText("₱"+list.getOrderAdditionalFee());

                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void showData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setTitle("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();


        ordersListsCompleted = new ArrayList<>();
        referenceOrderRequest = FirebaseDatabase.getInstance().getReference("BuyerPurchase").child(user.getUid());
        referenceOrderRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersListsCompleted.clear();


                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    OrdersList ordersList1 = snapshot.getValue(OrdersList.class);
                    if(ordersList1.getProductID().equalsIgnoreCase(prodID))
                    ordersListsCompleted.add(ordersList1);

                }
                Log.d("SELLERERROR", "error");

                ordersAdapterCompleted = new ViewReceiptPageAdapter(getApplicationContext(), ordersListsCompleted);
                receiptRecycler.setAdapter(ordersAdapterCompleted);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}