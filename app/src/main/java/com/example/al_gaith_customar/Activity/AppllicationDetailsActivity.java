package com.example.al_gaith_customar.Activity;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.ApplicationData;
import com.example.al_gaith_customar.Data.ApplicationField;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;

import java.util.ArrayList;

public class AppllicationDetailsActivity extends AppCompatActivity {
    LinearLayout rootView;
    int app_id;
    LoadApplicationDetails loadApplicationDetails;
    ArrayList<ApplicationData> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appllication_details);
        rootView = findViewById(R.id.application_ditals_contaner);
        app_id = getIntent().getIntExtra(AppData.APPLICATION_ID_KEY, 0);

        loadApplicationDetails = new LoadApplicationDetails();
        loadApplicationDetails.execute();

    }

    private void addTextField(ApplicationData applicationData) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        TextInputLayout editTextLayout = new TextInputLayout(this);
        editTextLayout.setPadding(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        editTextLayout.setLayoutParams(params);
        editTextLayout.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        rootView.addView(editTextLayout);

        EditText editText = new EditText(this);
        editText.setHint(applicationData.field_name);
        editText.setText("" + applicationData.value);
        editText.setEnabled(false);
        // setEditTextAttributes(editText);
        editTextLayout.addView(editText);

        //addLineSeperator();
    }

    private void addImageField(ApplicationData applicationData) {
        //RadioButtons are always added inside a RadioGroup
        LinearLayout linearLayout = new LinearLayout(AppllicationDetailsActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView titleTV = new TextView(this);
        titleTV.setText(applicationData.field_name);
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_photo));
        imageView.setBackground(getResources().getDrawable(R.drawable.fram_primary));
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
            Toast.makeText(AppllicationDetailsActivity.this, "" + dataList.size(), Toast.LENGTH_SHORT).show();
            Log.println(Log.ASSERT, "app details", s);
            for (ApplicationData applicationData : dataList) {
                if (applicationData.field_type.matches("text") && applicationData.value != null && !applicationData.value.isEmpty()) {
                    addTextField(applicationData);
                } else if (applicationData.field_type.matches("image")) {
                    addImageField(applicationData);
                }
            }
        }
    }
}
