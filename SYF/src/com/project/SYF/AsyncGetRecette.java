package com.project.SYF;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by annesohier on 26/04/2015.
 */
public class AsyncGetRecette extends AsyncTask<Void, Integer, Boolean> {

    private WeakReference<AffichageRecette> mActivity = null;

    private String mUrlString;
    private String mStartingUrl = "http://www.marmiton.org";

    private Document document;

    public AsyncGetRecette(Activity rechercheActivity, String urlString) {
        super();
        this.mActivity = new WeakReference<AffichageRecette>((AffichageRecette) rechercheActivity);
        mUrlString = urlString;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        mActivity.get().finalizeResearch(document);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        URL urlToCheck = null;

        try {
            urlToCheck = new URL(mStartingUrl + mUrlString);
            document = Jsoup.parse(urlToCheck, 5000);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Coulndt get infofromURL", "Couldn't Parse HTML " + e.toString());
        }
        return null;
    }

}