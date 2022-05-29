package com.example.diyhub.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.diyhub.R;

import java.util.ArrayList;

public class StandardProductBuyerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]{R.string.standard, R.string.custom};
    private final Context nContext;
    private ArrayList<String> data;

    public StandardProductBuyerAdapter(Context context, FragmentManager fm) {
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
                fragment = new StandardBuyerFragment();

                break;
            case 1:
                fragment = new CustomBuyerFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return nContext.getResources().getString(TAB_TITLES[position]);
    }
}