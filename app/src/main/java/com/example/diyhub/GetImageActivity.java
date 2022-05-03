package com.example.diyhub;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class GetImageActivity extends AppCompatActivity {

    ImageView imageView;
    Connection con;
    Statement stmt;
    ResultSet rs;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);

        imageView = (ImageView)findViewById(R.id.imageSql);
        textView = findViewById(R.id.sampleTxtview);

        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            con = connectionHelper.connectionClass();

            String query = "SELECT * FROM SHOP";

            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            if(rs.next()){
                String image = rs.getString(10);
                String status = rs.getString(5);

                byte[] bytes = Base64.decode(image,Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                imageView.setImageBitmap(bitmap);
                textView.setText(status);

            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}