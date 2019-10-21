package com.app.jeebo.driver.modules.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jeebo.driver.R;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.MyHolder> {

    @NonNull
    @Override
    public PendingOrderAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pending_order_list, null, false);

        return new PendingOrderAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderAdapter.MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
