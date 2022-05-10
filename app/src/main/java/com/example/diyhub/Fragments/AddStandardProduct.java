package com.example.diyhub.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyhub.AllProductsAdapter;
import com.example.diyhub.AllProductsList;
import com.example.diyhub.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddStandardProduct extends AppCompatActivity {




    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    EditText productName,productQuantity,productStocks;
    String playImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fillust58-7479-01-removebg-preview.png?alt=media&token=63a829e1-660e-47e6-9b26-dc66d8eaac48";
    String pauseImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fpause__video__stop-removebg-preview.png?alt=media&token=dc125631-d226-41e1-91ac-6abf0b97c18d";
    String id;
    String cutid;

    Button addVariations;

    EditText prodDescriptionTxt,prodMaterialTxt,prodPriceTxt,prodSoldTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_standard_product);

        productName = findViewById(R.id.addProdName);
        productQuantity = findViewById(R.id.addProdQuantity);
        productStocks = findViewById(R.id.addProdStocks);
        addVariations = findViewById(R.id.addVariationsStandard);
        prodDescriptionTxt = findViewById(R.id.productDescriptionTxtAddStandard);
        prodMaterialTxt = findViewById(R.id.productMaterialsUsedAddStandard);
        prodPriceTxt = findViewById(R.id.productPriceAddStandard);
        prodSoldTxt = findViewById(R.id.productSoldAddStandard);


        id = UUID.randomUUID().toString();
        cutid = id.substring(0,11);



        addVariations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prodName = productName.getText().toString().trim().isEmpty() ? "" : productName.getText().toString().trim();
                String pQuans = productQuantity.getText().toString().trim().isEmpty() ? "0" : productQuantity.getText().toString().trim();
                String pStocks = productStocks.getText().toString().trim().isEmpty() ? "0" : productStocks.getText().toString().trim();
                String description = prodDescriptionTxt.getText().toString().trim();
                String materialUsed = prodMaterialTxt.getText().toString().trim();
                double price = prodPriceTxt.getText().toString().trim().isEmpty() ? 0 : Double.parseDouble(prodPriceTxt.getText().toString().trim());
                double sold = prodSoldTxt.getText().toString().trim().isEmpty() ? 0 : Double.parseDouble(prodSoldTxt.getText().toString().trim());

                int pQUan = Integer.parseInt(pQuans);
                int pSTock = Integer.parseInt(pStocks);

                if(pQUan== 0)
                {
                    productQuantity.setError("Product Quantity should be greater than 0");
                    productQuantity.requestFocus();
                }
                else if(pSTock == 0)
                {
                    productStocks.setError("Product Stocks should greater than 0");
                    productStocks.requestFocus();
                }
                else if(productName.getText().toString().trim().isEmpty())
                {
                    productName.setError("Product Name is Required");
                    productName.requestFocus();
                    return;
                }
                else if(productQuantity.getText().toString().trim().isEmpty())
                {
                    productQuantity.setError("Product Quantity is Required");
                    productQuantity.requestFocus();
                    return;
                }
                else if(productStocks.getText().toString().trim().isEmpty())
                {
                    productStocks.setError("Product Stocks is Required");
                    productStocks.requestFocus();
                    return;
                }
                else if(prodDescriptionTxt.getText().toString().trim().isEmpty())
                {
                    prodDescriptionTxt.setError("Product Stocks is Required");
                    prodDescriptionTxt.requestFocus();
                    return;
                }
                else if(prodMaterialTxt.getText().toString().trim().isEmpty())
                {
                    prodMaterialTxt.setError("Product Stocks is Required");
                    prodMaterialTxt.requestFocus();
                    return;
                }
                else if(prodPriceTxt.getText().toString().trim().isEmpty())
                {
                    prodPriceTxt.setError("Product Stocks is Required");
                    prodPriceTxt.requestFocus();
                    return;
                }
                else if(prodSoldTxt.getText().toString().trim().isEmpty())
                {
                    prodSoldTxt.setError("Product Stocks is Required");
                    prodSoldTxt.requestFocus();
                    return;
                }
                else if(pQUan > pSTock)
                {
                    productQuantity.setError("Product Quantity should not be greater than Product Stocks");
                    productQuantity.requestFocus();
                    return;
                }
                else {

                    if(pQUan == pSTock)
                    {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String,Object> sellerProductsfb = new HashMap<>();
                        sellerProductsfb.put("ProductName", prodName);
                        sellerProductsfb.put("ProductQuantity", pQUan);
                        sellerProductsfb.put("ProductStocks", pSTock);
                        sellerProductsfb.put("ProductID", cutid);
                        sellerProductsfb.put("ProductStatus", "Hold");
                        sellerProductsfb.put("ProductType", "Standard");
                        sellerProductsfb.put("ProductStatusImage", playImageStatus);
                        sellerProductsfb.put("ProductDescription", description);
                        sellerProductsfb.put("ProductMaterial", materialUsed);
                        sellerProductsfb.put("ProductPrice", price);
                        sellerProductsfb.put("ProductSold", sold);

                        reference.child("SellerProducts").child(user.getUid()).child(cutid).setValue(sellerProductsfb);
                    }
                    else
                    {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String,Object> sellerProductsfb = new HashMap<>();
                        sellerProductsfb.put("ProductName", prodName);
                        sellerProductsfb.put("ProductQuantity", pQUan);
                        sellerProductsfb.put("ProductStocks", pSTock);
                        sellerProductsfb.put("ProductID", cutid);
                        sellerProductsfb.put("ProductStatus", "Active");
                        sellerProductsfb.put("ProductStatusImage", pauseImageStatus);
                        sellerProductsfb.put("ProductType", "Standard");
                        sellerProductsfb.put("ProductDescription", description);
                        sellerProductsfb.put("ProductMaterial", materialUsed);
                        sellerProductsfb.put("ProductPrice", price);
                        sellerProductsfb.put("ProductSold", sold);
                        reference.child("SellerProducts").child(user.getUid()).child(cutid).setValue(sellerProductsfb);
                    }

                    Intent intent = new Intent(AddStandardProduct.this, AddVariationsStandardPage.class);
                    intent.putExtra("itemid", cutid);
                    intent.putExtra("ProductName", prodName);
                    intent.putExtra("ProductStocks", pSTock);
                    intent.putExtra("ProductQuantity", pQUan);
                    startActivity(intent);
                }

            }
        });



    }
}