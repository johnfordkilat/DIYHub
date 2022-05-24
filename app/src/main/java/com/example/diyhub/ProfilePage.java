package com.example.diyhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.diyhub.Buyer.AddBookingAddressBuyer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AppCompatActivity implements MessageDialog.ExampleDialogListener {

    TextView fullname,logout;
    ImageView back;
    Button toPay;
    TextView changeP;

    private static final int SELECT_PHOTOGOV = 1;

    Uri imageUri1,imageUri2,imageUri3;
    StorageReference storageReference1,storageReference2,storageReference3;
    ProgressDialog progressDialog;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    int index = 0;
    private int uploads = 0;
    private int success = 0;
    ImageView profPic;
    String usernameBuyer,emailBuyer;
    FirebaseAuth mAuth;
    String myUri;
    private DatabaseReference databaseReference;
    FirebaseFirestore dbFirestore;
    String value1,value2,value3,value4,value5,value6,value7,value8,value9,value10,value11,value12;
    TextView shopN;
    ImageView profPicBuyer;

    TextView addBookingAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        fullname = findViewById(R.id.userFullname);
        back = findViewById(R.id.backButtonProfile);
        toPay = findViewById(R.id.toPayButton);
        logout = findViewById(R.id.logoutTxtview);
        changeP = findViewById(R.id.changePhoto2);
        profPicBuyer = findViewById(R.id.profilePicBuyer);

        addBookingAddress = findViewById(R.id.landBookingAdd);

        dbFirestore = FirebaseFirestore.getInstance();

        changeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOGOV);
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usernameBuyer = extras.getString("username");
            emailBuyer = extras.getString("buyerEmail");
            value1 = extras.getString("UserIDBuyer");
            value2= extras.getString("UserFirstnameBuyer");
            value3 = extras.getString("UserLastnameBuyer");
            value4 = extras.getString("UserBirthdateBuyer");
            value5 = extras.getString("UserPhoneBuyer");
            value6 = extras.getString("UserAddressBuyer");
            value7 = extras.getString("UserGenderBuyer");
            value8 = extras.getString("UserTypeBuyer");
            value9 = extras.getString("UserStatusBuyer");
            value10 = extras.getString("UserEmailBuyer");
            value11 = extras.getString("UserUsernameBuyer");
            value12 = extras.getString("UserPasswordBuyer");
        }
        mAuth = FirebaseAuth.getInstance();


        FirebaseUser firebaseUserBuyer = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUserBuyer != null)
        {
            fullname.setText(usernameBuyer);
            if(firebaseUserBuyer.getPhotoUrl() != null)
            {
                Glide.with(this)
                        .load(firebaseUserBuyer.getPhotoUrl())
                        .into(profPicBuyer);
            }
        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        toPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openToPayPage();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        addBookingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBookAddress();
            }
        });


    }

    private void addBookAddress() {
        Intent intent = new Intent(ProfilePage.this, AddBookingAddressBuyer.class);
            startActivity(intent);
    }


    private void backPage() {
        Intent intent = new Intent(ProfilePage.this, BuyerAccountHomePage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openToPayPage() {
        Intent intent = new Intent(ProfilePage.this, ToPayPage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void logout() {
            mAuth.signOut();
            signoutUser();
    }

    private void signoutUser() {
        Intent intent = new Intent(ProfilePage.this, LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void openDialog() {
        MessageDialog dialog = new MessageDialog();
        dialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void onYesClicked() {
    logout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTOGOV && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri1 = data.getData();
            profPicBuyer.setImageURI(imageUri1);
            ImageList.add(imageUri1);
            uploadImage();

        }
    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Changing Profile....");
        progressDialog.show();

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child(emailBuyer);
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
                            setProfileBuyer(uri);
                            DocumentReference documentReference = dbFirestore.collection("USERPROFILE").document(emailBuyer);
                            Map<String,Object> userBuyer = new HashMap<>();
                            userBuyer.put("UserProfileImage",url);
                            documentReference.update(userBuyer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", "SUCCESS");

                                }
                            });

                        }
                    });
                }
            });
        }
    }

    private void setProfileBuyer(Uri uri)
    {
        FirebaseUser firebaseUserBuyer = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        firebaseUserBuyer.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProfilePage.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePage.this, "Profile Image Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}