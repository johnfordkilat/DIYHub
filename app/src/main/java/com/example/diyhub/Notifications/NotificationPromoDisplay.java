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
            Notification  notification = new NotificationCompat.Builder(context, "diyhub")
                    .setSmallIcon(R.drawable.img_12)
                    .setContentTitle("DIY Hub Notifications Promo")
                    .setContentText(String.valueOf("Hello"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(0, notification);
        }




}
