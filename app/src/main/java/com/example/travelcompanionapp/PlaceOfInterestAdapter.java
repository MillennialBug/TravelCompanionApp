package com.example.travelcompanionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class PlaceOfInterestAdapter extends
        RecyclerView.Adapter<PlaceOfInterestAdapter.PlaceOfInterestHolder>{
    private final LinkedList<String> mPlaceOfInterestList;
    private LayoutInflater mInflater;

    public PlaceOfInterestAdapter(Context context, LinkedList<String> placeOfInterestList) {
        mInflater = LayoutInflater.from(context);
        this.mPlaceOfInterestList = placeOfInterestList;
    }

    @NonNull
    @Override
    public PlaceOfInterestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.placeofinterest_item, parent, false);
        return new PlaceOfInterestHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOfInterestHolder holder, int position) {
        String mCurrent = mPlaceOfInterestList.get(position);
        holder.placeOfInterestItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mPlaceOfInterestList.size();
    }

    class PlaceOfInterestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView placeOfInterestItemView;
        final PlaceOfInterestAdapter mAdapter;

        public PlaceOfInterestHolder(@NonNull View itemView, PlaceOfInterestAdapter adapter) {
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
            String element = mPlaceOfInterestList.get(mPosition);
            // Change the word in the mWordList.
            mPlaceOfInterestList.set(mPosition, "Clicked! " + element);
            // Notify the adapter that the data has changed so it can
            // update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }
    }
}
