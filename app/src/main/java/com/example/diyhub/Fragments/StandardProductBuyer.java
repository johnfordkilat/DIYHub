package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
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

import com.example.diyhub.AllProductsList;
import com.example.diyhub.Buyer.ItemViewModelCat1;
import com.example.diyhub.Buyer.ItemViewModelCat2;
import com.example.diyhub.Buyer.ItemViewModelCat3;
import com.example.diyhub.Buyer.ItemViewModelCustomSpecsText;
import com.example.diyhub.CartPageList;
import com.example.diyhub.PlaceOrderPageBuyer;
import com.example.diyhub.PlaceOrderPageBuyerAdapter;
import com.example.diyhub.R;
import com.google.android.material.tabs.TabLayout;
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

    int tab = 0;
    ViewPager standardCustomViewPager;
    TabLayout tabLayout;
    List<CartPageList> listCart;
    List<AllProductsList> allList;

    double shipFee;
    double addFee;

    String prodType;

    CardView hideCustom;

    private ItemViewModelCat1 viewModelCat1;
    private ItemViewModelCat2 viewModelCat2;
    private ItemViewModelCat3 viewModelCat3;
    private ItemViewModelCustomSpecsText viewModelSpecsTxt;

    DatabaseReference referenceCustom;
    DatabaseReference referenceSizeCustom;

    ArrayList<String> listSizeCustom;
    ArrayAdapter adapterSizeCustom;

    ArrayList<String> listCustom;
    ArrayAdapter adapterCustom;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_product_buyer);

        standardCustomViewPager = findViewById(R.id.standardCustomViewPager);
        tabLayout = findViewById(R.id.tabLayoutProductD1);

        productBookFrom = findViewById(R.id.bookFromProduct);
        descriptionProduct = findViewById(R.id.descriptionProduct);
        priceProduct = findViewById(R.id.priceProduct);
        soldProduct = findViewById(R.id.productSoldStandardProductBuyer);
        productRating = findViewById(R.id.ratingNum);
        buyNow = findViewById(R.id.buyNowBtn);
        stockProduct = findViewById(R.id.stockProduct);
        ratingBar = findViewById(R.id.ratingBarBuyer);
        shopNameTxt = findViewById(R.id.shopNameStandardProductBuyer);
        addToCart = findViewById(R.id.addToCartStandard);
        backButton = findViewById(R.id.backButtonStandardProduct);
        hideCustom = findViewById(R.id.alertMessageCardViewCustomBuyer);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        allList = new ArrayList<>();




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StandardProductBuyer.this.finish();
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
            tab = extras.getInt("Tab");
            shipFee = extras.getDouble("ProductShippingFee");
            addFee = extras.getDouble("ProductAdditionalFee");
            prodType = extras.getString("ProductType");

        }

        StandardProductBuyerAdapter adapter = new StandardProductBuyerAdapter(getApplicationContext(), getSupportFragmentManager());
        standardCustomViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(standardCustomViewPager);


        if(tab == 1)
        {
            tabLayout.getTabAt(1).select();
        }


        shopNameTxt.setText(name);
        priceProduct.setText("₱" + String.valueOf(price));

        //Standard Reference
        reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("Variations-Standard").child("Color");
        referenceSize = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("Variations-Standard").child("Size");

        //Custom Reference
        referenceCustom = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("Variations-Custom").child("Color");
        referenceSizeCustom = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("Variations-Custom").child("Size");


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
                        if (quantity.getText().toString().trim().isEmpty()) {
                            quantity.setError("Required");
                            quantity.requestFocus();
                            return;
                        } else {
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





                            Intent intent = new Intent(getApplicationContext(), PlaceOrderPageBuyer.class);
                            intent.putExtra("ProductID", prodID);
                            intent.putExtra("SellerID", sellerID);
                            intent.putExtra("ProductQuantity", quant);
                            intent.putExtra("ShopName", shopName);
                            intent.putExtra("Variations", selectedColor + ", " + selectedSize);
                            intent.putExtra("ProductPrice", price);
                            intent.putExtra("ProductName", name);
                            intent.putExtra("ProductImage", prodImage);
                            viewModelCat1 = new ViewModelProvider(StandardProductBuyer.this).get(ItemViewModelCat1.class);
                            viewModelCat1.getSelectedItem().observe(StandardProductBuyer.this, item -> {
                                intent.putExtra("Category1", item);
                            });
                            viewModelCat2 = new ViewModelProvider(StandardProductBuyer.this).get(ItemViewModelCat2.class);
                            viewModelCat2.getSelectedItem().observe(StandardProductBuyer.this, item -> {
                                intent.putExtra("Category2", item);

                            });
                            viewModelCat3 = new ViewModelProvider(StandardProductBuyer.this).get(ItemViewModelCat3.class);
                            viewModelCat3.getSelectedItem().observe(StandardProductBuyer.this, item -> {

                                intent.putExtra("Category3", item);
                            });
                            viewModelSpecsTxt = new ViewModelProvider(StandardProductBuyer.this).get(ItemViewModelCustomSpecsText.class);
                            viewModelSpecsTxt.getSelectedItem().observe(StandardProductBuyer.this, item -> {

                                intent.putExtra("CustomSpecsTxt", item);
                            });
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

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (quantity.getText().toString().trim().isEmpty()) {
                            quantity.setError("Required");
                            quantity.requestFocus();
                            return;
                        } else {


                            int quant = Integer.parseInt(String.valueOf(quantity.getText().toString().trim()));
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> map = new HashMap<>();
                            map.put("ProductName", name);
                            map.put("ProductID", prodID);
                            map.put("ProductImage", prodImage);
                            map.put("ProductQuantity", quant);
                            map.put("SellerID", sellerID);
                            map.put("Variations", selectedColor + ","+ selectedSize);
                            map.put("ShopName", shopName);
                            map.put("ProductShippingFee", shipFee);
                            map.put("ProductAdditionalFee", addFee);
                            map.put("ProductType", prodType);
                            map.put("ProductPrice", price);
                            map.put("TotalPrice", 0);
                            reference.child("ShoppingCart").child(user.getUid()).child(prodID).updateChildren(map);
                            Toast.makeText(getApplicationContext(), "Product Added to Cart", Toast.LENGTH_SHORT).show();
                            closeDialog.dismiss();

                        }

                    }
                });
                closeDialog = builder.create();
                closeDialog.show();


            }
        });


    }

    private void fetchDataColor() {
        //Color List
        list = new ArrayList<String>();
        list.add(0, "Choose Color Variation");

        //Payment Spinner
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
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
                if (position > 0) {
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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
        adapterSize = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listSize) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
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
                if (position > 0) {
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StandardProductSecondVariationList standardProductSecondVariationList = snapshot.getValue(StandardProductSecondVariationList.class);
                    listSize.add(standardProductSecondVariationList.getVarSize());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchDataColorCustom() {
        //Color List
        listCustom = new ArrayList<String>();
        listCustom.add(0, "Choose Color Variation");

        //Payment Spinner
        adapterCustom = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listCustom) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        colorSpinnerBuyNow.setAdapter(adapterCustom);

        //Get the data selected from dropdown spinner
        colorSpinnerBuyNow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedColor = listCustom.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        referenceCustom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StandardProductVariationList standardProductSecondVariationList = snapshot.getValue(StandardProductVariationList.class);
                    listCustom.add(standardProductSecondVariationList.getVariationName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchDataSizeCustom() {
        //Color List
        listSizeCustom = new ArrayList<String>();
        listSizeCustom.add(0, "Choose Size Variation");

        //Payment Spinner
        adapterSizeCustom = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listSizeCustom) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        sizeSpinnerBuyNow.setAdapter(adapterSizeCustom);

        //Get the data selected from dropdown spinner
        sizeSpinnerBuyNow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedSize = listSizeCustom.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        referenceSizeCustom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StandardProductSecondVariationList standardProductSecondVariationList = snapshot.getValue(StandardProductSecondVariationList.class);
                    listSizeCustom.add(standardProductSecondVariationList.getVarSize());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}