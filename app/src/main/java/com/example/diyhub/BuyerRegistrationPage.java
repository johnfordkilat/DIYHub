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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BuyerRegistrationPage extends AppCompatActivity {

    EditText username,password,firstname,lastname,birthdate,phone,address,email;
    RadioButton genderMale,genderFemale;
    TextView invalid17;
    Button registerbtn;
    String UserGender,UserID;
    String[] data;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    int UID;
    FirebaseFirestore dbFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_registration_page);


        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        dbFireStore = FirebaseFirestore.getInstance();

        firstname = (EditText)findViewById(R.id.firstnameTxt4);
        lastname = (EditText)findViewById(R.id.lastnameTxt4);
        birthdate = (EditText)findViewById(R.id.birthdateTxt4);
        genderMale = (RadioButton)findViewById(R.id.radio_one);
        genderFemale = (RadioButton)findViewById(R.id.radio_two);
        phone = (EditText)findViewById(R.id.contactTxt4);
        address = (EditText)findViewById(R.id.addressTxt4);
        email = (EditText)findViewById(R.id.emailTxt4);
        username = (EditText)findViewById(R.id.usernameTxt4);
        password = (EditText)findViewById(R.id.passwordTxt4);
        registerbtn = (Button)findViewById(R.id.registerBtn);
        invalid17 = (TextView) findViewById(R.id.textView17);





        registerbtn.setOnClickListener(new View.OnClickListener() {
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
        UserType = "Buyer";
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
                    UserID = mAuth.getCurrentUser().getUid();
                    Intent intent = new Intent(BuyerRegistrationPage.this, MainActivity.class);
                    intent.putExtra("UserIDBuyer", UserID);
                    intent.putExtra("UserFirstnameBuyer", UserFirstname);
                    intent.putExtra("UserLastnameBuyer", UserLastname);
                    intent.putExtra("UserBirthdateBuyer", UserBirthdate);
                    intent.putExtra("UserPhoneBuyer", UserPhone);
                    intent.putExtra("UserAddressBuyer", UserAddress);
                    intent.putExtra("UserGenderBuyer", UserGender);
                    intent.putExtra("UserTypeBuyer", UserType);
                    intent.putExtra("UserStatusBuyer", UserStatus);
                    intent.putExtra("UserEmailBuyer", UserEmail);
                    intent.putExtra("UserUsernameBuyer", UserUsername);
                    intent.putExtra("UserPasswordBuyer", UserPassword);
                    DocumentReference documentReference = dbFireStore.collection("USERPROFILE").document(UserEmail);
                    Map<String,Object> userBuyer = new HashMap<>();
                    userBuyer.put("UserID",UserID);
                    userBuyer.put("UserFirstname",UserFirstname);
                    userBuyer.put("UserLastname",UserLastname);
                    userBuyer.put("UserBirthdate",UserBirthdate);
                    userBuyer.put("UserPhone",UserPhone);
                    userBuyer.put("UserAddress",UserAddress);
                    userBuyer.put("UserGender",UserGender);
                    userBuyer.put("UserType",UserType);
                    userBuyer.put("UserStatus",UserStatus);
                    userBuyer.put("UserEmail",UserEmail);
                    userBuyer.put("UserUsername",UserUsername);
                    userBuyer.put("UserPassword",UserPassword);
                    userBuyer.put("UserProfileImage","");
                    documentReference.set(userBuyer).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG", "SUCCESS");
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(BuyerRegistrationPage.this, "Registered Successfully", Toast.LENGTH_SHORT).show();


                            firstname.setText("");
                            lastname.setText("");
                            birthdate.setText("");
                            phone.setText("");
                            address.setText("");
                            email.setText("");
                            username.setText("");
                            password.setText("");

                            startActivity(intent);


                        }
                    });

                }
                else
                {
                    Toast.makeText(BuyerRegistrationPage.this, "Email is already Registered!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}
