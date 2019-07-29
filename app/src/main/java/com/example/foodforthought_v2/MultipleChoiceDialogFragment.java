package com.example.foodforthought_v2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

public class MultipleChoiceDialogFragment extends DialogFragment {

    public interface onMultiChoiceListener {
        void onPostiveButtonClicked(String[] list, ArrayList<String> selectedItemList);
        void onNegativeButtonClicked();
    }


    onMultiChoiceListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mListener = (onMultiChoiceListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() + " onMultiChoiceListener must be implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final ArrayList<String> selectedItemList = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] list = getActivity().getResources().getStringArray(R.array.Type_of_food);

        builder.setTitle("Select Your Type of Food").setMultiChoiceItems(list, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if(isChecked) {
                    selectedItemList.add(list[which]);
                } else {
                    selectedItemList.remove(list[which]);
                }
            }
        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onPostiveButtonClicked(list, selectedItemList);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onNegativeButtonClicked();
                    }
                });

        return builder.create();
    }
}
