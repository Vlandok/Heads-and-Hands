package com.vlad.lesson8_maskaikin_kotlin.dao

import android.arch.persistence.room.*
import com.vlad.lesson8_maskaikin_kotlin.entity.Note
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface NoteDao {

    // Добавление Note в бд
    @Insert
    fun insertAllNote(vararg note: Note)

    //Запрос на изменение (Архивирование) Note в бд по id
    @Query("UPDATE notes SET check_archive = :archive WHERE id LIKE :idNote")
    fun updateArchiveNote(idNote: Int, archive: Boolean)

    //Запрос на удаление Note по id
    @Query("DELETE from notes WHERE id LIKE :idNote")
    fun deleteNote(idNote: Int)

    //Запрос на изменение (Архивирование) Note в бд по id
    @Query("UPDATE notes SET text = :text, title = :title, background_color = :backgroundColor WHERE id LIKE :idNote")
    fun updateTextAndTitleNote(idNote: Int, text: String, title: String, backgroundColor: String)

    // Получение всех Note из бд
    @Query("SELECT * FROM notes")
    fun getAllNote(): Flowable<List<Note>>

    // Получение Note из бд с по id
    @Query("SELECT * FROM notes WHERE id LIKE :idNote")
    fun getNoteId(idNote: Int): Note

    // Получение всех Note из бд с уловием ахивирования
    @Query("SELECT * FROM notes WHERE check_archive LIKE 0")
    fun getAllNoteWithOutArchive(): Flowable<List<Note>>

    // Получение всех Note из бд с условием из поиска
    @Query("SELECT * FROM notes WHERE title LIKE :searchText OR text LIKE :searchText AND check_archive LIKE 0")
    fun getAllNoteWithSearchTextWithOutArchive(searchText: String): Flowable<List<Note>>

}