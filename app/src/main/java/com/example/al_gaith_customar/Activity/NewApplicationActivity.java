package com.example.al_gaith_customar.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.R;

public class NewApplicationActivity extends AppCompatActivity {
    int appId = 0;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_application);
        appId = getIntent().getIntExtra(AppData.APPLICATION_ID_KEY, 0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getIntent().getStringExtra(AppData.APPLICATION_TITLE_KEY));
    }
}
