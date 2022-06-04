package com.example.diyhub.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyhub.Notifications.APIService;
import com.example.diyhub.Notifications.CLient;
import com.example.diyhub.Notifications.Data;
import com.example.diyhub.Notifications.MyResponse;
import com.example.diyhub.Notifications.NotificationPromoList;
import com.example.diyhub.Notifications.Sender;
import com.example.diyhub.Notifications.Token;
import com.example.diyhub.Notifications.UserNotif;
import com.example.diyhub.R;
import com.example.diyhub.RestockProductsAdapter;
import com.example.diyhub.RestockProductsList;
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


public class SellerOrdersFragment extends Fragment {


    public SellerOrdersFragment() {
        // Required empty public constructor
    }

    TextView all,res,hold;
    int count=0;
    String emailSeller;
    RecyclerView recyclerViewToPay;
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
    View view;
    RecyclerView recyclerViewToBook, recyclerViewToReceive;
    TabLayout tabLayout;
    TabLayout tabLayoutOngoing;
    TabLayout tabLayoutToReceive;
    RecyclerView recyclerOrdersAccepted,recyclerOredersOngoing;


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

    RecyclerView recyclerViewCompleted;
    RecyclerView recyclerViewCancelled;
    RecyclerView recyclerViewReturnOrRefund;


    String orderRequestImage="https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/johnfordtapales8%40gmail.com%2FOrderForm-Icon-removebg-preview.png?alt=media&token=a290336b-566b-4a74-bee0-b7ed261961d7";


    boolean orderRequestTab = false;
    boolean acceptedTab = false;
    boolean ongoingTab = false;

    CardView ordersnotifCardviewOrderRequest;
    TextView ordersnotifCounterOrderRequest;

    CardView ordersnotifCardviewAccepted;
    TextView ordersnotifCounterAccepted;

    CardView ordersnotifCardviewOngoing;
    TextView ordersnotifCounterOngoing;


    String[] notifidNotif;
    int indexcount = 0;

    List<String> listorder;
    List<Integer> indexorder;

    APIService apiService = apiService = CLient.getClient("https://fcm.googleapis.com/").create(APIService.class);

    int tabBelow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_seller_orders, container, false);


        tabLayout = view.findViewById(R.id.tabLayoutSellerOrdersPage);
        tabLayoutOngoing = view.findViewById(R.id.tabLayoutOngoingAcceptedOrderRequest);
        tabLayoutToReceive = view.findViewById(R.id.tabLayoutToReceive);


        recyclerViewToPay = view.findViewById(R.id.ordersRecyclerSeller);
        recyclerViewToPay.setHasFixedSize(true);
        recyclerViewToPay.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewToBook = view.findViewById(R.id.toBookRecyclerSeller);
        recyclerViewToBook.setHasFixedSize(true);
        recyclerViewToBook.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewToReceive = view.findViewById(R.id.toReceiveRecyclerSeller);
        recyclerViewToReceive.setHasFixedSize(true);
        recyclerViewToReceive.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerOrdersAccepted = view.findViewById(R.id.ordersAcceptedRecyclerSeller);
        recyclerOrdersAccepted.setHasFixedSize(true);
        recyclerOrdersAccepted.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerOredersOngoing = view.findViewById(R.id.ordersOngoingRecyclerSeller);
        recyclerOredersOngoing.setHasFixedSize(true);
        recyclerOredersOngoing.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewCompleted = view.findViewById(R.id.ordersCompletedRecyclerSeller);
        recyclerViewCompleted.setHasFixedSize(true);
        recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewCancelled = view.findViewById(R.id.ordersCancelledRecyclerSeller);
        recyclerViewCancelled.setHasFixedSize(true);
        recyclerViewCancelled.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewReturnOrRefund = view.findViewById(R.id.ordersReturnOrRefundRecyclerSeller);
        recyclerViewReturnOrRefund.setHasFixedSize(true);
        recyclerViewReturnOrRefund.setLayoutManager(new LinearLayoutManager(getContext()));

        ordersnotifCardviewOrderRequest = view.findViewById(R.id.ordersNotificationNumberContainerOrderRequest);
        ordersnotifCounterOrderRequest = view.findViewById(R.id.ordersNotifCounterOrderRequest);

        ordersnotifCardviewAccepted = view.findViewById(R.id.ordersNotificationNumberContainerAccepted);
        ordersnotifCounterAccepted = view.findViewById(R.id.ordersNotifCounterAccepted);

        ordersnotifCardviewOngoing= view.findViewById(R.id.ordersNotificationNumberContainerOngoing);
        ordersnotifCounterOngoing = view.findViewById(R.id.ordersNotifCounterOngoing);


        Bundle extra = this.getArguments();
        if(extra != null)
        {
            tabBelow = extra.getInt("TabBelow");
        }


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        /*
        DatabaseReference referenceHistory = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMapHistory = new HashMap<>();
        hashMapHistory.put("BookingAddress", "Cebu City");
        hashMapHistory.put("BuyerImage", "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/johnfordtapales8%40gmail.com%2FProfile-Picture%2FdTvGFHWaFKZ9DBqZmwyEsCl6NbU2.jpeg?alt=media&token=3e31b767-2d83-4d8b-ba1c-b33a8f0b2e36");
        hashMapHistory.put("BuyerName", "Sherwin Larona");
        hashMapHistory.put("ItemCode", "CK001");
        hashMapHistory.put("OrderDate", "05/02/2022");
        hashMapHistory.put("OrderID", "s2ssjwifk8f");
        hashMapHistory.put("OrderProductImage", "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F19%2F2018%2F04%2F04%2FMyRecipes_032718_1776-2000.jpg");
        hashMapHistory.put("OrderProductName", "Birthday Cake");
        hashMapHistory.put("OrderQuantity", "3");
        hashMapHistory.put("OrderType", "Custom");
        hashMapHistory.put("PaymentOption", "COD");
        hashMapHistory.put("PaymentStatus", "TO PAY");
        hashMapHistory.put("ShopName", "Chocolate Factory");
        hashMapHistory.put("OrderStatus", "Order Request");
        referenceHistory.child("Orders").child(user.getUid()).child("s2ssjwifk8f").updateChildren(hashMapHistory);

         */



        dbFirestore = FirebaseFirestore.getInstance();
        toPayLists = new ArrayList<>();


        progressDialog = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));
        recyclerViewToPay.addItemDecoration(dividerItemDecoration);
        recyclerViewToBook.addItemDecoration(dividerItemDecoration);
        recyclerViewToReceive.addItemDecoration(dividerItemDecoration);



        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
            showData();


        recyclerViewToPay.setVisibility(View.VISIBLE);
        recyclerViewToBook.setVisibility(View.INVISIBLE);
        recyclerViewToReceive.setVisibility(View.INVISIBLE);
        tabLayoutOngoing.setVisibility(View.VISIBLE);
        tabLayoutToReceive.setVisibility(View.INVISIBLE);
        ordersnotifCardviewOrderRequest.setVisibility(View.INVISIBLE);
        ordersnotifCardviewAccepted.setVisibility(View.INVISIBLE);
        ordersnotifCardviewOngoing.setVisibility(View.INVISIBLE);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == 0)
                {
                    recyclerViewToPay.setVisibility(View.VISIBLE);
                    recyclerViewToBook.setVisibility(View.INVISIBLE);
                    recyclerViewToReceive.setVisibility(View.INVISIBLE);
                    recyclerViewCompleted.setVisibility(View.INVISIBLE);
                    recyclerViewCancelled.setVisibility(View.INVISIBLE);
                    recyclerViewReturnOrRefund.setVisibility(View.INVISIBLE);
                    tabLayoutOngoing.setVisibility(View.VISIBLE);
                    tabLayoutToReceive.setVisibility(View.INVISIBLE);
                    updateOrdersNotificationsCountOrderRequest();
                    updateOrdersNotificationsCountAccepted();
                    updateOrdersNotificationsCountOngoing();



                    if(orderRequestTab == true)
                    {
                        recyclerOrdersAccepted.setVisibility(View.INVISIBLE);
                        recyclerViewToPay.setVisibility(View.VISIBLE);
                        recyclerOredersOngoing.setVisibility(View.INVISIBLE);

                    }

                    if(acceptedTab == true)
                    {
                        recyclerOrdersAccepted.setVisibility(View.VISIBLE);
                        recyclerViewToPay.setVisibility(View.INVISIBLE);
                        recyclerOredersOngoing.setVisibility(View.INVISIBLE);

                    }

                    if(ongoingTab == true)
                    {
                        recyclerOredersOngoing.setVisibility(View.VISIBLE);
                        recyclerViewToPay.setVisibility(View.INVISIBLE);
                        recyclerOrdersAccepted.setVisibility(View.INVISIBLE);

                    }

                }



                if(tab.getPosition() == 1)
                {
                    recyclerViewToPay.setVisibility(View.INVISIBLE);
                    recyclerViewToReceive.setVisibility(View.INVISIBLE);
                    recyclerViewToBook.setVisibility(View.VISIBLE);
                    recyclerViewCompleted.setVisibility(View.INVISIBLE);
                    recyclerViewCancelled.setVisibility(View.INVISIBLE);
                    recyclerViewReturnOrRefund.setVisibility(View.INVISIBLE);
                    tabLayoutOngoing.setVisibility(View.INVISIBLE);
                    tabLayoutToReceive.setVisibility(View.INVISIBLE);
                    ordersnotifCardviewOrderRequest.setVisibility(View.INVISIBLE);
                    ordersnotifCardviewAccepted.setVisibility(View.INVISIBLE);
                    ordersnotifCardviewOngoing.setVisibility(View.INVISIBLE);

                    if(acceptedTab == true)
                    {
                        recyclerOrdersAccepted.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOrderRequest.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewAccepted.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOngoing.setVisibility(View.INVISIBLE);
                    }

                    if(ongoingTab == true)
                    {
                        recyclerOredersOngoing.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOrderRequest.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewAccepted.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOngoing.setVisibility(View.INVISIBLE);
                    }

                    showData();


                }

                if(tab.getPosition() == 2)
                {
                    recyclerViewToPay.setVisibility(View.INVISIBLE);
                    recyclerViewToBook.setVisibility(View.INVISIBLE);
                    recyclerViewToReceive.setVisibility(View.VISIBLE);
                    recyclerViewCompleted.setVisibility(View.INVISIBLE);
                    recyclerViewCancelled.setVisibility(View.INVISIBLE);
                    recyclerViewReturnOrRefund.setVisibility(View.INVISIBLE);
                    tabLayoutOngoing.setVisibility(View.INVISIBLE);
                    tabLayoutToReceive.setVisibility(View.VISIBLE);
                    ordersnotifCardviewOrderRequest.setVisibility(View.INVISIBLE);
                    ordersnotifCardviewAccepted.setVisibility(View.INVISIBLE);
                    ordersnotifCardviewOngoing.setVisibility(View.INVISIBLE);

                    if(acceptedTab == true)
                    {
                        recyclerOrdersAccepted.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOrderRequest.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewAccepted.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOngoing.setVisibility(View.INVISIBLE);
                    }

                    if(ongoingTab == true)
                    {
                        recyclerOredersOngoing.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOrderRequest.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewAccepted.setVisibility(View.INVISIBLE);
                        ordersnotifCardviewOngoing.setVisibility(View.INVISIBLE);
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



        tabLayoutOngoing.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == 0)
                {
                    orderRequestTab = true;
                    acceptedTab = false;
                    ongoingTab = false;
                    recyclerViewToPay.setVisibility(View.VISIBLE);
                    recyclerOrdersAccepted.setVisibility(View.INVISIBLE);
                    recyclerOredersOngoing.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "ORDERS REQUEST", Toast.LENGTH_SHORT).show();

                }

                if(tab.getPosition() == 1)
                {
                    orderRequestTab = false;
                    acceptedTab = true;
                    ongoingTab = false;
                    recyclerViewToPay.setVisibility(View.INVISIBLE);
                    recyclerOrdersAccepted.setVisibility(View.VISIBLE);
                    recyclerOredersOngoing.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "ACCEPTED", Toast.LENGTH_SHORT).show();

                    showData();
                }

                if(tab.getPosition() == 2)
                {
                    orderRequestTab = false;
                    acceptedTab = false;
                    ongoingTab = true;
                    recyclerViewToPay.setVisibility(View.INVISIBLE);
                    recyclerOrdersAccepted.setVisibility(View.INVISIBLE);
                    recyclerOredersOngoing.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "ONGOING", Toast.LENGTH_SHORT).show();

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
                    ToReceiveAdapter adapterRestock = new ToReceiveAdapter(getContext(),filterListRestock);
                    recyclerViewToReceive.setAdapter(adapterRestock);
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



        return view;


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


                    ordersnotifCounterOrderRequest.setText(String.valueOf(counterNotif));
                    ordersnotifCardviewOrderRequest.setVisibility(counterNotif == 0 ? View.INVISIBLE : View.VISIBLE);
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


                    ordersnotifCounterAccepted.setText(String.valueOf(counterNotif));
                    ordersnotifCardviewAccepted.setVisibility(counterNotif == 0 ? View.INVISIBLE : View.VISIBLE);
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


                    ordersnotifCounterOngoing.setText(String.valueOf(counterNotif));
                    ordersnotifCardviewOngoing.setVisibility(counterNotif == 0 ? View.INVISIBLE : View.VISIBLE);
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



        referenceOrderRequest = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid());

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
                    if(ordersList1.getOrderStatus().equalsIgnoreCase("Order Request"))
                    {
                        ordersListsOrderRequest.add(ordersList1);
                        /*
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put("IsSeen","false");
                        map.put("NotifDateAndTime", currentDateAndTime);
                        map.put("NotifDescription", ordersList1.getBuyerName() + " made an Order of the Product "+ ordersList1.getOrderProductName());
                        map.put("NotifHeader", "Order Request");
                        map.put("NotifID",ordersList1.getOrderID());
                        map.put("NotifImage",ordersList1.getOrderProductImage());
                        reference.child("Notifications").child(user.getUid()).child(ordersList1.getOrderID()).setValue(map);

                         */
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

                ordersAdapterOrderRequest = new OrdersAdapter(getContext(), ordersListsOrderRequest);
                ordersAdapterAccepted = new OrdersAcceptedAdapter(getContext(), ordersListsAccepted);
                ordersAdapterOngoing = new OrdersOngoingAdapter(getContext(), ordersListsOngoing);
                ordersAdapterToBook = new ToBookAdapter(getContext(), ordersListsToBook);
                ordersAdapterToReceive = new ToReceiveAdapter(getContext(), ordersListsToReceive);
                ordersAdapterCompleted = new CompletedOrdersAdapter(getContext(), ordersListsCompleted);
                ordersAdapterCancelled = new CancelledOrdersAdapter(getContext(), ordersListsCancelled);
                ordersAdapterReturnOrRefund = new ReturnOrRefundOrdersAdapter(getContext(), ordersListsReturnOrRefund);


                recyclerViewToPay.setAdapter(ordersAdapterOrderRequest);
                recyclerOrdersAccepted.setAdapter(ordersAdapterAccepted);
                recyclerOredersOngoing.setAdapter(ordersAdapterOngoing);
                recyclerViewToBook.setAdapter(ordersAdapterToBook);
                recyclerViewToReceive.setAdapter(ordersAdapterToReceive);
                recyclerViewCompleted.setAdapter(ordersAdapterCompleted);
                recyclerViewCancelled.setAdapter(ordersAdapterCancelled);
                recyclerViewReturnOrRefund.setAdapter(ordersAdapterReturnOrRefund);


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
                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
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

        if(tabBelow == 1)
        {
            tabLayoutOngoing.getTabAt(1).select();
        }



    }

}
