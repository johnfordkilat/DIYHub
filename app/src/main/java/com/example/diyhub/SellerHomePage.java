package com.example.diyhub;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.diyhub.MESSAGES.Chat;
import com.example.diyhub.MESSAGES.ChatPage;
import com.example.diyhub.MESSAGES.MessageActivity;
import com.example.diyhub.MESSAGES.MessageAdapter;
import com.example.diyhub.Notifications.NotificationPromo;
import com.example.diyhub.Notifications.NotificationPromoDisplay;
import com.example.diyhub.Notifications.NotificationPromoList;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class SellerHomePage extends AppCompatActivity {

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
    StorageTask uploadTask;



    int[] images = {R.drawable.img_7,
            R.drawable.img_8};

    String[] names = {};
    String[] shopRatings = {};
    TextView txt11;
    float[] shRatings = {};
    TextView changeP;
    ImageView notifButton;

    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private ArrayList<String> mNames1 = new ArrayList<>();
    private ArrayList<String> mImageUrls1 = new ArrayList<>();

    private ArrayList<String> mNames2 = new ArrayList<>();
    private ArrayList<String> mImageUrls2 = new ArrayList<>();
    private static final int SELECT_PHOTOGOV = 1;

    Uri imageUri1,imageUri2,imageUri3;
    StorageReference storageReference1,storageReference2,storageReference3;
    ProgressDialog progressDialog;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    int index = 0;
    private int uploads = 0;
    private int success = 0;
    ImageView profPic;
    String usernameSeller,emailSeller;
    FirebaseAuth mAuth;
    String myUri;
    private DatabaseReference databaseReference;
    FirebaseFirestore dbFirestore;
    String value1,value2,value3,value4,value5,value6,value7,value8,value9,value10,value11,value12;
    TextView shopN,locSeller,phoneSeller;
    String locSell,phoneSell;
    ImageView products,orders,stats;
    TextView logout;
    String productSize;
    ImageView toOrderPage;
    TextView shopMotto;
    String shMotto;
    String userType;
    Button viewHistory;
    String header;
    TextView headerSeller;

    DatabaseReference reference;
    FirebaseUser firebaseUser;



    private TabLayout tabLayout;
    private ViewPager viewPager;


    private int[] tabIcons = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_products1,
            R.drawable.ic_orders1,
            R.drawable.ic_stats1
    };

    private ArrayList<String> userdata;

    ImageView chatImage;

    SharedPreferences sharedPreferences;

    public static final String fileName = "filename";
    public static final String Username = "username";
    public static final String Password = "password";

    Button increaseButton;
    int count = 0;
    int passedTab;

    TextView chatCounter;
    CardView chatCardview;

    CardView notifCardview;
    TextView notifCounter;

    CardView ordersnotifCardview;
    TextView ordersnotifCounter;

    int ordersTab;
    int orderRequestTab;

    String notifid[];

    int tabselected;

    ImageView image40;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home_page);

        tabLayout = findViewById(R.id.tabLayoutSellerHomePage);
        viewPager = findViewById(R.id.viewPageSellerHomePage1);
        headerSeller = findViewById(R.id.sellerHeader);

        chatImage = findViewById(R.id.chatImageViewButton);
        notifButton = findViewById(R.id.notificationButtonHome);

        chatCounter = findViewById(R.id.chatCounter);
        chatCardview = findViewById(R.id.chatNumberContainer);

        notifCounter = findViewById(R.id.notifCounter);
        notifCardview = findViewById(R.id.notificationNumberContainer);

        ordersnotifCardview = findViewById(R.id.ordersNotificationNumberContainer);
        ordersnotifCounter = findViewById(R.id.ordersNotifCounter);




        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usernameSeller = extras.getString("username");
            emailSeller = extras.getString("sellerEmail");
            locSell = extras.getString("locationSeller");
            phoneSell = extras.getString("phoneSeller");
            value1 = extras.getString("UserIDSeller");
            value2= extras.getString("UserFirstnameSeller");
            value3 = extras.getString("UserLastnameSeller");
            value4 = extras.getString("UserBirthdateSeller");
            value5 = extras.getString("UserPhoneSeller");
            value6 = extras.getString("UserAddressSeller");
            value7 = extras.getString("UserGenderSeller");
            value8 = extras.getString("UserTypeSeller");
            value9 = extras.getString("UserStatusSeller");
            value10 = extras.getString("UserEmailSeller");
            value11 = extras.getString("UserUsernameSeller");
            value12 = extras.getString("UserPasswordSeller");
            productSize = extras.getString("ProductSize");
            shMotto = extras.getString("UserMotto");
            userType = extras.getString("UserTypeSeller");
            passedTab = extras.getInt("Tab");
            ordersTab = extras.getInt("HomepageTab");
            orderRequestTab = extras.getInt("TablayoutTab");
        }




        if(ordersTab == 2)
        {
            tabLayout.getTabAt(2).select();
        }



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        userdata = new ArrayList<>();
        userdata.add(usernameSeller);
        userdata.add(shMotto);
        userdata.add(locSell);
        userdata.add(phoneSell);
        userdata.add(emailSeller);

        PageAdapter adapter = new PageAdapter(getApplicationContext(), getSupportFragmentManager(), userdata);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        
        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Toast.makeText(SellerHomePage.this, "Reminder Set", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SellerHomePage.this, NotificationPromoDisplay.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(SellerHomePage.this, 0, intent, FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();

                long tenSeconds = 500 * 10;

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSeconds, pendingIntent);

                //Log.d("SELLERERROR", "error");

                 */

                Intent intent1 = new Intent(SellerHomePage.this, NotificationPromo.class);
                startActivity(intent1);
            }
        });

        setupTabIcons();

        chatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerHomePage.this, ChatPage.class);
                startActivity(intent);
            }
        });

        if(passedTab == 1)
        {
            tabLayout.getTabAt(1).select();
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                if(tabLayout.getTabAt(0).isSelected())
                {
                    tabselected = 0;
                }
                else if(tabLayout.getTabAt(1).isSelected())
                {
                    tabselected = 1;
                }

                else if(tabLayout.getTabAt(2).isSelected())
                {
                    tabselected = 2;
                    if(tabselected == 2)
                    {
                        tabselected = 0;
                    }

                }
                else if(tabLayout.getTabAt(3).isSelected())
                {
                    tabselected = 3;
                }
                viewPager.setCurrentItem(tab.getPosition());
                headerSeller.setTextSize(44);
                headerSeller.setText(tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        createNotificationChannel();
        updateNotificationsCount();
        updateMessageCount();
        updateOrdersNotificationsCount();
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

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

    private void notification(){
            String name =  "DIY Hub Notifications Promo";
            String message = "Thank you";

             if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                 NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);

                 NotificationManager manager = getSystemService(NotificationManager.class);
                 manager.createNotificationChannel(channel);
             }




    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    private void updateOrdersNotificationsCount() {
        // TODO: larona, add your logic here

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counterNotif = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    NotificationPromoList notif = snapshot.getValue(NotificationPromoList.class);
                    if(notif.getIsSeen().equalsIgnoreCase("false") &&
                            (notif.getNotifHeader().equalsIgnoreCase("Order Request") ||
                             notif.getNotifHeader().equalsIgnoreCase("Accepted") ||
                             notif.getNotifHeader().equalsIgnoreCase("Ongoing")))
                    {
                        counterNotif++;
                    }


                    ordersnotifCounter.setText(String.valueOf(counterNotif));
                    ordersnotifCardview.setVisibility(counterNotif == 0 ? View.INVISIBLE : View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateNotificationsCount() {
        // TODO: larona, add your logic here

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counterNotif = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    NotificationPromoList notif = snapshot.getValue(NotificationPromoList.class);
                    if(notif.getIsSeen().equalsIgnoreCase("false"))
                    {
                        counterNotif++;
                    }

                    notifCounter.setText(String.valueOf(counterNotif));
                    notifCardview.setVisibility(counterNotif == 0 ? View.INVISIBLE : View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    chatCardview.setVisibility(counter == 0 ? View.INVISIBLE : View.VISIBLE);
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
        status("online");

        updateNotificationsCount();
        updateMessageCount();
        updateOrdersNotificationsCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    public void onBackPressed() {
    }
}
