package com.app.jeebo.driver.modules.home.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.home.adapter.AdapterCancelledOrders
import com.app.jeebo.driver.modules.home.model.OrderListResponse
import com.app.jeebo.driver.modules.home.model.OrderListResult
import com.app.jeebo.driver.utils.AppConstant
import com.app.jeebo.driver.utils.EndlessRecyclerViewScrollListener
import com.app.jeebo.driver.utils.ItemClickListener
import kotlinx.android.synthetic.main.activity_cancelled_orders.*
import kotlinx.android.synthetic.main.fragment_terminated_orders.*
import kotlinx.android.synthetic.main.fragment_terminated_orders.tv_no_record

class CancelledOrdersActivity : BaseActivity(), ItemClickListener {

    private  var adapterCancelledOrders: AdapterCancelledOrders?=null
    private lateinit var rvOrders: RecyclerView
    private var pageNo:Int=1
    private var adapterPos:Int=0
    private var mLayoutManager: LinearLayoutManager?=null
    private var orderList=ArrayList<OrderListResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancelled_orders)
        inIt()
    }

    private fun inIt() {
        rvOrders = findViewById(R.id.rv_orders) as RecyclerView
        iv_back.setOnClickListener {
            finish()
            val bundle=Bundle()
            bundle.putString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.PENDING_ORDER)
            launchActivity(HomeActivity::class.java,bundle)}
    }

    private fun getPendingOrderList(){
        showProgressBar(this)
        val request= ApiClient.getRequest()
        val call=request.getOrderList(AppConstant.CANCELLED_ORDER,pageNo.toString(),"10")
        call.enqueue(object : ApiCallback<OrderListResponse>(){
            override fun onSuccess(t: OrderListResponse?) {
                dismissProgressBar()
                if(pageNo==1){
                    orderList.clear()
                }
                if(t != null && t.results.size>0){
                    tv_no_record.visibility= View.GONE
                    orderList.addAll(t.results)
                }
                else{
                    if(pageNo==1){
                        tv_no_record.visibility= View.VISIBLE
                    }
                }
                setAdapter()
            }

            override fun onError(error: Error?) {
                dismissProgressBar()
            }

        })
    }

    private fun paginationScrollListner() {
        mLayoutManager = LinearLayoutManager(this)
        rvOrders.setLayoutManager(mLayoutManager)
        rvOrders.setItemAnimator(DefaultItemAnimator())
        rvOrders.addOnScrollListener(object : EndlessRecyclerViewScrollListener(mLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                pageNo = page
                getPendingOrderList()
            }

        })
    }

    private fun setAdapter(){
        if(adapterCancelledOrders != null && pageNo>1){
            adapterCancelledOrders!!.notifyDataSetChanged()
        }else{
            adapterCancelledOrders= AdapterCancelledOrders(this@CancelledOrdersActivity,orderList,this)
            rvOrders.adapter=adapterCancelledOrders
        }
    }

    override fun onResume() {
        super.onResume()
        pageNo=1
        paginationScrollListner()
        getPendingOrderList()
    }
    override fun onItemClickListener(view: View?, pos: Int) {
        when(view?.id){
            R.id.rl_main->{
                var intent= Intent(this, OrderDeatilsActivity::class.java)
                intent.putExtra(AppConstant.INTENT_EXTRAS.ORDER_ID,orderList.get(pos).id.toString())
                intent.putExtra(AppConstant.INTENT_EXTRAS.CAME_FROM,AppConstant.INTENT_EXTRAS.CANCELLED)

                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        finish()
        val bundle=Bundle()
        bundle.putString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.PENDING_ORDER)
        launchActivity(HomeActivity::class.java,bundle)
    }

}
