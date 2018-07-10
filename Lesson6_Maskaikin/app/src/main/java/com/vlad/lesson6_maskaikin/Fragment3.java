package com.vlad.lesson6_maskaikin;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Fragment3 extends Fragment {

    public final static String TAG_VIEW_PAGER = "view pager";

    public Fragment3() {

    }

    public static Fragment3 getInstance() {
        Fragment3 fragment3 = new Fragment3();
        return fragment3;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layoutView = inflater.inflate(R.layout.fragment_3, container, false);
        final TextView textViewFragment3 = layoutView.findViewById(R.id.textViewFragment3);

        textViewFragment3.setOnClickListener(new View.OnClickListener() {
            Boolean checkClick = false;

            @Override
            public void onClick(View view) {
                if (!checkClick) {
                    getChildFragmentManager()
                            .beginTransaction()
                            .add(R.id.containerInFragment3, Fragment3ViewPager.getInstance(), TAG_VIEW_PAGER)
                            .commit();
                    textViewFragment3.setText(R.string.collapse_banner);
                    checkClick = true;
                } else {
                    Fragment fragment = getChildFragmentManager().findFragmentByTag(TAG_VIEW_PAGER);
                    if (fragment != null) {
                        getChildFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    textViewFragment3.setText(R.string.show_banner);
                    checkClick = false;
                }
            }
        });
        return layoutView;
    }


}
