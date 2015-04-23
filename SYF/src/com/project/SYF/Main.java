package com.project.SYF;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.ArrayList;


public class Main extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    /**
     * Called when the activity is first created.
     */
    private Button scanBtn;
    private Button addBtn;
    private TextView formatTxt, contentTxt;
    private EditText addAlimentText;

    private ListView mainListView;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayList<String> mNameList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Scan button
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);

        scanBtn.setOnClickListener(this);

        //Add Button
        addBtn = (Button) findViewById(R.id.add_button);
        addBtn.setOnClickListener(this);

        //Add aliment text
        addAlimentText = (EditText) findViewById(R.id.add_aliment_text);
        addAlimentText.setOnClickListener(this);
        //Aliment List

        mainListView = (ListView) findViewById(R.id.aliments_list);
        mArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                mNameList);
        mainListView.setAdapter(mArrayAdapter);
        mainListView.setOnItemClickListener(this);

    }


    public void onClick(View v){
        //respond to clicks
        if(v.getId()==R.id.add_aliment_text) {
            addAlimentText.setText(addAlimentText.getText().toString());
        }
        if(v.getId()==R.id.scan_button){
            //scan
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
        if(v.getId()==R.id.add_button){
            String cour = addAlimentText.getText().toString();
            boolean estdeja = false;
            for (int i = 0; i < mNameList.size(); i++)
            {
                if (mNameList.get(i).compareTo(cour) == 0)
                    estdeja = true;
            }
            if (!estdeja) {
                mNameList.add(cour);
                mArrayAdapter.notifyDataSetChanged();
            }
            addAlimentText.setText("");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
           //addAlimentText.setText(scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Log the item's position and contents
        // to the console in Debug
        mNameList.remove(position);
        mArrayAdapter.notifyDataSetChanged();
        //Log.d("omg android", position + ": " + mNameList.get(position));
    }
}
