package com.vlad.lesson6_maskaikin_kotlin


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class Fragment1 : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_1, container, false)
    }
    companion object {
        fun getInstance(): Fragment1 {
            val fragment1 = Fragment1()
            return fragment1
        }
    }



}
