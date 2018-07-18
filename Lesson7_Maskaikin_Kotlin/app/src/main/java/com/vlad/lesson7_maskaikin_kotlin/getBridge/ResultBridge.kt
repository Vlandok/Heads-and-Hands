package com.vlad.lesson7_maskaikin_kotlin.getBridge

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResultBridge {

    @SerializedName("meta")
    @Expose
    var meta: Meta? = null
    @SerializedName("objects")
    @Expose
    var objects: List<Object>? = null
    //var objects: List<Object>? = null

}
