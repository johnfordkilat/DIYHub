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
import android.widget.Button;
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
import androidx.viewpager.widget.ViewPager;

import com.example.diyhub.Fragments.HomePageAdapter;
import com.example.diyhub.Fragments.RecommendedShopsList;
import com.example.diyhub.Fragments.ShopsList;
import com.example.diyhub.Fragments.StandardProductBuyerAdapter;
import com.example.diyhub.MESSAGES.Chat;
import com.example.diyhub.MESSAGES.ChatPage;
import com.example.diyhub.Nearby.DistanceList;
import com.example.diyhub.Notifications.NotificationPromo;
import com.example.diyhub.Notifications.NotificationPromoDisplay;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    List<String> listID;
    List<ShopNearYouList> listShops;
    String sellerID;
    String shopName;
    String shopImage;
    ArrayList<RecommendedShopsList> slist;
    Button addRecom;
    Double shopRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        txt11 = findViewById(R.id.textView11);
        notif = findViewById(R.id.notificationButton);
        chat = findViewById(R.id.chatImageView);
        chatCounter = findViewById(R.id.chatCounterBuyer);
        chatCardView = findViewById(R.id.chatNumberContainerBuyer1);
        imageView7 = findViewById(R.id.imageView7);

        tabLayout = findViewById(R.id.tabLayoutBuyerHomePage);
        homeViewPager = findViewById(R.id.homeViewPager);

        HomePageAdapter adapter = new HomePageAdapter(getApplicationContext(), getSupportFragmentManager());
        homeViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(homeViewPager);

        /*
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyerAccountHomePage.this, MapCoverage.class);
                startActivity(intent);
            }
        });
         */
        listID = new ArrayList<>();
        listShops = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatPage();
            }
        });
        slist = new ArrayList<>();





        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getTabAt(0).isSelected())
                {
                    txt11.setText("HOME");
                }
                if(tabLayout.getTabAt(1).isSelected())
                {
                    txt11.setText("CART");
                }
                if(tabLayout.getTabAt(2).isSelected())
                {
                    txt11.setText("DIY");
                }
                if(tabLayout.getTabAt(3).isSelected())
                {
                    txt11.setText("PROFILE");
                }
                if(tabLayout.getTabAt(4).isSelected())
                {
                    txt11.setText("LOCATION");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());





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

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("lastLogged", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMessageCount();
        //status("online");


    }


    @Override
    protected void onPause() {
        super.onPause();
        //status("offline");
    }

    @Override
    public void onBackPressed() {

    }
}