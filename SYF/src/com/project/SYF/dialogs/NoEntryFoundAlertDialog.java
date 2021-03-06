package com.project.SYF.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

@SuppressWarnings("unused")
public class NoEntryFoundAlertDialog extends DialogFragment {

    @SuppressWarnings("Convert2Lambda")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("No entry were found for this product")
                .setMessage("Do you still want to add it manually?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener
                        () {
                    public void onClick(DialogInterface dialog, int id) {
                        // continue with adding new aliment
                    }
                })
                .setNegativeButton("No", new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // reinitialize BarCode for new entry


                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
