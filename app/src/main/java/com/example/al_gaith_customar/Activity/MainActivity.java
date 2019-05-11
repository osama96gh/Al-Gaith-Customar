package com.example.al_gaith_customar.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ImageViewCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Boolean isLogIn = false;

    ImageView userPhoto;
    TextView userId, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hedar = navigationView.getHeaderView(0);
        userId = hedar.findViewById(R.id.userInfo_username);
        userName = hedar.findViewById(R.id.userInfo_name);
        userPhoto = hedar.findViewById(R.id.userInfo_photo);
        if (userName == null) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
        initilizeUserInfo();
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initilizeUserInfo() {
        userId.setText(AppData.userId);
        userName.setText(AppData.userName);
        Log.println(Log.ASSERT, "photo", AppData.userPhotoUrl);
        if (!AppData.userPhotoUrl.matches("null")) {
            ImageViewCompat.setImageTintList(userPhoto, null);
            Glide.with(this).load(AppData.userPhotoUrl).into(userPhoto);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_massages) {
            Intent intent = new Intent(this, MassagesActivity.class);
            startActivity(intent);
           // finish();
        } else if (id == R.id.nav_requests) {

            Toast.makeText(this, "الطلبات", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ApplicationActivity.class);
            startActivity(intent);
         //   finish();

        } else if (id == R.id.nav_log_out) {

            GeneralUtility.deleteValue(AppData.USER_TOKEN_KEY, MainActivity.this);
            AppData.userToken = null;
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
