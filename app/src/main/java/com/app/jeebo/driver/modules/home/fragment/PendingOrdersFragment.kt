package com.app.jeebo.driver.modules.home.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseFragment
import com.app.jeebo.driver.enums.EScreenType
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.auth.activity.OtpVerificationActivity
import com.app.jeebo.driver.modules.home.activity.HomeActivity
import com.app.jeebo.driver.modules.home.activity.OrderDeatilsActivity
import com.app.jeebo.driver.modules.home.adapter.PendingOrderAdapter
import com.app.jeebo.driver.modules.home.model.AcceptOrderReq
import com.app.jeebo.driver.modules.home.model.AcceptOrderResponse
import com.app.jeebo.driver.modules.home.model.OrderListResponse
import com.app.jeebo.driver.modules.home.model.OrderListResult
import com.app.jeebo.driver.utils.*
import com.app.jeebo.driver.view.CustomTextView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_pending_orders.*


class PendingOrdersFragment : BaseFragment(), ItemClickListener {


    private  var adapterPendingOrders:PendingOrderAdapter?=null
    private lateinit var rvPendingOrder:RecyclerView
    private lateinit var swipeRefreshLayout:SwipeRefreshLayout
    private var pageNo:Int=1
    private var adapterPos:Int=0
    private var mLayoutManager:LinearLayoutManager?=null
    private var orderList=ArrayList<OrderListResult>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pending_orders, container, false)
        inIt(view)
        return view
    }


    private fun inIt(view: View) {
        rvPendingOrder = view.findViewById(R.id.rv_pending_order) as RecyclerView
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout



        swipeRefreshLayout.setOnRefreshListener {
            if(PreferenceKeeper.getInstance().driverStatus==1){
                pageNo=1
                getPendingOrderList(true)
            }
        }

    }

    private fun getPendingOrderList(isRefresh:Boolean){
        if(!isRefresh)
        baseActivity.showProgressBar(baseActivity)
        val request= ApiClient.getRequest()
        val call=request.getOrderList(AppConstant.PENDING_ORDER,pageNo.toString(),"10")
        call.enqueue(object : ApiCallback<OrderListResponse>(){
            override fun onSuccess(t: OrderListResponse?) {
                baseActivity.dismissProgressBar()
                if(pageNo==1){
                    swipeRefreshLayout.isRefreshing=false
                    orderList.clear()
                }
                if(t != null && t.results.size>0)
                orderList.addAll(t.results)
                try{
                    if(orderList.size>0){
                        setAdapter()
                        tv_no_record.visibility=View.GONE
                    }else{
                        tv_no_record.visibility=View.VISIBLE
                    }
                }catch (e:Exception){

                }

            }

            override fun onError(error: Error?) {
                baseActivity.dismissProgressBar()
                if(pageNo==1){
                    tv_no_record.visibility=View.VISIBLE
                }
                if(error != null && !TextUtils.isEmpty(error.errMsg))
                    DialogManager.showValidationDialog(baseActivity,error.errMsg)
            }

        })
    }

    private fun paginationScrollListner() {
        mLayoutManager = LinearLayoutManager(baseActivity)
        rvPendingOrder.setLayoutManager(mLayoutManager)
        rvPendingOrder.setItemAnimator(DefaultItemAnimator())
        rvPendingOrder.addOnScrollListener(object : EndlessRecyclerViewScrollListener(mLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                pageNo = page
                if(PreferenceKeeper.getInstance().driverStatus==1)
                getPendingOrderList(false)
            }

        })
    }

    private fun setAdapter(){
        if(adapterPendingOrders != null && pageNo>1){
            adapterPendingOrders!!.notifyDataSetChanged()
        }else{
            adapterPendingOrders= PendingOrderAdapter(baseActivity,orderList,this)
            rvPendingOrder.adapter=adapterPendingOrders
        }
    }

    override fun getLayoutId(): Int {
        return 0
    }

    override fun onLayoutCreated(view: View?) {

    }

    override fun onResume() {
        super.onResume()
        pageNo=1
        paginationScrollListner()
        if(PreferenceKeeper.getInstance().driverStatus==1){
            tv_no_record.visibility=View.GONE
            getPendingOrderList(false)
        }
        else{
            tv_no_record.visibility=View.VISIBLE
        }
    }
    override fun onItemClickListener(view: View?, pos: Int) {
        adapterPos=pos
        when(view?.id){
            R.id.tv_take_incharge->{
                if(PreferenceKeeper.getInstance().driverStatus==1)
                showAcceptDialog()
            }
            R.id.rl_main->{
                var intent= Intent(baseActivity,OrderDeatilsActivity::class.java)
                intent.putExtra(AppConstant.INTENT_EXTRAS.ORDER_ID,orderList.get(pos).id.toString())
                intent.putExtra(AppConstant.INTENT_EXTRAS.CAME_FROM,AppConstant.INTENT_EXTRAS.PENDING)

                startActivity(intent)
            }
        }
    }

    private fun showAcceptDialog(){
        val dialog = Dialog(baseActivity)
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
        baseActivity.showProgressBar(baseActivity)
        val request=ApiClient.getRequest()
        val acceptOrderReq = AcceptOrderReq()
        acceptOrderReq.order_id=orderList.get(adapterPos).id.toString()
        val call=request.acceptOrder(acceptOrderReq)
        call.enqueue(object : ApiCallback<AcceptOrderResponse>(){
            override fun onSuccess(t: AcceptOrderResponse?) {
                baseActivity.dismissProgressBar()
                PreferenceKeeper.getInstance().isCurrentOrder=true
                if(t != null && !TextUtils.isEmpty(t.results))
                    baseActivity.showToast(t.results)
                    //DialogManager.showValidationDialog(baseActivity,t.results)
                //pageNo=1

              //  getPendingOrderList()
               // baseActivity.finish()
                val bundle=Bundle()
                bundle.putString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.IN_PROCESS_ORDER)
                var intent=Intent(baseActivity,HomeActivity::class.java)
                intent.putExtras(bundle)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                baseActivity.startActivity(intent)

            }

            override fun onError(error: Error?) {
                baseActivity.dismissProgressBar()
                if(error != null && !TextUtils.isEmpty(error.errMsg))
                    DialogManager.showValidationDialog(baseActivity,error.errMsg)
            }

        })
    }


}