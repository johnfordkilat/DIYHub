package com.example.diyhub.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyhub.AllProductsAdapter;
import com.example.diyhub.AllProductsList;
import com.example.diyhub.Notifications.UserNotif;
import com.example.diyhub.R;
import com.example.diyhub.SellerHomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.StringUtils;
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
import java.util.Map;
import java.util.UUID;

public class AddProduct extends AppCompatActivity {



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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        addProduct = findViewById(R.id.addProductImageButton);
        prodImage = findViewById(R.id.addProductImageView);
        dbFirestore = FirebaseFirestore.getInstance();
        uploadProduct = findViewById(R.id.addProductButton);
        mAuth = FirebaseAuth.getInstance();
        productName = findViewById(R.id.addProdName);
        productQuantity = findViewById(R.id.addProdQuantity);
        productStocks = findViewById(R.id.addProdStocks);



        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked++;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOGOV);
            }
        });


        uploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(productName.getText().toString().trim().isEmpty())
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
                else {
                    String pName = productName.getText().toString();
                    String pQuan = productQuantity.getText().toString();
                    String pStocks = productStocks.getText().toString();
                    String sellerEmail = mAuth.getCurrentUser().getEmail();
                    uploadImage(pName,pQuan,pStocks,sellerEmail);
                }



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTOGOV && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri1 = data.getData();
            ImageList.add(imageUri1);
            prodImage.setImageURI(imageUri1);
        }
    }

    private void uploadImage(String pN, String pQ, String pS, String selleEm)
    {
        String prodName=pN;
        String prodQuan=pQ;
        String prodStocks=pS;
        String sellerEmail = selleEm;



        Log.d("drawableImage", String.valueOf(imageUri1) + clicked);
        int pQUan = Integer.parseInt(prodQuan);
        int pSTock = Integer.parseInt(prodStocks);
        if(pQUan > pSTock)
        {
            productQuantity.setError("Product Quantity should not be greater than Product Stocks");
            productQuantity.requestFocus();
            return;
        }
        else if(clicked == 0 && imageUri1 == null)
        {
            Toast.makeText(this, "Please choose Product Image", Toast.LENGTH_SHORT).show();
            prodImage.requestFocus();
            return;
        }

        else if(pQUan == pSTock)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Uploading Product....");
            progressDialog.show();

            StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child(sellerEmail);
            for (uploads=0; uploads < ImageList.size(); uploads++) {
                Uri Image  = ImageList.get(uploads);
                StorageReference imagename = ImageFolder.child("Seller-Products/"+Image.getLastPathSegment());

                imagename.putFile(ImageList.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String url = String.valueOf(uri);
                                progressDialog.dismiss();
                                Log.d("DownloadUrl", url);
                                id = UUID.randomUUID().toString();
                                cutid = id.substring(0,11);

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                Map<String,Object> sellerProductsfb = new HashMap<>();
                                sellerProductsfb.put("ProductImage", url);
                                sellerProductsfb.put("ProductName", prodName);
                                sellerProductsfb.put("ProductQuantity", prodQuan);
                                sellerProductsfb.put("ProductStocks", prodStocks);
                                sellerProductsfb.put("ProductID", cutid);
                                sellerProductsfb.put("ProductStatus", "Hold");
                                sellerProductsfb.put("ProductStatusImage", playImageStatus);
                                reference.child("SellerProducts").child(user.getUid()).child(cutid).setValue(sellerProductsfb);
                                Toast.makeText(getApplicationContext(), "Product Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
            }
        }

        else
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Uploading Product....");
            progressDialog.show();

            StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child(sellerEmail);
            for (uploads=0; uploads < ImageList.size(); uploads++) {
                Uri Image  = ImageList.get(uploads);
                StorageReference imagename = ImageFolder.child("Seller-Products/"+Image.getLastPathSegment());

                imagename.putFile(ImageList.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String url = String.valueOf(uri);
                                progressDialog.dismiss();
                                Log.d("DownloadUrl", url);
                                id = UUID.randomUUID().toString();
                                cutid = id.substring(0,11);

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                Map<String,Object> sellerProductsfb = new HashMap<>();
                                sellerProductsfb.put("ProductImage", url);
                                sellerProductsfb.put("ProductName", prodName);
                                sellerProductsfb.put("ProductQuantity", prodQuan);
                                sellerProductsfb.put("ProductStocks", prodStocks);
                                sellerProductsfb.put("ProductID", cutid);
                                sellerProductsfb.put("ProductStatus", "Active");
                                sellerProductsfb.put("ProductStatusImage", pauseImageStatus);
                                reference.child("SellerProducts").child(user.getUid()).child(cutid).setValue(sellerProductsfb);
                                Toast.makeText(getApplicationContext(), "Product Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
            }
        }
    }


}