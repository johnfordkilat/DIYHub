package com.example.diyhub.MESSAGES;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.diyhub.R;
import com.example.diyhub.RecyclerAdapterViewChat;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatPage extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<MessagesList> messagesLists;
    RecyclerAdapterViewChat recyclerAdapterViewChat;

    FirebaseFirestore dbFirestore;
    FirebaseAuth mAuth;
    String profileID;
    RecyclerView recyclerView;
    String userType;
    ArrayList<User> mUsers;

    ImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);


        tabLayout = findViewById(R.id.tabLayoutChatPage);
        viewPager = findViewById(R.id.viewPageChatPage);


        profile_image = findViewById(R.id.profileImageChatPage);
        username = findViewById(R.id.usernameChatPage);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());

                if(user.getImageUrl().equals("default"))
                {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        viewPageAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPageAdapter.addFragment(new UsersFragment(), "Users");
        viewPageAdapter.addFragment(new BookmarkFragment(), "Bookmark");

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);





    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    class ViewPageAdapter extends FragmentPagerAdapter{


        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPageAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title)
        {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


}