package com.example.mohammeduzair.androidapp.Home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

//import com.androdocs.httprequest.HttpRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mohammeduzair.androidapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class weather extends AppCompatActivity {
    String CITY = "Leicester";
    String API = "88d07bb09e8d547cf46872a25208c018";
    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, windTxt, pressureTxt, humidityTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        //new weatherTask().execute();
        find_weather();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(weather.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void find_weather() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Leicester&units=metric&appid=88d07bb09e8d547cf46872a25208c018";
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main = response.getJSONObject("main");
                    JSONObject sys = response.getJSONObject("sys");
                    JSONObject wind = response.getJSONObject("wind");
                    JSONObject weather = response.getJSONArray("weather").getJSONObject(0);

                    Long updatedAt = response.getLong("dt");
                    String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                    String temp = main.getInt("temp") + "°C";
                    String tempMin = "Min Temp: " + main.getInt("temp_min") + "°C";
                    String tempMax = "Max Temp: " + main.getInt("temp_max") + "°C";
                    String pressure = main.getString("pressure");
                    String humidity = main.getString("humidity");
                    String windSpeed = wind.getString("speed");
                    String weatherDescription = weather.getString("description");

                    String address = response.getString("name") + ", " + sys.getString("country");


                    /* Populating extracted data into our views */
                    addressTxt.setText(address);
                    updated_atTxt.setText(updatedAtText);
                    statusTxt.setText(weatherDescription.toUpperCase());
                    tempTxt.setText(temp);
                    temp_minTxt.setText(tempMin);
                    temp_maxTxt.setText(tempMax);
                    windTxt.setText(windSpeed);
                    pressureTxt.setText(pressure);
                    humidityTxt.setText(humidity);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }

}
