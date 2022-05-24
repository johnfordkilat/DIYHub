package com.example.diyhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyhub.Fragments.StandardProductBuyer;
import com.example.diyhub.Notifications.Data;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Pair;

public class CartPage extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        paymentBackendUrl  = getResources().getString(R.string.paymentBackendUrl);
        paymentSheet = new PaymentSheet(this, this::onPaymentResult);
        dialog = new ProgressDialog(this);

        recyclerView = findViewById(R.id.recyclerPromos1);
        back = findViewById(R.id.backButtonCart);
        checkoutButton = findViewById(R.id.checkoutButtonCartPage);

        s1 = getResources().getStringArray(R.array.item_name);
        s2 = getResources().getStringArray(R.array.purchases);


        Bundle extras = getIntent().getExtras();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back.setOnClickListener(v -> finish());
        checkoutButton.setOnClickListener(v -> {
            getTotalPayment();
            Intent intent = new Intent(CartPage.this, PlaceOrderPageBuyer.class);
            intent.putExtra("ProductIDCart",prodID);
            intent.putExtra("SellerIDCart",sellerID);
            intent.putExtra("ProductQuantityCart",quantity);
            intent.putExtra("ShopNameCart",shopName);
            intent.putExtra("VariationsCart",variations);
            intent.putExtra("ProductPriceCart",price);
            intent.putExtra("ProductNameCart",prodName);
            intent.putExtra("ProductImageCart", prodImage);
            intent.putExtra("TotalPaymentCart",totalPrice);
            startActivity(intent);
        });
        getTotalPayment();
        showData();
    }

    private void getTotalPayment(){
        list = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ShoppingCart").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    CartPageList cartPageList = snapshot.getValue(CartPageList.class);
                    list.add(cartPageList);
                }
                checkoutButton.setText("Total P"+ String.valueOf(list.get(0).getTotalPrice()) + "\n CHECKOUT");
                totalPrice = list.get(0).getTotalPrice();
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
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    CartPageList cartPageList = snapshot.getValue(CartPageList.class);
                    listCart.add(cartPageList);
                }
                prodID = listCart.get(0).getProductID();
                myAdapterCart = new MyAdapterCart(CartPage.this,listCart);
                recyclerView.setAdapter(myAdapterCart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void backPage() {
        Intent intent = new Intent(CartPage.this, BuyerAccountHomePage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void favoritesPage() {
        Intent intent = new Intent(CartPage.this, FavoritesPage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            // Log.d("Canceled")
            Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            // Log.e("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
            Toast.makeText(this, String.valueOf(((PaymentSheetResult.Failed) paymentSheetResult).getError()), Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            PaymentSheetResult.Completed data = (PaymentSheetResult.Completed) paymentSheetResult;
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();


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

    @Override
    protected void onResume() {
        super.onResume();
        getTotalPayment();
        showData();

    }

    @Override
    protected void onPause() {
        super.onPause();
        showData();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> map = new HashMap<>();
        map.put("TotalPrice",0);
        reference.child("ShoppingCart").child(user.getUid()).child(prodID).updateChildren(map);
    }

}