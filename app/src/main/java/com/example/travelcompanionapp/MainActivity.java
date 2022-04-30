package com.example.travelcompanionapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelcompanionapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView mRecyclerView;
    private PlaceOfInterestAdapter mAdapter;
    private PlaceOfInterestViewModel mPlaceOfInterestViewModel;

    public static final byte[] BLANK_BITMAP = new byte[1];
    public static final String POI_EXTRA =
            "com.example.android.travelcompanionapp.extra.POI_EXTRA";
    public static final String POI_POS =
            "com.example.android.travelcompanionapp.extra.POI_POS";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.travelcompanionapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.fab.setOnClickListener(view -> {
            int itemCount = mAdapter.getItemCount();
            Intent poiIntent = new Intent(MainActivity.this, PlaceOfInterestActivity.class);
            poiIntent.putExtra(MainActivity.POI_EXTRA, new PlaceOfInterest());
            poiIntent.putExtra(POI_POS, itemCount);
            newPlaceOfInterestResultLauncher.launch(poiIntent);
        });

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PlaceOfInterestAdapter(this, this);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ViewModelProvider mViewModelProvider = new ViewModelProvider(this);
        mPlaceOfInterestViewModel = mViewModelProvider.get(PlaceOfInterestViewModel.class);
        mPlaceOfInterestViewModel.setUp(getApplication());

        mPlaceOfInterestViewModel.getAllPlaceOfInterests().observe(this, placeOfInterests -> {
            // Update the cached copy of the words in the adapter.
            mAdapter.setPlaceOfInterests(placeOfInterests);
        });

        requestLocation();
    }

    private void requestLocation() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            AlertDialog.Builder myAlertBuilder = new
                    AlertDialog.Builder(MainActivity.this);
            // Set the dialog title and message.
            myAlertBuilder.setTitle("Instructions");
            myAlertBuilder.setMessage("Add a Place of Interest with the plus button in the bottom corner.\n" +
                    "Click an item to edit it.\n" +
                    "Long click an item to delete.");
            // Add the dialog buttons.
            myAlertBuilder.setPositiveButton("OK", (dialog, which) -> {
                // User clicked OK button.
            });

            myAlertBuilder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        Intent poiIntent = new Intent(MainActivity.this, PlaceOfInterestActivity.class);
        poiIntent.putExtra(POI_EXTRA, mAdapter.getPlaceOfInterestAtPosition(position));
        poiIntent.putExtra(POI_POS, position);
        editPlaceOfInterestResultLauncher.launch(poiIntent);
    }

    ActivityResultLauncher<Intent> newPlaceOfInterestResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();
                if (result.getResultCode() == RESULT_OK) {
                    assert intent != null;
                    int pos = intent.getIntExtra(POI_POS, 0);
                    // Add a new poi to the list.
                    mPlaceOfInterestViewModel.insert(intent.getParcelableExtra(POI_EXTRA));
                    // Notify the adapter that the data has changed.
                    mAdapter.notifyItemInserted(pos);
                    // Scroll to the bottom.
                    mRecyclerView.smoothScrollToPosition(pos);
                }
            });

    ActivityResultLauncher<Intent> editPlaceOfInterestResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (result.getResultCode() == RESULT_OK) {
                        assert intent != null;
                        int pos = intent.getIntExtra(POI_POS, 0);
                        mPlaceOfInterestViewModel.update(intent.getParcelableExtra(MainActivity.POI_EXTRA));
                        mAdapter.notifyItemChanged(pos);
                    }
                }
            });

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onItemLongClick(int position) {
        String poiName = mAdapter.getPlaceOfInterestAtPosition(position).getName();
        AlertDialog.Builder myAlertBuilder = new
                AlertDialog.Builder(MainActivity.this);
        // Set the dialog title and message.
        myAlertBuilder.setTitle("Please confirm");
        myAlertBuilder.setMessage("Do you want to delete " + poiName + "?");
        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("YES", (dialog, which) -> {
            // User clicked YES button.
            mPlaceOfInterestViewModel.delete(mAdapter.getPlaceOfInterestAtPosition(position));
            mAdapter.notifyItemRemoved(position);
        });
        myAlertBuilder.setNegativeButton("NO", (dialog, which) -> {
            // User clicked NO button.
        });

        myAlertBuilder.show();

    }
}