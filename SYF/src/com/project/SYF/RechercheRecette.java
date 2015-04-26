package com.project.SYF;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by robinvermes on 25/04/2015.
 */
public class RechercheRecette extends Activity{

    private ArrayList<String> mAlimentList = new ArrayList<String>();
    private ArrayAdapter<String> mAlimentListAdapter;
    private ListView mAlimentListView;

    private ArrayList<HashMap<String, String>> recetteList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rechercherecette);

        Intent intent = getIntent();
        mAlimentList = intent.getStringArrayListExtra("ingredients");

        //list Aliment
        mAlimentListView = (ListView) findViewById(R.id.list_lien_recette);
        mAlimentListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                mAlimentList);
        mAlimentListView.setAdapter(mAlimentListAdapter);
        mAlimentListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //recherche internet de la recette
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
        mAlimentList.clear();




        mAlimentListAdapter.notifyDataSetChanged();
    }
}
