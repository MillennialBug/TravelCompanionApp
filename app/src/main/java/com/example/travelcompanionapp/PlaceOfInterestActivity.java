package com.example.travelcompanionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

public class PlaceOfInterestActivity extends AppCompatActivity {

    private Spinner mPoiSpinner;
    private TextView mPoiName;
    private TextView mPoiDescr;
    private ImageView mPoiMainImage;
    private TextView mPoiDateAdded;
    private TextView mNotes;
    private RatingBar mRating;
    private int mPosition = 0;
    private Button mSaveButton;
    private Button mCancelButton;
    private PlaceOfInterest mPlaceOfInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_of_interest);

        //Get views
        mPoiName = findViewById(R.id.poi_name_text);
        mPoiDescr = findViewById(R.id.poi_descr_text);
        mPoiMainImage = findViewById(R.id.poi_main_image);
        mRating = findViewById(R.id.poi_ratingBar);
        mPoiDateAdded = findViewById(R.id.poi_date_added_text);
        mPoiSpinner = findViewById(R.id.poi_cat_spinner);
        mNotes = findViewById(R.id.poi_notes_text);
        mSaveButton = findViewById(R.id.button_save);
        mCancelButton = findViewById(R.id.button_cancel);

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

        initialisePoi(mPlaceOfInterest);

        //Set up spinner.
        createSpinner();

        // Set Listeners
        setButtonOnClicks();
        setTextChangeListeners();
        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(b) mPlaceOfInterest.setRating(v);
            }
        });
    }

    public void initialisePoi(PlaceOfInterest poi) {
        mPoiName.setText(poi.getName());
        mPoiDescr.setText(poi.getShortDescription());
        mPoiMainImage.setImageResource(poi.getMainImage());
        mPoiDateAdded.setText(poi.getDateAdded());
        mPoiSpinner.setSelection(poi.getCategory());
        mNotes.setText(poi.getNotes());
        mRating.setRating(poi.getRating());
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
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnReply(mSaveButton);
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnReply(mCancelButton);
            }
        });
    }

    private void setTextChangeListeners() {
        mPoiName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                itemChanged(mPoiName);
            }
        });

        mPoiDescr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                itemChanged(mPoiDescr);
            }
        });

        mNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

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
        if (view == mNotes){
            mPlaceOfInterest.setNotes(mNotes.getText().toString());
            return;
        }
    }
}