package com.example.travelcompanionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class PlaceOfInterestActivity extends AppCompatActivity {

    private Spinner mPoiSpinner;
    private TextView mPoiName;
    private TextView mPoiDescr;
    private ImageView mPoiMainImage;
    private TextView mPoiDateAdded;
    private int mPosition;
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
        mPoiDateAdded = findViewById(R.id.poi_date_added_text);
        mPoiSpinner = findViewById(R.id.poi_cat_spinner);
        mSaveButton = findViewById(R.id.button_save);
        mCancelButton = findViewById(R.id.button_cancel);

        //Set up spinner.
        createSpinner();

        Intent intent = getIntent();
        mPosition = intent.getIntExtra(MainActivity.POI_POS, 0);
        mPlaceOfInterest = intent.getParcelableExtra(MainActivity.POI_EXTRA);
        initialisePoi(mPlaceOfInterest);
        setButtonOnClicks();
        setTextChangeListeners();
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
    }

    public void initialisePoi(PlaceOfInterest poi) {
        mPoiName.setText(poi.getName());
        mPoiDescr.setText(poi.getShortDescription());
        mPoiMainImage.setImageResource(poi.getMainImage());
        mPoiDateAdded.setText(poi.getDateAdded());
    }

    public void returnReply(View view) {
        int viewId = view.getId();
        Intent replyIntent = new Intent();
        if(viewId == R.id.button_save) {
            replyIntent.putExtra(MainActivity.POI_POS, mPosition);
            replyIntent.putExtra(MainActivity.POI_EXTRA, mPlaceOfInterest);
        }
        setResult(RESULT_OK,replyIntent);
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

    private void itemChanged(View view) {
        if (view == mPoiName) {
            mPlaceOfInterest.setName(mPoiName.getText().toString());
            return;
        }
        if (view == mPoiDescr){
            mPlaceOfInterest.setShortDescription(mPoiDescr.getText().toString());
        }
    }
}