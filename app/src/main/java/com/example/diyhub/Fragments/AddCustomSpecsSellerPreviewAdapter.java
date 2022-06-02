package com.example.diyhub.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.diyhub.R;

import java.util.List;

public class AddCustomSpecsSellerPreviewAdapter extends BaseAdapter {

    private Context context;
    private List<OrderCustomListSeller> varList;

    public AddCustomSpecsSellerPreviewAdapter(Context context, List<OrderCustomListSeller> varList) {
        this.context = context;
        this.varList = varList;
    }

    @Override
    public int getCount() {
        return varList != null ? varList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.custom_specs_layout, parent, false);
        ImageView bigImage = rootView.findViewById(R.id.imageVariationStandard);

        Glide.with(context).load(varList.get(position).getSpecsName()).into(bigImage);

        return rootView;
    }
}
