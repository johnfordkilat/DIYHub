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

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{
    Context context;
    ArrayList<FavoritesList> list;

    public FavoritesAdapter(Context context, ArrayList<FavoritesList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorites_layout,parent,false);{
            return new FavoritesAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.ViewHolder holder, int position) {
        FavoritesList user = list.get(position);
        holder.shopName.setText(user.getShopName());
        holder.productName.setText(user.getProductName());
        Glide.with(context).load(list.get(position).getProductImage()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView shopName, productName;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productNameFavoritePage);
            shopName = itemView.findViewById(R.id.shopNameFavoritePage);
            productImage = itemView.findViewById(R.id.favoritePageImage);

        }
    }
}
