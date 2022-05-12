package com.example.diyhub.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.diyhub.AllProductsAdapter;
import com.example.diyhub.AllProductsList;
import com.example.diyhub.LoginPage;
import com.example.diyhub.Notifications.Data;
import com.example.diyhub.PastTransaction.PastTransactionHistory;
import com.example.diyhub.R;
import com.example.diyhub.RestockProductsAdapter;
import com.example.diyhub.RestockProductsList;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import kotlin.Pair;

public class SellerHomeFragment extends Fragment {



    TextView changeP;
    private static final int SELECT_PHOTOGOV = 1;

    Uri imageUri1,imageUri2,imageUri3;
    ProgressDialog progressDialog;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    int index = 0;
    private int uploads = 0;
    private int success = 0;
    ImageView profPic;
    String usernameSeller,emailSeller;
    FirebaseAuth mAuth;
    String myUri;
    FirebaseFirestore dbFirestore;
    String value1,value2,value3,value4,value5,value6,value7,value8,value9,value10,value11,value12;
    TextView shopN,locSeller,phoneSeller;
    String locSell,phoneSell;
    ImageView products,orders,stats;
    TextView logout;
    String productSize;
    ImageView toOrderPage;
    TextView shopMotto;
    String shMotto;
    String userType;
    Button viewHistory;
    View view;
    String selectedItemText;
    String[] payAcc;
    List<String> payA;
    AlertDialog dialog;

    Spinner paymentSpinner;
    DatabaseReference reference;
    Button setPaymentOptions;
    EditText optionText;
    Button submit;
    ValueEventListener listener;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    String[] options = {"Choose Payment Option"};
    int position = 0;
    List<PaymentOptions> paymentOptionsList;

    Button removePayment;
    AlertDialog removeDialog;

    String[] removeList;

    String toDelete="";
    
    Button upgradeToPremium;


    public SellerHomeFragment() {
        // Required empty public constructor
    }

    EditText subscription;
    Dialog customDialog;
    Spinner accountsVerSpinner, paymentAccountsSpinner;
    ArrayList<String> listAccVer;

    ArrayAdapter<String> adapterAccVer;
    String[] allAccVerList;
    ImageView premiumLogo, editShopName;
    
    Spinner bookingSpinner;
    Button addBooking;

    ArrayList<String> listBooking;
    ArrayAdapter<String> adapterBooking;
    boolean upgraded = false;
    AlertDialog cancelSubs;
    Dialog bookingDialog;

    EditText optionTxtBooking;
    Button submitBooking;

    DatabaseReference referenceBooking;
    ArrayAdapter<String> adapterBookingOptions;

    ValueEventListener listenerBooking;

    String paymentBackendUrl;

    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;
    private PaymentSheet.CustomerConfiguration customerConfig;
    private ProgressDialog dialogPayment;

    boolean paymentSuccess = false;

    String id;
    String cutid;

    List<SubscriptionList> subsList;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();

        if(bundle != null)
        {
            usernameSeller = bundle.getString("username");
            emailSeller = bundle.getString("emailSeller");
            locSell = bundle.getString("locationSeller");
            phoneSell = bundle.getString("phoneSeller");
            value1 = bundle.getString("UserIDSeller");
            value2= bundle.getString("UserFirstnameSeller");
            value3 = bundle.getString("UserLastnameSeller");
            value4 = bundle.getString("UserBirthdateSeller");
            value5 = bundle.getString("UserPhoneSeller");
            value6 = bundle.getString("UserAddressSeller");
            value7 = bundle.getString("UserGenderSeller");
            value8 = bundle.getString("UserTypeSeller");
            value9 = bundle.getString("UserStatusSeller");
            value10 = bundle.getString("UserEmailSeller");
            value11 = bundle.getString("UserUsernameSeller");
            value12 = bundle.getString("UserPasswordSeller");
            productSize = bundle.getString("ProductSize");
            shMotto = bundle.getString("UserMotto");
            userType = bundle.getString("UserTypeSeller");
        }

        view = inflater.inflate(R.layout.fragment_seller_home, container, false);

        //profile = findViewById(R.id.profileImage);
        changeP = view.findViewById(R.id.changePhoto);
        profPic = view.findViewById(R.id.sellerProfilePic);
        dbFirestore = FirebaseFirestore.getInstance();
        shopN = view.findViewById(R.id.shopNameTxt2);
        locSeller = view.findViewById(R.id.locationSellerTxtView);
        phoneSeller = view.findViewById(R.id.phoneSellerTxtView);
        //products = findViewById(R.id.productsSeller);
        //orders = findViewById(R.id.ordersSeller);
        //stats = findViewById(R.id.statsSeller);
        logout = view.findViewById(R.id.logoutTxtview2);
        //toOrderPage = findViewById(R.id.ordersSeller);
        shopMotto = view.findViewById(R.id.shopMottoTxt);
        viewHistory = view.findViewById(R.id.viewHistoryButton);
        paymentSpinner = view.findViewById(R.id.PaymentOptionsSpinner);
        setPaymentOptions = view.findViewById(R.id.addPaymentOption);
        removePayment = view.findViewById(R.id.removePaymentOption);
        upgradeToPremium = view.findViewById(R.id.upgradeToPremiumButton);
        subscription = view.findViewById(R.id.subscriptionTxt);
        accountsVerSpinner = view.findViewById(R.id.accountsVerifiedSpinner);
        premiumLogo = view.findViewById(R.id.premiumLogoImage);
        editShopName = view.findViewById(R.id.editShopNameButton);
        bookingSpinner = view.findViewById(R.id.bookingOptionSpinner);
        addBooking = view.findViewById(R.id.addBookingOption);
        customDialog = new Dialog(getContext());

        paymentBackendUrl  = getResources().getString(R.string.paymentBackendUrl);
        paymentSheet = new PaymentSheet(this, this::onPaymentResult);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

         id = UUID.randomUUID().toString();
         cutid = id.substring(0,11);


        reference = FirebaseDatabase.getInstance().getReference("PaymentOptions").child(user.getUid());
        referenceBooking = FirebaseDatabase.getInstance().getReference("BookingOptions").child(user.getUid());

        String[] allList = getActivity().getResources().getStringArray(R.array.Payment_Accounts);
        String[] allListBooking = getActivity().getResources().getStringArray(R.array.Booking_Options);
        listBooking = new ArrayList<>();

        list = new ArrayList<String>();
        list.add(0, "Choose Payment Method");
        
        addBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderBooking = new AlertDialog.Builder(getContext());
                builderBooking.setTitle("Add Booking Option");
                builderBooking.setCancelable(false);
                builderBooking.setSingleChoiceItems(allListBooking, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        optionTxtBooking.setText(allListBooking[i]);
                    }
                });

                View view1 = getLayoutInflater().inflate(R.layout.layout_dialog_booking_options, null);
                optionTxtBooking = view1.findViewById(R.id.setPaymentOptionTxtBooking);
                submitBooking = view1.findViewById(R.id.submitOptionBooking);
                builderBooking.setView(view1);


                submitBooking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String data = optionTxtBooking.getText().toString().trim();
                        if(data.isEmpty())
                        {
                            optionTxtBooking.setError("Cannot be Empty");
                            optionTxtBooking.requestFocus();
                        }
                        else
                        {
                            bookingDialog.dismiss();
                            insertDataBookingOptions(data);
                            Toast.makeText(getContext(), "Added: " + data, Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                bookingDialog = builderBooking.create();
                bookingDialog.show();
            }
        });







        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, list)
        {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        paymentSpinner.setAdapter(adapter);

        upgradeToPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(upgraded == true){
                    AlertDialog.Builder builderRemove = new AlertDialog.Builder(getContext());
                    builderRemove.setTitle("Cancel Subscription");
                    builderRemove.setMessage("Are you sure you want to cancel your PREMIUM SUBSCRIPTION?");
                    builderRemove.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            upgraded = false;
                            cancelSubs.dismiss();
                            Toast.makeText(getContext(), "Cancelled Successfully!", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Map<String,Object> map = new HashMap<>();
                            map.put("SubscriptionStatus", "Regular");
                            reference.child("Subscription").child(user.getUid()).updateChildren(map);
                            getSubscriptionStatus();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    cancelSubs = builderRemove.create();
                    builderRemove.show();

                }
                else{
                    openDialog();
                }

            }
        });



        

        //Log.d("emalseller", emailSeller);


        mAuth = FirebaseAuth.getInstance();



        if(user != null)
        {
            dbFirestore.collection("USERPROFILE").document(user.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                DocumentSnapshot doc = task.getResult();
                                if(doc.exists())
                                {
                                    String shopname = doc.getString("UserFirstname") + " " + doc.getString("UserLastname");
                                    String location = doc.getString("UserAddress");
                                    String phone = doc.getString("UserPhone");
                                    String motto = doc.getString("UserMotto");

                                    shopN.setText(shopname);
                                    locSeller.setText("  Location: "+ location);
                                    phoneSeller.setText("  Contact Number: "+phone);
                                    shopMotto.setText(motto);
                                    if(user.getPhotoUrl() != null)
                                    {
                                        Glide.with(getContext())
                                                .load(user.getPhotoUrl())
                                                .into(profPic);
                                    }
                                }
                            }
                        }
                    });

        }

        changeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTOGOV);
            }
        });

        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PastTransactionHistory.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("LOGOUT CONFIRMATION");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        signoutUser();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();


            }
        });

        paymentOptionsList = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Payment Option");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(allList, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                optionText.setText(allList[i]);
            }
        });


        View view1 = getLayoutInflater().inflate(R.layout.layout_dialog, null);
        optionText = view1.findViewById(R.id.setPaymentOptionTxt);
        submit = view1.findViewById(R.id.submitOption);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = optionText.getText().toString().trim();
                if(data.isEmpty())
                {
                    optionText.setError("Cannot be Empty");
                    optionText.requestFocus();
                }
                else
                {
                    dialog.dismiss();
                    insertData(data);
                }


            }
        });
        fetchData();
        fetchDataBooking();

        paymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    toDelete = "";
                    if(position > 0)
                    {
                        String item = parent.getItemAtPosition(position).toString();
                        Toast.makeText(getContext(), "Selected: "+item, Toast.LENGTH_SHORT).show();
                        toDelete = item;
                    }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view1);
        dialog = builder.create();
        setPaymentOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        removeList = new String[list.size()];
        Log.d("ListSize", String.valueOf(list.size()));




        AlertDialog.Builder builderRemove = new AlertDialog.Builder(getContext());
        builderRemove.setTitle("Remove Payment Option");
        builderRemove.setMessage("Are you sure you want to delete " + toDelete + "?");
        builderRemove.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeData(toDelete);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        removeDialog = builderRemove.create();


        removePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toDelete.equals(""))
                {
                    Toast.makeText(getContext(), "Please choose item to DELETE", Toast.LENGTH_SHORT).show();
                }
                else
                removeDialog.show();
            }
        });
        
        editShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Edit Shop Name", Toast.LENGTH_SHORT).show();
            }
        });





        displayAccountsVerified();
        displayBookingOption();
        getSubscriptionStatus();

        return view;
    }

    private void getSubscriptionStatus()
    {
        subsList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Subscription");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    SubscriptionList subscriptionList = snapshot.getValue(SubscriptionList.class);
                    if(subscriptionList.getSubscriptionStatus().equalsIgnoreCase("Premium"))
                    {
                        subscription.setText("PREMIUM ACCOUNT");
                        subscription.setBackgroundResource(R.drawable.custom_green);
                        premiumLogo.setVisibility(View.VISIBLE);
                        customDialog.dismiss();
                        upgradeToPremium.setText("Cancel Subscription");
                        upgradeToPremium.setBackgroundResource(R.drawable.edittext_border_red);
                        upgraded = true;
                        subsList.add(subscriptionList);
                    }
                    else
                    {
                        upgradeToPremium.setText("Upgrade To Premium");
                        upgradeToPremium.setBackgroundResource(R.drawable.custom_green);
                        subscription.setText("NO SUBSCRIPTION");
                        subscription.setBackgroundResource(R.drawable.edittext_border_red);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void displayAccountsVerified(){
        listAccVer = new ArrayList<>();
        listAccVer.add(0, "Accounts Verified");
        allAccVerList = getActivity().getResources().getStringArray(R.array.Accounts_Verified);

        adapterAccVer = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, listAccVer)
        {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        accountsVerSpinner.setAdapter(adapterAccVer);

        accountsVerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getContext(), "Selected: "+item, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void displayBookingOption(){
        listBooking.add(0, "Booking Options");

        adapterBooking = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, listBooking)
        {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        bookingSpinner.setAdapter(adapterBooking);

        bookingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getContext(), "Selected: "+item, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void openDialog() {
        customDialog.setContentView(R.layout.upgrade_layout_dialog);
        customDialog.setCancelable(false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView close = customDialog.findViewById(R.id.imageViewClose);
        Button upgrade = customDialog.findViewById(R.id.upgradeButton);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                upgraded = false;
            }
        });

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCheckout();
            }
        });

        customDialog.show();

    }


    private void insertData(String word) {


        if(list.contains(word.toUpperCase(Locale.ROOT)))
        {
            Toast.makeText(getContext(), "Payment Method Already Exist!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            reference.child("id"+word.toUpperCase(Locale.ROOT)).setValue(word.toUpperCase(Locale.ROOT))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            optionText.setText("");
                            list.clear();
                            list.add(0, "Choose Payment Method");
                            fetchData();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Added: "+word, Toast.LENGTH_SHORT).show();
                        }
                    }); 
        }
    }
    private void insertDataBookingOptions(String word) {


        if(listBooking.contains(word.toUpperCase(Locale.ROOT)))
        {
            Toast.makeText(getContext(), "Booking Option Already Exist!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            referenceBooking.child("id"+word.toUpperCase(Locale.ROOT)).setValue(word.toUpperCase(Locale.ROOT))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            optionTxtBooking.setText("");
                            listBooking.clear();
                            listBooking.add(0, "Choose Booking Option");
                            fetchDataBooking();
                            adapterBooking.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Added: "+word, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void removeData(String word) {

        Query query = reference.child("id"+word);
        Log.d("Queryremove", query.toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        list.clear();
                        list.add(0, "Choose Payment Method");
                        adapter.notifyDataSetChanged();
                        fetchData();
                        Toast.makeText(getContext(), "Deleted: "+word, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchDataBooking() {
        listenerBooking = referenceBooking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot myData : snapshot.getChildren())
                {
                    listBooking.add(myData.getValue().toString());
                }
                adapterBooking.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchData() {
        listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot myData : snapshot.getChildren())
                {
                    list.add(myData.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        if(requestCode == SELECT_PHOTOGOV && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null)
        {
            imageUri1 = data.getData();
            profPic.setImageURI(imageUri1);
            ImageList.add(imageUri1);
            uploadImage();

        }
    }

    private void uploadImage() {
        Log.d("UPLOADINGERROR", "IMAGEERROR");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Changing Profile....");
        progressDialog.show();

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child(emailSeller);
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
                            setProfileSeller(uri);
                            DocumentReference documentReference = dbFirestore.collection("USERPROFILE").document(emailSeller);
                            Map<String,Object> user = new HashMap<>();
                            user.put("UserProfileImage",url);
                            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void setProfileSeller(Uri uri)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        firebaseUser.updateProfile(request)
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

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(getContext(), "Payment Cancelled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(getContext(), "Error Upgrading Account", Toast.LENGTH_SHORT).show();
            upgraded = false;
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            PaymentSheetResult.Completed data = (PaymentSheetResult.Completed) paymentSheetResult;
            DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy hh.mm aa");
            String currentDateAndTime = dateFormat2.format(new Date()).toString();
            Toast.makeText(getContext(), "Upgraded To Premium", Toast.LENGTH_SHORT).show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Map<String, Object> map = new HashMap<>();
            map.put("SubscriptionStatus","Premium");
            map.put("PaymentReference", user.getUid()+"-"+cutid);
            map.put("DateAndTime", currentDateAndTime);
            reference.child("Payments").child(user.getUid()).child(user.getUid()+"-"+cutid).setValue(map);

            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
            Map<String,Object> map1 = new HashMap<>();
            map1.put("PaymentReference", user.getUid()+"-"+cutid);
            map1.put("DateAndTime", currentDateAndTime);
            map1.put("SubscriptionStatus", "Premium");
            reference1.child("Subscription").child(user.getUid()).updateChildren(map1);
            upgraded = true;

        }
    }

    private void processCheckout() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dialogPayment = new ProgressDialog(getContext());
        dialogPayment.setCancelable(false);
        dialogPayment.setMessage("Processing...");
        dialogPayment.show();

        // TODO: Poy, ibutang dire ang amount
        // Note: The amount should be a whole number with the last two digits denoting the cents
        // For example:
        // If the amount is P5.99, send the value as 599
        // If the amount is P5.00, send the value as 500
        List<Pair<String, Integer>> params = Arrays.asList(
                new Pair("amount", 10000), // the total amount due
                new Pair("description", user.getUid()+"-"+cutid)); // order description

        Fuel.INSTANCE.post(paymentBackendUrl, params).responseString(new Handler<String>() {
            @Override
            public void success(String s) {
                try {
                    final JSONObject result = new JSONObject(s);
                    customerConfig = new PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                    );
                    paymentIntentClientSecret = result.getString("paymentIntent");
                    PaymentConfiguration.init(getContext(), result.getString("publishableKey"));

                    showPaymentSheet();
                } catch (JSONException e) {
                    Log.e("Error", e.getMessage());
                } finally {
                    dialogPayment.dismiss();
                    customDialog.dismiss();
                }
            }

            @Override
            public void failure(@NonNull FuelError fuelError) {
                Log.e("Error", fuelError.getMessage());
                dialogPayment.dismiss();
                customDialog.dismiss();
            }
        });
    }

    private void showPaymentSheet() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Example, Inc.")
                .customer(customerConfig)
                // Set `allowsDelayedPaymentMethods` to true if your business can handle payment methods
                // that complete payment after a delay, like SEPA Debit and Sofort.
                .allowsDelayedPaymentMethods(true)
                .build();

        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );

    }



}