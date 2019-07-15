package com.example.al_gaith_customar.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;

import org.json.JSONArray;
import org.json.JSONException;

import static com.example.al_gaith_customar.Utility.GeneralUtility.subsicribe;

public class SplachActivity extends AppCompatActivity {
    AsyncTask startApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalch);

        startApp = new startApp().execute();
    }


    class startApp extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            //super.onPostExecute(o);
            AppData.userToken = GeneralUtility.readString(AppData.USER_TOKEN_KEY, SplachActivity.this);
            AppData.userId = GeneralUtility.readString(AppData.USER_ID_KEY, SplachActivity.this);
            AppData.userName = GeneralUtility.readString(AppData.USER_NAME_KEY, SplachActivity.this);
            AppData.userPhotoUrl = GeneralUtility.readString(AppData.USER_PHOTO_KEY, SplachActivity.this);
            JSONArray jsonArray = null;
            try {
                String groupsJsonArray = GeneralUtility.readString(AppData.USER_GROUP_JSON_ARRAY_KEY, SplachActivity.this);
                if (groupsJsonArray != null)
                    jsonArray = new JSONArray(groupsJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonArray != null)
                for (int i = 0, count = jsonArray.length(); i < count; i++) {
                    try {
                        subsicribe(jsonArray.getString(i));
                        Log.println(Log.ASSERT, "group" + i, jsonArray.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            subsicribe("all");

            Intent intent;
            if (AppData.userToken != null && !AppData.userToken.isEmpty()) {
                intent = new Intent(SplachActivity.this, MainActivity.class);

            } else {
                intent = new Intent(SplachActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }
}
