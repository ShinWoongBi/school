package com.warbargic.school.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.warbargic.school.MainActivity;
import com.warbargic.school.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by kippe on 2017-04-28.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //추가한것
        sendNotification(remoteMessage.getData().get("message"));
    }

    private void sendNotification(String messageBody) {
        Log.e("friebase message",":"+messageBody);


        String title = null;
        String message = null;
        String time = null;


        try {
            JSONArray jsonArray = new JSONArray(messageBody);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            title = jsonObject.getString("title");
            message = jsonObject.getString("message");
            time = jsonObject.getString("time");
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("json_result", "title:"+title+"  message:"+message+"  time:"+time);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);



        NotificationManager Notifi_M;
        Notification Notifi ;
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Notifi = new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("알림!!!")
                .setContentIntent(pendingIntent)
                .setSubText(time)
                .build();

        //소리추가
        Notifi.defaults = Notification.DEFAULT_SOUND;

        //알림 소리를 한번만 내도록
        Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

        //확인하면 자동으로 알림이 제거 되도록
        Notifi.flags = Notification.FLAG_AUTO_CANCEL;

        Notifi_M.notify( 777 , Notifi);



    }

}
