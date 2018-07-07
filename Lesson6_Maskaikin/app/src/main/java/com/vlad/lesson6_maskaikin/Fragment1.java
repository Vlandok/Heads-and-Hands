package com.vlad.lesson6_maskaikin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Fragment1 extends Fragment {


    public Fragment1() {

    }

    public static Fragment1 getInstance() {
        Fragment1 fragment_1 = new Fragment1();
        return fragment_1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_1, container, false);
    }

}
