package com.example.diyhub.PastTransaction;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.diyhub.Fragments.OrdersAdapter;
import com.example.diyhub.Fragments.OrdersList;
import com.example.diyhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
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
    RecyclerView recyclerViewCompleted;
    ArrayList<CompletedOrderList> completedOrderLists;
    CompletedOrderRecyclerAdapter completedOrderRecyclerAdapter;

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
    RecyclerView recyclerViewCancelled;
    RecyclerView recyclerViewReturnRefund;

    private int[] tabIcons = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_products1,
            R.drawable.ic_orders1,
    };

    Button datePickerButton;
    private DatePickerDialog datePickerDialog;

    ArrayList<CompletedOrderList> filterList = new ArrayList<>();




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_transaction_history);

        searchTxt = findViewById(R.id.searchHistoryTxt);
        tabLayout = findViewById(R.id.tabLayoutTransactionHistory);
        viewPager = findViewById(R.id.viewPagePastTransac);
        datePickerButton = findViewById(R.id.datePickerButtonTransactionHistory);




        progressDialog = new ProgressDialog(this);

        recyclerViewCompleted = findViewById(R.id.completedOrderRecyclerPastTransac);
        recyclerViewCompleted.setHasFixedSize(true);
        recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewCancelled = findViewById(R.id.cancelledOrderRecyclerPastTransac);
        recyclerViewCancelled.setHasFixedSize(true);
        recyclerViewCancelled.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewReturnRefund = findViewById(R.id.returnRefundOrderRecyclerPastTransac);
        recyclerViewReturnRefund.setHasFixedSize(true);
        recyclerViewReturnRefund.setLayoutManager(new LinearLayoutManager(this));

        dbFirestore = FirebaseFirestore.getInstance();
        completedOrderLists = new ArrayList<CompletedOrderList>();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
        {
            showData();
        }



        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0)
                {
                    recyclerViewCompleted.setVisibility(View.VISIBLE);
                    recyclerViewCancelled.setVisibility(View.INVISIBLE);
                    recyclerViewReturnRefund.setVisibility(View.INVISIBLE);
                }

                if(tab.getPosition() == 1)
                {
                    recyclerViewCompleted.setVisibility(View.INVISIBLE);
                    recyclerViewReturnRefund.setVisibility(View.INVISIBLE);
                    recyclerViewCancelled.setVisibility(View.VISIBLE);
                }

                if(tab.getPosition() == 2)
                {
                    recyclerViewCompleted.setVisibility(View.INVISIBLE);
                    recyclerViewCancelled.setVisibility(View.INVISIBLE);
                    recyclerViewReturnRefund.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });






        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filterList.clear();

                if(s.toString().isEmpty())
                {
                    recyclerViewCompleted.setAdapter(new CompletedOrderRecyclerAdapter(getApplicationContext(), completedOrderLists));
                    completedOrderRecyclerAdapter.notifyDataSetChanged();

                    recyclerViewCancelled.setAdapter(new CompletedOrderRecyclerAdapter(getApplicationContext(), completedOrderLists));
                    completedOrderRecyclerAdapter.notifyDataSetChanged();

                    recyclerViewReturnRefund.setAdapter(new CompletedOrderRecyclerAdapter(getApplicationContext(), completedOrderLists));
                    completedOrderRecyclerAdapter.notifyDataSetChanged();
                }
                else
                {
                    Filter(s.toString());
                }
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

    private void Filter(String text) {

        for(CompletedOrderList post: completedOrderLists)
        {
            post.toString().toLowerCase();
            if(post.getDate().toLowerCase().equals(text))
            {
                filterList.add(post);
            }
            if(post.getProductName().toLowerCase().equals(text))
            {
                filterList.add(post);
            }
            if(post.getShopName().toLowerCase().equals(text))
            {
                filterList.add(post);
            }
        }
        recyclerViewCompleted.setAdapter(new CompletedOrderRecyclerAdapter(getApplicationContext(), filterList));
        completedOrderRecyclerAdapter.notifyDataSetChanged();

        recyclerViewCancelled.setAdapter(new CompletedOrderRecyclerAdapter(getApplicationContext(), filterList));
        completedOrderRecyclerAdapter.notifyDataSetChanged();

        recyclerViewReturnRefund.setAdapter(new CompletedOrderRecyclerAdapter(getApplicationContext(), filterList));
        completedOrderRecyclerAdapter.notifyDataSetChanged();
    }


    public void showData()
    {
        String sellerEmail = mAuth.getCurrentUser().getEmail();
        progressDialog.setTitle("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        dbFirestore.collection("USERPROFILE").document(sellerEmail).collection("PASTTRANSACTIONHISTORY")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        completedOrderLists.clear();
                        for(DocumentSnapshot doc : task.getResult())
                        {
                            CompletedOrderList list = new CompletedOrderList(doc.getString("TransactionDate"),
                                    doc.getString("TransProductName"),
                                    doc.getString("TransShopName"),
                                    doc.getString("TransImage"));

                            completedOrderLists.add(list);
                        }
                        completedOrderRecyclerAdapter = new CompletedOrderRecyclerAdapter(PastTransactionHistory.this,completedOrderLists);
                        recyclerViewCompleted.setAdapter(completedOrderRecyclerAdapter);
                        recyclerViewCancelled.setAdapter(completedOrderRecyclerAdapter);
                        recyclerViewReturnRefund.setAdapter(completedOrderRecyclerAdapter);
                        recyclerViewCancelled.setVisibility(View.INVISIBLE);
                        recyclerViewReturnRefund.setVisibility(View.INVISIBLE);

                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        String date = formatter. format(Date. parse(currentDateString));
        searchTxt.setText(date);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}