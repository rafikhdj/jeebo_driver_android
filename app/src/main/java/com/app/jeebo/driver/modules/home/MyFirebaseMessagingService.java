package com.app.jeebo.driver.modules.home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.app.jeebo.driver.R;
import com.app.jeebo.driver.modules.home.activity.HomeActivity;
import com.app.jeebo.driver.utils.AppConstant;
import com.app.jeebo.driver.utils.MyIntentService;
import com.app.jeebo.driver.utils.PreferenceKeeper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        PreferenceKeeper.getInstance().setDeviceToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage.getNotification().getBody());
        Intent localIntent = new Intent(MyIntentService.CUSTOM_ACTION);
        localIntent.putExtra(AppConstant.INTENT_EXTRAS.NOTIFICATION_TYPE,remoteMessage.getNotification().getBody());
        this.sendBroadcast(localIntent);
    }

    private void sendNotification(String msg){
        Intent intent=new Intent(this, HomeActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        notificationBuilder.setContentTitle(getString(R.string.app_name))
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setSmallIcon(R.mipmap.jeebo_icon);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.jeebo_icon);
        }

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.canShowBadge();
            mChannel.setShowBadge(true);
            notificationBuilder.setChannelId(CHANNEL_ID);
            notificationBuilder.setNumber(1);
            notificationBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
            mNotificationManager.createNotificationChannel(mChannel);
        }else{
            /*if(count != null)
                ShortcutBadger.applyCount(this, Integer.parseInt(count));*/

        }

        Notification notification = notificationBuilder.build();

        mNotificationManager.notify(1,notification);

    }
}