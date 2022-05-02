package com.example.travelcompanionapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaceOfInterestGalleryDAO {

    @Insert
    long insert(PlaceOfInterestGallery placeOfInterestGallery);

    @Query("DELETE FROM place_of_interest_gallery")
    void deleteAll();

    @Delete
    void delete(PlaceOfInterestGallery placeOfInterestGallery);

    @Query("SELECT * FROM place_of_interest_gallery WHERE poi_id = :poiId")
    LiveData<List<PlaceOfInterestGallery>> getGallery(int poiId);

}
