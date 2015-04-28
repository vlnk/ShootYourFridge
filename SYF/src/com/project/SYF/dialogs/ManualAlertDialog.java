package com.project.SYF.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.project.SYF.Main;
import com.project.SYF.R;

/**
 * Created by mimmy on 27/04/15.
 */
public class ManualAlertDialog extends DialogFragment {

    private Dialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Main activity = (Main) getActivity();
        final EditText input = new EditText(activity);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View addDialogView = inflater.inflate(R.layout.add_popup, null);
        builder.setView(addDialogView);

        EditText editText = (EditText) addDialogView.findViewById(R.id.add_aliment_manual);
        Button button = (Button) addDialogView.findViewById(R.id.add_aliment_manual_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // confirm modification

                Main activity = (Main) getActivity();
                String value = editText.getText().toString();

                if (value.compareTo("") != 0) {
                    activity.pushAddButton(value, false);
                }
                mDialog.dismiss();
            }
        });

        mDialog = builder.create();
        return mDialog;
    }
}
