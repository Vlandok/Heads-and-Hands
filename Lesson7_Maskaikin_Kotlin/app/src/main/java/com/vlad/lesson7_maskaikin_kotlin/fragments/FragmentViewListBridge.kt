package com.vlad.lesson7_maskaikin_kotlin.fragments


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vlad.lesson7_maskaikin_kotlin.InfoSetReminderActivity
import com.vlad.lesson7_maskaikin_kotlin.R
import com.vlad.lesson7_maskaikin_kotlin.adapters.RVAdapter
import com.vlad.lesson7_maskaikin_kotlin.checkAlarmBridge
import com.vlad.lesson7_maskaikin_kotlin.getBridge.Object
import com.vlad.lesson7_maskaikin_kotlin.getBridge.ResultBridge
import com.vlad.lesson7_maskaikin_kotlin.retrofit.ServiceBridge
import com.vlad.lesson7_maskaikin_kotlin.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_view_list_bridge.*


class FragmentViewListBridge : Fragment() {

    private lateinit var jsonApi: ServiceBridge
    private lateinit var disposable: Disposable


    companion object {
        const val BRIDGE = "bridge"

        fun getInstance(): FragmentViewListBridge {
            return FragmentViewListBridge()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val layoutView: View = inflater.inflate(R.layout.fragment_view_list_bridge, container, false)

        val retrofit = RetrofitClient.instance
        jsonApi = retrofit.create(ServiceBridge::class.java)

        return layoutView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerViewListBridges.layoutManager = LinearLayoutManager(activity)

        loadBridgeDisposableForListFragment()
    }

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }

    private fun displayData(bridges: ResultBridge) {
        val adapter = RVAdapter(bridges)
        recyclerViewListBridges.adapter = adapter

        adapter.setOnItemClickListener(object : RVAdapter.OnItemClickListener {
            override fun onClickItem(bridge: Object?) {
                val intent = Intent(activity, InfoSetReminderActivity::class.java)
                intent.putExtra(BRIDGE, bridge)
                startActivity(intent)
            }
        })

    }

    private fun loadBridgeDisposableForListFragment() {
        disposable = jsonApi.bridges()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { bridges ->
                            checkAlarmBridge(bridges, context)
                            displayData(bridges)
                            progressBar.visibility = View.INVISIBLE
                        },
                        { exception ->
                            exception.printStackTrace()
                            progressBar.visibility = View.INVISIBLE

                            val snackbarError = Snackbar.make(linerLayoutMainActivityParent,
                                    R.string.errorGetBridgesFromRx,
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction(getText(R.string.yes)) {
                                        loadBridgeDisposableForListFragment()
                                    }
                            snackbarError.show()

                        }
                )
    }

}

