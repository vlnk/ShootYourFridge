package com.project.SYF;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by annesohier on 25/04/2015.
 */

public class AsyncRechercheRecettes extends AsyncTask<Void, Integer, Boolean> {

    private WeakReference<RechercheRecette> mActivity = null;
    private static final String url1 = "http://www.marmiton.org/recettes/recherche.aspx?aqt=";
    private static final String url2 = "";

    private String mUrlString;

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
        InputStream input;

        URL urlToCheck = null;

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