package com.app.jeebo.driver.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;


/*
  Base class which used all fragment
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity baseActivity;
   // protected BaseActivity activity;
    private ProgressDialog progressDialog;
    public View view;

    public abstract int getLayoutId();

    public abstract void onLayoutCreated(View view);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
           // activity = (BaseActivity) getActivity();
            view = inflater.inflate(getLayoutId(), null);
            onLayoutCreated(view);
        }
        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.baseActivity = (BaseActivity) activity;
    }

    public void onTabSelected() {

    }

    public void showProgressBar(String msg) {
        if (progressDialog == null) {
          //  progressDialog = new ProgressDialog(baseActivity);
        }
        progressDialog.setTitle(msg);
        if (isVisible()) {
            progressDialog.show();
        }
    }

    public void hideProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void makeBundle() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            setArguments(bundle);
        }
    }


    public BaseFragment addBundleLong(String key, long value) {
        makeBundle();
        getArguments().putLong(key, value);
        return this;
    }

    public BaseFragment addBundleInt(String key, int value) {
        makeBundle();
        getArguments().putInt(key, value);
        return this;
    }

    public BaseFragment addBundleSting(String key, String value) {
        makeBundle();
        getArguments().putString(key, value);
        return this;
    }


    public BaseFragment addBundleBoolean(String key, boolean value) {
        makeBundle();
        getArguments().putBoolean(key, value);
        return this;
    }

    public BaseFragment addSerializable(String key, Serializable obj) {
        makeBundle();
        getArguments().putSerializable(key, obj);
        return this;
    }



}
