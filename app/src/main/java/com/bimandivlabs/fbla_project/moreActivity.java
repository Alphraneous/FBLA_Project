package com.bimandivlabs.fbla_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;

import org.w3c.dom.Attr;

public class moreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Attraction2 defaultAttraction = new Attraction2("null","null","null","null");
        Attraction2 data = (Attraction2) getIntent().getSerializableExtra("data");
    }
}