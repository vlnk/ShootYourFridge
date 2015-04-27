package com.project.SYF;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import com.project.SYF.helper.DatabaseHelper;
import com.project.SYF.model.Food;
import com.project.SYF.model.Recipe;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * Created by robinvermes on 25/04/2015.
 */
public class RechercheRecette extends ListActivity implements AdapterView.OnItemClickListener{

    private ArrayList<String> mAlimentList = new ArrayList<String>();
    private ArrayAdapter<String> mAlimentListAdapter;
    private ListView mRecetteListView;

    private ArrayList<HashMap<String, String>> recetteList;

    private static final String TAG_NAME = "name";
    private static final String TAG_DETAILS = "details";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_HREF = "href";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rechercherecette);

        // populate list of ingredients with elements in FOOD table
        mAlimentList = getAliments();

        recetteList = new ArrayList<HashMap<String, String>>();

        mRecetteListView = getListView();

        mRecetteListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent validateIntent = new Intent(RechercheRecette.this, AffichageRecette.class);
                validateIntent.putExtra("name", recetteList.get(position).get(TAG_NAME));
                validateIntent.putExtra("details", recetteList.get(position).get(TAG_DETAILS));
                validateIntent.putExtra("description", recetteList.get(position).get(TAG_DESCRIPTION));
                validateIntent.putExtra("href", recetteList.get(position).get(TAG_HREF));
                validateIntent.putExtra("thereIsButton", "true");

                startActivity(validateIntent);
            }
        });

        AsyncRechercheRecettes mTask = new AsyncRechercheRecettes(this, mAlimentList);
        mTask.execute();

    }

    /*
     * Get list of aliments in FOOD table
     */
    private ArrayList<String> getAliments() {
        ArrayList<String> resultList = new ArrayList<String>();
        DatabaseHelper db = new DatabaseHelper(this); //my database helper file
        String aliment;

        List<Food> foodList = db.getAllInFood();

        for (int i = 0; i < foodList.size(); i++) {
            aliment = foodList.get(i).getName();
            aliment = aliment.replace(' ', '-');
            
            resultList.add(i, aliment);
        }
        return resultList;
    }


    public void finalizeResearch(Document document){

        Elements recettes = document.getElementsByClass("m_contenu_resultat");


        for(Element recette : recettes) {
            Element titreDiv = recette.getElementsByClass("m_titre_resultat").first();

            Element lienTitre = null;
            if((lienTitre = titreDiv.getElementsByTag("a").first()) == null)
                continue;
            String titreHref  = lienTitre.attr("href");
            String titre = lienTitre.attr("title");

            Element detailDiv = recette.getElementsByClass("m_detail_recette").first();
            String detail = detailDiv.text();

            Element descriptionDiv = recette.getElementsByClass("m_texte_resultat").first();
            String description = descriptionDiv.text();

            // tmp hashmap for single contact
            HashMap<String, String> uneRecette = new HashMap<String, String>();


            // adding each child node to HashMap key => value
            uneRecette.put(TAG_NAME, titre);
            uneRecette.put(TAG_DETAILS, detail);
            uneRecette.put(TAG_DESCRIPTION, description);
            uneRecette.put(TAG_HREF, titreHref);

            // adding contact to contact list
            recetteList.add(uneRecette);

        }

        //actualiser la liste
        ListAdapter adapter = new SimpleAdapter(
                RechercheRecette.this, recetteList,
                R.layout.recette, new String[] { TAG_NAME, TAG_DETAILS,
                TAG_DESCRIPTION }, new int[] { R.id.name,
                R.id.details, R.id.description });

        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
