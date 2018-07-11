package com.vlad.lesson6_maskaikin_kotlin


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class Fragment3 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val layoutView = inflater.inflate(R.layout.fragment_3, container, false)
        val textViewFragment3 = layoutView.findViewById<TextView>(R.id.textViewFragment3)

        textViewFragment3.setOnClickListener(object : View.OnClickListener {
            var checkClick: Boolean = false

            override fun onClick(view: View) {
                if (!checkClick) {
                    childFragmentManager
                            .beginTransaction()
                            .add(R.id.containerInFragment3, Fragment3ViewPager.getInstance(), TAG_VIEW_PAGER)
                            .commit()
                    textViewFragment3.setText(R.string.collapse_banner)
                    checkClick = true
                } else {
                    val fragment = childFragmentManager.findFragmentByTag(TAG_VIEW_PAGER)
                    if (fragment != null) {
                        childFragmentManager.beginTransaction().remove(fragment).commit()
                    }
                    textViewFragment3.setText(R.string.show_banner)
                    checkClick = false
                }
            }
        })
        return layoutView
    }

    companion object {

        const val TAG_VIEW_PAGER = "view pager"


        fun getInstance() : Fragment3 {
            val fragment3 = Fragment3()
            return fragment3
        }
    }


}
