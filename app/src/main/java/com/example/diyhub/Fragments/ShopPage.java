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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.diyhub.AllProductsAdapter;
import com.example.diyhub.AllProductsBuyerAdapter;
import com.example.diyhub.AllProductsList;
import com.example.diyhub.FollowingListShopPageBuyer;
import com.example.diyhub.HoldProductsAdapter;
import com.example.diyhub.HoldProductsList;
import com.example.diyhub.Notifications.Data;
import com.example.diyhub.R;
import com.example.diyhub.RestockProductsAdapter;
import com.example.diyhub.RestockProductsList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopPage extends AppCompatActivity {

    AllProductsBuyerAdapter allProductsAdapter;
    RecyclerView allProductsRecyclerView;
    ArrayList<AllProductsList> allProductsLists;
    ProgressDialog progressDialog;
    String sellerID;
    SearchView searchView;
    ImageView favButton;
    String shopName;
    TextView shopNameTxt;
    TextView ratingTxt;
    RatingBar ratingBar;
    boolean followed;
    double shopRating;
    ImageView backButton;
    String shopImage;
    CircleImageView shopImageView;
    List<FollowingListShopPageBuyer> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);

        allProductsRecyclerView = findViewById(R.id.sellerProductsRecycler);
        allProductsRecyclerView.setHasFixedSize(true);
        allProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchView = findViewById(R.id.searchMessagesBuyer);
        favButton = findViewById(R.id.favoriteButtonShopPageBuyer);
        shopNameTxt = findViewById(R.id.shopNameTxtShopPage);
        ratingBar = findViewById(R.id.ratingBarShopPage);
        ratingTxt = findViewById(R.id.ratingTxtShopPage);
        backButton = findViewById(R.id.backButtonShopPageBuyer);
        shopImageView = findViewById(R.id.shopPageBuyerImage);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            sellerID = extras.getString("SellerID");
            shopName = extras.getString("ShopName");
            shopRating = extras.getDouble("Rating");
            shopImage = extras.getString("ShopImage");
        }
        shopNameTxt.setText(shopName);
        ratingTxt.setText(String.valueOf(shopRating));
        ratingBar.setRating(Float.parseFloat(String.valueOf(shopRating)));
        progressDialog = new ProgressDialog(ShopPage.this);
        Glide.with(this).load(shopImage).into(shopImageView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Following").child(user.getUid()).child(sellerID);
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    favButton.setImageResource(R.drawable.heart);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Following").child(user.getUid()).child(sellerID);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ShopPage.this, "Unfollowed", Toast.LENGTH_SHORT).show();
                                    favButton.setImageResource(R.drawable.ic_followed);
                                }
                            });
                        }
                        else
                        {
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> map1 = new HashMap<>();
                            map1.put("SellerID",sellerID);
                            map1.put("ShopName", shopName);
                            map1.put("isFollowed",true);
                            map1.put("ShopImage",shopImage);
                            reference1.child("Following").child(user.getUid()).child(sellerID).updateChildren(map1);
                            Toast.makeText(ShopPage.this, "Added to following", Toast.LENGTH_SHORT).show();
                            favButton.setImageResource(R.drawable.heart);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });








        allProductsLists = new ArrayList<>();

        showData();
    }


    public void showData()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setTitle("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allProductsLists.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    AllProductsList allList = snapshot.getValue(AllProductsList.class);
                    allProductsLists.add(allList);

                }
                allProductsAdapter = new AllProductsBuyerAdapter(getApplicationContext(),allProductsLists);
                allProductsRecyclerView.setAdapter(allProductsAdapter);
                progressDialog.dismiss();
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