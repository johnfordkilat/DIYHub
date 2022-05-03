package com.example.diyhub.PastTransaction;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.diyhub.Fragments.OrdersAdapter;
import com.example.diyhub.Fragments.OrdersList;
import com.example.diyhub.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CompletedOrderFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CompletedOrderFragment() {
        // Required empty public constructor
    }

    public static CompletedOrderFragment newInstance(String param1, String param2) {
        CompletedOrderFragment fragment = new CompletedOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    View view;
    RecyclerView recyclerViewCompleted;
    ArrayList<CompletedOrderList> completedOrderLists;
    CompletedOrderRecyclerAdapter completedOrderRecyclerAdapter;

    TextView all,res,hold;
    int count=0;
    String emailSeller;
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
    RecyclerView recyclerViewCancelled;
    RecyclerView recyclerViewReturnRefund;
    TabLayout tabLayout;
    ViewPager viewPager;


    private int[] tabIcons = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_products1,
            R.drawable.ic_orders1,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_completed_order, container, false);

        FragmentActivity completedOrderFragment = getActivity();









        return view;
    }


}