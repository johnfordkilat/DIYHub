package com.example.diyhub.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewReceiptPageAdapter extends RecyclerView.Adapter<ViewReceiptPageAdapter.MyViewHolder>{

    Context context;
    FirebaseFirestore dbFirestore;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    int pQuan;
    int pStocks;
    String type;
    private boolean clicked = false;
    String color[];
    String status;
    String playImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fillust58-7479-01-removebg-preview.png?alt=media&token=63a829e1-660e-47e6-9b26-dc66d8eaac48";
    String pauseImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fpause__video__stop-removebg-preview.png?alt=media&token=dc125631-d226-41e1-91ac-6abf0b97c18d";

    ArrayList<OrdersList> list;

    public ViewReceiptPageAdapter(){

    }



    public ViewReceiptPageAdapter(Context context, ArrayList<OrdersList> list)
    {
        this.context = context;
        this.list = list;
        this.dbFirestore = FirebaseFirestore.getInstance();
        this.progressDialog = new ProgressDialog(context);
        this.mAuth = FirebaseAuth.getInstance();
        this.type = "";
        this.pQuan = 0;
        this.pStocks = 0;
        this.color= new String[list.size()];
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.invoice_receipt_layout,parent,false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrdersList completedOrderList = list.get(position);
        holder.prodName.setText(completedOrderList.getOrderProductName());
        holder.quantAndPrice.setText(completedOrderList.getOrderQuantity()+" X ₱" +completedOrderList.getOrderProductPrice()+"/Unit");

        int quantity = completedOrderList.getOrderQuantity();
        double price = completedOrderList.getOrderProductPrice();
        double totalPrice = price * quantity;

        holder.totalPrice.setText("₱"+totalPrice);

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView quantAndPrice,prodName,totalPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.productNameReceiptTxtBuyer);
            quantAndPrice = itemView.findViewById(R.id.productPriceReceiptTxtBuyer);
            totalPrice = itemView.findViewById(R.id.totalPriceReceiptTxtBuyer);



        }
    }
}
