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



    Button addProduct;
    TextView all,res,hold;
    int count=0;
    String emailSeller;
    RecyclerView recyclerView;
    FirebaseFirestore dbFirestore;
    AllProductsAdapter allProductsAdapter;
    ArrayList<AllProductsList> allProductsLists;
    ProgressDialog progressDialog;
    Button uploadProduct;

    Uri imageUri1,imageUri2,imageUri3;
    StorageReference storageReference1,storageReference2,storageReference3;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    int index = 0;
    private int uploads = 0;
    private int success = 0;
    ImageView profPic;
    String usernameSeller;
    FirebaseAuth mAuth;
    String myUri;
    private DatabaseReference databaseReference;
    String value1,value2,value3,value4,value5,value6,value7,value8,value9,value10,value11,value12;
    TextView shopN,locSeller,phoneSeller;
    String locSell,phoneSell;
    ImageView products,orders,stats;
    EditText productName,productQuantity,productStocks;
    private static final int SELECT_PHOTOGOV = 1;
    int dialog = 0;
    TextView noProduct;
    ImageView prodImage;
    int clicked=0;
    String playImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fillust58-7479-01-removebg-preview.png?alt=media&token=63a829e1-660e-47e6-9b26-dc66d8eaac48";
    String pauseImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fpause__video__stop-removebg-preview.png?alt=media&token=dc125631-d226-41e1-91ac-6abf0b97c18d";


    String id;
    String cutid;

    Button addVariations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_standard_product);

        dbFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        productName = findViewById(R.id.addProdName);
        productQuantity = findViewById(R.id.addProdQuantity);
        productStocks = findViewById(R.id.addProdStocks);
        addVariations = findViewById(R.id.addVariationsStandard);


        id = UUID.randomUUID().toString();
        cutid = id.substring(0,11);



        addVariations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prodName = productName.getText().toString().trim().isEmpty() ? "" : productName.getText().toString().trim();
                String pQuans = productQuantity.getText().toString().trim().isEmpty() ? "0" : productQuantity.getText().toString().trim();
                String pStocks = productStocks.getText().toString().trim().isEmpty() ? "0" : productStocks.getText().toString().trim();
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
                        sellerProductsfb.put("ProductQuantity", String.valueOf(pQUan));
                        sellerProductsfb.put("ProductStocks", String.valueOf(pSTock));
                        sellerProductsfb.put("ProductID", cutid);
                        sellerProductsfb.put("ProductStatus", "Hold");
                        sellerProductsfb.put("ProductType", "Standard");
                        sellerProductsfb.put("ProductStatusImage", playImageStatus);
                        reference.child("SellerProducts").child(user.getUid()).child(cutid).setValue(sellerProductsfb);
                    }
                    else
                    {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String,Object> sellerProductsfb = new HashMap<>();
                        sellerProductsfb.put("ProductName", prodName);
                        sellerProductsfb.put("ProductQuantity", String.valueOf(pQUan));
                        sellerProductsfb.put("ProductStocks", String.valueOf(pSTock));
                        sellerProductsfb.put("ProductID", cutid);
                        sellerProductsfb.put("ProductStatus", "Active");
                        sellerProductsfb.put("ProductStatusImage", pauseImageStatus);
                        sellerProductsfb.put("ProductType", "Standard");
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