package com.app.jeebo.driver.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.jeebo.driver.R;
import com.app.jeebo.driver.base.BaseActivity;
import com.app.jeebo.driver.view.CustomTextView;


public class DialogManager {


    public static void openDialogCameraGallary(boolean showRemove, final BaseActivity activity, final IDialogUploadListener listner) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_camera_gallery);

        TextView cameraPhoto = (TextView) dialog.findViewById(R.id.tv_camera);
        TextView galleryPhoto = (TextView) dialog.findViewById(R.id.tv_gallery);
        TextView removePhoto = (TextView) dialog.findViewById(R.id.tv_remove);
        RelativeLayout rlRemove=dialog.findViewById(R.id.rl_remove);

        View view1 = (View) dialog.findViewById(R.id.view_remove);


        /*if(showRemove){
            view1.setVisibility(View.VISIBLE);
            rlRemove.setVisibility(View.VISIBLE);
        }
        else{
            view1.setVisibility(View.GONE);
            rlRemove.setVisibility(View.GONE);
        }*/

        view1.setVisibility(View.GONE);
        rlRemove.setVisibility(View.GONE);

        /*view1.setVisibility(View.VISIBLE);
        rlRemove.setVisibility(View.VISIBLE);*/

        cameraPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClick(true);
                dialog.dismiss();
            }
        });
        galleryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClick(false);
                dialog.dismiss();

            }
        });
        removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onRemove();
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    /*public static void showAlertDialog(BaseActivity context, String title, String msg, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("Ok", listener);
        dialogBuilder.setNegativeButton("Cancel", listener);

        if (context != null && !context.isFinishing()) {
            dialogBuilder.show();
        }

    }*/

    /*public static void showAlertDialog(Activity context, String title, String msg, String positiveBtnTxt, String negativeBtnText, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton(positiveBtnTxt, listener);
        dialogBuilder.setNegativeButton(negativeBtnText, listener);

        if (context != null && !context.isFinishing()) {
            dialogBuilder.show();
        }

    }*/

    /*public static void showAlertDialog(Context context, String title, String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialogBuilder.create();
        if (context != null) {
            alert.show();
        }
    }*/


   /* public static void showAlertDialog(Activity context, String title, String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialogBuilder.create();
        if (context != null && !context.isFinishing()) {
            alert.show();
        }
    }*/

   /* public static AlertDialog showDialog(Activity context, String title, String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialogBuilder.create();
        if (context != null && !context.isFinishing()) {
            alert.show();
        }
        return alert;
    }*/

   /* public static void showAlertDialg(BaseActivity context, String title, String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialogBuilder.create();
        if (context != null && !context.isFinishing()) {
            alert.show();
        }
    }
*/
    public static void showValidationDialog(Context activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.inflater_dialog);
        if(!dialog.isShowing())
            dialog.show();

        CustomTextView tvMsg= dialog.findViewById(R.id.tv_message);
        CustomTextView tvOk= dialog.findViewById(R.id.tv_ok);
        tvMsg.setText(msg);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /*public static void showCancelRequestDialog(Context activity, String msg, final IDialogClick iDialogClick){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.inflater_dialog);
        if(!dialog.isShowing())
            dialog.show();
        CustomTextView tvMsg= dialog.findViewById(R.id.tv_message);
        CustomTextView tvOk= dialog.findViewById(R.id.tv_ok);
        CustomTextView cancel= dialog.findViewById(R.id.tv_cancel);
        cancel.setVisibility(View.VISIBLE);
        tvMsg.setText(msg);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                iDialogClick.okBtnClick();
            }
        });



    }*/
}
