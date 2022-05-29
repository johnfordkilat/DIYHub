package com.example.diyhub.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diyhub.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCustomSpecsSellerAdapter extends BaseAdapter {

    private Context context;
    private List<AddCustomSpecsSellerList> varList;

    public AddCustomSpecsSellerAdapter(Context context, List<AddCustomSpecsSellerList> varList) {
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

        View rootView = LayoutInflater.from(context).inflate(R.layout.product_variations_standard, parent, false);
        TextView txtLabel = rootView.findViewById(R.id.variationLabelStandard);
        ImageView bigImage = rootView.findViewById(R.id.imageVariationStandard);

        txtLabel.setText(varList.get(position).getSpecsLabel());
        Glide.with(context).load(varList.get(position).getBigImage()).into(bigImage);

        return rootView;
    }
}
