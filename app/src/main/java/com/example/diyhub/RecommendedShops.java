package com.example.diyhub;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecommendedShops extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> seq = new ArrayList<>();
    //private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_shops);
        //getImages();
    }

    /*
    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://i0.wp.com/cebufinest.com/wp-content/uploads/2016/04/cebufinest_aizilymstore_budgetstorecebu.jpg?resize=600%2C380");
        mNames.add("Havasu Falls");
        seq.add("1");

        mImageUrls.add("https://i0.wp.com/cebufinest.com/wp-content/uploads/2016/04/cebufinest_OngKinKing_budgetstorecebu.jpg?resize=600%2C380");
        mNames.add("Trondheim");
        seq.add("2");

        mImageUrls.add("https://d1k571r5p7i4n1.cloudfront.net/28fbe0c02c4881006458f24488f6c602/large/Islands_Souvenirs.jpg");
        mNames.add("Portugal");
        seq.add("3");

        mImageUrls.add("https://www.travelingcebu.com/images/store-crystals.jpg");
        mNames.add("Rocky Mountain National Park");
        seq.add("4");


        mImageUrls.add("https://sugbo.ph/wp-content/uploads/2020/02/1cafes-and-coffee-shops-in-cebu-city.jpg");
        mNames.add("Mahahual");
        seq.add("5");

        mImageUrls.add("https://cebu247.com/wp-content/uploads/2020/01/Cebu-Souvenir-Shops-1-696x364.jpg");
        mNames.add("Frozen Lake");
        seq.add("6");


        mImageUrls.add("https://media-cdn.tripadvisor.com/media/photo-s/16/4c/85/12/bahandi-souvenir-and.jpg");
        mNames.add("White Sands Desert");
        seq.add("7");

        mImageUrls.add("https://sugbo.ph/wp-content/uploads/2019/05/Mangaholics-Reading-Lounge-Cebu-City-1-1024x576.jpg");
        mNames.add("Austrailia");
        seq.add("8");

        mImageUrls.add("https://mycebu.ph/wp-content/uploads/2017/11/Miniso-Cebu-24.jpg");
        mNames.add("Washington");
        seq.add("9");



        initRecyclerView();
        initRecyclerView1();

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recylcerRecom1);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapterRecom adapter = new RecyclerViewAdapterRecom(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }
    private void initRecyclerView1(){
        Log.d("RecomShops", "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recylcerRecom2);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapterRecomShops adapter = new RecyclerViewAdapterRecomShops(this, mNames, mImageUrls, seq);
        recyclerView.setAdapter(adapter);
    }

     */
}