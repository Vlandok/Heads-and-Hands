package com.vlad.lesson6_maskaikin_kotlin.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.vlad.lesson6_maskaikin_kotlin.Banner
import com.vlad.lesson6_maskaikin_kotlin.Fragment3ImageAndText
import com.vlad.lesson6_maskaikin_kotlin.R

import java.util.ArrayList

class ImageFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val banners: ArrayList<Banner>


    init {
        banners = ArrayList()
        banners.add(Banner(R.drawable.img_1, "Картинка 1"))
        banners.add(Banner(R.drawable.img_2, "Картинка 2"))
        banners.add(Banner(R.drawable.img_3, "Картинка 3"))

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return banners[position].stringBanner
    }

    override fun getItem(position: Int): Fragment {
        return Fragment3ImageAndText.getInstance(banners[position])
    }

    override fun getCount(): Int {
        return banners.size
    }

}

