package com.example.diyhub.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diyhub.AllProductsBuyerAdapter;
import com.example.diyhub.AllProductsList;
import com.example.diyhub.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddBookingAddressBuyer extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    EditText address, landmark;
    Button submitAddress, cancelDialog;
    FloatingActionButton addAddress;

    RecyclerView recyclerViewAddBooking;
    AddBookingAddressAdapter addressAdapter;
    ArrayList<AddBookingModel> list;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_booking_address_buyer);

        recyclerViewAddBooking = findViewById(R.id.bookingAddRV);
        addAddress = findViewById(R.id.addAddress_BTN);
        db = FirebaseDatabase.getInstance().getReference().child("BookingAddress");

        recyclerViewAddBooking.setHasFixedSize(true);
        recyclerViewAddBooking.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    AddBookingModel addBookingList = dataSnapshot.getValue(AddBookingModel.class);
                    list.add(addBookingList);
                }
                addressAdapter = new AddBookingAddressAdapter(AddBookingAddressBuyer.this,list);
                recyclerViewAddBooking.setAdapter(addressAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

    }

    public void createDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View addPopupView = getLayoutInflater().inflate(R.layout.address_popup, null);

        address = addPopupView.findViewById(R.id.newAddress);
        landmark = addPopupView.findViewById(R.id.newLandMark);

        submitAddress = addPopupView.findViewById(R.id.submitDialog);
        cancelDialog = addPopupView.findViewById(R.id.cancelDialog);

        dialogBuilder.setView(addPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        submitAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newAddress = address.getText().toString();
                String newLandmark = landmark.getText().toString();

                AddBookingModel addBookingModel = new AddBookingModel(newAddress,newLandmark);

                db.push().setValue(addBookingModel);
                Toast.makeText(AddBookingAddressBuyer.this, "Address Has Been Added", Toast.LENGTH_SHORT).show();
            }
        });

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



}
