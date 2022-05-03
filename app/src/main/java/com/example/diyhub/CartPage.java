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

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import kotlin.Pair;

public class CartPage extends AppCompatActivity {

    String paymentBackendUrl;

    RecyclerView recyclerView;
    ImageView notif,back;
    TextView favButton;
    TextView checkoutButton;

    String s1[], s2[];
    int images[] = {R.drawable.img_6,R.drawable.img_7,R.drawable.img_8,R.drawable.img_30,R.drawable.img_31,R.drawable.fb};
    int images1[] = {R.drawable.cart,R.drawable.cart,R.drawable.cart,R.drawable.cart,R.drawable.cart,R.drawable.cart};

    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;
    private PaymentSheet.CustomerConfiguration customerConfig;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        paymentBackendUrl  = getResources().getString(R.string.paymentBackendUrl);
        paymentSheet = new PaymentSheet(this, this::onPaymentResult);
        dialog = new ProgressDialog(this);

        recyclerView = findViewById(R.id.recyclerPromos1);
        back = findViewById(R.id.backButtonCart);
        favButton = findViewById(R.id.favoriteButton);
        checkoutButton = findViewById(R.id.checkoutButtonCartPage);

        s1 = getResources().getStringArray(R.array.item_name);
        s2 = getResources().getStringArray(R.array.purchases);

        MyAdapterCart myAdapterCart = new MyAdapterCart(this, s1,s2,images);
        recyclerView.setAdapter(myAdapterCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back.setOnClickListener(v -> backPage());

        favButton.setOnClickListener(v -> favoritesPage());
        checkoutButton.setOnClickListener(v -> {
            processCheckout();
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
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            // Log.e("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            PaymentSheetResult.Completed data = (PaymentSheetResult.Completed) paymentSheetResult;

        }
    }

    private void processCheckout() {
        dialog.setMessage("Processing...");
        dialog.show();

        // TODO: Poy, ibutang dire ang amount
        // Note: The amount should be a whole number with the last two digits denoting the cents
        // For example:
        // If the amount is P5.99, send the value as 599
        // If the amount is P5.00, send the value as 500
        List<Pair<String, Integer>> params = Arrays.asList(
                new Pair("amount", 10000), // the total amount due
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
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Example, Inc.")
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