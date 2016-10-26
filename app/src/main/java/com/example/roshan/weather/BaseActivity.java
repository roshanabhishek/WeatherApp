package com.example.roshan.weather;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by roshan on 26/10/16.
 */

public class BaseActivity extends AppCompatActivity {



    public String ApiKey = "ece6d7a1343ae0aa555c14a42ae75101";

    // ==============================================
    public Toolbar mToolbar;

    public DrawerLayout drawerLayout;
    public NavigationView navView;
    public static final String PREFS_NAME = "weatherapp";
    public android.content.SharedPreferences SharedPreferences;
    public WeatherHelper WeatherHelper;
    public int Theme;

    public void setupTheme(){
        SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (Integer.parseInt(SharedPreferences.getString("theme", "3"))){
            case 1:
                Theme = R.style.GreenTheme;
                break;
            case 2:
                Theme = R.style.MyMaterialTheme;
                break;
            case 3:
                Theme = R.style.BlueTheme;
                break;
            case 4:
                Theme = R.style.BlueTheme;
                break;
        }
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

    public void setupNavigationDrawer(){
        if (mToolbar != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_menu);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), MainPage.class));
                            }
                        }, 250);
                        return true;
                    case R.id.dailyforecast:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), DailyForecast.class));
                            }
                        }, 250);
                        return true;
                    case R.id.log_out:




                }
                return true;
            }
        });
    }

}
