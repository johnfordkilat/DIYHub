package com.example.diyhub;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.Notifications.Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.DescriptorProtos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapterCart extends RecyclerView.Adapter<MyAdapterCart.MyViewHolder> {

    List<CartPageList> list;
    Context context;
    double total = 0.0;
    double price = 0.0;
    int quant;
    int multiplier;
    int count = 0;
    double[] totalPrice = new double[10];
    double[] displayPrice = new double[10];
    public MyAdapterCart(Context ct, List<CartPageList> list)
    {
        this.context = ct;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.shoppingcart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartPageList cart = list.get(position);
        holder.itemName.setText(cart.getProductName());
        holder.itemPrice.setText("₱"+String.valueOf(cart.getProductPrice()));
        Glide.with(context).load(cart.getProductImage()).into(holder.itemImg);
        total = 0;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        holder.quantity.setText(String.valueOf(cart.getProductQuantity()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Remove Confirmation");
                builder.setMessage("Are you sure you want to remove this from Cart?");
                builder.setCancelable(false);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ShoppingCart").child(user.getUid()).child(cart.getProductID());
                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });

        updatePrice();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updatePrice()
    {
        double sum = 0;
        int i;
        for(i=0; i<list.size(); i++)
        {
            sum = sum+(list.get(i).getProductPrice()*list.get(i).getProductQuantity());
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> map = new HashMap<>();
        map.put("TotalPrice", sum);
        reference.child("ShoppingCart").child(user.getUid()).child(list.get(0).getProductID()).updateChildren(map);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName,itemPrice;
        ImageView itemImg,cartImg;
        CheckBox checkBox;
        Button minus,plus;
        TextView quantity;
        ImageView delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameTxtCart);
            itemPrice = itemView.findViewById(R.id.itemPriceTxtCart);
            itemImg = itemView.findViewById(R.id.itemImageView1);
            quantity = itemView.findViewById(R.id.quantityTotalTxtCart);
            delete = itemView.findViewById(R.id.deleteItemCartButton);
        }
    }

}
