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
import com.example.diyhub.Notifications.PromoProducts;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    List<PromoProducts> list;

    public MyAdapter(Context ct, List<PromoProducts> mList)
    {
        context = ct;
        list = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notif_promo, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PromoProducts products = list.get(position);

        holder.item.setText(products.getProductName());
        holder.purchase.setText(String.valueOf(products.getPurchases()));
        holder.cartImg.setImageResource(R.drawable.cart);

        if(products.getItemImage().equals("default")){
            holder.itemImg.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(context).load(products.getItemImage()).into(holder.itemImg);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView item,purchase;
        ImageView itemImg,cartImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.textView29);
            purchase = itemView.findViewById(R.id.purchases);
            itemImg = itemView.findViewById(R.id.itemImageView);
            cartImg = itemView.findViewById(R.id.cartImageView);
        }
    }
}
