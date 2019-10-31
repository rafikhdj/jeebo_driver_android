package com.app.jeebo.driver.modules.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.app.jeebo.driver.R
import com.app.jeebo.driver.base.BaseFragment
import com.app.jeebo.driver.modules.home.adapter.PendingOrderAdapter
import kotlinx.android.synthetic.main.fragment_pending_orders.*


class PendingOrdersFragment : BaseFragment() {

    private lateinit  var adapterPendingOrders:PendingOrderAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pending_orders, container, false)
       // inIt()
        return view
    }

    private fun inIt() {
        rv_pending_order.layoutManager=LinearLayoutManager(baseActivity)
        adapterPendingOrders= PendingOrderAdapter()
        rv_pending_order.adapter=adapterPendingOrders
    }


    override fun getLayoutId(): Int {
        return 0
    }

    override fun onLayoutCreated(view: View?) {

    }


}
