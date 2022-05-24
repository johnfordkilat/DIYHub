package com.example.diyhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import kotlin.Pair;

public class PaymentPageBuyer extends AppCompatActivity {

    TextView payButton;
    DatabaseReference reference;
    ValueEventListener listener;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    DatabaseReference referenceBooking;
    ValueEventListener listenerBooking;
    ArrayList<String> listBooking;
    ArrayAdapter<String> adapterBooking;

    String paymentBackendUrl;
    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;
    private PaymentSheet.CustomerConfiguration customerConfig;
    private ProgressDialog dialog;
    Spinner paymentSpinner;
    Spinner bookingSpinner;
    String sellerID;
    ImageView backButton;
    double totalPayment;
    TextView finalAmount;
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
    String paymentOption;
    String bookingOption;
    String deliveryType;
    double totalPaymentCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page_buyer);
        payButton = findViewById(R.id.payButtonPaymentPageBuyer);
        paymentSpinner = findViewById(R.id.paymentOptionPayment);
        bookingSpinner = findViewById(R.id.courierOption_btn);
        backButton = findViewById(R.id.backButtonPaymentPage);
        finalAmount = findViewById(R.id.finalAmountPaymentPage);

        paymentBackendUrl  = getResources().getString(R.string.paymentBackendUrl);
        paymentSheet = new PaymentSheet(this, this::onPaymentResult);
        dialog = new ProgressDialog(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
               if(paymentSpinner.getSelectedItem().equals(list.get(0)))
               {
                   Toast.makeText(PaymentPageBuyer.this, "Please choose Payment Option", Toast.LENGTH_SHORT).show();
               }
                else if(bookingSpinner.getSelectedItem().equals(listBooking.get(0)))
                {
                    Toast.makeText(PaymentPageBuyer.this, "Please choose Booking Option", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(paymentSpinner.getSelectedItem().equals("ONLINE(VISA DEBIT CARD)"))
                    {
                        processCheckout();
                    }
                    else
                    {
                        Toast.makeText(PaymentPageBuyer.this, "COD", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            sellerID = extras.getString("SellerID");
            totalPayment = extras.getDouble("TotalPayment");
            productName = extras.getString("OrderProductName");
            orderQuantity = extras.getInt("OrderQuantity",0);
            productImage = extras.getString("OrderProductImage");
            orderType = extras.getString("OrderType");
            itemCode = extras.getString("ItemCode");
            buyerName = extras.getString("BuyerName");
            bookingAddress = extras.getString("BookingAddress");
            orderStatus = extras.getString("OrderStatus");
            shopName1 = extras.getString("ShopName");
            productPrice = extras.getDouble("OrderProductPrice",0);
            shippingFee = extras.getDouble("OrderShippingFee",0);
            additionalFee = extras.getDouble("OrderAdditionalFee",0);
            totalPaymentCart = extras.getDouble("TotalPaymentCart",0);
        }
        Toast.makeText(this, "Total Cart: "+totalPaymentCart, Toast.LENGTH_SHORT).show();

        Toast.makeText(this, productName, Toast.LENGTH_SHORT).show();
        finalAmount.setText("Final amount to be paid ₱"+totalPayment);
        //Payment List
        list = new ArrayList<String>();
        list.add(0, "Choose Payment Method");

        //Booking List
        listBooking = new ArrayList<String>();
        listBooking.add(0, "Choose Booking Option");

        //Payment Spinner
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list)
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
        paymentSpinner.setAdapter(adapter);

        //Get the data selected from dropdown spinner
        paymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentOption = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Booking Spinner
        adapterBooking = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listBooking)
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

        //Get the data selected from dropdown spinner
        bookingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookingOption = listBooking.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Payment reference from firebase
        reference = FirebaseDatabase.getInstance().getReference("PaymentOptions").child(sellerID);

        //Booking reference from firebase
        referenceBooking = FirebaseDatabase.getInstance().getReference("BookingOptions").child(sellerID);


        //Display payment options data from database
        fetchData();

        //Display booking options data from database
        fetchDataBooking();




    }


    private void fetchData() {
        listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot myData : snapshot.getChildren())
                {
                    list.add(myData.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchDataBooking() {
        listenerBooking = referenceBooking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot myData : snapshot.getChildren())
                {
                    listBooking.add(myData.getValue().toString());
                }
                adapterBooking.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            // Log.d("Canceled")
            Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            // Log.e("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
            Toast.makeText(this, "Payment Failed"+String.valueOf(((PaymentSheetResult.Failed) paymentSheetResult).getError()), Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            PaymentSheetResult.Completed data = (PaymentSheetResult.Completed) paymentSheetResult;
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();

            DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy hh.mm aa");
            String currentDateAndTime = dateFormat2.format(new Date()).toString();
            String randomID = UUID.randomUUID().toString();
            String OrderID = randomID.substring(0,11);


            if(paymentOption.equalsIgnoreCase("ONLINE(VISA DEBIT CARD)"))
            {
                paymentOption = "ONLINE(VISA DEBIT CARD)";
                paymentStatus = "PAID";
            }
            else
            {
                paymentOption = "CASH ON DELIVERY";
                paymentStatus = "TO PAY";
            }
            if(bookingOption.equalsIgnoreCase("LALAMOVE") || bookingOption.equalsIgnoreCase("MAXIM"))
            {
                deliveryType = "For Delivery";
            }
            else
            {
                deliveryType = "For Pickup";
            }

            String RiderName,PlateNumber;
            String BookingOption;
            String BuyerID;

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Map<String, Object> map = new HashMap<>();
            map.put("OrderProductName",productName);
            map.put("OrderQuantity",orderQuantity);
            map.put("OrderProductImage",productImage);
            map.put("OrderType",orderType);
            map.put("ItemCode",itemCode);
            map.put("BuyerName",buyerName);
            map.put("PaymentStatus",paymentStatus);
            map.put("OrderDate",currentDateAndTime);
            map.put("BuyerImage",String.valueOf(user.getPhotoUrl()));
            map.put("BookingAddress",bookingAddress);
            map.put("OrderStatus","Order Request");
            map.put("ShopName",shopName1);
            map.put("OrderProductPrice",productPrice);
            map.put("OrderShippingFee",shippingFee);
            map.put("OrderAdditionalFee",additionalFee);
            map.put("OrderTotalPayment",totalPayment);
            map.put("PaymentReference",OrderID);
            map.put("OrderID",OrderID);
            map.put("PaymentOption", paymentOption);
            map.put("DeliveryType",deliveryType);
            map.put("RiderName","");
            map.put("PlateNumber","");
            map.put("BookingOption",bookingOption);
            map.put("BuyerID",user.getUid());
            map.put("OrderDeclineReason", "");
            reference.child("Orders").child(sellerID).child(OrderID).setValue(map);

            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
            Map<String, Object> map1 = new HashMap<>();
            map1.put("PaymentReference", user.getUid()+"-"+OrderID);
            map1.put("DateAndTime", currentDateAndTime);
            reference1.child("Payments").child(user.getUid()).child(user.getUid()+"-"+OrderID).setValue(map1);


        }
    }

    private void processCheckout() {
        dialog.setMessage("Processing...");
        dialog.setCancelable(false);
        dialog.show();

        String total = new DecimalFormat("#").format(totalPayment);
        String total1 = total+"00";
        int totalPayment = Integer.parseInt(total1);


        // Note: The amount should be a whole number with the last two digits denoting the cents
        // For example:
        // If the amount is P5.99, send the value as 599
        // If the amount is P5.00, send the value as 500
        List<Pair<String, Integer>> params = Arrays.asList(
                new Pair("amount", totalPayment), // the total amount due
                new Pair("description", "ORDER00001")); // order description

        Fuel.INSTANCE.post(paymentBackendUrl, params).responseString(new Handler<String>() {
            @Override
            public void success(String s) {
                try {
                    final JSONObject result = new JSONObject(s);
                    customerConfig = new PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                    );
                    paymentIntentClientSecret = result.getString("paymentIntent");
                    PaymentConfiguration.init(getApplicationContext(), result.getString("publishableKey"));

                    showPaymentSheet();
                } catch (JSONException e) {
                    Log.e("Error", e.getMessage());
                } finally {
                    dialog.dismiss();
                }
            }

            @Override
            public void failure(@NonNull FuelError fuelError) {
                Log.e("Error", fuelError.getMessage());
                dialog.dismiss();
            }
        });
    }

    private void showPaymentSheet() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("DIYHub, Inc.")
                .customer(customerConfig)
                // Set `allowsDelayedPaymentMethods` to true if your business can handle payment methods
                // that complete payment after a delay, like SEPA Debit and Sofort.
                .allowsDelayedPaymentMethods(true)
                .build();

        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );
    }

}