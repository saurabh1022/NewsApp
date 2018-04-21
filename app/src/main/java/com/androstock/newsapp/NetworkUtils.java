package com.androstock.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SAURABH on 04/20/2018.
 * This class contains methods to check internet connect and executing the URL.
 */

public class NetworkUtils {
    // Check if the network is available
    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }


    //function to get the data from the specified URL
    public static String excuteGet(String targetURL, String urlParameters)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create a connection with the target URL
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            //connection.setRequestMethod("POST");
            //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("content-type", "application/json;  charset=utf-8");


            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(false);



            InputStream is;

            int status = connection.getResponseCode();
            // if connection is established,get the input stream, else show the error message
            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream();


            // use the befferedReader to read the text from inputstream
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            //record the text read by bufferedReader into a stringbuffer and convert the text into a string
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {


            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }



}
