package com.vlad.lesson8_maskaikin_kotlin


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import com.vlad.lesson8_maskaikin_kotlin.datebase.AppDatabase
import android.content.DialogInterface
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import com.vlad.lesson8_maskaikin_kotlin.adapters.NotesAdapter
import com.vlad.lesson8_maskaikin_kotlin.entity.Note
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.appbar_with_toolbar.*
import android.support.v7.widget.SearchView
import com.vlad.lesson8_maskaikin_kotlin.datebase.App


class MainActivity : AppCompatActivity() {

    private lateinit var disposableNotes: Disposable
    private lateinit var disposableNotesFromSearch: Disposable
    private lateinit var db: AppDatabase

    companion object {
        const val MY_LOG = "My_Log"
        const val NUM_COLUMNS = 2
        const val ID_NOTE = "id_note"
        const val WHITE_COLOR_HEX = "#FFFFFF"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        fabAddNote.setOnClickListener {
            startActivity(AddNoteActivity.createStartIntent(this))
        }

        db = App.instance.database

        getNotesFromSqlRx()

    }

    override fun onDestroy() {
        if (!disposableNotes.isDisposed) {
            disposableNotes.dispose()
        }
        if (!disposableNotesFromSearch.isDisposed) {
            disposableNotes.dispose()
        }
        super.onDestroy()
    }

    private fun getNotesFromSqlRx() {

        disposableNotes = db.getNoteDao().getAllNoteWithOutArchive()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { notes ->
                            if (notes.isEmpty()) {
                                textViewNotesSqlEmpty.visibility = View.VISIBLE
                            }else {
                                textViewNotesSqlEmpty.visibility = View.INVISIBLE
                            }
                            displayNotes(notes)
                            progressBarActivityMain.visibility = View.INVISIBLE
                        }
                ) { exception ->
                    Log.e(MY_LOG, getString(R.string.error), exception)
                    progressBarActivityMain.visibility = View.INVISIBLE

                    val snackbarError = Snackbar.make(linerLayoutMainActivityParent,
                            getText(R.string.error_get_notes_from_sql_rx),
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(getText(R.string.yes)) {
                                getNotesFromSqlRx()
                            }
                    snackbarError.show()
                }
    }

    private fun getNotesFromSqlRxWithSearch(newText: String) {

        disposableNotesFromSearch = db.getNoteDao().getAllNoteWithSearchTextWithOutArchive(newText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { notes ->
                            if (notes.isEmpty()) {
                                textViewNotesSqlEmpty.text = getString(R.string.notes_sql_empty_for_search)
                                textViewNotesSqlEmpty.visibility = View.VISIBLE
                            } else {
                                textViewNotesSqlEmpty.visibility = View.INVISIBLE
                            }
                            displayNotes(notes)
                            progressBarActivityMain.visibility = View.INVISIBLE
                        }
                ) { exception ->
                    Log.e(MY_LOG, getString(R.string.error), exception)
                    progressBarActivityMain.visibility = View.INVISIBLE

                    val snackbarError = Snackbar.make(linerLayoutMainActivityParent,
                            getText(R.string.error_get_notes_from_sql_rx_with_search),
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(getText(R.string.yes)) {
                                getNotesFromSqlRxWithSearch(newText)
                            }
                    snackbarError.show()
                }
    }

    private fun displayNotes(notes: List<Note>) {

        val adapter = NotesAdapter(notes)

        val StaggeredGridLayoutManagerForNotes = StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL)
        recyclerViewNotes.layoutManager = StaggeredGridLayoutManagerForNotes
        recyclerViewNotes.adapter = adapter

        adapter.setOnItemLongClickListener(object : NotesAdapter.OnNoteLongClickListener {
            override fun onLongClickNote(idNote: Int) {

                val alertBuilderChooseAction = AlertDialog.Builder(this@MainActivity, R.style.DialogTheme)
                alertBuilderChooseAction.setTitle(getText(R.string.choose_action))
                        .setCancelable(true)
                        .setPositiveButton(getText(R.string.archive)) { _: DialogInterface, _: Int ->
                            db.getNoteDao().updateArchiveNote(idNote, true)
                        }
                        .setNegativeButton(getText(R.string.delete)) { dialog, _ ->
                            db.getNoteDao().deleteNote(idNote)
                            dialog.cancel()
                        }
                val alert: AlertDialog = alertBuilderChooseAction.create()
                alert.show()

            }
        })

        adapter.setOnItemClickListener(object : NotesAdapter.OnNoteClickListener {
            override fun onClickNote(idNote: Int) {
                startActivity(AddNoteActivity.createStartIntentWithExtra(applicationContext, ID_NOTE, idNote))
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val myActionMenuItem = menu?.findItem(R.id.search_item)
        val searchView = myActionMenuItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if(query.isNotEmpty()) {
                    getNotesFromSqlRxWithSearch(query)
                }else{
                    getNotesFromSqlRx()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    getNotesFromSqlRx()
                } else {
                    getNotesFromSqlRxWithSearch(newText)
                }
                return false
            }
        })


        return true
    }
}
