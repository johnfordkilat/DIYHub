package com.example.diyhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class SellerRegistrationPage extends AppCompatActivity {

    EditText username,password,firstname,lastname,birthdate,phone,address,email;
    RadioButton genderMale,genderFemale;
    TextView invalid22;
    Button registerbtn;
    String gender;
    Connection con;
    Statement stmt;
    String[] data;
    ImageView next;
    String count = "0";

    String UserGender,UserID;
    FirebaseFirestore dbFireStore;


    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("USERPROFILE");
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    int UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration_page);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        dbFireStore = FirebaseFirestore.getInstance();


        firstname = (EditText)findViewById(R.id.firstnameTxt3);
        lastname = (EditText)findViewById(R.id.lastnameTxt3);
        birthdate = (EditText)findViewById(R.id.birthdateTxt3);
        genderMale = (RadioButton)findViewById(R.id.radio_one);
        genderFemale = (RadioButton)findViewById(R.id.radio_two);
        phone = (EditText)findViewById(R.id.contactTxt3);
        address = (EditText)findViewById(R.id.addressTxt3);
        email = (EditText)findViewById(R.id.emailTxt3);
        username = (EditText)findViewById(R.id.usernameTxt3);
        password = (EditText)findViewById(R.id.passwordTxt3);
        invalid22 = (TextView) findViewById(R.id.textView22);
        next = findViewById(R.id.nextButton);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });
        new DateInputMask(birthdate);
        new PhoneTextFormatter(phone,"09055624759");



    }

    public class PhoneTextFormatter implements TextWatcher {

        private final String TAG = this.getClass().getSimpleName();

        private EditText mEditText;

        private String mPattern;

        public PhoneTextFormatter(EditText editText, String pattern) {
            mEditText = editText;
            mPattern = pattern;
            //set max length of string
            int maxLength = pattern.length();
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            StringBuilder phone = new StringBuilder(s);

            Log.d(TAG, "join");

            if (count > 0 && !isValid(phone.toString())) {
                for (int i = 0; i < phone.length(); i++) {
                    Log.d(TAG, String.format("%s", phone));
                    char c = mPattern.charAt(i);

                    if ((c != '#') && (c != phone.charAt(i))) {
                        phone.insert(i, c);
                    }
                }

                mEditText.setText(phone);
                mEditText.setSelection(mEditText.getText().length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        private boolean isValid(String phone)
        {
            for (int i = 0; i < phone.length(); i++) {
                char c = mPattern.charAt(i);

                if (c == '#') continue;

                if (c != phone.charAt(i)) {
                    return false;
                }
            }

            return true;
        }
    }

    public class DateInputMask implements TextWatcher {

        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();
        private EditText input;

        public DateInputMask(EditText input) {
            this.input = input;
            this.input.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().equals(current)) {
                return;
            }

            String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
            String cleanC = current.replaceAll("[^\\d.]|\\.", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8){
                clean = clean + ddmmyyyy.substring(clean.length());
            }else{
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int day  = Integer.parseInt(clean.substring(0,2));
                int mon  = Integer.parseInt(clean.substring(2,4));
                int year = Integer.parseInt(clean.substring(4,8));

                mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                cal.set(Calendar.MONTH, mon-1);
                year = (year<1900)?1900:(year>2100)?2100:year;
                cal.set(Calendar.YEAR, year);
                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                clean = String.format("%02d%02d%02d",day, mon, year);
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            sel = sel < 0 ? 0 : sel;
            current = clean;
            input.setText(current);
            input.setSelection(sel < current.length() ? sel : current.length());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void registerUser()
    {

        String UserFirstname,UserLastname,UserBirthdate,UserPhone,UserAddress,UserType,UserStatus,UserEmail,UserUsername,UserPassword;

        UserGender = "";

        if(genderMale.isChecked())
        {
            UserGender = genderMale.getText().toString();
        }
        else if(genderFemale.isChecked())
        {
            UserGender = genderFemale.getText().toString();
        }

        UserID = "";
        UserFirstname = firstname.getText().toString();
        UserLastname = lastname.getText().toString();
        UserBirthdate = birthdate.getText().toString();
        UserPhone = phone.getText().toString();
        UserAddress = address.getText().toString();
        UserType = "Seller";
        UserStatus = "Online";
        UserEmail = email.getText().toString();
        UserUsername = username.getText().toString();
        UserPassword = password.getText().toString();

        if(UserFirstname.isEmpty())
        {
            firstname.setError("First name is required!");
            firstname.requestFocus();
            return;
        }
        if(UserLastname.isEmpty())
        {
            lastname.setError("Last name is required!");
            lastname.requestFocus();
            return;
        }
        if(UserBirthdate.isEmpty())
        {
            birthdate.setError("Birthdate is required!");
            birthdate.requestFocus();
            return;
        }
        if(UserPhone.isEmpty())
        {
            phone.setError("Phone is required!");
            phone.requestFocus();
            return;
        }
        if(UserPhone.length() < 11)
        {
            phone.setError("Phone must be 11 digits!");
            phone.requestFocus();
            return;
        }
        if(UserAddress.isEmpty())
        {
            address.setError("Address is required!");
            address.requestFocus();
            return;
        }
        if(UserEmail.isEmpty())
        {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches())
        {
            email.setError("Please provide valid Email!");
            email.requestFocus();
            return;
        }
        if(UserUsername.isEmpty())
        {
            username.setError("Username is required!");
            username.requestFocus();
            return;
        }
        if(UserPassword.isEmpty())
        {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if(UserPassword.length() < 8)
        {
            password.setError("Password must have atleast 8 characters!");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(UserEmail,UserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SellerRegistrationPage.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    UserID = mAuth.getCurrentUser().getUid();
                    Intent intent = new Intent(SellerRegistrationPage.this, AccountVerificationPage.class);
                    intent.putExtra("UserIDSeller", UserID);
                    intent.putExtra("UserFirstnameSeller", UserFirstname);
                    intent.putExtra("UserLastnameSeller", UserLastname);
                    intent.putExtra("UserBirthdateSeller", UserBirthdate);
                    intent.putExtra("UserPhoneSeller", UserPhone);
                    intent.putExtra("UserAddressSeller", UserAddress);
                    intent.putExtra("UserGenderSeller", UserGender);
                    intent.putExtra("UserTypeSeller", UserType);
                    intent.putExtra("UserStatusSeller", UserStatus);
                    intent.putExtra("UserEmailSeller", UserEmail);
                    intent.putExtra("UserUsernameSeller", UserUsername);
                    intent.putExtra("UserPasswordSeller", UserPassword);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id",UserID);
                    map.put("imageUrl","");
                    map.put("status","offline");
                    map.put("username",UserFirstname+" "+UserLastname);
                    reference.child("Users").child(UserID).setValue(map);

                    DocumentReference documentReference = dbFireStore.collection("USERPROFILE").document(UserEmail);
                    Map<String,Object> user = new HashMap<>();
                    user.put("UserID",UserID);
                    user.put("UserFirstname",UserFirstname);
                    user.put("UserLastname",UserLastname);
                    user.put("UserBirthdate",UserBirthdate);
                    user.put("UserPhone",UserPhone);
                    user.put("UserAddress",UserAddress);
                    user.put("UserGender",UserGender);
                    user.put("UserType",UserType);
                    user.put("UserStatus",UserStatus);
                    user.put("UserEmail",UserEmail);
                    user.put("UserUsername",UserUsername);
                    user.put("UserPassword",UserPassword);
                    user.put("UserProfileImage","");
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG", "SUCCESS");


                            CollectionReference productRef = dbFireStore.collection("USERPROFILE").document(UserEmail).collection("SELLERPRODUCTS");
                            Map<String,Object> sellerProducts = new HashMap<>();
                            sellerProducts.put("ProductImage", "");
                            sellerProducts.put("ProductName", "");
                            sellerProducts.put("ProductQuantity", "");
                            sellerProducts.put("ProductStocks", "");
                            productRef.add(sellerProducts);
                            intent.putExtra("ProductSize",count);

                            progressBar.setVisibility(View.INVISIBLE);

                            firstname.setText("");
                            lastname.setText("");
                            birthdate.setText("");
                            phone.setText("");
                            address.setText("");
                            email.setText("");
                            username.setText("");
                            password.setText("");

                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }

                        }
                    });



                }
                else
                {
                    Toast.makeText(SellerRegistrationPage.this, "Email is already Registered!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }




}