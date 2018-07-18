package com.vlad.lesson7_maskaikin_kotlin.getBridge

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Object() : Parcelable {

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("description_eng")
    @Expose
    var descriptionEng: String? = null

    @SerializedName("divorces")
    @Expose
    var divorces: List<Divorce>? = null

    //    @SerializedName("divorces")
//    @Expose
//    var divorces: Parcelable? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("lat")
    @Expose
    var lat: Double? = null

    @SerializedName("lng")
    @Expose
    var lng: Double? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("name_eng")
    @Expose
    var nameEng: String? = null

    @SerializedName("photo_close")
    @Expose
    var photoClose: String? = null

    @SerializedName("photo_open")
    @Expose
    var photoOpen: String? = null

    @SerializedName("public")
    @Expose
    var public: Boolean? = null

    @SerializedName("resource_uri")
    @Expose
    var resourceUri: String? = null

    var checkAlarm : Boolean = false

    var checkTimeBeforeStartInMinute :Int? = null

    constructor(parcel: Parcel) : this() {
        description = parcel.readString()
        descriptionEng = parcel.readString()
        divorces = parcel.createTypedArrayList(Divorce)
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        lat = parcel.readValue(Double::class.java.classLoader) as? Double
        lng = parcel.readValue(Double::class.java.classLoader) as? Double
        name = parcel.readString()
        nameEng = parcel.readString()
        photoClose = parcel.readString()
        photoOpen = parcel.readString()
        public = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        resourceUri = parcel.readString()
        checkAlarm = parcel.readByte() != 0.toByte()
        checkTimeBeforeStartInMinute = parcel.readValue(Int::class.java.classLoader) as? Int
    }

//    constructor(parcel: Parcel) : this() {
//        description = parcel.readString()
//        descriptionEng = parcel.readString()
//        divorces = parcel.createTypedArrayList(Divorce)
//        id = parcel.readValue(Int::class.java.classLoader) as? Int
//        lat = parcel.readValue(Double::class.java.classLoader) as? Double
//        lng = parcel.readValue(Double::class.java.classLoader) as? Double
//        name = parcel.readString()
//        nameEng = parcel.readString()
//        photoClose = parcel.readString()
//        photoOpen = parcel.readString()
//        public = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
//        resourceUri = parcel.readString()
//        checkAlarm = parcel.readValue(Boolean::class.java.classLoader) as Boolean
//        checkTimeBeforeStartInMinute = parcel.readInt()
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(description)
//        parcel.writeString(descriptionEng)
//        parcel.writeTypedList(divorces)
//        parcel.writeValue(id)
//        parcel.writeValue(lat)
//        parcel.writeValue(lng)
//        parcel.writeString(name)
//        parcel.writeString(nameEng)
//        parcel.writeString(photoClose)
//        parcel.writeString(photoOpen)
//        parcel.writeValue(public)
//        parcel.writeString(resourceUri)
//        parcel.writeValue(checkAlarm)
//        checkTimeBeforeStartInMinute?.let { parcel.writeInt(it) }
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Object> {
//        override fun createFromParcel(parcel: Parcel): Object {
//            return Object(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Object?> {
//            return arrayOfNulls(size)
//        }
//    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(descriptionEng)
        parcel.writeTypedList(divorces)
        parcel.writeValue(id)
        parcel.writeValue(lat)
        parcel.writeValue(lng)
        parcel.writeString(name)
        parcel.writeString(nameEng)
        parcel.writeString(photoClose)
        parcel.writeString(photoOpen)
        parcel.writeValue(public)
        parcel.writeString(resourceUri)
        parcel.writeByte(if (checkAlarm) 1 else 0)
        parcel.writeValue(checkTimeBeforeStartInMinute)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Object> {
        override fun createFromParcel(parcel: Parcel): Object {
            return Object(parcel)
        }

        override fun newArray(size: Int): Array<Object?> {
            return arrayOfNulls(size)
        }
    }

}
