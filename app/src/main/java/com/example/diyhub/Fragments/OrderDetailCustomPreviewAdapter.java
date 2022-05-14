package com.example.diyhub.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.diyhub.R;

import java.util.List;

public class OrderDetailCustomPreviewAdapter extends PagerAdapter {


    private Context context;
    private LayoutInflater layoutInflater;
    private List<ProductDetailsImagesList> images;

    public OrderDetailCustomPreviewAdapter(Context context, List<ProductDetailsImagesList> imagesLists) {
        this.context = context;
        this.images = imagesLists;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull View container, int position) {

        ProductDetailsImagesList list = images.get(position);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_preview_images, null);

        ImageView imageView = view.findViewById(R.id.customPreviewImage);
        Glide.with(context).load(list.getProductImage()).into(imageView);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);

    }
}
