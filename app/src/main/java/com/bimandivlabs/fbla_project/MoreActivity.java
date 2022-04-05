package com.bimandivlabs.fbla_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Attraction defaultAttraction = new Attraction("null","null","null","null");
        Attraction data = (Attraction) getIntent().getSerializableExtra("data");
        ImageView image = findViewById(R.id.imageView);
        Picasso.with(this)
                .load(data.Image)
                .into(image);
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
        String name = data.Name;
        TextView nameText = findViewById(R.id.nameText);
        nameText.setText(name);

    }
}