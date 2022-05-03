package com.example.diyhub.Notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.diyhub.MyAdapter;
import com.example.diyhub.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PromoFragment extends Fragment {

    RecyclerView recyclerView;
    ImageView notif;

    List<PromoProducts> mProducts;
    DatabaseReference reference;

    TabLayout tabLayout;
    ViewPager viewPager;

    MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promo, container, false);

        recyclerView = view.findViewById(R.id.recyclerPromos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mProducts = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProducts.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    PromoProducts chatList = snapshot.getValue(PromoProducts.class);
                    mProducts.add(chatList);
                }
                Log.d("SELLERERROR", "error");

                myAdapter = new MyAdapter(getContext(), mProducts);
                recyclerView.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}