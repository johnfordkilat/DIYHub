package com.example.diyhub.Notifications;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.diyhub.AllProductsAdapter;
import com.example.diyhub.AllProductsList;
import com.example.diyhub.R;
import com.example.diyhub.RestockProductsAdapter;
import com.example.diyhub.RestockProductsList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RestockFragment extends Fragment {

    TextView all,res,hold;
    int count=0;
    String emailSeller;
    RecyclerView allProductsRecyclerView;
    FirebaseFirestore dbFirestore;
    AllProductsAdapter allProductsAdapter;
    ArrayList<AllProductsList> allProductsLists;
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
    int pQuan,pStocks;
    RecyclerView restockProductRecycler;
    RestockProductsAdapter restockProductsAdapter;
    ArrayList<RestockProductsList> restockProductsList;

    View view;

    TabLayout tabLayout;
    ViewPager viewPager;

    Button goToOrderPage;
    EditText searchProduct;

    ArrayList<RestockProductsList> filterListRestock = new ArrayList<>();
    FirebaseUser user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_restock, container, false);

        restockProductRecycler = view.findViewById(R.id.restockFragmentRecycler);
        restockProductRecycler.setHasFixedSize(true);
        restockProductRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getContext());


        dbFirestore = FirebaseFirestore.getInstance();
        restockProductsList = new ArrayList<RestockProductsList>();

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
            showData();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));
        restockProductRecycler.addItemDecoration(dividerItemDecoration);

        return view;
    }

    public void showData()
    {
        progressDialog.setTitle("Loading Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        /*
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RestockNotification").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */

        dbFirestore.collection("USERPROFILE").document(user.getEmail()).collection("SELLERPRODUCTS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        restockProductsList.clear();

                        for(DocumentSnapshot doc : task.getResult())
                        {
                            pQuan = Integer.parseInt(doc.getString("ProductQuantity"));
                            pStocks = Integer.parseInt(doc.getString("ProductStocks"));

                            if(pQuan >= pStocks)
                            {
                                RestockProductsList restockList = new RestockProductsList(doc.getString("ProductName"),
                                        doc.getString("ProductQuantity"),
                                        doc.getString("ProductStocks"),
                                        doc.getString("ProductImage"),
                                        doc.getString("ProductID"),
                                        doc.getString("ProductStatusImage"),
                                        doc.getString("ProductStatus"));
                                restockProductsList.add(restockList);
                            }

                        }
                        restockProductsAdapter = new RestockProductsAdapter(getContext(), restockProductsList);

                        restockProductRecycler.setAdapter(restockProductsAdapter);
                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}