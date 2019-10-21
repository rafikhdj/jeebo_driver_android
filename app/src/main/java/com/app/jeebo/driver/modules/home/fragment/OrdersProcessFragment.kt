package com.app.jeebo.driver.modules.home.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.app.jeebo.driver.R
import com.app.jeebo.driver.base.BaseFragment

open class OrdersProcessFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders_process, container, false)
    }
    override fun getLayoutId(): Int {
        return 1
    }

    override fun onLayoutCreated(view: View?) {

    }


}
