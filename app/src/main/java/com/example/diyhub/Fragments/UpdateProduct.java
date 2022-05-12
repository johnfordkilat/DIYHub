package com.example.diyhub.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diyhub.Notifications.UserNotif;
import com.example.diyhub.R;
import com.example.diyhub.SellerHomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UpdateProduct extends AppCompatActivity {

    EditText name,quantity,stocks;
    String prodName,prodID;
    Button updateProd;
    ProgressDialog progressDialog;
    FirebaseFirestore dbFirestore;
    String playImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fillust58-7479-01-removebg-preview.png?alt=media&token=63a829e1-660e-47e6-9b26-dc66d8eaac48";
    String pauseImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fpause__video__stop-removebg-preview.png?alt=media&token=dc125631-d226-41e1-91ac-6abf0b97c18d";

    String productID;
    String prodImage;
    String description,material;
    double price,sold;

    int prodStocks,prodQuant;

    EditText descriptionTxt,materialTxt,priceTxt,soldTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        name = findViewById(R.id.updateProdName);
        quantity = findViewById(R.id.updateProdQuantity);
        stocks = findViewById(R.id.updateProdStocks);
        updateProd = findViewById(R.id.updateProduct);
        descriptionTxt = findViewById(R.id.productDescriptionTxtUpdateStandard);
        materialTxt = findViewById(R.id.productMaterialsUsedUpdateStandard);
        priceTxt = findViewById(R.id.productPriceUpdateStandard);
        soldTxt = findViewById(R.id.productSoldUpdateStandard);
        progressDialog = new ProgressDialog(this);
        dbFirestore = FirebaseFirestore.getInstance();


        Bundle extra = getIntent().getExtras();
        if(extra != null)
        {
            prodName = extra.getString("ProductName");
            prodQuant = extra.getInt("ProductQuantity");
            prodStocks = extra.getInt("ProductStocks");
            prodID = extra.getString("ProductID");
            prodImage = extra.getString("ProductImage");
            description = extra.getString("ProductDescription");
            material = extra.getString("ProductMaterial");
            price = extra.getDouble("ProductPrice");
            sold = extra.getDouble("ProductSold");
        }

        productID = prodID;
        name.setText(prodName);
        quantity.setText(String.valueOf(prodQuant));
        stocks.setText(String.valueOf(prodStocks));
        descriptionTxt.setText(description);
        materialTxt.setText(material);
        priceTxt.setText(String.valueOf(price));
        soldTxt.setText(String.valueOf(sold));


        updateProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().trim().isEmpty())
                {
                    name.setError("Product Name is Required");
                    name.requestFocus();
                    return;
                }
                else if(quantity.getText().toString().trim().isEmpty())
                {
                    quantity.setError("Product Quantity is Required");
                    quantity.requestFocus();
                    return;
                }
                else if(stocks.getText().toString().trim().isEmpty())
                {
                    stocks.setError("Product Stocks is Required");
                    stocks.requestFocus();
                    return;
                }
                else if(descriptionTxt.getText().toString().trim().isEmpty())
                {
                    descriptionTxt.setError("Product Stocks is Required");
                    descriptionTxt.requestFocus();
                    return;
                }
                else if(materialTxt.getText().toString().trim().isEmpty())
                {
                    materialTxt.setError("Product Stocks is Required");
                    materialTxt.requestFocus();
                    return;
                }
                else if(priceTxt.getText().toString().trim().isEmpty())
                {
                    priceTxt.setError("Product Stocks is Required");
                    priceTxt.requestFocus();
                    return;
                }
                else if(soldTxt.getText().toString().trim().isEmpty())
                {
                    soldTxt.setError("Product Stocks is Required");
                    soldTxt.requestFocus();
                    return;
                }
                else {
                    String id = prodID;
                    String name1 = name.getText().toString().trim();
                    int quan = Integer.parseInt(quantity.getText().toString().trim());
                    int sto = Integer.parseInt(stocks.getText().toString().trim());
                    String desc = descriptionTxt.getText().toString().trim();
                    String materials = materialTxt.getText().toString().trim();
                    double price1 = Double.parseDouble(priceTxt.getText().toString().trim());
                    double sold1 = Double.parseDouble(soldTxt.getText().toString().trim());

                    updateData(id,name1,quan,sto,desc,materials,price1,sold1);
                }
            }
        });




    }

    private void updateData(String id1, String name1, int quan1, int stocks1, String description, String material, double price, double sold)
    {

        if(quan1 > stocks1)
        {
            quantity.setError("Product Quantity should not be greater than Product Stocks");
            quantity.requestFocus();
            return;
        }
        progressDialog.setTitle("Updating Product");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(quan1 == stocks1)
        {


            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

            DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("SellerProducts").child(firebaseUser.getUid()).child(id1);
            HashMap<String, Object> hashMap4 = new HashMap<>();
            hashMap4.put("ProductName", name1);
            hashMap4.put("ProductQuantity", quan1);
            hashMap4.put("ProductStocks", stocks1);
            hashMap4.put("ProductImage", prodImage);
            hashMap4.put("ProductID", id1);
            hashMap4.put("ProductStatus", "Hold");
            hashMap4.put("ProductStatusImage", playImageStatus);
            hashMap4.put("ProductDescription", description);
            hashMap4.put("ProductMaterial", material);
            hashMap4.put("ProductPrice", price);
            hashMap4.put("ProductSold", sold);
            reference4.updateChildren(hashMap4);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        UserNotif user = snapshot.getValue(UserNotif.class);

                        assert user != null;
                        assert firebaseUser != null;

                        if(!user.getId().equals(firebaseUser.getUid()))
                        {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("ProductName", name1);
                            hashMap.put("ProductQuantity", quan1);
                            hashMap.put("ProductStocks", stocks1);
                            hashMap.put("ProductImage", prodImage);
                            hashMap.put("ProductID", id1);
                            hashMap.put("ProductStatusImage", playImageStatus);
                            hashMap.put("ProductStatus", "Hold");
                            hashMap.put("ProductDescription", description);
                            hashMap.put("ProductMaterial", material);
                            hashMap.put("ProductPrice", price);
                            hashMap.put("ProductSold", sold);
                            reference.child("RestockNotification").child(user.getId()).child(id1).setValue(hashMap);

                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RestockNotification").child(user.getId()).child(id1);
                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("ProductName", name1);
                            hashMap1.put("ProductQuantity", quan1);
                            hashMap1.put("ProductStocks", stocks1);
                            hashMap1.put("ProductStatus", "Hold");
                            hashMap1.put("ProductStatusImage", playImageStatus);
                            hashMap1.put("ProductDescription", description);
                            hashMap1.put("ProductMaterial", material);
                            hashMap1.put("ProductPrice", price);
                            hashMap1.put("ProductSold", sold);
                            reference2.updateChildren(hashMap1);
                            progressDialog.dismiss();

                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
       else if(quan1 < stocks1)
        {

            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");


            DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("SellerProducts").child(firebaseUser.getUid()).child(id1);
            HashMap<String, Object> hashMap4 = new HashMap<>();
            hashMap4.put("ProductName", name1);
            hashMap4.put("ProductQuantity", quan1);
            hashMap4.put("ProductStocks", stocks1);
            hashMap4.put("ProductImage", prodImage);
            hashMap4.put("ProductID", id1);
            hashMap4.put("ProductStatus", "Active");
            hashMap4.put("ProductStatusImage", pauseImageStatus);
            hashMap4.put("ProductDescription", description);
            hashMap4.put("ProductMaterial", material);
            hashMap4.put("ProductPrice", price);
            hashMap4.put("ProductSold", sold);
            reference4.updateChildren(hashMap4);

            Toast.makeText(UpdateProduct.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        UserNotif user = snapshot.getValue(UserNotif.class);

                        assert user != null;
                        assert firebaseUser != null;

                        if(!user.getId().equals(firebaseUser.getUid()))
                        {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("ProductName", name1);
                            hashMap.put("ProductQuantity", quan1);
                            hashMap.put("ProductStocks", stocks1);
                            hashMap.put("ProductImage", prodImage);
                            hashMap.put("ProductID", id1);
                            hashMap.put("ProductStatusImage", pauseImageStatus);
                            hashMap.put("ProductStatus", "Active");
                            hashMap.put("ProductDescription", description);
                            hashMap.put("ProductMaterial", material);
                            hashMap.put("ProductPrice", price);
                            hashMap.put("ProductSold", sold);
                            reference.child("RestockNotification").child(user.getId()).child(id1).setValue(hashMap);


                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RestockNotification").child(user.getId()).child(id1);
                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("ProductName", name1);
                            hashMap1.put("ProductQuantity", quan1);
                            hashMap1.put("ProductStocks", stocks1);
                            hashMap1.put("ProductStatus", "Active");
                            hashMap1.put("ProductStatusImage", playImageStatus);
                            hashMap1.put("ProductDescription", description);
                            hashMap1.put("ProductMaterial", material);
                            hashMap1.put("ProductPrice", price);
                            hashMap1.put("ProductSold", sold);
                            reference2.updateChildren(hashMap1);

                            progressDialog.dismiss();

                            finish();


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


}