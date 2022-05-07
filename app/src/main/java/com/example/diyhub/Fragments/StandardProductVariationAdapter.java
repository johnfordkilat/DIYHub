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

public class StandardProductVariationAdapter extends BaseAdapter {

    private Context context;
    private List<StandardProductVariationList> varList;

    public StandardProductVariationAdapter(Context context, List<StandardProductVariationList> varList) {
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
        TextView varName = rootView.findViewById(R.id.variationLabelStandard);
        ImageView image = rootView.findViewById(R.id.imageVariationStandard);

        varName.setText(varList.get(position).getVariationName());
        Glide.with(context).load(varList.get(position).getVariationImage()).into(image);

        return rootView;
    }
}
