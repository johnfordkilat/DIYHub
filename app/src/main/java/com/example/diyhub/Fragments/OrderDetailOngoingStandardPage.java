package com.example.diyhub.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.diyhub.MESSAGES.ChatPage;
import com.example.diyhub.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailOngoingStandardPage extends AppCompatActivity {

    ImageView standardPageImage;

    String position;
    ArrayList<OrdersList> list;
    Spinner bookingAddressSpinner;
    Spinner customerRequestSpinner;
    Spinner orderTrackerSpinner;
    ArrayAdapter bookingAdapter;
    ArrayAdapter customerAdapter;
    ArrayAdapter orderAdapter;
    ArrayList<String> bookingAddressList;
    ArrayList<String> customerRequestList;
    ArrayList<String> orderTrackerList;
    EditText itemCode,itemName,quantity,orderType,buyerName,orderDate;
    Button paymentStatus;
    ImageView buyerImage;
    ImageView contactBuyer;

    ImageView backButton;
    ImageButton copyButton;

    ImageView moveToOngoing;
    CardView notif;

    TextView movetoLabel;
    Button viewPriceLiquidationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_ongoing_standard_page);

        standardPageImage = findViewById(R.id.standardPageImageOngoing);
        bookingAddressSpinner = findViewById(R.id.bookingAddressSpinnerStandardOngoing);
        customerRequestSpinner = findViewById(R.id.customerRequestSpinnerStandardOngoing);
        itemCode = findViewById(R.id.itemCodeTxtStandardOngoing);
        itemName = findViewById(R.id.itemNameTxtStandardOngoing);
        quantity = findViewById(R.id.quantityTxtStandardOngoing);
        orderType = findViewById(R.id.orderTypeTxtStandardOngoing);
        buyerName = findViewById(R.id.buyerNameTxtStandardOngoing);
        paymentStatus = findViewById(R.id.paymentStatusTxtStandardOngoing);
        orderDate = findViewById(R.id.orderDateTxtStandardOngoing);
        buyerImage = findViewById(R.id.buyerImageStandardOngoing);
        contactBuyer = findViewById(R.id.contactBuyerButtonStandardOngoing);
        backButton = findViewById(R.id.backButtonOngoingStandard);
        copyButton = findViewById(R.id.copyButtonStandardPageOngoing);
        moveToOngoing = findViewById(R.id.moveToToBookStandard);
        notif = findViewById(R.id.notificationNumberContainerOngoing);
        movetoLabel = findViewById(R.id.moveToTextviewOngoingStandard);
        viewPriceLiquidationButton = findViewById(R.id.viewPriceLiquidationOngoingStandard);



        Bundle extras = getIntent().getExtras();
        if(extras != null){
            position = extras.getString("position");
            list = extras.getParcelableArrayList("list");
        }

        int pos = Integer.parseInt(position);

        viewPriceLiquidationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        OrderDetailOngoingStandardPage.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet, (LinearLayout)findViewById(R.id.bottomSheetContainer));
                TextView merchTotal = (TextView) bottomSheetView.findViewById(R.id.merchSubtotalTxt);
                bottomSheetView.findViewById(R.id.confirmButtonPriceLiquidation).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                TextView shippingTotal = (TextView) bottomSheetView.findViewById(R.id.shippingSubTotalTxt);
                TextView addfees = (TextView) bottomSheetView.findViewById(R.id.additionalFeesTxt);
                TextView quantity = (TextView) bottomSheetView.findViewById(R.id.totalNumOfItemsTxt);
                TextView totalpayment = (TextView) bottomSheetView.findViewById(R.id.totalPaymentTxt);
                merchTotal.setText("???"+String.valueOf(list.get(pos).getOrderProductPrice()));
                shippingTotal.setText("???"+String.valueOf(list.get(pos).getOrderShippingFee()));
                addfees.setText("???"+String.valueOf(list.get(pos).getOrderAdditionalFee()));
                quantity.setText("x"+String.valueOf(list.get(pos).getOrderQuantity()));
                totalpayment.setText("???"+String.valueOf(list.get(pos).getOrderTotalPayment()));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderDetailOngoingStandardPage.this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });



        //Display All Data (OrdersList)
        Glide.with(this).load(list.get(pos).getOrderProductImage()).into(standardPageImage);
        Glide.with(this).load(list.get(pos).getBuyerImage()).into(buyerImage);
        itemCode.setText(list.get(pos).getItemCode());
        itemName.setText(list.get(pos).getOrderProductName());
        quantity.setText(""+list.get(pos).getOrderQuantity());
        orderType.setText(list.get(pos).getOrderType());
        buyerName.setText(list.get(pos).getBuyerName());



        contactBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatPage.class);
                startActivity(intent);            }
        });

        if(list.get(pos).getPaymentOption().equalsIgnoreCase("COD"))
        {
            paymentStatus.setBackgroundResource(R.drawable.custom_red);
            paymentStatus.setText(list.get(pos).getPaymentStatus());
        }
        else
        {
            paymentStatus.setBackgroundResource(R.drawable.custom_green);
            paymentStatus.setText(list.get(pos).getPaymentStatus());

        }
        orderDate.setText(list.get(pos).getOrderDate());





        bookingAddressList = new ArrayList<>();
        customerRequestList = new ArrayList<>();

        bookingAddressList.add(0, "Booking Address");
        bookingAddressList.add(1, list.get(pos).getBookingAddress());
        customerRequestList.add(0, "Customer Request");

        if(bookingAddressList.size() > 1)
        {
            notif.setVisibility(View.VISIBLE);
        }


        //Booking Address Spinner
        bookingAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, bookingAddressList)
        {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        bookingAddressSpinner.setAdapter(bookingAdapter);
        bookingAddressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(), "Selected: "+item, Toast.LENGTH_SHORT).show();
                    notif.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Customer Request Spinner
        customerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, customerRequestList)
        {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        customerRequestSpinner.setAdapter(customerAdapter);





        if(list.get(pos).getDeliveryType().equalsIgnoreCase("For Pickup"))
        {
            movetoLabel.setText("Move to TO RECEIVE");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            moveToOngoing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String OrderProductName, OrderQuantity;
                    String OrderProductImage,OrderID,OrderType,PaymentOption;
                    String ItemCode, BuyerName, PaymentStatus, OrderDate;
                    String BuyerImage;

                    //Seller
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("OrderProductImage",list.get(pos).getOrderProductImage());
                    hashMap.put("OrderStatus", "To Receive");
                    reference.child("Orders").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(hashMap);

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> map = new HashMap<>();
                    map.put("IsSeen","true");
                    map.put("NotifHeader","To Receive");
                    reference1.child("Notifications").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(map);

                    //Buyer
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> hashMap2 = new HashMap<>();
                    hashMap2.put("OrderProductImage",list.get(pos).getOrderProductImage());
                    hashMap2.put("OrderStatus", "To Receive");
                    reference2.child("BuyerPurchase").child(list.get(pos).getBuyerID()).child(list.get(pos).getOrderID()).updateChildren(hashMap2);

                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> map3 = new HashMap<>();
                    map3.put("IsSeen","true");
                    map3.put("NotifHeader","To Receive");
                    reference3.child("Notifications").child(list.get(pos).getBuyerID()).child(list.get(pos).getOrderID()).updateChildren(map3);

                    Toast.makeText(OrderDetailOngoingStandardPage.this, "Order is moved to TO RECEIVE", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        else
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            moveToOngoing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String OrderProductName, OrderQuantity;
                    String OrderProductImage,OrderID,OrderType,PaymentOption;
                    String ItemCode, BuyerName, PaymentStatus, OrderDate;
                    String BuyerImage;

                    //Seller
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("OrderProductImage",list.get(pos).getOrderProductImage());
                    hashMap.put("OrderStatus", "To Book");
                    reference.child("Orders").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(hashMap);

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> map = new HashMap<>();
                    map.put("IsSeen","true");
                    map.put("NotifHeader","To Book");
                    reference1.child("Notifications").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(map);

                    //Buyer
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> hashMap2 = new HashMap<>();
                    hashMap2.put("OrderProductImage",list.get(pos).getOrderProductImage());
                    hashMap2.put("OrderStatus", "To Book");
                    reference2.child("Orders").child(list.get(pos).getBuyerID()).child(list.get(pos).getOrderID()).updateChildren(hashMap2);

                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> map3 = new HashMap<>();
                    map3.put("IsSeen","true");
                    map3.put("NotifHeader","To Book");
                    reference3.child("Notifications").child(list.get(pos).getBuyerID()).child(list.get(pos).getOrderID()).updateChildren(map3);

                    Toast.makeText(OrderDetailOngoingStandardPage.this, "Order is moved to TO BOOK", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}