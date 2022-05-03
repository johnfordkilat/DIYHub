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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_seller_products, container, false);


        uploadProduct = view.findViewById(R.id.uploadProductButtonSeller2);
        progressDialog = new ProgressDialog(getContext());
        tabLayout = view.findViewById(R.id.tabLayoutSellerOrdersPageFragment);
        viewPager = view.findViewById(R.id.viewPageSellerOrdersPageFragment);
        searchProduct = view.findViewById(R.id.searchProductTxt);


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

        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filterListAll.clear();
                filterListRestock.clear();


                if(s.toString().isEmpty())
                {
                    allProductsRecyclerView.setAdapter(new AllProductsAdapter(getContext(), allProductsLists));
                    allProductsAdapter = new AllProductsAdapter(getContext(), allProductsLists);
                    allProductsAdapter.notifyDataSetChanged();


                    restockProductRecycler.setAdapter(new RestockProductsAdapter(getContext(), restockProductsList));
                    restockProductsAdapter = new RestockProductsAdapter(getContext(), restockProductsList);
                    restockProductsAdapter.notifyDataSetChanged();
                }
                else
                {
                    Filter(s.toString());
                }
            }
        });



        return view;

    }

    private void Filter(String text) {

        for(AllProductsList post: allProductsLists)
        {
            post.toString().toLowerCase();
            if(post.getProductName().toLowerCase().equals(text))
            {
                filterListAll.add(post);
            }

        }
        for(RestockProductsList post: restockProductsList)
        {
            post.toString().toLowerCase();
            if(post.getProductName().toLowerCase().equals(text))
            {
                filterListRestock.add(post);
            }

        }
        allProductsRecyclerView.setAdapter(new AllProductsAdapter(getContext(), filterListAll));
        allProductsAdapter.notifyDataSetChanged();

        restockProductRecycler.setAdapter(new RestockProductsAdapter(getContext(), filterListRestock));
        restockProductsAdapter.notifyDataSetChanged();

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


        dbFirestore.collection("USERPROFILE").document(firebaseUser.getEmail()).collection("SELLERPRODUCTS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        allProductsLists.clear();

                        for(DocumentSnapshot doc : task.getResult())
                        {
                            pQuan = Integer.parseInt(doc.getString("ProductQuantity"));
                            pStocks = Integer.parseInt(doc.getString("ProductStocks"));

                            if(pQuan >= pStocks)
                            {
                                RestockProductsList restockList = new RestockProductsList(doc.getString("ProductName"),
                                        doc.getString("ProductQuantity"),
                                        doc.getString("ProductStocks"),
                                        doc.getString("ProductImage"),
                                        doc.getString("ProductID"),
                                        doc.getString("ProductStatusImage"),
                                        doc.getString("ProductStatus"));
                                restockProductsList.add(restockList);
                            }
                            AllProductsList list = new AllProductsList(doc.getString("ProductName"),
                                    doc.getString("ProductQuantity"),
                                    doc.getString("ProductStocks"),
                                    doc.getString("ProductImage"),
                                    doc.getString("ProductID"),
                                    doc.getString("ProductStatusImage"),
                                    doc.getString("ProductStatus"));
                            allProductsLists.add(list);

                        }
                        allProductsAdapter = new AllProductsAdapter(getContext(),allProductsLists);
                        restockProductsAdapter = new RestockProductsAdapter(getContext(), restockProductsList);

                        allProductsRecyclerView.setAdapter(allProductsAdapter);
                        restockProductRecycler.setAdapter(restockProductsAdapter);
                        restockProductRecycler.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}