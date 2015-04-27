package com.project.SYF;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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
    private Button mValidButton;

    private View mPopupView = null;
    private PopupWindow mPopup;

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

        //ADD BUTTON
        ImageButton addBtn = (ImageButton) findViewById(R.id.add_button);

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


                return true;
            }
        });

        // modify element when simple click on it
        mainListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mNameList.get(position);

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

    public void deleteElementCurrentList(){
        String name = mNameList.get(positionToDelete);
        db.deleteAliment(name);

        mNameList.remove(positionToDelete);
        mArrayAdapter.notifyDataSetChanged();

        if (mArrayAdapter.isEmpty()) {
            mValidButton.setVisibility(View.INVISIBLE);
            mValidButton.setEnabled(false);
        }
    }

    public void deleteElementDataBase() {
        String name = mNameList.get(positionToDelete);
        db.deleteCatalog(name);
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

    public void addPopup(View view) {
        if (mPopupView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mPopupView = inflater.inflate(R.layout.add_popup, null, false);
        }

        mPopup = new PopupWindow(
                mPopupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        EditText editableName = (EditText)mPopupView.findViewById(R.id.add_aliment_text);
        editableName.setText("");

        mPopup.setOutsideTouchable(true);
        mPopup.showAtLocation(this.findViewById(R.id.main), Gravity.CENTER, 0, 0);

        //EditText addAlimentText = (EditText) findViewById(R.id.add_aliment_text);
        //addAlimentText.setText("");
    }

    public void addAliment(View view) {
        EditText addAlimentText = (EditText) mPopupView.findViewById(R.id.add_aliment_text);
        pushAddButton(addAlimentText.getText().toString(), false);

        mPopup.dismiss();
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

            mValidButton.setEnabled(true);
            mValidButton.setVisibility(View.VISIBLE);
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
