package com.example.diyhub.Fragments;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.content.Context.ALARM_SERVICE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.Notifications.NotificationPromoDisplay;
import com.example.diyhub.OrdersPage;
import com.example.diyhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ToReceiveAdapter extends RecyclerView.Adapter<ToReceiveAdapter.MyViewHolder> {

    Context context;
    FirebaseFirestore dbFirestore;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    OrdersPage ordersPage;

    ArrayList<OrdersList> list;

    public ToReceiveAdapter(Context context, ArrayList<OrdersList> list)
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
        View v = LayoutInflater.from(context).inflate(R.layout.to_receive_standard,parent,false);
        return new MyViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrdersList productsList = list.get(position);
        holder.buyerTxt.setText("Buyer: "+productsList.getBuyerName());
        holder.locationTxt.setText("Location: "+productsList.getBookingAddress());
        holder.riderAndPlateTxt.setText("Rider: "+productsList.getRiderName() + "    Plate: "+productsList.getPlateNumber());
        holder.orderid.setText("Order ID: "+productsList.getOrderID());
        holder.paymentStatus.setText(productsList.getPaymentStatus());
        holder.bookingOption.setText(productsList.getBookingOption());

        if(productsList.getOrderType().equalsIgnoreCase("Standard"))
        {
            holder.prodType.setText("Standard");
        }
        else
        {
            holder.prodType.setText("Customized");
        }
        
        Glide.with(context).load(list.get(position).getOrderProductImage()).into(holder.prodImage);

        holder.chatBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Chat with Buyer", Toast.LENGTH_SHORT).show();
            }
        });
        
        holder.addToNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Added to Notifications", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, NotificationPromoDisplay.class);
                intent.putExtra("BuyerName", productsList.getBuyerName());
                intent.putExtra("OrderID",productsList.getOrderID());
                intent.putExtra("Location", productsList.getBookingAddress());
                intent.putExtra("RiderName", productsList.getRiderName());
                intent.putExtra("PlateNumber", productsList.getPlateNumber());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();

                long tenSeconds = 500 * 10;

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSeconds, pendingIntent);

                //Log.d("SELLERERROR", "error");
            }
        });
        
        holder.locationTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Order Tracking", Toast.LENGTH_SHORT).show();
            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.get(position).getOrderType().equalsIgnoreCase("Standard")) {
                    Intent intent = new Intent(context, ToReceiveStandardOrderPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", list);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(context, ToReceiveCustomizationOrdersPage.class);
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

        TextView buyerTxt,locationTxt,riderAndPlateTxt;
        ImageView prodImage,addToNotif,locationTracker;
        Button paymentStatus,bookingOption;
        ImageView chatBuyer;

        TextView prodType;
        TextView orderid;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            prodImage = itemView.findViewById(R.id.orderProductImage2);
            buyerTxt = itemView.findViewById(R.id.buyerToReceiveTxtStandard);
            locationTxt = itemView.findViewById(R.id.locationToReceiveTxtStandard);
            riderAndPlateTxt = itemView.findViewById(R.id.riderAndPlateToReceiveTxtStandard);
            addToNotif = itemView.findViewById(R.id.addToNotificationToReceiveStandard);
            locationTracker = itemView.findViewById(R.id.locationTrackerToReceiveStandard);
            paymentStatus = itemView.findViewById(R.id.paymentStatusTxtToReceiveStandard);
            bookingOption = itemView.findViewById(R.id.bookingTypeToReceiveStandard);
            chatBuyer = itemView.findViewById(R.id.chatBuyerToReceiveStandard);
            prodType = itemView.findViewById(R.id.productTypeToReceivePage);
            orderid  = itemView.findViewById(R.id.orderIDToReceive);


        }
    }
}
