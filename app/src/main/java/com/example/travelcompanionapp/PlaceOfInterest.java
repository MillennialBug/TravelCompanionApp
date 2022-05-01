package com.example.travelcompanionapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "place_of_interest")
public class PlaceOfInterest implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "Bitmap")
    private byte[] bitmap;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "short_description")
    private String shortDescription;

    @ColumnInfo(name = "date_added")
    private String dateAdded;

    @ColumnInfo(name = "rating")
    private Float rating;

    @ColumnInfo(name = "catergory")
    private int category;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    @ColumnInfo(name = "latitude")
    private Double latitude;

    //private int[] photos;

    public PlaceOfInterest(String name, String shortDescription, Float rating, int category, String notes) {
        setName(name);
        setShortDescription(shortDescription);
        setRating(rating);
        setCategory(category);
        setNotes(notes);
        setDateAdded();
        setBitmap(MainActivity.BLANK_BITMAP);
        setLongitude(0d);
        setLatitude(0d);
    }

    public PlaceOfInterest(String name, String shortDescription, Float rating, int category, String notes, Bitmap bitmap) {
        setName(name);
        setShortDescription(shortDescription);
        setRating(rating);
        setCategory(category);
        setNotes(notes);
        setDateAdded();
        setBitmap(bitmap);
    }

    public PlaceOfInterest(){
        setDateAdded();
        setRating(0f);
        //setBitmap(((BitmapDrawable) Drawable.createFromPath("/storage/emulated/0/Download/poi_main_image_placeholder.png")).getBitmap());
        setBitmap(MainActivity.BLANK_BITMAP);
        setLongitude(0d);
        setLatitude(0d);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected PlaceOfInterest(Parcel in) {
        id = in.readInt();
        name = in.readString();
        shortDescription = in.readString();
        dateAdded = in.readString();
        rating = in.readFloat();
        category = in.readInt();
        notes = in.readString();
        location = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        //in.readByteArray(bitmap);
    }

    public static final Creator<PlaceOfInterest> CREATOR = new Creator<PlaceOfInterest>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public PlaceOfInterest createFromParcel(Parcel in) {
            return new PlaceOfInterest(in);
        }

        @Override
        public PlaceOfInterest[] newArray(int size) {
            return new PlaceOfInterest[size];
        }
    };

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName(){
        return this.name;
    }
    public void setName(String name) {this.name = name;}

    public String getShortDescription() {
        return this.shortDescription;
    }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getDateAdded() {
        return this.dateAdded;
    }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }

    public Bitmap getBitmapAsBitmap() {
        return BitmapFactory.decodeByteArray(this.bitmap, 0, this.bitmap.length);
    }

    public byte[] getBitmap() {
        return bitmap;
    }
    public void setBitmap(byte[] bitmap){this.bitmap = bitmap;}

    public void setBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        this.bitmap = stream.toByteArray();
    }
    public Float getRating() { return this.rating; }
    public void setRating(Float rating) { this.rating = rating; }

    public int getCategory() { return this.category; }
    public void setCategory(int category) { this.category = category; }

    public String getNotes() { return this.notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getLocation() { return this.location; }
    public void setLocation(String location) { this.location = location;}

    public Double getLongitude() { return this.longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude;}

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(shortDescription);
        parcel.writeString(dateAdded);
        parcel.writeFloat(rating);
        parcel.writeInt(category);
        parcel.writeString(notes);
        parcel.writeString(location);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        //parcel.writeByteArray(bitmap);
    }

    private void setDateAdded(){
        this.dateAdded = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
    }

    public void resetLocation(){
        setLocation(null);
        setLatitude(0d);
        setLongitude(0d);
    }
}
