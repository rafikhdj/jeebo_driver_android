package com.app.jeebo.driver.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.app.jeebo.driver.R;
import com.app.jeebo.driver.view.CustomEditText;

import java.util.ArrayList;

public class OtpTextWatcher implements TextWatcher {

    private View view;
    private ArrayList<CustomEditText> edList;

    public OtpTextWatcher(View view, ArrayList<CustomEditText> myList) {
        this.view = view;
        this.edList = myList;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override//int start,int before, int count
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        switch (view.getId()) {
            case R.id.edt_otp1:
                if (text.length() == 1)
                    //       imageViewList.get(0).setVisibility(View.GONE);
                    //     edList.get(0).setVisibility(View.INVISIBLE);
                    edList.get(1).requestFocus();
                break;
            case R.id.edt_otp2:
                if (text.equals("")) {
                    edList.get(0).requestFocus();
                } else {
                    if (text.length() == 1)
                        edList.get(2).requestFocus();
                }
                break;
            case R.id.edt_otp3:
                if (text.equals("")) {
                    if (edList.get(1).getText().toString().equals("")) {
                        edList.get(0).requestFocus();
                    } else {
                        edList.get(1).requestFocus();
                    }
                } else {
                    if (text.length() == 1)
                        edList.get(3).requestFocus();
                }
                break;
            case R.id.edt_otp4:
                if (text.equals("")) {
                    if (edList.get(2).getText().toString().equals("")) {
                        if (edList.get(1).getText().toString().equals("")) {
                            edList.get(0).requestFocus();
                        } else {
                            edList.get(1).requestFocus();
                        }
                    } else {
                        edList.get(2).requestFocus();
                    }
                }
                // edList.get(3).setVisibility(View.INVISIBLE);
                break;
        }
    }
}
