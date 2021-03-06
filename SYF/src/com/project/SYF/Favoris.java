package com.project.SYF;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.project.SYF.dialogs.DeleteFavorisCheckAlertDialog;
import com.project.SYF.helper.DatabaseHelper;
import com.project.SYF.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by annesohier on 26/04/2015.
 */
@SuppressWarnings("Convert2Lambda")
public class Favoris extends ListActivity implements AdapterView.OnItemClickListener {

    private ArrayList<HashMap<String, String>> recetteList;

    private ListAdapter adapter;

    private static final String TAG_NAME = "name";
    private static final String TAG_DETAILS = "details";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_HREF = "href";

    private int positionToDelete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rechercherecette);

        //recuperation depuis la BD A FAIRE
        recetteList = new ArrayList<HashMap<String, String>>();
        getRecipes();

        ListView mRecetteListView = getListView();

        mRecetteListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent validateIntent = new Intent(Favoris.this, AffichageRecette.class);
                validateIntent.putExtra("href", recetteList.get(position).get(TAG_HREF));
                validateIntent.putExtra("thereIsButton", "false");
                startActivity(validateIntent);
            }
        });
        mRecetteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int
                    position, long id) {

                // Alert Dialog : Deletion Check
                positionToDelete = position;
                DialogFragment dialog = new DeleteFavorisCheckAlertDialog();
                dialog.show(getFragmentManager(), "tag");


                return true;
            }
        });


        adapter = new SimpleAdapter(
                Favoris.this, recetteList,
                R.layout.recette, new String[] { TAG_NAME, TAG_DETAILS,
                TAG_DESCRIPTION }, new int[] { R.id.name,
                R.id.details, R.id.description });

        setListAdapter(adapter);


    }

    private void getRecipes() {
        DatabaseHelper db = new DatabaseHelper(this); //my database helper file
        List<Recipe> recipeList = db.getAllInRecipe();

        for (Recipe currentRecipe : recipeList){
            HashMap<String, String> uneRecette = new HashMap<String, String>();
            uneRecette.put(TAG_NAME, currentRecipe.getName());
            uneRecette.put(TAG_DETAILS, currentRecipe.getDetails());
            uneRecette.put(TAG_DESCRIPTION, currentRecipe.getDescription());
            uneRecette.put(TAG_HREF, currentRecipe.getHref());

            recetteList.add(uneRecette);
        }
    }

    public void deleteElementCurrentList(){
        DatabaseHelper db = new DatabaseHelper(this);
        HashMap<String, String> uneRecette;
        uneRecette = recetteList.get(positionToDelete);
        db.deleteRecipe(uneRecette.get(TAG_NAME));

        recetteList.remove(positionToDelete);
        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu topMenu)
    {
        //inflate the menu_home to use in the action bar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_favoris,topMenu);
        return super.onCreateOptionsMenu(topMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_home: finish(); break;
            case R.id.action_scanner: {
                Intent validateIntent = new Intent(this, Main.class);
                startActivity(validateIntent);
                break;
            }

            default: break;
        }
        return super.onOptionsItemSelected(item);
    }


}
