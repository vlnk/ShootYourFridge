package com.project.SYF;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AsyncRechercheRecettes extends AsyncTask<Void, Integer, Boolean> {

    private WeakReference<RechercheRecette> mActivity = null;
    private static final String url1 = "http://www.marmiton.org/recettes/recherche.aspx?aqt=";
    private static final String url2 = "";

    private final String mUrlString;

    private Document document;

    private ArrayList<String> mAlimentList = new ArrayList<String>();

    public AsyncRechercheRecettes(Activity rechercheActivity, ArrayList<String> alimentList) {
        super();
        this.mActivity = new WeakReference<RechercheRecette>((RechercheRecette) rechercheActivity);
        this.mAlimentList = alimentList;
        mUrlString = url1 + createURL() + url2;
    }

    private String createURL(){
        String url = "";
        for (int i = 0; i < mAlimentList.size() - 1; i++)
        {
            url = url + mAlimentList.get(i) + "-";
        }
        url = url + mAlimentList.get(mAlimentList.size() - 1);

        return url;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        mActivity.get().finalizeResearch(document);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        URL urlToCheck;

        try {
            urlToCheck = new URL(mUrlString);
            document = Jsoup.parse(urlToCheck, 5000);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Coulndt get infofromURL", "Couldn't Parse HTML " + e.toString());
        }
        return null;
    }

}