package com.app.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.app.utils.AppConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SmsReceiver extends BroadcastReceiver {

    private String otp;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction()
                .equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras(); // ---get the SMS message passed
            // in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                // ---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        if (msg_from.contains("App Name")) {
                            String msgBody = msgs[i].getMessageBody();
                            String pattern = "\\d+";
                            Pattern p = Pattern.compile(pattern);
                            Matcher m = p.matcher(msgBody);
                            while (m.find()) {
                                String otp = msgBody.substring(m.start(), m.end());
                                System.out.println(otp);
                                Intent msgIntent = new Intent(AppConstant.INTENT_EXTRAS.SMS_RECEIVED);
                                // add data
                                msgIntent.putExtra("message", otp);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);

                            }

                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }

}
