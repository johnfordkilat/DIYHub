package com.example.diyhub.PastTransaction;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.diyhub.R;


public class PageAdapterTransactionHistory extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]{R.string.pastTab1, R.string.pastTab2, R.string.pastTab3};
    private final Context mContext;



    public PageAdapterTransactionHistory(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position)
        {
            case 0:
                fragment = new CompletedOrderFragment();

                break;
            case 1:
                fragment = new CancelledOrderFragment();

                break;
            case 2:
                fragment = new ReturnRefundOrderFragment();

                break;
        }
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

