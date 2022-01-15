package com.bimandivlabs.fbla_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SelectActivity extends AppCompatActivity {
    int distanceMiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        SeekBar distanceBar = findViewById(R.id.seekBar);
        TextView distanceText = findViewById(R.id.textView2);
        distanceMiles = distanceBar.getProgress()*20;
        distanceText.setText("Within: " + distanceMiles + " Miles");
        CheckBox bathrooms = findViewById(R.id.bathroomCheckbox);
        CheckBox food = findViewById(R.id.foodCheckbox);
        CheckBox accessible = findViewById(R.id.accessibleCheckbox);
        Spinner type = findViewById(R.id.spinner);
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
                startActivity(intent);
            }
        });
    }
}