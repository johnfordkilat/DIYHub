package com.example.diyhub.Notifications;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.Fragments.OrderDetailStandardPage;
import com.example.diyhub.Fragments.SellerOrdersFragment;
import com.example.diyhub.MESSAGES.Bookmark;
import com.example.diyhub.MESSAGES.Chat;
import com.example.diyhub.MESSAGES.MessageActivity;
import com.example.diyhub.MESSAGES.User;
import com.example.diyhub.R;
import com.example.diyhub.SellerHomePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationPromoAdapter extends RecyclerView.Adapter<NotificationPromoAdapter.ViewHolder>{


    private Context mContext;
    private List<NotificationPromoList> notiflist;



    public NotificationPromoAdapter(Context mContext, List<NotificationPromoList> list) {
        this.mContext = mContext;
        this.notiflist = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notif_promo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        NotificationPromoList promoList = notiflist.get(position);

        Glide.with(mContext).load(promoList.getNotifImage()).into(holder.image);
        holder.notifList.setText(promoList.getNotifDescription());

        holder.notifHeader.setText(promoList.getNotifHeader());
        holder.dateAndTime.setText(promoList.getNotifDateAndTime());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.notifCardview.setCardBackgroundColor(Color.WHITE);

                if(promoList.getNotifHeader().equalsIgnoreCase("Order Request"))
                {
                    Intent intent = new Intent(mContext, SellerOrdersFragment.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(mContext, NotificationDisplayPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("notifid", promoList.getNotifID());
                    mContext.startActivity(intent);
                }


            }
        });

        if(promoList.getIsSeen().equalsIgnoreCase("true"))
        {
            holder.notifCardview.setCardBackgroundColor(Color.WHITE);
        }




    }

    @Override
    public int getItemCount() {
        return notiflist.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView notifList;
        Button viewNotif;
        CardView notifCardview;
        TextView dateAndTime;
        TextView notifHeader;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImageView);
            notifList = itemView.findViewById(R.id.notifListTextView);
            notifCardview = itemView.findViewById(R.id.notifCardViewNotifList);
            dateAndTime = itemView.findViewById(R.id.dateAndTimeTextViewNotif);
            notifHeader = itemView.findViewById(R.id.notificationHeaderNotifList);


        }
    }

}
