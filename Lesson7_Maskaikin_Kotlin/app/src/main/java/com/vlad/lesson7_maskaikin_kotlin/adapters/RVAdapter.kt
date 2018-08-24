package com.vlad.lesson7_maskaikin_kotlin.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.vlad.lesson7_maskaikin_kotlin.R
import com.vlad.lesson7_maskaikin_kotlin.getBridge.Object
import com.vlad.lesson7_maskaikin_kotlin.getBridge.ResultBridge
import com.vlad.lesson7_maskaikin_kotlin.getImageStatusAlarm
import com.vlad.lesson7_maskaikin_kotlin.getImageStatusBridge
import com.vlad.lesson7_maskaikin_kotlin.getTimeCloseBridge
import java.text.SimpleDateFormat


class RVAdapter(internal var bridges: ResultBridge)
    : RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    private lateinit var viewHolder: ViewHolder
    private lateinit var itemListener: OnItemClickListener

    companion object {
        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("HH:mm")
        @SuppressLint("SimpleDateFormat")
        val formatterHour = SimpleDateFormat("HH")
        @SuppressLint("SimpleDateFormat")
        val formatterMinute = SimpleDateFormat("mm")

        const val ONE_HOUR_IN_MINUTE = 60

    }

    interface OnItemClickListener {
        fun onClickItem(bridge: Object?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.ViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.bridge_view, parent, false)
        viewHolder = ViewHolder(view, itemListener)
        return viewHolder

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RVAdapter.ViewHolder, position: Int) {

        viewHolder = holder
        val bridge = bridges.objects?.get(position)
        viewHolder.imageViewStatusBridge.setBackgroundResource(getImageStatusBridge(position, bridges))
        viewHolder.textViewNameBridge.text = bridge?.name?.replace(" мост", "")?.replace("Мост ", "")
        viewHolder.textViewCloseTimeBridge.text = getTimeCloseBridge(bridge)
        viewHolder.imageViewStatusAlarm.setBackgroundResource(getImageStatusAlarm(bridge))

    }

    override fun getItemCount(): Int {
        return bridges.objects!!.size
    }

    inner class ViewHolder internal constructor(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        internal var imageViewStatusBridge: ImageView = itemView.findViewById(R.id.imageViewStatusBridge)
        internal var textViewNameBridge: TextView = itemView.findViewById(R.id.textViewNameBridge)
        internal var textViewCloseTimeBridge: TextView = itemView.findViewById(R.id.textViewCloseTimeBridge)
        internal var imageViewStatusAlarm: ImageView = itemView.findViewById(R.id.imageViewStatusAlarm)

        init {

            itemView.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClickItem(bridges.objects?.get(position))
                    }
                }
            }

        }
    }

}

