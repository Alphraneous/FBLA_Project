package com.bimandivlabs.fbla_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        //Show the go back arrow
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Search Results");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Get the data passed from customAdapter
        Attraction data = (Attraction) getIntent().getSerializableExtra("data");

        //Get the review if there is one and hide/show elements accordingly
        SharedPreferences sp = getSharedPreferences("reviews",MODE_PRIVATE);
        String review = sp.getString(Integer.toString(data.ID),"");
        Boolean bookmarked = sp.getBoolean((data.ID)+"bm",false);
        CheckBox bookmark = findViewById(R.id.bookmark);
        bookmark.setChecked(bookmarked);
        LinearLayout submitLayout = findViewById(R.id.submitReviewLayout);
        LinearLayout editLayout = findViewById(R.id.editReviewLayout);
        if(review.equals("")) {
           submitLayout.setVisibility(View.VISIBLE);
           editLayout.setVisibility(View.GONE);
        } else {
            submitLayout.setVisibility(View.GONE);
            editLayout.setVisibility(View.VISIBLE);
            TextView currentReviewText = findViewById(R.id.currentReviewText);
            currentReviewText.setText(review);
        }

        //Sets the name
        String name = data.Name;
        TextView nameText = findViewById(R.id.nameText);
        nameText.setText(name);

        //Sets the image
        ImageView image = findViewById(R.id.imageView);
        Picasso.with(this)
                .load(data.Image)
                .into(image);

        //Set the text for distance
        TextView distance = findViewById(R.id.distanceView);
        if (data.Distance.equals("1")) {
            distance.setText(data.Distance + " Mile Away");
        } else {
            distance.setText(data.Distance + " Miles Away");
        }

        //Sets the text for price
        Integer price = data.Price;
        TextView priceText = findViewById(R.id.priceView);
        if (price == 0) {
            priceText.setText("Free");
        }else if (price < 10) {
            priceText.setText("$");
        } else if (price < 25) {
            priceText.setText("$$");
        } else if (price < 60) {
            priceText.setText("$$$");
        } else if (price < 100) {
            priceText.setText("$$$$");
        } else if (price < 200) {
            priceText.setText("$$$$$");
        } else {
            priceText.setText("$$$$$$");
        }

        //Sets the text for address
        TextView addressView = findViewById(R.id.addressView);
        addressView.setText(data.Address);
        //Sets the image for rating
        Integer rating = data.Rating;
        ImageView ratingView = findViewById(R.id.ratingView);
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

        //Process input for saving/editing reviews
        Button reviewButton = findViewById(R.id.reviewButton);
        EditText reviewInput = findViewById(R.id.reviewInput);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Integer.toString(data.ID), String.valueOf(reviewInput.getText()));
                editor.commit();
                Toast toast=Toast.makeText(getApplicationContext(),"Review saved",Toast.LENGTH_SHORT);
                toast.show();
                TextView currentReviewText = findViewById(R.id.currentReviewText);
                currentReviewText.setText(reviewInput.getText());
                if (!reviewInput.getText().equals("")) {
                    submitLayout.setVisibility(View.GONE);
                    editLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        Button editReviewButton = findViewById(R.id.editReviewButton);
        editReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reviewNew = sp.getString(Integer.toString(data.ID),"");
                reviewInput.setText(reviewNew);
                submitLayout.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
            }
        });

        //Sets the onClickListener for the website button
        String website = data.Website;
        Button webButton = findViewById(R.id.webButton);
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(website);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        String address = data.Address;
        Button routeButton = findViewById(R.id.routeButton);
        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:"+data.Lat+","+data.Long+"?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean((data.ID)+"bm", bookmark.isChecked());
                editor.apply();
                try {
                    JSONArray bookmarkList = new JSONArray(sp.getString("bookmarkList", "[]"));
                    List<Integer> dataList = new ArrayList<Integer>();
                    for(int i=0;i<bookmarkList.length();i++){
                        dataList.add(bookmarkList.getInt(i));
                    }

                    if (!bookmark.isChecked()) {
                        bookmarkList.remove(dataList.indexOf(data.ID));
                        SharedPreferences.Editor editor2 = sp.edit();
                        editor2.putString("bookmarkList", bookmarkList.toString());
                        editor2.apply();

                    } else {
                        bookmarkList.put(data.ID);
                        SharedPreferences.Editor editor2 = sp.edit();
                        editor2.putString("bookmarkList", bookmarkList.toString());
                        editor2.apply();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getResources().openRawResource(R.raw.attractions);

            int size = is.available();

            byte[] buffer = new byte[size];

            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);

            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}