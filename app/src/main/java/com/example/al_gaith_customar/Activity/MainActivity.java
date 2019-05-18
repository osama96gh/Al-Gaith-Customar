package com.example.al_gaith_customar.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.widget.ImageViewCompat;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.al_gaith_customar.Data.Ads;
import com.example.al_gaith_customar.Data.Announcement;
import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Fragment.AnnouncementFragment;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.al_gaith_customar.Data.AppData.testMode;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AnnouncementFragment.OnListFragmentInteractionListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    Boolean isLogIn = false;

    ImageView userPhoto;
    TextView userId, userName;
    ArrayList<Ads> adsList = new ArrayList<>();
    private SliderLayout mDemoSlider;
    MenuItem massageItem, applicationItem, datesItem;
    LoadAppAndMassStat loadAppAndMassStat;

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
        }
        initilizeUserInfo();
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();

        // find MenuItem  want to change
        massageItem = menu.findItem(R.id.nav_massages);
        applicationItem = menu.findItem(R.id.nav_requests);
        datesItem = menu.findItem(R.id.nav_dates);
        setAnnouncmentFragment(AnnouncementFragment.newInstance(1));

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);


        loadAppAndMassStat = new LoadAppAndMassStat();
        LoadMyGroup loadMyGroup = new LoadMyGroup();
        loadMyGroup.execute();

        LoadMyApplication loadMyApplication = new LoadMyApplication();
        loadMyApplication.execute();

    }

    void setAdsSlider(HashMap<String, String> url_maps) {
        mDemoSlider.removeAllSliders();
        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()

                    .putString("extra", name);
            textSliderView.setScaleType(BaseSliderView.ScaleType.CenterInside);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.FlipHorizontal);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);


    }

    protected void setAnnouncmentFragment(Fragment fragment) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.annoument_container, fragment);
        t.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        loadAppAndMassStat.cancel(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadAppAndMassStat = new LoadAppAndMassStat();
        loadAppAndMassStat.execute();
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
            if(testMode)
                finish();
        } else if (id == R.id.nav_requests) {

            Intent intent = new Intent(MainActivity.this, ApplicationActivity.class);
            startActivity(intent);
            if(testMode)
               finish();

        } else if (id == R.id.nav_dates) {

            Intent intent = new Intent(MainActivity.this, DatesActivity.class);
            startActivity(intent);
            if(testMode)
                finish();

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


    @Override
    public void onListFragmentInteraction(Announcement item) {

    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class LoadAppAndMassStat extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return GeneralUtility.getAppAndMassageData(MainActivity.this, AppData.authType + AppData.userToken);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int massage = 0, application = 0, dates = 0;
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);


                jsonObject = jsonObject.getJSONObject("success");

                massage = jsonObject.getInt("messages");
                application = jsonObject.getInt("applications");
                dates = jsonObject.getInt("dates");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (massage != -1) {
                massageItem.setTitle("الرسائل " + "(" + massage + ")");
            }
            if (application != -1) {
                applicationItem.setTitle("الطلبات " + "(" + application + ")");
            }
            if (dates != -1) {
                datesItem.setTitle("المواعيد " + "(" + dates + ")");
            }
        }
    }

    class LoadMyGroup extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return GeneralUtility.getMyGroupData(MainActivity.this, AppData.authType + AppData.userToken);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<String> groups = new ArrayList<>();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);


                jsonObject = jsonObject.getJSONObject("success");

                JSONArray jsonArray = jsonObject.getJSONArray("groups");

                for (int i = 0; i < jsonArray.length(); i++) {
                    groups.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (String group : groups) {
                Log.println(Log.ASSERT, "main group", group);
                GeneralUtility.subsicribe(group);
            }
        }
    }

    class LoadMyApplication extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            return GeneralUtility.getMyAdsData(MainActivity.this, AppData.authType + AppData.userToken);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            HashMap<String, String> url_maps = new HashMap<String, String>();

            for (Ads announcement : GeneralUtility.parseAds(s)) {
                adsList.add(announcement);
                url_maps.put(announcement.announce, announcement.image);

            }
            setAdsSlider(url_maps);
        }
    }

}
