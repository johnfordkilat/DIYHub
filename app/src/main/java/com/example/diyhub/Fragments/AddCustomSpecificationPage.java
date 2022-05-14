package com.example.diyhub.Fragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diyhub.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCustomSpecificationPage extends AppCompatActivity {


    private static final int SELECT_PHOTOGOV_IMAGE1 = 1;
    private static final int SELECT_PHOTOGOV_IMAGE2 = 2;


    FloatingActionButton addImage1;
    FloatingActionButton addImage2;

    ImageView image1;
    ImageView image2;
    List<Uri> ImageListVariation;
    Uri imageCustom1;
    Uri imageCustom2;
    Spinner customSpecsSpinner;
    Button addToSpinner;
    List<String> listSecond;
    ArrayAdapter<String> adapter;

    EditText customTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom_specification_page);

        addImage1 = findViewById(R.id.addImage1FloatingButton);
        addImage2 = findViewById(R.id.addImage2FloatingButton);
        image1 = findViewById(R.id.image1ImageVIew);
        image2 = findViewById(R.id.image2Imageview);
        customSpecsSpinner = findViewById(R.id.customspecsSpinner);
        addToSpinner = findViewById(R.id.addCustomSpecsToSpinner);
        customTxt = findViewById(R.id.customSpecsTxt);

        String[] allData = {};

        List<String> list = new ArrayList<>(Arrays.asList(allData));

        listSecond = new ArrayList<>();
        adapter = new ArrayAdapter<String>(AddCustomSpecificationPage.this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        customSpecsSpinner.setAdapter(adapter);


        ImageListVariation = new ArrayList<>();

        addToSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = customTxt.getText().toString().trim();
                if(data.isEmpty())
                {
                    Toast.makeText(AddCustomSpecificationPage.this, "Please input Data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AddCustomSpecificationPage.this, "Added: "+data, Toast.LENGTH_SHORT).show();
                    list.add(data);
                    adapter.notifyDataSetChanged();
                }

            }
        });


        addImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOGOV_IMAGE1);
            }
        });
        addImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOGOV_IMAGE2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTOGOV_IMAGE1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageCustom1 = data.getData();
            image1.setImageURI(imageCustom1);
            ImageListVariation.add(imageCustom1);
        }
        if(requestCode == SELECT_PHOTOGOV_IMAGE2 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageCustom2 = data.getData();
            image2.setImageURI(imageCustom2);
            ImageListVariation.add(imageCustom2);
        }
    }
}