package com.example.al_gaith_customar.Activity;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.Application;
import com.example.al_gaith_customar.Fragments.ApplicationFragment;
import com.example.al_gaith_customar.R;

public class ApplicationActivity extends AppCompatActivity implements ApplicationFragment.OnListFragmentInteractionListener {
    ApplicationFragment applicationFragment =ApplicationFragment.newInstance(1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
//        SendApplication sendApplication = new SendApplication();
//        sendApplication.execute();
        setFragment(applicationFragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        applicationFragment.loadData();
    }

    protected void setFragment(Fragment fragment) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.application_contaner, fragment);
        t.commit();
    }


    @Override
    public void onListFragmentInteraction(Application item) {
        Intent intent = new Intent(ApplicationActivity.this, AppllicationDetailsActivity.class);
        intent.putExtra(AppData.APPLICATION_ID_KEY, item.id);
        startActivity(intent);
        finish();
    }

    public void newApplication(View view) {
        Intent intent = new Intent(ApplicationActivity.this, ApplicationTypeActivity.class);
        startActivity(intent);
       // finish();
    }
}
