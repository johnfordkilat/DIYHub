package com.example.diyhub.MESSAGES;

import static com.example.diyhub.MESSAGES.App.CHANNEL_1_ID;
import static com.example.diyhub.MESSAGES.App.CHANNEL_2_ID;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.Notifications.APIService;
import com.example.diyhub.Notifications.CLient;
import com.example.diyhub.Notifications.Data;
import com.example.diyhub.Notifications.MyResponse;
import com.example.diyhub.Notifications.Sender;
import com.example.diyhub.Notifications.Token;
import com.example.diyhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView username;

    Intent intent;
    FirebaseAuth mAuth;
    FirebaseFirestore dbFirestore;
    String sellerEmail;
    ImageButton btnSend;
    EditText msgText;
    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;
    String messageID;
    DatabaseReference fbUserDbReference;
    DatabaseReference fbChatDbReference;
    String receiver;
    FirebaseUser fUser;
    String senderImage;
    String senderName;
    User user;
    String userType;
    String userID;

    ValueEventListener seenListener;

    APIService apiService;
    boolean notify = false;


    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;

    private MediaSessionCompat mediaSession;

    String senderNameNotif;
    String chatID;
    String hisID;
    String hisImage;

    String token;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        apiService = CLient.getClient("https://fcm.googleapis.com/").create(APIService.class);

        btnSend = findViewById(R.id.sendChatButton);
        msgText = findViewById(R.id.chatSend);

        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);





        profileImage = findViewById(R.id.chatProfileImage);
        username = findViewById(R.id.chatUsername);
        intent = getIntent();
        userID = intent.getStringExtra("UserID");

        //mAuth = FirebaseAuth.getInstance();
        //dbFirestore = FirebaseFirestore.getInstance();
        //sellerEmail = mAuth.getCurrentUser().getEmail();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String image = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/tapalesjanpord%40gmail.com%2FProfile-Picture%2FLXzFeVvJDrQDQwhbWrylND39Z0H3.jpeg?alt=media&token=d4d31276-5e94-41ec-abd6-16aadd85bd91";
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notify = true;
                String msg = msgText.getText().toString();
                if(!msg.equals(""))
                {
                    sendMessage(fUser.getUid(),userID,msg);
                }
                else
                {
                    msgText.setError("Empty Message");
                    msgText.requestFocus();
                }
                //sendOnChannel1();
                msgText.setText("");
            }
        });

        fbUserDbReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        fbUserDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                senderNameNotif = user.getUsername();
                if(user.getImageUrl().equals("default"))
                {
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profileImage);
                }
                readMessages(fUser.getUid(), userID, user.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void status(String status){
        fbUserDbReference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        fbUserDbReference.updateChildren(hashMap);
    }


    private void seenMessage(final String userid){
        fbChatDbReference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = fbChatDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(fUser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void sendMessage(String sender, String receiver, String messages)
    {
        messageID = UUID.randomUUID().toString();


        //Receiver
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        /*
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("MessageSender", sender);
        hashMap.put("MessageReceiver", receiver);
        hashMap.put("SenderImage", fUser.getPhotoUrl().toString());
        hashMap.put("SenderName", "John Ford Kilat");
        hashMap.put("MessageID",messageID);
        hashMap.put("MessageChat",messages);
        hashMap.put("MessageDateTime",currentDateandTime);

         */
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("message",messages);
        hashMap.put("receiver",receiver);
        hashMap.put("isseen", false);
        hashMap.put("msg_id",messageID);
        hashMap.put("MessageDateTime",currentDateandTime);

        reference.child("Chats").child(messageID).setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fUser.getUid())
                .child(userID);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists())
                {
                    chatRef.child("id").setValue(userID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final String msg = messages;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                if(notify){
                    sendNotification(receiver,user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendNotification(String receiver, String username, String msg) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fUser.getUid(), R.drawable.diy, username+": "+msg, "New Message", userID);
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200)
                                    {
                                        Log.d("Response123", fUser.getUid() + username+": "+msg +"New Message"+ userID);
                                        if(response.body().success != 1){
                                            Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readMessages(String myID, String userid, String imageUrl)
    {
        Log.d("ERRORMESSAGE","ERROR");

        mChat = new ArrayList<>();

        Query reference = FirebaseDatabase.getInstance().getReference("Chats").orderByChild("MessageDateTime");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myID) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myID))
                    {
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imageUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    @Override
    protected void onStart() {
        super.onStart();
        seenMessage(userID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fbChatDbReference.removeEventListener(seenListener);
    }

    public void sendOnChannel1() {

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.diy)
                .setContentTitle("title")
                .setContentText("message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }


    public void sendOnChannel2() {
        String title = fUser.getDisplayName();
        String message = editTextMessage.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_bookmark_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        notificationManager.notify(2, notification);
    }




}
