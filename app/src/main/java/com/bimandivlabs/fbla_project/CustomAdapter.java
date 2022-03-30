package com.bimandivlabs.fbla_project;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

class CustomAdapter implements ListAdapter {
    ArrayList<Attraction2> arrayList;
    Context context;
    public CustomAdapter(Context context, ArrayList<Attraction2> arrayList) {
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
        Attraction2 subjectData = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_row, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(subjectData.Website);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
            TextView name = convertView.findViewById(R.id.resultName);
            TextView distance = convertView.findViewById(R.id.distanceText);
            ImageView image = convertView.findViewById(R.id.resultImage);
            name.setText(subjectData.SubjectName);
            if (distance.getText() == ("1")) {
                distance.setText(subjectData.Distance + " Mile Away");
            } else {
                distance.setText(subjectData.Distance + " Miles Away");
            }

            Picasso.with(context)
                    .load(subjectData.Image)
                    .into(image);
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
