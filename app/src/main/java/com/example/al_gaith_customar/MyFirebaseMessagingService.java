package com.example.al_gaith_customar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;

import com.example.al_gaith_customar.Activity.MainActivity;
import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Utility.GeneralUtility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.println(Log.ASSERT, "firebase masage", "Message Notification Body: ");
        String notificationTitle = "no title", notificationBody = "no body";

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.println(Log.ASSERT, "firebase masage", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
        } else if (remoteMessage.getData() != null) {
            Log.println(Log.ASSERT, "firebase masage", "Message data Body: " + remoteMessage.getData().get("body"));
            Map<String, String> data = remoteMessage.getData();
            notificationTitle = data.get("title");
            notificationBody = data.get("body");
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendNotification(notificationTitle, notificationBody);

        LoadAppAndMassStat loadAppAndMassStat = new LoadAppAndMassStat();
        loadAppAndMassStat.execute();

        LoadMyGroup loadMyGroup = new LoadMyGroup();
        loadMyGroup.execute();
    }


    private void sendNotification(String notificationTitle, String notificationBody) {
        createNotificationChannel();
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        final int min = 0;
        final int max = 1000;
        final int random = new Random().nextInt((max - min) + 1) + min;
        notificationManager.notify(random, builder.build());

    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "new data";
            String description = "get notification for new data";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    class LoadAppAndMassStat extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return GeneralUtility.getAppAndMassageData(MyFirebaseMessagingService.this, AppData.authType + AppData.userToken);

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
            int badgeCount = massage + application + dates;
            ShortcutBadger.applyCount(getApplicationContext(), badgeCount); //for 1.1.4+
            Log.println(Log.ASSERT, "tag", "massage resived ... num: " + badgeCount);

        }
    }

    class LoadMyGroup extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return GeneralUtility.getMyGroupData(getApplicationContext(), AppData.authType + AppData.userToken);

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
}
