package com.project.SYF;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.Toast;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by annesohier on 25/04/2015.
 */
public class AffichageRecette extends Activity {

    private static String mHref;
    private static TextView mTemps;
    private static TextView mIngredients;
    private static TextView mPreparation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.affichagerecette);

        Intent intent = getIntent();
        mHref = intent.getStringExtra("href");

        mTemps = (TextView)findViewById(R.id.temps);
        mIngredients = (TextView)findViewById(R.id.ingredients);
        mPreparation = (TextView)findViewById(R.id.preparation);


        AsyncGetRecette mTask = new AsyncGetRecette(this, mHref);
        mTask.execute();

    }

    public void finalizeResearch(Document document){

        Element recetteDescription = document.getElementsByClass("m_content_recette_main").first();
        Element recetteDuree = recetteDescription.getElementsByClass("m_content_recette_info").first();
        Element recetteIngredients = recetteDescription.getElementsByClass("m_content_recette_ingredients").first();
        Element recetteInfo = recetteDescription.getElementsByClass("m_content_recette_todo").first();

        String duree = recetteDuree.text();
        String ingredients = recetteIngredients.text();
        String infos = recetteInfo.text();

        mTemps.setText(duree);
        mIngredients.setText(ingredients);
        mPreparation.setText(infos);
    }
}
