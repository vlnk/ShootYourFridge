package com.project.SYF;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.project.SYF.helper.DatabaseHelper;
import com.project.SYF.model.Recipe;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.squareup.picasso.Picasso;

/**
 * Created by robinvermes on 25/04/2015.
 */
public class AffichageRecette extends Activity implements View.OnClickListener{

    private static String mRecipeHref;
    private static String mRecipeName;
    private static String mRecipeDetails;
    private static String mRecipeDescription;

    private static TextView mTempsPrepa;
    private static TextView mTempsCuisson;
    private static TextView mIngredients;
    private static TextView mTitrePreparation;
    private static TextView mPreparation;
    private static TextView mNameRecette;


    private static Recipe mCurrentRecipe;

    private Button mAddToFavorisBtn;
    private String mThereIsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mRecipeHref  = intent.getStringExtra("href");

        mThereIsButton = intent.getStringExtra("thereIsButton");
        mRecipeName  = intent.getStringExtra("name");

        if (mThereIsButton.compareTo("true") == 0) {
            setContentView(R.layout.affichagerecette);
            mRecipeDetails  = intent.getStringExtra("details");
            mRecipeDescription  = intent.getStringExtra("description");

            mAddToFavorisBtn = (Button) findViewById(R.id.addtofavoris_button);
            mAddToFavorisBtn.setOnClickListener(this);
        }
        else{
            setContentView(R.layout.affichagerecettefavoris);
        }

        mNameRecette = (TextView)findViewById(R.id.name);
        mNameRecette.setText(mRecipeName);
        mTempsPrepa = (TextView)findViewById(R.id.temps_preparation);
        mTempsCuisson = (TextView)findViewById(R.id.temps_cuisson);
        mIngredients = (TextView)findViewById(R.id.ingredients);
        mTitrePreparation = (TextView)findViewById(R.id.titre_preparation);
        mPreparation =  (TextView)findViewById(R.id.preparation);

        AsyncGetRecette mTask = new AsyncGetRecette(this, mRecipeHref);
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

        Element recettePhoto = document.getElementsByClass("photo").first();
        if (recettePhoto != null) {
            String photoUrl = recettePhoto.attr("src");

            ImageView imageView = (ImageView) findViewById(R.id.photo);
            Picasso.with(this).load(photoUrl).into(imageView);
        }
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
        String toToastString;
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Recipe thisRecipe = new Recipe(mRecipeName, mRecipeDetails, mRecipeDescription, mRecipeHref);
        if(db.addRecipe(thisRecipe)){
            toToastString = "Déjà dans les favoris";
        }
        else{
            toToastString = "Enregistrement effectué";
        }

        Toast mtoast = Toast.makeText(getApplicationContext(), toToastString, Toast.LENGTH_SHORT);
        mtoast.show();
    }
}
