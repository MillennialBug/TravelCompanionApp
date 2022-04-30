package com.example.travelcompanionapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class PlaceOfInterestActivity extends AppCompatActivity implements LocationListener {

    private Spinner mPoiSpinner;
    private TextView mPoiName;
    private TextView mPoiDescr;
    private ImageView mPoiMainImage;
    private TextView mPoiDateAdded;
    private TextView mNotes;
    private TextView mPoiLocation;
    private RatingBar mRating;
    private int mPosition = 0;
    private Button mMapButton;
    private Button mShareButton;
    private Button mSaveButton;
    private Button mCancelButton;
    private PlaceOfInterest mPlaceOfInterest;
    private String[] mCategories;
    private LocationManager locationManager;
    private Double mPoiLongitude;
    private Double mPoiLatitude;
    private Location mLocation;
    private Context mContext;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_of_interest);
        mContext = getApplicationContext();

        //Get views
        mPoiName = findViewById(R.id.poi_name_text);
        mPoiDescr = findViewById(R.id.poi_descr_text);
        mPoiMainImage = findViewById(R.id.poi_main_image);
        mRating = findViewById(R.id.poi_ratingBar);
        mPoiDateAdded = findViewById(R.id.poi_date_added_text);
        mPoiSpinner = findViewById(R.id.poi_cat_spinner);
        mPoiLocation = findViewById(R.id.poi_location_text);
        mNotes = findViewById(R.id.poi_notes_text);
        mMapButton = findViewById(R.id.button_location);
        mShareButton = findViewById(R.id.button_share);
        mSaveButton = findViewById(R.id.button_save);
        mCancelButton = findViewById(R.id.button_cancel);

        //Set LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Get intent
        Intent intent = getIntent();

        // Retrieve POI info from Intent
        if (intent.hasExtra(MainActivity.POI_POS))
            mPosition = intent.getIntExtra(MainActivity.POI_POS, 0);

        if (intent.hasExtra(MainActivity.POI_EXTRA)) {
            mPlaceOfInterest = intent.getParcelableExtra(MainActivity.POI_EXTRA);
        } else {
            mPlaceOfInterest = new PlaceOfInterest();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        initialisePoi(mPlaceOfInterest);
        initialiseCategories();

        //Set up spinner.
        createSpinner();

        // Set Listeners
        setButtonOnClicks();
        setTextChangeListeners();
        mRating.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            if(b) mPlaceOfInterest.setRating(v);
        });
    }

    private void initialiseCategories() {
        Resources res = getResources();
        TypedArray ta = res.obtainTypedArray(R.array.poi_cat_array);
        int n = ta.length();
        String[] array = new String[n];
        for (int i = 0; i < n; ++i) {
            array[i] = ta.getString(i);
        }
        mCategories = array;
        ta.recycle(); // Important!
    }

    public void initialisePoi(PlaceOfInterest poi) {
        mPoiName.setText(poi.getName());
        mPoiDescr.setText(poi.getShortDescription());
        //if (poi.getBitmap() != MainActivity.BLANK_BITMAP)
            //mPoiMainImage.setImageBitmap(poi.getBitmapAsBitmap());
        mPoiDateAdded.setText(poi.getDateAdded());
        mPoiSpinner.setSelection(poi.getCategory());
        mNotes.setText(poi.getNotes());
        mRating.setRating(poi.getRating());
        if(poi.getName() == null){
            getLocation();
        } else {
            mPoiLongitude = poi.getLongitude();
            mPoiLatitude = poi.getLatitude();
        }
        if(mPlaceOfInterest.getUserSetLocation()){
            mPoiLocation.setText(poi.getLocation());
        } else {
            mPoiLocation.setText(String.format("%s, %s",mPoiLongitude.toString(), mPoiLatitude.toString()));
        }
    }

    public void returnReply(View view) {
        int viewId = view.getId();
        Intent replyIntent = new Intent();
        if(viewId == R.id.button_save) {
            replyIntent.putExtra(MainActivity.POI_POS, mPosition);
            replyIntent.putExtra(MainActivity.POI_EXTRA, mPlaceOfInterest);
            setResult(RESULT_OK,replyIntent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickImageResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
    ActivityResultLauncher<Intent> pickImageResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (result.getResultCode() == RESULT_OK) {
                        assert intent != null;
                        try {
                            InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(intent.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            mPlaceOfInterest.setBitmap(bitmap);
                            mPoiMainImage.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private void createSpinner(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.poi_cat_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mPoiSpinner.setAdapter(adapter);
        mPoiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlaceOfInterest.setCategory(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mPoiSpinner.setSelection(mPlaceOfInterest.getCategory());
            }
        });
        mPoiSpinner.setSelection(mPlaceOfInterest.getCategory());
    }

    private void setButtonOnClicks() {
        mMapButton.setOnClickListener(view -> viewOnMap());
        mShareButton.setOnClickListener(view -> sharePlaceOfInterest());
        mSaveButton.setOnClickListener(view -> returnReply(mSaveButton));
        mCancelButton.setOnClickListener(view -> returnReply(mCancelButton));
        mPoiMainImage.setOnClickListener(view -> pickImage());
    }

    private void setTextChangeListeners() {
        mPoiName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                itemChanged(mPoiName);
            }
        });
        mPoiDescr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                itemChanged(mPoiDescr);
            }
        });
        mPoiLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) { itemChanged(mPoiLocation);}
        });
        mNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) { itemChanged(mNotes); }
        });
    }

    private void itemChanged(View view) {
        if (view == mPoiName) {
            mPlaceOfInterest.setName(mPoiName.getText().toString());
            return;
        }
        if (view == mPoiDescr){
            mPlaceOfInterest.setShortDescription(mPoiDescr.getText().toString());
            return;
        }
        if (view == mPoiLocation) {
            mPlaceOfInterest.setLocation(mPoiLocation.getText().toString());
            return;
        }
        if (view == mNotes){
            mPlaceOfInterest.setNotes(mNotes.getText().toString());
        }
    }

    private void sharePlaceOfInterest() {
        String mimeType = "text/plain";
        String txt = String.format("Hey! Check this place out!\n" +
                "Name: %s\n" +
                "Category: %s\n" +
                "Description: %s", mPlaceOfInterest.getName(), mCategories[mPlaceOfInterest.getCategory()], mPlaceOfInterest.getShortDescription());
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share this text with: ")
                .setText(txt)
                .startChooser();
    }

    private void viewOnMap() {
        // Get the string indicating a location. Input is not validated; it is
        // passed to the location handler intact.
        if (!mPlaceOfInterest.getLocation().isEmpty() || mPlaceOfInterest.getLocation() != null) {

            // Parse the location and create the intent.
            Uri addressUri = Uri.parse("geo:0,0?q=" + mPlaceOfInterest.getLocation());
            Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);

            // Find an activity to handle the intent, and start that activity.
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    private void getLocation(){
        // getting GPS status
        Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        Boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled) {
            if (mLocation == null) {
                //check the network permission
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {
                    mLocation = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (mLocation != null) {
                        mPoiLatitude = mLocation.getLatitude();
                        mPoiLongitude = mLocation.getLongitude();
                        mPlaceOfInterest.setLongitude(mPoiLongitude);
                        mPlaceOfInterest.setLatitude(mPoiLatitude);
                    }
                }
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}