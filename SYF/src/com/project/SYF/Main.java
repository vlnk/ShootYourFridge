package com.project.SYF;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.*;
import com.project.SYF.helper.DatabaseHelper;
import com.project.SYF.model.Food;
import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class Main extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    /**
     * Called when the activity is first created.
     */
    private Button scanBtn;
    private Button addBtn;
    private Button validBtn;
    private TextView formatTxt, contentTxt, resultsTxt;
    private EditText addAlimentText;

    private ListView mainListView;
    private ListView resultListView;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayAdapter<String> mArrayAdapterResult;
    private ArrayList<String> mNameList = new ArrayList<String>();
    private ArrayList<String> mResultList = new ArrayList<String>();

    private String mCurrentBarCode;


    // Database Helper
    DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        db = new DatabaseHelper(getApplicationContext());

        // init mNameList
        mNameList = getResults();


        // Display dataBas in LogCat
        Log.d("Reading: ", "Reading all aliments from food..");
        List<Food> food = db.getAllInFood();

        for (Food cn : food) {
            String log = "Id: " + cn.getId() + " ,Name: " + cn.getName() + " ," +
                    "Scan Id: " + cn.getScanId();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

        Log.d("Reading: ", "Reading all aliments from Catalog..");
        List<Catalog> cat = db.getAllInCatalog();

        for (Catalog cn : cat) {
            String log = "Id: " + cn.getId() + " ,Name: " + cn.getName() + " ," +
                    "Scan Id: " + cn.getScanId();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }



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

        // suppress element when long click on it
        mainListView.setLongClickable(true);
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int
                    position, long id) {

                // Alert Dialog : Deletion Check
                DialogFragment dialog = new DeleteCheckAlertDialog();
                dialog.show(getFragmentManager(), "tag");



                /*
                String name = mNameList.get(position);
                db.deleteAliment(name);

                mNameList.remove(position);
                mArrayAdapter.notifyDataSetChanged();

                */
                return true;
            }
        });

        //Validate Button
        validBtn = (Button) findViewById(R.id.validate_button);
        validBtn.setOnClickListener(this);
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
        if(v.getId()==R.id.validate_button){

            Intent validateIntent = new Intent(this, RechercheRecette.class);
            validateIntent.putExtra("ingredients", mNameList);
            startActivity(validateIntent);
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


    /**
     * Add aliment to the current list, and to the database
     *      when button "Add" is pressed
     * */
    public void pushAddButton(String toListText, boolean withBarCode){
        // if string not empty
        if ("".compareTo(toListText) != 0){
            boolean estdeja = false;

            // search for this value in the actual list of aliments
            for (int i = 0; i < mNameList.size(); i++)
            {
                if (mNameList.get(i).compareTo(toListText) == 0)
                    estdeja = true;
            }

            // add a new element to the list and the database
            if (!estdeja) {
                mNameList.add(toListText);
                mArrayAdapter.notifyDataSetChanged();

                Food newAliment = new Food(toListText, mCurrentBarCode);
                db.addFood(newAliment);


                Catalog newCatalog = new Catalog(toListText,
                        mCurrentBarCode);
                db.addCatalog(newCatalog);

            }
            addAlimentText.setText("");
        }

    }

    public void replaceAllInKeyWordsList(ArrayList<String> keywordsList){
        mResultList.clear();
        mResultList.addAll(keywordsList);
        mArrayAdapterResult.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // initialize variable
        mCurrentBarCode = null;

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

            DatabaseHelper db = new DatabaseHelper(this);
            String newAliment = db.getCatalogByScan(mCurrentBarCode);

            // if element exists in database
            if (newAliment != null) {
                mNameList.add(newAliment);
            }
            else {
                // else, the element is searched on the Internet
                AsyncTaskClass mTask = new AsyncTaskClass(this, mCurrentBarCode);
                mTask.execute();
            }
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
     * Called by asyncTask to reinitialize the current barcode
     */
    public void changeBarCode()
    {
        mCurrentBarCode = null;
    }

    /*
     * Populate mNameList with the elements in the dataBase
     */
    private ArrayList<String> getResults() {
        ArrayList<String> resultList = new ArrayList<String>();
        DatabaseHelper db = new DatabaseHelper(this); //my database helper file

        List<Food> foodList = db.getAllInFood();

        for (int i = 0; i < foodList.size(); i++) {
            resultList.add(i, foodList.get(i).getName());
        }

        return resultList;
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
