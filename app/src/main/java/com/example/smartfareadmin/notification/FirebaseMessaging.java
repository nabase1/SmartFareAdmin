package com.example.smartfareadmin.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.smartfareadmin.AdminActivity;
import com.example.smartfareadmin.BookingRequest;
import com.example.smartfareadmin.R;
import com.example.smartfareadmin.Trips;
import com.example.smartfareadmin.activities.Constants;
import com.example.smartfareadmin.adapters.PendingBookingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        SharedPreferences sp = getSharedPreferences(Constants.SP_USER,MODE_PRIVATE);
        String savedCurrentUser = sp.getString(Constants.USER_ID,"none");

        String sent = remoteMessage.getData().get("sent");
        String user = remoteMessage.getData().get("user");

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(fUser != null ){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                if(user != null){
                    if(user.equals("pending")){
                        sendOAndAboveNotification(remoteMessage, BookingRequest.class);
                    }
                    if(user.equals("completed")){
                        sendOAndAboveNotification(remoteMessage, Trips.class);
                    }
                    if(user.equals("confirm")){
                        sendOAndAboveNotification(remoteMessage, BookingRequest.class);
                    }
                    if(user.equals("startTrip")){
                        sendOAndAboveNotification(remoteMessage, Trips.class);
                    }
                    if(user.equals("completeTrip")){
                        sendOAndAboveNotification(remoteMessage, Trips.class);
                    }
                }
                else {
                    sendOAndAboveNotification(remoteMessage, AdminActivity.class);
                }

            }
            else {
//                if(user != null){
//
//                    if(user.equals("pending")){
//                        sendNormalNotification(remoteMessage, BookingRequest.class);
//                    }
//
//                    if(user.equals("completed")){
//                        sendNormalNotification(remoteMessage, Trips.class);
//                    }
//                    if(user.equals("confirm")){
//                        sendNormalNotification(remoteMessage, BookingRequest.class);
//                    }
//                    if(user.equals("startTrip")){
//                        sendNormalNotification(remoteMessage, Trips.class);
//                    }
//                    if(user.equals("completeTrip")){
//                        sendNormalNotification(remoteMessage, Trips.class);
//                    }
//                }else {
//                    sendNormalNotification(remoteMessage, AdminActivity.class);
//                }

                sendNormalNotification(remoteMessage, AdminActivity.class);


            }
        }

    }

    private void sendNormalNotification(RemoteMessage remoteMessage, Class cls) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");


        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]",""));
        int num = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setColor(ContextCompat.getColor(this, R.color.colorDark))
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setContentIntent(pIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//        int j=0;
//        if(i>0){
//            j=i;
//        }
        notificationManager.notify(num,builder.build());


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOAndAboveNotification(RemoteMessage remoteMessage, Class cls) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]",""));
        int num = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoAndAboveNotification oreoAndAboveNotification = new OreoAndAboveNotification(this);
        Notification.Builder builder = oreoAndAboveNotification.getNotifications(title,body,pIntent,defSoundUri,icon);
        

//        int j=0;
//        if(i>0){
//            j=i;
//        }

        oreoAndAboveNotification.getManager().notify(num,builder.build());
    }
}
