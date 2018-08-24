package com.vlad.lesson7_maskaikin_kotlin.getBridge

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResultBridge() : Parcelable {

    @SerializedName("meta")
    @Expose
    var meta: Meta? = null
    @SerializedName("objects")
    @Expose
    var objects: List<Object>? = null

    constructor(parcel: Parcel) : this() {
        objects = parcel.createTypedArrayList(Object)
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeTypedList(objects)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultBridge> {
        override fun createFromParcel(parcel: Parcel): ResultBridge {
            return ResultBridge(parcel)
        }

        override fun newArray(size: Int): Array<ResultBridge?> {
            return arrayOfNulls(size)
        }
    }

}
