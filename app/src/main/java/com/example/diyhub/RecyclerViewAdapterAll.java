package com.example.diyhub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.Fragments.RecommendedShopsList;
import com.example.diyhub.Fragments.ShopPage;
import com.example.diyhub.Fragments.ShopsList;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterAll extends RecyclerView.Adapter<RecyclerViewAdapterAll.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<ShopsList> mImageUrls;
    private Context mContext;
    int counter;
    String id;

    public RecyclerViewAdapterAll(Context context, ArrayList<ShopsList> imageUrls) {
        mImageUrls = imageUrls;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext).load(mImageUrls.get(position).getShopImage()).into(holder.image);

        holder.name.setText(mImageUrls.get(position).getShopName());

        Intent intent1 = new Intent("ProceedToCartFromHomepage");
        intent1.putExtra("SellerIDCart", mImageUrls.get(position).getSellerID());
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent1);
        int countApp;
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                DatabaseReference refRetrieve = FirebaseDatabase.getInstance().getReference();
                refRetrieve.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                if(dataSnapshot.child("ShopViews").getValue().toString().equalsIgnoreCase("100")) {
                                    id = dataSnapshot.getKey();
                                    Integer count = snapshot.child(id).child("count").getValue(Integer.class);

                                    refRetrieve.child(id).child("count").setValue(count + 1);
                                    break;
                                }
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                 */

                Intent intent = new Intent(mContext, ShopPage.class);
                intent.putExtra("SellerID", mImageUrls.get(position).getSellerID());
                intent.putExtra("ShopName", mImageUrls.get(position).getShopName());
                intent.putExtra("Rating", mImageUrls.get(position).getShopRating());
                intent.putExtra("ShopImage", mImageUrls.get(position).getShopImage());
                mContext.startActivity(intent);
            }
        });






    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
    }
}
