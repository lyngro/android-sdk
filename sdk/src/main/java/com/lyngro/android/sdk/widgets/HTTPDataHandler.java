package com.lyngro.android.sdk.widgets;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by j.naim on 10-Jun-16.
 */
public class HTTPDataHandler {

    public static final int DEFAULT_CONNECTION_TIMEOUT = 10 * 10000;  // 5s
    private volatile int mTimeOut = DEFAULT_CONNECTION_TIMEOUT;

    public static final long DEFAULT_DISPATCH_INTERVAL = 120 * 1000; // 120s
    private volatile long mDispatchInterval = DEFAULT_DISPATCH_INTERVAL;

    String stream = null;

    public HTTPDataHandler(){
    }

    public String ReceiveHTTPData(String urlString){
        HttpURLConnection urlConnection = null;

        try{
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(mTimeOut);
            urlConnection.setReadTimeout(mTimeOut);
            // Check the connection status
            if(urlConnection.getResponseCode() == 200)
            {
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                stream = sb.toString();
                // End reading...............
            }
            else
            {
                // Do something
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            // Disconnect the HttpURLConnection
            if(urlConnection != null) urlConnection.disconnect();
        }

        // Return the data from specified url
        return stream;
    }

    public void SendHTTPData(String urlString, JSONObject jsonObject) {
        try {

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(mTimeOut);
            urlConnection.setReadTimeout(mTimeOut);

            urlConnection.setDoOutput(true); // Forces post;
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("charset", "utf-8");

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();

            if( urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                Log.d("HTTP Status", "OK");
            }
            else
            {
                Log.d("HTTP Status", String.valueOf(urlConnection.getResponseCode()));
            }

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}