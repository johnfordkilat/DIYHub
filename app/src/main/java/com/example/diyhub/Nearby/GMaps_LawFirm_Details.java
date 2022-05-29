package com.example.diyhub.Nearby;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.diyhub.Fragments.ShopsList;
import com.example.diyhub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

public class GMaps_LawFirm_Details extends AppCompatActivity {
    //Variables
    private String selectedUID;
    private CircularImageView gmapsDialogLawyerProfileImage;
    private TextView id_txtview_Lawyer_Name, id_txtview_Lawyer_Address, id_txtview_Lawyer_Distance;
    private String name;
    private Button lawyerInformationViewInfo;
    private RatingBar GmapsRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmaps_lawyer_details);
        selectedUID = getIntent().getStringExtra("LAWFIRM_ID");
        initialize();
        //Profile information
        //User Information
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Shops").child(selectedUID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ShopsList lawFirm = snapshot.getValue(ShopsList.class);

                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                int width = dm.widthPixels;
                int height = dm.heightPixels;

                getWindow().setLayout((int) (width*.8),(int) (ListPopupWindow.WRAP_CONTENT));

                id_txtview_Lawyer_Name.setText(lawFirm.getShopName());
                id_txtview_Lawyer_Address.setText(lawFirm.getShopAddress());
                GmapsRatingBar.setRating(Float.parseFloat(String.valueOf(lawFirm.getShopRating())));
                if(lawFirm.getShopImage().equals("")) {
                    gmapsDialogLawyerProfileImage.setBackgroundResource(R.drawable.diy);
                }
                else {
                    if (getApplicationContext() != null) {
                        Glide.with(getApplicationContext()).load(lawFirm.getShopImage()).diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(true).circleCrop().into(gmapsDialogLawyerProfileImage);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        lawyerInformationViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), Activity_LawFirm_Information.class);
                intent.putExtra("LAWFIRM_ID", selectedUID);
                startActivity(intent);
            }
        });

         */
    }

    public void initialize() {
        gmapsDialogLawyerProfileImage = findViewById(R.id.gmapsDialogLawyerProfileImage);
        id_txtview_Lawyer_Name = findViewById(R.id.id_txtview_Lawyer_Name);
        id_txtview_Lawyer_Distance = findViewById(R.id.id_txtview_Lawyer_Distance);
        lawyerInformationViewInfo = findViewById(R.id.lawyerInformationViewInfo);
        id_txtview_Lawyer_Address = findViewById(R.id.id_txtview_Lawyer_Address);
        GmapsRatingBar = findViewById(R.id.GmapsRatingBar);
    }
}