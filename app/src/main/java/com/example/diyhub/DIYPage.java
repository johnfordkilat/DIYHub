package com.example.diyhub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

public class DIYPage extends AppCompatActivity {

    EditText editText;
    Button button,qrbutton;
    ImageView imageView;

    TextView textView;

    Connection connect;
    Statement stmt;

    int SELECT_PHOTO = 1;
    Uri uri;
    ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diypage);

        editText = (EditText)findViewById(R.id.edittext);
        button = (Button)findViewById(R.id.chooseButton);
        qrbutton = (Button)findViewById(R.id.qrbutton);

        Button choose = findViewById(R.id.chooseButton);
        imgView = findViewById(R.id.chooseImageView);

        textView = (TextView)findViewById(R.id.status);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });

        qrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DIYPage.sendImage().execute("");
            }
        });
    }

    public class sendImage extends AsyncTask<String,String ,String> {

        @Override
        protected String doInBackground(String... strings) {
            String s = "false";
            int id=2,followers=100,products=5,sellerID=1;
            String name="pordshop",status="online",address="cebu",verified="facebook";
            float rating= 2.5F;
            try{
                textView.setText("Sending Image to Database");
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                String image = Base64.encodeToString(bytes , Base64.DEFAULT);

                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionClass();
                String query = "insert into SHOP (ShopID,ShopName,ShopRating,ShopFollowers,ShopStatus,ShopProducts,ShopAddress,ShopVerifiedAccounts,SellerID,ShopImage) values ('"+id+"','"+name+"','"+rating+"','"+followers+"','"+status+"','"+products+"','"+address+"','"+verified+"','"+sellerID+"','"+image+"')";
                stmt = connect.createStatement();
                stmt.executeUpdate(query);
                s  = "true";
                textView.setText("Image Send to Database Success");
            }catch (Exception e){
                e.printStackTrace();
            }

            return s;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imgView.setImageBitmap(bitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }


}