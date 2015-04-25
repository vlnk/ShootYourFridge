package com.project.SYF;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import android.content.Intent;

/**
 * Created by robinvermes on 25/04/2015.
 */
public class RechercheRecette extends Activity{

    private ArrayList<String> mAlimentList = new ArrayList<String>();
    private ArrayAdapter<String> mAlimentListAdapter;
    private ListView mAlimentListView;

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

    public void putURL(String url){
        mAlimentList.clear();
        mAlimentList.add(url);
        mAlimentListAdapter.notifyDataSetChanged();
    }
}
