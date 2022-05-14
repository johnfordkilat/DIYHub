package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.diyhub.Adapter;
import com.example.diyhub.R;
import com.example.diyhub.ViewPageAdapterProductDetailsStandard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailCustomPagePreview extends AppCompatActivity {

    Spinner firstSpinner;
    Spinner secondSpinner;
    List<String> list;
    ArrayAdapter<String> adapter;
    List<String> listSecond;
    ArrayAdapter<String> adapterSecond;
    ViewPager viewPager;
    String prodID;

    TextView customPrevFirstTxt;
    ImageView customPrevFirstImage;

    List<OrderCustomizationsSpecs> specsList;


    List<ProductDetailsImagesList> prodImagesList;

    List<OrderCustomerUploads> uploadsList;

    ImageView customPrevSecondImage;
    ImageView customPrevThirdImage;

    List<OrderAllItemSpecs> allItemSpecs;
    List<String> allItemSpecsList;

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_custom_page_preview);

        firstSpinner = findViewById(R.id.firstSpinnerCustomPreview);
        secondSpinner = findViewById(R.id.secondSpinnerCustomPreview);
        viewPager = findViewById(R.id.customPreviewViewPager);
        customPrevFirstTxt = findViewById(R.id.customPreviewFirstTxt);
        customPrevFirstImage = findViewById(R.id.customPreviewFirstImage);
        customPrevSecondImage = findViewById(R.id.customPrevSecondImage);
        customPrevThirdImage = findViewById(R.id.customPrevThirdImage);
        backButton = findViewById(R.id.backButtonOrderDetailCustomPreview);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        prodImagesList = new ArrayList<>();
        specsList = new ArrayList<>();
        uploadsList = new ArrayList<>();
        allItemSpecs = new ArrayList<>();
        allItemSpecsList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            prodID = extras.getString("ProductID");
        }

        list = new ArrayList<>();
        list.add(0, "Glass Style: Customer Upload");

        listSecond = new ArrayList<>();
        listSecond.add(0, "Glass Print: Valentines Snow");
        listSecond.add(1,"Color: Red");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        firstSpinner.setAdapter(adapter);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child("2716501e-59").child("ProductImages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prodImagesList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ProductDetailsImagesList allList = snapshot.getValue(ProductDetailsImagesList.class);
                    prodImagesList.add(allList);

                }
                OrderDetailCustomPreviewAdapter viewPageAdapterProductDetailsStandard = new OrderDetailCustomPreviewAdapter(OrderDetailCustomPagePreview.this, prodImagesList);

                viewPager.setAdapter(viewPageAdapterProductDetailsStandard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid()).child(prodID).child("OrderCustomizationsSpecs");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                specsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    OrderCustomizationsSpecs specs = snapshot.getValue(OrderCustomizationsSpecs.class);
                    specsList.add(specs);

                }
                customPrevFirstTxt.setText(specsList.get(0).getColor());
                Glide.with(OrderDetailCustomPagePreview.this).load(specsList.get(0).getImage()).into(customPrevFirstImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid()).child(prodID).child("CustomerUploads");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    OrderCustomerUploads uploads = snapshot.getValue(OrderCustomerUploads.class);
                    uploadsList.add(uploads);

                }
                Glide.with(OrderDetailCustomPagePreview.this).load(uploadsList.get(0).getStyle()).into(customPrevSecondImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid()).child(prodID).child("AllItemSpecs");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allItemSpecs.clear();
                allItemSpecsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    OrderAllItemSpecs allItemSpecs1 = snapshot.getValue(OrderAllItemSpecs.class);
                    allItemSpecs.add(allItemSpecs1);
                }

                for(int i = 0; i < allItemSpecs.size(); i++)
                {
                    allItemSpecsList.add(allItemSpecs.get(i).getSpecs());
                }
                adapterSecond = new ArrayAdapter<String>(OrderDetailCustomPagePreview.this, android.R.layout.simple_spinner_dropdown_item, allItemSpecsList);
                secondSpinner.setAdapter(adapterSecond);

                Glide.with(OrderDetailCustomPagePreview.this).load(allItemSpecs.get(0).getImage()).into(customPrevThirdImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}