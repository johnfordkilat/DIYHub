package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diyhub.R;
import com.example.diyhub.SellerHomePage;
import com.example.diyhub.UploadListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddVariationsCustomizePage extends AppCompatActivity {

    Spinner varSpinner;

    StandardProductVariationAdapter adapter;

    List<StandardProductVariationList> list;

    TextView addVarButton;

    int position = 0;

    String allList[];

    Button submitBooking;
    EditText optionTxtBooking;

    Dialog varDialog;

    int count = 0;


    ImageView varImage;
    ImageButton selectImageButton;
    private static final int SELECT_PHOTOGOV_VARIATION = 1;
    private static final int SELECT_PHOTOGOV_PRODUCT = 2;


    Uri imageUriProduct;

    ProgressDialog progressDialog;

    private ArrayList<Uri> ImageListVariation = new ArrayList<Uri>();
    private ArrayList<Uri> ImageListProduct = new ArrayList<Uri>();

    int uploadsVariation = 0;
    int uploadsProduct = 0;


    String itemid;
    int stocks;
    int quantity;
    String itemname;

    Button uploadProduct;

    String playImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fillust58-7479-01-removebg-preview.png?alt=media&token=63a829e1-660e-47e6-9b26-dc66d8eaac48";
    String pauseImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fpause__video__stop-removebg-preview.png?alt=media&token=dc125631-d226-41e1-91ac-6abf0b97c18d";

    ImageView prodImage;

    int clicked = 0;
    Button addProduct;

    Uri imageUriVariation;

    StandardProductVariationList listVar;

    StorageReference firebaseStorage;
    List<Uri> productImages;


    int index = 0;

    boolean present = false;

    String todelete;
    TextView deleteVarButton;

    RecyclerView productImagesRecycler;

    List<String> fileNameList;
    List<String> fileDoneList;

    UploadListAdapter uploadListAdapter;

    StorageReference mStorage;
    List<Uri> fileImageList;
    List<Integer> counterList;
    Uri singleProduct;

    int cnt;

    int upload_count = 0;

    String fileName;

    List<String> fileNameListVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_variations_customize_page);

        varSpinner = findViewById(R.id.spinnerAddVariationsCustom);
        addVarButton = findViewById(R.id.addVariationButtonCustom);
        uploadProduct = findViewById(R.id.addProductButtonCustom);
        //prodImage = findViewById(R.id.addProductImageView);
        addProduct = findViewById(R.id.addProductImageButtonCustom);
        deleteVarButton = findViewById(R.id.removeVariationButtonCustom);
        productImagesRecycler = findViewById(R.id.uploadProductImagesCustomPage);

        mStorage = FirebaseStorage.getInstance().getReference();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));
        productImagesRecycler.addItemDecoration(dividerItemDecoration);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        fileImageList = new ArrayList<>();
        counterList = new ArrayList<>();
        listVar = new StandardProductVariationList();
        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList, fileImageList, counterList);

        fileNameListVar = new ArrayList<>();






        productImagesRecycler.setLayoutManager(new LinearLayoutManager(this));
        productImagesRecycler.setHasFixedSize(true);
        productImagesRecycler.setAdapter(uploadListAdapter);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            itemid = extras.getString("itemid");
            itemname = extras.getString("ProductName");
            stocks = extras.getInt("ProductStocks");
            quantity = extras.getInt("ProductQuantity");
        }


        list = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(itemid).child("Variations-Customized");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    StandardProductVariationList standardProductVariationList = snapshot.getValue(StandardProductVariationList.class);
                    list.add(standardProductVariationList);
                }

                adapter = new StandardProductVariationAdapter(AddVariationsCustomizePage.this, list);
                varSpinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        varSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                todelete = list.get(position).getVariationName();
                Toast.makeText(AddVariationsCustomizePage.this, todelete, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        deleteVarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder removeVar = new AlertDialog.Builder(AddVariationsCustomizePage.this);
                removeVar.setTitle("Delete Confirmation");
                removeVar.setMessage("Are you sure you want to delete Color - "+ todelete+" ?");
                removeVar.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(itemid).child("Variations-Customized").child(todelete);
                        reference1.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddVariationsCustomizePage.this, "Variation Deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                removeVar.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                removeVar.create().show();
            }
        });



        addVarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderBooking = new AlertDialog.Builder(AddVariationsCustomizePage.this);
                builderBooking.setTitle("Add Variation");

                View view1 = getLayoutInflater().inflate(R.layout.layout_dialog_variation_standard, null);
                optionTxtBooking = view1.findViewById(R.id.setPaymentOptionTxt);
                submitBooking = view1.findViewById(R.id.submitOption);
                varImage = view1.findViewById(R.id.variationImageViewStandard);
                selectImageButton = view1.findViewById(R.id.selectionVariationImageButtonStandard);
                builderBooking.setView(view1);

                selectImageButton.setOnClickListener(new View.OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent,SELECT_PHOTOGOV_VARIATION);
                    }
                });

                submitBooking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String data = optionTxtBooking.getText().toString().trim();
                        if(data.isEmpty())
                        {
                            optionTxtBooking.setError("Cannot be Empty");
                            optionTxtBooking.requestFocus();
                        }
                        else
                        {
                            if(ImageListVariation.size() == 0)
                            {
                                Toast.makeText(AddVariationsCustomizePage.this, "Please select Image", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if(list.size() == 0)
                                {
                                    uploadImageVariation(data.toUpperCase());
                                    varDialog.dismiss();
                                    deleteVarButton.setVisibility(View.VISIBLE);
                                    varImage = null;
                                    imageUriVariation = null;
                                }
                                else
                                {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(itemid).child("Variations-Customized");
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            list.clear();
                                            for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                            {
                                                StandardProductVariationList dbList = snapshot.getValue(StandardProductVariationList.class);
                                                list.add(dbList);
                                                if(data.equalsIgnoreCase(dbList.getVariationName()))
                                                {
                                                    Toast.makeText(getApplicationContext(), "Variation Already Exist!", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                else
                                                {
                                                    uploadImageVariation(data.toUpperCase());
                                                    varDialog.dismiss();
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


                    }
                });
                varDialog = builderBooking.create();
                varDialog.show();
            }
        });


        uploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child(user.getEmail());
                for(upload_count = 0; upload_count < fileImageList.size(); upload_count++)
                {

                    Uri eachImage = fileImageList.get(upload_count);
                    final StorageReference ImageName = ImageFolder.child("Seller-Products").child(itemid).child(fileName);

                    ImageName.putFile(eachImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = String.valueOf(uri);
                                    storeLink(url);
                                }
                            });
                        }
                    });
                }

                 */
                uploadImage(itemname,quantity,stocks,user.getEmail());
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clicked++;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent,SELECT_PHOTOGOV_PRODUCT);


            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTOGOV_VARIATION && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUriVariation = data.getData();
            varImage.setImageURI(imageUriVariation);
            ImageListVariation.add(imageUriVariation);
            String fileName = getFileName(imageUriVariation);
            fileNameListVar.add(fileName);

        }

        if(requestCode == SELECT_PHOTOGOV_PRODUCT && resultCode == RESULT_OK)
        {
            if(data.getClipData() != null)
            {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                ImageListProduct.add(data.getClipData().getItemAt(0).getUri());
                int totalData = data.getClipData().getItemCount();
                int currentSelected = 0;
                while(currentSelected < totalData)
                {
                    singleProduct = data.getClipData().getItemAt(currentSelected).getUri();

                    int i = fileDoneList.size() > 0 ? fileDoneList.size() : 0;
                    cnt = i+1;

                    fileName = getFileName(singleProduct);

                    fileNameList.add(fileName);
                    fileDoneList.add("Uploading");
                    fileImageList.add(singleProduct);
                    counterList.add(cnt);
                    uploadListAdapter.notifyDataSetChanged();
                    currentSelected = currentSelected + 1;

                    StorageReference fileToUpload = mStorage.child(user.getEmail()).child("Seller-Products").child(itemid).child(fileName);

                    final int finalI = i;
                    fileToUpload.putFile(singleProduct).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");

                            uploadListAdapter.notifyDataSetChanged();

                            fileToUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgUri = String.valueOf(uri);
                                    storeLink(imgUri);
                                }
                            });



                        }
                    });


                    i++;

                }
            }
            else if(data.getData() != null)
            {
                ImageListProduct.add(data.getData());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                int i = fileDoneList.size() > 0 ? fileDoneList.size() : 0;
                cnt = i+1;

                Uri fileUri = data.getData();

                String fileName = getFileName(fileUri);

                fileNameList.add(fileName);
                fileDoneList.add("Uploading");
                fileImageList.add(fileUri);
                counterList.add(cnt);
                uploadListAdapter.notifyDataSetChanged();

                StorageReference fileToUpload = mStorage.child(user.getEmail()).child("Seller-Products").child(itemid).child(fileName);

                final int finalI = i;
                fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileDoneList.remove(finalI);
                        fileDoneList.add(finalI, "done");

                        uploadListAdapter.notifyDataSetChanged();

                        fileToUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imgUri = String.valueOf(uri);
                                storeLink(imgUri);
                            }
                        });



                    }
                });

                i++;
            }
        }
    }

    private void storeLink(String url) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> map = new HashMap<>();
        map.put("ProductImage", url);
        reference.child("SellerProducts").child(user.getUid()).child(itemid).child("ProductImages").push().setValue(map);
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri)
    {
        String result = null;
        if(uri.getScheme().equals("content"))
        {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try{
                if(cursor != null && cursor.moveToFirst())
                {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }

            } finally {
                cursor.close();
            }
        }
        if(result == null)
        {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut != -1)
            {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void uploadImage(String pN, int pQ, int pS, String selleEm)
    {

        String prodName=pN;
        String sellerEmail = selleEm;

        if(clicked == 0 && imageUriProduct == null)
        {
            Toast.makeText(AddVariationsCustomizePage.this, "Please choose Product Image", Toast.LENGTH_SHORT).show();
            return;
        }

        else if(pQ == pS)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Uploading Product....");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance(); // add this
            firebaseStorage = storage.getReference(); // and this
            StorageReference ImageFolder =  firebaseStorage.child(sellerEmail);
            for (uploadsProduct=0; uploadsProduct < ImageListProduct.size(); uploadsProduct++) {
                Uri Image  = ImageListProduct.get(uploadsProduct);
                StorageReference imagename = ImageFolder.child("Seller-Products/"+Image.getLastPathSegment());

                imagename.putFile(ImageListProduct.get(uploadsProduct)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String url = String.valueOf(uri);
                                progressDialog.dismiss();
                                Log.d("DownloadUrl", url);

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                Map<String,Object> sellerProductsfb = new HashMap<>();
                                sellerProductsfb.put("ProductImage", url);
                                sellerProductsfb.put("ProductName", prodName);
                                sellerProductsfb.put("ProductQuantity", pQ);
                                sellerProductsfb.put("ProductStocks", pS);
                                sellerProductsfb.put("ProductID", itemid);
                                sellerProductsfb.put("ProductStatus", "Hold");
                                sellerProductsfb.put("ProductType", "Customized");
                                sellerProductsfb.put("ProductStatusImage", playImageStatus);
                                reference.child("SellerProducts").child(user.getUid()).child(itemid).updateChildren(sellerProductsfb);
                                Toast.makeText(getApplicationContext(), "Product Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddVariationsCustomizePage.this, SellerHomePage.class);
                                startActivity(intent);
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

            FirebaseStorage storage = FirebaseStorage.getInstance(); // add this
            firebaseStorage = storage.getReference(); // and this
            StorageReference ImageFolder =  firebaseStorage.child(sellerEmail);
            for (uploadsProduct=0; uploadsProduct < ImageListProduct.size(); uploadsProduct++) {
                Uri Image  = ImageListProduct.get(uploadsProduct);
                StorageReference imagename = ImageFolder.child("Seller-Products/"+Image.getLastPathSegment());

                imagename.putFile(ImageListProduct.get(uploadsProduct)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String url = String.valueOf(uri);
                                progressDialog.dismiss();
                                Log.d("DownloadUrl", url);


                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                Map<String,Object> sellerProductsfb = new HashMap<>();
                                sellerProductsfb.put("ProductImage", url);
                                sellerProductsfb.put("ProductName", prodName);
                                sellerProductsfb.put("ProductQuantity", pQ);
                                sellerProductsfb.put("ProductStocks", pS);
                                sellerProductsfb.put("ProductID", itemid);
                                sellerProductsfb.put("ProductStatus", "Active");
                                sellerProductsfb.put("ProductStatusImage", pauseImageStatus);
                                sellerProductsfb.put("ProductType", "Customized");
                                reference.child("SellerProducts").child(user.getUid()).child(itemid).updateChildren(sellerProductsfb);
                                Toast.makeText(getApplicationContext(), "Product Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddVariationsCustomizePage.this, SellerHomePage.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                    }
                });
            }
        }
    }



    private void uploadImageVariation(String varNameLabel) {
        Log.d("UPLOADINGERROR", "IMAGEERROR");

        Toast.makeText(AddVariationsCustomizePage.this, "Added: " + varNameLabel, Toast.LENGTH_SHORT).show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseStorage storage = FirebaseStorage.getInstance(); // add this
        firebaseStorage = storage.getReference(); // and this
        StorageReference ImageFolder =  firebaseStorage.child(user.getEmail());
        for (uploadsVariation=0; uploadsVariation < ImageListVariation.size(); uploadsVariation++) {
            Uri Image  = ImageListVariation.get(uploadsVariation);
            StorageReference imagename = ImageFolder.child("Seller-Products").child("Variations-Customized").child(fileNameListVar.get(uploadsVariation)+".jpeg");

            imagename.putFile(Image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = String.valueOf(uri);

                            Log.d("DownloadUrl", url);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> map = new HashMap<>();
                            map.put("variationImage", url);
                            map.put("variationName", varNameLabel);
                            reference.child("SellerProducts").child(user.getUid()).child(itemid).child("Variations-Customized").child(varNameLabel).updateChildren(map);
                            ImageListVariation.clear();
                            //reference.child("Products").child(varNameLabel).setValue(map);

                        }
                    });
                }
            });
        }
    }

}