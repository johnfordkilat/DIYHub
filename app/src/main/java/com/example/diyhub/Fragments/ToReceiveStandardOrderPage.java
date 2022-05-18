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

public class ToReceiveStandardOrderPage extends AppCompatActivity {


    ArrayList<String> listTagAs;
    String[] allAccVerList;
    ArrayAdapter<String> adapterBooking;
    Spinner bookingSpinner;
    Button confirmedBtn;

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

    ImageView moveToToReceive;
    CardView notif;

    int pos;

    Button viewPriceLiquidationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_receive_order);


        bookingSpinner = findViewById(R.id.tagOrderAsSpinnerStandard);
        confirmedBtn = findViewById(R.id.confirmedButtonStandard);

        standardPageImage = findViewById(R.id.standardPageImageToReceive);
        bookingAddressSpinner = findViewById(R.id.bookingAddressSpinnerStandardToReceive);
        customerRequestSpinner = findViewById(R.id.customerRequestSpinnerStandardToReceive);
        itemCode = findViewById(R.id.itemCodeTxtStandardToReceive);
        itemName = findViewById(R.id.itemNameTxtStandardToReceive);
        quantity = findViewById(R.id.quantityTxtStandardToReceive);
        orderType = findViewById(R.id.orderTypeTxtStandardToReceive);
        buyerName = findViewById(R.id.buyerNameTxtStandardToReceive);
        paymentStatus = findViewById(R.id.paymentStatusTxtStandardToReceive);
        orderDate = findViewById(R.id.orderDateTxtStandardToReceive);
        buyerImage = findViewById(R.id.buyerImageStandardToReceive);
        contactBuyer = findViewById(R.id.contactBuyerButtonStandardToReceive);
        backButton = findViewById(R.id.backButtonToReceiveStandard);
        copyButton = findViewById(R.id.copyButtonStandardPageToReceive);
        moveToToReceive = findViewById(R.id.moveToToReceiveStandard);
        notif = findViewById(R.id.notificationNumberContainerToReceiveStandard);
        viewPriceLiquidationButton = findViewById(R.id.viewPriceLiquidationToReceiveStandard);



        Bundle extras = getIntent().getExtras();
        if(extras != null){
            position = extras.getString("position");
            list = extras.getParcelableArrayList("list");
        }

        pos = Integer.parseInt(position);

        viewPriceLiquidationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        ToReceiveStandardOrderPage.this, R.style.BottomSheetDialogTheme
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
                Toast.makeText(ToReceiveStandardOrderPage.this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
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







        displayBookingOption();

    }

    private void displayBookingOption(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String[] tagOptions = getApplicationContext().getResources().getStringArray(R.array.Tag_Options);
        listTagAs = new ArrayList<>();
        listTagAs.add(0, "Choose Tag Order Option");
        listTagAs.add(1, tagOptions[0]);
        listTagAs.add(2, tagOptions[1]);
        listTagAs.add(3, tagOptions[2]);
        allAccVerList = getApplicationContext().getResources().getStringArray(R.array.Accounts_Verified);

        adapterBooking = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listTagAs)
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
        bookingSpinner.setAdapter(adapterBooking);

        bookingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    String item = parent.getItemAtPosition(position).toString();
                    confirmedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ToReceiveStandardOrderPage.this, "Order Tagged As: "+ item, Toast.LENGTH_SHORT).show();
                            String BookingAddress,BuyerImage,BuyerName,ItemCode,OrderDate;
                            String OrderID,OrderProductImage,OrderProductName;
                            String OrderQuantity,OrderStatus,OrderType,PaymentOption,PaymentStatus;

                            DatabaseReference referenceHistory = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, Object> hashMapHistory = new HashMap<>();
                            hashMapHistory.put("BookingAddress", list.get(pos).getBookingAddress());
                            hashMapHistory.put("BuyerImage", list.get(pos).getBuyerImage());
                            hashMapHistory.put("BuyerName", list.get(pos).getBuyerName());
                            hashMapHistory.put("ItemCode", list.get(pos).getItemCode());
                            hashMapHistory.put("OrderDate", list.get(pos).getOrderDate());
                            hashMapHistory.put("OrderID", list.get(pos).getOrderID());
                            hashMapHistory.put("OrderProductImage", list.get(pos).getOrderProductImage());
                            hashMapHistory.put("OrderProductName", list.get(pos).getOrderProductName());
                            hashMapHistory.put("OrderQuantity", list.get(pos).getOrderQuantity());
                            hashMapHistory.put("OrderType", list.get(pos).getOrderType());
                            hashMapHistory.put("PaymentOption", list.get(pos).getPaymentOption());
                            hashMapHistory.put("PaymentStatus", list.get(pos).getPaymentStatus());
                            hashMapHistory.put("ShopName", list.get(pos).getShopName());
                            hashMapHistory.put("OrderStatus", item);
                            referenceHistory.child("TransactionHistory").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(hashMapHistory);



                            //Add to Orders Reference
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("OrderStatus", item);
                            reference.child("Orders").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(hashMap);

                            Toast.makeText(ToReceiveStandardOrderPage.this, "Order is moved to " + item, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}