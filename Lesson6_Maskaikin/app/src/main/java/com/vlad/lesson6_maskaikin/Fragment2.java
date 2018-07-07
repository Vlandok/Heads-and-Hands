package com.vlad.lesson6_maskaikin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Fragment2 extends Fragment {


    public Fragment2() {

    }

    public static Fragment2 getInstance() {
        Fragment2 fragment_2 = new Fragment2();
        return fragment_2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_2, container, false);
    }

}
