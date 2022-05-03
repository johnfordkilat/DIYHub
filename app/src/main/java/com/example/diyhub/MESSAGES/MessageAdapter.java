package com.example.diyhub.MESSAGES;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_TYPE_LEFT = 0;

    private Context mContext;
    private List<Chat> mChats;
    private String imageUrl;
    FirebaseUser fUser;
    boolean toDelete=false;

    public MessageAdapter(Context mContext, List<Chat> mUsers, String imageUrl) {
        this.mContext = mContext;
        this.mChats = mUsers;
        this.imageUrl = imageUrl;
    }



    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        Chat chat = mChats.get(position);

        holder.showMessage.setText(chat.getMessage());


        holder.showMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(getItemViewType(position) == MSG_TYPE_RIGHT)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("DELETE CONFIRMATION");
                    builder.setMessage("Are you sure you want to delete?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats").child(chat.getMsg_id());

                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ref.removeValue();
                                        mChats.remove(position);
                                        Toast.makeText(mContext, "Message Deleted", Toast.LENGTH_SHORT).show();
                                        notifyItemRemoved(position);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
                return true;
            }
        });

        if(imageUrl.equals("default"))
        {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }
        else
        {
            Glide.with(mContext).load(imageUrl).into(holder.profileImage);
        }

        if(position == mChats.size() - 1)
        {
            if(chat.isIsseen())
            {
                holder.txt_seen.setText("Seen");
            }
            else
            {
                holder.txt_seen.setText("Delivered");
            }
        }
        else
        {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView showMessage;
        public ImageView profileImage;
        public TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.showMessage);
            profileImage = itemView.findViewById(R.id.ProfileImage);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mChats.get(position).getSender().equals(fUser.getUid()))
        {
            toDelete = true;
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }
}
