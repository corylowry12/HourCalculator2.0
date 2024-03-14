package com.cory.hourcalculator.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.sharedprefs.ShowBreakTimeInDecimalData
import com.cory.hourcalculator.sharedprefs.ShowWagesInHistoryData
import com.cory.hourcalculator.sharedprefs.ShowWagesInTimeCardData
import com.cory.hourcalculator.sharedprefs.WagesData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputEditText
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class TimeCardItemCustomAdapter(
    private val context: Context,
    private val dataList: ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateCardColor() {
        notifyDataSetChanged()
    }

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var inTime: TextView = itemView.findViewById(R.id.row_in_time_card_item_info)
        var outTime: TextView = itemView.findViewById(R.id.row_out_time_card_item_info)
        var breakTime: TextView = itemView.findViewById(R.id.row_break_time_card_item_info)
        var totalHours: TextView = itemView.findViewById(R.id.row_total_time_card_item_info)
        val wages: TextView = itemView.findViewById(R.id.row_time_card_item_info_wages)
        var day: TextView = itemView.findViewById(R.id.row_day_time_card_item_info)

        fun bind(position: Int) {

            val dataItem = dataList[position]
            val timeCardItemCardView = itemView.findViewById<MaterialCardView>(R.id.cardViewTimeCardItem)

            timeCardItemCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))

            val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
            val dateString = formatter.format(dataItem["day"]!!.toLong())

            inTime.text = context.getString(R.string.in_time_adapter, dataItem["inTime"])
            outTime.text = context.getString(R.string.out_time_adapter, dataItem["outTime"])

            if (ShowBreakTimeInDecimalData(context).loadShowBreakTimeInDecimal()) {
                try {
                    val decimal =
                        (dataItem["breakTime"]!!.toDouble() / 60).toBigDecimal()
                            .setScale(2, RoundingMode.HALF_EVEN)
                    breakTime.text = context.getString(R.string.break_time_hours, decimal)
                } catch (e : Exception) {
                    e.printStackTrace()
                    breakTime.text = context.getString(R.string.break_time_error)
                }
            }
            else {
                if (dataItem["breakTime"] == "1") {
                    breakTime.text =
                        context.getString(
                            R.string.break_time_adapter_singular,
                            dataItem["breakTime"]
                        )
                } else {
                    breakTime.text =
                        context.getString(R.string.break_time_adapter_plural, dataItem["breakTime"])
                }
            }

            val totalHoursRounded =
                dataItem["totalHours"].toString().toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
                    .toDouble()

            val (wholeNumber, decimal) = dataItem["totalHours"]!!.split(".")
            var minute = (".$decimal".toDouble() * 60).toInt().toString()
            if (minute.length == 1) {
                minute = "0$minute"
            }

            totalHours.text =
                context.getString(R.string.total, totalHoursRounded.toString(), wholeNumber, minute)
            day.text = context.getString(R.string.date_time_card_custom_item_adapter, dateString)

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
            if (ShowWagesInTimeCardData(context).loadShowWages()) {
                if (dataItem["totalHours"]!!.contains(":")) {
                    val (hours, minutes) = dataItem["totalHours"]!!.split(":")
                    val decimal2 =
                        (minutes.toDouble() / 60).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
                            .toString().drop(1)

                    try {
                        val wagesDecimal = "$hours$decimal2".toDouble()
                        val wagesFormat = String.format("%.2f", wagesDecimal)
                        wages.text = context.getString(
                            R.string.wages_adapter,
                            String.format(
                                "%,.2f",
                                wagesFormat.toDouble() * WagesData(context).loadWageAmount()!!
                                    .toDouble()
                            )
                        )
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        if (WagesData(context).loadWageAmount() == "") {
                            wages.text = context.getString(R.string.wages_must_set_wages)
                        }
                        else {
                            wages.text = context.getString(R.string.wages_error)
                        }
                    }

                } else {
                    try {
                        wages.text = context.getString(
                            R.string.wages_adapter,
                            String.format(
                                "%,.2f",
                                dataItem["totalHours"]!!.toDouble() * WagesData(context).loadWageAmount()!!
                                    .toDouble()
                            )
                        )
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        if (WagesData(context).loadWageAmount() == "") {
                            wages.text = context.getString(R.string.wages_must_set_wages)
                        }
                        else {
                            wages.text = context.getString(R.string.wages_error)
                        }
                    }
                }
            }
            else {
                wages.visibility = View.GONE
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