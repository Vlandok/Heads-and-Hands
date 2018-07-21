package com.vlad.lesson8_maskaikin_kotlin.datebase

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.vlad.lesson8_maskaikin_kotlin.dao.NoteDao
import com.vlad.lesson8_maskaikin_kotlin.entity.Note

@Database(entities = [(Note::class)], version = 1, exportSchema = false)

abstract class AppDatabase: RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
}