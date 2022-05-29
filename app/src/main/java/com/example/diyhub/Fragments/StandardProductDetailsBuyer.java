package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class StandardProductDetailsBuyer extends AppCompatActivity {

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
    Dialog closeDialogBuyNow;
    Dialog closeDialogCart;

    Spinner sizeSpinnerBuyNow;
    Spinner colorSpinnerBuyNow;
    ValueEventListener listener;
    DatabaseReference reference;

    ArrayList<String> list;
    ArrayAdapter adapter;

    ValueEventListener listenerSize;
    DatabaseReference referenceSize;

    ArrayList<String> listSize;
    ArrayAdapter adapterSize;
    String selectedSize="";
    String selectedColor="";
    ImageView backButton;
    TextView shopRatingTxt;
    TextView shippedFromTxt;
    TextView productNameTxt;
    ImageView shopImage;
    double shopRating;
    boolean favorite = false;

    double shipFee;
    double addFee;

    Button addToFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_product_details_buyer);

        productBookFrom = findViewById(R.id.shopAddressStandardDetailsBuyer);
        descriptionProduct = findViewById(R.id.descriptionStandardDetailsFinalBuyer);
        priceProduct = findViewById(R.id.ProductPriceStandardDetailsBuyer);
        soldProduct = findViewById(R.id.productSoldStandardDetailsBuyer);
        productRating = findViewById(R.id.productRatingTxtStandardBuyer);
        buyNow = findViewById(R.id.buyNowButtonStandardProductBuyer);
        stockProduct = findViewById(R.id.productStockStandardDetailsBuyer);
        ratingBar = findViewById(R.id.ratingBar2Buyer);
        shopNameTxt = findViewById(R.id.shopNameStandardDetailsBuyer);
        viewPager = findViewById(R.id.viewPagerStandardProductDetailsBuyer);
        addToCart = findViewById(R.id.addToCartButtonStandardProductBuyer);
        shopRatingTxt = findViewById(R.id.shopRatingStandardDetailsBuyer);
        shippedFromTxt = findViewById(R.id.shippedFromStandardDetailsBuyer);
        productNameTxt = findViewById(R.id.productNameStandardDetailsBuyer);
        backButton = findViewById(R.id.backButtonStandardProductBuyer);
        shopImage = findViewById(R.id.shopImageViewStandardDetailsBuyer);
        addToFav = findViewById(R.id.addToFavoritesButtonStandardBuyer);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        prodImagesList = new ArrayList<>();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



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
            shipFee = extras.getDouble("ProductShippingFee");
            addFee = extras.getDouble("ProductShippingFee");

        }

        productBookFrom.setText("Shop Address: "+bookfrom);
        shippedFromTxt.setText("Shipped From: "+bookfrom);
        productRating.setText(String.valueOf(rating));
        ratingBar.setRating((float)rating);
        priceProduct.setText("₱"+String.valueOf(price));
        descriptionProduct.setText(description);
        stockProduct.setText("Stock: "+String.valueOf(stock));
        shopNameTxt.setText("Shop Name: "+shopName);
        soldProduct.setText(String.valueOf(sold)+" Sold");
        productNameTxt.setText(name);

        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!favorite)
                {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> map = new HashMap<>();
                    map.put("ProductID",prodID);
                    map.put("ShopName", shopName);
                    map.put("ProductImage", prodImage);
                    map.put("isFavorites",true);
                    reference.child("Favorites").child(user.getUid()).child(prodID).updateChildren(map);
                    Toast.makeText(StandardProductDetailsBuyer.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    favorite = true;
                }
                else if(favorite)
                {
                    Toast.makeText(StandardProductDetailsBuyer.this, "Already added to Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DatabaseReference referenceshop = FirebaseDatabase.getInstance().getReference("Shops");
        referenceshop.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     for(DataSnapshot snapshot : dataSnapshot.getChildren())
                     {
                         ShopsList shopsList = snapshot.getValue(ShopsList.class);
                         if(shopsList.getSellerID().equalsIgnoreCase(sellerID))
                         {
                            shopRating = (float)shopsList.getShopRating();
                            shopRatingTxt.setText("Shop Rating: "+shopRating);
                             Glide.with(getApplicationContext()).load(shopsList.getShopImage()).into(shopImage);
                         }
                     }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("Variations-Standard").child("Color");
        referenceSize = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("Variations-Standard").child("Size");

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Order Confirmation");

                View view = getLayoutInflater().inflate(R.layout.layout_dialog_buy_now, null);
                EditText quantity = view.findViewById(R.id.setQuantityCartPage);
                Button submit = view.findViewById(R.id.submitButtonCartPage);
                colorSpinnerBuyNow = view.findViewById(R.id.colorSpinnerBuyNow);
                sizeSpinnerBuyNow = view.findViewById(R.id.sizeSpinnerBuyNow);

                builder.setView(view);
                fetchDataColor();
                fetchDataSize();

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
                            /*
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

                             */
                            closeDialogBuyNow.dismiss();

                            Intent intent = new Intent(StandardProductDetailsBuyer.this, PlaceOrderPageBuyer.class);
                            intent.putExtra("ProductID",prodID);
                            intent.putExtra("SellerID",sellerID);
                            intent.putExtra("ProductQuantity",quant);
                            intent.putExtra("ShopName",shopName);
                            intent.putExtra("Variations",selectedColor+", "+selectedSize);
                            intent.putExtra("ProductPrice",price);
                            intent.putExtra("ProductName",name);
                            intent.putExtra("ProductImage", prodImage);
                            startActivity(intent);
                        }

                    }
                });
                closeDialogBuyNow = builder.create();
                closeDialogBuyNow.show();



            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Cart Confirmation");

                View view = getLayoutInflater().inflate(R.layout.layout_dialog_buy_now, null);
                EditText quantity = view.findViewById(R.id.setQuantityCartPage);
                Button submit = view.findViewById(R.id.submitButtonCartPage);
                colorSpinnerBuyNow = view.findViewById(R.id.colorSpinnerBuyNow);
                sizeSpinnerBuyNow = view.findViewById(R.id.sizeSpinnerBuyNow);

                builder.setView(view);
                fetchDataColor();
                fetchDataSize();

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
                            map.put("SellerID",sellerID);
                            map.put("Variations",selectedColor+", "+selectedSize);
                            map.put("ShopName",shopName);
                            map.put("ProductShippingFee",shipFee);
                            map.put("ProductAdditionalFee",addFee);
                            reference.child("ShoppingCart").child(user.getUid()).child(prodID).updateChildren(map);
                            Toast.makeText(StandardProductDetailsBuyer.this, "Product Added to Cart", Toast.LENGTH_SHORT).show();
                            closeDialog.dismiss();


                        }

                    }
                });
                closeDialog = builder.create();
                closeDialog.show();


            }
        });

        DatabaseReference referenceprod = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("ProductImages");
        referenceprod.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prodImagesList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ProductDetailsImagesList allList = snapshot.getValue(ProductDetailsImagesList.class);
                    prodImagesList.add(allList);
                }
                ViewPageAdapterProductDetailsStandard viewPageAdapterProductDetailsStandard = new ViewPageAdapterProductDetailsStandard(StandardProductDetailsBuyer.this, prodImagesList);

                viewPager.setAdapter(viewPageAdapterProductDetailsStandard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchDataColor() {
        //Color List
        list = new ArrayList<String>();
        list.add(0, "Choose Color Variation");

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
        colorSpinnerBuyNow.setAdapter(adapter);

        //Get the data selected from dropdown spinner
        colorSpinnerBuyNow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    selectedColor = list.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    StandardProductVariationList standardProductSecondVariationList = snapshot.getValue(StandardProductVariationList.class);
                    list.add(standardProductSecondVariationList.getVariationName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchDataSize() {
        //Color List
        listSize = new ArrayList<String>();
        listSize.add(0, "Choose Size Variation");

        //Payment Spinner
        adapterSize = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listSize)
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
        sizeSpinnerBuyNow.setAdapter(adapterSize);

        //Get the data selected from dropdown spinner
        sizeSpinnerBuyNow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    selectedSize = listSize.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        referenceSize.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    StandardProductSecondVariationList standardProductSecondVariationList = snapshot.getValue(StandardProductSecondVariationList.class);
                    listSize.add(standardProductSecondVariationList.getVarSize());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}