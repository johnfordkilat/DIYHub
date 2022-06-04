package com.example.diyhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowingActivity extends AppCompatActivity {

    ArrayList<FollowingList> listFollowing;
    FollowingAdapter followingAdapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        recyclerView = findViewById(R.id.followingRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        listFollowing = new ArrayList<>();

        showDataFollowing();
    }

    private void showDataFollowing(){
        listFollowing = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Following").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listFollowing.clear();
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        FollowingList cartPageList = snapshot.getValue(FollowingList.class);
                        listFollowing.add(cartPageList);
                    }
                    followingAdapter = new FollowingAdapter(getApplicationContext(),listFollowing);
                    recyclerView.setAdapter(followingAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}