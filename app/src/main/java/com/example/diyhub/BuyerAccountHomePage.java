package com.example.diyhub;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyhub.MESSAGES.Chat;
import com.example.diyhub.MESSAGES.ChatPage;
import com.example.diyhub.Notifications.NotificationPromo;
import com.example.diyhub.Notifications.NotificationPromoDisplay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.util.ArrayList;

public class BuyerAccountHomePage extends AppCompatActivity {

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



    int[] images = {R.drawable.img_7,
            R.drawable.img_8};

    String[] names = {};
    String[] shopRatings = {};
    TextView txt11;
    float[] shRatings = {};

    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private ArrayList<String> mNames1 = new ArrayList<>();
    private ArrayList<String> mImageUrls1 = new ArrayList<>();

    private ArrayList<String> mNames2 = new ArrayList<>();
    private ArrayList<String> mImageUrls2 = new ArrayList<>();
    String value1,value2,value3,value4,value5,value6,value7,value8,value9,value10,value11,value12,usernameBuyer,emailBuyer;
    TextView chatCounter;
    CardView chatCardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        cart = findViewById(R.id.cartImage);
        diy = findViewById(R.id.diyImage);
        profile = findViewById(R.id.profileImage);
        txt11 = findViewById(R.id.textView11);
        person = findViewById(R.id.searchForProducts);
        notif = findViewById(R.id.notificationButton);
        chat = findViewById(R.id.chatImageView);
        chatCounter = findViewById(R.id.chatCounterBuyer);
        chatCardView = findViewById(R.id.chatNumberContainerBuyer);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usernameBuyer = extras.getString("username");
            emailBuyer = extras.getString("buyerEmail");
            value1 = extras.getString("UserIDBuyer");
            value2= extras.getString("UserFirstnameBuyer");
            value3 = extras.getString("UserLastnameBuyer");
            value4 = extras.getString("UserBirthdateBuyer");
            value5 = extras.getString("UserPhoneBuyer");
            value6 = extras.getString("UserAddressBuyer");
            value7 = extras.getString("UserGenderBuyer");
            value8 = extras.getString("UserTypeBuyer");
            value9 = extras.getString("UserStatusBuyer");
            value10 = extras.getString("UserEmailBuyer");
            value11 = extras.getString("UserUsernameBuyer");
            value12 = extras.getString("UserPasswordBuyer");
        }


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCartPage();
            }
        });

        diy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomizePage();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfilePage();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChatPage();
            }
        });
        getImages();
        getImages1();
        getImages2();

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BuyerAccountHomePage.this, "Reminder Set", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(BuyerAccountHomePage.this, NotificationPromoDisplay.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(BuyerAccountHomePage.this, 0, intent, FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();

                long tenSeconds = 500 * 10;

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSeconds, pendingIntent);

                openNotifPromoPage();
            }
        });

        createNotificationChannel();
        updateMessageCount();

    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "DIY Hub Notifications Promo";
            String description = "DIY Hub Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("diyhub", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =  getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/jfkta.pale.s12.3%40gmail.com%20Valid%20ID's%2FVALID%20ID%2Fimage%3A4690?alt=media&token=2468ce64-2e76-45d0-bcc3-4e794ce3b594");
        mNames.add("Havasu Falls");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/jfkta.pale.s12.3%40gmail.com%20Valid%20ID's%2FVALID%20ID%2Fimage%3A4691?alt=media&token=762680f5-298b-425c-ab7b-609fbcae18d0");
        mNames.add("Trondheim");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/jfkta.pale.s12.3%40gmail.com%20Valid%20ID's%2FVALID%20ID%2Fimage%3A4693?alt=media&token=704e2712-472f-4696-a833-0736ec5bbb10");
        mNames.add("Portugal");

        mImageUrls.add("https://www.travelingcebu.com/images/store-crystals.jpg");
        mNames.add("Rocky Mountain National Park");


        mImageUrls.add("https://sugbo.ph/wp-content/uploads/2020/02/1cafes-and-coffee-shops-in-cebu-city.jpg");
        mNames.add("Mahahual");

        mImageUrls.add("https://cebu247.com/wp-content/uploads/2020/01/Cebu-Souvenir-Shops-1-696x364.jpg");
        mNames.add("Frozen Lake");


        mImageUrls.add("https://media-cdn.tripadvisor.com/media/photo-s/16/4c/85/12/bahandi-souvenir-and.jpg");
        mNames.add("White Sands Desert");

        mImageUrls.add("https://sugbo.ph/wp-content/uploads/2019/05/Mangaholics-Reading-Lounge-Cebu-City-1-1024x576.jpg");
        mNames.add("Austrailia");

        mImageUrls.add("https://mycebu.ph/wp-content/uploads/2017/11/Miniso-Cebu-24.jpg");
        mNames.add("Washington");

        initRecyclerView();

    }

    private void initRecyclerView(){
        Log.d("RecomShops", "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapterRecom adapter = new RecyclerViewAdapterRecom(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }
    private void getImages1(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls1.add("https://static.cdn.printful.com/blog/wp-content/uploads/2021/01/filter-aaa_Your-Design-Here-18-1.jpg");
        mNames1.add("Havasu Falls");

        mImageUrls1.add("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/etsy-gifts-1600809493.jpg?crop=0.497xw:0.993xh;0,0&resize=640:*");
        mNames1.add("Trondheim");

        mImageUrls1.add("https://media-cldnry.s-nbcnews.com/image/upload/t_fit-720w,f_auto,q_auto:best/newscms/2021_40/1784202/42080_1_640px.jpeg");
        mNames1.add("Portugal");

        mImageUrls1.add("https://media-cldnry.s-nbcnews.com/image/upload/t_fit-720w,f_auto,q_auto:best/newscms/2020_49/1645283/screen_shot_2020-11-30_at_10-32-04_pm.png");
        mNames1.add("Rocky Mountain National Park");


        mImageUrls1.add("https://i.insider.com/617873625d1dca0019a12265?width=1000&format=jpeg");
        mNames1.add("Mahahual");

        mImageUrls1.add("https://www.gannett-cdn.com/presto/2020/09/18/USAT/4ea71274-0f37-4391-b4be-21aef14a8c90-Personalizedgifts2020_mugs.png");
        mNames1.add("Frozen Lake");


        mImageUrls1.add("https://cdn.shopify.com/s/files/1/0070/7032/files/trending-products_c8d0d15c-9afc-47e3-9ba2-f7bad0505b9b.png?format=jpg&quality=90&v=1614559651");
        mNames1.add("White Sands Desert");

        mImageUrls1.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFHCClFT1HVt__sjqyCIKcI9BKf0J9L-ybCA&usqp=CAU");
        mNames1.add("Austrailia");

        mImageUrls1.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR0FRzMCYCFMAIN6CBTbqv5Qnqzdl38nWmrxA&usqp=CAU");
        mNames1.add("Washington");

        initRecyclerView1();

    }
    private void initRecyclerView1(){
        Log.d("TrendingItems", "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapterTrending adapter = new RecyclerViewAdapterTrending(this, mNames1, mImageUrls1);
        recyclerView.setAdapter(adapter);
    }
    private void getImages2(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls2.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFeF16TJpQjzXTDlkXyi-U1ffaz8q-X5K37Q&usqp=CAU");
        mNames2.add("Havasu Falls");

        mImageUrls2.add("https://sugbo.ph/wp-content/uploads/2019/12/artbarcebu-1-1024x768.jpg");
        mNames2.add("Trondheim");

        mImageUrls2.add("https://www.sunstar.com.ph/uploads/images/2018/12/28/112864.jpg");
        mNames2.add("Portugal");

        mImageUrls2.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTS93aXNDsQa2F4CbATvT3OAJnRAbEryzrIZA&usqp=CAU");
        mNames2.add("Rocky Mountain National Park");


        mImageUrls2.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFZyCwKSaDonxzNcQCfHnT4lwullmbQuKkAw&usqp=CAU");
        mNames2.add("Mahahual");

        mImageUrls2.add("https://cebu-sakura.com/uploads/column/37a6a124f38e7a7b45e775513e7ea20c.jpg");
        mNames2.add("Frozen Lake");


        mImageUrls2.add("https://sugbo.ph/wp-content/uploads/2018/02/flowershopsincebu.jpg");
        mNames2.add("White Sands Desert");

        mImageUrls2.add("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/18/60/23/92/getlstd-property-photo.jpg?w=500&h=400&s=1");
        mNames2.add("Austrailia");

        mImageUrls2.add("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/12/61/24/ef/straightforward-presentation.jpg?w=500&h=500&s=1");
        mNames2.add("Washington");

        initRecyclerView2();

    }
    private void initRecyclerView2(){
        Log.d("ShopsNearYou", "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapterShopsNear adapter = new RecyclerViewAdapterShopsNear(this, mNames2, mImageUrls2);
        recyclerView.setAdapter(adapter);
    }

    private void openCartPage() {
        Intent intent = new Intent(BuyerAccountHomePage.this, CartPage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openDIYPage() {
        Intent intent = new Intent(BuyerAccountHomePage.this, DIYPage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }


    }

    private void openProfilePage() {
        Intent intent = new Intent(BuyerAccountHomePage.this, ProfilePage.class);
        intent.putExtra("username",usernameBuyer);
        intent.putExtra("buyerEmail",emailBuyer);
        intent.putExtra("UserIDBuyer", value1);
        intent.putExtra("UserFirstnameBuyer", value2);
        intent.putExtra("UserLastnameBuyer", value3);
        intent.putExtra("UserBirthdateBuyer", value4);
        intent.putExtra("UserPhoneBuyer", value5);
        intent.putExtra("UserAddressBuyer", value6);
        intent.putExtra("UserGenderBuyer", value7);
        intent.putExtra("UserTypeBuyer", value8);
        intent.putExtra("UserStatusBuyer", value9);
        intent.putExtra("UserEmailBuyer", value10);
        intent.putExtra("UserUsernameBuyer", value11);
        intent.putExtra("UserPasswordBuyer", value12);
        startActivity(intent);
    }

    private void openNotifPromoPage() {
        Intent intent = new Intent(BuyerAccountHomePage.this, NotificationPromo.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    private void openChatPage() {
        Intent intent = new Intent(BuyerAccountHomePage.this, ChatPage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openCustomizePage() {
        Intent intent = new Intent(BuyerAccountHomePage.this, CustomizedProductPage.class);
        intent.putExtra("docName",data);
        startActivity(intent);
    }

    private void updateMessageCount() {
        Query reference = FirebaseDatabase.getInstance().getReference("Chats").orderByChild("MessageDateTime");
        String myID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myID) && !chat.isIsseen())
                    {
                        counter++;
                    }

                    chatCounter.setText(String.valueOf(counter));
                    chatCardView.setVisibility(counter == 0 ? View.INVISIBLE : View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMessageCount();
    }
}