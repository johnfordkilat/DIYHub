package com.example.diyhub;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

public class AccountVerificationPage extends AppCompatActivity {

    Connection connect;
    Statement stmt;

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
    String value1,value2,value3,value4,value5,value6,value7,value8,value9,value10,value11,value12;

    Uri imageUri1,imageUri2,imageUri3;
    StorageReference storageReference1,storageReference2,storageReference3;
    ProgressDialog progressDialog;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    int index = 0;
    private int uploads = 0;
    private int success = 0;
    String productSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification_page);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value1 = extras.getString("UserIDSeller");
            value2= extras.getString("UserFirstnameSeller");
            value3 = extras.getString("UserLastnameSeller");
            value4 = extras.getString("UserBirthdateSeller");
            value5 = extras.getString("UserPhoneSeller");
            value6 = extras.getString("UserAddressSeller");
            value7 = extras.getString("UserGenderSeller");
            value8 = extras.getString("UserTypeSeller");
            value9 = extras.getString("UserStatusSeller");
            value10 = extras.getString("UserEmailSeller");
            value11 = extras.getString("UserUsernameSeller");
            value12 = extras.getString("UserPasswordSeller");
            productSize = extras.getString("ProductSize");
        }

        back = findViewById(R.id.backButton);
        govID = findViewById(R.id.govIDUpload);
        twobytwo = findViewById(R.id.govTwoByTwoUpload);
        permit = findViewById(R.id.govPermitsUpload);
        govStatus = findViewById(R.id.govIDTxt);
        twobytwoStatus = findViewById(R.id.twoByTwoTxt);
        permitStatus = findViewById(R.id.permitsTxt);
        govImg  = findViewById(R.id.govIDImage);
        twobytwoImg = findViewById(R.id.twobytwoImage);
        permitImg = findViewById(R.id.permitImage);
        signup = findViewById(R.id.signupButtonVerification);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });


        govID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOGOV);
            }
        });
        twobytwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOTWO);
            }
        });
        permit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOPERMIT);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });



    }

    private void backPage() {
        Intent intent = new Intent(AccountVerificationPage.this, SellerRegistrationPage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }



    public static boolean contains(final int[] arr, final int key) {
        return ArrayUtils.contains(arr, key);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTOGOV && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri1 = data.getData();
            govImg.setImageURI(imageUri1);
            ImageList.add(imageUri1);

        }
        if(requestCode == SELECT_PHOTOTWO && resultCode == RESULT_OK  && data != null && data.getData() != null)
        {
            imageUri2 = data.getData();
            twobytwoImg.setImageURI(imageUri2);
            ImageList.add(imageUri2);

        }
        if(requestCode == SELECT_PHOTOPERMIT && resultCode == RESULT_OK  && data != null && data.getData() != null)
        {
            imageUri3 = data.getData();
            permitImg.setImageURI(imageUri3);
            ImageList.add(imageUri3);

        }
    }

    private String getFileExtensionGov(Uri uriGov)
    {
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uriGov));
    }


    private void loginPage() {
        Intent intent = new Intent(AccountVerificationPage.this, LoginPage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();


        final StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child(value10);
        for (uploads=0; uploads < ImageList.size(); uploads++) {
            Uri Image  = ImageList.get(uploads);
            final StorageReference imagename = ImageFolder.child("VALID ID/"+Image.getLastPathSegment());

            imagename.putFile(ImageList.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = String.valueOf(uri);

                            Log.d("DownloadUrl", url);
                            progressDialog.dismiss();

                        }
                    });
                }
            });
            success++;
        }
        Toast.makeText(AccountVerificationPage.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

        if(success == uploads)
        {
            String verify = "Please wait 1 to 3 days to verify your account";
            Intent intent = new Intent(AccountVerificationPage.this, LoginPage.class);
            intent.putExtra("emailVer", value9);
            intent.putExtra("verMessage", verify);
            intent.putExtra("UserIDSeller", value1);
            intent.putExtra("UserFirstnameSeller", value2);
            intent.putExtra("UserLastnameSeller", value3);
            intent.putExtra("UserBirthdateSeller", value4);
            intent.putExtra("UserPhoneSeller", value5);
            intent.putExtra("UserAddressSeller", value6);
            intent.putExtra("UserGenderSeller", value7);
            intent.putExtra("UserTypeSeller", value8);
            intent.putExtra("UserStatusSeller", value9);
            intent.putExtra("UserEmailSeller", value10);
            intent.putExtra("UserUsernameSeller", value11);
            intent.putExtra("UserPasswordSeller", value12);
            intent.putExtra("ProductSize",productSize);
            startActivity(intent);
        }



    }


}