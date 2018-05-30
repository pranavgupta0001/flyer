package com.example.prana.flyer.Navigation;

import android.content.Intent;


import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prana.flyer.FlyerSelectionActivity;
import com.example.prana.flyer.MainNavigationActivity;

import com.example.prana.flyer.R;

public class ServicesActivity extends MainNavigationActivity implements View.OnClickListener {

    ImageButton drawerIcon;
    LinearLayout drawerLayout;

    Intent intent;
    TextView h1, h6, sh5, id6;
    LinearLayout other_booking;
    TextView booking4;

    @Override
    public int setLayout() {
        return R.layout.activity_services;
    }

    @Override
    public void onLayoutCreated() {

        drawerIcon = (ImageButton) findViewById(R.id.drawer_image);
        drawerLayout = (LinearLayout) findViewById(R.id.drawer_menu);
        h1 = (TextView) findViewById(R.id.serviceh1);

        h6 = (TextView) findViewById(R.id.service_others);

        sh5 = (TextView) findViewById(R.id.service_others_h1);

        other_booking = (LinearLayout) findViewById(R.id.other_bookings);

        booking4 = (TextView) findViewById(R.id.book_others);

        id6 = (TextView) findViewById(R.id.serviceid6);

        navigationView.setCheckedItem(R.id.nav_our_services);


        drawerIcon.setOnClickListener(this);
        drawerLayout.setOnClickListener(this);
        other_booking.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        intent = new Intent(this, FlyerSelectionActivity.class);
        startActivity(intent);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.drawer_image:
            case R.id.drawer_menu:

                drawer.openDrawer(Gravity.LEFT);
                break;

            case R.id.other_bookings:
                intent = new Intent(this, FlyerSelectionActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }
}
