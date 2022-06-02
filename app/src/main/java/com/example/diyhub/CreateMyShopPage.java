package com.example.diyhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    map.put("Fullname",fullName.getText().toString().trim());
                    map.put("ShopMotto",shopMotto.getText().toString().trim());
                    reference.child("Shops").child(shopName.getText().toString().trim()).updateChildren(map);
                }


            }
        });

        //Payment List
        list = new ArrayList<String>();
        list.add(0, "Choose Business Type");

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
}