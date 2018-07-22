package com.vlad.lesson8_maskaikin_kotlin.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "notes")
class Note(
           @ColumnInfo(name = "title") var title: String,
           @ColumnInfo(name = "text") var text: String,
           @ColumnInfo(name = "background_color") var backgroundColor: String,
           @ColumnInfo(name = "archive") var isArchive: Boolean = false) {
    @PrimaryKey (autoGenerate = true) var id: Int = 0
}