package com.example.mohammeduzair.androidapp.Home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mohammeduzair.androidapp.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class Homepage extends AppCompatActivity implements View.OnClickListener{
    private static final int NUM = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        // Defining what you want your cards to be
        CardView sensorHub = (CardView) findViewById(R.id.sensor1);
        CardView doorLocks = (CardView) findViewById(R.id.locksystem);
        CardView locationTracker = (CardView) findViewById(R.id.location_tracker);
        CardView weatherCard = (CardView) findViewById(R.id.weatherPage);


        // Setting up onclick listeners to the cards
        sensorHub.setOnClickListener(this);
        doorLocks.setOnClickListener(this);
        locationTracker.setOnClickListener(this);
        weatherCard.setOnClickListener(this);


}


    @Override
    public void onClick(View view) {
        Intent i;
        //switch statements to click into the cards created above
        //when switching making sure to start the new activity
        switch (view.getId()) {
            case R.id.sensor1:
                i = new Intent(Homepage.this, sensorhub.class);
                startActivity(i);
                break;
            case R.id.locksystem:
                i = new Intent (Homepage.this, locksys.class);
                startActivity(i);
                break;
            case R.id.location_tracker:
                i = new Intent (Homepage.this, locationtracker.class);
                startActivity(i);
                break;
            case R.id.weatherPage:
                i = new Intent (Homepage.this, weather.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

}
