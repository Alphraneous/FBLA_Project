package com.bimandivlabs.fbla_project;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.bimandivlabs.fbla_project.databinding.ActivityMapBinding;

import org.w3c.dom.Attr;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    ArrayList resultsArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageButton backBtn = findViewById(R.id.mapBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent dataIntent = getIntent();
        if(dataIntent != null) {
            resultsArray = (ArrayList) dataIntent.getSerializableExtra("results");
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Attraction attractionIn = (Attraction) resultsArray.get(0);
        LatLng poiIn = new LatLng(attractionIn.Lat, attractionIn.Long);
        mMap.addMarker(new MarkerOptions().position(poiIn).title("1 - " + attractionIn.Name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(poiIn));
        for (int i = 1;i < resultsArray.size();i++) {
            Attraction attraction = (Attraction) resultsArray.get(i);
            LatLng poi = new LatLng(attraction.Lat, attraction.Long);
            mMap.addMarker(new MarkerOptions().position(poi).title((i+1) + " - " + attraction.Name));

        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String title = marker.getTitle();
        assert title != null;
        String id = title.substring(0,1);
        Attraction data = (Attraction) resultsArray.get(Integer.parseInt(id)-1);
        Intent intent = new Intent(MapActivity.this, MoreActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        return false;
    }
}