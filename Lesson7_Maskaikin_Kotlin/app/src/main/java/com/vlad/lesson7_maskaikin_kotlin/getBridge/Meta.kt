package com.vlad.lesson7_maskaikin_kotlin.getBridge

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Meta {

    @SerializedName("limit")
    @Expose
    var limit: Int? = null
    @SerializedName("next")
    @Expose
    var next: Any? = null
    @SerializedName("offset")
    @Expose
    var offset: Int? = null
    @SerializedName("previous")
    @Expose
    var previous: Any? = null
    @SerializedName("total_count")
    @Expose
    var totalCount: Int? = null

}
