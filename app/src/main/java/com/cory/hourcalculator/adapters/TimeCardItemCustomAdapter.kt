package com.cory.hourcalculator.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import java.text.SimpleDateFormat
import java.util.*

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
            val timeCardItemCardView = itemView.findViewById<MaterialCardView>(R.id.cardViewTimeCardItem)

            timeCardItemCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))

            val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
            val dateString = formatter.format(dataItem["day"]!!.toLong())

            inTime.text = "In Time: ${dataItem["inTime"]}"
            outTime.text = "Out Time: ${dataItem["outTime"]}"
            breakTime.text = "Break Time: ${dataItem["breakTime"]}"
            totalHours.text = "Total: ${dataItem["totalHours"]}"
            day.text = "Date: $dateString"

            if (dataList.count() == 1) {
                timeCardItemCardView.shapeAppearanceModel = timeCardItemCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(28f)
                    .setBottomLeftCornerSize(28f)
                    .build()
            }
            else if (dataList.count() > 1) {
                if (position == 0) {
                    timeCardItemCardView.shapeAppearanceModel = timeCardItemCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                        .setBottomRightCornerSize(0f)
                        .setBottomLeftCornerSize(0f)
                        .build()
                }
                else if (position > 0 && position < dataList.count() - 1) {
                    timeCardItemCardView.shapeAppearanceModel = timeCardItemCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                        .setBottomRightCornerSize(0f)
                        .setBottomLeftCornerSize(0f)
                        .build()
                }
                else if (position == dataList.count() - 1) {
                    timeCardItemCardView.shapeAppearanceModel = timeCardItemCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                        .setBottomRightCornerSize(28f)
                        .setBottomLeftCornerSize(28f)
                        .build()
                }
            }

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