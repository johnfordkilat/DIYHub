package com.example.diyhub;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyhub.Fragments.OrdersAdapter;
import com.example.diyhub.Fragments.OrdersList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class OrdersPage extends AppCompatActivity {

    TextView all,res,hold;
    int count=0;
    String emailSeller;
    RecyclerView recyclerView;
    FirebaseFirestore dbFirestore;
    OrdersAdapter ordersAdapter;
    ArrayList<OrdersList> ordersLists;
    ProgressDialog progressDialog;
    Button uploadProduct;

    Uri imageUri1,imageUri2,imageUri3;
    StorageReference storageReference1,storageReference2,storageReference3;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    int index = 0;
    private int uploads = 0;
    private int success = 0;
    ImageView profPic;
    String usernameSeller;
    FirebaseAuth mAuth;
    String myUri;
    private DatabaseReference databaseReference;
    String value1,value2,value3,value4,value5,value6,value7,value8,value9,value10,value11,value12;
    TextView shopN,locSeller,phoneSeller;
    String locSell,phoneSell;
    ImageView products,orders,stats;
    String productName,productQuantity,productStocks;
    private static final int SELECT_PHOTOGOV = 1;
    int dialog = 0;
    TextView noProduct;
    Button toOrderPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_page);


        recyclerView = findViewById(R.id.toPayRecyclerSeller);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbFirestore = FirebaseFirestore.getInstance();
        ordersLists = new ArrayList<OrdersList>();
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    }

    /*
    public void showData()
    {
        String sellerEmail = mAuth.getCurrentUser().getEmail();
        progressDialog.setTitle("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        dbFirestore.collection("USERPROFILE").document(sellerEmail).collection("BUYERORDERS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ordersLists.clear();
                        progressDialog.dismiss();
                        for(DocumentSnapshot doc : task.getResult())
                        {
                            OrdersList list = new OrdersList(doc.getString("ProductName"),
                                    doc.getString("OrderQuantity"),
                                    doc.getString("ProductImage"),
                                    doc.getString("OrderID"),
                                    doc.getString("OrderType"),
                                    doc.getString("OrderPaymentMethod"));

                            ordersLists.add(list);
                        }
                        ordersAdapter = new OrdersAdapter(OrdersPage.this,ordersLists);
                        recyclerView.setAdapter(ordersAdapter);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

     */
}