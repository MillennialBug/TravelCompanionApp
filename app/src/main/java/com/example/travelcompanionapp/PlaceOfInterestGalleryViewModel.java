package com.example.travelcompanionapp;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class PlaceOfInterestGalleryViewModel extends ViewModel {

    private PlaceOfInterestGalleryRepository mRepository;

    private LiveData<List<PlaceOfInterestGallery>> mGallery;

    public PlaceOfInterestGalleryViewModel() {
        super();
    }

    public void setUp(Application application, int poiId) {
        mRepository = new PlaceOfInterestGalleryRepository(application, poiId);
        mGallery = mRepository.getGallery();
    }

    LiveData<List<PlaceOfInterestGallery>> getGallery() { return mGallery; }

    public void insert(PlaceOfInterestGallery placeOfInterestGallery) { mRepository.insert(placeOfInterestGallery); }

    public void delete(PlaceOfInterestGallery placeOfInterestGallery) { mRepository.delete(placeOfInterestGallery); }
}
