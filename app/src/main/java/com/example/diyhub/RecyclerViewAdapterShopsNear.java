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

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.Fragments.ShopPage;
import com.example.diyhub.Fragments.ShopsList;
import com.example.diyhub.Nearby.DistanceList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterShopsNear extends RecyclerView.Adapter<RecyclerViewAdapterShopsNear.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<ShopsList> mNames = new ArrayList<>();
    private Context mContext;


    public RecyclerViewAdapterShopsNear(Context context, ArrayList<ShopsList> names) {
        mNames = names;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        Glide.with(mContext).load(mNames.get(position).getShopImage()).into(holder.image);

        holder.name.setText(mNames.get(position).getFullname());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShopPage.class);
                intent.putExtra("SellerID", mNames.get(position).getSellerID());
                intent.putExtra("ShopName", mNames.get(position).getShopName());
                intent.putExtra("Rating", mNames.get(position).getShopRating());
                intent.putExtra("ShopImage", mNames.get(position).getShopImage());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
    }
}
