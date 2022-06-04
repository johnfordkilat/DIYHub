package com.example.diyhub.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.diyhub.Buyer.AddBookingAddressBuyer;
import com.example.diyhub.Buyer.PastTransactionHistoryBuyer;
import com.example.diyhub.BuyerOrdersPage;
import com.example.diyhub.FollowingActivity;
import com.example.diyhub.LoginPage;
import com.example.diyhub.MESSAGES.User;
import com.example.diyhub.MessageDialog;
import com.example.diyhub.OrdersPage;
import com.example.diyhub.ProfilePage;
import com.example.diyhub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuyerProfileFragment extends Fragment {

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
    View view;
    TextView openBooking;
    TextView pastTransacButton;
    Button toBook, toReceive;
    TextView followingButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buyer_profile, container, false);

        fullname = view.findViewById(R.id.userFullname);

        toPay = view.findViewById(R.id.toPayButton);
        logout = view.findViewById(R.id.logoutTxtview);
        changeP = view.findViewById(R.id.changePhoto2);
        profPicBuyer = view.findViewById(R.id.profilePicBuyer);
        dbFirestore = FirebaseFirestore.getInstance();
        openBooking = view.findViewById(R.id.landBookingAdd);
        pastTransacButton = view.findViewById(R.id.pastTransacButtonBuyer);
        toBook = view.findViewById(R.id.toBookButton);
        toReceive = view.findViewById(R.id.toReceiveButton);
        followingButton = view.findViewById(R.id.followingButtonProfilePageBuyer);

        changeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOGOV);
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowingActivity.class);
                startActivity(intent);
            }
        });

        toBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BuyerOrdersPage.class);
                intent.putExtra("tabToBook",1);
                startActivity(intent);

            }
        });
        toReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BuyerOrdersPage.class);
                intent.putExtra("tabToReceive",2);
                startActivity(intent);

            }
        });

        pastTransacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PastTransactionHistoryBuyer.class);
                startActivity(intent);
            }
        });

        openBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddBookingAddressBuyer.class);
                startActivity(intent);
            }
        });

        Bundle extras = getActivity().getIntent().getExtras();
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    User users = snapshot.getValue(User.class);
                    if(users.getId().equalsIgnoreCase(firebaseUserBuyer.getUid()))
                    {
                        fullname.setText(users.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(firebaseUserBuyer != null)
        {
            if(firebaseUserBuyer.getPhotoUrl() != null)
            {
                Glide.with(this)
                        .load(firebaseUserBuyer.getPhotoUrl())
                        .into(profPicBuyer);
            }
        }




        toPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openToPayPage();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Logout Confirmation")
                        .setMessage("Are you sure you want to logout?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                logout();
                            }
                        });
                builder.show();
            }
        });

        return view;
    }



    private void openToPayPage() {
        Intent intent = new Intent(getContext(), BuyerOrdersPage.class);
        startActivity(intent);
    }


    private void logout() {
        mAuth.signOut();
        signoutUser();
    }

    private void signoutUser() {
        Intent intent = new Intent(getContext(), LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

        progressDialog = new ProgressDialog(getContext());
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
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> map = new HashMap<>();
                            map.put("imageUrl",url);
                            reference.child("Users").child(user.getUid()).updateChildren(map);

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
                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Profile Image Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}