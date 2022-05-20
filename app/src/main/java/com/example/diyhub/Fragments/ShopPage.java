package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.example.diyhub.AllProductsAdapter;
import com.example.diyhub.AllProductsBuyerAdapter;
import com.example.diyhub.AllProductsList;
import com.example.diyhub.HoldProductsAdapter;
import com.example.diyhub.HoldProductsList;
import com.example.diyhub.R;
import com.example.diyhub.RestockProductsAdapter;
import com.example.diyhub.RestockProductsList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopPage extends AppCompatActivity {

    AllProductsBuyerAdapter allProductsAdapter;
    RecyclerView allProductsRecyclerView;
    ArrayList<AllProductsList> allProductsLists;
    ProgressDialog progressDialog;
    String sellerID;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);

        allProductsRecyclerView = findViewById(R.id.sellerProductsRecycler);
        allProductsRecyclerView.setHasFixedSize(true);
        allProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchView = findViewById(R.id.searchMessagesBuyer);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            sellerID = extras.getString("SellerID");
        }

        progressDialog = new ProgressDialog(ShopPage.this);


        allProductsLists = new ArrayList<>();

        showData();
    }

    public void showData()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setTitle("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allProductsLists.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    AllProductsList allList = snapshot.getValue(AllProductsList.class);
                    allProductsLists.add(allList);

                }
                allProductsAdapter = new AllProductsBuyerAdapter(getApplicationContext(),allProductsLists);
                allProductsRecyclerView.setAdapter(allProductsAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    protected void onStart() {
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
        AllProductsAdapter adapter = new AllProductsAdapter(getApplicationContext(),filterList);
        allProductsRecyclerView.setAdapter(adapter);


    }
}