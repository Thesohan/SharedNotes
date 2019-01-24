package com.umangSRTC.thesohankathait.classes.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.umangSRTC.thesohankathait.classes.Activity.Login;
import com.umangSRTC.thesohankathait.classes.Utill.Admin;
import com.umangSRTC.thesohankathait.classes.Utill.Initialisation;
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


        FirebaseDatabase.getInstance().getReference(Initialisation.selectedCollege+"/Tokens").push().setValue(token);
        Log.d("tokenupdated",token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        /* There are two types of messages data messages and notification messages. Data messages are handled here in onMessageReceived whether the app is in the foreground or background. Data messages are the type traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app is in the foreground. When the app is in the background an automatically generated notification is displayed. */
        String notificationTitle = null, notificationBody = null;
      /*  String dataTitle = null, dataMessage = null;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
           Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("message"));
            dataTitle = remoteMessage.getData().get("title");
            dataMessage = remoteMessage.getData().get("message");
        }
        */

//      since we only need notification payload
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendNotification(notificationTitle, notificationBody);//, dataTitle, dataMessage);
    }

    /**
     //     * Create and show a simple notification containing the received FCM message.
     //     */
    private void sendNotification(String notificationTitle, String notificationBody){//, String dataTitle, String dataMessage) {
        Intent intent = new Intent(this, Login.class);
//        intent.putExtra("title", dataTitle);
//        intent.putExtra("message", dataMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(this,getString(R.string.notificationChannelId))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationBody))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int randomNotificationId=new Random().nextInt(100)+1;
        notificationManager.notify(randomNotificationId, notification);



//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /*
    private void showNotificationImage(RemoteMessage remoteMessage) {


        Intent notificationIntent = new Intent(getApplicationContext(),Login.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);

        //building a notification with the remote message
        Notification notification = new NotificationCompat.Builder(this,getString(R.string.notificationChannelId))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(intent)
                .setAutoCancel(true)


                .build();
    }
    */


}
