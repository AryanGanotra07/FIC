package com.aryanganotra.ficsrcc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyMessagingService extends FirebaseMessagingService {


    String name="";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);




        shownotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

    }


    public void shownotification(String title,String message){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){







            NotificationChannel channel=new NotificationChannel("MyNotifications","MyNotifications",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=this.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);


        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"MyNotifications")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.notilogo).setAutoCancel(true).setContentText(message).setTicker("Learn More").setSmallIcon(R.drawable.notilogo).setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.logo));

        NotificationManagerCompat manager=NotificationManagerCompat.from(this);
        manager.notify(999,builder.build());





    }
}
