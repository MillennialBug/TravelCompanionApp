package com.example.travelcompanionapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class PlaceOfInterestImageAdapter extends
        RecyclerView.Adapter<PlaceOfInterestImageAdapter.PlaceOfInterestImageHolder>{
    private ArrayList<PlaceOfInterestGallery> mGallery;
    private LayoutInflater mInflater;
    private final RecyclerViewInterface mRecyclerViewInterface;

    public PlaceOfInterestImageAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        mInflater = LayoutInflater.from(context);
        mRecyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public PlaceOfInterestImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.poi_image_item, parent, false);
        return new PlaceOfInterestImageHolder(mItemView, mRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOfInterestImageHolder holder, int position) {
        if (mGallery != null) {
            PlaceOfInterestGallery mCurrent = mGallery.get(position);
            holder.placeOfInterestItemView.setImageBitmap(BitmapFactory.decodeByteArray(mCurrent.getImage(), 0, mCurrent.getImage().length));
        }
    }

    void setImages(List<PlaceOfInterestGallery> gallery){
        mGallery = (ArrayList<PlaceOfInterestGallery>) gallery;
        notifyDataSetChanged();
    }

    PlaceOfInterestGallery getImageAtPosition(int position){
        return mGallery.get(position);
    }

    @Override
    public int getItemCount() {
        if (mGallery != null)
            return mGallery.size();
        else return 0;
    }

    class PlaceOfInterestImageHolder extends RecyclerView.ViewHolder {

        public final ImageView placeOfInterestItemView;

        public PlaceOfInterestImageHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            placeOfInterestItemView = itemView.findViewById(R.id.poi_image_view_item);

            itemView.setOnClickListener(view -> {
                if (recyclerViewInterface != null){
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(position);
                    }
                }
            });
            itemView.setOnLongClickListener(view -> {
                if (recyclerViewInterface != null){
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemLongClick(position);
                    }
                }
                return true;
            });
        }
    }
}
