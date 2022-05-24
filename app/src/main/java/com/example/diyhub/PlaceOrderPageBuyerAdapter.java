package com.example.diyhub;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.Notifications.APIService;
import com.example.diyhub.Notifications.CLient;
import com.example.diyhub.Notifications.UserNotif;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PlaceOrderPageBuyerAdapter extends RecyclerView.Adapter<PlaceOrderPageBuyerAdapter.MyViewHolder> {

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
    String playImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/johnfordtapales8%40gmail.com%2Fillust58-7479-01-removebg-preview.png?alt=media&token=a322e775-e3fd-4fb3-8d77-3767a348120d";
    String pauseImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/johnfordtapales8%40gmail.com%2Fpause__video__stop-removebg-preview.png?alt=media&token=ec3433a8-9579-4aca-8faa-6fe1c58d8474";

    ArrayList<AllProductsList> list;
    Intent intent;


    public PlaceOrderPageBuyerAdapter(){

    }

    APIService apiService = apiService = CLient.getClient("https://fcm.googleapis.com/").create(APIService.class);


    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());

    List<UserNotif> mUsers = new ArrayList<>();
    int pos;
    String dbStatus;
    String imageStatus;

    public PlaceOrderPageBuyerAdapter(Context context, ArrayList<AllProductsList> list)
    {
        this.context = context;
        this.list = list;
        this.dbFirestore = FirebaseFirestore.getInstance();
        this.progressDialog = new ProgressDialog(context);
        this.mAuth = FirebaseAuth.getInstance();
        this.type = "";
        this.color= new String[list.size()];

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.proceed_with_payment_layout,parent,false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return new MyViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AllProductsList productsList = list.get(position);
        holder.prodName.setText(productsList.getProductName());
        holder.prodQuan.setText("x"+productsList.getProductQuantity());
        holder.shopName.setText(productsList.getShopName());
        holder.prodPrice.setText("₱"+String.valueOf(productsList.getProductPrice()));
        holder.prodVariation.setText(productsList.getProductName());

        Glide.with(context).load(list.get(position).getProductImage()).into(holder.prodImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView shopName,prodName,prodVariation,prodPrice,prodQuan;
        ImageView prodImage;
        
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.productNamePlaceOrder);
            prodQuan = itemView.findViewById(R.id.productQuantityPlaceOrder);
            shopName = itemView.findViewById(R.id.shopNameTxtPlaceOrder);
            prodImage = itemView.findViewById(R.id.productImagePlaceOrder);
            prodVariation = itemView.findViewById(R.id.variationPlaceOrder);
            prodPrice = itemView.findViewById(R.id.productPricePlaceOrder);
            

        }
    }
}
