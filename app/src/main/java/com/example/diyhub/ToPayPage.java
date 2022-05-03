package com.example.diyhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ToPayPage extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView notif,back;
    TextView favButton;

    String s1[], s2[];
    int images[] = {R.drawable.img_6,R.drawable.img_7,R.drawable.img_8,R.drawable.img_30,R.drawable.img_31,R.drawable.fb};
    int images1[] = {R.drawable.cart,R.drawable.cart,R.drawable.cart,R.drawable.cart,R.drawable.cart,R.drawable.cart};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_pay_page);

        recyclerView = findViewById(R.id.recyclerToPay);
        back = findViewById(R.id.backButtonToPay);

        s1 = getResources().getStringArray(R.array.item_name_toPay);
        s2 = getResources().getStringArray(R.array.amount);

        MyAdapterToPay myAdapterToPay = new MyAdapterToPay(this, s1,s2,images);
        recyclerView.setAdapter(myAdapterToPay);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

    }

    private void backPage() {
        Intent intent = new Intent(ToPayPage.this, ProfilePage.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}