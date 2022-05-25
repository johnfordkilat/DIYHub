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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.MESSAGES.User;
import com.example.diyhub.Notifications.APIService;
import com.example.diyhub.Notifications.CLient;
import com.example.diyhub.Notifications.UserNotif;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    ArrayList<String> integers;
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
    String buyerName = null;
    String buyerAddress = null;


    public PlaceOrderPageBuyerAdapter(Context context, ArrayList<AllProductsList> list, ArrayList<String> integers)
    {
        this.context = context;
        this.list = list;
        this.dbFirestore = FirebaseFirestore.getInstance();
        this.progressDialog = new ProgressDialog(context);
        this.mAuth = FirebaseAuth.getInstance();
        this.type = "";
        this.color= new String[list.size()];
        this.integers = integers;

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
        holder.prodName.setText(list.get(position).getProductName());
        holder.prodQuan.setText("x"+integers.get(0));
        holder.shopName.setText(list.get(position).getShopName());
        holder.prodPrice.setText("₱"+list.get(position).getProductPrice());
        holder.prodVariation.setText(integers.get(1));
        holder.subTotal.setText("₱"+list.get(position).getProductPrice());
        holder.shipping.setText("₱"+list.get(position).getProductShippingFee());
        holder.addFees.setText("₱"+list.get(position).getProductAdditionalFee());
        holder.numOfItems.setText(String.valueOf(integers.get(0)));

        int quantity = Integer.parseInt(integers.get(0));
        double shipping = list.get(position).getProductShippingFee();
        double addFees = list.get(position).getProductAdditionalFee();
        double price = list.get(position).getProductPrice();
        double totalPayment = (price * quantity) + (addFees + shipping);
        holder.totalPayment.setText("₱"+""+totalPayment);

        Glide.with(context).load(list.get(position).getProductImage()).into(holder.prodImage);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    User user1 = snapshot.getValue(User.class);
                    if(user1.getId().equalsIgnoreCase(user.getUid()))
                    {
                        buyerName = user1.getUsername();
                        buyerAddress = user1.getAddress();
                        Intent intent = new Intent("ProceedWithPaymentData");
                        intent.putExtra("OrderProductName",productsList.getProductName());
                        intent.putExtra("OrderQuantity",productsList.getProductQuantity());
                        intent.putExtra("OrderProductImage",productsList.getProductImage());
                        intent.putExtra("OrderType",productsList.getProductType());
                        intent.putExtra("ItemCode",productsList.getProductID());
                        intent.putExtra("BuyerName",buyerName);
                        intent.putExtra("PaymentStatus","Pending");
                        intent.putExtra("BookingAddress",buyerAddress);
                        intent.putExtra("OrderStatus","To Pay");
                        intent.putExtra("ShopName",productsList.getShopName());
                        intent.putExtra("OrderProductPrice",productsList.getProductPrice());
                        intent.putExtra("OrderShippingFee",productsList.getProductShippingFee());
                        intent.putExtra("OrderAdditionalFee",productsList.getProductAdditionalFee());
                        intent.putExtra("OrderTotalPayment",totalPayment);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






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
        TextView subTotal, shipping, addFees, numOfItems,totalPayment;
        
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.productNamePlaceOrder);
            prodQuan = itemView.findViewById(R.id.productQuantityPlaceOrder);
            shopName = itemView.findViewById(R.id.shopNameTxtPlaceOrder);
            prodImage = itemView.findViewById(R.id.productImagePlaceOrder);
            prodVariation = itemView.findViewById(R.id.variationPlaceOrder);
            prodPrice = itemView.findViewById(R.id.productPricePlaceOrder);
            subTotal = itemView.findViewById(R.id.merchSubtotalTxt);
            shipping = itemView.findViewById(R.id.shippingSubTotalTxt);
            addFees = itemView.findViewById(R.id.additionalFeesTxt);
            numOfItems = itemView.findViewById(R.id.totalNumOfItemsTxt);
            totalPayment = itemView.findViewById(R.id.totalPaymentTxt);
            

        }
    }
}
