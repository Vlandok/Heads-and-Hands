package com.vlad.lesson7_maskaikin_kotlin.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.vlad.lesson7_maskaikin_kotlin.R


class FragmentMap : Fragment() {

    companion object {
        fun getInstance () : FragmentMap {
            val fragmentMap = FragmentMap()
            return fragmentMap
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }


}
