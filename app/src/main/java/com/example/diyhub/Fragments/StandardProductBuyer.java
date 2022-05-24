package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diyhub.PlaceOrderPageBuyer;
import com.example.diyhub.R;
import com.example.diyhub.ViewPageAdapterProductDetailsStandard;
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

public class StandardProductBuyer extends AppCompatActivity {

    ImageView productImg;
    TextView productBookFrom;
    TextView descriptionProduct;
    TextView priceProduct;
    TextView soldProduct;
    TextView productRating;
    TextView buyNow;
    TextView stockProduct;
    TextView addToCart;

    String prodImage,bookfrom;
    double rating,sold,stock,price;

    RatingBar ratingBar;
    String description;
    TextView prodName;
    String name;

    List<ProductDetailsImagesList> prodImagesList;
    ViewPager viewPager;
    String sellerID;
    String prodID;
    Dialog closeDialog;
    TextView shopNameTxt;
    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_product_buyer);

        productBookFrom = findViewById(R.id.bookFromProduct);
        descriptionProduct = findViewById(R.id.descriptionProduct);
        priceProduct = findViewById(R.id.priceProduct);
        soldProduct = findViewById(R.id.productSoldStandardProductBuyer);
        productRating = findViewById(R.id.ratingNum);
        buyNow = findViewById(R.id.buyNowBtn);
        stockProduct = findViewById(R.id.stockProduct);
        ratingBar = findViewById(R.id.ratingBarBuyer);
        shopNameTxt = findViewById(R.id.shopNameStandardProductBuyer);
        viewPager = findViewById(R.id.viewPagerStandardBuyer);
        addToCart = findViewById(R.id.addToCartStandard);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        prodImagesList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            prodImage = extras.getString("ProductImage");
            bookfrom = extras.getString("BookFrom");
            rating = extras.getDouble("Rating");
            sold = extras.getDouble("ProductSold");
            stock = extras.getInt("ProductStocks");
            price = extras.getDouble("ProductPrice");
            description = extras.getString("ProductDescription");
            name = extras.getString("ProductName");
            sellerID = extras.getString("SellerID");
            prodID  = extras.getString("ProductID");
            shopName = extras.getString("ShopName");

        }

        productBookFrom.setText("Shop Address: "+bookfrom);
        productRating.setText(String.valueOf(rating));
        ratingBar.setRating((float)rating);
        priceProduct.setText("₱"+String.valueOf(price));
        descriptionProduct.setText(description);
        stockProduct.setText("Stock: "+String.valueOf(stock));
        shopNameTxt.setText(name);
        soldProduct.setText(String.valueOf(sold)+" Sold");

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StandardProductBuyer.this, PlaceOrderPageBuyer.class);
                intent.putExtra("ProductID",prodID);
                intent.putExtra("SellerID",sellerID);
                startActivity(intent);
            }
        });
        
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Cart Confirmation");

                View view = getLayoutInflater().inflate(R.layout.layout_dialog_for_all, null);
                EditText quantity = view.findViewById(R.id.setQuantityCartPage);
                Button submit = view.findViewById(R.id.submitButtonCartPage);
                builder.setView(view);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(quantity.getText().toString().trim().isEmpty())
                        {
                            quantity.setError("Required");
                            quantity.requestFocus();
                            return;
                        }
                        else
                        {
                            int quant = Integer.parseInt(String.valueOf(quantity.getText().toString().trim()));
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> map = new HashMap<>();
                            map.put("ProductName",name);
                            map.put("ProductID",prodID);
                            map.put("ProductImage",prodImage);
                            map.put("ProductQuantity",quant);
                            map.put("ProductPrice",price);
                            map.put("TotalPrice",0);
                            reference.child("ShoppingCart").child(user.getUid()).child(prodID).updateChildren(map);
                            Toast.makeText(StandardProductBuyer.this, "Product Added to Cart", Toast.LENGTH_SHORT).show();
                            closeDialog.dismiss();
                        }

                    }
                });
                closeDialog = builder.create();
                closeDialog.show();


            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("ProductImages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prodImagesList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ProductDetailsImagesList allList = snapshot.getValue(ProductDetailsImagesList.class);
                    prodImagesList.add(allList);
                }
                ViewPageAdapterProductDetailsStandard viewPageAdapterProductDetailsStandard = new ViewPageAdapterProductDetailsStandard(StandardProductBuyer.this, prodImagesList);

                viewPager.setAdapter(viewPageAdapterProductDetailsStandard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}