package com.cory.hourcalculator.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.cory.hourcalculator.fragments.TimeCardItemInfoFragment
import com.cory.hourcalculator.intents.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.shape.CornerFamily
import java.math.RoundingMode

class TimeCardCustomAdapter(
    private val context: Context,
    private val dataList: ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.row_name)
        var week: TextView = itemView.findViewById(R.id.row_week)
        var totalHours: TextView = itemView.findViewById(R.id.row_total_hours)
        var countChip = itemView.findViewById<Chip>(R.id.timeCardItemInfoCountChip)

        fun bind(position: Int) {

            itemView.findViewById<MaterialCardView>(R.id.cardViewTimeCard)
                .setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))
            itemView.findViewById<Chip>(R.id.timeCardItemInfoCountChip)
                .setTextColor(Color.parseColor(CustomColorGenerator(context).generateBottomNavTextColor()))
            itemView.findViewById<Chip>(R.id.timeCardItemInfoCountChip).closeIconTint =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(context).generateBottomNavTextColor()))
            itemView.findViewById<Chip>(R.id.timeCardItemInfoCountChip).chipBackgroundColor =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(context).generateChipBackgroundColor()))

            val dataItem = dataList[position]

            if (dataItem["name"] == null || dataItem["name"] == "") {
                name.text = "Name: Unknown"
            } else {
                name.text = "Name: ${dataItem["name"]}"
            }

            val totalHoursRounded =
                dataItem["totalHours"].toString().toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
                    .toDouble()

            val (wholeNumber, decimal) = dataItem["totalHours"]!!.split(".")
            var minute = (".$decimal".toDouble() * 60).toInt().toString()
            if (minute.length == 1) {
                minute = "0$minute"
            }

            //outputColon = "$wholeNumber" + ":$minute"

            totalHours.text = "Total: $totalHoursRounded/$wholeNumber:$minute"

            if (dataItem["count"]!!.toInt() > 1) {
                week.text = "Week: ${dataItem["week"]}"
            } else {
                week.text = "Day: ${dataItem["week"]}"
            }
            countChip.text = dataItem["count"]

            val timeCardCardView = itemView.findViewById<MaterialCardView>(R.id.cardViewTimeCard)

            if (dataList.count() == 1) {
                timeCardCardView.shapeAppearanceModel = timeCardCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(28f)
                    .setBottomLeftCornerSize(28f)
                    .build()
            } else if (dataList.count() > 1) {
                if (position == 0) {
                    timeCardCardView.shapeAppearanceModel = timeCardCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                        .setBottomRightCornerSize(0f)
                        .setBottomLeftCornerSize(0f)
                        .build()
                } else if (position > 0 && position < dataList.count() - 1) {
                    timeCardCardView.shapeAppearanceModel = timeCardCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                        .setBottomRightCornerSize(0f)
                        .setBottomLeftCornerSize(0f)
                        .build()
                } else if (position == dataList.count() - 1) {
                    timeCardCardView.shapeAppearanceModel = timeCardCardView.shapeAppearanceModel
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
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.time_card_list_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.findViewById<MaterialCardView>(R.id.cardViewTimeCard).setOnClickListener {
            Vibrate().vibration(context)
            val timeCardInfoFragment = TimeCardItemInfoFragment()
            timeCardInfoFragment.arguments = Bundle().apply {
                putString("id", dataList[holder.adapterPosition]["id"])
                putString("name", dataList[holder.adapterPosition]["name"])
            }
            val manager =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            manager.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            manager.replace(R.id.fragment_container, timeCardInfoFragment)
                .addToBackStack(null)
            manager.commit()
        }

        holder.itemView.findViewById<MaterialCardView>(R.id.cardViewTimeCard)
            .setOnLongClickListener {
                Vibrate().vibration(context)
                val dialog = BottomSheetDialog(context)
                val timeCardOptionsLayout = LayoutInflater.from(context)
                    .inflate(R.layout.time_card_options_bottom_sheet, null)
                dialog.setContentView(timeCardOptionsLayout)

                val deleteCardView =
                    timeCardOptionsLayout.findViewById<MaterialCardView>(R.id.deleteCardView)
                val deleteAllCardView =
                    timeCardOptionsLayout.findViewById<MaterialCardView>(R.id.deleteAllCardView)

                deleteCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))
                deleteAllCardView.setCardBackgroundColor(
                    Color.parseColor(
                        CustomColorGenerator(
                            context
                        ).generateCardColor()
                    )
                )


                deleteCardView.shapeAppearanceModel = deleteCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
                deleteAllCardView.shapeAppearanceModel = deleteAllCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(28f)
                    .setBottomLeftCornerSize(28f)
                    .build()

                deleteCardView.setOnClickListener {
                    Vibrate().vibration(context)
                    dialog.dismiss()
                    val dialog = BottomSheetDialog(context)
                    val deleteEntryLayout =
                        LayoutInflater.from(context)
                            .inflate(R.layout.delete_single_time_card_entry_bottom_sheet, null)
                    dialog.window?.navigationBarColor =
                        ContextCompat.getColor(context, R.color.black)
                    dialog.setContentView(deleteEntryLayout)
                    dialog.setCancelable(true)

                    val yesButton = deleteEntryLayout.findViewById<Button>(R.id.yesButton)
                    val noButton = deleteEntryLayout.findViewById<Button>(R.id.noButton)
                    val infoCardView =
                        deleteEntryLayout.findViewById<MaterialCardView>(R.id.bodyCardView)

                    infoCardView.setCardBackgroundColor(
                        Color.parseColor(
                            CustomColorGenerator(
                                context
                            ).generateCardColor()
                        )
                    )
                    yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary()))
                    noButton.setTextColor(Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary()))


                    yesButton.setOnClickListener {
                        Vibrate().vibration(context)
                        TimeCardDBHelper(
                            context,
                            null
                        ).deleteRow(dataList[position]["id"].toString())
                        TimeCardsItemDBHelper(context, null).deleteAllItemRow(
                            context,
                            dataList[position]["id"].toString()
                        )
                        dataList.removeAt(holder.adapterPosition)
                        notifyItemRemoved(holder.adapterPosition)

                        val runnable = Runnable {
                            (context as MainActivity).saveTimeCardState()
                        }
                        MainActivity().runOnUiThread(runnable)
                        dialog.dismiss()
                        Toast.makeText(context, "Entry Deleted", Toast.LENGTH_SHORT).show()
                    }
                    noButton.setOnClickListener {
                        Vibrate().vibration(context)
                        dialog.dismiss()
                    }

                    dialog.show()
                }
                deleteAllCardView.setOnClickListener {
                    Vibrate().vibration(context)
                    dialog.dismiss()
                    val deleteAllDialog = BottomSheetDialog(context)
                    val deleteAllLayout =
                        LayoutInflater.from(context).inflate(R.layout.delete_all_bottom_sheet, null)
                    deleteAllDialog.setContentView(deleteAllLayout)
                    deleteAllDialog.setCancelable(true)

                    val infoCardView =
                        deleteAllLayout.findViewById<MaterialCardView>(R.id.infoCardView)
                    val yesButton = deleteAllLayout.findViewById<Button>(R.id.yesButton)
                    val noButton = deleteAllLayout.findViewById<Button>(R.id.noButton)

                    infoCardView.setCardBackgroundColor(
                        Color.parseColor(
                            CustomColorGenerator(
                                context
                            ).generateCardColor()
                        )
                    )
                    yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary()))
                    noButton.setTextColor(Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary()))


                    yesButton.setOnClickListener {
                        TimeCardDBHelper(context, null).deleteAll()
                        TimeCardsItemDBHelper(context, null).deleteAll()
                        dataList.clear()

                        val runnable = Runnable {
                            (context as MainActivity).deleteAllTimeCards()
                        }
                        MainActivity().runOnUiThread(runnable)

                        deleteAllDialog.dismiss()
                        Toast.makeText(context, "All time card entries deleted", Toast.LENGTH_SHORT)
                            .show()
                    }
                    noButton.setOnClickListener {
                        deleteAllDialog.dismiss()
                    }
                    deleteAllDialog.show()
                }

                dialog.show()
                return@setOnLongClickListener true
            }
        (holder as TimeCardCustomAdapter.ViewHolder).bind(position)
    }
}