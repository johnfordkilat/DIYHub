package com.example.diyhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.diyhub.Fragments.ProductDetailsImagesList;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapterProductDetailsStandard extends PagerAdapter {


    private Context context;
    private LayoutInflater layoutInflater;
    private List<ProductDetailsImagesList> images;

    public ViewPageAdapterProductDetailsStandard(Context context, List<ProductDetailsImagesList> imagesLists) {
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
        View view = layoutInflater.inflate(R.layout.product_details_images_standard, null);

        ImageView imageView = view.findViewById(R.id.imageViewProductDetailsStandard);
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
