package com.vlad.lesson6_maskaikin;

import android.widget.ImageView;
import android.widget.TextView;

public class Banner {

    private int imageBanner;
    private String stringBanner;

    public Banner(int imageBanner, String stringBanner) {
        this.imageBanner = imageBanner;
        this.stringBanner = stringBanner;
    }

    public int getImageBanner() {
        return imageBanner;
    }

    public void setImageBanner(int imageBanner) {
        this.imageBanner = imageBanner;
    }

    public String getStringBanner() {
        return stringBanner;
    }

    public void setStringBanner(String stringBanner) {
        this.stringBanner = stringBanner;
    }
}
