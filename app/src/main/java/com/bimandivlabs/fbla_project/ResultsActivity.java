package com.bimandivlabs.fbla_project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class ResultsActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    Boolean isInRange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        final ListView resultsList = findViewById(R.id.resultsList);
        ArrayList<Attraction> attractionArray = new ArrayList<Attraction>();
        ArrayList<Attraction2> resultArray = new ArrayList<Attraction2>();
        String attractionsJSON = loadJSONFromAsset(ResultsActivity.this);
        try {
            JSONObject jsonRootObject = new JSONObject(attractionsJSON);
            JSONArray attractionsJSONArray = jsonRootObject.optJSONArray("Attraction");

            for (int i = 0; i < attractionsJSONArray.length(); i++) {
                JSONObject attraction = attractionsJSONArray.getJSONObject(i);
                String name = attraction.optString("name");
                Boolean bathrooms = attraction.optBoolean("bathrooms");
                String imageLink = attraction.optString("image");
                Double Lat = attraction.optDouble("lat");
                Double Long = attraction.optDouble("long");
                attractionArray.add(new Attraction(name, bathrooms, Lat, Long, imageLink));
            }
            for (int i = 0; i < attractionArray.size(); i++) {
                String name = attractionArray.get(i).getName();
                String image = attractionArray.get(i).getImage();
                Double Lat = attractionArray.get(i).getLat();
                Double Long = attractionArray.get(i).getLong();
                Boolean hasBathrooms = attractionArray.get(i).getBathrooms();
                runChecks(20,Lat,Long, name, image);
            }
            CustomAdapter customAdapter = new CustomAdapter(this, resultArray);
            resultsList.setAdapter(customAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.attractions);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }



    public Boolean mbr (Boolean needsBathroom, Boolean hasBathroom) {
        if (needsBathroom) {
            return hasBathroom;
        } else {
            return true;
        }
    }
    public void runChecks (Integer maxRange, Double aLat, Double aLong, String name, String image) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

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
                            LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                            LatLng dest = new LatLng(aLat,aLong);
                            int distance = (int) Math.round(SphericalUtil.computeDistanceBetween(currentLocation,dest));
                            int distanceMiles = (int) Math.round(distance / 1609.34);
                            Boolean mdr = distanceMiles < maxRange;
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

    private void setInRange(boolean b) {
        isInRange=b;
    }

}