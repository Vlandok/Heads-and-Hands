package com.vlad.lesson8_maskaikin_kotlin.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.vlad.lesson8_maskaikin_kotlin.MainActivity.Companion.WHITE_COLOR_HEX
import com.vlad.lesson8_maskaikin_kotlin.R
import com.vlad.lesson8_maskaikin_kotlin.entity.Note

class NotesAdapter(internal var notes: List<Note>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {


    private lateinit var viewHolder: ViewHolder
    private lateinit var noteLongListener: OnNoteLongClickListener
    private lateinit var noteListener: OnNoteClickListener

    interface OnNoteClickListener {
        fun onClickNote(idNote: Int)
    }

    interface OnNoteLongClickListener {
        fun onLongClickNote(idNote: Int)
    }

    fun setOnItemClickListener(listener: OnNoteClickListener) {
        noteListener = listener
    }

    fun setOnItemLongClickListener(listenerLong: OnNoteLongClickListener) {
        noteLongListener = listenerLong
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_view_list_note, parent, false)
        viewHolder = ViewHolder(view, noteLongListener, noteListener)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        viewHolder = holder
        checkNotesOnEmpty(notes[position].text, notes[position].title)
        checkNotesOnColorBacgroun(position)
        viewHolder.textViewTitleNote.text = notes[position].title
        viewHolder.textViewTextNote.text = notes[position].text
        viewHolder.linearLayoutForNoteInCardView.setBackgroundColor(Color.parseColor(notes[position].backgroundColor))

    }

    inner class ViewHolder internal constructor(noteView: View, listenerLong: OnNoteLongClickListener?, listener: OnNoteClickListener?) : RecyclerView.ViewHolder(noteView) {
        internal var textViewTitleNote: TextView = noteView.findViewById(R.id.textViewTitleNote)
        internal var textViewTextNote: TextView = noteView.findViewById(R.id.textViewTextNote)
        internal var linearLayoutForNoteInCardView: LinearLayout = noteView.findViewById(R.id.linearLayoutForNoteInCardView)

        init {

            noteView.setOnLongClickListener {
                if (listenerLong != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listenerLong.onLongClickNote(notes[position].id)
                    }
                }
                return@setOnLongClickListener true
            }

            noteView.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClickNote(notes[position].id)
                    }
                }
            }
        }
    }

    private fun checkNotesOnEmpty(text: String, title: String) {
        viewHolder.textViewTitleNote.visibility = View.VISIBLE
        viewHolder.textViewTextNote.visibility = View.VISIBLE

        if (text.isEmpty()) {
            viewHolder.textViewTextNote.visibility = View.GONE
        }
        if (title.isEmpty()) {
            viewHolder.textViewTitleNote.visibility = View.GONE
        }
    }

    private fun checkNotesOnColorBacgroun(position: Int) {
        viewHolder.textViewTitleNote.setTextColor(Color.WHITE)
        viewHolder.textViewTextNote.setTextColor(Color.WHITE)
        if (notes[position].backgroundColor == WHITE_COLOR_HEX) {
            viewHolder.textViewTitleNote.setTextColor(Color.BLACK)
            viewHolder.textViewTextNote.setTextColor(Color.GRAY)
        }
    }
}