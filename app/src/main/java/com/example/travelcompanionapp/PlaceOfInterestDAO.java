package com.example.travelcompanionapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaceOfInterestDAO {

    @Insert
    long insert(PlaceOfInterest placeOfInterest);

    @Delete
    void delete(PlaceOfInterest placeOfInterest);

    @Update
    void update(PlaceOfInterest placeOfInterest);

    @Query("SELECT * FROM place_of_interest")
    LiveData<List<PlaceOfInterest>> getAllPlaceOfInterests();
}
