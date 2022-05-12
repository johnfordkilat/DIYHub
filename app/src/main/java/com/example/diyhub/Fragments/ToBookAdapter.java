package com.example.diyhub.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.OrdersPage;
import com.example.diyhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ToBookAdapter extends RecyclerView.Adapter<ToBookAdapter.MyViewHolder> {

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
    public ToBookAdapter(Context context, ArrayList<OrdersList> list)
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
        View v = LayoutInflater.from(context).inflate(R.layout.tobook_page,parent,false);
        return new MyViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrdersList productsList = list.get(position);

        holder.itemName.setText("Item: "+productsList.getOrderProductName());
        holder.buyerName.setText("Buyer: "+productsList.getBuyerName());
        holder.location.setText("Location: "+productsList.getBookingAddress());
        holder.orderType.setText(productsList.getOrderType());
        holder.paymentOption.setText(productsList.getPaymentOption());
        //holder.orderid.setText("Order ID: "+productsList.getOrderID());


        Glide.with(context).load(productsList.getOrderProductImage()).into(holder.itemImage);

        holder.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        holder.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(position).getOrderType().equalsIgnoreCase("Standard")){
                    Intent intent = new Intent(context, OrderDetailToBookStandardPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", list);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(context, OrderDetailToBookCustomizationPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", list);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });


        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                dialog = builder.create();
                builder.setTitle("Choose Booking Application");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(allList, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        app =  allList[i];
                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(app.equals("")){
                            AlertDialog.Builder error = new AlertDialog.Builder(context);
                            errordialog = error.create();
                            error.setTitle("NO OPTION SELECTED!");
                            error.setMessage("PLese select BOOKING OPTION!");
                            error.setCancelable(false);
                            error.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                       errordialog.dismiss();
                                }
                            });

                            error.show();
                        }
                        else
                        {
                            Toast.makeText(context, app, Toast.LENGTH_SHORT).show();
                            if(app.equalsIgnoreCase("Lalamove")){
                                Intent intent = context.getPackageManager().getLaunchIntentForPackage("hk.easyvan.app.client");
                                if(intent != null){
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                    app = "";
                                }
                                else{
                                    Toast.makeText(context, "App is not installed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.taxsee.taxsee");
                                if(intent != null){
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                    app = "";
                                }
                                else{
                                    Toast.makeText(context, "App is not installed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                        }
                    }
                });
                builder.show();
            }
        });


    }





    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageButton copyButton;
        ImageView nextButton;
        Button bookButton;
        TextView itemName,buyerName,location;
        ImageView itemImage;
        Button orderType,paymentOption;
        //TextView orderid;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            copyButton = itemView.findViewById(R.id.copyButtonToBook);
            nextButton = itemView.findViewById(R.id.nextButtonToBook);
            bookButton = itemView.findViewById(R.id.bookButtonToBook);
            itemName = itemView.findViewById(R.id.itemNameTxtToBook);
            buyerName = itemView.findViewById(R.id.buyerNameTxtToBook);
            location = itemView.findViewById(R.id.locationTxtToBook);
            itemImage = itemView.findViewById(R.id.itemImageToBook);
            orderType = itemView.findViewById(R.id.orderTypeToBook);
            paymentOption = itemView.findViewById(R.id.paymentOptionToBook);
            //orderid = itemView.findViewById(R.id.orderIDOrdersPage);

        }
    }
}
