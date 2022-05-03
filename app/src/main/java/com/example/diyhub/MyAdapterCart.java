package com.example.diyhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterCart extends RecyclerView.Adapter<MyAdapterCart.MyViewHolder> {

    String data1[], data2[];
    int images[],images1[];
    Context context;

    public MyAdapterCart(Context ct, String s1[], String s2[], int img[])
    {
        context = ct;
        data1 = s1;
        data2 = s2;
        images = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.shoppingcart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.item.setText(data1[position]);
        holder.purchase.setText(data2[position]);
        holder.itemImg.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView item,purchase;
        ImageView itemImg,cartImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.textView30);
            purchase = itemView.findViewById(R.id.purchases1);
            itemImg = itemView.findViewById(R.id.itemImageView1);
        }
    }
}
