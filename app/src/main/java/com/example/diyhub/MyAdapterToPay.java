package com.example.diyhub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterToPay extends RecyclerView.Adapter<MyAdapterToPay.MyViewHolder> {

    String itemName[], itemamount[];
    int images[],images1[];
    Context context;

    public MyAdapterToPay(Context ct, String s1[], String s2[], int img[])
    {
        context = ct;
        itemName = s1;
        itemamount = s2;
        images = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.topaylayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemName.setText(itemName[position]);
        holder.amount.setText(itemamount[position]);
        holder.itemImg.setImageResource(images[position]);
        holder.toPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentPage.class);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName,amount;
        ImageView itemImg,toPay;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.toPayItemName);
            amount = itemView.findViewById(R.id.toPayAmount);
            itemImg = itemView.findViewById(R.id.toPayImage);
            toPay = itemView.findViewById(R.id.paymentButtonToPay);
        }
    }
}
