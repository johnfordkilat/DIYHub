package com.example.diyhub.MESSAGES;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder>{


    private Context mContext;
    private List<User> mUsers;
    private List<User> mUsersFull;
    private List<Bookmark> bmark;
    String theLastMessage;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    boolean bookmarked = false;


    public BookmarkAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        User user = mUsers.get(position);


        if(user.getImageUrl().equals("default"))
        {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }
        else
        {
            Glide.with(mContext).load(user.getImageUrl()).into(holder.image);
        }


        holder.name.setText(user.getUsername());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, mUsers.get(position).getUsername(), Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(mContext, ChatPage.class);
                //mContext.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("UserID", user.getId());
                mContext.startActivity(intent);
            }
        });

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Bookmarks")
                        .child(firebaseUser.getUid())
                        .child(user.getId());

                chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.exists())
                        {
                            chatRef.child("id").setValue(user.getId());
                            chatRef.child("bookmarked").setValue("true");
                            Toast.makeText(mContext, "Added to Bookmark", Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.exists()){
                            AlertDialog.Builder alert = new AlertDialog.Builder(mContext)
                                    .setTitle("Remove Bookmark")
                                    .setMessage("Are you sure you want to remove to bookmarks?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            chatRef.getRef().removeValue();
                                            Toast.makeText(mContext, "Bookmark Removed!", Toast.LENGTH_SHORT).show();
                                            mUsers.remove(position);
                                            notifyItemRemoved(position);

                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            alert.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        lastMessage(user.getId(), holder.last_msg);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Bookmarks")
                .child(firebaseUser.getUid())
                .child(user.getId());

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    holder.bookmark.setImageResource(R.drawable.bookmark);
                }
                if(!dataSnapshot.exists())
                {
                    holder.bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name;
        TextView last_msg;
        ImageButton bookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profileImageChat);
            name = itemView.findViewById(R.id.username);
            last_msg = itemView.findViewById(R.id.last_msg);
            bookmark = itemView.findViewById(R.id.bookmarkImageButton);

        }
    }

    private void lastMessage(String userid, TextView last_msg)
    {
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query reference = FirebaseDatabase.getInstance().getReference("Chats").orderByChild("MessageDateTime");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)
                            || chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid()))
                    {
                        theLastMessage = chat.getMessage();
                    }
                }

                switch (theLastMessage)
                {
                    case "default":
                        last_msg.setText("No Message");
                        break;
                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
