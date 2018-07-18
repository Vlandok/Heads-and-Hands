package com.vlad.lesson7_maskaikin_kotlin.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.vlad.lesson7_maskaikin_kotlin.Activity2
import com.vlad.lesson7_maskaikin_kotlin.AlarmUtils

import com.vlad.lesson7_maskaikin_kotlin.R
import com.vlad.lesson7_maskaikin_kotlin.adapters.RVAdapter
import com.vlad.lesson7_maskaikin_kotlin.getBridge.Object
import com.vlad.lesson7_maskaikin_kotlin.getBridge.ResultBridge
import com.vlad.lesson7_maskaikin_kotlin.retrofit.ServiceBridge
import com.vlad.lesson7_maskaikin_kotlin.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*


class FragmentList : Fragment() {

    private lateinit var jsonApi: ServiceBridge
    private lateinit var disposable: Disposable


    companion object {
        const val BRIDGE = "bridge"
        const val CHECK_ALARM = "check alarm from fragment"
        const val TEXT_FOR_VIEW_ALARM = "text_from_fragment"

        fun getInstance(): FragmentList {
            val fragmentList = FragmentList()
            return fragmentList
        }

        fun getInstance(idBridge: Int, checkAlarm: Boolean, text: String?): FragmentList {
            val fragmentList = FragmentList()
            val bundle = Bundle()
            bundle.putInt(Activity2.ID_BRIDGE, idBridge)
            bundle.putBoolean(Activity2.CHECK_ALARM, checkAlarm)
            bundle.putString (Activity2.TEXT_FOR_VIEW_ALARM, text)
            fragmentList.arguments = bundle

            return fragmentList
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val mContext = activity

        val layoutView: View = inflater.inflate(R.layout.fragment_list, container, false)

        val retrofit = RetrofitClient.instance
        jsonApi = retrofit.create(ServiceBridge::class.java)


        return layoutView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var idBridge = this.arguments?.getInt(Activity2.ID_BRIDGE)
        var checkAlarm = this.arguments?.getBoolean(Activity2.CHECK_ALARM)
        var text = this.arguments?.getString(Activity2.TEXT_FOR_VIEW_ALARM)

        recyclerViewListBridges.layoutManager = LinearLayoutManager(activity)

        disposable = jsonApi.bridges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { bridges ->
//                            var i = 0
//                            while ( i < bridges.objects?.size ?: 1) {
//                                var aa = Activity2()
//                                val msgText = "sss"
//                                 var intent = aa.createIntent(msgText, bridges.objects?.get(i)?.name, bridges.objects?.get(i)?.id)
//                                bridges.objects?.get(i)?.checkAlarm = AlarmUtils.hasAlarm(context , intent , requestCode = )
//                                i++
//                            }
                            displayData(bridges, idBridge, checkAlarm,text)
                            progressBar.visibility = View.INVISIBLE
                        },
                        { exception ->
                            Log.e("ERROR", "error", exception)
                            progressBar.visibility = View.INVISIBLE
                        }
                )


    }

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }

    private fun displayData(bridges: ResultBridge, idBridge: Int?, checkAlarm: Boolean?, text: String?) {
        val adapter = RVAdapter(bridges, idBridge, checkAlarm)
        recyclerViewListBridges.adapter = adapter

        adapter.setOnItemClickListener(object : RVAdapter.OnItemClickListener {
            override fun onClickItem(bridge: Object?) {
                val intent = Intent(activity, Activity2::class.java)
                intent.putExtra(BRIDGE, bridge)
                intent.putExtra(CHECK_ALARM, checkAlarm)
                intent.putExtra(TEXT_FOR_VIEW_ALARM, text)
                startActivity(intent)
            }
        })

    }
}

