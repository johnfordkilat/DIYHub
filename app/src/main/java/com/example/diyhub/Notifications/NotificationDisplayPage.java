package com.example.diyhub.Notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.diyhub.MESSAGES.Chat;
import com.example.diyhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NotificationDisplayPage extends AppCompatActivity {

    ValueEventListener seenListener;
    DatabaseReference reference;
    String notifid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_display_page);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            notifid = extras.getString("notifid");
        }

    }

    private void seenMessage(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Notifications").child(user.getUid());
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    NotificationPromoList list = snapshot.getValue(NotificationPromoList.class);
                    if(list.getIsSeen().equalsIgnoreCase("false"))
                    {
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put("IsSeen", "true");
                        reference1.child("Notifications").child(user.getUid()).child(notifid).updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        seenMessage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        reference.removeEventListener(seenListener);

    }
}