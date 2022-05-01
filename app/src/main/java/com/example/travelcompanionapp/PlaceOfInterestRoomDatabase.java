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
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.File;

@Database(entities = {PlaceOfInterest.class}, version = 5, exportSchema = false)
public abstract class PlaceOfInterestRoomDatabase extends RoomDatabase {

    public abstract PlaceOfInterestDAO placeOfInterestDAO();
    private static PlaceOfInterestRoomDatabase INSTANCE;

    static PlaceOfInterestRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlaceOfInterestRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlaceOfInterestRoomDatabase.class, "place_of_interest_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

            File file = new File("");
            private final PlaceOfInterestDAO mDao;
            PlaceOfInterest[] placeOfInterests = {
                    //new PlaceOfInterest("Dudley Zoo", "A zoo and castle in Dudley.", 3.5f, 11, "This is a Zoo in the Black Country town of Dudley.", ((BitmapDrawable)Drawable.createFromPath("/storage/emulated/0/Download/zoo1.jpg")).getBitmap()),
                    new PlaceOfInterest("Dudley Zoo", "A zoo and castle in Dudley.", 3.5f, 11, "This is a Zoo in the Black Country town of Dudley."),
                    new PlaceOfInterest("Buckingham Palace", "Palace of the Queen of England.", 4.0f, 5, "Royal Palace in London, England. Popular tourist destination."),
                    new PlaceOfInterest("Stonehenge", "Random rocks in the English countryside.", 3.0f, 7, "How did they get here? No one really seems to know."),
                    new PlaceOfInterest("Sweet Emporium", "Large sweet shop in Wakefield", 4.0f, 8, "Sells imported sweets and goodies. Expensive."),
                    new PlaceOfInterest("National Science and Media Museum", "Museum celebrating science, TV, Film and Video Games.", 2.5f, 6, "Includes an Imax screen and a smaller cinema for indie films."),
                    new PlaceOfInterest("Molineux", "Football Staidum for Wolves.", 5.0f, 9, "Home to mighty Wolverhampton Wanderers. Heaven on Earth."),
                    new PlaceOfInterest("Merry Hill", "Large shopping complex in Dudley, UK.", 3.0f, 8, "One of those places that was way more enjoyable as a kid. Bring back the Monorail."),
                    new PlaceOfInterest("Appleloosa", "A desert town with crazy Ponies feat Apple Orchards.", 3.5f, 7, "Fictional place. Dummy data is hard.")
            };

            PopulateDbAsync(PlaceOfInterestRoomDatabase db) {
                mDao = db.placeOfInterestDAO();
            }

            @Override
            protected Void doInBackground(final Void... params) {
                // Start the app with a clean database every time.
                // Not needed if you only populate the database
                // when it is first created
                mDao.deleteAll();

                for (int i = 0; i <= placeOfInterests.length - 1; i++) {
                    PlaceOfInterest placeOfInterest = placeOfInterests[i];
                    mDao.insert(placeOfInterest);
                }
                return null;
            }
        }
}
