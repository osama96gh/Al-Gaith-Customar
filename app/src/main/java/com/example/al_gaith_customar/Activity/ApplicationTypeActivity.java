package com.example.al_gaith_customar.Activity;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.ApplicationType;
import com.example.al_gaith_customar.Fragments.ApplicationTypeFragment;
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
        Log.println(Log.ASSERT,"id",""+item.id);
        intent.putExtra(AppData.APPLICATION_TITLE_KEY, item.name);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ApplicationActivity.class);
      //  startActivity(intent);
        finish();
    }
}
