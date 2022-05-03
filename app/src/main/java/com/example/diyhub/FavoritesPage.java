package com.example.diyhub;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoritesPage extends AppCompatActivity {

    RecyclerView dataList;
    ArrayList<String> itemName;
    ArrayList<String> shopName;
    ArrayList<Integer> images;
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_page);

        dataList = findViewById(R.id.cartRecyclerView);

        itemName = new ArrayList<>();
        shopName = new ArrayList<>();
        images = new ArrayList<>();

        itemName.add("First Item");
        itemName.add("Second Item");
        itemName.add("Third Item");
        itemName.add("Fourth Item");
        itemName.add("First Item");
        itemName.add("Second Item");
        itemName.add("Third Item");
        itemName.add("Fourth Item");

        shopName.add("Shop One");
        shopName.add("Shop Two");
        shopName.add("Shop Three");
        shopName.add("Shop Fourth");
        shopName.add("Shop One");
        shopName.add("Shop Two");
        shopName.add("Shop Three");
        shopName.add("Shop Fourth");

        images.add(R.drawable.img_20);
        images.add(R.drawable.img_21);
        images.add(R.drawable.img_22);
        images.add(R.drawable.img_23);
        images.add(R.drawable.img_20);
        images.add(R.drawable.img_21);
        images.add(R.drawable.img_22);
        images.add(R.drawable.img_23);




        adapter = new Adapter(this, itemName,shopName,images);

        int spanCount = 2; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = false;
        dataList.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);



    }
}

 class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}