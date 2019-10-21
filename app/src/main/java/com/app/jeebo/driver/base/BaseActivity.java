package com.app.jeebo.driver.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.jeebo.driver.R;

public class BaseActivity extends AppCompatActivity {

    private Dialog alertDialog;
    public static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        checkForCrashes();
//    }

//    public void checkForCrashes() {
//        CrashManager.register(this);
//    }


    public void launchActivity(Class<? extends BaseActivity> activityClass) {
        if (activityClass != null) {
            launchActivity(activityClass, null);
        }
    }

    public void dismissProgressBar() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            Log.e("e", "=" + e);
        }

    }

    public void showProgressBar( Context context) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                dismissProgressBar();
            }

            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (activity.isFinishing()) {
                    return;
                }
            }

            progressDialog = new ProgressDialog(context, R.style.DialogTheme);
            if (context != null) {
                progressDialog.show();
                WindowManager.LayoutParams layoutParams = progressDialog.getWindow().getAttributes();
                layoutParams.dimAmount = 0.5f;
                progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.dialog_loading);
                RelativeLayout frameLayout = (RelativeLayout) progressDialog.findViewById(R.id.dlgProgress);

                ((ProgressBar) progressDialog.findViewById(R.id.progress_wheel)).setProgress(10);
                /*TextView messageText = (TextView) progressDialog.findViewById(R.id.tvProgress);
                //     messageText.setTypeface(DataUtil.getFont(context));
                //messageText.setText(message);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToast(String message) {
        if (!TextUtils.isEmpty(message))
            Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    public void launchActivity(Class<? extends BaseActivity> activityClass, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*public void showProgressBar(String msg) {
        if (alertDialog != null && alertDialog.isShowing() && this.isFinishing()) {
            return;
        }
        alertDialog = new Dialog(this);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }*/

    public void hideProgressBar() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

}
