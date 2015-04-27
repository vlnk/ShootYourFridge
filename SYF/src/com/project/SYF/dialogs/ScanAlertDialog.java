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

        builder.setTitle("Scanning results")
                .setMessage("Choose in the list below the best description " +
                        "for your product, by clicking on it");
        ListView list = new ListView(getActivity());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                , android.R.layout.simple_list_item_1, android.R.id.text1,
                activity.mResultList);

        list.setAdapter(adapter);
        builder.setView(list);

        return builder.create();

    }

}