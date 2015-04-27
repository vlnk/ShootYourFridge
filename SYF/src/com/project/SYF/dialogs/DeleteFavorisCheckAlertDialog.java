package com.project.SYF.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.project.SYF.Favoris;

/**
 * Created by mimmy on 26/04/15.
 */

@SuppressWarnings("Convert2Lambda")
public class DeleteFavorisCheckAlertDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("You're about to delete this recipe from the list.")
                .setMessage("Do you really want to delete this?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener
                        () {
                    public void onClick(DialogInterface dialog, int id) {

                        //delete the entry from the current list
                        Favoris callingActivity = (Favoris) getActivity();
                        callingActivity.deleteElementCurrentList();

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
