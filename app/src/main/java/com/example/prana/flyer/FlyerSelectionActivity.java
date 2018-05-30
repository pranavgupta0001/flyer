package com.example.prana.flyer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlyerSelectionActivity extends MainNavigationActivity implements View.OnClickListener{

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String cotent;
    ImageButton drawerIcon;
    LinearLayout drawerLayout;

    @Override
    public int setLayout() {
        return R.layout.activity_flyer_selection;
    }

    @Override
    public void onLayoutCreated() {



        drawerIcon = (ImageButton) findViewById(R.id.drawer_image);
        drawerLayout = (LinearLayout) findViewById(R.id.drawer_menu);
        navigationView.setCheckedItem(R.id.nav_home);

        DownloadTask task = new DownloadTask();
        try {
            task.execute("https://drive.google.com/open?id=1Gpk4ScmJT2L0dy8LzZ1kFkBKkzz7dAY1");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.drawer_image:
            case R.id.drawer_menu:
                drawer.openDrawer(Gravity.LEFT);
                break;

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Exit Flyer?");
            alertDialogBuilder
                    .setMessage("Click yes to exit!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                }
                            })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }

    public void resumeView() {


        Intent intent = new Intent(getApplicationContext(), WebBrowserActivity.class);
        intent.putExtra("content", cotent);
        startActivity(intent);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while (data != -1) {
                    char currrent = (char) data;
                    result += currrent;
                    data = inputStreamReader.read();
                }
                Log.i("URL Content", result);
                cotent = result;
                resumeView();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
