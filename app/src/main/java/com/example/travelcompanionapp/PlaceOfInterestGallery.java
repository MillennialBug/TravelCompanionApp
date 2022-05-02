package com.example.travelcompanionapp;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "place_of_interest_gallery")
public class PlaceOfInterestGallery implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "poi_id")
    private int poiId;

    @ColumnInfo(name = "image")
    private byte[] image;

    public PlaceOfInterestGallery(){ }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected PlaceOfInterestGallery(Parcel in) {
        id = in.readInt();
        poiId = in.readInt();
        image = in.createByteArray();
    }

    public static final Creator<PlaceOfInterestGallery> CREATOR = new Creator<PlaceOfInterestGallery>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public PlaceOfInterestGallery createFromParcel(Parcel in) {
            return new PlaceOfInterestGallery(in);
        }

        @Override
        public PlaceOfInterestGallery[] newArray(int size) {
            return new PlaceOfInterestGallery[size];
        }
    };

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPoiId(){
        return this.poiId;
    }
    public void setPoiId(int poiId) {this.poiId = poiId;}

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(poiId);
        parcel.writeByteArray(image);
    }
}
