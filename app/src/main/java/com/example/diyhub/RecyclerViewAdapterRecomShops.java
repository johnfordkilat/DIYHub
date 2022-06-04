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
import com.example.diyhub.Fragments.RecommendedShopsList;
import com.example.diyhub.Fragments.ShopPage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterRecomShops extends RecyclerView.Adapter<RecyclerViewAdapterRecomShops.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapterRecomShops";

    //vars
    private ArrayList<RecommendedShopsList> recomshops = new ArrayList<>();
    private Context mContext;
    private ArrayList<String> seq = new ArrayList<>();


    public RecyclerViewAdapterRecomShops(Context context, ArrayList<RecommendedShopsList> recomshops) {
        this.recomshops = recomshops;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_recommshops, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        RecommendedShopsList shopsList = recomshops.get(position);


        Glide.with(mContext).load(shopsList.getShopImage()).into(holder.shopimage);
        holder.shopname.setText(shopsList.getShopName() + "    " + shopsList.getShopRating());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShopPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("SellerID", recomshops.get(position).getSellerID());
                intent.putExtra("ShopName", recomshops.get(position).getShopName());
                intent.putExtra("Rating", recomshops.get(position).getShopRating());
                intent.putExtra("ShopImage", recomshops.get(position).getShopImage());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recomshops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView shopimage;
        TextView shopname;

        public ViewHolder(View itemView) {
            super(itemView);
            shopimage = itemView.findViewById(R.id.shopImageRecommShopHomePage);
            shopname = itemView.findViewById(R.id.shopNameRecommShopHomePage);
        }
    }
}
