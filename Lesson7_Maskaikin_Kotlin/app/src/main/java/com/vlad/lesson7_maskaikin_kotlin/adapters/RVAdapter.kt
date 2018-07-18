package com.vlad.lesson7_maskaikin_kotlin.adapters


import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.vlad.lesson7_maskaikin_kotlin.R
import com.vlad.lesson7_maskaikin_kotlin.getBridge.Object
import com.vlad.lesson7_maskaikin_kotlin.getBridge.ResultBridge
import java.text.SimpleDateFormat


class RVAdapter(internal var bridges: ResultBridge, var idBridge : Int?, var checkAlarm: Boolean?)
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

        viewHolder.imageViewStatusBridge.setBackgroundResource(getImageStatusBridge(position))
        viewHolder.textViewNameBridge.text = bridges.objects?.get(position)?.name?.replace(" мост","")?.replace("Мост ","")
        viewHolder.textViewCloseTimeBridge.text = getTimeCloseBridge(position)
        viewHolder.imageViewStatusAlarm.setBackgroundResource(getImageStatusAlarm(bridges.objects?.get(position),idBridge, checkAlarm))

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

    private fun getTimeCloseBridge(position: Int): String {

        val getDivorceSize = bridges.objects?.get(position)?.divorces?.size
        var index = 0
        var divorse = ""

        while (index < getDivorceSize!!) {
            divorse = formatter.format(bridges.objects?.get(position)?.divorces?.get(index)?.start) +
                    " - " + formatter.format(bridges.objects?.get(position)?.divorces?.get(index)?.end) + "  $divorse"
            index++
        }
        return divorse
    }

    private fun getImageStatusAlarm (bridge: Object?, idBridge: Int?, checkAlarm: Boolean?) :Int {

        var imageStatusAlarm :Int

                if (bridge?.id == idBridge && checkAlarm == true) {
                    bridge?.checkAlarm = true
                    imageStatusAlarm = R.drawable.ic_kolocol_on
                } else {
                    imageStatusAlarm = R.drawable.ic_kolocol_off
                }
        return imageStatusAlarm
    }

    private fun getImageStatusBridge(position: Int) :Int {

        val getTimeNow = System.currentTimeMillis()
        val timeNowForrmatedHour = formatterHour.format(getTimeNow).toInt()
        val timeNowForrmatedMinute = formatterMinute.format(getTimeNow).toInt()

        val timeNowInMinute = timeNowForrmatedHour * ONE_HOUR_IN_MINUTE + timeNowForrmatedMinute

        val getDivorceSize = bridges.objects?.get(position)?.divorces?.size
        var index = 0
        var resultImg: Int = -1

        while (index < getDivorceSize!!) {

            val startTimeCloseBridgeHour = formatterHour.format(bridges.objects?.get(position)?.divorces?.get(index)?.start).toInt()
            val endTimeCloseBridgeHour = formatterHour.format(bridges.objects?.get(position)?.divorces?.get(index)?.end).toInt()
            val startTimeCloseBridgeMinute = formatterMinute.format(bridges.objects?.get(position)?.divorces?.get(index)?.start).toInt()
            val endTimeCloseBridgeMinute = formatterMinute.format(bridges.objects?.get(position)?.divorces?.get(index)?.end).toInt()

            val startTimeCloseBridgeInMinute = startTimeCloseBridgeHour * ONE_HOUR_IN_MINUTE  + startTimeCloseBridgeMinute
            val endTimeCloseBridgeInMinute = endTimeCloseBridgeHour * ONE_HOUR_IN_MINUTE + endTimeCloseBridgeMinute

            if (timeNowInMinute in startTimeCloseBridgeInMinute..endTimeCloseBridgeInMinute) {
                resultImg =R.drawable.ic_brige_late
                break
            } else if (timeNowInMinute + ONE_HOUR_IN_MINUTE in startTimeCloseBridgeInMinute..endTimeCloseBridgeInMinute) {
                resultImg = R.drawable.ic_brige_soon
                break
            } else {
                resultImg = R.drawable.ic_brige_normal
            }
            index++
        }
        return resultImg
    }
}

