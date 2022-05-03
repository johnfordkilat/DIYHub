package com.example.diyhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    Button loginActv,sellerActv,buyerActv;
    String usernameBuyer,emailBuyer;
    String value111,value112,value13,value14,value15,value16,value17,value18,value19,value20,value21,value22;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    SharedPreferences sharedPreferences;

    public static final String fileName = "filename";
    public static final String Username = "username";
    public static final String Password = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginActv = findViewById(R.id.loginButton);
        sellerActv = findViewById(R.id.sellerButton);
        buyerActv = findViewById(R.id.buyerButton);



        loginActv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginPage();
            }
        });

        sellerActv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSellerPage();
            }
        });

        buyerActv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBuyerPage();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    }



    private void openLoginPage()
    {
        Intent intent = new Intent(MainActivity.this, LoginPage.class);
        startActivity(intent);

    }

    private void openSellerPage()
    {
        Intent intent = new Intent(MainActivity.this, SellerRegistrationPage.class);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openBuyerPage()
    {
        Intent intent = new Intent(MainActivity.this, BuyerRegistrationPage.class);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openListPage()
    {
        Intent intent = new Intent(MainActivity.this, ListviewPage.class);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}