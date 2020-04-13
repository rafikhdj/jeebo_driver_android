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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.jeebo.driver.modules.home.activity.HomeActivity
import com.app.jeebo.driver.modules.home.activity.OrderDeatilsActivity
import com.app.jeebo.driver.modules.home.activity.OrderInProcessActivity
import com.app.jeebo.driver.modules.home.adapter.AdapterOrderInProcess
import com.app.jeebo.driver.modules.home.adapter.PendingOrderAdapter
import com.app.jeebo.driver.modules.home.adapter.SpinnerAdapter
import com.app.jeebo.driver.utils.*
import com.app.jeebo.driver.view.CustomTextView
import kotlinx.android.synthetic.main.fragment_orders_process.tv_no_record
import kotlinx.android.synthetic.main.fragment_pending_orders.*
import kotlinx.android.synthetic.main.inflater_cancel_delivery_dialog.*


class OrdersProcessFragment : BaseFragment(),ItemClickListener {
    private  var adapterOrderInProcess: AdapterOrderInProcess?=null
    private lateinit var rvOrder:RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var pageNo:Int=1
    private var adapterPos:Int=0
    private var mLayoutManager:LinearLayoutManager?=null
    private var orderList=ArrayList<OrderListResult>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_orders_process, container, false)
        inIt(view)
        return view
    }

    private fun inIt(view: View) {
        rvOrder = view.findViewById(R.id.rv_process_order) as RecyclerView
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout



        swipeRefreshLayout.setOnRefreshListener {
            if(PreferenceKeeper.getInstance().driverStatus==1){
                pageNo=1
                getOrderInProcessList(true)
            }
        }

    }

    private fun getOrderInProcessList(isRefresh:Boolean){
        if(!isRefresh)
            baseActivity.showProgressBar(baseActivity)
        val request= ApiClient.getRequest()
        val call=request.getOrderList(AppConstant.IN_PROCESS_ORDER,pageNo.toString(),"100")
        call.enqueue(object : ApiCallback<OrderListResponse>(){
            override fun onSuccess(t: OrderListResponse?) {
                baseActivity.dismissProgressBar()
                if(pageNo==1){
                    swipeRefreshLayout.isRefreshing=false
                    orderList.clear()
                }
                if(t != null && t.results.size>0){
                    PreferenceKeeper.getInstance().currentOrderCount=t.results.size
                    orderList.addAll(t.results)
                }

                try{
                    if(orderList.size>0){
                        setAdapter()
                        tv_no_record.visibility=View.GONE
                        rvOrder.visibility=View.VISIBLE
                    }else{
                        tv_no_record.visibility=View.VISIBLE
                        rvOrder.visibility=View.GONE
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
        rvOrder.setLayoutManager(mLayoutManager)
        rvOrder.setItemAnimator(DefaultItemAnimator())
        rvOrder.addOnScrollListener(object : EndlessRecyclerViewScrollListener(mLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                pageNo = page
                if(PreferenceKeeper.getInstance().driverStatus==1)
                    getOrderInProcessList(false)
            }

        })
    }

    private fun setAdapter(){
        if(adapterOrderInProcess != null && pageNo>1){
            adapterOrderInProcess!!.notifyDataSetChanged()
        }else{
            adapterOrderInProcess= AdapterOrderInProcess(baseActivity,orderList,this)
            rvOrder.adapter=adapterOrderInProcess
        }
    }


    override fun onResume() {
        super.onResume()
        pageNo=1
        paginationScrollListner()
        if(PreferenceKeeper.getInstance().driverStatus==1){
            tv_no_record.visibility=View.GONE
            getOrderInProcessList(false)
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
                var intent= Intent(baseActivity, OrderInProcessActivity::class.java)
                intent.putExtra(AppConstant.INTENT_EXTRAS.ORDER_POSITION,pos)
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
                PreferenceKeeper.getInstance().currentOrderCount=PreferenceKeeper.getInstance().currentOrderCount+1
                if(t != null && !TextUtils.isEmpty(t.results))
                    baseActivity.showToast(t.results)
                //DialogManager.showValidationDialog(baseActivity,t.results)
                //pageNo=1

                //  getPendingOrderList()
                // baseActivity.finish()
                val bundle=Bundle()
                bundle.putString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.IN_PROCESS_ORDER)
                var intent=Intent(baseActivity, HomeActivity::class.java)
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

    override fun getLayoutId(): Int {
        return 1
    }
    override fun onLayoutCreated(view: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}