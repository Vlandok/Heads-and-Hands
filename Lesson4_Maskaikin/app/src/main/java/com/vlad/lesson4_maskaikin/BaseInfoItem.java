package com.vlad.lesson4_maskaikin;

public class BaseInfoItem {

    public int icon;

    public int getTitle() {
        return title;
    }

    public int title;

    BaseInfoItem(int icon, int title) {
        this.icon = icon;
        this.title = title;
    }


}

