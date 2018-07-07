package com.vlad.lesson6_maskaikin.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vlad.lesson6_maskaikin.Banner;
import com.vlad.lesson6_maskaikin.Fragment3ImageAndText;
import com.vlad.lesson6_maskaikin.R;

import java.util.ArrayList;

public class ImageFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Banner> banners;


    public ImageFragmentAdapter(FragmentManager fm) {
        super(fm);
        banners = new ArrayList<>();
        banners.add(new Banner(R.drawable.img_1, "Картинка 1"));
        banners.add(new Banner(R.drawable.img_2, "Картинка 2"));
        banners.add(new Banner(R.drawable.img_3, "Картинка 3"));

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return banners.get(position).getStringBanner();

    }

    @Override
    public Fragment getItem(int position) {
        return Fragment3ImageAndText.getInstance(banners.get(position));
    }

    @Override
    public int getCount() {
        return banners.size();
    }

}

