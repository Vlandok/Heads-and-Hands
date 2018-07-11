package com.vlad.lesson6_maskaikin_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity(), GetStringFromClickedImage {

    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        fragmentManager = supportFragmentManager

        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            mDrawerLayout.openDrawer(GravityCompat.START)
        }


        toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            val id = item.itemId

            when (id) {
                R.id.search_menu -> {
                    val toastSearchMenu = Toast.makeText(this@MainActivity, R.string.search, Toast.LENGTH_SHORT)
                    toastSearchMenu.show()
                    return@OnMenuItemClickListener true
                }
                R.id.extra_menu_item1 -> {
                    val toastExtraMenuItem1 = Toast.makeText(this@MainActivity, R.string.extra_menu_item_1, Toast.LENGTH_SHORT)
                    toastExtraMenuItem1.show()
                    return@OnMenuItemClickListener true
                }
                R.id.extra_menu_item2 -> {
                    val toastExtraMenuItem2 = Toast.makeText(this@MainActivity, R.string.extra_menu_item_2, Toast.LENGTH_SHORT)
                    toastExtraMenuItem2.show()
                    return@OnMenuItemClickListener true
                }
                R.id.extra_menu_item3 -> {
                    val toastExtraMenuItem3 = Toast.makeText(this@MainActivity, R.string.extra_menu_item_3, Toast.LENGTH_SHORT)
                    toastExtraMenuItem3.show()
                    return@OnMenuItemClickListener true
                }
            }
            false
        })

        navigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId

            when (id) {
                R.id.mainActivity -> {
                    mDrawerLayout.closeDrawers()
                    val toastActivityMain = Toast.makeText(this@MainActivity, R.string.activity_main, Toast.LENGTH_SHORT)
                    toastActivityMain.show()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.activity_2 -> {
                    mDrawerLayout.closeDrawers()
                    val toastActivity2 = Toast.makeText(this@MainActivity, R.string.activity_2, Toast.LENGTH_SHORT)
                    toastActivity2.show()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.activity_3 -> {
                    mDrawerLayout.closeDrawers()
                    val toastActivity3 = Toast.makeText(this@MainActivity, R.string.activity_3, Toast.LENGTH_SHORT)
                    toastActivity3.show()
                    return@OnNavigationItemSelectedListener true
                }
            }

            true
        })

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val id = item.itemId
            item.isChecked = true
            if (item.isChecked) {
                toolbar.title = item.title
            }

            fragmentTransaction = fragmentManager.beginTransaction()

            when (id) {
                R.id.bottom_navigation_item_one -> fragmentTransaction.replace(R.id.container, Fragment1.getInstance())
                R.id.bottom_navigation_item_two -> fragmentTransaction.replace(R.id.container, Fragment2.getInstance())
                R.id.bottom_navigation_item_three -> fragmentTransaction.replace(R.id.container, Fragment3.getInstance())
            }

            fragmentTransaction.commit()

            false
        }

        bottomNavigationView.selectedItemId = R.id.bottom_navigation_item_one


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        menuInflater.inflate(R.menu.extra_menu, menu)
        return true
    }

    override fun getStringFromClickedImage(data: String) {
        val toastTextImage = Toast.makeText(this, data, Toast.LENGTH_SHORT)
        toastTextImage.show()
    }
}
