package com.example.diyhub.PastTransaction;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.diyhub.Fragments.CancelledOrdersAdapter;
import com.example.diyhub.Fragments.CompletedOrdersAdapter;
import com.example.diyhub.Fragments.OrdersAcceptedAdapter;
import com.example.diyhub.Fragments.OrdersAdapter;
import com.example.diyhub.Fragments.OrdersList;
import com.example.diyhub.Fragments.OrdersOngoingAdapter;
import com.example.diyhub.Fragments.ReturnOrRefundOrdersAdapter;
import com.example.diyhub.Fragments.ToBookAdapter;
import com.example.diyhub.Fragments.ToReceiveAdapter;
import com.example.diyhub.MESSAGES.User;
import com.example.diyhub.MESSAGES.UserAdapter;
import com.example.diyhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PastTransactionHistory extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Button searchButton;
    EditText searchTxt;
    String word;
    TabLayout tabLayout;
    ViewPager viewPager;

    View view;


    TextView all,res,hold;
    int count=0;
    String emailSeller;
    FirebaseFirestore dbFirestore;
    OrdersAdapter ordersAdapter;
    ArrayList<OrdersList> ordersLists;
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

    private int[] tabIcons = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_products1,
            R.drawable.ic_orders1,
    };

    Button datePickerButton;
    private DatePickerDialog datePickerDialog;

    DatabaseReference referenceOrderRequest;
    DatabaseReference referenceAccepted;
    DatabaseReference referenceOngoing;
    DatabaseReference referenceToBook;
    DatabaseReference referenceToReceive;


    ArrayList<OrdersList> ordersListsCompleted;
    ArrayList<OrdersList> ordersListsCancelled;
    ArrayList<OrdersList> ordersListsReturnOrRefund;



    CompletedOrderRecyclerAdapter ordersAdapterCompleted;
    CancelledOrdersRecyclerAdapter ordersAdapterCancelled;
    ReturnRefundOrdersRecyclerAdapter ordersAdapterReturnOrRefund;

    RecyclerView recyclerViewCompleted;
    RecyclerView recyclerViewCancelled;
    RecyclerView recyclerViewReturnOrRefund;

    SearchView searchView;

    int tabSelect;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_transaction_history);

        tabLayout = findViewById(R.id.tabLayoutTransactionHistory);
        viewPager = findViewById(R.id.viewPagePastTransac);
        datePickerButton = findViewById(R.id.datePickerButtonTransactionHistory);
        searchView = findViewById(R.id.searchHistoryTxt);




        progressDialog = new ProgressDialog(this);

        recyclerViewCompleted = findViewById(R.id.completedOrderRecyclerPastTransac);
        recyclerViewCompleted.setHasFixedSize(true);
        recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewCancelled = findViewById(R.id.cancelledOrderRecyclerPastTransac);
        recyclerViewCancelled.setHasFixedSize(true);
        recyclerViewCancelled.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewReturnOrRefund = findViewById(R.id.returnRefundOrderRecyclerPastTransac);
        recyclerViewReturnOrRefund.setHasFixedSize(true);
        recyclerViewReturnOrRefund.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));
        recyclerViewCompleted.addItemDecoration(dividerItemDecoration);
        recyclerViewCancelled.addItemDecoration(dividerItemDecoration);
        recyclerViewReturnOrRefund.addItemDecoration(dividerItemDecoration);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
        {
            showData();
        }

        recyclerViewCompleted.setVisibility(View.VISIBLE);
        recyclerViewCancelled.setVisibility(View.INVISIBLE);
        recyclerViewReturnOrRefund.setVisibility(View.INVISIBLE);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0)
                {
                    tabSelect = 0;
                    recyclerViewCompleted.setVisibility(View.VISIBLE);
                    recyclerViewCancelled.setVisibility(View.INVISIBLE);
                    recyclerViewReturnOrRefund.setVisibility(View.INVISIBLE);
                    showData();
                }

                if(tab.getPosition() == 1)
                {
                    tabSelect = 1;
                    recyclerViewCompleted.setVisibility(View.INVISIBLE);
                    recyclerViewReturnOrRefund.setVisibility(View.INVISIBLE);
                    recyclerViewCancelled.setVisibility(View.VISIBLE);
                    showData();
                }

                if(tab.getPosition() == 2)
                {
                    tabSelect = 2;
                    recyclerViewCompleted.setVisibility(View.INVISIBLE);
                    recyclerViewCancelled.setVisibility(View.INVISIBLE);
                    recyclerViewReturnOrRefund.setVisibility(View.VISIBLE);
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



        setupTabIcons();

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


    }


    public void showData() {
        String sellerEmail = mAuth.getCurrentUser().getEmail();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setTitle("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();


        ordersListsCompleted = new ArrayList<>();
        ordersListsCancelled = new ArrayList<>();
        ordersListsReturnOrRefund = new ArrayList<>();



        referenceOrderRequest = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid());

        referenceOrderRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersListsCompleted.clear();
                ordersListsCancelled.clear();
                ordersListsReturnOrRefund.clear();


                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    OrdersList ordersList1 = snapshot.getValue(OrdersList.class);

                    if(ordersList1.getOrderStatus().equalsIgnoreCase("Completed Order"))
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

                ordersAdapterCompleted = new CompletedOrderRecyclerAdapter(getApplicationContext(), ordersListsCompleted);
                ordersAdapterCancelled = new CancelledOrdersRecyclerAdapter(getApplicationContext(), ordersListsCancelled);
                ordersAdapterReturnOrRefund = new ReturnRefundOrdersRecyclerAdapter(getApplicationContext(), ordersListsReturnOrRefund);


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

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String date = formatter. format(Date.parse(currentDateString));
        searchView.setQuery(date,false);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void onStart() {
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

        //Search for completed orders
        ArrayList<OrdersList> filterList = new ArrayList<>();
        for(OrdersList post: ordersListsCompleted)
        {
            post.toString().toLowerCase();
            if(post.getOrderDate().toLowerCase().contains(text.toLowerCase()) ||
                    post.getOrderProductName().toLowerCase().contains(text.toLowerCase()) ||
                    post.getShopName().toLowerCase().contains(text.toLowerCase()) ||
                    post.getOrderID().toLowerCase().contains(text.toLowerCase()))
            {
                filterList.add(post);
            }
        }
        CompletedOrderRecyclerAdapter adapter = new CompletedOrderRecyclerAdapter(getApplicationContext(),filterList);
        recyclerViewCompleted.setAdapter(adapter);

        //Search for cancelled orders
        ArrayList<OrdersList> filterListCancelled = new ArrayList<>();
        for(OrdersList post: ordersListsCancelled)
        {
            post.toString().toLowerCase();
            if(post.getOrderDate().toLowerCase().contains(text.toLowerCase()) ||
                    post.getOrderProductName().toLowerCase().contains(text.toLowerCase()) ||
                    post.getShopName().toLowerCase().contains(text.toLowerCase()) ||
                    post.getOrderID().toLowerCase().contains(text.toLowerCase()))
            {
                filterListCancelled.add(post);
            }
        }
        CancelledOrdersRecyclerAdapter adapterCancelled = new CancelledOrdersRecyclerAdapter(getApplicationContext(),filterListCancelled);
        recyclerViewCancelled.setAdapter(adapterCancelled);

        //Search for return or refund orders
        ArrayList<OrdersList> filterListReturnRefund = new ArrayList<>();
        for(OrdersList post: ordersListsReturnOrRefund)
        {
            post.toString().toLowerCase();
            if(post.getOrderDate().toLowerCase().contains(text.toLowerCase()) ||
                    post.getOrderProductName().toLowerCase().contains(text.toLowerCase()) ||
                    post.getShopName().toLowerCase().contains(text.toLowerCase()) ||
                    post.getOrderID().toLowerCase().contains(text.toLowerCase()))
            {
                filterListReturnRefund.add(post);
            }
        }
        ReturnRefundOrdersRecyclerAdapter adapterReturnRefund = new ReturnRefundOrdersRecyclerAdapter(getApplicationContext(),filterListReturnRefund);
        recyclerViewReturnOrRefund.setAdapter(adapterReturnRefund);



    }

}