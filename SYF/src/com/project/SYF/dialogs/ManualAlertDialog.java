package com.project.SYF.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.project.SYF.Main;

/**
 * Created by mimmy on 27/04/15.
 */
public class ManualAlertDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Main activity = (Main) getActivity();
        final EditText input = new EditText(activity);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Manual entry")
                .setMessage("Enter a new ingredient")
                .setView(input)
                .setPositiveButton("Modify", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // confirm modification

                        Main activity = (Main) getActivity();
                        String value = input.getText().toString();

                        if (value.compareTo("") != 0) {
                            activity.pushAddButton(value, false);
                        }
                        dialog.dismiss();
                    }
                });

        return builder.create();

    }
}
