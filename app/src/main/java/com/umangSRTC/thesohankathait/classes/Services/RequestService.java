package com.umangSRTC.thesohankathait.classes.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umangSRTC.thesohankathait.classes.Activity.Features;
import com.umangSRTC.thesohankathait.classes.Fragment.Notification;
import com.umangSRTC.thesohankathait.umang.R;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class RequestService extends Service implements ChildEventListener {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference requestDatabaseReferenece;
    public RequestService() {



    }

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseDatabase=FirebaseDatabase.getInstance();
        requestDatabaseReferenece=firebaseDatabase.getReference("Requests");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        requestDatabaseReferenece.addChildEventListener(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  null;


//         TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        notifyAdminAboutRequest(dataSnapshot.getRef());
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }


    private void notifyAdminAboutRequest(DatabaseReference ref) {

        Intent intent=new Intent(getBaseContext(),Features.class);
        intent.putExtra("FRAGMENT_NAME","Request");
        PendingIntent pendingIntent=PendingIntent.getActivity(getBaseContext(),10,intent,0);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext(), getString(R.string.notificationChannelId));

        builder.setAutoCancel(true)
                .setDefaults(android.app.Notification.DEFAULT_ALL)
                .setTicker("sohan")
                .setContentInfo("New Requests")
                .setContentText("you have a new notice Request")
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);


        int randomNotificationId=new Random().nextInt(10000)+1;
        notificationManager.notify(randomNotificationId, builder.build());
    }

}
