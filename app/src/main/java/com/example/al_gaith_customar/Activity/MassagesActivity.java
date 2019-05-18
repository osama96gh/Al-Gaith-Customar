package com.example.al_gaith_customar.Activity;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.example.al_gaith_customar.Data.Massage;
import com.example.al_gaith_customar.Fragment.MassageFragment;
import com.example.al_gaith_customar.R;
import com.google.gson.Gson;

public class MassagesActivity extends AppCompatActivity implements MassageFragment.OnListFragmentInteractionListener {
    FrameLayout massagesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massages);
        setFragment(MassageFragment.newInstance(1));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void newMassage(View view) {
        Intent intent = new Intent(this, NewMassageActivity.class);
        startActivity(intent);
        finish();
    }

    protected void setFragment(Fragment fragment) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.massages_container, fragment);
        t.commit();
    }

    @Override
    public void onListFragmentInteraction(Massage item) {
        Intent intent = new Intent(this, MassageDetailsActivity.class);
        Gson gson = new Gson();

        String massage = gson.toJson(item);
        intent.putExtra("massage", massage);
        startActivity(intent);
        finish();
    }


}
