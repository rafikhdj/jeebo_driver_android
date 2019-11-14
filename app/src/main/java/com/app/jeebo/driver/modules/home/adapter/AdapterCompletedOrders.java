package com.app.jeebo.driver.modules.home.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jeebo.driver.R;
import com.app.jeebo.driver.base.BaseActivity;
import com.app.jeebo.driver.modules.home.model.OrderListResult;
import com.app.jeebo.driver.utils.ItemClickListener;
import com.app.jeebo.driver.view.CustomTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class AdapterCompletedOrders extends RecyclerView.Adapter<AdapterCompletedOrders.CustomViewHolder> {

    private BaseActivity mContext;
    private List<OrderListResult> orderList;
    private ItemClickListener itemClickListener;
    private String address;

    public AdapterCompletedOrders(BaseActivity context, List<OrderListResult> orderList,ItemClickListener itemClickListener) {
        this.mContext = context;
        this.orderList = orderList;
        this.itemClickListener = itemClickListener;
        address=" ";
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new CustomViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        OrderListResult orderListResult=orderList.get(position);

        if(!TextUtils.isEmpty(orderListResult.getOrderId()))
            holder.tvOrderNo.setText(orderListResult.getOrderId());

        if(!TextUtils.isEmpty(orderListResult.getPhone())){
            holder.tvPhone.setText(orderListResult.getPhone());
            holder.tvPhone.setVisibility(View.VISIBLE);
        }else{
            holder.tvPhone.setVisibility(View.GONE);
        }

        if(orderListResult.getUserOrderDetails() != null && !TextUtils.isEmpty(orderListResult.getUserOrderDetails().getName()))
            holder.tvClientName.setText(orderListResult.getUserOrderDetails().getName());

        try{
            holder.tvMerchantName.setText(orderListResult.getCustomerOrderDetails().get(0).getMerchantProductOrderDetails().getMerchantProfile().getBusinessDetails().getBusinessName());
        }catch (Exception e){

        }

        holder.tvMerchantAddress.setText(getMerchantAddress(position));

        holder.tvClientAddress.setText(getClientAddress(position));

        holder.tvTakeIncharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClickListener(view,position);
            }
        });

        try{

            if(!TextUtils.isEmpty(orderListResult.getCustomerOrderDetails().get(0).getProductOrder().getParent_category_url())){
                Glide.with(mContext).load(orderListResult.getCustomerOrderDetails().get(0).getProductOrder().getParent_category_url())
                        .apply(RequestOptions.circleCropTransform()).into(holder.ivItem);
            }

            if(orderListResult.getCustomerOrderDetails().get(0).getProductOrder().getParent_category_id()==1){
                holder.tvItem.setText("Food");
            }else if(orderListResult.getCustomerOrderDetails().get(0).getProductOrder().getParent_category_id()==2){
                holder.tvItem.setText("Home");
            }else if(orderListResult.getCustomerOrderDetails().get(0).getProductOrder().getParent_category_id()==3){
                holder.tvItem.setText("Beauty");
            }else if(orderListResult.getCustomerOrderDetails().get(0).getProductOrder().getParent_category_id()==4){
                holder.tvItem.setText("Healthcare");
            }else if(orderListResult.getCustomerOrderDetails().get(0).getProductOrder().getParent_category_id()==5){
                holder.tvItem.setText("Fashion");
            }else{
                holder.tvItem.setText("Grocery");
            }
        }catch (Exception e){

        }

    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.inflater_completed_order;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private String getMerchantAddress(int i) {
        try {
            if (!TextUtils.isEmpty(orderList.get(i).getCustomerOrderDetails().get(0).getMerchantProductOrderDetails().getMerchantProfile().getBusinessDetails().getMerchantAddress().get(0).getFullAddress()))
                address = orderList.get(i).getCustomerOrderDetails().get(0).getMerchantProductOrderDetails().getMerchantProfile().getBusinessDetails().getMerchantAddress().get(0).getFullAddress() + ", ";

        } catch (Exception e) {

        }
        return address;
    }

    private String getClientAddress(int i){
        try{
           /* if(!TextUtils.isEmpty(orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getHouse_no()))
                address=orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getHouse_no()+", ";

            if(!TextUtils.isEmpty(orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getLocality()))
                address=address+orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getLocality()+", ";

            if(!TextUtils.isEmpty(orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getCity()))
                address=address+orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getCity()+", ";

            if(!TextUtils.isEmpty(orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getState()))
                address=address+orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getState()+", ";

            if(!TextUtils.isEmpty(orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getCountry()))
                address=address+orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getCountry()+", ";

            if(!TextUtils.isEmpty(orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getZipcode()))
                address=address+orderList.get(i).getUserOrderDetails().getDeliveryAddress().get(0).getZipcode();*/

           address=orderList.get(i).getDelivery_address();

        }catch (Exception e){

        }


        return address;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        /*@BindView(R.id.tv_order_no)
        CustomTextView tvOrderNo;
        @BindView(R.id.tv_merchant_name)
        CustomTextView tvMerchantName;
        @BindView(R.id.tv_merchant_address)
        CustomTextView tvMerchantAddress;
        @BindView(R.id.tv_client_name)
        CustomTextView tvClientName;
        @BindView(R.id.tv_client_address)
        CustomTextView tvClientAddress;*/

        CustomTextView tvOrderNo;
        CustomTextView tvMerchantName;
        CustomTextView tvMerchantAddress;
        CustomTextView tvClientName;
        CustomTextView tvClientAddress;
        CustomTextView tvTakeIncharge;
        CustomTextView tvPhone;
        CustomTextView tvItem;
        ImageView ivItem;

        CustomViewHolder(View itemView, int viewType) {
            super(itemView);
            //ButterKnife.bind(this,itemView);
            tvOrderNo=itemView.findViewById(R.id.tv_order_no);
            tvMerchantName=itemView.findViewById(R.id.tv_merchant_name);
            tvClientName=itemView.findViewById(R.id.tv_client_name);
            tvMerchantAddress=itemView.findViewById(R.id.tv_merchant_address);
            tvClientAddress=itemView.findViewById(R.id.tv_client_address);
            tvTakeIncharge=itemView.findViewById(R.id.tv_take_incharge);
            tvPhone=itemView.findViewById(R.id.tv_phone);
            ivItem=itemView.findViewById(R.id.iv_item);
            tvItem=itemView.findViewById(R.id.tv_item);
        }

    }


}
