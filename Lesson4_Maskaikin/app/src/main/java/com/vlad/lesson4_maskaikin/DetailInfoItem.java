package com.vlad.lesson4_maskaikin;

public class DetailInfoItem extends BaseInfoItem {
    public int description;
    public boolean needIntent;

    DetailInfoItem(int icon, int title, int description, boolean needIntent) {
        super(icon, title);
        this.description = description;
        this.needIntent = needIntent;
    }

    public boolean getNeedIntent() {
        return needIntent;
    }

}
