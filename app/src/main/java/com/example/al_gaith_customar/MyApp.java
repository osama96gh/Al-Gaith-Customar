package com.example.al_gaith_customar;

import android.app.Application;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import net.gotev.uploadservice.UploadService;

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
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
                        String msg = "Subscribed to "+topic;
                        if (!task.isSuccessful()) {
                            msg = "Fail subscribe to "+topic;
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

                        // Log and toast
                        String msg = "message token ft" + " " + token;
                        Log.println(Log.ASSERT, "tag", msg);
                    }
                });
    }
}