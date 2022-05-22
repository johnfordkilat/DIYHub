package com.example.diyhub;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.StrictMode;
import android.util.Log;
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
import com.example.diyhub.Fragments.CustomProductDetails;
import com.example.diyhub.Fragments.StandardProductDetails;
import com.example.diyhub.Fragments.UpdateProduct;
import com.example.diyhub.Notifications.APIService;
import com.example.diyhub.Notifications.CLient;
import com.example.diyhub.Notifications.Data;
import com.example.diyhub.Notifications.MyResponse;
import com.example.diyhub.Notifications.Sender;
import com.example.diyhub.Notifications.Token;
import com.example.diyhub.Notifications.UserNotif;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.MyViewHolder> {

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


    public AllProductsAdapter(){

    }

    APIService apiService = apiService = CLient.getClient("https://fcm.googleapis.com/").create(APIService.class);


    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());

    List<UserNotif> mUsers = new ArrayList<>();
    int pos;
    String dbStatus;
    String imageStatus;

    public AllProductsAdapter(Context context, ArrayList<AllProductsList> list)
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
                    Intent intent = new Intent(context, StandardProductDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ProductID", productsList.getProductID());
                    intent.putExtra("ProductName", productsList.getProductName());
                    intent.putExtra("ProductPrice", productsList.getProductPrice());
                    intent.putExtra("ProductSold", productsList.getProductSold());
                    intent.putExtra("ProductMaterial", productsList.getProductMaterial());
                    intent.putExtra("ProductDescription", productsList.getProductDescription());
                    intent.putExtra("ProductStock", productsList.getProductStocks());
                    context.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(context, CustomProductDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ProductID", productsList.getProductID());
                    intent.putExtra("ProductName", productsList.getProductName());
                    intent.putExtra("ProductPrice", productsList.getProductPrice());
                    intent.putExtra("ProductSold", productsList.getProductSold());
                    intent.putExtra("ProductMaterial", productsList.getProductMaterial());
                    intent.putExtra("ProductDescription", productsList.getProductDescription());
                    intent.putExtra("ProductStock", productsList.getProductStocks());
                    context.startActivity(intent);
                }
            }
        });


        pQuan = productsList.getProductQuantity();
        pStocks = productsList.getProductStocks();

        if(pQuan >= pStocks)
        {
            if(productsList.getProductStatus().equalsIgnoreCase("Hold"))
            {
                color[position]="RED";
            }
            holder.prodQuan.setTextColor(Color.RED);
            holder.prodStocks.setTextColor(Color.RED);

        }
        Glide.with(context).load(list.get(position).getProductStatusImage()).into(holder.pauseButton);



        String sellerEmail = mAuth.getCurrentUser().getEmail();

        holder.pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(color[position]=="RED")
                {
                    //Toast.makeText(context, "Cannot Change the Status of the Product to Active!!!\n Product is out of STOCK!", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alert = new AlertDialog.Builder(context)
                            .setTitle("ERROR CHANGING PRODUCT STATUS!")
                            .setMessage("Cannot Change the Status of the Product to Active!!!\n Product is out of STOCK!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    alert.show();
                }
                else
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context)
                            .setTitle("UPDATE PRODUCT STATUS")
                            .setMessage("Do you want to update the status of the product?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressDialog = new ProgressDialog(context);
                                    progressDialog.setTitle("Updating Product Status");
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();

                                    String sellerEmail = mAuth.getCurrentUser().getEmail();

                                    if(productsList.getProductStatus().equals("Hold"))
                                    {

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(fUser.getUid()).child(list.get(position).getProductID());
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("ProductStatus", "Active");
                                        map.put("ProductStatusImage", pauseImageStatus);
                                        reference.updateChildren(map);
                                        Toast.makeText(context, "Product is NOW ACTIVE!!", Toast.LENGTH_SHORT).show();
                                        //Glide.with(context).load(list.get(position).getProductImageStatus()).into(holder.pauseButton);
                                        holder.pauseButton.setImageBitmap(getBitmapFromURL(pauseImageStatus));
                                        productsList.setProductStatusImage(pauseImageStatus);
                                        productsList.setProductStatus("Active");
                                        dbStatus = "Active";
                                        imageStatus = pauseImageStatus;
                                        pos = position;
                                        readUsers("Product is NOW ACTIVE!!");
                                        progressDialog.dismiss();

                                    }
                                    else if(productsList.getProductStatus().equals("Active"))
                                    {

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(fUser.getUid()).child(list.get(position).getProductID());
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("ProductStatus", "Hold");
                                        map.put("ProductStatusImage", playImageStatus);
                                        reference.updateChildren(map);
                                        Toast.makeText(context, "Product is ON HOLD!!", Toast.LENGTH_SHORT).show();
                                        //Glide.with(context).load(list.get(position).getProductImageStatus()).into(holder.pauseButton);
                                        holder.pauseButton.setImageBitmap(getBitmapFromURL(playImageStatus));
                                        productsList.setProductStatusImage(playImageStatus);
                                        productsList.setProductStatus("Hold");
                                        dbStatus = "Hold";
                                        imageStatus = playImageStatus;
                                        pos = position;
                                        readUsers("Product is ON HOLD!!");
                                        progressDialog.dismiss();
                                    }
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    alert.show();
                }



            }
        });




        holder.updateProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("UPDATE A PRODUCT")
                        .setMessage("Do you want to update the product?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String sellerEmail = mAuth.getCurrentUser().getEmail();
                                String id = list.get(position).getProductID();
                                String name = list.get(position).getProductName();
                                int quantity = list.get(position).getProductQuantity();
                                int stocks = list.get(position).getProductStocks();
                                String prodImage = list.get(position).getProductImage();
                                String description = list.get(position).getProductDescription();
                                String material = list.get(position).getProductMaterial();
                                double price = list.get(position).getProductPrice();
                                double sold = list.get(position).getProductSold();

                                Intent intent = new Intent(context, UpdateProduct.class);
                                intent.putExtra("ProductName", name);
                                intent.putExtra("ProductQuantity", quantity);
                                intent.putExtra("ProductStocks", stocks);
                                intent.putExtra("ProductID", id);
                                intent.putExtra("ProductImage", prodImage);
                                intent.putExtra("EmailSeller", sellerEmail);
                                intent.putExtra("ProductDescription", description);
                                intent.putExtra("ProductMaterial", material);
                                intent.putExtra("ProductPrice", price);
                                intent.putExtra("ProductSold", sold);

                                context.startActivity(intent);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();



            }
        });

        if(productsList.getProductType().equalsIgnoreCase("Standard"))
        {
            holder.prodTypeLabel.setText("Standard Product");
        }
        else
        {
            holder.prodTypeLabel.setText("Customizable Product");
        }



        holder.deleteProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setTitle("CANNOT UNDO AFTER DELETION!!!")
                        .setMessage("Do you want to delete the product?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog = new ProgressDialog(context);
                                progressDialog.setCancelable(false);
                                progressDialog.setTitle("Deleting Product....");
                                progressDialog.show();
                                //Toast.makeText(context,   "Product ID: " + productsList.getProductID(), Toast.LENGTH_SHORT).show();

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(fUser.getUid()).child(list.get(position).getProductID());
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        reference.removeValue();
                                        list.remove(position);
                                        Toast.makeText(context, "Product Deleted", Toast.LENGTH_SHORT).show();
                                        notifyItemRemoved(position);
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alert.show();

            }
        });
    }

    private void sendNotification(String receiver, String username, String msg) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fUser.getUid(), R.drawable.diy, username+": "+msg, "New Message", receiver);
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200)
                                    {
                                        Log.d("Response123", fUser.getUid() + username+": "+msg +"New Message"+ receiver);
                                        if(response.body().success != 1){
                                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers(String status) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    UserNotif user = snapshot.getValue(UserNotif.class);

                    assert user != null;
                    assert firebaseUser != null;

                    if(!user.getId().equals(firebaseUser.getUid()))
                    {
                        mUsers.add(user);
                        sendNotification(user.getId(),
                                user.getUsername(), status);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("ProductName", list.get(pos).getProductName());
                        hashMap.put("ProductQuantity", list.get(pos).getProductQuantity());
                        hashMap.put("ProductStocks", list.get(pos).getProductStocks());
                        hashMap.put("ProductImage", list.get(pos).getProductImage());
                        hashMap.put("ProductID", list.get(pos).getProductID());
                        hashMap.put("ProductImageStatus", list.get(pos).getProductStatusImage());
                        hashMap.put("ProductStatus", list.get(pos).getProductStatus());
                        reference.child("RestockNotification").child(user.getId()).child(list.get(pos).getProductID()).setValue(hashMap);

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RestockNotification").child(user.getId()).child(list.get(pos).getProductID());
                        HashMap<String, Object> hashMap1 = new HashMap<>();
                        hashMap1.put("ProductStatus", dbStatus);
                        hashMap1.put("ProductImageStatus", imageStatus);
                        reference2.updateChildren(hashMap1);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
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
