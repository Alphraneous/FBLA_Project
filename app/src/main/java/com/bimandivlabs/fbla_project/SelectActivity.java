package com.bimandivlabs.fbla_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SelectActivity extends AppCompatActivity {
    int distanceMiles;
    int maxPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Spinner typeSpinner = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.spinner_options,R.layout.spinner_list);
        adapter.setDropDownViewResource(R.layout.spinner_list_dropdown);
        typeSpinner.setAdapter(adapter);
        SeekBar distanceBar = findViewById(R.id.seekBar);
        TextView distanceText = findViewById(R.id.textView2);
        SeekBar priceBar = findViewById(R.id.seekBar2);
        TextView priceText = findViewById(R.id.textView6);
        distanceText.setText("Within: " + distanceMiles + " Miles");
        priceText.setText("Under: $60");
        CheckBox bathrooms = findViewById(R.id.bathroomCheckbox);
        CheckBox food = findViewById(R.id.foodCheckbox);
        CheckBox accessible = findViewById(R.id.accessibleCheckbox);
        Spinner type = findViewById(R.id.spinner);
        distanceText.setText("Within: " + 60 + " Miles");
        distanceMiles = distanceBar.getProgress()*20;
        maxPrice = 60;
                distanceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int pos, boolean b) {
                if(pos == 0) {
                    seekBar.setProgress(1);
                } else if (pos == 10) {
                    distanceMiles = 100000;
                    distanceText.setText(R.string.ndl);
                } else {
                    distanceMiles = (pos)*20;
                    distanceText.setText("Within: " + distanceMiles + " Miles");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });

        priceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int pos, boolean b) {
                if (pos == 0) {
                    maxPrice = 0;
                    priceText.setText("Free");
                } else if (pos == 6) {
                    maxPrice = 1000000000;
                    priceText.setText("No price limit");
                } else if (pos == 1) {
                    maxPrice = 10;{

                    }
                    priceText.setText("Under: $" + maxPrice);
                } else if (pos == 2) {
                    maxPrice = 25;
                    priceText.setText("Under: $" + maxPrice);
                } else if (pos == 3) {
                    maxPrice = 60;
                    priceText.setText("Under: $" + maxPrice);
                } else if (pos == 4) {
                    maxPrice = 100;
                    priceText.setText("Under: $" + maxPrice);
                } else if (pos == 5) {
                    maxPrice = 200;
                    priceText.setText("Under: $" + maxPrice);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button searchButton = findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, ResultsActivity.class);
                intent.putExtra("within",distanceMiles);
                intent.putExtra("bathrooms",bathrooms.isChecked());
                intent.putExtra("food",food.isChecked());
                intent.putExtra("accessible",accessible.isChecked());
                intent.putExtra("type",type.getSelectedItemPosition());
                intent.putExtra("maxPrice",maxPrice);
                startActivity(intent);
            }
        });

        FloatingActionButton bmBtn = findViewById(R.id.bookmarkPage);
        bmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, BookmarkActivity.class);
                startActivity(intent);
            }
        });


    }

}