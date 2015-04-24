package com.project.SYF;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by robinvermes on 24/04/2015.
 */
public class AsyncTaskClass extends AsyncTask<Void, Integer, Boolean> {

    private WeakReference<Main> mActivity = null;
    private static final int timeoutConnection = 3000;
    private static final int timeoutSocket = 5000;
    private static final String url1 = "http://en.openfoodfacts.org/api/v0/produit/";
    private static final String url2 = ".json";
    private String mUrlString;
    private StringBuilder sb;

    public AsyncTaskClass(Activity mainActivity, String urlEndString) {
        super();
        this.mActivity = new WeakReference<Main>((Main) mainActivity);
        this.mUrlString = urlEndString;
        this.sb = new StringBuilder();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        mActivity.get().pushAddButton(sb.toString());
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String urlS = url1 + mUrlString + url2;
        InputStream input;

        try {
            input = getInputStreamFromUrl(urlS);

      /*      URL url = new URL(urlS);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            input = con.getInputStream();
        */
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
