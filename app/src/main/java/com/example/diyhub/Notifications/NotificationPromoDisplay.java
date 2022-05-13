package com.example.diyhub.Notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.diyhub.R;


public class NotificationPromoDisplay extends BroadcastReceiver{
    String word[] = {"one","two","three","four","five","six"};



    @Override
    public void onReceive(Context context, Intent intent) {


            String buyerName = intent.getExtras().getString("BuyerName");
            String orderid = intent.getExtras().getString("OrderID");
            String location = intent.getExtras().getString("Location");
            String riderName = intent.getExtras().getString("RiderName");
            String plateNumber = intent.getExtras().getString("PlateNumber");

            Notification  notification = new NotificationCompat.Builder(context, "diyhub")
                    .setSmallIcon(R.drawable.img_12)
                    .setContentTitle("DIYHub Notifications")
                    .setContentText("Order "+orderid+ " is Completed")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(0, notification);
        }




}
