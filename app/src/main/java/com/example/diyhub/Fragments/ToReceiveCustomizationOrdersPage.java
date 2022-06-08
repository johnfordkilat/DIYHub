package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import com.bumptech.glide.Glide;
import com.example.diyhub.MESSAGES.ChatPage;
import com.example.diyhub.R;
import com.example.diyhub.Wallet.WalletList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class ToReceiveCustomizationOrdersPage extends AppCompatActivity {


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

    List<OrderCustomizationsSpecs> specsList;

    CardView customerReqNotif;

    Button viewPriceLiquidationButton;


    ImageView moveToCustom;
    double sellerBalance;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_receive_customization_orders_page);

        bookingSpinner = findViewById(R.id.tagOrderAsSpinnerCustomPage);
        confirmedBtn = findViewById(R.id.confirmedButtonCustomPage);

        standardPageImage = findViewById(R.id.CustomPageImageToReceive);
        bookingAddressSpinner = findViewById(R.id.bookingAddressSpinnerCustomToReceive);
        customerRequestSpinner = findViewById(R.id.customerRequestSpinnerCustomToReceive);
        itemCode = findViewById(R.id.itemCodeTxtCustomToReceive);
        itemName = findViewById(R.id.itemNameTxtCustomToReceive);
        quantity = findViewById(R.id.quantityTxtCustomToReceive);
        orderType = findViewById(R.id.orderTypeTxtCustomToReceive);
        buyerName = findViewById(R.id.buyerNameTxtCustomToReceive);
        paymentStatus = findViewById(R.id.paymentStatusTxtCustomToReceive);
        orderDate = findViewById(R.id.orderDateTxtCustomToReceive);
        buyerImage = findViewById(R.id.buyerImageCustomToReceive);
        contactBuyer = findViewById(R.id.contactBuyerButtonCustomToReceive);
        backButton = findViewById(R.id.backButtonToReceiveCustom);
        copyButton = findViewById(R.id.copyButtonCustomPageToReceive);
        moveToToReceive = findViewById(R.id.moveToToReceiveCustom);
        notif = findViewById(R.id.notificationNumberContainerToReceiveCustom);
        customerReqNotif = findViewById(R.id.notificationNumberContainerToReceiveCustomCustomerRequest);
        viewPriceLiquidationButton = findViewById(R.id.viewPriceLiquidationToReceiveCustom);
        moveToCustom = findViewById(R.id.moveToCustomizationButtonToReceive);






        Bundle extras = getIntent().getExtras();
        if(extras != null){
            position = extras.getString("position");
            list = extras.getParcelableArrayList("list");
        }

        pos = Integer.parseInt(position);

        moveToCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToReceiveCustomizationOrdersPage.this, OrderDetailCustomPagePreview.class);
                intent.putExtra("ProductID", list.get(pos).getProductID());
                intent.putExtra("ProductImage", list.get(pos).getOrderProductImage());
                startActivity(intent);
            }
        });

        viewPriceLiquidationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        ToReceiveCustomizationOrdersPage.this, R.style.BottomSheetDialogTheme
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
                Toast.makeText(ToReceiveCustomizationOrdersPage.this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
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
        specsList = new ArrayList<>();

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
        displayBookingOption();

        showDataCat1();

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

    }
    public void showDataCat1()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid()).child(list.get(pos).getProductID()).child("OrderCustomizationsSpecs").child("Category-Textbox");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    OrderCustomListSeller addCustomSpecsSellerList = snapshot.getValue(OrderCustomListSeller.class);
                    customerRequestList.add(addCustomSpecsSellerList.getSpecsName());
                }
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
                            Toast.makeText(ToReceiveCustomizationOrdersPage.this, "Order Tagged As: "+ item, Toast.LENGTH_SHORT).show();


                            //Seller
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

                            //Get Seller Balance
                            DatabaseReference referenceBalance = FirebaseDatabase.getInstance().getReference("Wallet").child(user.getUid());
                            referenceBalance.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                        {
                                            WalletList list1 = snapshot.getValue(WalletList.class);
                                            sellerBalance = list1.getBalance();

                                            if(list.get(pos).getPaymentOption().equalsIgnoreCase("ONLINE(VISA DEBIT CARD)"))
                                            {
                                                //Amount to be added to seller wallet
                                                double amount = list.get(pos).getOrderTotalPayment();
                                                double shipping = list.get(pos).getOrderShippingFee();
                                                double exactAmount = amount - shipping;
                                                double deduct = exactAmount * 0.02;
                                                double totalAmountBefore = (exactAmount - deduct);
                                                double totalAmount = (exactAmount - deduct) + sellerBalance;

                                                //Seller Wallet
                                                if(position == 1)
                                                {
                                                    DatabaseReference referenceWallet = FirebaseDatabase.getInstance().getReference();
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("Balance", totalAmount);
                                                    referenceWallet.child("Wallet").child(user.getUid()).child("Balance").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(ToReceiveCustomizationOrdersPage .this, "Total of ₱"+totalAmountBefore +" has been credited to your wallet", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }
                                            else
                                            {
                                                //Amount to be added to seller wallet
                                                double amount = list.get(pos).getOrderTotalPayment();
                                                double shipping = list.get(pos).getOrderShippingFee();
                                                double exactAmount = amount - shipping;
                                                double deduct = exactAmount * 0.02;
                                                double totalAmountBefore = (exactAmount - deduct);
                                                double totalAmount = sellerBalance - deduct;

                                                //Seller Wallet
                                                if(position == 1)
                                                {
                                                    DatabaseReference referenceWallet = FirebaseDatabase.getInstance().getReference();
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("Balance", totalAmount);
                                                    referenceWallet.child("Wallet").child(user.getUid()).child("Balance").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(ToReceiveCustomizationOrdersPage .this, "Total of ₱"+deduct +" has been deducted from your wallet", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("OrderStatus", item);

                            reference.child("Orders").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(hashMap);

                            //Buyer
                            DatabaseReference referenceHistory1 = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, Object> hashMapHistory1 = new HashMap<>();
                            hashMapHistory1.put("BookingAddress", list.get(pos).getBookingAddress());
                            hashMapHistory1.put("BuyerImage", list.get(pos).getBuyerImage());
                            hashMapHistory1.put("BuyerName", list.get(pos).getBuyerName());
                            hashMapHistory1.put("ItemCode", list.get(pos).getItemCode());
                            hashMapHistory1.put("OrderDate", list.get(pos).getOrderDate());
                            hashMapHistory1.put("OrderID", list.get(pos).getOrderID());
                            hashMapHistory1.put("OrderProductImage", list.get(pos).getOrderProductImage());
                            hashMapHistory1.put("OrderProductName", list.get(pos).getOrderProductName());
                            hashMapHistory1.put("OrderQuantity", list.get(pos).getOrderQuantity());
                            hashMapHistory1.put("OrderType", list.get(pos).getOrderType());
                            hashMapHistory1.put("PaymentOption", list.get(pos).getPaymentOption());
                            hashMapHistory1.put("PaymentStatus", list.get(pos).getPaymentStatus());
                            hashMapHistory1.put("ShopName", list.get(pos).getShopName());
                            hashMapHistory1.put("OrderStatus", item);
                            referenceHistory1.child("TransactionHistory").child(list.get(pos).getBuyerID()).child(list.get(pos).getOrderID()).updateChildren(hashMapHistory1);


                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("OrderStatus", item);

                            reference1.child("BuyerPurchase").child(list.get(pos).getBuyerID()).child(list.get(pos).getOrderID()).updateChildren(hashMap1);

                            Toast.makeText(ToReceiveCustomizationOrdersPage.this, "Order is moved to " + item, Toast.LENGTH_SHORT).show();
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