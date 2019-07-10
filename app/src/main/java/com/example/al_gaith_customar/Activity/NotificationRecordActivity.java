package com.example.al_gaith_customar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.al_gaith_customar.Data.Notification;
import com.example.al_gaith_customar.Fragments.NotificationFragment;
import com.example.al_gaith_customar.R;

public class NotificationRecordActivity extends AppCompatActivity implements NotificationFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_record);
        setFragment(NotificationFragment.newInstance(1));

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
        t.replace(R.id.notification_container, fragment);
        t.commit();
    }

    @Override
    public void onListFragmentInteraction(Notification item) {

    }
}
