package com.example.travelcompanionapp;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
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
                mPlaceOfInterestList.addLast("+ Word " + placeOfInterestListSize);
                // Notify the adapter that the data has changed.
                mRecyclerView.getAdapter().notifyItemInserted(placeOfInterestListSize);
                // Scroll to the bottom.
                mRecyclerView.smoothScrollToPosition(placeOfInterestListSize);
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
}