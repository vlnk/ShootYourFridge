package com.project.SYF;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import android.view.*;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import com.project.SYF.dialogs.ManualAlertDialog;
import com.project.SYF.dialogs.ModifyDeleteAlertDialog;
import com.project.SYF.dialogs.ScanAlertDialog;
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
    private Button mValidButton;

    public View mPopupView = null;
    public PopupWindow mPopup;

    public ArrayAdapter<String> mArrayAdapter, mArrayAdapterResult;
    public ArrayList<String> mNameList = new ArrayList<String>();

    @SuppressWarnings("CanBeFinal")
    public ArrayList<String> mResultList = new ArrayList<String>();

    private int positionToDelete;
    public String nameToModify;

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

        //ALIMENT LIST
        ListView mainListView = (ListView) findViewById(R.id.aliments_list);
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mNameList);
        mainListView.setAdapter(mArrayAdapter);

        // suppress element when long click on it
        mainListView.setLongClickable(true);
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int
                    position, long id) {

                // Alert Dialog : Deletion Check
                positionToDelete = position;
                nameToModify = mNameList.get(positionToDelete);
                DialogFragment dialog = new ModifyDeleteAlertDialog();
                dialog.show(getFragmentManager(), "tag");
                return true;
            }
        });

        //VALIDATE BUTTON
        mValidButton = (Button) findViewById(R.id.validate_button);

        if (mArrayAdapter.isEmpty()) {
            mValidButton.setEnabled(false);
            mValidButton.setVisibility(View.INVISIBLE);
        }
        else {
            mValidButton.setEnabled(true);
            mValidButton.setVisibility(View.VISIBLE);
        }
    }

    /*
     * Delete element from current list and FOOD table
     */
    public void deleteElementCurrentList(){
        String name = mNameList.get(positionToDelete);
        db.deleteAliment(name);

        mNameList.remove(positionToDelete);
        mArrayAdapter.notifyDataSetChanged();
        Toast mtoast = Toast.makeText(getApplicationContext(), "Ingredient " +
                "deleted", Toast.LENGTH_SHORT);
        mtoast.show();

        if (mArrayAdapter.isEmpty()){
            mValidButton.setEnabled(false);
            mValidButton.setVisibility(View.INVISIBLE);
        }
    }

    /*
     * Delete element from CATALOG table
     */
    public void deleteElementDataBase() {
        String name = mNameList.get(positionToDelete);
        db.deleteCatalog(name);
    }

    /*
     * Modify name in current list, FOOD and CATALOG table
     */
    public void modifyName(String modifiedName) {

        String name = mNameList.get(positionToDelete);
        Food aliment = db.getFoodByName(name);
        db.updateAliment(aliment, modifiedName);
        Catalog cat = db.getCatalogByName(name);
        db.updateCatalog(cat, modifiedName);

        mNameList.clear();
        mNameList.addAll(getResults());
        mArrayAdapter.notifyDataSetChanged();
        Toast mtoast = Toast.makeText(getApplicationContext(), "Ingredient " +
                "modified", Toast.LENGTH_SHORT);
        mtoast.show();
    }

    public void scan(View view) {
        //scan + lancement recherche du produit (dans OnActivityResult)
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    /**
     *  Launch the Recipe Search with the current list of ingredients
     * */
    public void validate(View view) {
        Intent validateIntent = new Intent(this, RechercheRecette.class);
        startActivity(validateIntent);
    }

    /**
     * Launch dialog to scan and add new ingredient
     * */
    public void addPopup_scan() {

        DialogFragment dialog = new ScanAlertDialog();
        dialog.show(getFragmentManager(), "tag");
    }

    /**
     * Launch dialog to add new ingredient manually
     * */
    public void addPopup_manual() {
        DialogFragment dialog = new ManualAlertDialog();
        dialog.show(getFragmentManager(), "tag");
    }

    public void addPopup_manual(View view) {
        addPopup_manual();
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

            mValidButton.setEnabled(true);
            mValidButton.setVisibility(View.VISIBLE);
        }

    }

    public void replaceAllInKeyWordsList(ArrayList<String> keywordsList){
        mResultList.clear();
        mResultList.addAll(keywordsList);
        //mArrayAdapterResult.notifyDataSetChanged();

        if (mResultList.isEmpty()){
            addPopup_manual();
        }
        else{
            addPopup_scan();
        }
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
            //if (newAliment != null) {
              //  mNameList.add(newAliment);
            //}
            //else {
                // else, the element is searched on the Internet
                AsyncTaskClass mTask = new AsyncTaskClass(this, mCurrentBarCode);
                mTask.execute();
                /*Toast mtoast = Toast.makeText(getApplicationContext(), "FAIL"
                        , Toast.LENGTH_SHORT);
                mtoast.show();*/
            //}

        }
        else {
            Toast mtoast = Toast.makeText(getApplicationContext(), "FAIL"
                    , Toast.LENGTH_SHORT);
            mtoast.show();
        }
    }

    /**
     *  Populate mNameList with the elements in the FOOD_TABLE
     **/
    private ArrayList<String> getResults() {
        ArrayList<String> resultList = new ArrayList<String>();
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

    @Override
    public boolean onCreateOptionsMenu(Menu topMenu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_scanner,topMenu);
        return super.onCreateOptionsMenu(topMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.action_home: finish(); break;
            case R.id.action_favoris: {
                Intent validateIntent = new Intent(this, Favoris.class);
                startActivity(validateIntent);
                break;
            }
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }
}
