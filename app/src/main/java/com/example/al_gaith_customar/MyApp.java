package com.example.al_gaith_customar;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.al_gaith_customar.Activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


import static android.support.constraint.Constraints.TAG;

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseApp.initializeApp(this);


        subsicribe("all");
        getToakenId();
    }
    void subsicribe(final String topic) {

        Log.d(TAG, "Subscribing to weather topic");
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to "+topic;
                        if (!task.isSuccessful()) {
                            msg = "Fail subscribe to "+topic;
                        }
                        Log.println(Log.ASSERT, TAG, msg);
                    }
                });

    }

    void getToakenId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.println(Log.ASSERT, TAG, "getInstanceId failed " + task.getException().getMessage());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "message token ft" + " " + token;
                        Log.println(Log.ASSERT, TAG, msg);
                    }
                });
    }
}