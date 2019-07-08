package com.patanjali.attendencemodule.service;

import android.app.Notification;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.patanjali.attendencemodule.R;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class Utils {

    public static final int NOTIFICATION_ID = 22;
    private static final String CHANNEL_ID = "notify";
    private static final String CHANNEL_NAME = "workmanager-reminder";


    static void sendNotification(Context context) {
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        //   Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("Attendence App Location Starts")
                .setContentText("Your Location Fetching Start Successfully")
                .setSmallIcon(R.drawable.bell_icon)
                .setSound(defaultSoundUri)
                .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(123, notification);
    }
}
