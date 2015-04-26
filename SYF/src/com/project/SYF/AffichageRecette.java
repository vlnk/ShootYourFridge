package com.project.SYF;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by robinvermes on 25/04/2015.
 */
public class AffichageRecette extends Activity implements View.OnClickListener{

    private static String mHref;
    private static TextView mTempsPrepa;
    private static TextView mTempsCuisson;
    private static TextView mIngredients;
    private static TextView mTitrePreparation;
    private static TextView mPreparation;

    private static Recipe mCurrentRecipe;

    private Button mAddToFavorisBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.affichagerecette);

        Intent intent = getIntent();
        mCurrentRecipe = intent.getStringExtra("current recipe");

        mHref = mCurrentRecipe.getHref();

        mTempsPrepa = (TextView)findViewById(R.id.temps_preparation);
        mTempsCuisson = (TextView)findViewById(R.id.temps_cuisson);
        mIngredients = (TextView)findViewById(R.id.ingredients);
        mTitrePreparation = (TextView)findViewById(R.id.titre_preparation);
        mPreparation =  (TextView)findViewById(R.id.preparation);

        mAddToFavorisBtn = (Button)findViewById(R.id.addtofavoris_button);

        mAddToFavorisBtn.setOnClickListener(this);

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

        int idx = duree.indexOf("Temps de cuisson");
        String preparation = duree.substring(0, idx);
        String cuisson = duree.substring(idx);

        ingredients = ingredients.replace("- ", "\n- ");

        idx = infos.indexOf(": ") + 1;
        String titrePreparation = infos.substring(0, idx);
        String contenuPreparation = infos.substring(idx+1);
        contenuPreparation = contenuPreparation.replace(". ", ".\n");
        contenuPreparation = contenuPreparation.replace("Remarques :", "\n\nRemarques :");

        mTempsPrepa.setText(preparation);
        mTempsCuisson.setText(cuisson);
        mIngredients.setText(ingredients);
        mTitrePreparation.setText(titrePreparation);
        mPreparation.setText(contenuPreparation);
    }

    @Override
    public void onClick(View v) {
        //rechercher dans la BD si la recette y est deja

        //si non, on l'ajoute a la BD
            //Sans oublier de bine mettre l'ID
    }
}
