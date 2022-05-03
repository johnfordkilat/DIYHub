package com.example.diyhub.Fragments;

import android.app.ProgressDialog;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyhub.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


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
    TabLayout tabLayoutToBook;
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




    boolean orderRequestTab = false;
    boolean acceptedTab = false;
    boolean ongoingTab = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_seller_orders, container, false);


        tabLayout = view.findViewById(R.id.tabLayoutSellerOrdersPage);
        tabLayoutOngoing = view.findViewById(R.id.tabLayoutOngoingAcceptedOrderRequest);
        tabLayoutToBook = view.findViewById(R.id.tabLayoutToBook);
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







        dbFirestore = FirebaseFirestore.getInstance();
        toPayLists = new ArrayList<>();


        progressDialog = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));
        recyclerViewToPay.addItemDecoration(dividerItemDecoration);
        recyclerViewToBook.addItemDecoration(dividerItemDecoration);
        recyclerViewToReceive.addItemDecoration(dividerItemDecoration);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
            showData();


        recyclerViewToPay.setVisibility(View.VISIBLE);
        recyclerViewToBook.setVisibility(View.INVISIBLE);
        recyclerViewToReceive.setVisibility(View.INVISIBLE);
        tabLayoutOngoing.setVisibility(View.VISIBLE);
        tabLayoutToBook.setVisibility(View.INVISIBLE);
        tabLayoutToReceive.setVisibility(View.INVISIBLE);

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
                    tabLayoutToBook.setVisibility(View.INVISIBLE);
                    tabLayoutToReceive.setVisibility(View.INVISIBLE);

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
                    tabLayoutToBook.setVisibility(View.VISIBLE);
                    tabLayoutToReceive.setVisibility(View.INVISIBLE);

                    if(acceptedTab == true)
                    {
                        recyclerOrdersAccepted.setVisibility(View.INVISIBLE);
                    }

                    if(ongoingTab == true)
                    {
                        recyclerOredersOngoing.setVisibility(View.INVISIBLE);
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
                    tabLayoutToBook.setVisibility(View.INVISIBLE);
                    tabLayoutToReceive.setVisibility(View.VISIBLE);

                    if(acceptedTab == true)
                    {
                        recyclerOrdersAccepted.setVisibility(View.INVISIBLE);
                    }

                    if(ongoingTab == true)
                    {
                        recyclerOredersOngoing.setVisibility(View.INVISIBLE);
                    }

                    showData();


                }

                if(tab.getPosition() == 3)
                {
                    recyclerViewToPay.setVisibility(View.INVISIBLE);
                    recyclerViewToBook.setVisibility(View.INVISIBLE);
                    recyclerViewToReceive.setVisibility(View.INVISIBLE);
                    recyclerViewCompleted.setVisibility(View.VISIBLE);
                    recyclerViewCancelled.setVisibility(View.INVISIBLE);
                    recyclerViewReturnOrRefund.setVisibility(View.INVISIBLE);
                    tabLayoutOngoing.setVisibility(View.INVISIBLE);
                    tabLayoutToBook.setVisibility(View.INVISIBLE);
                    tabLayoutToReceive.setVisibility(View.VISIBLE);

                    if(acceptedTab == true)
                    {
                        recyclerOrdersAccepted.setVisibility(View.INVISIBLE);
                    }

                    if(ongoingTab == true)
                    {
                        recyclerOredersOngoing.setVisibility(View.INVISIBLE);
                    }

                    showData();


                }

                if(tab.getPosition() == 4)
                {
                    recyclerViewToPay.setVisibility(View.INVISIBLE);
                    recyclerViewToBook.setVisibility(View.INVISIBLE);
                    recyclerViewToReceive.setVisibility(View.INVISIBLE);
                    recyclerViewCompleted.setVisibility(View.INVISIBLE);
                    recyclerViewCancelled.setVisibility(View.VISIBLE);
                    recyclerViewReturnOrRefund.setVisibility(View.INVISIBLE);
                    tabLayoutOngoing.setVisibility(View.INVISIBLE);
                    tabLayoutToBook.setVisibility(View.INVISIBLE);
                    tabLayoutToReceive.setVisibility(View.VISIBLE);

                    if(acceptedTab == true)
                    {
                        recyclerOrdersAccepted.setVisibility(View.INVISIBLE);
                    }

                    if(ongoingTab == true)
                    {
                        recyclerOredersOngoing.setVisibility(View.INVISIBLE);
                    }

                    showData();


                }

                if(tab.getPosition() == 5)
                {
                    recyclerViewToPay.setVisibility(View.INVISIBLE);
                    recyclerViewToBook.setVisibility(View.INVISIBLE);
                    recyclerViewToReceive.setVisibility(View.INVISIBLE);
                    recyclerViewCompleted.setVisibility(View.INVISIBLE);
                    recyclerViewCancelled.setVisibility(View.INVISIBLE);
                    recyclerViewReturnOrRefund.setVisibility(View.VISIBLE);
                    tabLayoutOngoing.setVisibility(View.INVISIBLE);
                    tabLayoutToBook.setVisibility(View.INVISIBLE);
                    tabLayoutToReceive.setVisibility(View.VISIBLE);

                    if(acceptedTab == true)
                    {
                        recyclerOrdersAccepted.setVisibility(View.INVISIBLE);
                    }

                    if(ongoingTab == true)
                    {
                        recyclerOredersOngoing.setVisibility(View.INVISIBLE);
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

        return view;
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


                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    OrdersList ordersList1 = snapshot.getValue(OrdersList.class);
                    if(ordersList1.getOrderStatus().equalsIgnoreCase("Order Request"))
                    {
                        ordersListsOrderRequest.add(ordersList1);
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("Accepted"))
                    {
                        ordersListsAccepted.add(ordersList1);
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("Ongoing"))
                    {
                        ordersListsOngoing.add(ordersList1);
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("To Book"))
                    {
                        ordersListsToBook.add(ordersList1);
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("To Receive"))
                    {
                        ordersListsToReceive.add(ordersList1);
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("Completed Order"))
                    {
                        ordersListsCompleted.add(ordersList1);
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("Cancelled Order"))
                    {
                        ordersListsCancelled.add(ordersList1);
                    }
                    else if(ordersList1.getOrderStatus().equalsIgnoreCase("Return/Refund Order"))
                    {
                        ordersListsReturnOrRefund.add(ordersList1);
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
}
