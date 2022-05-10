package com.example.diyhub;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    public List<String> fileNameList;
    public List<String> fileDoneList;
    public List<Uri> fileImageList;
    public List<Integer> counterList;

    public UploadListAdapter(List<String> fileNameList, List<String> fileDoneList, List<Uri> fileImageList, List<Integer> counterList) {
        this.fileNameList = fileNameList;
        this.fileDoneList = fileDoneList;
        this.fileImageList = fileImageList;
        this.counterList = counterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String filename = fileNameList.get(position);
        Uri fileImage = fileImageList.get(position);
        Integer count = counterList.get(position);

        holder.filenameView.setText(filename);
        holder.counter.setText(String.valueOf(count));

        holder.prodImage.setImageURI(fileImage);

        String filedone = fileDoneList.get(position);
        if(filedone.equals("Uploading"))
        {
            holder.filedoneView.setImageResource(R.drawable.loading);
        }
        else
        {
            holder.filedoneView.setImageResource(R.drawable.checkicon);
        }
    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public TextView filenameView;
        public ImageView filedoneView;
        public CircleImageView prodImage;
        public TextView counter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            filenameView = mView.findViewById(R.id.uploadFileNameTxtProductsStandard);
            filedoneView = mView.findViewById(R.id.uploadLoadingProductsStandard);
            prodImage = mView.findViewById(R.id.prodImageProductsStandard);
            counter = mView.findViewById(R.id.counterTxtStandardProductImages);

        }
    }
}
