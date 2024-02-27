package com.cory.hourcalculator.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
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
import com.cory.hourcalculator.sharedprefs.AnimationData
import com.cory.hourcalculator.sharedprefs.ShowWagesInTimeCardData
import com.cory.hourcalculator.sharedprefs.WagesData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.lang.Exception
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TimeCardCustomAdapter(
    private val context: Context,
    private val dataList: ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lastPosition = -1

    fun updateCardColor() {
        notifyDataSetChanged()
    }

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.row_name)
        var week: TextView = itemView.findViewById(R.id.row_week)
        var totalHours: TextView = itemView.findViewById(R.id.row_total_hours)
        val wages: TextView = itemView.findViewById(R.id.row_time_card_wages)
        var countChip : Chip = itemView.findViewById<Chip>(R.id.timeCardItemInfoCountChip)
        fun bind(position: Int) {

            itemView.findViewById<MaterialCardView>(R.id.cardViewTimeCard)
                .setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))
            itemView.findViewById<Chip>(R.id.timeCardItemInfoCountChip)
                .setTextColor(Color.parseColor(CustomColorGenerator(context).generateBottomNavTextColor()))
            itemView.findViewById<Chip>(R.id.timeCardItemInfoCountChip).closeIconTint =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(context).generateBottomNavTextColor()))
            itemView.findViewById<Chip>(R.id.timeCardItemInfoCountChip).chipIconTint =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(context).generateBottomNavTextColor()))
            itemView.findViewById<Chip>(R.id.timeCardItemInfoCountChip).chipBackgroundColor =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(context).generateChipBackgroundColor()))

            try {
                val imagePath = File(dataList[position]["image"]!!)
                if (imagePath.exists()) {
                    countChip.chipIcon = ContextCompat.getDrawable(context, R.drawable.baseline_image_24)
                } else {
                    countChip.chipIcon = null
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                countChip.chipIcon = null
            }

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

            totalHours.text = "Total: $totalHoursRounded/$wholeNumber:$minute"

            if (!ShowWagesInTimeCardData(context).loadShowWages()) {
                wages.visibility = View.GONE
            }
            else {
                try {
                    val wagesFormatted =  String.format(
                        "%,.2f",
                        dataItem["totalHours"]!!.toDouble() * WagesData(context).loadWageAmount()!!
                            .toDouble()
                    )

                    wages.text = "Wages: $${wagesFormatted}"
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    if (WagesData(context).loadWageAmount() == "") {
                        wages.text = "Wages: Must Set Wages"
                    }
                    else {
                        wages.text = "Wages: Error"
                    }
                }
            }

            if (dataItem["count"]!!.toInt() > 1) {
                week.text = "Week: ${dataItem["week"]}"
            } else {
                week.text = "Day: ${dataItem["week"]}"
            }
            countChip.text = dataItem["count"]

            val timeCardCardView = itemView.findViewById<MaterialCardView>(R.id.cardViewTimeCard)

            if (!context.resources.getBoolean(R.bool.isTablet)) {
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
                        timeCardCardView.shapeAppearanceModel =
                            timeCardCardView.shapeAppearanceModel
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                                .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                                .setBottomRightCornerSize(0f)
                                .setBottomLeftCornerSize(0f)
                                .build()
                    } else if (position > 0 && position < dataList.count() - 1) {
                        timeCardCardView.shapeAppearanceModel =
                            timeCardCardView.shapeAppearanceModel
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                .setBottomRightCornerSize(0f)
                                .setBottomLeftCornerSize(0f)
                                .build()
                    } else if (position == dataList.count() - 1) {
                        timeCardCardView.shapeAppearanceModel =
                            timeCardCardView.shapeAppearanceModel
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                .setBottomRightCornerSize(28f)
                                .setBottomLeftCornerSize(28f)
                                .build()
                    }
                }
            }
            else {
                timeCardCardView.shapeAppearanceModel =
                    timeCardCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 14f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 14f)
                        .setBottomRightCornerSize(14f)
                        .setBottomLeftCornerSize(14f)
                        .build()
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
        if (AnimationData(context).loadTimeCardsScrollingAnimation()) {
            setAnimation(holder.itemView, position)
        }

        holder.itemView.findViewById<TextView>(R.id.row_time_card_wages).setOnLongClickListener {
            val dialog = BottomSheetDialog(context)
            val updateWagesLayout = LayoutInflater.from(context)
                .inflate(R.layout.update_wages_bottom_sheet, null)
            dialog.setContentView(updateWagesLayout)
            dialog.setCancelable(true)

            if (context.resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }

                    val editText =
                        updateWagesLayout.findViewById<TextInputEditText>(R.id.updateWagesTextInputEditText)
                    val updateWagesButton =
                        updateWagesLayout.findViewById<Button>(R.id.updateWagesButton)
                    val cancelButton = updateWagesLayout.findViewById<Button>(R.id.cancelButton)
                    val updateWagesCardView =
                        updateWagesLayout.findViewById<MaterialCardView>(R.id.updateWagesCardView)

                    editText.textCursorDrawable = null

                    updateWagesCardView.setCardBackgroundColor(
                        Color.parseColor(
                            CustomColorGenerator(context).generateCardColor()
                        )
                    )
                    updateWagesButton.setBackgroundColor(
                        Color.parseColor(
                            CustomColorGenerator(
                                context
                            ).generateCustomColorPrimary()
                        )
                    )
                    cancelButton.setTextColor(Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary()))

                    editText.setText(WagesData(context).loadWageAmount())

                    updateWagesButton.setOnClickListener {
                        Vibrate().vibration(context)
                        WagesData(context).setWageAmount(editText.text.toString())
                        notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    cancelButton.setOnClickListener {
                        Vibrate().vibration(context)
                        dialog.dismiss()
                    }

            if (holder.itemView.findViewById<TextView>(R.id.row_time_card_wages).text.toString().contains("Error") ||
                holder.itemView.findViewById<TextView>(R.id.row_time_card_wages).text.toString().contains("Must")) {
                dialog.show()
                return@setOnLongClickListener true
            }
            return@setOnLongClickListener false
        }

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
                Vibrate().vibrateOnLongClick(context)
                val dialog = BottomSheetDialog(context)
                val timeCardOptionsLayout = LayoutInflater.from(context)
                    .inflate(R.layout.time_card_options_bottom_sheet, null)
                dialog.setContentView(timeCardOptionsLayout)

                if (context.resources.getBoolean(R.bool.isTablet)) {
                    val bottomSheet =
                        dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                    val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    bottomSheetBehavior.skipCollapsed = true
                    bottomSheetBehavior.isHideable = false
                    bottomSheetBehavior.isDraggable = false
                }

                val renameCardView = timeCardOptionsLayout.findViewById<MaterialCardView>(R.id.renameCardView)
                val deleteCardView =
                    timeCardOptionsLayout.findViewById<MaterialCardView>(R.id.deleteCardView)
                val deleteAllCardView =
                    timeCardOptionsLayout.findViewById<MaterialCardView>(R.id.deleteAllCardView)

                renameCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))
                deleteCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))
                deleteAllCardView.setCardBackgroundColor(
                    Color.parseColor(
                        CustomColorGenerator(
                            context
                        ).generateCardColor()
                    )
                )

                renameCardView.shapeAppearanceModel = renameCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
                deleteCardView.shapeAppearanceModel = deleteCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
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

                renameCardView.setOnClickListener {
                    Vibrate().vibration(context)
                    dialog.dismiss()
                    val renameDialog = BottomSheetDialog(context)
                    val renameLayout = LayoutInflater.from(context)
                        .inflate(R.layout.rename_bottom_sheet, null)
                    renameDialog.setContentView(renameLayout)
                    renameDialog.setCancelable(false)

                    if (context.resources.getBoolean(R.bool.isTablet)) {
                        val bottomSheet =
                            renameDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.isDraggable = false
                    }

                    val editText = renameLayout.findViewById<TextInputEditText>(R.id.renameTextInputEditText)
                    val renameButton = renameLayout.findViewById<Button>(R.id.renameButton)
                    val cancelButton = renameLayout.findViewById<Button>(R.id.cancelButton)
                    val renameBottomSheetCardView = renameLayout.findViewById<MaterialCardView>(R.id.renameCardView)

                    renameBottomSheetCardView.setCardBackgroundColor(
                        Color.parseColor(
                            CustomColorGenerator(context).generateCardColor()
                        )
                    )
                    renameButton.setBackgroundColor(
                        Color.parseColor(
                            CustomColorGenerator(
                                context
                            ).generateCustomColorPrimary()
                        )
                    )
                    cancelButton.setTextColor(Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary()))

                    editText.textCursorDrawable = null
                    
                    editText.setText(dataList[holder.adapterPosition]["name"]?.trim())

                    editText?.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
                        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                            rename(editText, holder)
                            renameDialog.dismiss()
                            return@OnKeyListener true
                        }
                        false
                    })
                    
                    renameButton.setOnClickListener {
                        Vibrate().vibration(context)
                        rename(editText, holder)
                        renameDialog.dismiss()
                    }
                    cancelButton.setOnClickListener { 
                        Vibrate().vibration(context)
                        renameDialog.dismiss()
                    }
                    renameDialog.show()
                }
                deleteCardView.setOnClickListener {
                    Vibrate().vibration(context)
                    dialog.dismiss()
                    val deleteDialog = BottomSheetDialog(context)
                    val deleteEntryLayout =
                        LayoutInflater.from(context)
                            .inflate(R.layout.delete_single_time_card_entry_bottom_sheet, null)

                    deleteDialog.setContentView(deleteEntryLayout)
                    deleteDialog.setCancelable(true)

                    if (context.resources.getBoolean(R.bool.isTablet)) {
                        val bottomSheet =
                            deleteDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.isDraggable = false
                    }

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
                            dataList[position]["id"].toString()
                        )
                        dataList.removeAt(holder.adapterPosition)
                        if (AnimationData(context).loadRecyclerViewAnimation()) {
                            notifyItemRemoved(holder.adapterPosition)
                        }
                        else {
                            notifyDataSetChanged()
                        }

                        val runnable = Runnable {
                            (context as MainActivity).saveTimeCardState()
                        }
                        MainActivity().runOnUiThread(runnable)
                        deleteDialog.dismiss()
                        Toast.makeText(context, "Entry Deleted", Toast.LENGTH_SHORT).show()
                    }
                    noButton.setOnClickListener {
                        Vibrate().vibration(context)
                        deleteDialog.dismiss()
                    }

                    deleteDialog.show()
                }
                deleteAllCardView.setOnClickListener {
                    Vibrate().vibration(context)
                    dialog.dismiss()
                    val deleteAllDialog = BottomSheetDialog(context)
                    val deleteAllLayout =
                        LayoutInflater.from(context).inflate(R.layout.delete_all_bottom_sheet, null)
                    deleteAllDialog.setContentView(deleteAllLayout)
                    deleteAllDialog.setCancelable(true)

                    if (context.resources.getBoolean(R.bool.isTablet)) {
                        val bottomSheet =
                            deleteAllDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.isDraggable = false
                    }

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

    private fun rename(
        editText: TextInputEditText,
        holder: RecyclerView.ViewHolder
    ) {
        TimeCardDBHelper(context, null).updateName(
            editText.text.toString().trim(),
            dataList[holder.adapterPosition]["id"]!!
        )
        dataList.clear()
        val cursor = TimeCardDBHelper(context, null).getAllRow()
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_ID))
            try {
                map["name"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_NAME))
            } catch (e: java.lang.NullPointerException) {
                e.printStackTrace()
                map["name"] = ""
            }
            map["totalHours"] =
                cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_TOTAL))
            map["week"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_WEEK))
            try {
                map["image"] =
                    cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_IMAGE))
            } catch (e: NullPointerException) {
                e.printStackTrace()
                map["image"] = ""
            }
            map["count"] =
                TimeCardsItemDBHelper(context, null).getCountForItemID(
                    map["id"].toString().toInt()
                ).toString()

            if (map["week"]!!.contains("-") && !map["week"]!!.contains(" - ")) {
                TimeCardDBHelper(context, null).updateWeek(
                    map["week"]!!.replace(
                        "-",
                        " - "
                    ), map["id"]!!
                )
                map["week"] = map["week"]!!.replace("-", " - ")
            }

            if (map["count"]!!.toInt() == 1) {
                if (map["week"]!!.contains("-")) {
                    val (first, _) = map["week"]!!.split("-")
                    TimeCardDBHelper(context, null).updateWeek(first, map["id"]!!)
                    map["week"] = first
                }
            }

            dataList.add(map)

            cursor.moveToNext()
        }
        if (AnimationData(context).loadRecyclerViewAnimation()) {
            notifyItemChanged(holder.adapterPosition)
        }
        else {
            notifyDataSetChanged()
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {

        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_recycler_view)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}