package com.example.diyhub.Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diyhub.BuyerAccountHomePage;
import com.example.diyhub.CartPageList;
import com.example.diyhub.FavoritesPage;
import com.example.diyhub.MyAdapterCart;
import com.example.diyhub.PlaceOrderFromCartBuyerTwo;
import com.example.diyhub.PlaceOrderPageFromCartBuyer;
import com.example.diyhub.R;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.gms.location.LocationRequest;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.Pair;

public class BuyerCartFragment extends Fragment {

    View view;

    String paymentBackendUrl;

    RecyclerView recyclerView;
    ImageView notif,back;
    TextView favButton;
    TextView checkoutButton;
    List<CartPageList> list;
    String s1[], s2[];
    int images[] = {R.drawable.img_6,R.drawable.img_7,R.drawable.img_8,R.drawable.img_30,R.drawable.img_31,R.drawable.fb};
    int images1[] = {R.drawable.cart,R.drawable.cart,R.drawable.cart,R.drawable.cart,R.drawable.cart,R.drawable.cart};

    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;
    private PaymentSheet.CustomerConfiguration customerConfig;
    private ProgressDialog dialog;
    double totalPrice=0;
    List<CartPageList> listCart;
    MyAdapterCart myAdapterCart;

    String prodID;
    String sellerID;
    int quantity;
    String shopName;
    double price;
    String variations;
    String prodName;
    String prodImage;
    boolean exists=false;

    private LocationRequest locationRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buyer_cart, container, false);


        paymentBackendUrl  = getResources().getString(R.string.paymentBackendUrl);
        paymentSheet = new PaymentSheet(this, this::onPaymentResult);
        dialog = new ProgressDialog(getContext());

        recyclerView = view.findViewById(R.id.recyclerPromos1);
        checkoutButton = view.findViewById(R.id.checkoutButtonCartPage);

        s1 = getResources().getStringArray(R.array.item_name);
        s2 = getResources().getStringArray(R.array.purchases);


        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null)
        {
            prodID = extras.getString("ProductID");
            sellerID = extras.getString("SellerID");
            quantity = extras.getInt("ProductQuantity");
            shopName = extras.getString("ShopName");
            price = extras.getDouble("ProductPrice");
            variations = extras.getString("Variations");
            prodName = extras.getString("ProductName");
            prodImage = extras.getString("ProductImage");

        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        checkoutButton.setOnClickListener(v -> {
            getTotalPayment();
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ShoppingCart").child(user.getUid());
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            CartPageList cartPageList = snapshot.getValue(CartPageList.class);
                            if(snapshot.getChildrenCount() > 1)
                            {
                                Intent intent = new Intent(getContext(), PlaceOrderFromCartBuyerTwo.class);
                                intent.putExtra("ProductIDCart",prodID);
                                intent.putExtra("SellerIDCart",cartPageList.getSellerID());
                                intent.putExtra("ProductQuantityCart",quantity);
                                intent.putExtra("ShopNameCart",shopName);
                                intent.putExtra("VariationsCart",variations);
                                intent.putExtra("ProductPriceCart",price);
                                intent.putExtra("ProductNameCart",prodName);
                                intent.putExtra("ProductImageCart", prodImage);
                                intent.putExtra("TotalPaymentCart",totalPrice);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(getContext(), PlaceOrderPageFromCartBuyer.class);
                                intent.putExtra("ProductIDCart",prodID);
                                intent.putExtra("SellerIDCart",cartPageList.getSellerID());
                                intent.putExtra("ProductQuantityCart",quantity);
                                intent.putExtra("ShopNameCart",shopName);
                                intent.putExtra("VariationsCart",variations);
                                intent.putExtra("ProductPriceCart",price);
                                intent.putExtra("ProductNameCart",prodName);
                                intent.putExtra("ProductImageCart", prodImage);
                                intent.putExtra("TotalPaymentCart",totalPrice);
                                startActivity(intent);
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
        getTotalPayment();
        showData();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("ProceedToCartFromHomepage"));



        return view;
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            sellerID = intent.getStringExtra("SellerIDCart");

        }
    };

    private void getTotalPayment(){
        list = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ShoppingCart").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        CartPageList cartPageList = snapshot.getValue(CartPageList.class);
                        list.add(cartPageList);
                    }
                    checkoutButton.setText("Total P"+ String.valueOf(list.get(0).getTotalPrice()) + "\n CHECKOUT");
                    totalPrice = list.get(0).getTotalPrice();
                    exists = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showData(){
        listCart = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ShoppingCart").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCart.clear();
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        CartPageList cartPageList = snapshot.getValue(CartPageList.class);
                        listCart.add(cartPageList);
                    }
                    exists = true;
                    prodID = listCart.get(0).getProductID();
                    myAdapterCart = new MyAdapterCart(getContext(),listCart,exists);
                    recyclerView.setAdapter(myAdapterCart);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void backPage() {
        Intent intent = new Intent(getContext(), BuyerAccountHomePage.class);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void favoritesPage() {
        Intent intent = new Intent(getContext(), FavoritesPage.class);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            // Log.d("Canceled")
            Toast.makeText(getActivity(), "Payment Cancelled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            // Log.e("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
            Toast.makeText(getActivity(), String.valueOf(((PaymentSheetResult.Failed) paymentSheetResult).getError()), Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            PaymentSheetResult.Completed data = (PaymentSheetResult.Completed) paymentSheetResult;
            Toast.makeText(getActivity(), "Payment Success", Toast.LENGTH_SHORT).show();


        }
    }

    private void processCheckout() {
        dialog.setMessage("Processing...");
        dialog.setCancelable(false);
        dialog.show();
        getTotalPayment();
        String total = new DecimalFormat("#").format(totalPrice);
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
                    PaymentConfiguration.init(getContext(), result.getString("publishableKey"));

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
                // Set allowsDelayedPaymentMethods to true if your business can handle payment methods
                // that complete payment after a delay, like SEPA Debit and Sofort.
                .allowsDelayedPaymentMethods(true)
                .build();

        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        getTotalPayment();
        showData();

    }

    @Override
    public void onPause() {
        super.onPause();
        showData();

    }
}