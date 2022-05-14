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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
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
import java.util.List;
import java.util.Map;

public class OrderDetailCustomizationPage extends AppCompatActivity {

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
    ImageButton backButton;
    ImageButton copyButton;
    ImageView movetoAccepted;

    ImageView moveToCustom;
    CardView notif;

    List<OrderCustomizationsSpecs> specsList;
    CardView customerReqNotif;
    Button viewPriceLiquidationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_customization_page);

        standardPageImage = findViewById(R.id.customPageImage);
        bookingAddressSpinner = findViewById(R.id.bookingAddressSpinnerCustom);
        customerRequestSpinner = findViewById(R.id.customerRequestSpinnerCustom);
        orderTrackerSpinner = findViewById(R.id.orderTrackerSpinnerCustom);
        itemCode = findViewById(R.id.itemCodeTxtCustom);
        itemName = findViewById(R.id.itemNameTxtCustom);
        quantity = findViewById(R.id.quantityTxtCustom);
        orderType = findViewById(R.id.orderTypeTxtCustom);
        buyerName = findViewById(R.id.buyerNameTxtCustom);
        paymentStatus = findViewById(R.id.paymentStatusTxtCustom);
        orderDate = findViewById(R.id.orderDateTxtCustom);
        buyerImage = findViewById(R.id.buyerImageCustom);
        contactBuyer = findViewById(R.id.contactBuyerButtonCustom);
        backButton = findViewById(R.id.backButtonCustomPage);
        copyButton = findViewById(R.id.copyButtonCustomPage);
        movetoAccepted = findViewById(R.id.moveToAcceptedButtonCustom);
        moveToCustom = findViewById(R.id.moveToCustomizationButton);
        notif = findViewById(R.id.notificationNumberContainerOrderRequestCustom);
        customerReqNotif = findViewById(R.id.notificationNumberContainerOrderRequestCustomCustomerRequest);
        viewPriceLiquidationButton = findViewById(R.id.viewPriceLiquidationOrderRequestCustom);






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
                        OrderDetailCustomizationPage.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet, (LinearLayout)findViewById(R.id.bottomSheetContainer));
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

        specsList = new ArrayList<>();

        moveToCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailCustomizationPage.this, OrderDetailCustomPagePreview.class);
                intent.putExtra("ProductID", list.get(pos).getProductID());
                startActivity(intent);
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
                Toast.makeText(OrderDetailCustomizationPage.this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });



        //Display All Data
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
                Toast.makeText(OrderDetailCustomizationPage.this, "Contact Buyer!", Toast.LENGTH_SHORT).show();
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        movetoAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("OrderStatus", "Accepted");



                reference1.child("Orders").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(hashMap1);

                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                Map<String, Object> map = new HashMap<>();
                map.put("IsSeen","true");
                reference2.child("Notifications").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(map);



                Toast.makeText(OrderDetailCustomizationPage.this, "Order is MOVED TO ACCEPTED", Toast.LENGTH_SHORT).show();
                finish();

            }
        });




        bookingAddressList = new ArrayList<>();
        customerRequestList = new ArrayList<>();
        orderTrackerList = new ArrayList<>();

        bookingAddressList.add(0, "Booking Address");
        bookingAddressList.add(1, list.get(pos).getBookingAddress());
        customerRequestList.add(0, "Customer Request");
        orderTrackerList.add(0, "Order Tracker");

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



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid()).child(list.get(pos).getOrderID()).child("OrderCustomizationsSpecs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                specsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    OrderCustomizationsSpecs orderCustomizationsSpecs = snapshot.getValue(OrderCustomizationsSpecs.class);
                    specsList.add(orderCustomizationsSpecs);
                }
                customerRequestList.add(specsList.get(0).getCustomerRequest());
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

                if(customerRequestList.size() > 1)
                {
                    customerReqNotif.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        customerRequestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(), "Selected: "+item, Toast.LENGTH_SHORT).show();
                    customerReqNotif.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //Order Tracker Spinner
        orderAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, orderTrackerList)
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
        orderTrackerSpinner.setAdapter(orderAdapter);


    }
}