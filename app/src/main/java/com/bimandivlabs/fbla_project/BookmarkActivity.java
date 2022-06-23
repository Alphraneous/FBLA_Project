package com.bimandivlabs.fbla_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

public class BookmarkActivity extends AppCompatActivity {

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Bookmarks");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
                    SharedPreferences sp = getSharedPreferences("reviews", MODE_PRIVATE);
                    ArrayList<Attraction> resultArray = new ArrayList<>();
                    String attractionsJSON = loadJSONFromAsset();
                    JSONObject jsonRootObject;
                    TextView nrf = findViewById(R.id.textView8);
                    TextView nrf2 = findViewById(R.id.textView9);
                    ListView resultsList = findViewById(R.id.resultsList2);
                    try {
                        jsonRootObject = new JSONObject(attractionsJSON);
                        JSONArray attractionsJSONArray = jsonRootObject.optJSONArray("Attraction");
                        JSONArray bookmarkList = new JSONArray(sp.getString("bookmarkList", "[]"));
                        if (!sp.getString("bookmarkList", "[]").equals("[]")) {
                            for (int i = 0; i < bookmarkList.length(); i++) {
                                JSONObject attraction = Objects.requireNonNull(attractionsJSONArray).getJSONObject(bookmarkList.getInt(i));
                                String name = attraction.optString("name");
                                String image = attraction.optString("image");
                                double Lat = attraction.optDouble("lat");
                                double Long = attraction.optDouble("long");
                                String website = attraction.optString("website");
                                String address = attraction.optString("address", "null");
                                Integer rating = attraction.optInt("rating", 0);
                                Integer price = attraction.optInt("price", 0);
                                LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                                LatLng dest = new LatLng(Lat,Long);
                                int distance = (int) Math.round(SphericalUtil.computeDistanceBetween(currentLocation,dest));
                                int distanceMiles = (int) Math.round(distance / 1609.34);
                                resultArray.add(new Attraction(bookmarkList.getInt(i),name,image,Integer.toString(distanceMiles),website,rating,price,address,Lat,Long));
                            }
                            nrf.setVisibility(View.GONE);
                            nrf2.setVisibility(View.GONE);
                            resultsList.setVisibility(View.VISIBLE);
                            Collections.sort(resultArray);
                            CustomAdapter customAdapter = new CustomAdapter(BookmarkActivity.this, resultArray);
                            resultsList.setAdapter(customAdapter);
                        } else {
                            nrf.setVisibility(View.VISIBLE);
                            nrf2.setVisibility(View.VISIBLE);
                            resultsList.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

}