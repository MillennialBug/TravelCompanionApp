package com.example.travelcompanionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

class PlaceOfInterestImageAdapter extends
        RecyclerView.Adapter<PlaceOfInterestImageAdapter.PlaceOfInterestImageHolder>{
    private final LinkedList<Integer> mPlaceOfInterestImageList;
    private LayoutInflater mInflater;

    public PlaceOfInterestImageAdapter(Context context, LinkedList<Integer> placeOfInterestList) {
        mInflater = LayoutInflater.from(context);
        this.mPlaceOfInterestImageList = placeOfInterestList;
    }

    @NonNull
    @Override
    public PlaceOfInterestImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.poi_image_item, parent, false);
        return new PlaceOfInterestImageHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOfInterestImageHolder holder, int position) {
        int mCurrent = mPlaceOfInterestImageList.get(position);
        holder.placeOfInterestItemView.setImageResource(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mPlaceOfInterestImageList.size();
    }

    class PlaceOfInterestImageHolder extends RecyclerView.ViewHolder {

        public final ImageView placeOfInterestItemView;
        final PlaceOfInterestImageAdapter mAdapter;

        public PlaceOfInterestImageHolder(@NonNull View itemView, PlaceOfInterestImageAdapter adapter) {
            super(itemView);
            placeOfInterestItemView = itemView.findViewById(R.id.poi_image_view_item);
            this.mAdapter = adapter;
        }
    }
}
