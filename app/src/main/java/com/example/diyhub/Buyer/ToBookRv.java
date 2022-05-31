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
import com.example.diyhub.Fragments.ToBookList;
import com.example.diyhub.OrdersPage;
import com.example.diyhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ToBookRv extends RecyclerView.Adapter<ToBookRv.MyViewHolder>{

    Context context;
    FirebaseFirestore dbFirestore;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    OrdersPage ordersPage;

    ArrayList<ToBookList> list;
    String app = "";
    String[] allList = {"Lalamove","Maxim"};
    AlertDialog dialog;
    AlertDialog errordialog;
    boolean clicked = false;

    public ToBookRv(Context context, ArrayList<ToBookList> list) {
        this.context = context;
        this.list = list;
        this.dbFirestore = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.progressDialog = new ProgressDialog(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.tobooklayout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToBookList toBookList = list.get(position);

        holder.orderProductName2.setText("Item: "+toBookList.getOrderProductName());
        holder.orderQuantity2.setText(""+toBookList.getOrderQuantity());
        holder.orderType2.setText(toBookList.getOrderType());
        holder.paymentStatus2.setText(toBookList.getPaymentStatus());
        holder.orderIDTxtView2.setText("Order ID: "+toBookList.getOrderID());

        Glide.with(context).load(toBookList.getOrderProductImage()).into(holder.orderProductImage2);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderProductName2,orderIDTxtView2,orderType2,orderQuantity2;
        ImageView prodImage2,addToNotif,locationTracker;
        Button paymentStatus2,bookingOption2, orderReceived2;
        ImageView orderProductImage2;

        TextView prodType;
        TextView orderid;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderProductImage2 = itemView.findViewById(R.id.orderProductImageB2);
            orderQuantity2 = itemView.findViewById(R.id.orderQuantityTxtB2);
            orderProductName2 = itemView.findViewById(R.id.orderProductNameB2);
            orderType2 = itemView.findViewById(R.id.orderTypeB2);
            paymentStatus2 = itemView.findViewById(R.id.paymentOptionB2);
            orderIDTxtView2= itemView.findViewById(R.id.orderIDOrdersPageB2);
            bookingOption2 = itemView.findViewById(R.id.deliveryTypeOrdersPageB2);

            prodImage2 = itemView.findViewById(R.id.orderReceived3);
        }
    }
}
