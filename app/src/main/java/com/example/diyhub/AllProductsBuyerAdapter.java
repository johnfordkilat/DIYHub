package com.example.diyhub;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyhub.Fragments.CustomProductDetails;
import com.example.diyhub.Fragments.StandardProductBuyer;
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

public class AllProductsBuyerAdapter extends RecyclerView.Adapter<AllProductsBuyerAdapter.MyViewHolder> {

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


    public AllProductsBuyerAdapter(){

    }

    APIService apiService = apiService = CLient.getClient("https://fcm.googleapis.com/").create(APIService.class);


    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());

    List<UserNotif> mUsers = new ArrayList<>();
    int pos;
    String dbStatus;
    String imageStatus;

    public AllProductsBuyerAdapter(Context context, ArrayList<AllProductsList> list)
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
        View v = LayoutInflater.from(context).inflate(R.layout.allproducts,parent,false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return new MyViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AllProductsList productsList = list.get(position);
        holder.prodName.setText("Product Name: "+productsList.getProductName());
        holder.prodQuan.setText("Purchases: "+String.valueOf(productsList.getProductQuantity()));
        holder.prodStocks.setText("Stocks: "+String.valueOf(productsList.getProductStocks()));
        holder.priceTxt.setText("Price: ₱"+String.valueOf(productsList.getProductPrice()));
        Glide.with(context).load(list.get(position).getProductImage()).into(holder.prodImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productsList.getProductType().equalsIgnoreCase("Standard"))
                {
                    Intent intent = new Intent(context, StandardProductBuyer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Rating",productsList.getProductRating());
                    intent.putExtra("ProductSold",productsList.getProductSold());
                    intent.putExtra("ProductStocks", productsList.getProductStocks());
                    intent.putExtra("BookFrom", productsList.getProductBookFrom());
                    intent.putExtra("ProductPrice",productsList.getProductPrice());
                    intent.putExtra("ProductImage", productsList.getProductImage());
                    intent.putExtra("ProductDescription",productsList.getProductDescription());
                    intent.putExtra("ProductName",productsList.getProductName());
                    intent.putExtra("SellerID",productsList.getSellerID());
                    intent.putExtra("ProductID", productsList.getProductID());
                    context.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(context, CustomProductDetails.class);
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

        TextView prodName,prodQuan,prodStocks,stocksLabel,restockTab,holdTab,productNameLabel,purchasesLabel;
        ImageView prodImage,deleteProd,updateProd,pauseButton;
        Button toOrderPage;
        TextView prodTypeLabel;
        TextView priceTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.productNameSeller);
            prodQuan = itemView.findViewById(R.id.purchaseCountSeller);
            prodStocks = itemView.findViewById(R.id.stocksCountSeller);
            prodImage = itemView.findViewById(R.id.productImageSeller);
            deleteProd = itemView.findViewById(R.id.deleteProduct);
            updateProd = itemView.findViewById(R.id.updateProduct);
            restockTab = itemView.findViewById(R.id.restockTab);
            holdTab = itemView.findViewById(R.id.holdTab);
            pauseButton = itemView.findViewById(R.id.pauseButton);
            prodTypeLabel = itemView.findViewById(R.id.productTypeProductsPage);
            priceTxt = itemView.findViewById(R.id.priceTxtSeller);

        }
    }
}
