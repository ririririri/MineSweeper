package com.example.jianingsun.mineseeker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Jianing Sun on 2017-02-23.
 */

public class messageFragment extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        View v= LayoutInflater.from(getActivity())
                .inflate(R.layout.message,null);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("TAG","You clicked dialog button");
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        getActivity().finish();
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle("Congratulations!")
                .setView(v)
                .setPositiveButton(android.R.string.ok,listener)
                .create();
    }
}
