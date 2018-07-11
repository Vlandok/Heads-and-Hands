package com.vlad.lesson6_maskaikin_kotlin


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class Fragment2 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_2, container, false)
    }
companion object {
    fun getInstance(): Fragment2 {
        val fragment2 = Fragment2()
        return fragment2
    }
}

}
