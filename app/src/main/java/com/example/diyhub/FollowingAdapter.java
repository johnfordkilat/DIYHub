package com.example.diyhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {
    View view;
    Context context;
    ArrayList<FollowingList> list;

    public FollowingAdapter(Context context, ArrayList<FollowingList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.following_layout,parent,false);{
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.ViewHolder holder, int position) {
        FollowingList followingList = list.get(position);
        holder.followingShopName.setText(followingList.getShopName());
        holder.shopRating.setText(""+followingList.getShopRating());
        Glide.with(context).load(list.get(position).getShopImage()).into(holder.followingShopImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView followingShopName, shopRating;
        CircleImageView followingShopImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shopRating = itemView.findViewById(R.id.ratingFollowing);
            followingShopName = itemView.findViewById(R.id.shopNameFollowing);
            followingShopImage = itemView.findViewById(R.id.imageFollowing);


        }
    }
}

