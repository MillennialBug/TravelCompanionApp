package com.example.travelcompanionapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelcompanionapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private final LinkedList<String> mPlaceOfInterestList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private PlaceOfInterestAdapter mAdapter;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*int placeOfInterestListSize = mPlaceOfInterestList.size();
                // Add a new word to the wordList.
                mPlaceOfInterestList.addLast("+ Word " + placeOfInterestListSize);
                // Notify the adapter that the data has changed.
                Objects.requireNonNull(mRecyclerView.getAdapter()).notifyItemInserted(placeOfInterestListSize);
                // Scroll to the bottom.
                mRecyclerView.smoothScrollToPosition(placeOfInterestListSize);*/
                Intent poiIntent = new Intent(MainActivity.this, PlaceOfInterestActivity.class);
                startActivity(poiIntent);

            }
        });

        for (int i = 0; i < 20; i++) {
            mPlaceOfInterestList.addLast("Word " + i);
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PlaceOfInterestAdapter(this, mPlaceOfInterestList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //requestLocationPermissions();
    }

    /*@RequiresApi(api = Build.VERSION_CODES.N)
    private void requestLocationPermissions() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}