package com.example.diyhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.StorageReference;

import java.sql.Connection;
import java.util.ArrayList;

public class CustomizedProductPage extends AppCompatActivity {

    TextView standardButton,addtocart;

    private static final int SELECT_PHOTOGOV = 1;
    private static final int SELECT_PHOTOTWO = 2;
    private static final int SELECT_PHOTOPERMIT = 3;
    ImageView back,govID,twobytwo,permit,govImg,twobytwoImg,permitImg;
    TextView govStatus,twobytwoStatus,permitStatus;
    Button signup;
    String type,status,gender;
    String username,password,firstname,lastname,birthdate,phone,address,email;
    Connection con;
    String[] data;
    String value;

    Uri imageUri1,imageUri2,imageUri3;
    StorageReference storageReference1,storageReference2,storageReference3;
    ProgressDialog progressDialog;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    int index = 0;
    private int uploads = 0;
    private int success = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customized_product_page);



        standardButton = findViewById(R.id.standardButton);
        addtocart = findViewById(R.id.addToCartCustom);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("docName");
        }

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Glass Print", "2", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Spinner dropdown1 = findViewById(R.id.spinner2);
        String[] items1 = new String[]{"Jar Cap", "2", "three"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        dropdown1.setAdapter(adapter1);

        //get the spinner from the xml.
        Spinner dropdown2 = findViewById(R.id.spinner3);
        String[] items2 = new String[]{"Glass Form", "2", "three"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown2.setAdapter(adapter3);

        standardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStandardPage();
            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Product Added to Cart Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openStandardPage() {
        Intent intent = new Intent(CustomizedProductPage.this, StandardProductPage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    
}