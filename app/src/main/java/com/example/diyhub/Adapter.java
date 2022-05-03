package com.example.diyhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    ArrayList<String> itemName;
    ArrayList<String> shopName;
    ArrayList<Integer> images;
    LayoutInflater inflater;

    public Adapter(Context cs, ArrayList<String> iN, ArrayList<String> sN, ArrayList<Integer> iM)
    {
        this.itemName = iN;
        this.shopName = sN;
        this.images = iM;
        this.inflater = LayoutInflater.from(cs);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.favorites_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(itemName.get(position));
        holder.shopName.setText(shopName.get(position));
        holder.favImage.setImageResource(images.get(position));

    }

    @Override
    public int getItemCount() {
        return itemName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName,shopName;
        ImageView favImage;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.favItemNameTxt);
            shopName = itemView.findViewById(R.id.favShopNameTxt);
            favImage = itemView.findViewById(R.id.favoriteImageVIew);
        }
    }
}
