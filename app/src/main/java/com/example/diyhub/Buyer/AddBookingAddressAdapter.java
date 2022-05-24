package com.example.diyhub.Buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyhub.R;
import com.example.diyhub.RecyclerViewAdapterShopsNearYou;

import java.util.ArrayList;

public class AddBookingAddressAdapter extends RecyclerView.Adapter<AddBookingAddressAdapter.ViewHolder>{

    Context context;
    ArrayList<AddBookingModel> list;

    public AddBookingAddressAdapter() {
    }

    public AddBookingAddressAdapter(Context context, ArrayList<AddBookingModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AddBookingAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_booking_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddBookingAddressAdapter.ViewHolder holder, int position) {

        AddBookingModel addBookingModel = list.get(position);
        holder.newAddress.setText(addBookingModel.getAddress());
        holder.newLandmark.setText(addBookingModel.getLandmark());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView newAddress,newLandmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             newAddress = itemView.findViewById(R.id.newAddress_txtView);
             newLandmark = itemView.findViewById(R.id.landMark_txtView);
        }
    }
}
