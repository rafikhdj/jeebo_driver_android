package com.app.jeebo.driver.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.TypedValue;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


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
}
