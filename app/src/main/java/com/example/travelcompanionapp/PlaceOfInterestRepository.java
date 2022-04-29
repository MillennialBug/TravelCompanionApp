package com.example.travelcompanionapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PlaceOfInterestRepository {

    private PlaceOfInterestDAO mPlaceOfInterestDao;
    private LiveData<List<PlaceOfInterest>> mAllPlaceOfInterests;

    PlaceOfInterestRepository(Application application) {
        PlaceOfInterestRoomDatabase db = PlaceOfInterestRoomDatabase.getDatabase(application);
        mPlaceOfInterestDao = db.placeOfInterestDAO();
        mAllPlaceOfInterests = mPlaceOfInterestDao.getAllPlaceOfInterests();
    }

    LiveData<List<PlaceOfInterest>> getAllPlaceOfInterests() {
        return mAllPlaceOfInterests;
    }

    public void insert (PlaceOfInterest placeOfInterest) {
        new insertAsyncTask(mPlaceOfInterestDao).execute(placeOfInterest);
    }

    public void update (PlaceOfInterest placeOfInterest) {
        new updateAsyncTask(mPlaceOfInterestDao).execute(placeOfInterest);
    }

    public void delete (PlaceOfInterest placeOfInterest) {
        new deleteAsyncTask(mPlaceOfInterestDao).execute(placeOfInterest);
    }

    private static class insertAsyncTask extends AsyncTask<PlaceOfInterest, Void, Void> {

        private PlaceOfInterestDAO mAsyncTaskDao;

        insertAsyncTask(PlaceOfInterestDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlaceOfInterest... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<PlaceOfInterest, Void, Void> {
        private PlaceOfInterestDAO mAsyncTaskDao;

        updateAsyncTask(PlaceOfInterestDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlaceOfInterest... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<PlaceOfInterest, Void, Void> {
        private PlaceOfInterestDAO mAsyncTaskDao;

        deleteAsyncTask(PlaceOfInterestDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlaceOfInterest... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}