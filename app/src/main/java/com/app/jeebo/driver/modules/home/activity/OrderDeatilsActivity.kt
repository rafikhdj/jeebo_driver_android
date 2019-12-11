package com.app.jeebo.driver.modules.home.activity

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.home.adapter.AdapterOrderProducts
import com.app.jeebo.driver.modules.home.model.*
import com.app.jeebo.driver.utils.AppConstant
import com.app.jeebo.driver.utils.DialogManager
import com.app.jeebo.driver.view.CustomTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_order_deatils.*

class OrderDeatilsActivity : BaseActivity() {

    private var orderList=ArrayList<OrderListResult>()
    private var orderId:String?=null
    private var cameFrom:String?=null


    private lateinit var ivBack: ImageView
    private lateinit var tvConfirm: CustomTextView

    private var address:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_deatils)
        init()
    }

    private fun init(){
        ivBack=findViewById(R.id.iv_back)
        tvConfirm=findViewById(R.id.tv_confirm)
        orderId= intent.extras?.getString(AppConstant.INTENT_EXTRAS.ORDER_ID,"")
        cameFrom= intent.extras?.getString(AppConstant.INTENT_EXTRAS.CAME_FROM,"")

        if(!TextUtils.isEmpty(cameFrom) && cameFrom.equals(AppConstant.INTENT_EXTRAS.PENDING)){
            tvConfirm.visibility=View.VISIBLE
        }else{
            tvConfirm.visibility=View.GONE
        }

        ivBack.setOnClickListener {
            //finish()
            finish()
            val bundle=Bundle()
            if(!TextUtils.isEmpty(cameFrom) && cameFrom.equals(AppConstant.INTENT_EXTRAS.PENDING)){
                bundle.putString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.PENDING_ORDER)
                launchActivity(HomeActivity::class.java,bundle)
            }else if(!TextUtils.isEmpty(cameFrom) && cameFrom.equals(AppConstant.INTENT_EXTRAS.COMPLETED)){
                bundle.putString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.COMPLETED_ORDER)
                launchActivity(HomeActivity::class.java,bundle)
            }else if(!TextUtils.isEmpty(cameFrom) && cameFrom.equals(AppConstant.INTENT_EXTRAS.CANCELLED)){
               finish()
            }
            }

        tvConfirm.setOnClickListener { showAcceptDialog() }

        try{
            tv_map_eshop.setOnClickListener {
                var destinationLat=orderList[0].customerOrderDetails[0].merchantProductOrderDetails.merchantProfile.businessDetails.merchantAddress[0].lat
                var destinationLng=orderList[0].customerOrderDetails[0].merchantProductOrderDetails.merchantProfile.businessDetails.merchantAddress[0].lng
                val uri = "geo:0,0?q=" + destinationLat + "," + destinationLng + " (" + orderList[0].customerOrderDetails[0].merchantProductOrderDetails.merchantProfile.businessDetails.businessName + ")"

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }
        }catch (e:Exception){

        }

        try{
            tv_map_client.setOnClickListener {
                var destinationLat=orderList[0].latitude
                var destinationLng=orderList[0].longitude
                val uri = "geo:0,0?q=" + destinationLat + "," + destinationLng + " (" + orderList[0].userOrderDetails.name + ")"

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }

        }catch (e:Exception){

        }

        getOrderDetails()
    }

    private fun showAcceptDialog(){
        val dialog = Dialog(this@OrderDeatilsActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.inflater_dialog)
        if (!dialog.isShowing)
            dialog.show()

        val tvMsg:CustomTextView = dialog.findViewById<View>(R.id.tv_message) as CustomTextView
        val tvOk = dialog.findViewById<View>(R.id.tv_ok)
        val tvCancel = dialog.findViewById<View>(R.id.tv_cancel)
        tvCancel.visibility = View.VISIBLE
        tvMsg.setText(getString(R.string.accept_order_warning))
        tvOk.setOnClickListener{view->
            run {
                dialog.dismiss()
                acceptOrder()
            }
        }
        tvCancel.setOnClickListener { view -> dialog.dismiss() }
    }

    private fun acceptOrder(){
        //baseActivity.showToast(""+adapterPos)
        showProgressBar(this@OrderDeatilsActivity)
        val request=ApiClient.getRequest()
        val acceptOrderReq = AcceptOrderReq()
        acceptOrderReq.order_id=orderId
        val call=request.acceptOrder(acceptOrderReq)
        call.enqueue(object : ApiCallback<AcceptOrderResponse>(){
            override fun onSuccess(t: AcceptOrderResponse?) {
                dismissProgressBar()
                if(t != null && !TextUtils.isEmpty(t.results))
                    showToast(t.results)
                  //  DialogManager.showValidationDialog(this@OrderDeatilsActivity,t.results)
               // finish()
                finish()
                val bundle=Bundle()
                bundle.putString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.IN_PROCESS_ORDER)
                launchActivity(HomeActivity::class.java,bundle)
            }

            override fun onError(error: Error?) {
                dismissProgressBar()
                if(error != null && !TextUtils.isEmpty(error.errMsg))
                    DialogManager.showValidationDialog(this@OrderDeatilsActivity,error.errMsg)
            }

        })
    }


    private fun getOrderDetails(){
        showProgressBar(this@OrderDeatilsActivity)
        val request= ApiClient.getRequest()
        val call=request.getOrderDetails(orderId)
        call.enqueue(object : ApiCallback<OrderDetailResponse>(){
            override fun onSuccess(t: OrderDetailResponse?) {
                dismissProgressBar()

                orderList.clear()

                if(t != null && t.results!=null)
                    orderList.add(t.results)
                setOrderDetails()
            }

            override fun onError(error: Error?) {
                dismissProgressBar()
            }

        })
    }

    private fun setOrderDetails(){
        if(orderList.size>0){
            nested_scroll_view.visibility= View.VISIBLE
            tv_no_record.visibility= View.GONE

            val orderListResult=orderList.get(0)

            try{

                if(!TextUtils.isEmpty(orderListResult.customerOrderDetails.get(0).productOrder.parent_category_url)){
                    Glide.with(this@OrderDeatilsActivity).load(orderListResult.customerOrderDetails.get(0).productOrder.parent_category_url)
                            .apply(RequestOptions.circleCropTransform()).into(iv_category)
                }

                if(orderListResult.customerOrderDetails.get(0).productOrder.parent_category_id==1){
                    tv_cat_name.text="Food"
                }else if(orderListResult.customerOrderDetails.get(0).productOrder.parent_category_id==2){
                    tv_cat_name.text="Home"
                }else if(orderListResult.customerOrderDetails.get(0).productOrder.parent_category_id==3){
                    tv_cat_name.text="Beauty"
                }else if(orderListResult.customerOrderDetails.get(0).productOrder.parent_category_id==4){
                    tv_cat_name.text="Healthcare"
                }else if(orderListResult.customerOrderDetails.get(0).productOrder.parent_category_id==5){
                    tv_cat_name.text="Fashion"
                }else{
                    tv_cat_name.text="Grocery"
                }
            }catch (e:Exception){

            }



            /*if(!TextUtils.isEmpty(orderListResult.orderId))
                tv_order_no.text=orderListResult.orderId

            if(!TextUtils.isEmpty(orderListResult.comment)){
                tv_instructions.text=orderListResult.comment
                *//* tv_instructions.visibility=View.VISIBLE
                 tv_instructions_heading.visibility=View.VISIBLE*//*
            }
            else{
                *//* tv_instructions.visibility=View.GONE
                 tv_instructions_heading.visibility=View.GONE*//*
            }*/

            if (orderListResult.userOrderDetails != null && !TextUtils.isEmpty(orderListResult.userOrderDetails.name))
                tv_client_name.setText(orderListResult.userOrderDetails.name)

            try {
                tv_eshop_name.setText(orderListResult.customerOrderDetails[0].merchantProductOrderDetails.merchantProfile.businessDetails.businessName)
            } catch (e: Exception) {

            }
            try{
                if(!TextUtils.isEmpty(orderListResult.customerOrderDetails.get(0).merchantProductOrderDetails.merchantProfile.businessDetails.phone))
                    tv_eshop_phone.text=orderListResult.customerOrderDetails.get(0).merchantProductOrderDetails.merchantProfile.businessDetails.phone
            }catch (e:Exception){

            }
            try{
                if(!TextUtils.isEmpty(orderListResult.phone))
                    tv_client_phone.text=orderListResult.phone
                else if(!TextUtils.isEmpty(orderListResult.userOrderDetails.customerProfile.phone))
                    tv_client_phone.text=orderListResult.userOrderDetails.customerProfile.phone
            }catch (e:Exception){

            }

            tv_eshop_address.setText(getMerchantAddress(0))

           /* tv_pay_to_merchant.text="DA "+orderListResult.pay_to_merchant

            tv_collect_from_client.text="DA "+orderListResult.collect_from_client

            tv_delivery_fee_value.text="DA "+orderListResult.delivery_charge*/

            tv_pay_to_merchant.text="DA "+orderListResult.pay_to_merchant
            tv_collect_from_client.text="DA "+orderListResult.collect_from_client
            tv_delivery_fee_value.text="DA "+orderListResult.delivery_charge


            tv_client_address.setText(getClientAddress(0))

            if(!TextUtils.isEmpty(orderListResult.payment_type))
                tv_payment_mode.text=orderListResult.payment_type

            if(orderListResult.customerOrderDetails != null && orderListResult.customerOrderDetails.size>0){
                var products:String=""
                for(i in 0..orderListResult.customerOrderDetails.size-1){
                    if(!orderListResult.customerOrderDetails.get(i).status.equals("Cancelled")){
                        val quantity:Int= orderListResult.customerOrderDetails.get(i).quantity.toInt();
                        if(i==0){
                            products=""+quantity +" "+ orderListResult.customerOrderDetails.get(i).productOrder.productTitle+"\n"
                        }else{
                            products=products+quantity +" "+ orderListResult.customerOrderDetails.get(i).productOrder.productTitle+"\n"
                        }
                    }

                }
                tv_cat_products.text=products

                /*val adapter= AdapterOrderProducts(this@OrderDeatilsActivity,orderListResult.customerOrderDetails)
                rv_item_price.adapter=adapter
                rv_item_price.layoutManager= LinearLayoutManager(this@OrderDeatilsActivity)*/

            }

            /*tv_pay_to_merchant.text="DA "+orderListResult.pay_to_merchant

            tv_collect_from_client.text="DA "+orderListResult.collect_from_client*/


        }else{
            nested_scroll_view.visibility= View.GONE
            tv_no_record.visibility= View.VISIBLE
        }
    }

    private fun getMerchantAddress(i: Int): String {
        try {
            if (!TextUtils.isEmpty(orderList[i].customerOrderDetails[0].merchantProductOrderDetails.merchantProfile.businessDetails.merchantAddress[0].fullAddress))
                address = orderList[i].customerOrderDetails[0].merchantProductOrderDetails.merchantProfile.businessDetails.merchantAddress[0].fullAddress

        } catch (e: Exception) {

        }

        return address
    }

    private fun getClientAddress(i: Int): String {
        try {
           /* if (!TextUtils.isEmpty(orderList[i].userOrderDetails.deliveryAddress[0].house_no))
                address = orderList[i].userOrderDetails.deliveryAddress[0].house_no + ", "

            if (!TextUtils.isEmpty(orderList[i].userOrderDetails.deliveryAddress[0].locality))
                address = address + orderList[i].userOrderDetails.deliveryAddress[0].locality + ", "

            if (!TextUtils.isEmpty(orderList[i].userOrderDetails.deliveryAddress[0].city))
                address = address + orderList[i].userOrderDetails.deliveryAddress[0].city + ", "

            if (!TextUtils.isEmpty(orderList[i].userOrderDetails.deliveryAddress[0].state))
                address = address + orderList[i].userOrderDetails.deliveryAddress[0].state + ", "

            if (!TextUtils.isEmpty(orderList[i].userOrderDetails.deliveryAddress[0].country))
                address = address + orderList[i].userOrderDetails.deliveryAddress[0].country + ", "

            if (!TextUtils.isEmpty(orderList[i].userOrderDetails.deliveryAddress[0].zipcode))
                address = address + orderList[i].userOrderDetails.deliveryAddress[0].zipcode*/

            address = orderList[i].delivery_address

        } catch (e: Exception) {

        }


        return address
    }

    override fun onBackPressed() {
       // finish()
        finish()
        /*val bundle=Bundle()
        bundle.putString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.PENDING_ORDER)
        launchActivity(HomeActivity::class.java,bundle)*/
        val bundle=Bundle()
        if(!TextUtils.isEmpty(cameFrom) && cameFrom.equals(AppConstant.INTENT_EXTRAS.PENDING)){
            bundle.putString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.PENDING_ORDER)
            launchActivity(HomeActivity::class.java,bundle)
        }else if(!TextUtils.isEmpty(cameFrom) && cameFrom.equals(AppConstant.INTENT_EXTRAS.COMPLETED)){
            bundle.putString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.COMPLETED_ORDER)
            launchActivity(HomeActivity::class.java,bundle)
        }else if(!TextUtils.isEmpty(cameFrom) && cameFrom.equals(AppConstant.INTENT_EXTRAS.CANCELLED)){
            finish()
        }
    }

}
