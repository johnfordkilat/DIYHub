package com.example.diyhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StandardProductPage extends AppCompatActivity {

    TextView customButton,addtocart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_product_page);

        customButton = findViewById(R.id.customizeButton);
        addtocart = findViewById(R.id.addToCartStandard);

        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCUstomizePage();
            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StandardProductPage.this, "Product Added to cart Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openCUstomizePage() {
        Intent intent = new Intent(StandardProductPage.this, CustomizedProductPage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}