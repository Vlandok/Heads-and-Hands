package com.vlad.lesson7_maskaikin_kotlin.fragments


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vlad.lesson7_maskaikin_kotlin.InfoSetReminderActivity
import com.vlad.lesson7_maskaikin_kotlin.MainActivity.Companion.MY_LOG
import com.vlad.lesson7_maskaikin_kotlin.R
import com.vlad.lesson7_maskaikin_kotlin.adapters.RVAdapter
import com.vlad.lesson7_maskaikin_kotlin.checkAlarmBridge
import com.vlad.lesson7_maskaikin_kotlin.getBridge.Object
import com.vlad.lesson7_maskaikin_kotlin.getBridge.ResultBridge
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_view_list_bridge.*


class FragmentViewListBridge : Fragment() {

    private var bridges: ResultBridge? = null


    companion object {
        const val BRIDGE = "bridge"
        const val ARGUMENT_BRIDGES = "argument_bridges"

        fun getInstance(bridges: ResultBridge?): FragmentViewListBridge {

            val fragmentViewListBridge = FragmentViewListBridge()
            val arguments = Bundle()
            arguments.putParcelable(ARGUMENT_BRIDGES, bridges)
            fragmentViewListBridge.arguments = arguments
            return fragmentViewListBridge
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.getParcelable<ResultBridge>(ARGUMENT_BRIDGES)?.objects?.isNotEmpty() != null) {
            Log.d(MY_LOG, "Получилось")
            bridges = arguments!!.getParcelable(ARGUMENT_BRIDGES)
        } else {
            Log.d(MY_LOG, "Не получилось")

            progressBar.visibility = View.INVISIBLE

            val snackbarError = Snackbar.make(linerLayoutMainActivityParent,
                    R.string.errorGetBridgesFromRx,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getText(R.string.yes)) {
                        onCreate(savedInstanceState)
                    }
            snackbarError.show()

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_view_list_bridge, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerViewListBridges.layoutManager = LinearLayoutManager(activity)

        bridges?.let { checkAlarmBridge(it, context) }
        bridges?.let { displayData(it) }
        progressBar.visibility = View.INVISIBLE
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
}

