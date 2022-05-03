package com.example.diyhub.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.diyhub.AllProductsAdapter;
import com.example.diyhub.AllProductsList;
import com.example.diyhub.R;
import com.example.diyhub.RestockProductsAdapter;
import com.example.diyhub.RestockProductsList;
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

import java.util.ArrayList;
import java.util.Locale;


public class SellerProductsFragment extends Fragment {


    TextView all,res,hold;
    int count=0;
    String emailSeller;
    RecyclerView allProductsRecyclerView;
    FirebaseFirestore dbFirestore;
    AllProductsAdapter allProductsAdapter;
    ArrayList<AllProductsList> allProductsLists;
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
    int pQuan,pStocks;
    RecyclerView restockProductRecycler;
    RestockProductsAdapter restockProductsAdapter;
    ArrayList<RestockProductsList> restockProductsList;

    View view;

    TabLayout tabLayout;
    ViewPager viewPager;

    Button goToOrderPage;
    EditText searchProduct;

    public SellerProductsFragment() {
        // Required empty public constructor
    }
    ArrayList<AllProductsList> filterListAll = new ArrayList<>();
    ArrayList<RestockProductsList> filterListRestock = new ArrayList<>();

    SearchView searchView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_seller_products, container, false);


        uploadProduct = view.findViewById(R.id.uploadProductButtonSeller2);
        progressDialog = new ProgressDialog(getContext());
        tabLayout = view.findViewById(R.id.tabLayoutSellerOrdersPageFragment);
        viewPager = view.findViewById(R.id.viewPageSellerOrdersPageFragment);
        searchView = view.findViewById(R.id.searchProductTxt);


        Bundle extra = this.getArguments();
        if(extra != null)
        {
            emailSeller = extra.getString("emailSeller");
        }

        restockProductRecycler = view.findViewById(R.id.restockProductsSellerRecyclerFragment);
        restockProductRecycler.setHasFixedSize(true);
        restockProductRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        allProductsRecyclerView = view.findViewById(R.id.allProductsSellerRecyclerFragment);
        allProductsRecyclerView.setHasFixedSize(true);
        allProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbFirestore = FirebaseFirestore.getInstance();
        restockProductsList = new ArrayList<RestockProductsList>();
        allProductsLists = new ArrayList<AllProductsList>();




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
            showData();


        uploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), AddProduct.class);
                startActivity(intent);

            }
        });



        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));
        allProductsRecyclerView.addItemDecoration(dividerItemDecoration);
        restockProductRecycler.addItemDecoration(dividerItemDecoration);



        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0)
                {
                    allProductsRecyclerView.setVisibility(View.VISIBLE);
                    restockProductRecycler.setVisibility(View.INVISIBLE);
                    uploadProduct.setVisibility(View.VISIBLE);
                }

                if(tab.getPosition() == 1)
                {
                    allProductsRecyclerView.setVisibility(View.INVISIBLE);
                    restockProductRecycler.setVisibility(View.VISIBLE);
                    uploadProduct.setVisibility(View.INVISIBLE);
                }

                if(tab.getPosition() == 2)
                {
                    allProductsRecyclerView.setVisibility(View.INVISIBLE);
                    restockProductRecycler.setVisibility(View.INVISIBLE);
                    uploadProduct.setVisibility(View.INVISIBLE);
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTOGOV && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri1 = data.getData();
            ImageList.add(imageUri1);
        }
    }

    public void showData()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setTitle("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allProductsLists.clear();
                restockProductsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    AllProductsList allList = snapshot.getValue(AllProductsList.class);
                    String pQuan = allList.getProductQuantity();
                    String pStocks = allList.getProductStocks();
                    allProductsLists.add(allList);
                    int quantity = Integer.parseInt(pQuan);
                    int stocks = Integer.parseInt(pStocks);
                    if(quantity >= stocks)
                    {
                        RestockProductsList restockList = snapshot.getValue(RestockProductsList.class);
                        restockProductsList.add(restockList);
                    }

                }
                allProductsAdapter = new AllProductsAdapter(getContext(),allProductsLists);
                restockProductsAdapter = new RestockProductsAdapter(getContext(), restockProductsList);

                allProductsRecyclerView.setAdapter(allProductsAdapter);
                restockProductRecycler.setAdapter(restockProductsAdapter);
                restockProductRecycler.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
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
        ArrayList<AllProductsList> filterList = new ArrayList<>();
        for(AllProductsList list : allProductsLists)
        {
            if(list.getProductName().toLowerCase().contains(text.toLowerCase()))
            {
                filterList.add(list);
            }
        }
        AllProductsAdapter adapter = new AllProductsAdapter(getContext(),filterList);
        allProductsRecyclerView.setAdapter(adapter);

        ArrayList<RestockProductsList> filterListRestock = new ArrayList<>();
        for(RestockProductsList list : restockProductsList)
        {
            if(list.getProductName().toLowerCase().contains(text.toLowerCase()))
            {
                filterListRestock.add(list);
            }
        }
        RestockProductsAdapter adapterRestock = new RestockProductsAdapter(getContext(),filterListRestock);
        restockProductRecycler.setAdapter(adapterRestock);

    }
}