package com.project.SYF.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import com.project.SYF.AsyncTaskClass;
import com.project.SYF.Main;
import com.project.SYF.R;

import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanAlertDialog extends DialogFragment {

    public static String TAG = "DialogMyListFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Main activity = (Main) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View scanDialogView = inflater.inflate(R.layout.scan_popup, null);
        builder.setView(scanDialogView);

        ListView list = (ListView) scanDialogView.findViewById(R.id.list_proposal);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                , android.R.layout.simple_list_item_1, android.R.id.text1,
                activity.mResultList);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Main activity = (Main) getActivity();
                activity.pushAddButton(activity.mResultList.get(position),
                        true);
                dismiss();
            }
        });

        return builder.create();
    }
}