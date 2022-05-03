package com.example.diyhub.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.OrdersPage;
import com.example.diyhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OrdersOngoingAdapter extends RecyclerView.Adapter<OrdersOngoingAdapter.MyViewHolder> {

    Context context;
    FirebaseFirestore dbFirestore;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    OrdersPage ordersPage;

    ArrayList<OrdersList> list;

    public OrdersOngoingAdapter(Context context, ArrayList<OrdersList> list)
    {
        this.context = context;
        this.list = list;
        this.dbFirestore = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.progressDialog = new ProgressDialog(context);
        this.ordersPage = new OrdersPage();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.orders_page,parent,false);
        return new MyViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrdersList productsList = list.get(position);
        holder.prodName.setText(productsList.getOrderProductName());
        holder.prodQuan.setText(productsList.getOrderQuantity());
        holder.orderType.setText(productsList.getOrderType());
        holder.paymentOption.setText(productsList.getPaymentOption());
        Glide.with(context).load(list.get(position).getOrderProductImage()).into(holder.prodImage);
        holder.orderid.setText("Order ID: "+productsList.getOrderID());


        holder.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.get(position).getOrderType().equalsIgnoreCase("Standard")){
                    Intent intent = new Intent(context, OrderDetailOngoingStandardPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", list);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(context, OrderDetailOngoingCustomizationPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", list);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });


    }





    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView prodName,prodQuan,prodStocks;
        ImageView prodImage,deleteProd,updateProd,nextButton;
        Button orderType,paymentOption;
        TextView orderid;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.orderProductName);
            prodQuan = itemView.findViewById(R.id.orderQuantityTxt);
            prodImage = itemView.findViewById(R.id.orderProductImage);
            orderType = itemView.findViewById(R.id.orderType);
            paymentOption = itemView.findViewById(R.id.paymentOption);
            nextButton = itemView.findViewById(R.id.nextButtonOrdersPage);
            orderid = itemView.findViewById(R.id.orderIDOrdersPage);

        }
    }
}
