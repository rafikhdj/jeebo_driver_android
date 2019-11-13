package com.app.jeebo.driver.modules.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.jeebo.driver.R;
import com.app.jeebo.driver.modules.home.model.CancelCategoriesResult;
import com.app.jeebo.driver.view.CustomTextView;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<CancelCategoriesResult> items;

    public SpinnerAdapter(@NonNull Context context,@NonNull List objects) {
        super(context, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        items = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(R.layout.inflater_spinner_item, parent, false);


        CustomTextView tvName = (CustomTextView) view.findViewById(R.id.tv_name);

        CancelCategoriesResult offerData = items.get(position);

        tvName.setText(offerData.getName());

        return view;
    }
}