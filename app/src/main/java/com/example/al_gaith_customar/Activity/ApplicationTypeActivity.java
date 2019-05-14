package com.example.al_gaith_customar.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.ApplicationType;
import com.example.al_gaith_customar.Fragment.ApplicationTypeFragment;
import com.example.al_gaith_customar.R;

public class ApplicationTypeActivity extends AppCompatActivity implements ApplicationTypeFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_type);
        setFragment(ApplicationTypeFragment.newInstance(2));

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
        t.replace(R.id.application_type_container, fragment);
        t.commit();
    }

    @Override
    public void onListFragmentInteraction(ApplicationType item) {
        Intent intent = new Intent(ApplicationTypeActivity.this, NewApplicationActivity.class);
        intent.putExtra(AppData.APPLICATION_ID_KEY, item.id);
        intent.putExtra(AppData.APPLICATION_TITLE_KEY, item.name);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ApplicationActivity.class);
        startActivity(intent);
        finish();
    }
}
