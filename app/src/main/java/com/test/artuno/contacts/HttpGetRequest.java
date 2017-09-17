package com.test.artuno.contacts;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Artuno on 9/15/2017.
 */

public class HttpGetRequest extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... params) {
        StringBuilder s;
        String endpoint = params[1];
        String mainUrl = params[0];
        StringBuilder result = new StringBuilder();
        s = new StringBuilder(mainUrl);
        s.append(endpoint);
        try{
            URL myUrl = new URL(s.toString());

            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);

            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            String line;

            while((line=br.readLine())!=null){
                result.append(line);
            }

            br.close();
            isr.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.v("parse",result.toString());
        return (result.toString());
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }

}
