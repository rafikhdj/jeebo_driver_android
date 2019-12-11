package com.app.jeebo.driver.modules.home.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jeebo.driver.R;
import com.app.jeebo.driver.base.BaseActivity;
import com.app.jeebo.driver.modules.home.model.CustomerOrderDetail;
import com.app.jeebo.driver.modules.home.model.OrderListResult;
import com.app.jeebo.driver.utils.ItemClickListener;
import com.app.jeebo.driver.view.CustomTextView;

import java.util.List;


public class AdapterOrderProducts extends RecyclerView.Adapter<AdapterOrderProducts.CustomViewHolder> {

    private BaseActivity mContext;
    private List<CustomerOrderDetail> orderList;

    public AdapterOrderProducts(BaseActivity context, List<CustomerOrderDetail> orderList) {
        this.mContext = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public AdapterOrderProducts.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new AdapterOrderProducts.CustomViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        CustomerOrderDetail orderListResult=orderList.get(position);

        if(orderListResult.getStatus() != null && !orderListResult.getStatus().equalsIgnoreCase("Cancelled")){
            if(!TextUtils.isEmpty(orderListResult.getProductOrder().getProductTitle())){
                String quantity=String.valueOf(orderListResult.getQuantity());
                if(quantity.contains(".")){
                /*String[] quantityArr=quantity.split(".");
                quantity=quantityArr[0];*/
                    quantity=quantity.substring(0,quantity.indexOf("."));
                }
                holder.tvProductName.setText(quantity+" "+orderListResult.getProductOrder().getProductTitle());
            }

            holder.tvProductPrice.setText("DA "+orderListResult.getAmount());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.inflater_product_price_item;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    static class CustomViewHolder extends RecyclerView.ViewHolder {

        CustomTextView tvProductName;
        CustomTextView tvProductPrice;

        CustomViewHolder(View itemView, int viewType) {
            super(itemView);
            //ButterKnife.bind(this,itemView);
            tvProductName=itemView.findViewById(R.id.tv_product_name);
            tvProductPrice=itemView.findViewById(R.id.tv_product_price);
        }

    }


}
