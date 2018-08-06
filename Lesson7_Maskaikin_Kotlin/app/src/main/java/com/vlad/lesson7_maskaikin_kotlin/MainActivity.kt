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

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var myToolbar: Toolbar

    companion object {
        const val MY_LOG = "My_Log"
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myToolbar = findViewById (R.id.toolbar)
        setSupportActionBar(myToolbar)
        val myFragmentManager = supportFragmentManager

        val checkAlarm = intent.getBooleanExtra(InfoSetReminderActivity.CHECK_ALARM, false)
        val idBridge = intent.getIntExtra(InfoSetReminderActivity.ID_BRIDGE, -1)


        when {
            checkAlarm -> {
                showSnackbar(getString(R.string.reminder_set))
                myFragmentManager.beginTransaction().replace(R.id.container, FragmentViewListBridge.getInstance()).commit()
            }
            idBridge != -1 -> {
                showSnackbar(getString(R.string.reminder_cancel))
                myFragmentManager.beginTransaction().replace(R.id.container, FragmentViewListBridge.getInstance()).commit()
            }
            else -> myFragmentManager.beginTransaction().replace(R.id.container, FragmentViewListBridge.getInstance()).commit()
        }

        myToolbar.setOnMenuItemClickListener { item ->

            fragmentTransaction = myFragmentManager.beginTransaction()

            when (item.itemId) {
                R.id.map_menu -> {
                        loadFragment(item, FragmentViewMapBridge.getInstance(), R.id.list_menu)
                }
                R.id.list_menu -> {
                        loadFragment(item, FragmentViewListBridge.getInstance(), R.id.map_menu)
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

    private fun showSnackbar (text: String){
        val snackbarAlert = Snackbar.make(linerLayoutMainActivityParent, text, Snackbar.LENGTH_LONG)
        snackbarAlert.show()
    }

    private fun loadFragment(item: MenuItem, fragment: Fragment, itemIdVisible: Int) {

        fragmentTransaction.replace(R.id.container, fragment)
        item.isVisible = false
        myToolbar.menu.findItem(itemIdVisible).isVisible = true

    }
}

