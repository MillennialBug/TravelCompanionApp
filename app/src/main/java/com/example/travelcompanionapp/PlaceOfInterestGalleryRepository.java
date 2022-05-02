package com.example.travelcompanionapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PlaceOfInterestGalleryRepository {

    private PlaceOfInterestGalleryDAO placeOfInterestGalleryDAO;
    private LiveData<List<PlaceOfInterestGallery>> mGallery;

    PlaceOfInterestGalleryRepository(Application application, int poiId) {
        PlaceOfInterestRoomDatabase db = PlaceOfInterestRoomDatabase.getDatabase(application);
        placeOfInterestGalleryDAO = db.placeOfInterestGalleryDAO();
        mGallery = placeOfInterestGalleryDAO.getGallery(poiId);
    }

    LiveData<List<PlaceOfInterestGallery>> getGallery() {
        return mGallery;
    }

    public void insert (PlaceOfInterestGallery placeOfInterestGallery) {
        new insertAsyncTask(placeOfInterestGalleryDAO)
                .execute(placeOfInterestGallery);
    }

    public void delete (PlaceOfInterestGallery placeOfInterestGallery) {
        new deleteAsyncTask(placeOfInterestGalleryDAO).execute(placeOfInterestGallery);
    }

    private static class insertAsyncTask extends AsyncTask<PlaceOfInterestGallery, Void, Void> {

        private PlaceOfInterestGalleryDAO mAsyncTaskDao;

        insertAsyncTask(PlaceOfInterestGalleryDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlaceOfInterestGallery... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<PlaceOfInterestGallery, Void, Void> {
        private PlaceOfInterestGalleryDAO mAsyncTaskDao;

        deleteAsyncTask(PlaceOfInterestGalleryDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlaceOfInterestGallery... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}