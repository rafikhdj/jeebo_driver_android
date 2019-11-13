package com.app.jeebo.driver.modules.home.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.home.model.DriverStatsReq
import com.app.jeebo.driver.modules.home.model.DriverStatsResponse
import com.app.jeebo.driver.modules.home.model.DriverStatsResult
import com.app.jeebo.driver.utils.DialogManager
import com.app.jeebo.driver.utils.PreferenceKeeper
import kotlinx.android.synthetic.main.activity_stats.*

class StatsActivity : BaseActivity() {

    private var spinnerMonths:Spinner?=null
    private var month:Int=1
    private var spinnerArray = arrayOf("January", "February", "March", "April", "May","June","July","August","September",
                                        "October","November","December")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        init()
    }

    private fun init(){
        val spinnerArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_months.adapter = spinnerArrayAdapter

        tv_driver_name.setText(PreferenceKeeper.getInstance().name)

        iv_back.setOnClickListener { finish() }

        /*spinnerSubCat?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                subCatid=cancelSubCatList.get(position).id
            }

        }*/

        spinner_months.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                month=p2+1
                getDriverStats()
            }

        }

        getDriverStats()

    }

    private fun getDriverStats(){
        showProgressBar(this)
        val request=ApiClient.getRequest()
        var driverStatsReq=DriverStatsReq()
        driverStatsReq.month=month
        val call=request.getDriverStats(driverStatsReq)
        call.enqueue(object:ApiCallback<DriverStatsResponse>(){
            override fun onSuccess(t: DriverStatsResponse?) {
                dismissProgressBar()
                if(t!=null && t.results != null)
                setStats(t.results)
            }

            override fun onError(error: Error?) {
                dismissProgressBar()
                if(error != null && !TextUtils.isEmpty(error.errMsg))
                    DialogManager.showValidationDialog(this@StatsActivity,error.errMsg)
            }

        })
    }

    private fun setStats(statsResut:DriverStatsResult){
        if(statsResut.earnings != null){
            tv_earning.setText("DA "+statsResut.earnings)
        }else{
            tv_earning.setText("DA 0.0")
        }
        if(statsResut.return_to_jeebo != null){
            tv_return.setText("DA "+statsResut.return_to_jeebo)
        }else{
            tv_return.setText("DA 0.0")
        }

    }
}
