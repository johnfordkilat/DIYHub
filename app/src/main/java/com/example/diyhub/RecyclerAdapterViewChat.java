package com.example.diyhub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.MESSAGES.MessageActivity;
import com.example.diyhub.MESSAGES.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterViewChat extends RecyclerView.Adapter<RecyclerAdapterViewChat.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<User> mList;
    private Context mContext;

    FirebaseFirestore dbFirestore;
    FirebaseAuth mAuth;


    public RecyclerAdapterViewChat(Context context, ArrayList<User> messagesLists) {
        mList = messagesLists;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        Glide.with(mContext)
                .asBitmap()
                .load(mList.get(position).getImageUrl())
                .into(holder.image);

        holder.name.setText(mList.get(position).getUsername());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + mList.get(position).getUsername());
                Toast.makeText(mContext, mList.get(position).getId(), Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(mContext, ChatPage.class);
                //mContext.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("Name", mList.get(position).getUsername());
                intent.putExtra("ProfileImage", mList.get(position).getImageUrl());
                intent.putExtra("UserID", mList.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view1Chat);
            name = itemView.findViewById(R.id.name1Chat);
        }
    }
}
