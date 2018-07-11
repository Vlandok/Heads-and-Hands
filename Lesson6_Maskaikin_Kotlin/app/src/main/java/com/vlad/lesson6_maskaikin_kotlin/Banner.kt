package com.vlad.lesson6_maskaikin_kotlin

class Banner (private var imageBanner:Int, private var stringBanner: String)
{
    fun getImageBanner(): Int {
        return imageBanner
    }

    fun setImageBanner(imageBanner: Int) {
        this.imageBanner = imageBanner
    }

    fun getStringBanner(): String {
        return stringBanner
    }

    fun setStringBanner(stringBanner: String) {
        this.stringBanner = stringBanner
    }
}