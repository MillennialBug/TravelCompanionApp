package com.example.travelcompanionapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

class PlaceOfInterestImageAdapter extends
        RecyclerView.Adapter<PlaceOfInterestImageAdapter.PlaceOfInterestImageHolder>{
    private final LinkedList<Integer> mPlaceOfInterestList;
    private LayoutInflater mInflater;

    public PlaceOfInterestImageAdapter(Context context, LinkedList<Integer> placeOfInterestList) {
        mInflater = LayoutInflater.from(context);
        this.mPlaceOfInterestList = placeOfInterestList;
    }

    @NonNull
    @Override
    public PlaceOfInterestImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.poi_image_item, parent, false);
        return new PlaceOfInterestImageHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOfInterestImageHolder holder, int position) {
        int mCurrent = mPlaceOfInterestList.get(position);
        holder.placeOfInterestItemView.setImageResource(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mPlaceOfInterestList.size();
    }

    class PlaceOfInterestImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView placeOfInterestItemView;
        final PlaceOfInterestImageAdapter mAdapter;

        public PlaceOfInterestImageHolder(@NonNull View itemView, PlaceOfInterestImageAdapter adapter) {
            super(itemView);
            placeOfInterestItemView = itemView.findViewById(R.id.poi_name_text);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mWordList.
            //String element = mPlaceOfInterestList.get(mPosition);
            // Change the word in the mWordList.
            //mPlaceOfInterestList.set(mPosition, "Clicked! " + element);
            // Notify the adapter that the data has changed so it can
            // update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }
    }
}
