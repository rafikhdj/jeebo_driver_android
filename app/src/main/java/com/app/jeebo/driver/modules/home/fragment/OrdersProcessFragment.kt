package com.app.jeebo.driver.modules.home.fragment

import android.Manifest
import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager

import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseFragment
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.home.adapter.AdapterOrderProducts
import com.app.jeebo.driver.modules.home.model.*
import com.app.jeebo.driver.utils.AppConstant
import com.app.jeebo.driver.utils.DialogManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_orders_process.*
import retrofit2.Call
import android.widget.AdapterView
import net.hockeyapp.android.metrics.model.User
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.jeebo.driver.modules.home.adapter.SpinnerAdapter
import com.app.jeebo.driver.view.CustomTextView
import kotlinx.android.synthetic.main.inflater_cancel_delivery_dialog.*


class OrdersProcessFragment : BaseFragment() {

    private var orderList=ArrayList<OrderListResult>()
    private var address:String=""
    private var subCatid:Int=0
    private var cancelCatList=ArrayList<CancelCategoriesResult>()
    private var cancelSubCatList=ArrayList<CancelCategoriesResult>()
    private lateinit var tvArrived:CustomTextView
    private lateinit var tvPicked:CustomTextView
    private lateinit var tvCompleted:CustomTextView
    private lateinit var tvCancel:CustomTextView
    private lateinit var tvCallEshop:CustomTextView
    private lateinit var tvEshopDirections:CustomTextView
    private lateinit var tvCallClient:CustomTextView
    private lateinit var tvClientDirections:CustomTextView
    private var spinnerCat: Spinner?=null
    private var spinnerSubCat:Spinner?=null
    private val REQUEST_PHONE_CALL = 1000
    private var callClient=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_orders_process, container, false)
        inIt(view)
        return view
    }
    override fun getLayoutId(): Int {
        return 1
    }

    private fun inIt(view : View){

        tvArrived=view.findViewById(R.id.tv_arrived)
        tvPicked=view.findViewById(R.id.tv_picked_up)
        tvCompleted=view.findViewById(R.id.tv_completed)
        tvCancel=view.findViewById(R.id.tv_cancel)
        tvCallEshop=view.findViewById(R.id.tv_call_eshop)
        tvCallClient=view.findViewById(R.id.tv_call_client)
        tvEshopDirections=view.findViewById(R.id.tv_map_eshop)
        tvClientDirections=view.findViewById(R.id.tv_map_client)

        try{
            tvClientDirections.setOnClickListener {
                var destinationLat=orderList[0].latitude
                var destinationLng=orderList[0].longitude
                val uri = "geo:0,0?q=" + destinationLat + "," + destinationLng + " (" + orderList[0].userOrderDetails.name + ")"

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }

        }catch (e:Exception){

        }
        try{
            tvEshopDirections.setOnClickListener {
                var destinationLat=orderList[0].customerOrderDetails[0].merchantProductOrderDetails.merchantProfile.businessDetails.merchantAddress[0].lat
                var destinationLng=orderList[0].customerOrderDetails[0].merchantProductOrderDetails.merchantProfile.businessDetails.merchantAddress[0].lng
                val uri = "geo:0,0?q=" + destinationLat + "," + destinationLng + " (" + orderList[0].customerOrderDetails[0].merchantProductOrderDetails.merchantProfile.businessDetails.businessName + ")"

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }
        }catch (e:Exception){

        }
        tvCallClient.setOnClickListener {
            if(!TextUtils.isEmpty(orderList.get(0).phone)){
                callClient=true
                callOwner(orderList.get(0).phone)
            }else if(!TextUtils.isEmpty(orderList.get(0).userOrderDetails.customerProfile.phone)){
                callClient=true
                callOwner(orderList.get(0).userOrderDetails.customerProfile.phone)
            }else{
                baseActivity.showToast(getString(R.string.phone_not_available))
            }
        }

        tvCallEshop.setOnClickListener {
            if(!TextUtils.isEmpty(orderList.get(0).customerOrderDetails.get(0).merchantProductOrderDetails.merchantProfile.businessDetails.phone)){
                callClient=false
                callOwner(orderList.get(0).customerOrderDetails.get(0).merchantProductOrderDetails.merchantProfile.businessDetails.phone)
            }else{
                baseActivity.showToast(getString(R.string.phone_not_available))
            }
        }


        getCancelCategoriesList()
        getOrderDetails()
        tvArrived.setOnClickListener {
            if(orderList.get(0).delivery_stage==0)
            changeDeliveryStage(1)
        }
        tvPicked.setOnClickListener {
            if(orderList.get(0).delivery_stage==1)
            changeDeliveryStage(2)
        }
        tvCompleted.setOnClickListener {
            if(orderList.get(0).delivery_stage==2)
            changeDeliveryStage(3)
        }

        tvCancel.setOnClickListener {
            cancelOrder()
        }
    }

    private fun getCancelCategoriesList(){
        val request=ApiClient.getRequest()
        val call= request.getCancelCategories()
        call.enqueue(object:ApiCallback<CancelCategoriesResponse>(){
            override fun onSuccess(t: CancelCategoriesResponse?) {
                cancelCatList.clear()
                if(t != null && t.results !=null){
                    var cancelCategoriesResult=CancelCategoriesResult()
                    cancelCategoriesResult.name=getString(R.string.select_cat)
                    cancelCatList.add(cancelCategoriesResult)
                    cancelCatList.addAll(t.results)
                }

            }

            override fun onError(error: Error?) {

            }

        })
    }

    private fun getCancelSubcategory(id:Int){
        baseActivity.showProgressBar(baseActivity)
        val request=ApiClient.getRequest()
        val call=request.getCancelSubCategories(id);
        call.enqueue(object:ApiCallback<CancelCategoriesResponse>(){
            override fun onSuccess(t: CancelCategoriesResponse?) {
                baseActivity.dismissProgressBar()
                cancelSubCatList.clear()
                if(t != null && t.results !=null){
                    var cancelCategoriesResult=CancelCategoriesResult()
                    cancelCategoriesResult.name=getString(R.string.select_sub_cat)
                    cancelSubCatList.add(cancelCategoriesResult)
                    cancelSubCatList.addAll(t.results)
                    val adapter=SpinnerAdapter(baseActivity,cancelSubCatList)
                    spinnerSubCat?.adapter=adapter
                }
            }

            override fun onError(error: Error?) {
                baseActivity.dismissProgressBar()
            }

        })
    }

    private fun cancelOrder(){
        val dialog = Dialog(baseActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.inflater_cancel_delivery_dialog)
        if (!dialog.isShowing)
            dialog.show()

        val window = dialog.getWindow()
        val wlp = window!!.getAttributes()

        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window!!.setAttributes(wlp)
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        spinnerCat=dialog.findViewById(R.id.spinner_cat)
        spinnerSubCat=dialog.findViewById(R.id.spinner_sub_cat)
         var ivBack=dialog.findViewById(R.id.iv_back) as ImageView

        var tvSubmit=dialog.findViewById(R.id.tv_submit) as CustomTextView
        var tvCancel=dialog.findViewById(R.id.tv_cancel) as CustomTextView

        ivBack.setOnClickListener { dialog.dismiss() }

        tvCancel.setOnClickListener { dialog.dismiss() }

        tvSubmit.setOnClickListener {
            if(subCatid != 0){
                baseActivity.showProgressBar(baseActivity)

                var cancelOrderReq=CancelOrderRequest()
                cancelOrderReq.order_id=orderList.get(0).id
                cancelOrderReq.cancel_subcategory=subCatid

                val request=ApiClient.getRequest()
                val call=request.cancelOrder(cancelOrderReq)
                call.enqueue(object:ApiCallback<AcceptOrderResponse>(){
                    override fun onSuccess(t: AcceptOrderResponse?) {
                        dialog.dismiss()
                        if(t != null && !TextUtils.isEmpty(t.results))
                            DialogManager.showValidationDialog(baseActivity,t.results)
                        getOrderDetails()
                    }

                    override fun onError(error: Error?) {
                        baseActivity.dismissProgressBar()
                        if(error != null && !TextUtils.isEmpty(error.errMsg))
                            DialogManager.showValidationDialog(baseActivity,error.errMsg)
                    }

                })
            }
        }

        val adapter=SpinnerAdapter(baseActivity,cancelCatList)
        spinnerCat?.adapter=adapter

        spinnerCat?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getCancelSubcategory(cancelCatList.get(position).id)
            }

        }

        spinnerSubCat?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                subCatid=cancelSubCatList.get(position).id
            }

        }

    }

    override fun onLayoutCreated(view: View?) {

    }

    private fun changeDeliveryStage(stage:Int){
        baseActivity.showProgressBar(baseActivity)
        var changeDeliveryStageReq = ChangeDeliveryStageReq()
        changeDeliveryStageReq.stage=stage
        changeDeliveryStageReq.order_id=orderList.get(0).id
        val request=ApiClient.getRequest()
        val call= request.changeDeliveryStage(changeDeliveryStageReq);
        call.enqueue(object : ApiCallback<AcceptOrderResponse>(){
            override fun onSuccess(t: AcceptOrderResponse?) {
                orderList.get(0).delivery_stage=stage
                baseActivity.dismissProgressBar()
                setDeliveryStage(stage)
                /*if(t != null && !TextUtils.isEmpty(t.result))
                    DialogManager.showValidationDialog(baseActivity,t.result)*/
            }

            override fun onError(error: Error?) {
                baseActivity.dismissProgressBar()
                if(error != null && !TextUtils.isEmpty(error.errMsg))
                    DialogManager.showValidationDialog(baseActivity,error.errMsg)
            }

        })
    }

    private fun getOrderDetails(){
        baseActivity.showProgressBar(baseActivity)
        val request= ApiClient.getRequest()
        val call=request.getOrderList(AppConstant.IN_PROCESS_ORDER,"1","10")
        call.enqueue(object : ApiCallback<OrderListResponse>(){
            override fun onSuccess(t: OrderListResponse?) {
                baseActivity.dismissProgressBar()

                orderList.clear()

                if(t != null && t.results.size>0)
                    orderList.addAll(t.results)
                setOrderDetails()
            }

            override fun onError(error: Error?) {
                baseActivity.dismissProgressBar()
                if(error != null && !TextUtils.isEmpty(error.errMsg))
                    DialogManager.showValidationDialog(baseActivity,error.errMsg)
            }

        })
    }

    private fun setOrderDetails(){
        nested_scroll_view.visibility=View.VISIBLE
        if(orderList.size>0){
            nested_scroll_view.visibility=View.VISIBLE
            tv_no_record.visibility=View.GONE

            val orderListResult=orderList.get(0)

            try{

                if(!TextUtils.isEmpty(orderListResult.customerOrderDetails.get(0).productOrder.parent_category_url)){
                    Glide.with(baseActivity).load(orderListResult.customerOrderDetails.get(0).productOrder.parent_category_url)
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



            if(!TextUtils.isEmpty(orderListResult.orderId))
                tv_order_no.text=orderListResult.orderId

            if(!TextUtils.isEmpty(orderListResult.comment)){
                tv_instructions.text=orderListResult.comment
               /* tv_instructions.visibility=View.VISIBLE
                tv_instructions_heading.visibility=View.VISIBLE*/
            }
            else{
               /* tv_instructions.visibility=View.GONE
                tv_instructions_heading.visibility=View.GONE*/
            }

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

            tv_client_address.setText(getClientAddress(0))

            if(!TextUtils.isEmpty(orderListResult.payment_type))
                tv_payment_mode.text=orderListResult.payment_type

            if(orderListResult.customerOrderDetails != null && orderListResult.customerOrderDetails.size>0){
                var products:String=""
                for(i in 0..orderListResult.customerOrderDetails.size-1){
                    if(orderListResult.customerOrderDetails != null && !orderListResult.customerOrderDetails.get(i).status.equals("Cancelled")){
                        val quantity:Int= orderListResult.customerOrderDetails.get(i).quantity.toInt();
                        if(i==0){
                            products=""+quantity +" "+ orderListResult.customerOrderDetails.get(i).productOrder.productTitle+"\n"
                        }else{
                            products=products+quantity +" "+ orderListResult.customerOrderDetails.get(i).productOrder.productTitle+"\n"
                        }
                    }
                }
                tv_cat_products.text=products

                val adapter=AdapterOrderProducts(baseActivity,orderListResult.customerOrderDetails)
                rv_item_price.adapter=adapter
                rv_item_price.layoutManager= LinearLayoutManager(baseActivity) as RecyclerView.LayoutManager?

            }

            tv_pay_to_merchant.text="DA "+orderListResult.pay_to_merchant

            tv_collect_from_client.text="DA "+orderListResult.collect_from_client

            tv_delivery_fee_value.text="DA "+orderListResult.delivery_charge

            setDeliveryStage(orderListResult.delivery_stage)


        }else{
            nested_scroll_view.visibility=View.GONE
            tv_no_record.visibility=View.VISIBLE
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
            /*if (!TextUtils.isEmpty(orderList[i].userOrderDetails.deliveryAddress[0].house_no))
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

            address=orderList[i].delivery_address

        } catch (e: Exception) {

        }


        return address
    }

    private fun setDeliveryStage(stage:Int){
        if(stage==0){
            iv_stage_1.setImageResource(R.drawable.blue_line_unfilled)
            iv_stage_2.setImageResource(R.drawable.blue_line_unfilled)
            iv_stage_3.setImageResource(R.drawable.blue_line_unfilled)
            tv_stage_1.setBackgroundResource(R.drawable.green_circle_unfilled)
            tv_stage_2.setBackgroundResource(R.drawable.green_circle_unfilled)
            tv_stage_3.setBackgroundResource(R.drawable.green_circle_unfilled)
            tv_stage_1.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_stage_1.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_stage_1.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))

            tv_arrived.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_arrived.background=baseActivity.resources.getDrawable(R.drawable.bg_button_unfilled)

            tv_picked_up.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_picked_up.background=baseActivity.resources.getDrawable(R.drawable.bg_button_unfilled)

            tv_completed.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_completed.background=baseActivity.resources.getDrawable(R.drawable.bg_button_unfilled)

        }else if(stage==1){
            iv_stage_1.setImageResource(R.drawable.green_line_filled)
            iv_stage_2.setImageResource(R.drawable.blue_line_unfilled)
            iv_stage_3.setImageResource(R.drawable.blue_line_unfilled)
            tv_stage_1.setBackgroundResource(R.drawable.green_circle_filled)
            tv_stage_2.setBackgroundResource(R.drawable.green_circle_unfilled)
            tv_stage_3.setBackgroundResource(R.drawable.green_circle_unfilled)
            tv_stage_1.setTextColor(baseActivity.resources.getColor(R.color.white))
            tv_stage_1.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_stage_1.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))

            tv_arrived.setTextColor(baseActivity.resources.getColor(R.color.white))
            tv_stage_1.setTextColor(baseActivity.resources.getColor(R.color.white))
            tv_arrived.background=baseActivity.resources.getDrawable(R.drawable.bg_button_filled)

            tv_picked_up.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_picked_up.background=baseActivity.resources.getDrawable(R.drawable.bg_button_unfilled)

            tv_completed.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_completed.background=baseActivity.resources.getDrawable(R.drawable.bg_button_unfilled)

        }else if(stage==2){
            iv_stage_1.setImageResource(R.drawable.green_line_filled)
            iv_stage_2.setImageResource(R.drawable.green_line_filled)
            iv_stage_3.setImageResource(R.drawable.blue_line_unfilled)
            tv_stage_1.setBackgroundResource(R.drawable.green_circle_filled)
            tv_stage_2.setBackgroundResource(R.drawable.green_circle_filled)
            tv_stage_3.setBackgroundResource(R.drawable.green_circle_unfilled)
            tv_stage_1.setTextColor(baseActivity.resources.getColor(R.color.white))
            tv_stage_2.setTextColor(baseActivity.resources.getColor(R.color.white))
            tv_stage_3.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))

            tv_arrived.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_arrived.background=baseActivity.resources.getDrawable(R.drawable.bg_button_unfilled)

            tv_picked_up.setTextColor(baseActivity.resources.getColor(R.color.white))
            tv_stage_2.setTextColor(baseActivity.resources.getColor(R.color.white))
            tv_picked_up.background=baseActivity.resources.getDrawable(R.drawable.bg_button_filled)

            tv_completed.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_completed.background=baseActivity.resources.getDrawable(R.drawable.bg_button_unfilled)

        }else if(stage==3){
            tv_arrived.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_arrived.background=baseActivity.resources.getDrawable(R.drawable.bg_button_unfilled)

            tv_picked_up.setTextColor(baseActivity.resources.getColor(R.color.color_0B94C7))
            tv_picked_up.background=baseActivity.resources.getDrawable(R.drawable.bg_button_unfilled)

            tv_completed.setTextColor(baseActivity.resources.getColor(R.color.white))
            tv_stage_3.setTextColor(baseActivity.resources.getColor(R.color.white))
            tv_completed.background=baseActivity.resources.getDrawable(R.drawable.bg_button_filled)
            getOrderDetails()
        }
    }

        private fun callOwner(phone:String) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + phone)
            if (ContextCompat.checkSelfPermission(baseActivity, Manifest.permission.CALL_PHONE) !== PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(baseActivity, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            } else {
                startActivity(callIntent)
            }
        }

    override fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PHONE_CALL -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(callClient)
                        callOwner(orderList.get(0).phone)
                    else
                        callOwner(orderList.get(0).customerOrderDetails.get(0).merchantProductOrderDetails.merchantProfile.businessDetails.phone)
                } else {
                    baseActivity.showToast(getString(R.string.call_permission_msg))
                }
                return
            }
        }
    }

}
