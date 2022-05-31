package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diyhub.R;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Locale;
import java.util.Map;

public class AddCustomSpecificationPageSeller extends AppCompatActivity {

    Button addCategoryButton;
    EditText txtBox;
    List<AddCustomSpecsSellerList> listCat1;
    List<AddCustomSpecsSellerList> listCat2;
    List<AddCustomSpecsSellerList> listCat3;

    AddCustomSpecsSellerAdapter adapterCat1;
    AddCustomSpecsSellerAdapter adapterCat2;
    AddCustomSpecsSellerAdapter adapterCat3;

    Dialog varDialogCat1;
    Dialog varDialogCat2;
    Dialog varDialogCat3;

    EditText optionTxtBookingCat1;
    ImageView varImageCat1;
    Button submitBookingCat1;
    ImageButton selectImageButtonCat1;

    EditText optionTxtBookingCat2;
    ImageView varImageCat2;
    Button submitBookingCat2;
    ImageButton selectImageButtonCat2;

    EditText optionTxtBookingCat3;
    ImageView varImageCat3;
    Button submitBookingCat3;
    ImageButton selectImageButtonCat3;




    Spinner category1,category2,category3;
    DatabaseReference reference;
    String prodID;
    TextView category1Txt,category2Txt,category3Txt;
    Button addCat1Button,addCat2Button,addCat3Button;


    private static final int SELECT_PHOTOGOV_VARIATIONCAT1 = 1;
    private static final int SELECT_PHOTOGOV_VARIATIONCAT2 = 2;
    private static final int SELECT_PHOTOGOV_VARIATIONCAT3 = 3;

    Uri imageUriVariationCat1;
    Uri imageUriVariationCat2;
    Uri imageUriVariationCat3;



    private ArrayList<Uri> ImageListVariation = new ArrayList<Uri>();
    StorageReference firebaseStorage;
    int uploadsVariation = 0;
    int uploadsProduct = 0;

    List<String> fileNameListVar;

    String dataCat1;
    String dataCat2;
    String dataCat3;

    Button confirmButton;

    int counter = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom_specification_page_seller);
        addCategoryButton = findViewById(R.id.addCategoryButton);
        txtBox = findViewById(R.id.customSpecsTxt);
        category1 = findViewById(R.id.category1SpinnerSeller);
        category2 = findViewById(R.id.category2SpinnerSeller);
        category3 = findViewById(R.id.category3SpinnerSeller);
        category1Txt = findViewById(R.id.category1TxtSeller);
        category2Txt = findViewById(R.id.category2TxtSeller );
        category3Txt = findViewById(R.id.category3TxtSeller);
        addCat1Button = findViewById(R.id.addCategory1Button);
        addCat2Button = findViewById(R.id.addCategory2Button);
        addCat3Button = findViewById(R.id.addCategory3Button);
        confirmButton = findViewById(R.id.confirmSelectionButton2);
        varDialogCat1 = new Dialog(this);
        varDialogCat2 = new Dialog(this);
        varDialogCat3 = new Dialog(this);



        //Payment List
        listCat1 = new ArrayList();
        listCat2 = new ArrayList();
        listCat3 = new ArrayList();

        fileNameListVar = new ArrayList<>();




        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            prodID = extras.getString("ProductID");
        }


        showDataCat1();
        showDataCat2();
        showDataCat3();
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = txtBox.getText().toString().trim();

                if(data.isEmpty())
                {
                    Toast.makeText(AddCustomSpecificationPageSeller.this, "Please input Data", Toast.LENGTH_SHORT).show();
                }
                else if(listCat1.contains(data.toUpperCase(Locale.ROOT)))
                {
                    Toast.makeText(AddCustomSpecificationPageSeller.this, "Already Exist!", Toast.LENGTH_SHORT).show();
                }
                else if(addCategoryButton.getText().toString().trim().equalsIgnoreCase("ADD CATEGORY 1"))
                {
                    category1Txt.setText(txtBox.getText().toString().trim().toUpperCase(Locale.ROOT));
                    addCategoryButton.setText("ADD CATEGORY 2");
                    txtBox.setText("");
                }
                else if(addCategoryButton.getText().toString().trim().equalsIgnoreCase("ADD CATEGORY 2"))
                {
                    category2Txt.setText(txtBox.getText().toString().trim().toUpperCase(Locale.ROOT));
                    addCategoryButton.setText("ADD CATEGORY 3");
                    txtBox.setText("");
                }
                else if(addCategoryButton.getText().toString().trim().equalsIgnoreCase("ADD CATEGORY 3"))
                {
                    category3Txt.setText(txtBox.getText().toString().trim().toUpperCase(Locale.ROOT));
                    txtBox.setText("");
                    addCategoryButton.setVisibility(View.INVISIBLE);
                }


            }
        });

        addCat1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category1Txt.getText().toString().trim().toUpperCase(Locale.ROOT).equalsIgnoreCase("Category 1"))
                {
                    Toast.makeText(AddCustomSpecificationPageSeller.this, "Add Category 1 First", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder builderBooking = new AlertDialog.Builder(AddCustomSpecificationPageSeller.this);
                    builderBooking.setTitle("Add Specification");

                    View view1 = getLayoutInflater().inflate(R.layout.layout_dialog_variation_standard, null);
                    optionTxtBookingCat1 = view1.findViewById(R.id.setPaymentOptionTxt);
                    submitBookingCat1 = view1.findViewById(R.id.submitOption);
                    varImageCat1 = view1.findViewById(R.id.variationImageViewStandard);
                    selectImageButtonCat1 = view1.findViewById(R.id.selectionVariationImageButtonStandard);
                    builderBooking.setView(view1);

                    selectImageButtonCat1.setOnClickListener(new View.OnClickListener() {
                        @SuppressWarnings("deprecation")
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent,SELECT_PHOTOGOV_VARIATIONCAT1);
                        }
                    });

                    submitBookingCat1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dataCat1 = optionTxtBookingCat1.getText().toString().trim();
                            if(dataCat1.isEmpty())
                            {
                                optionTxtBookingCat1.setError("Cannot be Empty");
                                optionTxtBookingCat1.requestFocus();
                            }
                            else
                            {
                                if(ImageListVariation.size() == 0)
                                {
                                    Toast.makeText(AddCustomSpecificationPageSeller.this, "Please select Image", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    if(listCat1.size() == 0)
                                    {
                                        uploadImageVariationCat1(dataCat1.toUpperCase());
                                        varDialogCat1.dismiss();
                                        //deleteVarButton.setVisibility(View.VISIBLE);
                                        varImageCat1 = null;
                                        imageUriVariationCat1 = null;
                                    }
                                    else
                                    {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications").child("Category-1");
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                                {
                                                    uploadImageVariationCat1(dataCat1.toUpperCase());
                                                    varDialogCat1.dismiss();
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
                    varDialogCat1 = builderBooking.create();
                    varDialogCat1.show();
                }
            }
        });

        addCat2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category2Txt.getText().toString().trim().toUpperCase(Locale.ROOT).equalsIgnoreCase("Category 2"))
                {
                    Toast.makeText(AddCustomSpecificationPageSeller.this, "Add Category 2 First", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder builderBooking = new AlertDialog.Builder(AddCustomSpecificationPageSeller.this);
                    builderBooking.setTitle("Add Specification");

                    View view1 = getLayoutInflater().inflate(R.layout.layout_dialog_variation_standard, null);
                    optionTxtBookingCat2 = view1.findViewById(R.id.setPaymentOptionTxt);
                    submitBookingCat2 = view1.findViewById(R.id.submitOption);
                    varImageCat2 = view1.findViewById(R.id.variationImageViewStandard);
                    selectImageButtonCat2 = view1.findViewById(R.id.selectionVariationImageButtonStandard);
                    builderBooking.setView(view1);

                    selectImageButtonCat2.setOnClickListener(new View.OnClickListener() {
                        @SuppressWarnings("deprecation")
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent,SELECT_PHOTOGOV_VARIATIONCAT2);
                        }
                    });

                    submitBookingCat2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dataCat2 = optionTxtBookingCat2.getText().toString().trim();
                            if(dataCat2.isEmpty())
                            {
                                optionTxtBookingCat2.setError("Cannot be Empty");
                                optionTxtBookingCat2.requestFocus();
                            }
                            else
                            {
                                if(ImageListVariation.size() == 0)
                                {
                                    Toast.makeText(AddCustomSpecificationPageSeller.this, "Please select Image", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    if(listCat2.size() == 0)
                                    {
                                        uploadImageVariationCat2(dataCat2.toUpperCase());
                                        varDialogCat2.dismiss();
                                        //deleteVarButton.setVisibility(View.VISIBLE);
                                        varImageCat2 = null;
                                        imageUriVariationCat2 = null;
                                    }
                                    else
                                    {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications").child("Category-2");
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                                {
                                                    uploadImageVariationCat2(dataCat2.toUpperCase());
                                                    varDialogCat2.dismiss();
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
                    varDialogCat2 = builderBooking.create();
                    varDialogCat2.show();
                }
            }
        });

        addCat3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category3Txt.getText().toString().trim().toUpperCase(Locale.ROOT).equalsIgnoreCase("Category 3"))
                {
                    Toast.makeText(AddCustomSpecificationPageSeller.this, "Add Category 3 First", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder builderBooking = new AlertDialog.Builder(AddCustomSpecificationPageSeller.this);
                    builderBooking.setTitle("Add Specification");

                    View view1 = getLayoutInflater().inflate(R.layout.layout_dialog_variation_standard, null);
                    optionTxtBookingCat3 = view1.findViewById(R.id.setPaymentOptionTxt);
                    submitBookingCat3 = view1.findViewById(R.id.submitOption);
                    varImageCat3 = view1.findViewById(R.id.variationImageViewStandard);
                    selectImageButtonCat3 = view1.findViewById(R.id.selectionVariationImageButtonStandard);
                    builderBooking.setView(view1);

                    selectImageButtonCat3.setOnClickListener(new View.OnClickListener() {
                        @SuppressWarnings("deprecation")
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent,SELECT_PHOTOGOV_VARIATIONCAT3);
                        }
                    });

                    submitBookingCat3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dataCat3 = optionTxtBookingCat3.getText().toString().trim();
                            if(dataCat3.isEmpty())
                            {
                                optionTxtBookingCat3.setError("Cannot be Empty");
                                optionTxtBookingCat3.requestFocus();
                            }
                            else
                            {
                                if(ImageListVariation.size() == 0)
                                {
                                    Toast.makeText(AddCustomSpecificationPageSeller.this, "Please select Image", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    if(listCat3.size() == 0)
                                    {
                                        uploadImageVariationCat3(dataCat3.toUpperCase());
                                        varDialogCat3.dismiss();
                                        //deleteVarButton.setVisibility(View.VISIBLE);
                                        varImageCat3 = null;
                                        imageUriVariationCat3 = null;
                                    }
                                    else
                                    {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications").child("Category-3");
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                                {
                                                    uploadImageVariationCat3(dataCat3.toUpperCase());
                                                    varDialogCat3.dismiss();
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
                    varDialogCat3 = builderBooking.create();
                    varDialogCat3.show();
                }
            }
        });
        
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listCat1.size() > 0 )
                {
                    counter++;
                }
                if(listCat2.size() > 0)
                {
                    counter++;
                }
                if(listCat3.size() > 0)
                {
                    counter++;
                }
                if(counter == 0)
                {
                    Toast.makeText(AddCustomSpecificationPageSeller.this, "Please choose atleast one category!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AddCustomSpecificationPageSeller.this, "Specifications Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    public void showDataCat1()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications").child("Category-1");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCat1.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    AddCustomSpecsSellerList addCustomSpecsSellerList = snapshot.getValue(AddCustomSpecsSellerList.class);
                    listCat1.add(addCustomSpecsSellerList);
                }
                adapterCat1 = new AddCustomSpecsSellerAdapter(AddCustomSpecificationPageSeller.this, listCat1);
                category1.setAdapter(adapterCat1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showDataCat2()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications").child("Category-2");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCat2.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    AddCustomSpecsSellerList addCustomSpecsSellerList = snapshot.getValue(AddCustomSpecsSellerList.class);
                    listCat2.add(addCustomSpecsSellerList);
                }
                adapterCat2 = new AddCustomSpecsSellerAdapter(AddCustomSpecificationPageSeller.this, listCat2);
                category2.setAdapter(adapterCat2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showDataCat3()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications").child("Category-3");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCat3.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    AddCustomSpecsSellerList addCustomSpecsSellerList = snapshot.getValue(AddCustomSpecsSellerList.class);
                    listCat3.add(addCustomSpecsSellerList);
                }
                adapterCat3 = new AddCustomSpecsSellerAdapter(AddCustomSpecificationPageSeller.this, listCat3);
                category3.setAdapter(adapterCat3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTOGOV_VARIATIONCAT1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUriVariationCat1 = data.getData();
            varImageCat1.setImageURI(imageUriVariationCat1);
            ImageListVariation.add(imageUriVariationCat1);
            String fileName = getFileName(imageUriVariationCat1);
            fileNameListVar.add(fileName);
        }
        if (requestCode == SELECT_PHOTOGOV_VARIATIONCAT2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUriVariationCat2 = data.getData();
            varImageCat2.setImageURI(imageUriVariationCat2);
            ImageListVariation.add(imageUriVariationCat2);
            String fileName = getFileName(imageUriVariationCat2);
            fileNameListVar.add(fileName);
        }
        if (requestCode == SELECT_PHOTOGOV_VARIATIONCAT3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUriVariationCat3 = data.getData();
            varImageCat3.setImageURI(imageUriVariationCat3);
            ImageListVariation.add(imageUriVariationCat3);
            String fileName = getFileName(imageUriVariationCat3);
            fileNameListVar.add(fileName);
        }
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

    private void uploadImageVariationCat1(String varNameLabel) {
        Log.d("UPLOADINGERROR", "IMAGEERROR");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, "Added: "+varNameLabel, Toast.LENGTH_SHORT).show();

        FirebaseStorage storage = FirebaseStorage.getInstance(); // add this
        firebaseStorage = storage.getReference(); // and this
        StorageReference ImageFolder =  firebaseStorage.child(user.getEmail());
        for (uploadsVariation=0; uploadsVariation < ImageListVariation.size(); uploadsVariation++) {
            Uri Image  = ImageListVariation.get(uploadsVariation);
            StorageReference imagename = ImageFolder.child("Seller-Products").child("CustomSpecifications").child(fileNameListVar.get(uploadsVariation)+".jpeg");

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
                            map.put("bigImage", url);
                            reference.child("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications").child("Category-1").child(varNameLabel).updateChildren(map);
                            ImageListVariation.clear();
                            //reference.child("Products").child(varNameLabel).setValue(map);

                        }
                    });
                }
            });
        }
    }

    private void uploadImageVariationCat2(String varNameLabel) {
        Log.d("UPLOADINGERROR", "IMAGEERROR");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, "Added: "+varNameLabel, Toast.LENGTH_SHORT).show();


        FirebaseStorage storage = FirebaseStorage.getInstance(); // add this
        firebaseStorage = storage.getReference(); // and this
        StorageReference ImageFolder =  firebaseStorage.child(user.getEmail());
        for (uploadsVariation=0; uploadsVariation < ImageListVariation.size(); uploadsVariation++) {
            Uri Image  = ImageListVariation.get(uploadsVariation);
            StorageReference imagename = ImageFolder.child("Seller-Products").child("CustomSpecifications").child(fileNameListVar.get(uploadsVariation)+".jpeg");

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
                            map.put("bigImage", url);
                            reference.child("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications").child("Category-2").child(varNameLabel).updateChildren(map);
                            ImageListVariation.clear();
                            //reference.child("Products").child(varNameLabel).setValue(map);

                        }
                    });
                }
            });
        }
    }

    private void uploadImageVariationCat3(String varNameLabel) {
        Log.d("UPLOADINGERROR", "IMAGEERROR");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, "Added: "+varNameLabel, Toast.LENGTH_SHORT).show();


        FirebaseStorage storage = FirebaseStorage.getInstance(); // add this
        firebaseStorage = storage.getReference(); // and this
        StorageReference ImageFolder =  firebaseStorage.child(user.getEmail());
        for (uploadsVariation=0; uploadsVariation < ImageListVariation.size(); uploadsVariation++) {
            Uri Image  = ImageListVariation.get(uploadsVariation);
            StorageReference imagename = ImageFolder.child("Seller-Products").child("CustomSpecifications").child(fileNameListVar.get(uploadsVariation)+".jpeg");

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
                            map.put("bigImage", url);
                            reference.child("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications").child("Category-3").child(varNameLabel).updateChildren(map);
                            ImageListVariation.clear();
                            //reference.child("Products").child(varNameLabel).setValue(map);

                        }
                    });
                }
            });
        }
    }


}