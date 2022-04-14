package com.bimandivlabs.fbla_project;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

class CustomAdapter implements ListAdapter{
    ArrayList<Attraction> arrayList;
    Context context;
    public CustomAdapter(Context context, ArrayList<Attraction> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Attraction subjectData = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_row, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MoreActivity.class);
                    intent.putExtra("data", (Serializable) subjectData);
                    context.startActivity(intent);
                }
            });
            TextView name = convertView.findViewById(R.id.resultName);
            TextView distance = convertView.findViewById(R.id.distanceText);
            ImageView image = convertView.findViewById(R.id.resultImage);
            name.setText(subjectData.Name);
            if (subjectData.Distance.equals("1")) {
                distance.setText(subjectData.Distance + " Mile Away");
            } else {
                distance.setText(subjectData.Distance + " Miles Away");
            }

            Picasso.with(context)
                    .load(subjectData.Image)
                    .into(image);

            Integer rating = subjectData.Rating;
            ImageView ratingView = convertView.findViewById(R.id.ratingImage);
            if (rating == 0) {
                ratingView.setImageResource(R.drawable.star_rating_0_of_5);
            } else if (rating == 1) {
                ratingView.setImageResource(R.drawable.star_rating_1_of_5);
            } else if (rating == 2) {
                ratingView.setImageResource(R.drawable.star_rating_2_of_5);
            } else if (rating == 3) {
                ratingView.setImageResource(R.drawable.star_rating_3_of_5);
            } else if (rating == 4) {
                ratingView.setImageResource(R.drawable.star_rating_4_of_5);
            } else {
                ratingView.setImageResource(R.drawable.star_rating_5_of_5);
            }
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
}
