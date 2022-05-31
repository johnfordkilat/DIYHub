package com.example.diyhub.Buyer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.Fragments.OrdersList;
import com.example.diyhub.Fragments.StandardProductDetails;
import com.example.diyhub.OrdersPage;
import com.example.diyhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ToPayRv extends RecyclerView.Adapter<ToPayRv.MyViewHolder>{

    Context context;
    FirebaseFirestore dbFirestore;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    OrdersPage ordersPage;

    ArrayList<OrdersList> list;
    String app = "";
    String[] allList = {"Lalamove","Maxim"};
    AlertDialog dialog;
    AlertDialog errordialog;
    boolean clicked = false;

    public ToPayRv(Context context, ArrayList<OrdersList> list) {
        this.context = context;
        this.list = list;
        this.dbFirestore = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.progressDialog = new ProgressDialog(context);
    }

    @NonNull
    @Override
    public ToPayRv.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.topaylayout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToPayRv.MyViewHolder holder, int position) {

        OrdersList ordersList = list.get(position);

        holder.orderProductName1.setText("Item: "+ordersList.getOrderProductName());
        holder.orderQuantity1.setText(""+ordersList.getOrderQuantity());
        holder.bookingOption1.setText("Location: "+ordersList.getBookingOption());
        holder.orderType1.setText(ordersList.getOrderType());
        holder.paymentStatus1.setText(ordersList.getPaymentStatus());
        holder.orderIDTxtView1.setText("Order ID: "+ordersList.getOrderID());

        Glide.with(context).load(ordersList.getOrderProductImage()).into(holder.orderProductImage1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StandardProductDetails.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderProductName1,orderIDTxtView1,orderType1,orderQuantity1;
        ImageView prodImage1,addToNotif,locationTracker;
        Button paymentStatus1,bookingOption1, orderReceived1;
        ImageView orderProductImage1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orderProductImage1 = itemView.findViewById(R.id.orderProductImageB1);
            orderQuantity1 = itemView.findViewById(R.id.orderQuantityTxtB1);
            orderProductName1 = itemView.findViewById(R.id.orderProductNameB1);
            orderType1 = itemView.findViewById(R.id.orderTypeB1);
            paymentStatus1 = itemView.findViewById(R.id.paymentOptionB1);
            orderIDTxtView1= itemView.findViewById(R.id.orderIDOrdersPageB1);
            bookingOption1 = itemView.findViewById(R.id.deliveryTypeOrdersPageB1);


        }
    }
}
