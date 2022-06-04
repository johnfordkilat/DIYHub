package com.example.diyhub.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diyhub.Nearby.DistanceList;
import com.example.diyhub.R;
import com.example.diyhub.RecyclerViewAdapterAll;
import com.example.diyhub.RecyclerViewAdapterRecom;
import com.example.diyhub.RecyclerViewAdapterShopsNear;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


public class BuyerHomeFragment extends Fragment {

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
    ArrayList<RecommendedShopsList> slist;
    Button addRecom;
    Double shopRating;

    ImageView filterButton;
    Dialog customDialog;

    TextView recommButton;
    TextView shopsNearYouButton;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_buyer_home, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        mImageUrls = new ArrayList<>();
        getLocation = view.findViewById(R.id.getLocationButton);
        shopsNearRecycler = view.findViewById(R.id.recyclerView2);
        recomShops = view.findViewById(R.id.recyclerView3sample);
        filterButton = view.findViewById(R.id.filterButtonHomePage);
        recommButton = view.findViewById(R.id.recommButtonBuyerHomePage);
        shopsNearYouButton = view.findViewById(R.id.viewAllShopsNearYouButtonHomePage);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCurrentLocation();

            }
        });
        slist = new ArrayList<>();
        customDialog = new Dialog(getContext());

        recommButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllRecommShopsListViewHomePage.class);
                intent.putExtra("SellerID",sellerID);
                startActivity(intent);
            }
        });

        shopsNearYouButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllShopsNearYouListViewHomePage.class);
                intent.putExtra("SellerID",sellerID);
                startActivity(intent);
            }
        });


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    customDialog.setContentView(R.layout.filter_layout);
                    customDialog.setCancelable(false);
                    customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    ImageView close = customDialog.findViewById(R.id.imageViewClose);

                    Button mostViewed = customDialog.findViewById(R.id.mostViewedButtonFilter);
                    Button purchases = customDialog.findViewById(R.id.purchasesButtonFilter);
                    Button shopsRating = customDialog.findViewById(R.id.shopsRatingButtonFilter);



                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customDialog.dismiss();

                        }
                    });

                    customDialog.show();
            }
        });


        listShops = new ArrayList<>();
        listID = new ArrayList<>();
        shopsLists = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        shopsNearRecycler.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recomShops.setLayoutManager(layoutManager2);

        getImagesAll();
        getImagesShopsNearYou();
        getImagesRecomShops();
        //getImagesTrendingItems();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("ProceedToCartFromHomepage"));

        return view;



    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            sellerID = intent.getStringExtra("SellerIDCart");

        }
    };

    private void getImagesAll(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Shops");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mImageUrls.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ShopsList list = snapshot.getValue(ShopsList.class);
                    mImageUrls.add(list);
                }
                RecyclerViewAdapterAll adapter = new RecyclerViewAdapterAll(getContext(), mImageUrls);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getImagesShopsNearYou(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        DatabaseReference refNearYou1 = FirebaseDatabase.getInstance().getReference("Shops");
        refNearYou1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ShopsList listShops1 = snapshot.getValue(ShopsList.class);
                    double dist = Double.parseDouble(listShops1.getDistance());
                    if(dist <= 2.0)
                    {
                        shopsLists.add(listShops1);
                    }
                }
                RecyclerViewAdapterShopsNear adapter = new RecyclerViewAdapterShopsNear(getContext(), shopsLists);
                shopsNearRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getImagesRecomShops(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

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
                    RecyclerViewAdapterRecom adapter = new RecyclerViewAdapterRecom(getContext(), slist);
                    recomShops.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /*
    private void getImagesTrendingItems(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

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
                    RecyclerViewAdapterAll adapter = new RecyclerViewAdapterAll(getContext(), slist);
                    recyclerTrending.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

     */


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(getActivity())
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(getActivity())
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("latitude", String.valueOf(latitude));
                                        map.put("longitude", String.valueOf(longitude));
                                        reference.child("UserLocation").child(user.getUid()).updateChildren(map);


                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(getContext(), "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(getActivity(), 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }


}