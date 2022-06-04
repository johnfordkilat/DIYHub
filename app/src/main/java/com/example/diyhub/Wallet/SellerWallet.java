package com.example.diyhub.Wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.diyhub.R;

public class SellerWallet extends AppCompatActivity {

    CardView viewTransac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_wallet);

        viewTransac = findViewById(R.id.transacButtonWallet);

        viewTransac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerWallet.this, WalletTransactions.class);
                startActivity(intent);
            }
        });

    }
}