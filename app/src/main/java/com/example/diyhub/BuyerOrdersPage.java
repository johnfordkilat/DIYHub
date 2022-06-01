package com.example.diyhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyhub.Fragments.CancelledOrdersAdapter;
import com.example.diyhub.Fragments.CompletedOrdersAdapter;
import com.example.diyhub.Fragments.OrdersAcceptedAdapter;
import com.example.diyhub.Fragments.OrdersAdapter;
import com.example.diyhub.Fragments.OrdersList;
import com.example.diyhub.Fragments.OrdersOngoingAdapter;
import com.example.diyhub.Fragments.ReturnOrRefundOrdersAdapter;
import com.example.diyhub.Fragments.ToBookAdapter;
import com.example.diyhub.Fragments.ToReceiveAdapter;
import com.example.diyhub.Notifications.APIService;
import com.example.diyhub.Notifications.CLient;
import com.example.diyhub.Notifications.Data;
import com.example.diyhub.Notifications.MyResponse;
import com.example.diyhub.Notifications.NotificationPromoList;
import com.example.diyhub.Notifications.Sender;
import com.example.diyhub.Notifications.Token;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyerOrdersPage extends AppCompatActivity{

    RecyclerView recyclerView;
    ImageView notif,back;
    TextView all,res,hold;
    int count=0;
    String emailSeller;
    FirebaseFirestore dbFirestore;
    OrdersAdapter toPayAdapter;
    ArrayList<OrdersList> toPayLists;

    ProgressDialog progressDialog;
    Button uploadProduct;

    Uri imageUri1,imageUri2,imageUri3;
    StorageReference storageReference1,storageReference2,storageReference3;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    int index = 0;
    private int uploads = 0;
    private int success = 0;
    ImageView profPic;
    String usernameSeller;
    FirebaseAuth mAuth;
    String myUri;
    private DatabaseReference databaseReference;
    String value1,value2,value3,value4,value5,value6,value7,value8,value9,value10,value11,value12;
    TextView shopN,locSeller,phoneSeller;
    String locSell,phoneSell;
    ImageView products,orders,stats;
    String productName,productQuantity,productStocks;
    private static final int SELECT_PHOTOGOV = 1;
    int dialog = 0;
    TextView noProduct;
    Button toOrderPage;

    RecyclerView rvToBook, rvToReceive,rvToPay;
    TabLayout tabLayoutBuyerOrdersPage1;
    TabLayout tabLayoutBuyerOrdersPage2;
    TabLayout tabLayoutToReceive;
    RecyclerView rvPending,rvOngoing, rvAccepted;
    RecyclerView rvAll,rvPickUp;


    DatabaseReference referenceOrderRequest;
    DatabaseReference referenceAccepted;
    DatabaseReference referenceOngoing;
    DatabaseReference referenceToBook;
    DatabaseReference referenceToReceive;

    ArrayList<OrdersList> ordersListsOrderRequest;
    ArrayList<OrdersList> ordersListsAccepted;
    ArrayList<OrdersList> ordersListsOngoing;
    ArrayList<OrdersList> ordersListsToBook;
    ArrayList<OrdersList> ordersListsToReceive;
    ArrayList<OrdersList> ordersListsCompleted;
    ArrayList<OrdersList> ordersListsCancelled;
    ArrayList<OrdersList> ordersListsReturnOrRefund;


    OrdersAdapter ordersAdapterOrderRequest;
    OrdersAcceptedAdapter ordersAdapterAccepted;
    OrdersOngoingAdapter ordersAdapterOngoing;
    ToBookAdapter ordersAdapterToBook;
    ToReceiveAdapter ordersAdapterToReceive;
    CompletedOrdersAdapter ordersAdapterCompleted;
    CancelledOrdersAdapter ordersAdapterCancelled;
    ReturnOrRefundOrdersAdapter ordersAdapterReturnOrRefund;

    /*RecyclerView recyclerViewCompleted;
    RecyclerView recyclerViewCancelled;
    RecyclerView recyclerViewReturnOrRefund;*/


    String orderRequestImage="https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/johnfordtapales8%40gmail.com%2FOrderForm-Icon-removebg-preview.png?alt=media&token=a290336b-566b-4a74-bee0-b7ed261961d7";


    boolean pendingRequestTab = false;
    boolean acceptedTab = false;
    boolean ongoingTab = false;

    CardView ordersnotifCardviewOrderRequestB;
    TextView ordersnotifCounterOrderRequestB;

    CardView ordersnotifCardviewAcceptedB;
    TextView ordersnotifCounterAcceptedB;

    CardView ordersnotifCardviewOngoingB;
    TextView ordersnotifCounterOngoingB;


    String[] notifidNotif;
    int indexcount = 0;

    List<String> listorder;
    List<Integer> indexorder;

    APIService apiService = apiService = CLient.getClient("https://fcm.googleapis.com/").create(APIService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_orders_page);


        back = findViewById(R.id.imageView49);
        tabLayoutBuyerOrdersPage1 = findViewById(R.id.tabLayoutBuyerOrdersPage1);
        tabLayoutBuyerOrdersPage2 = findViewById(R.id.tabLayoutBuyerOrdersPage2);
        tabLayoutToReceive = findViewById(R.id.tabLayoutBuyerOrdersPage3);


        rvToPay = findViewById(R.id.toPayRecyclerBuyer);
        rvToPay.setHasFixedSize(true);
        rvToPay.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rvToBook = findViewById(R.id.toBookRecyclerBuyer);
        rvToBook.setHasFixedSize(true);
        rvToBook.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rvToReceive = findViewById(R.id.toReceiveRecyclerBuyer);
        rvToReceive.setHasFixedSize(true);
        rvToReceive.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rvPending = findViewById(R.id.pendingRvBuyer);
        rvPending.setHasFixedSize(true);
        rvPending.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rvAccepted = findViewById(R.id.acceptedRvBuyer);
        rvAccepted.setHasFixedSize(true);
        rvAccepted.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rvOngoing = findViewById(R.id.ongoingRvBuyer);
        rvOngoing.setHasFixedSize(true);
        rvOngoing.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rvAll = findViewById(R.id.allRvBuyer);
        rvAll.setHasFixedSize(true);
        rvAll.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rvPickUp = findViewById(R.id.pickUpRvBuyer);
        rvPickUp.setHasFixedSize(true);
        rvPickUp.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ordersnotifCardviewOrderRequestB = findViewById(R.id.ordersNotificationNumberContainerOrderRequestB);
        ordersnotifCounterOrderRequestB = findViewById(R.id.ordersNotifCounterOrderRequestB);

        ordersnotifCardviewAcceptedB = findViewById(R.id.ordersNotificationNumberContainerAcceptedB);
        ordersnotifCounterAcceptedB = findViewById(R.id.ordersNotifCounterAcceptedB);

        ordersnotifCardviewOngoingB = findViewById(R.id.ordersNotificationNumberContainerOngoingB);
        ordersnotifCounterOngoingB = findViewById(R.id.ordersNotifCounterOngoingB);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        dbFirestore = FirebaseFirestore.getInstance();
        toPayLists = new ArrayList<>();


        progressDialog = new ProgressDialog(BuyerOrdersPage.this);
        mAuth = FirebaseAuth.getInstance();


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));
        rvToPay.addItemDecoration(dividerItemDecoration);
        rvToBook.addItemDecoration(dividerItemDecoration);
        rvToReceive.addItemDecoration(dividerItemDecoration);



        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
            showData();


        rvToPay.setVisibility(View.VISIBLE);
        rvToBook.setVisibility(View.INVISIBLE);
        rvToReceive.setVisibility(View.INVISIBLE);
        tabLayoutBuyerOrdersPage1.setVisibility(View.VISIBLE);
        tabLayoutToReceive.setVisibility(View.INVISIBLE);
        ordersnotifCardviewOrderRequestB.setVisibility(View.INVISIBLE);
        ordersnotifCardviewAcceptedB.setVisibility(View.INVISIBLE);
        ordersnotifCardviewOngoingB.setVisibility(View.INVISIBLE);

        tabLayoutBuyerOrdersPage1.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == 0)
                {
                    rvToPay.setVisibility(View.VISIBLE);
                    rvToBook.setVisibility(View.INVISIBLE);
                    rvToReceive.setVisibility(View.INVISIBLE);
                    rvPending.setVisibility(View.INVISIBLE);
                    rvOngoing.setVisibility(View.VISIBLE);
                    rvAccepted.setVisibility(View.INVISIBLE);
                    rvAll.setVisibility(View.INVISIBLE);
                    rvPickUp.setVisibility(View.INVISIBLE);
                    tabLayoutBuyerOrdersPage2.setVisibility(View.VISIBLE);
                    tabLayoutToReceive.setVisibility(View.INVISIBLE);
                    updateOrdersNotificationsCountOrderRequest();
                    updateOrdersNotificationsCountAccepted();
                    updateOrdersNotificationsCountOngoing();



                    if(pendingRequestTab == true)
                    {
                        rvAccepted.setVisibility(View.INVISIBLE);
                        rvToPay.setVisibility(View.VISIBLE);
                        rvOngoing.setVisibility(View.INVISIBLE);

                    }

                    if(acceptedTab == true)
                    {
                        rvAccepted.setVisibility(View.VISIBLE);
                        rvToPay.setVisibility(View.INVISIBLE);
                        rvOngoing.setVisibility(View.INVISIBLE);

                    }

                    if(ongoingTab == true)
                    {
                        rvOngoing.setVisibility(View.VISIBLE);
                        rvToPay.setVisibility(View.INVISIBLE);
                        rvAccepted.setVisibility(View.INVISIBLE);

                    }

                }



                if(tab.getPosition() == 1)
                {
                    rvToPay.setVisibility(View.INVISIBLE);
                    rvToBook.setVisibility(View.VISIBLE);
                    rvToReceive.setVisibility(View.INVISIBLE);
                    rvPending.setVisibility(View.INVISIBLE);
                    rvOngoing.setVisibility(View.INVISIBLE);
                    rvAccepted.setVisibility(View.INVISIBLE);
                    rvAll.setVisibility(View.INVISIBLE);
                    rvPickUp.setVisibility(View.INVISIBLE);
                    tabLayoutBuyerOrdersPage2.setVisibility(View.INVISIBLE);
                    tabLayoutToReceive.setVisibility(View.INVISIBLE);
                    ordersnotifCardviewOrderRequestB.setVisibility(View.INVISIBLE);
                    ordersnotifCardviewAcceptedB.setVisibility(View.INVISIBLE);
                    ordersnotifCardviewOngoingB.setVisibility(View.INVISIBLE);

                    if(acceptedTab == true)
                    {
                        rvAccepted.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOrderRequestB.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewAcceptedB.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOngoingB.setVisibility(View.INVISIBLE);
                    }

                    if(ongoingTab == true)
                    {
                        rvOngoing.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOrderRequestB.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewAcceptedB.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOngoingB.setVisibility(View.INVISIBLE);
                    }

                    showData();


                }

                if(tab.getPosition() == 2)
                {
                    rvToPay.setVisibility(View.INVISIBLE);
                    rvToBook.setVisibility(View.INVISIBLE);
                    rvToReceive.setVisibility(View.VISIBLE);
                    rvPending.setVisibility(View.INVISIBLE);
                    rvOngoing.setVisibility(View.INVISIBLE);
                    rvAccepted.setVisibility(View.INVISIBLE);
                    rvAll.setVisibility(View.INVISIBLE);
                    rvPickUp.setVisibility(View.INVISIBLE);
                    tabLayoutBuyerOrdersPage2.setVisibility(View.INVISIBLE);
                    tabLayoutToReceive.setVisibility(View.VISIBLE);
                    ordersnotifCardviewOrderRequestB.setVisibility(View.INVISIBLE);
                    ordersnotifCardviewAcceptedB.setVisibility(View.INVISIBLE);
                    ordersnotifCardviewOngoingB.setVisibility(View.INVISIBLE);

                    if(acceptedTab == true)
                    {
                        rvAccepted.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOrderRequestB.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewAcceptedB.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOngoingB.setVisibility(View.INVISIBLE);
                    }

                    if(ongoingTab == true)
                    {
                        rvOngoing.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOrderRequestB.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewAcceptedB.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOngoingB.setVisibility(View.INVISIBLE);
                    }

                    showData();

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayoutBuyerOrdersPage2.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == 0)
                {
                    pendingRequestTab = true;
                    acceptedTab = false;
                    ongoingTab = false;
                    rvToPay.setVisibility(View.VISIBLE);
                    rvAccepted.setVisibility(View.INVISIBLE);
                    rvOngoing.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "ORDERS REQUEST", Toast.LENGTH_SHORT).show();

                }

                if(tab.getPosition() == 1)
                {
                    pendingRequestTab = false;
                    acceptedTab = true;
                    ongoingTab = false;
                    rvToPay.setVisibility(View.INVISIBLE);
                    rvAccepted.setVisibility(View.VISIBLE);
                    rvOngoing.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "ACCEPTED", Toast.LENGTH_SHORT).show();

                    showData();
                }

                if(tab.getPosition() == 2)
                {
                    pendingRequestTab = false;
                    acceptedTab = false;
                    ongoingTab = true;
                    rvToPay.setVisibility(View.INVISIBLE);
                    rvAccepted.setVisibility(View.INVISIBLE);
                    rvOngoing.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "ONGOING", Toast.LENGTH_SHORT).show();

                    showData();

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayoutToReceive.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)
                {
                    showData();
                }
                if(tab.getPosition() == 1)
                {
                    ArrayList<OrdersList> filterListRestock = new ArrayList<>();
                    for(OrdersList list : ordersListsToReceive)
                    {
                        if(list.getBookingOption().toLowerCase().contains("pickup"))
                        {
                            filterListRestock.add(list);
                        }
                    }
                    ToReceiveAdapter adapterRestock = new ToReceiveAdapter(getApplicationContext(),filterListRestock);
                    rvToReceive.setAdapter(adapterRestock);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        updateOrdersNotificationsCountOrderRequest();
        updateOrdersNotificationsCountAccepted();
        updateOrdersNotificationsCountOngoing();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

    }

    private void backPage() {
        Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void updateOrdersNotificationsCountOrderRequest() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counterNotif = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    NotificationPromoList notif = snapshot.getValue(NotificationPromoList.class);
                    if(notif.getIsSeen().equalsIgnoreCase("false") && notif.getNotifHeader().equalsIgnoreCase("Order Request"))
                    {
                        counterNotif++;
                    }


                    ordersnotifCounterOrderRequestB.setText(String.valueOf(counterNotif));
                    ordersnotifCardviewOrderRequestB.setVisibility(counterNotif == 0 ? View.INVISIBLE : View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateOrdersNotificationsCountAccepted() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counterNotif = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    NotificationPromoList notif = snapshot.getValue(NotificationPromoList.class);
                    if(notif.getIsSeen().equalsIgnoreCase("false") && notif.getNotifHeader().equalsIgnoreCase("Accepted"))
                    {
                        counterNotif++;
                    }


                    ordersnotifCounterAcceptedB.setText(String.valueOf(counterNotif));
                    ordersnotifCardviewAcceptedB.setVisibility(counterNotif == 0 ? View.INVISIBLE : View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateOrdersNotificationsCountOngoing() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counterNotif = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    NotificationPromoList notif = snapshot.getValue(NotificationPromoList.class);
                    if(notif.getIsSeen().equalsIgnoreCase("false") && notif.getNotifHeader().equalsIgnoreCase("Ongoing"))
                    {
                        counterNotif++;
                    }


                    ordersnotifCounterOngoingB.setText(String.valueOf(counterNotif));
                    ordersnotifCardviewOngoingB.setVisibility(counterNotif == 0 ? View.INVISIBLE : View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    public void showData() {
        String sellerEmail = mAuth.getCurrentUser().getEmail();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setTitle("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();


        ordersListsOrderRequest = new ArrayList<>();
        ordersListsAccepted = new ArrayList<>();
        ordersListsOngoing = new ArrayList<>();
        ordersListsToBook = new ArrayList<>();
        ordersListsToReceive = new ArrayList<>();
        ordersListsCompleted = new ArrayList<>();
        ordersListsCancelled = new ArrayList<>();
        ordersListsReturnOrRefund = new ArrayList<>();
        listorder = new ArrayList<>();
        indexorder = new ArrayList<>();



        referenceOrderRequest = FirebaseDatabase.getInstance().getReference("BuyerPurchase").child(user.getUid());

        referenceOrderRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersListsOrderRequest.clear();
                ordersListsAccepted.clear();
                ordersListsOngoing.clear();
                ordersListsToBook.clear();
                ordersListsToReceive.clear();
                ordersListsCompleted.clear();
                ordersListsCancelled.clear();
                ordersListsReturnOrRefund.clear();
                listorder.clear();
                indexorder.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy hh.mm aa");
                    String currentDateAndTime = dateFormat2.format(new Date()).toString();
                    String notifid = UUID.randomUUID().toString();
                    String finalid = notifid.substring(0,11);
                    OrdersList ordersList1 = snapshot.getValue(OrdersList.class);
                    if(ordersList1.getOrderStatus().equalsIgnoreCase("To Pay"))
                    {
                        ordersListsOrderRequest.add(ordersList1);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put("IsSeen","false");
                        map.put("NotifDateAndTime", currentDateAndTime);
                        map.put("NotifDescription", ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName());
                        map.put("NotifHeader", "Order Request");
                        map.put("NotifID",ordersList1.getOrderID());
                        map.put("NotifImage",ordersList1.getOrderProductImage());
                        reference.child("Notifications").child(user.getUid()).child(ordersList1.getOrderID()).setValue(map);
                        //sendNotification(user.getUid(),ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName(),ordersList1.getOrderStatus());
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("Accepted"))
                    {
                        ordersListsAccepted.add(ordersList1);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put("IsSeen","false");
                        map.put("NotifDateAndTime", currentDateAndTime);
                        map.put("NotifDescription", ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName());
                        map.put("NotifHeader", "Accepted");
                        map.put("NotifID",ordersList1.getOrderID());
                        map.put("NotifImage",ordersList1.getOrderProductImage());
                        reference.child("Notifications").child(user.getUid()).child(ordersList1.getOrderID()).setValue(map);
                        //sendNotification(user.getUid(),ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName(),ordersList1.getOrderStatus());

                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("Ongoing"))
                    {
                        ordersListsOngoing.add(ordersList1);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put("IsSeen","false");
                        map.put("NotifDateAndTime", currentDateAndTime);
                        map.put("NotifDescription", ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName());
                        map.put("NotifHeader", "Ongoing");
                        map.put("NotifID",ordersList1.getOrderID());
                        map.put("NotifImage",ordersList1.getOrderProductImage());
                        reference.child("Notifications").child(user.getUid()).child(ordersList1.getOrderID()).setValue(map);
                        //sendNotification(user.getUid(),ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName(),ordersList1.getOrderStatus());

                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("To Book"))
                    {
                        ordersListsToBook.add(ordersList1);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put("IsSeen","false");
                        map.put("NotifDateAndTime", currentDateAndTime);
                        map.put("NotifDescription", ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName());
                        map.put("NotifHeader", "To Book");
                        map.put("NotifID",ordersList1.getOrderID());
                        map.put("NotifImage",ordersList1.getOrderProductImage());
                        reference.child("Notifications").child(user.getUid()).child(ordersList1.getOrderID()).setValue(map);
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("To Receive"))
                    {
                        ordersListsToReceive.add(ordersList1);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put("IsSeen","false");
                        map.put("NotifDateAndTime", currentDateAndTime);
                        map.put("NotifDescription", ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName());
                        map.put("NotifHeader", "To Receive");
                        map.put("NotifID",ordersList1.getOrderID());
                        map.put("NotifImage",ordersList1.getOrderProductImage());
                        reference.child("Notifications").child(user.getUid()).child(ordersList1.getOrderID()).setValue(map);
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("Completed Order"))
                    {
                        ordersListsCompleted.add(ordersList1);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put("IsSeen","false");
                        map.put("NotifDateAndTime", currentDateAndTime);
                        map.put("NotifDescription", ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName());
                        map.put("NotifHeader", "Completed Order");
                        map.put("NotifID",ordersList1.getOrderID());
                        map.put("NotifImage",ordersList1.getOrderProductImage());
                        reference.child("Notifications").child(user.getUid()).child(ordersList1.getOrderID()).setValue(map);
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("Cancelled Order"))
                    {
                        ordersListsCancelled.add(ordersList1);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put("IsSeen","false");
                        map.put("NotifDateAndTime", currentDateAndTime);
                        map.put("NotifDescription", ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName());
                        map.put("NotifHeader", "Cancelled Order");
                        map.put("NotifID",ordersList1.getOrderID());
                        map.put("NotifImage",ordersList1.getOrderProductImage());
                        reference.child("Notifications").child(user.getUid()).child(ordersList1.getOrderID()).setValue(map);
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("Return/Refund Order"))
                    {
                        ordersListsReturnOrRefund.add(ordersList1);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put("IsSeen","false");
                        map.put("NotifDateAndTime", currentDateAndTime);
                        map.put("NotifDescription", ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName());
                        map.put("NotifHeader", "Return/Refund Order");
                        map.put("NotifID",ordersList1.getOrderID());
                        map.put("NotifImage",ordersList1.getOrderProductImage());
                        reference.child("Notifications").child(user.getUid()).child(ordersList1.getOrderID()).setValue(map);
                    }
                }
                Log.d("SELLERERROR", "error");

                ordersAdapterOrderRequest = new OrdersAdapter(getApplicationContext(), ordersListsOrderRequest);
                ordersAdapterAccepted = new OrdersAcceptedAdapter(getApplicationContext(), ordersListsAccepted);
                ordersAdapterOngoing = new OrdersOngoingAdapter(getApplicationContext(), ordersListsOngoing);
                ordersAdapterToBook = new ToBookAdapter(getApplicationContext(), ordersListsToBook);
                ordersAdapterToReceive = new ToReceiveAdapter(getApplicationContext(), ordersListsToReceive);
                ordersAdapterCompleted = new CompletedOrdersAdapter(getApplicationContext(), ordersListsCompleted);
                ordersAdapterCancelled = new CancelledOrdersAdapter(getApplicationContext(), ordersListsCancelled);
                ordersAdapterReturnOrRefund = new ReturnOrRefundOrdersAdapter(getApplicationContext(), ordersListsReturnOrRefund);


                rvToPay.setAdapter(ordersAdapterOrderRequest);
                rvAccepted.setAdapter(ordersAdapterAccepted);
                rvOngoing.setAdapter(ordersAdapterOngoing);
                rvToBook.setAdapter(ordersAdapterToBook);
                rvToReceive.setAdapter(ordersAdapterToReceive);
               /* rcyclerVieweCompleted.setAdapter(ordersAdapterCompleted);
                recyclerViewCancelled.setAdapter(ordersAdapterCancelled);
                recyclerViewReturnOrRefund.setAdapter(ordersAdapterReturnOrRefund);*/


                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendNotification(String receiver, String msg, String orderType) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(user.getUid(), R.drawable.diy, msg, orderType, receiver);
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200)
                                    {
                                        if(response.body().success != 1){
                                            Toast.makeText(getApplication(), "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        updateOrdersNotificationsCountOrderRequest();
        updateOrdersNotificationsCountAccepted();
        updateOrdersNotificationsCountOngoing();



    }

}