package com.app.jeebo.driver.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.app.jeebo.driver.R;
import com.app.jeebo.driver.modules.home.activity.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Class is used contain utility methods..
 */
public final class AppUtils {
    private static final String TAG = AppUtils.class.getName();
    public static final String DIR_NAME = "Xefyr";


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        boolean isNetwork = (activeNetworkInfo != null && activeNetworkInfo.isConnected());
        if (!isNetwork) {
            // context.showToast(context.getResources().getString(R.string.network_not_available));
        }
        return isNetwork;
    }

    public static void getKey(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
    }

    public static Typeface findTypeface(Context context, String initPath,
                                        String typeface) {
        AssetManager assets = context.getAssets();
        return Typeface.createFromAsset(assets, (initPath + File.separator)
                + typeface);
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = android.util.Base64.decode(strEncoded.getBytes(), android.util.Base64.URL_SAFE);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }



    /**
     * Returns int[] {scaledWidth, scaledHeight} for dimensions that fit within the given maxWidth,
     * maxHeight at the given inWidth, inHeight aspect ratio.  If the in dimensions fit fully inside
     * the max dimensions, no scaling is applied.  Otherwise, at least one scaled dimension is set
     * to a max dimension, and the other scaled dimension is scaled to fit.
     *
     * @param inWidth
     * @param inHeight
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static int[] scaleDownInside(int inWidth, int inHeight, int maxWidth, int maxHeight) {
        int scaledWidth;
        int scaledHeight;
        if (inWidth <= maxWidth && inHeight <= maxHeight) {
            scaledWidth = inWidth;
            scaledHeight = inHeight;
        } else {
            double widthRatio = (double) inWidth / (double) maxWidth;
            double heightRatio = (double) inHeight / (double) maxHeight;
            if (widthRatio > heightRatio) {
                scaledWidth = maxWidth;
                scaledHeight = (int) Math.round((double) inHeight / widthRatio);
            } else {
                scaledHeight = maxHeight;
                scaledWidth = (int) Math.round((double) inWidth / heightRatio);
            }
        }
        return new int[]{scaledWidth, scaledHeight};
    }

    public static String getFolderPath() {
        return Environment.getExternalStorageDirectory() + "/" + AppUtils.DIR_NAME;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    public static String findLabelHeading(String vic) {
        if (vic != null)
            return vic.replaceAll("<br/>", "");
        return "";
    }

    public static int convertDpToInt(Context context, int dp) {
        int dia = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return dia;

    }

    public static void getDeviceToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful())
                PreferenceKeeper.getInstance().setDeviceToken(task.getResult().getToken());
            }
        });
    }

    public static void sendNotification(String msg,Context context){
        Intent intent=new Intent(context, HomeActivity.class);

        /*intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        // Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri soundUri = Uri.parse("android.resource://"+context.getPackageName()+"/"+ R.raw.notification_sound);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        notificationBuilder.setContentTitle(context.getString(R.string.app_name))
                .setContentText(msg)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri)
                .setVibrate(new long[]{ 1 })
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setSmallIcon(R.mipmap.jeebo_icon);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.jeebo_icon);
        }

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            CharSequence name = context.getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Uri defaultSoundUri = Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.notification_sound);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.canShowBadge();
            mChannel.setShowBadge(true);
            mChannel.enableVibration(true);
            mChannel.setSound(defaultSoundUri, attributes);
            notificationBuilder.setChannelId(CHANNEL_ID);
            notificationBuilder.setNumber(1);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
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
