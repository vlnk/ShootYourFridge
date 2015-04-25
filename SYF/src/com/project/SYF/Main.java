package com.project.SYF;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
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
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;



public class Main extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    /**
     * Called when the activity is first created.
     */
    private Button scanBtn;
    private Button addBtn;
    private TextView formatTxt, contentTxt, resultsTxt;
    private EditText addAlimentText;

    private ListView mainListView;
    private ListView resultListView;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayAdapter<String> mArrayAdapterResult;
    private ArrayList<String> mNameList = new ArrayList<String>();
    private ArrayList<String> mResultList = new ArrayList<String>();

    private String mCurrentBarCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        //Scan button
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);

        scanBtn.setOnClickListener(this);

        //text result
        resultsTxt = (TextView)findViewById(R.id.resultTextView);

        //list result
        resultListView = (ListView) findViewById(R.id.list_proposal);
        mArrayAdapterResult = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                mResultList);
        resultListView.setAdapter(mArrayAdapterResult);
        resultListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pushAddButton(mResultList.get(position), true);
            }
        });


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
        mainListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mNameList.remove(position);
                mArrayAdapter.notifyDataSetChanged();
            }
        });
    }


    public void onClick(View v){
        //respond to clicks
        if(v.getId()==R.id.add_aliment_text) {
            addAlimentText.setText(addAlimentText.getText().toString());
        }
        if(v.getId()==R.id.scan_button){
            //scan + lancement recherche du produit (dans OnActivityResult)
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();


        }
        if(v.getId()==R.id.add_button){
            pushAddButton(addAlimentText.getText().toString(), false);
        }

  /*      try {
            URL url = new URL("http://www.vogella.com");
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            readStream(con.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
   */
    }


    public void pushAddButton(String toListText, boolean withBarCode){
        boolean estdeja = false;
        if (withBarCode) {
            //ajouter a la base de donner avec le code barre???

        }
        for (int i = 0; i < mNameList.size(); i++)
        {
            if (mNameList.get(i).compareTo(toListText) == 0)
                estdeja = true;
        }

        if (!estdeja) {
            mNameList.add(toListText);
            mArrayAdapter.notifyDataSetChanged();
        }
        addAlimentText.setText("");
    }

    public void replaceAllInKeyWordsList(ArrayList<String> keywordsList){
        mResultList.clear();
        mResultList.addAll(keywordsList);
        mArrayAdapterResult.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
       /*     String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
         */
            mCurrentBarCode = scanningResult.getContents();

            AsyncTaskClass mTask = new AsyncTaskClass(this, mCurrentBarCode);
            mTask.execute();
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

/*
    private void readStream(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                addAlimentText.setText(line);
                mNameList.add(addAlimentText.getText().toString());
                mArrayAdapter.notifyDataSetChanged();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/

}
