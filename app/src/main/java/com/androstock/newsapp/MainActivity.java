package com.androstock.newsapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
/*
Create by - Saurabh Kumar Dwivedi
Date - 04/20/2018
 */

public class MainActivity extends AppCompatActivity {

    String API_KEY = "3bb8f3abd9844b2d82f8987a285108d0"; // ### KET OF NEWS API###
    String NEWS_SOURCE = "bbc-news";
    CardView cardNews;
    ProgressBar loader;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    // Initialise an arraylist of maps mapping below attributes with their values.
    ArrayList<HashMap<String, String>> NewsDataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";
    // Initialise onCreate
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Retrieve the listNews and loader using findViewById
        cardNews = (CardView) findViewById(R.id.card_view);
        loader = (ProgressBar) findViewById(R.id.loader);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ListNewsAdapter(MainActivity.this,NewsDataList);
        mRecyclerView.setAdapter(mAdapter);

        //use isNetworkAvailable method of NetworkUtils Class to check if network is available,
        //if it is so, then downloadNews data else, log an error.
        if(NetworkUtils.isNetworkAvailable(getApplicationContext()))
        {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        }else{
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        //call setUpWindowsAnimation to setup exit ransition
        setUpWindowsAnimation();
    }
    //This method is used to create animated exit transition from mainactivity
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpWindowsAnimation(){
        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        getWindow().setExitTransition(fade);
    }

    //This class will help load the news data in the backgroud using the property of asyncTask class.
     class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        //use ExecuteGet function of NetworkUtils to retrieve the json data from the url
        protected String doInBackground(String... args) {
            String xml = "";

            String urlParameters = "";
            xml = NetworkUtils.excuteGet("https://newsapi.org/v1/articles?source="+NEWS_SOURCE+"&sortBy=top&apiKey="+API_KEY, urlParameters);
            return  xml;
        }
        //After the data is retrieved, map the values in the json file and add them to the NewsDatalist.
        @Override
        protected void onPostExecute(String xml) {

                if(xml.length()>10){ // Just checking if not empty

                    try {
                        JSONObject jsonResponse = new JSONObject(xml);
                        JSONArray jsonArray = jsonResponse.optJSONArray("articles");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR).toString());
                            map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE).toString());
                            map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION).toString());
                            map.put(KEY_URL, jsonObject.optString(KEY_URL).toString());
                            map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE).toString());
                            map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT).toString());
                            NewsDataList.add(map);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No news found", Toast.LENGTH_SHORT).show();
                }
        }



    }



}
