package com.example.diyhub.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diyhub.AllProductsAdapter;
import com.example.diyhub.AllProductsList;
import com.example.diyhub.HoldProductsAdapter;
import com.example.diyhub.HoldProductsList;
import com.example.diyhub.R;
import com.example.diyhub.RestockProductsAdapter;
import com.example.diyhub.RestockProductsList;

import java.util.ArrayList;

public class StandardProductBuyer extends AppCompatActivity {

    ImageView productImg;
    TextView productBookFrom;
    TextView descriptionProduct;
    TextView priceProduct;
    TextView soldProduct;
    TextView productRating;
    TextView buyNow;
    TextView stockProduct;

    String prodImage,bookfrom;
    double rating,sold,stock,price;

    RatingBar ratingBar;
    String description;
    TextView prodName;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_product);

        productImg = findViewById(R.id.imageProduct);
        productBookFrom = findViewById(R.id.bookFromProduct);
        descriptionProduct = findViewById(R.id.descriptionProduct);
        priceProduct = findViewById(R.id.priceProduct);
        soldProduct = findViewById(R.id.productSold);
        productRating = findViewById(R.id.ratingNum);
        buyNow = findViewById(R.id.buyNowBtn);
        stockProduct = findViewById(R.id.stockProduct);
        ratingBar = findViewById(R.id.ratingBarBuyer);
        prodName = findViewById(R.id.nameProductBuyer);

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

        }

        Glide.with(this).load(prodImage).into(productImg);
        productBookFrom.setText(bookfrom);
        productRating.setText(String.valueOf(rating));
        ratingBar.setRating((float)rating);
        priceProduct.setText(String.valueOf(price));
        descriptionProduct.setText(description);
        stockProduct.setText(String.valueOf(stock));
        prodName.setText(name);




    }
}