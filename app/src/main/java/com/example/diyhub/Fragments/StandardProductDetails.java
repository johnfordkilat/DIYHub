package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class StandardProductDetails extends AppCompatActivity {


    ViewPager viewPager;
    String prodID;
    List<ProductDetailsImagesList> prodImagesList;
    double prodPrice;
    String prodName;
    TextView prodNameTxt;
    TextView prodPriceTxt;
    TextView prodSoldTxt;
    double prodSold;
    List<ShopsList> shopsLists;
    TextView shopRatingTxt;
    TextView shopNameTxt;
    TextView shopAddressTxt;

    ImageView shopImageView;
    String prodDescription;
    String prodMaterial;
    TextView prodStockTxt,prodDescriptionTxt,prodMaterialTxt,prodShippedFromTxt;
    double prodStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_product_details);

        viewPager = (ViewPager) findViewById(R.id.viewPagerStandardProductDetails);
        prodNameTxt = findViewById(R.id.productNameStandardDetails);
        prodPriceTxt = findViewById(R.id.ProductPriceStandardDetails);
        prodSoldTxt = findViewById(R.id.productSoldStandardDetails);
        shopRatingTxt = findViewById(R.id.shopRatingStandardDetails);
        shopNameTxt = findViewById(R.id.shopNameStandardDetails);
        shopAddressTxt = findViewById(R.id.shopAddressStandardDetails);
        shopImageView = findViewById(R.id.shopImageViewStandardDetails);
        prodStockTxt = findViewById(R.id.productStockStandardDetails);
        prodDescriptionTxt = findViewById(R.id.descriptionStandardDetailsFinal);
        prodMaterialTxt = findViewById(R.id.materialStandardDetails);
        prodShippedFromTxt = findViewById(R.id.shippedFromStandardDetails);

        prodImagesList = new ArrayList<>();
        shopsLists = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            prodID = extras.getString("ProductID");
            prodName = extras.getString("ProductName");
            prodPrice = extras.getDouble("ProductPrice");
            prodSold = extras.getDouble("ProductSold");
            prodDescription = extras.getString("ProductDescription");
            prodMaterial = extras.getString("ProductMaterial");
            prodStock = extras.getInt("ProductStock");
        }
        Toast.makeText(this, "Product ID: "+ prodID, Toast.LENGTH_SHORT).show();

        prodNameTxt.setText("Product Name: "+ prodName);
        prodPriceTxt.setText("Product Price: ₱"+ String.valueOf(prodPrice));
        prodSoldTxt.setText(String.valueOf(prodSold)+ " Sold");
        prodStockTxt.setText("Stocks: "+ String.valueOf(prodStock));
        prodMaterialTxt.setText("Materials Used: "+prodMaterial);
        prodDescriptionTxt.setText("Description: "+prodDescription);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(prodID).child("ProductImages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prodImagesList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ProductDetailsImagesList allList = snapshot.getValue(ProductDetailsImagesList.class);
                    prodImagesList.add(allList);
                }
                ViewPageAdapterProductDetailsStandard viewPageAdapterProductDetailsStandard = new ViewPageAdapterProductDetailsStandard(StandardProductDetails.this, prodImagesList);

                viewPager.setAdapter(viewPageAdapterProductDetailsStandard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Shops").child(user.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopsLists.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ShopsList listshops = snapshot.getValue(ShopsList.class);
                    shopsLists.add(listshops);
                }
                shopNameTxt.setText("Shop Name: "+shopsLists.get(0).getShopName());
                shopRatingTxt.setText("Shop Rating: "+shopsLists.get(0).getShopRating());
                shopAddressTxt.setText("Shop Address: "+shopsLists.get(0).getShopAddress());
                Glide.with(StandardProductDetails.this).load(shopsLists.get(0).getShopImage()).into(shopImageView);
                prodShippedFromTxt.setText("Shipped From: "+shopsLists.get(0).getShopAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}