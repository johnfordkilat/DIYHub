package com.example.diyhub.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyhub.AllProductsAdapter;
import com.example.diyhub.AllProductsList;
import com.example.diyhub.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddStandardProduct extends AppCompatActivity {




    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    EditText productName,productQuantity,productStocks;
    String playImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/johnfordtapales8%40gmail.com%2Fillust58-7479-01-removebg-preview.png?alt=media&token=a322e775-e3fd-4fb3-8d77-3767a348120d";
    String pauseImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/johnfordtapales8%40gmail.com%2Fpause__video__stop-removebg-preview.png?alt=media&token=ec3433a8-9579-4aca-8faa-6fe1c58d8474";
    String id;
    String cutid;

    Button addVariations;

    EditText prodDescriptionTxt,prodMaterialTxt,prodPriceTxt,prodSoldTxt;
    String shopName = "";
    String shopAddress = "";

    Spinner addCategorySpinner;
    ArrayAdapter<String> adapter;
    List<String> list;
    String prodCategory = "";

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
        addCategorySpinner = findViewById(R.id.customProductSpinnerAddStandardProduct);

        list = new ArrayList<String>();
        list.add(0, "Choose Product Category");
        list.add(1, "Birthdays");
        list.add(2, "Wedding");
        list.add(3, "Christening");
        list.add(4, "Graduation");



        //Payment Spinner
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list)
        {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        addCategorySpinner.setAdapter(adapter);

        addCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    prodCategory = list.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        id = UUID.randomUUID().toString();
        cutid = id.substring(0,11);

        getShopName();

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
                    prodDescriptionTxt.setError("Product Description is Required");
                    prodDescriptionTxt.requestFocus();
                    return;
                }
                else if(prodMaterialTxt.getText().toString().trim().isEmpty())
                {
                    prodMaterialTxt.setError("Product Material is Required");
                    prodMaterialTxt.requestFocus();
                    return;
                }
                else if(prodPriceTxt.getText().toString().trim().isEmpty())
                {
                    prodPriceTxt.setError("Product Price is Required");
                    prodPriceTxt.requestFocus();
                    return;
                }
                else if(prodSoldTxt.getText().toString().trim().isEmpty())
                {
                    prodSoldTxt.setError("Product Sold is Required");
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
                        sellerProductsfb.put("SellerID",user.getUid());
                        sellerProductsfb.put("ShopName",shopName);
                        sellerProductsfb.put("ProductBookFrom",shopAddress);
                        sellerProductsfb.put("ProductRating",4.5);
                        sellerProductsfb.put("ProductShippingFee",60);
                        sellerProductsfb.put("ProductAdditionalFee",0);




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
                        sellerProductsfb.put("SellerID",user.getUid());
                        sellerProductsfb.put("ShopName",shopName);
                        sellerProductsfb.put("ProductBookFrom",shopAddress);
                        sellerProductsfb.put("ProductRating",4.5);
                        sellerProductsfb.put("ProductShippingFee",60);
                        sellerProductsfb.put("ProductAdditionalFee",0);





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

    private void getShopName()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Shops");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ShopsList shopsList = snapshot.getValue(ShopsList.class);
                    if(shopsList.getSellerID().equalsIgnoreCase(user.getUid()))
                    {
                        shopName = shopsList.getShopName();
                        shopAddress = shopsList.getShopAddress();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}