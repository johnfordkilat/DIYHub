package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diyhub.AllProductsAdapter;
import com.example.diyhub.AllProductsBuyerAdapter;
import com.example.diyhub.AllProductsList;
import com.example.diyhub.AllProductsSellerAdapter;
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

public class ShopPageSeller extends AppCompatActivity {

    AllProductsSellerAdapter allProductsAdapter;
    RecyclerView allProductsRecyclerView;
    ArrayList<AllProductsList> allProductsLists;
    ArrayList<ShopsList> shopsLists;
    ProgressDialog progressDialog;
    String sellerID;
    SearchView searchView;
    ImageView back;
    TextView shopName;
    TextView rating;
    TextView location;
    ImageView shopImage;
    RecyclerView recommendedRecycler;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page_seller);

        allProductsRecyclerView = findViewById(R.id.sellerProductsRecyclerSeller);
        allProductsRecyclerView.setHasFixedSize(true);
        allProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recommendedRecycler = findViewById(R.id.sellerProductsRecyclerSellerRecommended);
        recommendedRecycler.setHasFixedSize(true);
        recommendedRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchView = findViewById(R.id.searchMessagesSeller);
        back = findViewById(R.id.backButtonShopPageSeller);
        shopName = findViewById(R.id.shopNameShopPageSeller);
        rating = findViewById(R.id.ratingShopPageSeller);
        shopImage = findViewById(R.id.shopImageSeller);
        location = findViewById(R.id.locationTxtShopPageSeller);
        tabLayout = findViewById(R.id.tabLayoutShopPageSeller);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getTabAt(0).isSelected())
                {
                    allProductsRecyclerView.setVisibility(View.VISIBLE);
                    recommendedRecycler.setVisibility(View.INVISIBLE);

                }
                if(tabLayout.getTabAt(1).isSelected())
                {
                    allProductsRecyclerView.setVisibility(View.INVISIBLE);
                    recommendedRecycler.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        progressDialog = new ProgressDialog(ShopPageSeller.this);


        allProductsLists = new ArrayList<>();
        shopsLists = new ArrayList<>();


        showData();

    }

    public void showData()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setTitle("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allProductsLists.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    AllProductsList allList = snapshot.getValue(AllProductsList.class);
                    allProductsLists.add(allList);

                }
                allProductsAdapter = new AllProductsSellerAdapter(getApplicationContext(),allProductsLists);
                allProductsRecyclerView.setAdapter(allProductsAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Shops");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopsLists.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {

                    ShopsList allList = snapshot.getValue(ShopsList.class);
                    if(allList.getSellerID().equalsIgnoreCase(user.getUid()))
                    {
                        shopsLists.add(allList);
                    }
                }
                shopName.setText(shopsLists.get(0).getShopName());
                rating.setText(String.valueOf(shopsLists.get(0).getShopRating()));
                location.setText(shopsLists.get(0).getShopAddress());
                Glide.with(ShopPageSeller.this).load(shopsLists.get(0).getShopImage()).into(shopImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(searchView != null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String text) {
        ArrayList<AllProductsList> filterList = new ArrayList<>();
        for(AllProductsList list : allProductsLists)
        {
            if(list.getProductName().toLowerCase().contains(text.toLowerCase()))
            {
                filterList.add(list);
            }
        }
        AllProductsAdapter adapter = new AllProductsAdapter(getApplicationContext(),filterList);
        allProductsRecyclerView.setAdapter(adapter);


    }
}