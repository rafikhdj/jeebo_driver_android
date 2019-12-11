package com.app.jeebo.driver.modules.home.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseFragment
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.home.activity.OrderDeatilsActivity
import com.app.jeebo.driver.modules.home.adapter.AdapterCompletedOrders
import com.app.jeebo.driver.modules.home.adapter.PendingOrderAdapter
import com.app.jeebo.driver.modules.home.model.OrderListResponse
import com.app.jeebo.driver.modules.home.model.OrderListResult
import com.app.jeebo.driver.utils.AppConstant
import com.app.jeebo.driver.utils.DialogManager
import com.app.jeebo.driver.utils.EndlessRecyclerViewScrollListener
import com.app.jeebo.driver.utils.ItemClickListener
import kotlinx.android.synthetic.main.fragment_terminated_orders.*

class TerminatedOrdersFragment : BaseFragment(), ItemClickListener {

    private  var AdapterCompletedOrders:AdapterCompletedOrders?=null
    private lateinit var rvTerminatedOrder:RecyclerView
    private var pageNo:Int=1
    private var adapterPos:Int=0
    private var mLayoutManager:LinearLayoutManager?=null
    private var orderList=ArrayList<OrderListResult>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_terminated_orders, container, false)
        inIt(view)
        return view
    }

    private fun inIt(view: View) {
        rvTerminatedOrder = view.findViewById(R.id.rv_terminated_order) as RecyclerView
    }

    private fun getPendingOrderList(){
        baseActivity.showProgressBar(baseActivity)
        val request= ApiClient.getRequest()
        val call=request.getOrderList(AppConstant.COMPLETED_ORDER,pageNo.toString(),"10")
        call.enqueue(object : ApiCallback<OrderListResponse>(){
            override fun onSuccess(t: OrderListResponse?) {
                baseActivity.dismissProgressBar()
                if(pageNo==1){
                    orderList.clear()
                }
                if(t != null && t.results.size>0){
                    tv_no_record.visibility=View.GONE
                    orderList.addAll(t.results)
                }
                else{
                    if(pageNo==1){
                        tv_no_record.visibility=View.VISIBLE
                    }
                }
                setAdapter()
            }

            override fun onError(error: Error?) {
                baseActivity.dismissProgressBar()
                if(error != null && !TextUtils.isEmpty(error.errMsg))
                    DialogManager.showValidationDialog(baseActivity,error.errMsg)
            }

        })
    }

    private fun paginationScrollListner() {
        mLayoutManager = LinearLayoutManager(baseActivity)
        rvTerminatedOrder.setLayoutManager(mLayoutManager)
        rvTerminatedOrder.setItemAnimator(DefaultItemAnimator())
        rvTerminatedOrder.addOnScrollListener(object : EndlessRecyclerViewScrollListener(mLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                pageNo = page
                getPendingOrderList()
            }

        })
    }

    private fun setAdapter(){
        if(AdapterCompletedOrders != null && pageNo>1){
            AdapterCompletedOrders!!.notifyDataSetChanged()
        }else{
            AdapterCompletedOrders= AdapterCompletedOrders(baseActivity,orderList,this)
            rvTerminatedOrder.adapter=AdapterCompletedOrders
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
        getPendingOrderList()
    }
    override fun onItemClickListener(view: View?, pos: Int) {
        adapterPos=pos
        when(view?.id){
            R.id.rl_main->{
                var intent= Intent(baseActivity, OrderDeatilsActivity::class.java)
                intent.putExtra(AppConstant.INTENT_EXTRAS.ORDER_ID,orderList.get(pos).id.toString())
                intent.putExtra(AppConstant.INTENT_EXTRAS.CAME_FROM,AppConstant.INTENT_EXTRAS.COMPLETED)

                startActivity(intent)
            }
        }
    }


}
