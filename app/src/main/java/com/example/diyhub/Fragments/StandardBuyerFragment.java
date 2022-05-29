package com.example.diyhub.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.diyhub.R;
import com.example.diyhub.ViewPageAdapterProductDetailsStandard;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class StandardBuyerFragment extends Fragment {

    TabLayout tabLayout;

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
    ViewPager standardCustomViewPager;
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
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_standard_buyer, container, false);


        productBookFrom = view.findViewById(R.id.bookFromProduct);
        descriptionProduct = view.findViewById(R.id.descriptionProduct);
        priceProduct = view.findViewById(R.id.priceProduct);
        soldProduct = view.findViewById(R.id.productSoldStandardProductBuyer);
        productRating = view.findViewById(R.id.ratingNum);
        buyNow = view.findViewById(R.id.buyNowBtn);
        stockProduct = view.findViewById(R.id.stockProduct);
        ratingBar = view.findViewById(R.id.ratingBarBuyer);

        viewPager = view.findViewById(R.id.viewPagerStandardBuyer);
        addToCart = view.findViewById(R.id.addToCartStandard);
        backButton = view.findViewById(R.id.backButtonStandardProduct);


        prodImagesList = new ArrayList<>();



        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            prodImage = extras.getString("ProductImage");
            bookfrom = extras.getString("BookFrom");
            rating = extras.getDouble("Rating");
            sold = extras.getDouble("ProductSold");
            stock = extras.getInt("ProductStocks");

            description = extras.getString("ProductDescription");

            sellerID = extras.getString("SellerID");
            prodID = extras.getString("ProductID");
            shopName = extras.getString("ShopName");

        }

        productBookFrom.setText("Shop Address: " + bookfrom);
        productRating.setText(String.valueOf(rating));
        ratingBar.setRating((float) rating);

        descriptionProduct.setText(description);
        stockProduct.setText("Stock: " + String.valueOf(stock));

        soldProduct.setText(String.valueOf(sold) + " Sold");



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("ProductImages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prodImagesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductDetailsImagesList allList = snapshot.getValue(ProductDetailsImagesList.class);
                    prodImagesList.add(allList);
                }
                ViewPageAdapterProductDetailsStandard viewPageAdapterProductDetailsStandard = new ViewPageAdapterProductDetailsStandard(getContext(), prodImagesList);

                viewPager.setAdapter(viewPageAdapterProductDetailsStandard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}