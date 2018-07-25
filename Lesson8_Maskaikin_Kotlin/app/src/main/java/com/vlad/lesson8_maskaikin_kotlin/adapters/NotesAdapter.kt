package com.vlad.lesson8_maskaikin_kotlin.adapters

import android.graphics.Color
import android.support.v7.widget.CardView
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
        return ViewHolder(view, noteLongListener, noteListener)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewHolder: ViewHolder = holder

        checkNotesOnEmpty(viewHolder,notes[position].text, notes[position].title)
        checkNotesOnColorBackground(viewHolder,position)
        viewHolder.textViewTitleNote.text = notes[position].title
        viewHolder.textViewTextNote.text = notes[position].text
       // viewHolder.linearLayoutForNoteInCardView.setBackgroundColor(Color.parseColor(notes[position].backgroundColor))
        viewHolder.cardViewForNote.setCardBackgroundColor(Color.parseColor(notes[position].backgroundColor))

    }

    inner class ViewHolder internal constructor(noteView: View, listenerLong: OnNoteLongClickListener?, listener: OnNoteClickListener?) : RecyclerView.ViewHolder(noteView) {
        internal var textViewTitleNote: TextView = noteView.findViewById(R.id.textViewTitleNote)
        internal var textViewTextNote: TextView = noteView.findViewById(R.id.textViewTextNote)
       // internal var linearLayoutForNoteInCardView: LinearLayout = noteView.findViewById(R.id.linearLayoutForNoteInCardView)
        internal var cardViewForNote: CardView = noteView.findViewById(R.id.cardViewForNote)

        init {

            noteView.setOnLongClickListener {
                if (listenerLong != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        notes[position].id?.let { it1 -> listenerLong.onLongClickNote(it1) }
                    }
                }
                return@setOnLongClickListener true
            }

            noteView.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        notes[position].id?.let { it1 -> listener.onClickNote(it1) }
                    }
                }
            }
        }
    }

    private fun checkNotesOnEmpty(viewHolder: ViewHolder, text: String, title: String) {
        viewHolder.textViewTitleNote.visibility = View.VISIBLE
        viewHolder.textViewTextNote.visibility = View.VISIBLE

        if (text.isEmpty()) {
            viewHolder.textViewTextNote.visibility = View.GONE
        }
        if (title.isEmpty()) {
            viewHolder.textViewTitleNote.visibility = View.GONE
        }
    }

    private fun checkNotesOnColorBackground(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewTitleNote.setTextColor(Color.WHITE)
        viewHolder.textViewTextNote.setTextColor(Color.WHITE)
        if (notes[position].backgroundColor == WHITE_COLOR_HEX) {
            viewHolder.textViewTitleNote.setTextColor(Color.BLACK)
            viewHolder.textViewTextNote.setTextColor(Color.GRAY)
        }
    }
}