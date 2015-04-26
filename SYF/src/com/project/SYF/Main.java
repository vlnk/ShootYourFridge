package com.project.SYF;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import com.project.SYF.dialogs.DeleteCheckAlertDialog;
import com.project.SYF.helper.DatabaseHelper;
import com.project.SYF.model.Catalog;
import com.project.SYF.model.Food;
import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Main extends Activity {
    private EditText addAlimentText;
    private ArrayAdapter<String> mArrayAdapter, mArrayAdapterResult;

    private ArrayList<String> mNameList = new ArrayList<>();

    @SuppressWarnings("CanBeFinal")
    private ArrayList<String> mResultList = new ArrayList<>();

    private int positionToDelete;

    private String mCurrentBarCode;

    // Database Helper
    private DatabaseHelper db;

    @SuppressWarnings("Convert2Lambda")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //INITIALIZE DB (mNameList)
        db = new DatabaseHelper(getApplicationContext());
        mNameList = getResults();
        initializeDb();

        //Scan button
        Button scanBtn = (Button) findViewById(R.id.scan_button);

        //text result
        @SuppressWarnings("UnusedAssignment")
        TextView resultsTxt = (TextView) findViewById(R.id.resultTextView);

        @SuppressWarnings("UnusedAssignment")
        TextView formatTxt = (TextView) findViewById(R.id.scan_format);

        @SuppressWarnings("UnusedAssignment")
        TextView contentTxt = (TextView) findViewById(R.id.scan_content);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });

        //LIST RESULT
        ListView resultListView = (ListView) findViewById(R.id.list_proposal);
        mArrayAdapterResult = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mResultList);
        resultListView.setAdapter(mArrayAdapterResult);
        resultListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pushAddButton(mResultList.get(position), true);
            }
        });

        //ADD BUTTON
        Button addBtn = (Button) findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushAddButton(addAlimentText.getText().toString(), false);
            }
        });

        //ADD ALIMENT BY TEXT
        addAlimentText = (EditText) findViewById(R.id.add_aliment_text);
        addAlimentText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addAlimentText.setText(addAlimentText.getText().toString());
                    }
                });

        //ALIMENT LIST
        ListView mainListView = (ListView) findViewById(R.id.aliments_list);
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mNameList);
        mainListView.setAdapter(mArrayAdapter);

        // suppress element when long click on it
        mainListView.setLongClickable(true);
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int
                    position, long id) {

                // Alert Dialog : Deletion Check
                positionToDelete = position;
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

        //VALIDATE BUTTON
        Button validBtn = (Button) findViewById(R.id.validate_button);
        validBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    public void deleteElementCurrentList(){
        String name = mNameList.get(positionToDelete);
        db.deleteAliment(name);

        mNameList.remove(positionToDelete);
        mArrayAdapter.notifyDataSetChanged();
    }

    public void deleteElementDataBase() {
        String name = mNameList.get(positionToDelete);
        db.deleteCatalog(name);
    }

    private void scan() {
        //scan + lancement recherche du produit (dans OnActivityResult)
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    private void validate() {
        Intent validateIntent = new Intent(this, RechercheRecette.class);
        validateIntent.putExtra("ingredients", mNameList);
        startActivity(validateIntent);
    }

    /**
     * Add aliment to the current list, and to the database
     *      when button "Add" is pressed
     * */
    private void pushAddButton(String toListText, boolean withBarCode){
        // if string not empty
        if ("".compareTo(toListText) != 0){
            boolean estdeja = false;

            // search for this value in the actual list of aliments
            for (String aMNameList : mNameList) {
                if (aMNameList.compareTo(toListText) == 0)
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

    /*
     * Called by asyncTask to reinitialize the current barcode
     */
    public void changeBarCode()
    {
        mCurrentBarCode = null;
    }

    /*
     *  Populate mNameList with the elements in the FOOD_TABLE
     */
    private ArrayList<String> getResults() {
        ArrayList<String> resultList = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(this); //my database helper file

        List<Food> foodList = db.getAllInFood();

        for (int i = 0; i < foodList.size(); i++) {
            resultList.add(i, foodList.get(i).getName());
        }

        return resultList;
    }

    private void initializeDb() {
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
    }
}
