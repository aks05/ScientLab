package com.example.scientlab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            String s= remoteMessage.getData().get("message");
            Intent intent= new Intent("com.example.scientlab_FCM-MESSAGE");
            intent.putExtra("message",s);
            LocalBroadcastManager localBroadcastManager= LocalBroadcastManager.getInstance(this);
            localBroadcastManager.sendBroadcast(intent);


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("Tag", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
