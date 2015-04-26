package com.project.SYF.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.project.SYF.R;

/**
 * Created by mimmy on 26/04/15.
 */

public class DeleteCheckAlertDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("You're about to delete this entry.")
                .setMessage("Do you really want to delete this " +
                        "aliment from your list ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener
                        () {
                    public void onClick(DialogInterface dialog, int id) {
                        //delete the entry

                        // ask for complete deletion


                    }
                })
                .setNegativeButton("No", new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
