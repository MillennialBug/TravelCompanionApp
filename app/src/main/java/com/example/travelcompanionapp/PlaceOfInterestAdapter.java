package com.example.travelcompanionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlaceOfInterestAdapter extends
        RecyclerView.Adapter<PlaceOfInterestAdapter.PlaceOfInterestHolder>{
    private ArrayList<PlaceOfInterest> mPlaceOfInterestList;
    private LayoutInflater mInflater;
    private RecyclerViewInterface mRecyclerViewInterface;

    public PlaceOfInterestAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        mInflater = LayoutInflater.from(context);
        mRecyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public PlaceOfInterestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.placeofinterest_item, parent, false);
        return new PlaceOfInterestHolder(mItemView, mRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOfInterestHolder holder, int position) {
        if (mPlaceOfInterestList != null) {
            PlaceOfInterest mCurrent = mPlaceOfInterestList.get(position);
            holder.placeOfInterestNameText.setText(mCurrent.getName());
            holder.placeOfInterestDescrText.setText(mCurrent.getShortDescription());
            holder.placeOfInterestDateAddedText.setText(mCurrent.getDateAdded());
            //holder.placeOfInterestMainImage.setImageBitmap(mCurrent.getBitmapAsBitmap());
        }
    }

    void setPlaceOfInterests(List<PlaceOfInterest> placeOfInterests){
        mPlaceOfInterestList = (ArrayList<PlaceOfInterest>) placeOfInterests;
        notifyDataSetChanged();
    }

    PlaceOfInterest getPlaceOfInterestAtPosition(int position){
        return mPlaceOfInterestList.get(position);
    }

    @Override
    public int getItemCount() {
        if (mPlaceOfInterestList != null)
            return mPlaceOfInterestList.size();
        else return 0;
    }

    static class PlaceOfInterestHolder extends RecyclerView.ViewHolder {

        public final TextView placeOfInterestNameText;
        public final TextView placeOfInterestDateAddedText;
        public final TextView placeOfInterestDescrText;
        public final ImageView placeOfInterestMainImage;

        public PlaceOfInterestHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            placeOfInterestNameText = itemView.findViewById(R.id.poi_item_name_text);
            placeOfInterestDateAddedText = itemView.findViewById(R.id.poi_item_date_added_text);
            placeOfInterestDescrText = itemView.findViewById(R.id.poi_item_descr_text);
            placeOfInterestMainImage = itemView.findViewById(R.id.poi_item_main_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (recyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemLongClick(position);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
