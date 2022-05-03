package com.example.diyhub;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView prodName,prodQuan,prodStocks;
    ImageView prodImage,deleteProd;
    View mView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        prodName = itemView.findViewById(R.id.productNameSeller);
        prodQuan = itemView.findViewById(R.id.purchaseCountSeller);
        prodStocks = itemView.findViewById(R.id.stocksCountSeller);
        prodImage = itemView.findViewById(R.id.productImageSeller);
        deleteProd = itemView.findViewById(R.id.deleteProduct);
    }

    private ViewHolder.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);

    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener)
    {
        mClickListener = clickListener;
    }
}
