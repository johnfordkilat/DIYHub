package com.example.diyhub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateMyShopPage extends AppCompatActivity {


    Button chooseShopImage;
    EditText shopAddress;
    EditText shopName;
    EditText shopMotto;
    EditText fullName;
    Button createShopButton;
    Spinner businessType;
    ArrayAdapter adapter;
    List<String> list;
    private static final int SELECT_PHOTOGOV = 1;

    CircleImageView shopImage;
    Uri imageUri1;
    List<Uri> ImageList;

    ProgressDialog progressDialog;
    int uploads = 0;
    EditText latTxt,longTxt;
    String businessTypeTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_my_shop_page);

        chooseShopImage = findViewById(R.id.chooseShopImageButtonCreateShop);
        shopAddress = findViewById(R.id.shopAddressTxtCreateShop);
        shopName = findViewById(R.id.shopNameTxtCreateShop);
        shopMotto = findViewById(R.id.shopMottoTxtCreateShop);
        fullName = findViewById(R.id.fullNameTxtCreateShop);
        createShopButton = findViewById(R.id.createShopNowButtonCreateShop);
        businessType = findViewById(R.id.createMyShopSpinnerSeller);
        shopImage = findViewById(R.id.shopImageCreateShop);
        latTxt = findViewById(R.id.latitudeTxtCreateShop);
        longTxt = findViewById(R.id.longitudeTxtCreateShop);


        chooseShopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shopName.getText().toString().trim().isEmpty())
                {
                    shopName.setError("Please choose shop name first");
                    shopName.requestFocus();
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent,SELECT_PHOTOGOV);
                }

            }
        });
        ImageList = new ArrayList<>();

        createShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(shopAddress.getText().toString().trim().isEmpty())
                {
                    shopAddress.setError("Required");
                    shopAddress.requestFocus();
                    return;
                }
                if(shopName.getText().toString().trim().isEmpty())
                {
                    shopName.setError("Required");
                    shopName.requestFocus();
                    return;
                }
                if(fullName.getText().toString().trim().isEmpty())
                {
                    fullName.setError("Required");
                    fullName.requestFocus();
                    return;
                }
                if(shopMotto.getText().toString().trim().isEmpty())
                {
                    shopMotto.setError("Required");
                    shopMotto.requestFocus();
                    return;
                }
                if(latTxt.getText().toString().trim().isEmpty())
                {
                    latTxt.setError("Required");
                    latTxt.requestFocus();
                    return;
                }
                if(longTxt.getText().toString().trim().isEmpty())
                {
                    longTxt.setError("Required");
                    longTxt.requestFocus();
                    return;
                }
                else
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> map = new HashMap<>();
                    map.put("SellerID",user.getUid());
                    map.put("ShopAddress",shopAddress.getText().toString().trim());
                    map.put("ShopImage","");
                    map.put("ShopName",shopName.getText().toString().trim());
                    map.put("ShopRating",0);
                    map.put("ShopViews",0);
                    map.put("ShopRating","");
                    map.put("Fullname",fullName.getText().toString().trim());
                    map.put("ShopMotto",shopMotto.getText().toString().trim());
                    map.put("latitude",latTxt.getText().toString().trim());
                    map.put("longitude",longTxt.getText().toString().trim());
                    map.put("BusinessType",businessTypeTxt);
                    reference.child("Shops").child(shopName.getText().toString().trim()).updateChildren(map);
                }


            }
        });

        businessType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    businessTypeTxt = list.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Payment List
        list = new ArrayList<String>();
        list.add(0, "Choose Business Type");
        list.add(1, "Single Owner");
        list.add(2, "Company");

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
        businessType.setAdapter(adapter);



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTOGOV && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri1 = data.getData();
            shopImage.setImageURI(imageUri1);
            ImageList.add(imageUri1);
            uploadImage();

        }
    }

    private void uploadImage() {
        Log.d("UPLOADINGERROR", "IMAGEERROR");

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Changing Profile....");
        progressDialog.show();

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child(id);
        for (uploads=0; uploads < ImageList.size(); uploads++) {
            Uri Image  = ImageList.get(uploads);
            StorageReference imagename = ImageFolder.child("Profile-Picture").child(id+".jpeg");

            imagename.putFile(ImageList.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = String.valueOf(uri);

                            Log.d("DownloadUrl", url);
                            progressDialog.dismiss();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> map = new HashMap<>();
                            map.put("ShopImage","");
                            reference.child("Shops").child(shopName.getText().toString().trim()).updateChildren(map);
                        }
                    });
                }
            });
        }
    }
}