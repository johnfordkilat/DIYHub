package com.example.diyhub.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.diyhub.R;

public class CustomDialog extends AppCompatDialogFragment {

    EditText option;
    Custom_DialogInterface dialogInterface;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view =  inflater.inflate(R.layout.layout_dialog, null);


        builder.setView(view)
                .setTitle("ADD PAYMENT OPTION")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String word = option.getText().toString();
                        dialogInterface.applyTexts(word);
                    }
                });

        option = view.findViewById(R.id.setPaymentOptionTxt);


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        dialogInterface = (Custom_DialogInterface) context;
    }

    public interface Custom_DialogInterface{
        void applyTexts(String option);
    }
}
