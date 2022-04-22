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

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{
    private ArrayList<PlaceOfInterest> mPlaceOfInterestList = new ArrayList<>();
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
                int placeOfInterestListSize = mPlaceOfInterestList.size();
                // Add a new word to the wordList.
                mPlaceOfInterestList.add(new PlaceOfInterest("New Place " + placeOfInterestListSize, "A new place!"));
                // Notify the adapter that the data has changed.
                Objects.requireNonNull(mRecyclerView.getAdapter()).notifyItemInserted(placeOfInterestListSize);
                // Scroll to the bottom.
                mRecyclerView.smoothScrollToPosition(placeOfInterestListSize);
                //TODO: call on click to move to activity.
            }
        });

        if (savedInstanceState != null) {
            mPlaceOfInterestList = savedInstanceState.getParcelableArrayList("poiList");
        }else {
            setUpPoiList();
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PlaceOfInterestAdapter(this, mPlaceOfInterestList, this);
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

    public void setUpPoiList() {
        // Add POI objects to list.
        mPlaceOfInterestList.add(new PlaceOfInterest("Dudley Zoo", "A zoo and castle in Dudley.", R.drawable.zoo1));
        mPlaceOfInterestList.add(new PlaceOfInterest("Buckingham Palace", "Palace of the Queen of England."));
        mPlaceOfInterestList.add(new PlaceOfInterest("Stonehenge", "Random rocks in the English countryside."));
        mPlaceOfInterestList.add(new PlaceOfInterest("Sweet Emporium", "Large sweet shop in Wakefield"));
        mPlaceOfInterestList.add(new PlaceOfInterest("National Science and Media Museum", "Museum celebrating science, TV, Film and Video Games."));
        mPlaceOfInterestList.add(new PlaceOfInterest("Molineux", "Football Staidum for Wolves."));
        mPlaceOfInterestList.add(new PlaceOfInterest("Merry Hill", "Large shopping complex in Dudley, UK."));
        mPlaceOfInterestList.add(new PlaceOfInterest("Appleloosa", "A desert town with crazy Ponies feat Apple Orchards."));
    }

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("poiList", mPlaceOfInterestList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(int position) {
        Intent poiIntent = new Intent(MainActivity.this, PlaceOfInterestActivity.class);
        //TODO: PASS POI ITEM TO ACTIVITY AND READ IT IN ON THE OTHER SIDE.
        startActivity(poiIntent);
    }

    @Override
    public void onItemLongClick(int position) {
        mPlaceOfInterestList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }
}