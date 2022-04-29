package com.example.travelcompanionapp;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlaceOfInterestViewModel extends ViewModel {

    private PlaceOfInterestRepository mRepository;

    private LiveData<List<PlaceOfInterest>> mAllPlaceOfInterests;

    public PlaceOfInterestViewModel() {
        super();
    }

    public void setUp(Application application) {
        mRepository = new PlaceOfInterestRepository(application);
        mAllPlaceOfInterests = mRepository.getAllPlaceOfInterests();
    }

    LiveData<List<PlaceOfInterest>> getAllPlaceOfInterests() { return mAllPlaceOfInterests; }

    public void insert(PlaceOfInterest placeOfInterest) { mRepository.insert(placeOfInterest); }

    public void update(PlaceOfInterest placeOfInterest) { mRepository.update(placeOfInterest); }

    public void delete(PlaceOfInterest placeOfInterest) { mRepository.delete(placeOfInterest); }
}
