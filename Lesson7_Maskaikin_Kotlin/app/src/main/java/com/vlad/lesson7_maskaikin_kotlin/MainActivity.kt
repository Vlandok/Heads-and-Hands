package com.vlad.lesson7_maskaikin_kotlin

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.vlad.lesson7_maskaikin_kotlin.fragments.FragmentViewListBridge
import com.vlad.lesson7_maskaikin_kotlin.fragments.FragmentViewMapBridge
import kotlinx.android.synthetic.main.activity_main.*
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.util.Log
import com.vlad.lesson7_maskaikin_kotlin.getBridge.ResultBridge
import com.vlad.lesson7_maskaikin_kotlin.retrofit.RetrofitClient
import com.vlad.lesson7_maskaikin_kotlin.retrofit.ServiceBridge
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var myToolbar: Toolbar
    private lateinit var jsonApi: ServiceBridge
    private var disposable: Disposable? = null
    private var bridges: ResultBridge? = null

    companion object {
        const val MY_LOG = "My_Log"
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = RetrofitClient.instance
        jsonApi = retrofit.create(ServiceBridge::class.java)



        myToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(myToolbar)
        val myFragmentManager = supportFragmentManager

        val checkAlarm = intent.getBooleanExtra(InfoSetReminderActivity.CHECK_ALARM, false)
        val idBridge = intent.getIntExtra(InfoSetReminderActivity.ID_BRIDGE, -1)

        loadBridgeDisposable(myFragmentManager,checkAlarm,idBridge)


//        when {
//            checkAlarm -> {
//                showSnackbar(getString(R.string.reminder_set))
//                myFragmentManager.beginTransaction().replace(R.id.container, FragmentViewListBridge.getInstance(bridges)).commit()
//            }
//            idBridge != -1 -> {
//                showSnackbar(getString(R.string.reminder_cancel))
//                myFragmentManager.beginTransaction().replace(R.id.container, FragmentViewListBridge.getInstance(bridges)).commit()
//            }
//            else -> myFragmentManager.beginTransaction().replace(R.id.container, FragmentViewListBridge.getInstance(bridges)).commit()
//        }

        myToolbar.setOnMenuItemClickListener { item ->

            fragmentTransaction = myFragmentManager.beginTransaction()

            when (item.itemId) {
                R.id.map_menu -> {
                    loadFragment(item, FragmentViewMapBridge.getInstance(bridges), R.id.list_menu)
                }
                R.id.list_menu -> {
                    loadFragment(item, FragmentViewListBridge.getInstance(bridges), R.id.map_menu)
                }
            }
            fragmentTransaction.commit()

            return@setOnMenuItemClickListener false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }

    private fun showSnackbar(text: String) {
        val snackbarAlert = Snackbar.make(linerLayoutMainActivityParent, text, Snackbar.LENGTH_LONG)
        snackbarAlert.show()
    }

    private fun loadFragment(item: MenuItem, fragment: Fragment, itemIdVisible: Int) {

        fragmentTransaction.replace(R.id.container, fragment)
        item.isVisible = false
        myToolbar.menu.findItem(itemIdVisible).isVisible = true

    }

    private fun loadBridgeDisposable(myFragmentManager: FragmentManager , checkAlarm : Boolean , idBridge : Int) {
        disposable = jsonApi.bridges()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { loadBridges -> bridges = loadBridges
                    Log.d(MY_LOG, bridges.toString())

                    when {
                        checkAlarm -> {
                            showSnackbar(getString(R.string.reminder_set))
                            myFragmentManager.beginTransaction().replace(R.id.container, FragmentViewListBridge.getInstance(bridges)).commit()
                        }
                        idBridge != -1 -> {
                            showSnackbar(getString(R.string.reminder_cancel))
                            myFragmentManager.beginTransaction().replace(R.id.container, FragmentViewListBridge.getInstance(bridges)).commit()
                        }
                        else -> myFragmentManager.beginTransaction().replace(R.id.container, FragmentViewListBridge.getInstance(bridges)).commit()
                    }

                }
    }
}

