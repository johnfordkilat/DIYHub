package com.example.diyhub.PastTransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.diyhub.Fragments.OrdersList;
import com.example.diyhub.Fragments.ViewReceiptPageAdapter;
import com.example.diyhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewReceiptPageSeller extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipt_page_seller);

        orderIDTxt = findViewById(R.id.orderIDTextReceiptPageSeller);
        orderFromTxt = findViewById(R.id.orderFromTxtReceiptPageSeller);
        delAddressTxt = findViewById(R.id.deliveryAddressTxtReceiptPageSeller);
        totalTxt = findViewById(R.id.totalTxtReceiptPageSeller);
        subTotalTxt = findViewById(R.id.prodPriceTxtReceiptPageSeller);
        delFeeTxt = findViewById(R.id.deliveryFeeTxtReceiptPageSeller);
        taxTxt = findViewById(R.id.taxTxtReceiptPageSeller);
        total1Txt = findViewById(R.id.totalTxtReceiptPage1Seller);
        paymentOptionTxt = findViewById(R.id.paymentOptionTxtReceiptPageSeller);
        totalPaymentTxt = findViewById(R.id.totalPaymentTxtReceiptPageSeller);
        additionalFeeTxt = findViewById(R.id.additionalFeeTxtReceiptPageSeller);


        receiptRecycler = findViewById(R.id.receiptRecyclerSeller);
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    OrdersList list = snapshot.getValue(OrdersList.class);
                    if(list.getProductID().equalsIgnoreCase(prodID))
                    {
                        orderIDTxt.setText(list.getOrderID());
                        orderFromTxt.setText(list.getShopName());
                        delAddressTxt.setText(list.getBookingAddress());
                        totalTxt.setText("₱"+list.getOrderTotalPayment());
                        subTotalTxt.setText("₱"+list.getOrderProductPrice());
                        delFeeTxt.setText("₱"+list.getOrderShippingFee());
                        taxTxt.setText("₱10.00");
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
        referenceOrderRequest = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid());
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