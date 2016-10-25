package com.example.roshan.weather;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEventSource;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DailyForecast  extends MainPage {
    public ArrayList<Day> items;
    ForecastOverviewAdapter adapter;
    String city, CountryCode, language, unit;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setupTheme();
        setContentView(R.layout.activity_daily_forecast);
      //  setupToolbar();
      //  setToolbarBackIcon();
      //  items = new ArrayList<>();
        //downloadJSON();

        RecyclerView rvUsers = (RecyclerView) findViewById(R.id.rvUsers);
        adapter = new ForecastOverviewAdapter(this, getDays());
        rvUsers.setAdapter(adapter);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
    }


    public void setupToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    public void setToolbarBackIcon(){
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

   private ArrayList<Day> getDays() {
       return items;
    }

   /* public void downloadJSON() {
        /**
         * Get settings
         */
     //   city = SharedPreferences.getString("location", "Berlin");
     //   CountryCode = SharedPreferences.getString("countrykey", "DE");
     //   unit = SharedPreferences.getString("unitcode", "metric");
      //  language = SharedPreferences.getString("lang", "de");
        /**
         * Start JSON data download
         */
     /*   AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + latitude + "&lon=" + longitude +  "&cnt=7" + "&APPID=" + ApiKey, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            } */

       /*   @Override
            public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String in = new String(response);
                ParseData(in);
            }

            @Override
            public void onFailure(int statusCode, PreferenceActivity.Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Toast.makeText(DailyForecast.this, "Fail", Toast.LENGTH_SHORT).show();
            }  */

          /*  @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    } */

  public void ParseData(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            JSONArray list = reader.getJSONArray("list");
            JSONObject JSONList = list.getJSONObject(0);
            for (int i = 0; i < list.length(); i++) {
                JSONObject forJSONList = list.getJSONObject(i);
                JSONArray forWeather = forJSONList.getJSONArray("weather");
                JSONObject forJSONWeather = forWeather.getJSONObject(0);
                JSONObject fortemp = forJSONList.getJSONObject("temp");
                Log.i("RecyclerView", "JSON Parsing Nr." + i);
                items.add(new Day(forJSONList.getInt("dt"), fortemp.getDouble("max"), fortemp.getDouble("min"), forJSONList.getDouble("pressure"), forJSONList.getInt("humidity"), forJSONWeather.getInt("id"), forJSONWeather.getString("description"), forJSONList.getDouble("speed")));
                Log.i("RecyclerView", "Added items Nr" + i);
                adapter.notifyItemInserted(0);
                Log.i("RecyclerView", "notifyItemInserted Nr." + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("JSON Parsing", e.toString());
        }
    }
}

