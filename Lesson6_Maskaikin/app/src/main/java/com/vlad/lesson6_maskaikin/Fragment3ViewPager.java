package com.vlad.lesson6_maskaikin;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vlad.lesson6_maskaikin.adapter.ImageFragmentAdapter;

import me.relex.circleindicator.CircleIndicator;


public class Fragment3ViewPager extends Fragment {


    public Fragment3ViewPager() {

    }

    public static Fragment3ViewPager getInstance() {
        Fragment3ViewPager fragment3ViewPager = new Fragment3ViewPager();
        return fragment3ViewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layoutView = inflater.inflate(R.layout.fragment_3_view_pager, container, false);

        WrapContentHeightViewPager viewPager = layoutView.findViewById(R.id.viewPager);
        CircleIndicator indicator = layoutView.findViewById(R.id.indicator);
        PagerAdapter pagerAdapter = new ImageFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);
        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        return layoutView;
    }


}
