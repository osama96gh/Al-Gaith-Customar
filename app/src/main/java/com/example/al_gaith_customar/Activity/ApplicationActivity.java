package com.example.al_gaith_customar.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
    }

    public void newApplication(View view) {
        Intent intent = new Intent(ApplicationActivity.this, ApplicationTypeActivity.class);
        startActivity(intent);
     }

    class SendApplication extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String resppon = GeneralUtility.sendApplication(ApplicationActivity.this, AppData.authType + AppData.userToken);
            return resppon;
        }
    }
}
