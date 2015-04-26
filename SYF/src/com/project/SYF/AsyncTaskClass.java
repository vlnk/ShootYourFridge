package com.project.SYF;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.project.SYF.dialogs.DeleteCheckAlertDialog;
import com.project.SYF.dialogs.NoEntryFoundAlertDialog;
import com.project.SYF.helper.DatabaseHelper;
import com.project.SYF.model.Food;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by robinvermes on 24/04/2015.
 */
public class AsyncTaskClass extends AsyncTask<Void, Integer, Boolean> {

    private WeakReference<Main> mActivity = null;
    private static final int timeoutConnection = 3000;
    private static final int timeoutSocket = 5000;
    private static final String url1 = "http://world.openfoodfacts" +
            ".org/api/v0/produit/";
    private static final String url2 = ".json";

    private static final String htmlurl1 = "http://www.digit-eyes.com/cgi-bin/digiteyes.fcgi?action=upcList&upcCode=";
    private static final String htmlurl2 = "#.VT0LY2S8PGd";
    private String mUrlString;
    private StringBuilder sb;
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_KEYWORDS = "_keywords";

    private ArrayList<String> keywordsList = new ArrayList<String>();
    private boolean mNoValueFromProduct;

    private Document document;

    public AsyncTaskClass(Activity mainActivity, String urlEndString) {
        super();
        this.mActivity = new WeakReference<Main>((Main) mainActivity);
        this.mUrlString = urlEndString;
        this.sb = new StringBuilder();
        mNoValueFromProduct = false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        finalizeGetKeywordsList();

        if (mNoValueFromProduct){
            // alert dialog to inform no info were found
            new NoEntryFoundAlertDialog().show(mActivity.get().getFragmentManager(), "tag");
        }
        else{
            mActivity.get().replaceAllInKeyWordsList(keywordsList);
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        URL urlToCheck = null;

        try {
            urlToCheck = new URL(htmlurl1 + mUrlString + htmlurl2);
            document = Jsoup.parse(urlToCheck, 5000);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Coulndt get infofromURL", "Couldn't Parse HTML " + e.toString());
        }

        return null;
    }


    private void finalizeGetKeywordsList()
    {
        String name;
        String content;
        String[] res;
        mNoValueFromProduct = true;

        Elements aliments = document.getElementsByTag("meta");

        for(Element alim : aliments) {
            name = alim.attr("name");
            if (name.compareTo("keywords") == 0)
            {
                content = alim.attr("content");
                res = content.split(" ");
                mNoValueFromProduct = false;
                for (int i = 0; i < res.length; i++){
                    keywordsList.add(i, res[i]);
                }
            }
        }
    }


}
