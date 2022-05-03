package com.example.diyhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity {


    TextView btn;
    Button login;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("USERPROFILE");
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    FirebaseFirestore dbFireStore;
    String username,password;
    String value1,value2,value3,value4,value5,value6,value7,value8,value9,value10,value11,value12;
    String usernameBuyer,emailBuyer;
    String value111,value112,value13,value14,value15,value16,value17,value18,value19,value20,value21,value22;
    String productSize;
    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;

    public static final String fileName = "filename";
    public static final String Username = "username";
    public static final String Password = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        btn = findViewById(R.id.textView2);
        login = findViewById(R.id.loginButton);
        mAuth = FirebaseAuth.getInstance();
        dbFireStore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);




        Bundle extrasSeller = getIntent().getExtras();
        if(extrasSeller != null)
        {
            String value = extrasSeller.getString("verMessage");
            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
            value1 = extrasSeller.getString("UserIDSeller");
            value2= extrasSeller.getString("UserFirstnameSeller");
            value3 = extrasSeller.getString("UserLastnameSeller");
            value4 = extrasSeller.getString("UserBirthdateSeller");
            value5 = extrasSeller.getString("UserPhoneSeller");
            value6 = extrasSeller.getString("UserAddressSeller");
            value7 = extrasSeller.getString("UserGenderSeller");
            value8 = extrasSeller.getString("UserTypeSeller");
            value9 = extrasSeller.getString("UserStatusSeller");
            value10 = extrasSeller.getString("UserEmailSeller");
            value11 = extrasSeller.getString("UserUsernameSeller");
            value12 = extrasSeller.getString("UserPasswordSeller");
            productSize = extrasSeller.getString("ProductSize");
        }
        Bundle extrasBuyer = getIntent().getExtras();
        if(extrasBuyer != null)
        {
            usernameBuyer = extrasBuyer.getString("username");
            emailBuyer = extrasBuyer.getString("buyerEmail");
            value111 = extrasBuyer.getString("UserIDBuyer");
            value112= extrasBuyer.getString("UserFirstnameBuyer");
            value13 = extrasBuyer.getString("UserLastnameBuyer");
            value14 = extrasBuyer.getString("UserBirthdateBuyer");
            value15 = extrasBuyer.getString("UserPhoneBuyer");
            value16 = extrasBuyer.getString("UserAddressBuyer");
            value17 = extrasBuyer.getString("UserGenderBuyer");
            value18 = extrasBuyer.getString("UserTypeBuyer");
            value19 = extrasBuyer.getString("UserStatusBuyer");
            value20 = extrasBuyer.getString("UserEmailBuyer");
            value21 = extrasBuyer.getString("UserUsernameBuyer");
            value22 = extrasBuyer.getString("UserPasswordBuyer");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterPage();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


    }

    private void openRegisterPage() {
        Intent intent = new Intent(LoginPage.this, MainActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openHomePage() {
        Intent intent = new Intent(LoginPage.this, BuyerAccountHomePage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void loginUser()
    {
        EditText txt1 = (EditText) findViewById(R.id.usernameTxtbox);
        EditText txt2 = (EditText) findViewById(R.id.passwordTxtbox);

        username = txt1.getText().toString().trim();
        password = txt2.getText().toString();

        if(!username.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(username).matches())
        {
            if(!password.isEmpty())
            {
                progressDialog.setTitle("Logging in");
                progressDialog.setCancelable(false);
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(username,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginPage.this, "Login Successfully", Toast.LENGTH_LONG).show();
                                fetchData();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginPage.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            }
            else
            {
                Toast.makeText(LoginPage.this, "Password must not be empty!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(username.isEmpty())
        {
            Toast.makeText(LoginPage.this, "Username must not be empty!", Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty())
        {
            Toast.makeText(LoginPage.this, "Password must not be empty!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(LoginPage.this, "Username is invalid!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void fetchData()
    {
        DocumentReference documentReference = dbFireStore.collection("USERPROFILE").document(username);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    String data = documentSnapshot.getString("UserFirstname") + " " + documentSnapshot.getString("UserLastname");
                    String email = documentSnapshot.getString("UserEmail");
                    String userType = documentSnapshot.getString("UserType");
                    String locationSeller = documentSnapshot.getString("UserAddress");
                    String phoneSeller = documentSnapshot.getString("UserPhone");
                    String motto = documentSnapshot.getString("UserMotto");
                    if(userType.equalsIgnoreCase("Buyer"))
                    {
                        Intent intent = new Intent(LoginPage.this, BuyerAccountHomePage.class);
                        intent.putExtra("username",data);
                        intent.putExtra("buyerEmail",email);
                        intent.putExtra("UserIDBuyer", value111);
                        intent.putExtra("UserFirstnameBuyer", value112);
                        intent.putExtra("UserLastnameBuyer", value13);
                        intent.putExtra("UserBirthdateBuyer", value14);
                        intent.putExtra("UserPhoneBuyer", value15);
                        intent.putExtra("UserAddressBuyer", value16);
                        intent.putExtra("UserGenderBuyer", value17);
                        intent.putExtra("UserTypeBuyer", value18);
                        intent.putExtra("UserStatusBuyer", value19);
                        intent.putExtra("UserEmailBuyer", value20);
                        intent.putExtra("UserUsernameBuyer", value21);
                        intent.putExtra("UserPasswordBuyer", value22);
                        startActivity(intent);
                    }
                    else if(userType.equalsIgnoreCase("Seller"))
                    {
                        Intent intent = new Intent(LoginPage.this, SellerHomePage.class);
                        intent.putExtra("username",data);
                        intent.putExtra("sellerEmail",email);
                        intent.putExtra("locationSeller",locationSeller);
                        intent.putExtra("phoneSeller",phoneSeller);
                        intent.putExtra("UserMotto", motto);
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
                else
                {
                    Toast.makeText(getApplicationContext(), "Data not Found!", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}