package com.umangSRTC.thesohankathait.classes.Firebase;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.umangSRTC.thesohankathait.umang.R;

import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    //On initial startup of your app, the
    // FCM SDK generates a registration token for
    // the client app instance.
    // If you want to target single devices or create device groups,
    // you'll need to access this token by extending
    // FirebaseMessagingService and overriding onNewToken.


    //The registration token may change when:
    //
    //The app deletes Instance ID
    //The app is restored on a new device
    //The user uninstalls/reinstall the app
    //The user clears app data.

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        //Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {


        Log.d("tokenupdated",token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData()!=null){
            showNotificationImage(remoteMessage);
        }
    }

    private void showNotificationImage(RemoteMessage remoteMessage) {

        //building a notification with the remote message
        Notification notification = new NotificationCompat.Builder(this,getString(R.string.notificationChannelId))
                .setSmallIcon(R.drawable.notificatioin_icon)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();

        //displaying notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        int randomNotificationId=new Random().nextInt(100)+1;
        notificationManager.notify(randomNotificationId, notification);

    }


}
