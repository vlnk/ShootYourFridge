package com.project.SYF.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.project.SYF.Main;

/**
 * Created by mimmy on 26/04/15.
 */
public class ModifyDeleteAlertDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Modify aliment")
                .setMessage("Do you want to delete or modify this aliment ?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener
                        () {
                    public void onClick(DialogInterface dialog, int id) {
                        // ask for confirmation for deletion
                        DialogFragment deletionDialog = new
                                DeleteCheckAlertDialog();
                        deletionDialog.show(getFragmentManager(), "tag");
                    }
                })
                .setNeutralButton("Modify", new DialogInterface.OnClickListener
                        () {
                    public void onClick(DialogInterface dialog, int id) {
                        // modification dialog
                        DialogFragment deletionDialog = new
                                ModifyAlertDialog();
                        deletionDialog.show(getFragmentManager(), "tag");

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
