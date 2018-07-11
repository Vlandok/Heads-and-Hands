package com.vlad.lesson6_maskaikin_kotlin


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.vlad.lesson6_maskaikin_kotlin.adapter.ImageFragmentAdapter
import me.relex.circleindicator.CircleIndicator


class Fragment3ViewPager : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val layoutView = inflater.inflate(R.layout.fragment_3_view_pager, container, false)

        val viewPager = layoutView.findViewById<WrapContentHeightViewPager>(R.id.viewPager)
        val indicator = layoutView.findViewById<CircleIndicator>(R.id.indicator)
        val pagerAdapter = ImageFragmentAdapter(childFragmentManager)
        viewPager.adapter = pagerAdapter
        indicator.setViewPager(viewPager)
        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        return layoutView
    }

    companion object {
        fun getInstance() :Fragment3ViewPager {
            val fragment3ViewPager = Fragment3ViewPager()
            return fragment3ViewPager
        }
    }


}
