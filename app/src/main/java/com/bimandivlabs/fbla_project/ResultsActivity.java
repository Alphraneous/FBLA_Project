package com.bimandivlabs.fbla_project;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Search Results");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent dataIntent = getIntent();
        if (dataIntent != null) {
            Integer maxRange = dataIntent.getIntExtra("within",100);
            Integer requestedType = dataIntent.getIntExtra("type",0);
            Boolean needsBathroom = dataIntent.getBooleanExtra("bathrooms",false);
            Boolean needsFood = dataIntent.getBooleanExtra("food",false);
            Boolean needsAccessible = dataIntent.getBooleanExtra("accessible",false);
            Integer maxPrice = dataIntent.getIntExtra("maxPrice",25);
            runChecks(needsBathroom, needsFood, needsAccessible, requestedType, maxRange, maxPrice);
        }
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getResources().openRawResource(R.raw.attractions);

            int size = is.available();

            byte[] buffer = new byte[size];

            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);

            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }




    public void runChecks(Boolean needsBathroom, Boolean needsFood, Boolean needsAccessible, Integer requestedType, Integer maxRange, Integer maxPrice) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        fusedLocationClient.getCurrentLocation(100, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                //noinspection ConstantConditions
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        })
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        final ListView resultsList = findViewById(R.id.resultsList);
                        ArrayList<Attraction> resultArray = new ArrayList<>();
                        String attractionsJSON = loadJSONFromAsset();
                        try {
                            JSONObject jsonRootObject = new JSONObject(attractionsJSON);
                            JSONArray attractionsJSONArray = jsonRootObject.optJSONArray("Attraction");
                            for (int i = 0; i < Objects.requireNonNull(attractionsJSONArray).length(); i++) {
                                JSONObject attraction = attractionsJSONArray.getJSONObject(i);
                                String name = attraction.optString("name");
                                Boolean hasBathrooms = attraction.optBoolean("bathrooms");
                                Boolean hasFood = attraction.optBoolean("food");
                                Boolean hasAccessible = attraction.optBoolean("accessible");
                                JSONArray activityType = attraction.optJSONArray("type");
                                String image = attraction.optString("image");
                                double Lat = attraction.optDouble("lat");
                                double Long = attraction.optDouble("long");
                                String website = attraction.optString("website");
                                String address = attraction.optString("address","null");
                                Integer rating = attraction.optInt("rating",0);
                                Integer price = attraction.optInt("price",0);
                                LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                                LatLng dest = new LatLng(Lat,Long);
                                int distance = (int) Math.round(SphericalUtil.computeDistanceBetween(currentLocation,dest));
                                int distanceMiles = (int) Math.round(distance / 1609.34);
                                Boolean mdr = distanceMiles < maxRange;
                                if (checkReq(needsBathroom,hasBathrooms) && checkReq(needsFood,hasFood) && checkReq(needsAccessible, hasAccessible) && checkType(requestedType, activityType) && mdr && (price <= maxPrice)) {
                                    resultArray.add(new Attraction(i,name,image,Integer.toString(distanceMiles),website,rating,price,address));
                                }
                            }
                            TextView nrf = findViewById(R.id.textView3);
                            TextView nrf2 = findViewById(R.id.textView4);
                            if (resultArray.size() != 0) {
                                Collections.sort(resultArray);
                                CustomAdapter customAdapter = new CustomAdapter(ResultsActivity.this, resultArray);
                                resultsList.setAdapter(customAdapter);
                                Toast toast = Toast.makeText(ResultsActivity.this, resultArray.size() + " Results Found", Toast.LENGTH_LONG);
                                toast.show();
                                nrf.setVisibility(View.GONE);
                                nrf2.setVisibility(View.GONE);
                                resultsList.setVisibility(View.VISIBLE);
                            } else {
                                nrf.setVisibility(View.VISIBLE);
                                nrf2.setVisibility(View.VISIBLE);
                                resultsList.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast toast = Toast.makeText(ResultsActivity.this, "Unable to Fetch Location", Toast.LENGTH_LONG);
                        toast.show();
                        try {
                            Thread.sleep(100000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    public Boolean checkReq (Boolean needsReq, Boolean hasReq) {
        if (needsReq) {
            return hasReq;
        } else {
            return true;
        }
    }
    public Boolean checkType (Integer requestedType, JSONArray aType) {
        if (requestedType == 0) {
            return true;
        } else {
            for(int i =0; i< aType.length();i++) {
                try {
                    if(aType.getInt(i) == requestedType) {
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



}