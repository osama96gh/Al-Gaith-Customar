package com.example.al_gaith_customar.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.Application;
import com.example.al_gaith_customar.Fragment.ApplicationFragment;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;

public class ApplicationActivity extends AppCompatActivity implements ApplicationFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
//        SendApplication sendApplication = new SendApplication();
//        sendApplication.execute();
        setFragment(ApplicationFragment.newInstance(1));
    }

    protected void setFragment(Fragment fragment) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.application_contaner, fragment);
        t.commit();
    }

    @Override
    public void onListFragmentInteraction(Application item) {
        Toast.makeText(this, "" + item.id, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ApplicationActivity.this, AppllicationDetailsActivity.class);
        intent.putExtra(AppData.APPLICATION_ID_KEY, item.id);
        startActivity(intent);
        // finish();
    }

    public void newApplication(View view) {
        Intent intent = new Intent(ApplicationActivity.this, ApplicationTypeActivity.class);
        startActivity(intent);
        finish();
    }
}
