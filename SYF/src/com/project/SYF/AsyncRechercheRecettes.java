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
import java.util.ArrayList;

/**
 * Created by annesohier on 25/04/2015.
 */

public class AsyncRechercheRecettes extends AsyncTask<Void, Integer, Boolean> {

    private WeakReference<RechercheRecette> mActivity = null;
    private static final int timeoutConnection = 3000;
    private static final int timeoutSocket = 5000;
    private static final String url1 = "http://www.marmiton.org/recettes/recherche.aspx?aqt=";
    private static final String url2 = "";

    private String mUrlString;
    private StringBuilder sb;

    private ArrayList<String> mAlimentList = new ArrayList<String>();

    public AsyncRechercheRecettes(Activity rechercheActivity, ArrayList<String> alimentList) {
        super();
        this.mActivity = new WeakReference<RechercheRecette>((RechercheRecette) rechercheActivity);
        this.mAlimentList = alimentList;
        this.sb = new StringBuilder();
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
        mActivity.get().putURL(sb.toString());

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        InputStream input;

        try {
            input = getInputStreamFromUrl(mUrlString);

            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            input.close();
        } catch (IOException e) {
            Log.e("couldnt create IStream", "Error getting stream " + e.toString());
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }


        return null;
    }

    public static InputStream getInputStreamFromUrl(String url) throws IOException
    {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpPost httpPost = new HttpPost(url);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();

        return httpEntity.getContent();
    }
}