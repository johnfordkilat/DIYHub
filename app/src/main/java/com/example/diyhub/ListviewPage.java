package com.example.diyhub;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ListviewPage extends AppCompatActivity {

    ListView listView;

    int[] images = {
                    R.drawable.img_1,
                    R.drawable.img_1,
                    R.drawable.img_1,
                    R.drawable.img_1,
                    R.drawable.img_1,
                    R.drawable.img_1,
                    R.drawable.img_1,
                    R.drawable.img_1,
                    R.drawable.img_1,
                    R.drawable.img_1,
                    R.drawable.img_2,
                    R.drawable.img_3,
                    R.drawable.img_4,
                    R.drawable.img_5,
                    R.drawable.img_6,
                    R.drawable.img_7,
                    R.drawable.img_8};

    String[] names = {"one",
                      "two",
                      "three",
                      "four",
                      "five",
                      "six",
                      "seven",
                      "eight",
                      "nine",
                    "one",
                    "two",
                    "three",
                    "four",
                    "five",
                    "six",
                    "seven",
                    "eight",
                    "nine"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_page);

        listView = (ListView) findViewById(R.id.itemListview);

        CustomAdaptor customAdaptor = new CustomAdaptor();
        listView.setAdapter(customAdaptor);


    }

    class CustomAdaptor extends BaseAdapter{

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.customlayout, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.itemImage);
            TextView textView = (TextView) view.findViewById(R.id.itemNameTxtview);

            imageView.setImageResource(images[position]);
            textView.setText(names[position]);
            return view;
        }
    }
}