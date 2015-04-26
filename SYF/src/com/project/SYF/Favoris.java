package com.project.SYF;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by annesohier on 26/04/2015.
 */
public class Favoris extends ListActivity implements AdapterView.OnItemClickListener {

    private ArrayList<HashMap<String, String>> recetteList;

    private ArrayAdapter<String> mAlimentListAdapter;
    private ListView mRecetteListView;

    private static final String TAG_NAME = "name";
    private static final String TAG_DETAILS = "details";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_HREF = "href";

    private ArrayList<String> mAlimentList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rechercherecette);

        Intent intent = getIntent();

        //recuperation depuis la BD A FAIRE

        recetteList = new ArrayList<HashMap<String, String>>();



        mRecetteListView = getListView();

        mRecetteListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent validateIntent = new Intent(Favoris.this, AffichageRecette.class);
                validateIntent.putExtra("href", recetteList.get(position).get(TAG_HREF));
                startActivity(validateIntent);
            }
        });


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }





}
