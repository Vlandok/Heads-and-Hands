package com.vlad.lesson11_maskaikin_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var visitersArray = ArrayList<Visiter>()

    companion object {
        const val MAX_TIME_VISITE = 100
        const val ONE_DAY_IN_MILLISECONDS = 86400000
        const val NUM_DAYS_VISITERS = 14
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createVisitorsArray(NUM_DAYS_VISITERS)
        myCustomView.setData(visitersArray)
    }

    private fun createVisitorsArray(numDaysVisiters: Int) {
        val today = System.currentTimeMillis()
        var i = 0
        while (i < numDaysVisiters) {
            visitersArray.add(i, (Visiter(today - ONE_DAY_IN_MILLISECONDS * i)))
            i++
        }
    }
}
