package com.example.travelcompanionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedList;

public class PlaceOfInterestActivity extends AppCompatActivity implements OnMapReadyCallback {

    //private boolean isPermissionGranted;
    //private MapView mapView;

    private final LinkedList<Integer> mPlaceOfInterestImageList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private PlaceOfInterestImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_of_interest);

        //mapView = findViewById(R.id.poi_location_mapview);

        Spinner spinner = (Spinner) findViewById(R.id.poi_cat_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.poi_cat_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        mPlaceOfInterestImageList.addLast(R.drawable.zoo1);
        mPlaceOfInterestImageList.addLast(R.drawable.zoo2);
        mPlaceOfInterestImageList.addLast(R.drawable.zoo3);
        mPlaceOfInterestImageList.addLast(R.drawable.zoo4);

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.poi_image_recycleview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PlaceOfInterestImageAdapter(this, mPlaceOfInterestImageList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}