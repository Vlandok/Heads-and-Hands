package com.vlad.lesson8_maskaikin_kotlin

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import com.vlad.lesson8_maskaikin_kotlin.datebase.AppDatabase
import com.vlad.lesson8_maskaikin_kotlin.entity.Note
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.appbar_with_toolbar.*
import com.vlad.lesson8_maskaikin_kotlin.MainActivity.Companion.WHITE_COLOR_HEX
import com.vlad.lesson8_maskaikin_kotlin.datebase.App
import io.reactivex.Completable
import petrov.kristiyan.colorpicker.ColorPicker


class AddNoteActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var title: String
    private lateinit var text: String
    private var idNote: Int = FAIL_GET_ID_EXTRA
    private var backgroundColorNote = WHITE_COLOR_HEX
    private lateinit var note: Note

    companion object {

        const val NUM_COLUMNS_COLOR_PICKER = 4
        const val FAIL_GET_ID_EXTRA = -1

        fun createStartIntent(context: Context): Intent {
            return Intent(context, AddNoteActivity::class.java)
        }

        fun createStartIntentWithExtra(context: Context, constant: String, idNote: Int): Intent {
            val intent = Intent(context, AddNoteActivity::class.java)
            intent.putExtra(constant, idNote)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        toolbar.setNavigationIcon(R.drawable.ic_arrow)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        db = App.instance.database

        idNote = intent.getIntExtra(MainActivity.ID_NOTE, FAIL_GET_ID_EXTRA)

        toolbar.setOnMenuItemClickListener {
            openColorPicker()
            return@setOnMenuItemClickListener true
        }

        toolbar.setNavigationOnClickListener {
            comeBack()
            finish()
        }

        if (idNote != FAIL_GET_ID_EXTRA) {
            note = db.getNoteDao().getNoteId(idNote)
            editTextAddTitleNote.setText(note.title)
            editTextAddTextNote.setText(note.text)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.color_menu, menu)
        return true
    }

    override fun onBackPressed() {
        comeBack()
        super.onBackPressed()
    }

    private fun comeBack() {

        text = editTextAddTextNote.text.toString()
        title = editTextAddTitleNote.text.toString()

        if (idNote != FAIL_GET_ID_EXTRA) {
            Completable.fromCallable { db.getNoteDao().updateTextAndTitleNote(idNote, text, title, backgroundColorNote) }
                    .subscribe()
        } else if (title.isNotEmpty() || text.isNotEmpty()) {
            note = Note(title, text, backgroundColorNote, false)
            Completable.fromCallable { db.getNoteDao().insertAllNote(note) }
                    .subscribe()
        }

    }

    private fun openColorPicker () {
        val colorPicker = ColorPicker(this)
        val colors = ArrayList<String> ()
        colors.add("#e51c23")
        colors.add("#e91e63")
        colors.add("#9c27b0")
        colors.add("#673ab7")
        colors.add("#5677fc")
        colors.add("#02a9f4")
        colors.add("#00bcd4")
        colors.add("#009788")
        colors.add("#8cc34a")
        colors.add("#cddc38")
        colors.add("#ffeb3c")
        colors.add("#fec10a")
        colors.add("#ff5623")
        colors.add("#9e9e9e")
        colors.add("#607d8b")



        colorPicker.setColors(colors)
                .setColumns(NUM_COLUMNS_COLOR_PICKER)
                .setRoundColorButton(true)
                .setTitle(getString(R.string.choose_color_alert))
                .setOnChooseColorListener(object :ColorPicker.OnChooseColorListener {
                    override fun onChooseColor(position: Int, color: Int) {
                        backgroundColorNote = colors[position]
                    }

                    override fun onCancel() {
                        backgroundColorNote = WHITE_COLOR_HEX
                    }

                })
                .show()
        colorPicker.positiveButton.text = getText(R.string.ok)
        colorPicker.negativeButton.text = getText(R.string.cancel)
    }

}



