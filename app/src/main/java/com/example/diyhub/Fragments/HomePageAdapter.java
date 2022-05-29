package com.example.diyhub.Fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.diyhub.Nearby.BuyerLocationFragment;
import com.example.diyhub.R;

import java.util.ArrayList;

public class HomePageAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]{R.string.homeTab1, R.string.homeTab2,R.string.homeTab3, R.string.homeTab4, R.string.homeTab5};
    private final Context nContext;
    private ArrayList<String> data;

    public HomePageAdapter(Context context, FragmentManager fm) {
        super(fm);
        nContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position)
        {
            case 0:
                fragment = new BuyerHomeFragment();
                break;
            case 1:
                fragment = new BuyerCartFragment();
                break;
            case 2:
                fragment = new BuyerDiyFragment();
                break;
            case 3:
                fragment = new BuyerProfileFragment();
                break;
            case 4:
                fragment = new BuyerLocationFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return nContext.getResources().getString(TAB_TITLES[position]);
    }
}
