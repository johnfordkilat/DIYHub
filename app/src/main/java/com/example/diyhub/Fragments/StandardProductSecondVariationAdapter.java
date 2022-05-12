package com.example.diyhub.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.diyhub.R;

import java.util.List;

public class StandardProductSecondVariationAdapter extends BaseAdapter {

    private Context context;
    private List<StandardProductSecondVariationList> varList;

    public StandardProductSecondVariationAdapter(Context context, List<StandardProductSecondVariationList> varList) {
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

        View rootView = LayoutInflater.from(context).inflate(R.layout.product_second_variation_standard, parent, false);
        TextView varName = rootView.findViewById(R.id.variationLabelStandard);

        varName.setText(varList.get(position).getVarSize());

        return rootView;
    }
}
