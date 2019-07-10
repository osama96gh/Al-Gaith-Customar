package com.example.al_gaith_customar;

import android.app.Application;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.al_gaith_customar.Activity.LoginActivity;
import com.example.al_gaith_customar.Activity.MainActivity;
import com.example.al_gaith_customar.Activity.SplachActivity;
import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Utility.GeneralUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import net.gotev.uploadservice.UploadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.example.al_gaith_customar.Utility.GeneralUtility.subsicribe;

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        AppData.userToken = GeneralUtility.readString(AppData.USER_TOKEN_KEY, getApplicationContext());
        AppData.userId = GeneralUtility.readString(AppData.USER_ID_KEY, getApplicationContext());
        AppData.userName = GeneralUtility.readString(AppData.USER_NAME_KEY, getApplicationContext());
        AppData.userPhotoUrl = GeneralUtility.readString(AppData.USER_PHOTO_KEY, getApplicationContext());

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

        /* Enable disk persistence  */
        FirebaseApp.initializeApp(this);


        subsicribe("all");
        getToakenId();

    }

    void subsicribe(final String topic) {

        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to " + topic;
                        if (!task.isSuccessful()) {
                            msg = "Fail subscribe to " + topic;
                        }
                    }
                });

    }

    void getToakenId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.println(Log.ASSERT, "tag", "getInstanceId failed " + task.getException().getMessage());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();


                        SendTokenTask sendTokenTask = new SendTokenTask(token);
                        sendTokenTask.execute();

                        // Log and toast
                        String msg = "message token ft" + " " + token;
                        Log.println(Log.ASSERT, "tag", msg);
                    }
                });


    }


    class SendTokenTask extends AsyncTask<Void, Void, String> {
        String token;

        public SendTokenTask(String token) {
            this.token = token;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return GeneralUtility.sendTokenData(getApplicationContext(), AppData.authType + AppData.userToken, token);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}