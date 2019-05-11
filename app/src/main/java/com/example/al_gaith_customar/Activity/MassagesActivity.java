package com.example.al_gaith_customar.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.al_gaith_customar.Data.Massage;
import com.example.al_gaith_customar.Fragment.MassageFragment;
import com.example.al_gaith_customar.R;

public class MassagesActivity extends AppCompatActivity implements MassageFragment.OnListFragmentInteractionListener {
    FrameLayout massagesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massages);
        setFragment(MassageFragment.newInstance(1));
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
        Toast.makeText(this, "" + item.message, Toast.LENGTH_SHORT).show();
    }
}
