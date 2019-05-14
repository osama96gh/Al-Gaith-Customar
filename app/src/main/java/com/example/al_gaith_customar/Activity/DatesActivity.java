package com.example.al_gaith_customar.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.ApplicationDate;
import com.example.al_gaith_customar.Fragment.AppLicationDateFragment;
import com.example.al_gaith_customar.R;

public class DatesActivity extends AppCompatActivity implements AppLicationDateFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates);
        setFragment(AppLicationDateFragment.newInstance(1));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void setFragment(Fragment fragment) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.dates_container, fragment);
        t.commit();
    }

    @Override
    public void onListFragmentInteraction(ApplicationDate item) {
        Intent intent = new Intent(DatesActivity.this, AppllicationDetailsActivity.class);
        intent.putExtra(AppData.APPLICATION_ID_KEY, item.id);
        startActivity(intent);
        finish();
    }
}
