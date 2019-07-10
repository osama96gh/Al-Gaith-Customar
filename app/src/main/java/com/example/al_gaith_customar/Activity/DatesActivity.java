package com.example.al_gaith_customar.Activity;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.ApplicationDate;
import com.example.al_gaith_customar.Fragments.AppLicationDateFragment;
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
