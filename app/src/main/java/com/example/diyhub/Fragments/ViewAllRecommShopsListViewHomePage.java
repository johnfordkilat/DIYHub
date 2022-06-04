package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.diyhub.Nearby.DistanceList;
import com.example.diyhub.R;
import com.example.diyhub.RecyclerViewAdapterRecom;
import com.example.diyhub.RecyclerViewAdapterRecomShops;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllRecommShopsListViewHomePage extends AppCompatActivity {

    ArrayList<RecommendedShopsList> slist;
    Connection connect;
    String ConnectionResult = "";
    ImageView cart,diy,profile,notif,chat;
    RatingBar simpleRatingBar;
    String data;

    ListView listView;
    String names1;
    String pics;
    ImageView imageView;
    String shRating;
    private int lastFocussedPosition = -1;
    private Handler handler = new Handler();

    EditText person;
    View view;
    TabLayout tabLayout;
    ViewPager homeViewPager;

    int[] images = {R.drawable.img_7,
            R.drawable.img_8};

    String[] names = {};
    String[] shopRatings = {};
    TextView txt11;
    float[] shRatings = {};

    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<ShopsList> mImageUrls;

    private ArrayList<String> mNames1 = new ArrayList<>();
    private ArrayList<String> mImageUrls1 = new ArrayList<>();

    private ArrayList<String> mNames2 = new ArrayList<>();
    private ArrayList<String> mImageUrls2 = new ArrayList<>();
    String value1,value2,value3,value4,value5,value6,value7,value8,value9,value10,value11,value12,usernameBuyer,emailBuyer;
    TextView chatCounter;
    CardView chatCardView;

    RecyclerView recyclerView;

    ImageView imageView7;
    FloatingActionButton getLocation;
    private LocationRequest locationRequest;
    String sellerID;
    Button seeLoc;
    TextView addressTxt;

    EditText mapTxt;
    ArrayList<DistanceList> listShops;
    List<String> listID;

    RecyclerView shopsNearRecycler;
    ArrayList<ShopsList> shopsLists;
    int i=0;
    RecyclerView recomShops;
    String shopName;
    String shopImage;
    Button addRecom;
    Double shopRating;

    ImageView filterButton;
    Dialog customDialog;

    TextView recommButton;
    RecyclerView recommShopsRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_shops_list_view_home_page);

        recommShopsRV = findViewById(R.id.recommShopsRV);
        recommShopsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        slist = new ArrayList<>();

        getImagesRecomShops();
    }


    private void getImagesRecomShops(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Shops");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //Get number of Shops followed
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RecommendedShopsList list = snapshot.getValue(RecommendedShopsList.class);
                    double rating = list.getShopRating();
                    if (rating >= 4.8) {
                        int views = list.getShopViews();
                        if (views > 20) {
                            slist.add(list);
                            sellerID = list.getSellerID();
                            shopName = list.getShopName();
                            shopImage = list.getShopImage();
                            shopRating = list.getShopRating();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> map = new HashMap<>();
                            map.put("SellerId", sellerID);
                            map.put("ShopName", shopName);
                            map.put("ShopImage", shopImage);
                            map.put("ShopRating", shopRating);
                            reference.child("RecommendedShops").child(sellerID).updateChildren(map);
                        }
                    }
                    RecyclerViewAdapterRecomShops adapter = new RecyclerViewAdapterRecomShops(getApplicationContext(), slist);
                    recommShopsRV.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}