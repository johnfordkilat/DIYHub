package com.example.diyhub.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.diyhub.MESSAGES.ChatPage;
import com.example.diyhub.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailStandardPage extends AppCompatActivity {

    ImageView standardPageImage;

    String position;
    ArrayList<OrdersList> list;
    ArrayList<OrdersAcceptedList> acceptedLists;
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

    ImageView moveToAccepted;
    CardView notif;

    Button viewPriceLiquidationButton;

    Button declineOrder;

    Dialog varDialog;
    EditText optionTxtBooking;
    Button submitBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_standard_page);

        standardPageImage = findViewById(R.id.standardPageImage);
        bookingAddressSpinner = findViewById(R.id.bookingAddressSpinnerStandard);
        customerRequestSpinner = findViewById(R.id.customerRequestSpinnerStandard);
        itemCode = findViewById(R.id.itemCodeTxtStandard);
        itemName = findViewById(R.id.itemNameTxtStandard);
        quantity = findViewById(R.id.quantityTxtStandard);
        orderType = findViewById(R.id.orderTypeTxtStandard);
        buyerName = findViewById(R.id.buyerNameTxtStandard);
        paymentStatus = findViewById(R.id.paymentStatusTxtStandard);
        orderDate = findViewById(R.id.orderDateTxtStandard);
        buyerImage = findViewById(R.id.buyerImageStandard);
        contactBuyer = findViewById(R.id.contactBuyerButtonStandard);
        backButton = findViewById(R.id.backButtonOrderRequestStandard);
        copyButton = findViewById(R.id.copyButtonStandardPage);
        moveToAccepted = findViewById(R.id.moveToAcceptedStandard);
        notif = findViewById(R.id.notificationNumberContainerOrderRequest);
        viewPriceLiquidationButton = findViewById(R.id.viewPriceLiquidationOrderRequestStandard);
        declineOrder = findViewById(R.id.declineOrderButtonStandard);
        
        

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            position = extras.getString("position");
            list = extras.getParcelableArrayList("list");
        }

        int pos = Integer.parseInt(position);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        declineOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderBooking = new AlertDialog.Builder(OrderDetailStandardPage.this);
                builderBooking.setTitle("Decline Reason Confirmation");

                View view1 = getLayoutInflater().inflate(R.layout.layout_dialog_decline_order, null);
                optionTxtBooking = view1.findViewById(R.id.setPaymentOptionTxtDeclineOrder);
                submitBooking = view1.findViewById(R.id.submitButtonDeclineOrder);
                builderBooking.setView(view1);


                submitBooking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String data = optionTxtBooking.getText().toString().trim();
                        if(data.isEmpty())
                        {
                            optionTxtBooking.setError("Cannot be Empty");
                            optionTxtBooking.requestFocus();
                        }
                        else
                        {
                            if(list.get(pos).getPaymentStatus().equalsIgnoreCase("PAID"))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailStandardPage.this);
                                builder.setTitle("Confirmation");
                                builder.setMessage("Order will be tagged as Return/Refund Order");
                                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                                        Map<String, Object> map1 = new HashMap<>();
                                        map1.put("NotifHeader","Return/Refund Order");
                                        reference1.child("Notifications").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(map1);

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("OrderStatus","Return/Refund Order");
                                        map.put(("OrderDeclineReason"), data);
                                        reference.child("Orders").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(map);
                                        Toast.makeText(OrderDetailStandardPage.this, "Order is moved to Return/Refund Order", Toast.LENGTH_SHORT).show();
                                        varDialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.create().show();

                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailStandardPage.this);
                                builder.setTitle("Confirmation");
                                builder.setMessage("Order will be tagged as Cancelled Order");
                                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                                        Map<String, Object> map1 = new HashMap<>();
                                        map1.put("NotifHeader","Cancelled Order");
                                        reference1.child("Notifications").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(map1);

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("OrderStatus","Cancelled Order");
                                        map.put(("OrderDeclineReason"), data);
                                        reference.child("Orders").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(map);
                                        Toast.makeText(OrderDetailStandardPage.this, "Order is moved to Cancelled Order", Toast.LENGTH_SHORT).show();
                                        varDialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.create().show();
                            }

                        }
                    }
                });
                varDialog = builderBooking.create();
                varDialog.show();


            }
        });
        
        viewPriceLiquidationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        OrderDetailStandardPage.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet, (LinearLayout)findViewById(R.id.bottomSheetContainer));
                bottomSheetView.findViewById(R.id.confirmButtonPriceLiquidation).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                TextView merchTotal = (TextView) bottomSheetView.findViewById(R.id.merchSubtotalTxt);
                TextView shippingTotal = (TextView) bottomSheetView.findViewById(R.id.shippingSubTotalTxt);
                TextView addfees = (TextView) bottomSheetView.findViewById(R.id.additionalFeesTxt);
                TextView quantity = (TextView) bottomSheetView.findViewById(R.id.totalNumOfItemsTxt);
                TextView totalpayment = (TextView) bottomSheetView.findViewById(R.id.totalPaymentTxt);
                merchTotal.setText("₱"+String.valueOf(list.get(pos).getOrderProductPrice()));
                shippingTotal.setText("₱"+String.valueOf(list.get(pos).getOrderShippingFee()));
                addfees.setText("₱"+String.valueOf(list.get(pos).getOrderAdditionalFee()));
                quantity.setText("x"+String.valueOf(list.get(pos).getOrderQuantity()));
                totalpayment.setText("₱"+String.valueOf(list.get(pos).getOrderTotalPayment()));
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
                Toast.makeText(OrderDetailStandardPage.this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });



        //Display All Data (OrdersList)
        Glide.with(this).load(list.get(pos).getOrderProductImage()).into(standardPageImage);
        Glide.with(this).load(list.get(pos).getBuyerImage()).into(buyerImage);
        itemCode.setText(list.get(pos).getItemCode());
        itemName.setText(list.get(pos).getOrderProductName());
        quantity.setText(list.get(pos).getOrderQuantity());
        orderType.setText(list.get(pos).getOrderType());
        buyerName.setText(list.get(pos).getBuyerName());


        
        contactBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatPage.class);
                startActivity(intent);
            }
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





        moveToAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String OrderProductName, OrderQuantity;
                String OrderProductImage,OrderID,OrderType,PaymentOption;
                String ItemCode, BuyerName, PaymentStatus, OrderDate;
                String BuyerImage;

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("OrderStatus", "Accepted");
                reference.child("Orders").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(hashMap);

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                Map<String, Object> map = new HashMap<>();
                map.put("IsSeen","true");
                reference1.child("Notifications").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(map);

                Toast.makeText(OrderDetailStandardPage.this, "Order is MOVED TO ACCEPTED", Toast.LENGTH_SHORT).show();
                finish();
            }
        });





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}