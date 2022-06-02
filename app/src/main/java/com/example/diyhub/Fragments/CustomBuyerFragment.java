package com.example.diyhub.Fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.diyhub.Buyer.ItemViewModelCat1;
import com.example.diyhub.Buyer.ItemViewModelCat2;
import com.example.diyhub.Buyer.ItemViewModelCat3;
import com.example.diyhub.Buyer.ItemViewModelCustomSpecsText;
import com.example.diyhub.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class CustomBuyerFragment extends Fragment {

    View view;

    Spinner category1Spinner, category2Spinner, category3Spinner;
    List<AddCustomSpecsSellerList> listCat1;
    List<AddCustomSpecsSellerList> listCat2;
    List<AddCustomSpecsSellerList> listCat3;

    AddCustomSpecsSellerAdapter adapterCat1;
    AddCustomSpecsSellerAdapter adapterCat2;
    AddCustomSpecsSellerAdapter adapterCat3;

    String prodID;
    String sellerID;
    String prodImage;
    ImageView productImageImageView;

    FloatingActionButton pickImage1;
    FloatingActionButton pickImage2;
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

    ImageView imageview1,imageView2;
    String bookfrom;
    double rating;
    double sold;
    int stock;
    String description;
    String shopName;
    TextView descriptionProductCustom;
    RatingBar ratingBarBuyerCustom;
    TextView ratingNumCustom;
    TextView productSoldStandardProductBuyerCustom;
    TextView stockProductCustom;
    TextView reviewsCustom;
    EditText customSpecsTxtBuyerCustom;
    TextView bookFromProductCustom;

    String category1Equi,category2Equi,category3Equi;

    ItemViewModelCat1 viewModelCat1;
    ItemViewModelCat2 viewModelCat2;
    ItemViewModelCat3 viewModelCat3;
    ItemViewModelCustomSpecsText viewModelSpecsTxt;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_custom_buyer, container, false);

        category1Spinner = view.findViewById(R.id.customDropdown1);
        category2Spinner = view.findViewById(R.id.customDropdown2);
        category3Spinner = view.findViewById(R.id.customDropdown3);
        productImageImageView = view.findViewById(R.id.viewPagerStandardBuyerCustom);
        imageview1 = view.findViewById(R.id.image1ImageVIewCustom);
        imageView2 = view.findViewById(R.id.image2ImageviewCustom);
        pickImage1 = view.findViewById(R.id.addImage1FloatingButtonBuyerCustom);
        pickImage2 = view.findViewById(R.id.addImage2FloatingButtonBuyerCustom);
        bookFromProductCustom = view.findViewById(R.id.bookFromProductCustom);
        descriptionProductCustom = view.findViewById(R.id.descriptionProductCustom);
        ratingBarBuyerCustom = view.findViewById(R.id.ratingBarBuyerCustom);
        ratingNumCustom = view.findViewById(R.id.ratingNumCustom);
        productSoldStandardProductBuyerCustom = view.findViewById(R.id.productSoldStandardProductBuyerCustom);
        stockProductCustom = view.findViewById(R.id.stockProductCustom);
        reviewsCustom = view.findViewById(R.id.reviewsCustom);
        customSpecsTxtBuyerCustom = view.findViewById(R.id.customSpecsTxtBuyerCustom);





        listCat1 = new ArrayList<>();
        listCat2 = new ArrayList<>();
        listCat3 = new ArrayList<>();
        fileNameListVar = new ArrayList<>();

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null)
        {
            prodID = extras.getString("ProductID");
            sellerID = extras.getString("SellerID");
            prodImage = extras.getString("ProductImage");
            bookfrom = extras.getString("BookFrom");
            rating = extras.getDouble("Rating");
            sold = extras.getDouble("ProductSold");
            stock = extras.getInt("ProductStocks");
            description = extras.getString("ProductDescription");
            shopName = extras.getString("ShopName");

        }




        bookFromProductCustom.setText("Shop Address: " + bookfrom);
        ratingNumCustom.setText(String.valueOf(rating));
        ratingBarBuyerCustom.setRating((float) rating);
        descriptionProductCustom.setText(description);
        stockProductCustom.setText("Stock: " + String.valueOf(stock));

        productSoldStandardProductBuyerCustom.setText(String.valueOf(sold) + " Sold");

        Glide.with(getContext()).load(prodImage).into(productImageImageView);

        pickImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOGOV_VARIATIONCAT1);
            }
        });

        pickImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOGOV_VARIATIONCAT2);
            }
        });



        showDataCat1();
        showDataCat2();
        showDataCat3();




        category1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category1Equi = listCat1.get(position).getSpecsName();
                viewModelCat1 = new ViewModelProvider(requireActivity()).get(ItemViewModelCat1.class);
                viewModelCat1.setData(category1Equi);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        category2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category2Equi = listCat2.get(position).getSpecsName();
                viewModelCat2 = new ViewModelProvider(requireActivity()).get(ItemViewModelCat2.class);
                viewModelCat2.setData(category2Equi);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        category3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category3Equi = listCat3.get(position).getSpecsName();
                viewModelCat3 = new ViewModelProvider(requireActivity()).get(ItemViewModelCat3.class);
                viewModelCat3.setData(category3Equi);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpecsTxtBuyerCustom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String data = s.toString().trim();
                viewModelSpecsTxt = new ViewModelProvider(requireActivity()).get(ItemViewModelCustomSpecsText.class);
                viewModelSpecsTxt.setData(data);
            }
        });


        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTOGOV_VARIATIONCAT1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUriVariationCat1 = data.getData();
            imageview1.setImageURI(imageUriVariationCat1);
            ImageListVariation.add(imageUriVariationCat1);
            String fileName = getFileName(imageUriVariationCat1);
            fileNameListVar.add(fileName);
        }
        if (requestCode == SELECT_PHOTOGOV_VARIATIONCAT2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUriVariationCat2 = data.getData();
            imageView2.setImageURI(imageUriVariationCat2);
            ImageListVariation.add(imageUriVariationCat2);
            String fileName = getFileName(imageUriVariationCat2);
            fileNameListVar.add(fileName);
        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri)
    {
        String result = null;
        if(uri.getScheme().equals("content"))
        {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
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


    public void showDataCat1()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("CustomSpecifications").child("Category-1");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCat1.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    AddCustomSpecsSellerList addCustomSpecsSellerList = snapshot.getValue(AddCustomSpecsSellerList.class);
                    listCat1.add(addCustomSpecsSellerList);
                }
                adapterCat1 = new AddCustomSpecsSellerAdapter(getContext(), listCat1);
                category1Spinner.setAdapter(adapterCat1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showDataCat2()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("CustomSpecifications").child("Category-2");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCat2.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    AddCustomSpecsSellerList addCustomSpecsSellerList = snapshot.getValue(AddCustomSpecsSellerList.class);
                    listCat2.add(addCustomSpecsSellerList);
                }
                adapterCat2 = new AddCustomSpecsSellerAdapter(getContext(), listCat2);
                category2Spinner.setAdapter(adapterCat2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showDataCat3()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(sellerID).child(prodID).child("CustomSpecifications").child("Category-3");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCat3.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    AddCustomSpecsSellerList addCustomSpecsSellerList = snapshot.getValue(AddCustomSpecsSellerList.class);
                    listCat3.add(addCustomSpecsSellerList);
                }
                adapterCat3 = new AddCustomSpecsSellerAdapter(getContext(), listCat3);
                category3Spinner.setAdapter(adapterCat3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}