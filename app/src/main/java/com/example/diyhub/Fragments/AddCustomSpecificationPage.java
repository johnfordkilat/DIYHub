package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diyhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    Button removeSpecs;
    String prodID;
    String todeleteSecond;
    Button confirmSelect;
    List<CustomProductSpecsList> specsLists;

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
        removeSpecs = findViewById(R.id.removeCustomSpecsToSpinner);
        confirmSelect = findViewById(R.id.confirmSelectionButton);

        String[] allData = {};

        List<String> list = new ArrayList<>(Arrays.asList(allData));

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            prodID = extras.getString("ProductID");
        }

        listSecond = new ArrayList<>();
        specsLists = new ArrayList<>();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                    if(list.contains(data.toUpperCase(Locale.ROOT)))
                    {
                        Toast.makeText(AddCustomSpecificationPage.this, "Already Exist!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(AddCustomSpecificationPage.this, "Added: "+data, Toast.LENGTH_SHORT).show();
                        list.add(data.toUpperCase(Locale.ROOT));
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put("SpecsName", data.toUpperCase(Locale.ROOT));
                        reference.child("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications").push().updateChildren(map);
                        adapter.notifyDataSetChanged();
                        customTxt.setText("");
                    }
                }

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                specsLists.clear();
                list.clear();
                int i = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    CustomProductSpecsList standardProductVariationList = snapshot.getValue(CustomProductSpecsList.class);
                    specsLists.add(standardProductVariationList);
                    list.add(standardProductVariationList.getSpecsName());
                }

                adapter = new ArrayAdapter<String>(AddCustomSpecificationPage.this, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                customSpecsSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        customSpecsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                todeleteSecond = list.get(position).toUpperCase(Locale.ROOT).trim();
                Toast.makeText(AddCustomSpecificationPage.this, todeleteSecond, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        confirmSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = ImageListVariation.size() == 0 ? 0 : ImageListVariation.size();
                if(ImageListVariation.size() == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddCustomSpecificationPage.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("No Images Added. Want to Proceed?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(AddCustomSpecificationPage.this, "Specifications Added Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                }

            }
        });

        removeSpecs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder removeVar = new AlertDialog.Builder(AddCustomSpecificationPage.this);
                removeVar.setTitle("Delete Confirmation");
                removeVar.setMessage("Are you sure you want to delete "+ todeleteSecond+" ?");
                removeVar.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("SellerProducts").child(user.getUid()).child(prodID).child("CustomSpecifications").child(todeleteSecond);
                        reference1.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                list.remove(todeleteSecond);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(AddCustomSpecificationPage.this, "Variation Deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                removeVar.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                removeVar.create().show();
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