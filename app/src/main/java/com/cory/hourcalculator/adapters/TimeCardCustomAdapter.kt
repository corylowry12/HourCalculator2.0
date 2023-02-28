package com.cory.hourcalculator.adapters

import android.content.Context
import android.os.Bundle
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

class TimeCardCustomAdapter(
    private val context: Context,
    private val dataList: ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.row_name)
        var week: TextView = itemView.findViewById(R.id.row_week)
        var totalHours: TextView = itemView.findViewById(R.id.row_total_hours)

        fun bind(position: Int) {

            val dataItem = dataList[position]

            if (dataItem["name"] == null) {
                name.text = "Name: Unknown"
            }
            else {
                name.text = "Name: ${dataItem["name"]}"
            }

            totalHours.text = "Total: ${dataItem["totalHours"]}"
            week.text = "Week: ${dataItem["week"]}"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.time_card_list_row, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.findViewById<MaterialCardView>(R.id.cardViewTimeCard).setOnClickListener {
            val timeCardInfoFragment = TimeCardItemInfoFragment()
            timeCardInfoFragment.arguments = Bundle().apply {
                putString("id", dataList[holder.adapterPosition]["id"])
            }
            val manager =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            manager.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            manager.replace(R.id.fragment_container, timeCardInfoFragment)
                .addToBackStack(null)
            manager.commit()
        }
        (holder as TimeCardCustomAdapter.ViewHolder).bind(position)
    }
}