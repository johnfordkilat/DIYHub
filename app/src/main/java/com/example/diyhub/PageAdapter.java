package com.example.diyhub;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.diyhub.Fragments.SellerHomeFragment;
import com.example.diyhub.Fragments.SellerOrdersFragment;
import com.example.diyhub.Fragments.SellerProductsFragment;
import com.example.diyhub.Fragments.SellerStatsFragment;

import java.util.ArrayList;


public class PageAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]{R.string.tab1, R.string.tab2, R.string.tab3, R.string.tab4};
    private final Context mContext;
    private ArrayList<String> data;

    public PageAdapter(Context context, FragmentManager fm, ArrayList<String> word) {
        super(fm);
        mContext = context;
        this.data = word;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position)
        {
            case 0:
                fragment = new SellerHomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username", data.get(0));
                bundle.putString("UserMotto", data.get(1));
                bundle.putString("locationSeller", data.get(2));
                bundle.putString("phoneSeller", data.get(3));
                bundle.putString("emailSeller", data.get(4));
                fragment.setArguments(bundle);

                break;
            case 1:
                fragment = new SellerProductsFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("emailSeller", data.get(4));
                fragment.setArguments(bundle1);

                break;
            case 2:
                fragment = new SellerOrdersFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("TabBelow", Integer.parseInt(data.get(5)));
                break;
            case 3:
                fragment = new SellerStatsFragment();

                break;
        }
        return fragment;
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return 4;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}
