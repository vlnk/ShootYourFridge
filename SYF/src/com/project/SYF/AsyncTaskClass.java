package com.project.SYF;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.project.SYF.dialogs.NoEntryFoundAlertDialog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

public class AsyncTaskClass extends AsyncTask<Void, Integer, Boolean> {

    private WeakReference<Main> mActivity = null;

    @SuppressWarnings("unused")
    private static final int timeoutConnection = 3000;

    @SuppressWarnings("unused")
    private static final int timeoutSocket = 5000;

    @SuppressWarnings("unused")
    private static final String url1 = "http://world.openfoodfacts" + ".org/api/v0/produit/";

    @SuppressWarnings("unused")
    private static final String url2 = ".json";

    private static final String htmlurl1 = "http://www.upcdatabase.com/item/";
    private static final String htmlurl2 = "";

    @SuppressWarnings("CanBeFinal")
    private String mUrlString;

    @SuppressWarnings("unused")
    private StringBuilder sb;

    @SuppressWarnings("unused")
    private static final String TAG_PRODUCT = "product";

    @SuppressWarnings("unused")
    private static final String TAG_KEYWORDS = "_keywords";

    private final ArrayList<String> keywordsList = new ArrayList<>();
    private boolean mNoValueFromProduct;

    private Document document;

    @SuppressWarnings("unused")
    public AsyncTaskClass(Activity mainActivity, String urlEndString) {
        super();
        this.mActivity = new WeakReference<>((Main) mainActivity);
        this.mUrlString = urlEndString;
        mNoValueFromProduct = false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        finalizeGetKeywordsList();

        if (mNoValueFromProduct) {
            // alert dialog to inform no info were found
            new NoEntryFoundAlertDialog().show(mActivity.get().getFragmentManager(), "tag");
        } else {
            mActivity.get().replaceAllInKeyWordsList(keywordsList);
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        URL urlToCheck;

        try {
            urlToCheck = new URL(htmlurl1 + mUrlString + htmlurl2);
            document = Jsoup.parse(urlToCheck, 5000);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Coulndt get infofromURL", "Couldn't Parse HTML " + e.toString());
        }

        return null;
    }

    private void finalizeGetKeywordsList() {
        String name;
        String content;
        String[] res;
        int idx1;
        int idx2;
        mNoValueFromProduct = true;

        Log.i("  doc :   ", document.toString());
        Element title = document.getElementsByTag("title").first();
        name = title.text();
        idx1 = name.indexOf(": ") + 2;
        name = name.substring(idx1);

        if (name.compareTo("Item Not Found") == 0) {
            mNoValueFromProduct = true;
        } else {
            mNoValueFromProduct = false;

            Element aliments = document.getElementsByClass("data").first();

            content = aliments.text();
            idx1 = content.indexOf("Description ") + 12;
            idx2 = content.indexOf("Size/Weight");
            content = content.substring(idx1, idx2);
            res = content.split(" ");
            for (int i = 0; i < res.length; i++) {
                keywordsList.add(i, res[i]);
            }
        }
    }
}
