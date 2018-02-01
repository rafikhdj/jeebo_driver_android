package com.clientname.androidbase.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.clientname.androidbase.activity.BaseActivity;

/*
  Base class which used all fragment
 */
public class BaseFragment extends Fragment {

    protected BaseActivity activity;
    private ProgressDialog progressDialog;


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = (BaseActivity) activity;
    }

    public void onTabSelected() {

    }

    public void showProgressBar(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
        }
        progressDialog.setTitle(msg);
        if (isVisible()) {
            progressDialog.show();
        }
    }

    public void hideProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
