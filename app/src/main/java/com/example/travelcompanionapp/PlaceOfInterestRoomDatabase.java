package com.example.travelcompanionapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.File;

@Database(entities = {PlaceOfInterest.class, PlaceOfInterestGallery.class}, version = 8, exportSchema = false)
public abstract class PlaceOfInterestRoomDatabase extends RoomDatabase {

    public abstract PlaceOfInterestDAO placeOfInterestDAO();
    public abstract PlaceOfInterestGalleryDAO placeOfInterestGalleryDAO();
    private static PlaceOfInterestRoomDatabase INSTANCE;

    static PlaceOfInterestRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlaceOfInterestRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlaceOfInterestRoomDatabase.class, "place_of_interest_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
