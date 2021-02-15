package com.example.mohammeduzair.androidapp.Home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mohammeduzair.androidapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class locationtracker extends AppCompatActivity implements OnMapReadyCallback{
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationtracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getLocationPermission();

    }
    //when map is ready this method is invoked
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //calling in the actual map
        mMap = googleMap;
        //method invoked to creating markers
        setMapLongClick(mMap);
        //using if statement to see whether permission has been granted and what to do when this occurs
        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            //method created that finds current location using google methods
            //Ensuring all permissions are meant in the manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //enables location tracking
            mMap.setMyLocationEnabled(true);
            //adds user interface settings for compass, and toolbar and to provide a button that moves the map to current location
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);

        }
    }
    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            //when long click done on application invoke this method
            public void onMapLongClick(LatLng latLng) {
                //creating a snippet to be printed on the hover of the marker
                String snippet = String.format(Locale.getDefault(), //string prints out the co-ordinates of the marker
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);
                /*this clears the map of all markers to ensure only one marker is present, if not done map could be flooded
                with too many markers*/
                mMap.clear();
                //adding the marker and giving it features and calling in the snippet earlier
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.distance))
                        .draggable(true)
                        .snippet(snippet)
                        //changing the colour of the default colour marker to match the colour scheme of the application
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); // Inflating the menu to show on the page
        inflater.inflate(R.menu.map_menu, menu); //Referring to which menu we would like to be inflated via the correct menu id.
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection using switch statement.
        switch (item.getItemId()) {
            //if the normal button is pressed implement this method
            case R.id.normal_map:
                //change the maptype to normal using google provided method

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            //if the hybrid button is pressed implement this method
            case R.id.hybrid_map:
                //change the maptype to hybrid using google provided method
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(locationtracker.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getDeviceLocation(){

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        }else{
                            Toast.makeText(locationtracker.this,
                                    "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(locationtracker.this);
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    //initialising the map
                    initMap();
                }
            }
        }
    }

}
