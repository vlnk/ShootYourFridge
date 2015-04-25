package com.project.SYF;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
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
public class RechercheRecette extends ListActivity {

    private ArrayList<String> mAlimentList = new ArrayList<String>();
    private ArrayAdapter<String> mAlimentListAdapter;
    private ListView mRecetteListView;

    private ArrayList<HashMap<String, String>> recetteList;

    private static final String TAG_NAME = "name";
    private static final String TAG_DETAILS = "details";
    private static final String TAG_DESCRIPTION = "description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rechercherecette);

        Intent intent = getIntent();
        mAlimentList = intent.getStringArrayListExtra("ingredients");

        recetteList = new ArrayList<HashMap<String, String>>();

      //  mRecetteListView = (ListView) findViewById(R.id.list_lien_recette);
        mRecetteListView = getListView();


        mRecetteListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //traitement : lancement de la recette sur internet
            }
        });



        AsyncRechercheRecettes mTask = new AsyncRechercheRecettes(this, mAlimentList);
        mTask.execute();

    }


    public void finalizeResearch(Document document){

        Elements recettes = document.getElementsByClass("m_contenu_resultat");

        for(Element recette : recettes) {
            Element titreDiv = recette.getElementsByClass("m_titre_resultat").first();

            Elements titreA = null;
            if((titreA = titreDiv.getElementsByAttribute("a")) == null)
                continue;
            Element lienTitre = titreA.first();
            String titreHref  = lienTitre.attr("href");
            String titre = lienTitre.attr("title");

            Element detailDiv = recette.getElementsByClass("m_detail_recette").first();
            String detail = detailDiv.text();

            Element descriptionDiv = recette.getElementsByClass("m_texte_resultat").first();
            String description = descriptionDiv.text();
        }

        //actualiser la liste
        ListAdapter adapter = new SimpleAdapter(
                RechercheRecette.this, recetteList,
                R.layout.recette, new String[] { TAG_NAME, TAG_DETAILS,
                TAG_DESCRIPTION }, new int[] { R.id.name,
                R.id.details, R.id.description });

        setListAdapter(adapter);
    }
}
