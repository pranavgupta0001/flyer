package com.example.prana.flyer;


import android.app.ActivityManager;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.example.prana.flyer.Navigation.ServicesActivity;

import java.util.List;

public abstract class MainNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public NavigationView navigationView;
    public DrawerLayout drawer;
    Toolbar toolbar;
    View headerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        replaceContentLayout(setLayout());
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerLayout = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);
        onLayoutCreated();

    }

    public abstract int setLayout();

    public abstract void onLayoutCreated();





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Menu m = navigationView.getMenu();
        int id = item.getItemId();
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

        switch (id) {
            case R.id.nav_home:
                startActivity(new Intent(this, FlyerSelectionActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_view_profile:
                startActivity(new Intent(this, FlyerSelectionActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_our_services:
                startActivity(new Intent(this, ServicesActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_manage:
                startActivity(new Intent(this, ServicesActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_share:
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String abc = "Zahabia's Makeover Launge";
                    String sAux = "\n Amazing services at our place. Want a relaxing and body comforting services? Sign up and enjoy the most affordable offers. Download:\n\n";
                    sAux = sAux + "http://play.google.com/store/apps/details?id=com.zahabia";
                    intent.putExtra(Intent.EXTRA_TEXT, abc + sAux);
                    startActivity(Intent.createChooser(intent, ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                drawer.closeDrawer(GravityCompat.START);
                break;


            case R.id.nav_rating:
//                startActivity(new Intent(this, RateUs.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_about_us:
//                startActivity(new Intent(this, AboutUs.class));
                drawer.closeDrawer(GravityCompat.START);
                break;



            case R.id.nav_contact_us:
//                startActivity(new Intent(this, Contact_Us.class));
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_logout:

                try {


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return false;
    }

    private void replaceContentLayout(int sourceId) {
        View contentLayout = findViewById(R.id.drawer_content);
        ViewGroup parent = (ViewGroup) contentLayout.getParent();
        int index = parent.indexOfChild(contentLayout);
        parent.removeView(contentLayout);
        contentLayout = getLayoutInflater().inflate(sourceId, parent, false);
        parent.addView(contentLayout, index);
    }


   }
