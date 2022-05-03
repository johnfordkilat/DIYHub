package com.example.diyhub.Fragments;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.diyhub.R;

import java.util.ArrayList;


public class PageAdapterProductsFragment extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]{R.string.AllTab, R.string.RestockTab, R.string.Hold};
    private final Context mContext;
    private ArrayList<String> data;

    public PageAdapterProductsFragment(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        return fragment;
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}

