package com.example.diyhub.Buyer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.diyhub.Fragments.ToReceiveList;
import com.example.diyhub.OrdersPage;
import com.example.diyhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ToReceiveRv extends RecyclerView.Adapter<ToReceiveRv.MyViewHolder> {

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

    public ToReceiveRv(Context context, ArrayList<OrdersList> list) {
        this.context = context;
        this.list = list;
        this.dbFirestore = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.progressDialog = new ProgressDialog(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.toreceivelayout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrdersList toReceiveList = list.get(position);

        holder.orderProductName3.setText("Item: "+toReceiveList.getOrderProductName());
        holder.orderQuantity3.setText(""+toReceiveList.getOrderQuantity());
        //holder.bookingOption3.setText("Location: "+toReceiveList.getBooking());
        holder.orderType3.setText(toReceiveList.getOrderType());
        holder.paymentStatus3.setText(toReceiveList.getPaymentStatus());
        holder.orderIDTxtView3.setText("Order ID: "+toReceiveList.getOrderID());

        Glide.with(context).load(toReceiveList.getOrderProductImage()).into(holder.orderProductImage3);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderProductName3,orderIDTxtView3,orderQuantity3,bookingOption3;
        ImageView prodImage3,addToNotif,locationTracker;
        Button paymentStatus3,orderType3, orderReceived3;
        ImageView orderProductImage3;

        TextView prodType;
        TextView orderid;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderProductImage3 = itemView.findViewById(R.id.orderProductImageB3);
            orderQuantity3 = itemView.findViewById(R.id.orderQuantityTxtB3);
            orderProductName3 = itemView.findViewById(R.id.orderProductNameB3);
            orderType3 = itemView.findViewById(R.id.orderTypeB3);
            paymentStatus3 = itemView.findViewById(R.id.paymentOptionB3);
            orderIDTxtView3= itemView.findViewById(R.id.orderIDOrdersPageB3);
            bookingOption3 = itemView.findViewById(R.id.deliveryTypeOrdersPageB3);

            orderReceived3 = itemView.findViewById(R.id.orderReceived3);
        }
    }
}
