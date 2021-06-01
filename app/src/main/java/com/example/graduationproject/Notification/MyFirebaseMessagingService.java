package com.example.graduationproject.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.graduationproject.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String title,message;
  String CHANNAL_ID="good";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(message);
       /* NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
*/

     if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        NotificationChannel notificationChannel = new NotificationChannel(CHANNAL_ID,"notification",NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
        manager.notify(0,builder.build());
            Log.i("Gouda", "onMessageReceived: ");
    }
   }

}
