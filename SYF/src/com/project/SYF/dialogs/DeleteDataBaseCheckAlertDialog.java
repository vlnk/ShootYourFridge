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
public class DeleteDataBaseCheckAlertDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete entry")
                .setMessage("Do you want to save this entry for later ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener
                        () {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .setNegativeButton("No", new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //delete the entry from the dataBase
                        Main callingActivity = (Main) getActivity();
                        callingActivity.deleteElementDataBase();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
