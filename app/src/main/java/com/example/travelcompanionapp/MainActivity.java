package com.example.travelcompanionapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelcompanionapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{
    private ArrayList<PlaceOfInterest> mPlaceOfInterestList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private PlaceOfInterestAdapter mAdapter;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    public static final String POI_LIST =
            "com.example.android.travelcompanionapp.extra.POI_LIST";
    public static final String POI_EXTRA =
            "com.example.android.travelcompanionapp.extra.POI_EXTRA";
    public static final String POI_POS =
            "com.example.android.travelcompanionapp.extra.POI_POS";
    public static final int NEW_POI_REQUEST = 1;
    public static final int EDIT_POI_REQUEST = 2;

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
                Intent poiIntent = new Intent(MainActivity.this, PlaceOfInterestActivity.class);
                poiIntent.putExtra(POI_EXTRA, new PlaceOfInterest());
                poiIntent.putExtra(POI_POS, placeOfInterestListSize);
                startActivityForResult(poiIntent, NEW_POI_REQUEST);
            }
        });

        if (savedInstanceState != null) {
            System.out.println("onCreate");
            mPlaceOfInterestList = savedInstanceState.getParcelableArrayList(POI_LIST);
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
        mPlaceOfInterestList.add(new PlaceOfInterest("Dudley Zoo", "A zoo and castle in Dudley.", R.drawable.zoo1, 3.5f, 11, "This is a Zoo in the Black Country town of Dudley."));
        mPlaceOfInterestList.add(new PlaceOfInterest("Buckingham Palace", "Palace of the Queen of England.", 4.0f, 5, "Royal Palace in London, England. Popular tourist destination."));
        mPlaceOfInterestList.add(new PlaceOfInterest("Stonehenge", "Random rocks in the English countryside.", 3.0f, 7, "How did they get here? No one really seems to know."));
        mPlaceOfInterestList.add(new PlaceOfInterest("Sweet Emporium", "Large sweet shop in Wakefield", 4.0f, 8, "Sells imported sweets and goodies. Expensive."));
        mPlaceOfInterestList.add(new PlaceOfInterest("National Science and Media Museum", "Museum celebrating science, TV, Film and Video Games.", 2.5f, 6, "Includes an Imax screen and a smaller cinema for indie films."));
        mPlaceOfInterestList.add(new PlaceOfInterest("Molineux", "Football Staidum for Wolves.", 5.0f, 9, "Home to mighty Wolverhampton Wanderers. Heaven on Earth."));
        mPlaceOfInterestList.add(new PlaceOfInterest("Merry Hill", "Large shopping complex in Dudley, UK.", 3.0f, 8, "One of those places that was way more enjoyable as a kid. Bring back the Monorail."));
        mPlaceOfInterestList.add(new PlaceOfInterest("Appleloosa", "A desert town with crazy Ponies feat Apple Orchards.", 3.5f, 7, "Fictional place. Dummy data is hard."));
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
        outState.putParcelableArrayList(POI_LIST, mPlaceOfInterestList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(int position) {
        Intent poiIntent = new Intent(MainActivity.this, PlaceOfInterestActivity.class);
        poiIntent.putExtra(POI_EXTRA, mPlaceOfInterestList.get(position));
        poiIntent.putExtra(POI_POS, position);
        startActivityForResult(poiIntent, EDIT_POI_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int pos = 0;
        if (requestCode == NEW_POI_REQUEST) {
            if (resultCode == RESULT_OK) {
                data.getIntExtra(POI_POS, 0);
                // Add a new poi to the list.
                mPlaceOfInterestList.add(data.getParcelableExtra(POI_EXTRA));
                // Notify the adapter that the data has changed.

                Objects.requireNonNull(mRecyclerView.getAdapter()).notifyItemInserted(pos);
                // Scroll to the bottom.
                mRecyclerView.smoothScrollToPosition(pos);
            }
        } else if (requestCode == EDIT_POI_REQUEST) {
            if (resultCode == RESULT_OK) {
                data.getIntExtra(POI_POS, 0);
                mPlaceOfInterestList.set(pos, data.getParcelableExtra(POI_EXTRA));
                mAdapter.notifyItemChanged(data.getIntExtra(POI_POS, 0));
            }
        }
    }

    @Override
    public void onItemLongClick(int position) {
        String poiName = mPlaceOfInterestList.get(position).getName();
        AlertDialog.Builder myAlertBuilder = new
                AlertDialog.Builder(MainActivity.this);
        // Set the dialog title and message.
        myAlertBuilder.setTitle("Please confirm");
        myAlertBuilder.setMessage("Do you want to delete " + poiName + "?");
        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("YES", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked YES button.
                        mPlaceOfInterestList.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                });
        myAlertBuilder.setNegativeButton("NO", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked NO button.
                    }
                });

        myAlertBuilder.show();

    }
}