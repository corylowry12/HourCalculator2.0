package com.cory.hourcalculator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.fragments.EditHours
import com.cory.hourcalculator.fragments.TimeCardItemInfoFragment
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TimeCardItemCustomAdapter(
    private val context: Context,
    private val dataList: ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var inTime: TextView = itemView.findViewById(R.id.row_in_time_card_item_info)
        var outTime: TextView = itemView.findViewById(R.id.row_out_time_card_item_info)
        var breakTime: TextView = itemView.findViewById(R.id.row_break_time_card_item_info)
        var totalHours: TextView = itemView.findViewById(R.id.row_total_time_card_item_info)
        var day: TextView = itemView.findViewById(R.id.row_day_time_card_item_info)

        fun bind(position: Int) {

            val dataItem = dataList[position]

            val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
            val dateString = formatter.format(dataItem["day"]!!.toLong())

            inTime.text = "In Time: ${dataItem["inTime"]}"
            outTime.text = "Out Time: ${dataItem["outTime"]}"
            breakTime.text = "Break Time: ${dataItem["breakTime"]}"
            totalHours.text = "Total: ${dataItem["totalHours"]}"
            day.text = "Date: ${dateString}"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.time_card_item_info_list_row, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as TimeCardItemCustomAdapter.ViewHolder).bind(position)
    }
}