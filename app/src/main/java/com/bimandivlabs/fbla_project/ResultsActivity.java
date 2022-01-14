package com.bimandivlabs.fbla_project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        runChecks(this,false,false,false,0,80);
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getResources().openRawResource(R.raw.attractions);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }




    public void runChecks (Context context, Boolean needsBathroom, Boolean needsFood, Boolean needsAccessible, Integer requestedType, Integer maxRange) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        fusedLocationClient.getCurrentLocation(100, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        })
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onSuccess(Location location) {
                        if (location != null) {
                            final ListView resultsList = findViewById(R.id.resultsList);
                            ArrayList<Attraction> attractionArray = new ArrayList<Attraction>();
                            ArrayList<Attraction2> resultArray = new ArrayList<Attraction2>();
                            String attractionsJSON = loadJSONFromAsset();
                            try {
                                JSONObject jsonRootObject = new JSONObject(attractionsJSON);
                                JSONArray attractionsJSONArray = jsonRootObject.optJSONArray("Attraction");

                                for (int i = 0; i < Objects.requireNonNull(attractionsJSONArray).length(); i++) {
                                    JSONObject attraction = attractionsJSONArray.getJSONObject(i);
                                    String name = attraction.optString("name");
                                    Boolean bathrooms = attraction.optBoolean("bathrooms");
                                    Boolean food = attraction.optBoolean("food");
                                    Boolean accessible = attraction.optBoolean("accessible");
                                    Integer type = attraction.optInt("type");
                                    String imageLink = attraction.optString("image");
                                    Double Lat = attraction.optDouble("lat");
                                    Double Long = attraction.optDouble("long");
                                    attractionArray.add(new Attraction(name, bathrooms, food, accessible, type, Lat, Long, imageLink));
                                }
                                for (int i = 0; i < attractionArray.size(); i++) {
                                    String name = attractionArray.get(i).getName();
                                    String image = attractionArray.get(i).getImage();
                                    Double Lat = attractionArray.get(i).getLat();
                                    Double Long = attractionArray.get(i).getLong();
                                    Boolean hasBathrooms = attractionArray.get(i).getBathrooms();
                                    Boolean hasFood = attractionArray.get(i).getFood();
                                    Boolean hasAccessible = attractionArray.get(i).getAccessible();
                                    Integer activityType = attractionArray.get(i).getActivityType();
                                    LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                                    LatLng dest = new LatLng(Lat,Long);
                                    int distance = (int) Math.round(SphericalUtil.computeDistanceBetween(currentLocation,dest));
                                    int distanceMiles = (int) Math.round(distance / 1609.34);
                                    Boolean mdr = distanceMiles < maxRange;
                                    if (checkReq(needsBathroom,hasBathrooms) && checkReq(needsFood,hasFood) && checkReq(needsAccessible, hasAccessible) && checkType(requestedType, activityType) && mdr) {
                                        resultArray.add(new Attraction2(name,image,Integer.toString(distanceMiles)));
                                    }
                                }
                                TextView nrf = findViewById(R.id.textView3);
                                TextView nrf2 = findViewById(R.id.textView4);
                                if (resultArray.size() != 0) {
                                    CustomAdapter customAdapter = new CustomAdapter(ResultsActivity.this, resultArray);
                                    resultsList.setAdapter(customAdapter);
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
    public Boolean checkType (Integer requestedType, Integer aType) {
        if (requestedType == 0) {
            return true;
        } else {
            return requestedType.equals(aType);
        }
    }


}