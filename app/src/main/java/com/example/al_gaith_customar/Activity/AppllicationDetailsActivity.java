package com.example.al_gaith_customar.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.ApplicationData;
import com.example.al_gaith_customar.Data.ApplicationState;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;

import java.util.ArrayList;

public class AppllicationDetailsActivity extends AppCompatActivity {
    LinearLayout rootView;
    int app_id;
    TextView stateTV, responseTV, responseAdminTV, reviewDateTV, sendDateTV;

    LoadApplicationDetails loadApplicationDetails;
    ArrayList<ApplicationData> dataList = new ArrayList<>();


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ApplicationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appllication_details);
        rootView = findViewById(R.id.application_ditals_contaner);
        //rootView.setBackground(getResources().getDrawable(R.drawable.fram_primary));

        responseTV = findViewById(R.id.app_detal_response);
        responseAdminTV = findViewById(R.id.app_detal_response_admin);

        reviewDateTV = findViewById(R.id.app_detal_review_date);
        sendDateTV = findViewById(R.id.app_detal_send_date);
        stateTV = findViewById(R.id.app_detal_state);

        app_id = getIntent().getIntExtra(AppData.APPLICATION_ID_KEY, 0);

        loadApplicationDetails = new LoadApplicationDetails();
        loadApplicationDetails.execute();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addTextField(ApplicationData applicationData) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        TextInputLayout editTextLayout = new TextInputLayout(this);
        editTextLayout.setDefaultHintTextColor(getResources().getColorStateList(R.color.colorAccent));
        editTextLayout.setPadding(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        editTextLayout.setLayoutParams(params);
        rootView.addView(editTextLayout);

        EditText editText = new EditText(this);
        editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Typeface type = Typeface.createFromAsset(getAssets(), "tajawal.ttf");
        editText.setTypeface(type);
        editText.setGravity(Gravity.RIGHT);
        editText.setHint(applicationData.field_name);
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
        editText.setText("" + applicationData.value);
        editText.setEnabled(false);
        // setEditTextAttributes(editText);
        editTextLayout.addView(editText);

        //addLineSeperator();
    }

    private void addImageField(ApplicationData applicationData) {
        //RadioButtons are always added inside a RadioGroup
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));

        LinearLayout linearLayout = new LinearLayout(AppllicationDetailsActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(params1);

        TextView titleTV = new TextView(this);
        titleTV.setText(applicationData.field_name);
        Typeface type = Typeface.createFromAsset(getAssets(), "tajawal.ttf");

        titleTV.setTypeface(type);
        titleTV.setTextColor(getResources().getColor(R.color.colorAccent));
        titleTV.setTextSize(12);
        titleTV.setPadding(convertDpToPixel(8), convertDpToPixel(4), convertDpToPixel(8), convertDpToPixel(4));

        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_photo));
        //imageView.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        Log.println(Log.ASSERT, "image uri", applicationData.field_id + " : " + applicationData.value);
        Glide.with(this).load(applicationData.value).into(imageView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                convertDpToPixel(250));
        imageView.setLayoutParams(params);

        linearLayout.addView(titleTV);
        linearLayout.addView(imageView);

        linearLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        rootView.addView(linearLayout);

        //addLineSeperator();
    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    class LoadApplicationDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return GeneralUtility.getApplicationDetailsData(AppllicationDetailsActivity.this, AppData.authType + AppData.userToken, "" + app_id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dataList = GeneralUtility.parseApplicationData(s);
            Log.println(Log.ASSERT, "app details", s);
            for (ApplicationData applicationData : dataList) {
                if ((applicationData.value != null && !applicationData.value.isEmpty())) {

                    if ((applicationData.field_type.matches("enum") || applicationData.field_type.matches("text")||applicationData.field_type.matches("number"))) {
                        addTextField(applicationData);
                    } else if (applicationData.field_type.matches("image")) {
                        addImageField(applicationData);
                    }
                }
            }

            ApplicationState applicationState = GeneralUtility.parseApplicationStateData(s);
            if (applicationState.status != null && !applicationState.status.isEmpty()) {
                stateTV.setText(applicationState.status);
            }
            if (applicationState.response != null && !applicationState.response.isEmpty()) {
                responseTV.setText(applicationState.response);
            }
            if (applicationState.admin_response != null && !applicationState.admin_response.isEmpty()) {
                responseAdminTV.setText(applicationState.admin_response);
            }
            if (applicationState.review_date != null && !applicationState.review_date.isEmpty()) {
                reviewDateTV.setText(applicationState.review_date);
            }
            if (applicationState.created_at != null && !applicationState.created_at.isEmpty()) {
                sendDateTV.setText(applicationState.created_at);
            }

        }
    }
}
