package com.project.SYF.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import com.project.SYF.Main;

/**
 * Created by mimmy on 26/04/15.
 */
public class ModifyAlertDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        Main callingActivity = (Main) getActivity();
        final EditText input = new EditText(callingActivity);

        String originalName = callingActivity.nameToModify;
        input.setText(originalName);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle("Entry modification ")
                .setMessage("Modify the name of this entry ")
                .setView(input)
                .setPositiveButton("Modify", new DialogInterface
                        .OnClickListener
                        () {
                    public void onClick(DialogInterface dialog, int id) {
                        // confirm modification
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
