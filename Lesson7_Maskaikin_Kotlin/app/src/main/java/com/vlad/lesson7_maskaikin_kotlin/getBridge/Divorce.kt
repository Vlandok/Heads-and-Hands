package com.vlad.lesson7_maskaikin_kotlin.getBridge

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


class Divorce() : Parcelable {

    @SerializedName("end")
    @Expose
    var end: Date? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("start")
    @Expose
    var start: Date? = null

    constructor(parcel: Parcel) : this() {
        end = Date(parcel.readLong())
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        start = Date(parcel.readLong())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(end?.time!!)
        parcel.writeValue(id)
        parcel.writeLong(start?.time!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Divorce> {
        override fun createFromParcel(parcel: Parcel): Divorce {
            return Divorce(parcel)
        }

        override fun newArray(size: Int): Array<Divorce?> {
            return arrayOfNulls(size)
        }
    }


}
