package com.example.travelcompanionapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PlaceOfInterest implements Parcelable {

    private int mainImage;
    private String name;
    private String shortDescription;
    private String dateAdded;
    private Long rating;
    private String category;
    private String notes;
    //private int[] photos;
    //location

    public PlaceOfInterest(String name, String shortDescription) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.mainImage = R.drawable.poi_main_image_placeholder;
        setDateAdded();
    }

    public PlaceOfInterest(String name, String shortDescription, int mainImage) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.mainImage = mainImage;
        setDateAdded();
    }

    public PlaceOfInterest(){
        setDateAdded();
    }

    protected PlaceOfInterest(Parcel in) {
        name = in.readString();
        shortDescription = in.readString();
        dateAdded = in.readString();
        mainImage = in.readInt();
        //rating = in.readLong();
        //category = in.readString();
        //notes = in.readString();
    }

    public static final Creator<PlaceOfInterest> CREATOR = new Creator<PlaceOfInterest>() {
        @Override
        public PlaceOfInterest createFromParcel(Parcel in) {
            return new PlaceOfInterest(in);
        }

        @Override
        public PlaceOfInterest[] newArray(int size) {
            return new PlaceOfInterest[size];
        }
    };

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

    public int getMainImage() { return this.mainImage; }
    public void setMainImage(int mainImage) { this.mainImage = mainImage; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(shortDescription);
        parcel.writeString(dateAdded);
        parcel.writeInt(mainImage);
        //parcel.writeLong(rating);
        //parcel.writeString(category);
        //parcel.writeString(notes);
    }

    private void setDateAdded(){
        this.dateAdded = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
    }
}
