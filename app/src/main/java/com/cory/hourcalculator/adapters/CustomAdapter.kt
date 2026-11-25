package com.cory.hourcalculator.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Outline
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.cory.hourcalculator.fragments.EditHours
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.shape.CornerFamily
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class CustomAdapter(
    private val context: Context,
    private val dataList: ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lastPosition = -1
    var checkBoxVisible = false
    private var selectedItems = arrayOf<Int>()
    private var selectedItemsList = selectedItems.toMutableList()

    lateinit var snackbarDeleteSelected: Snackbar
    lateinit var snackbarDismissCheckBox: Snackbar

    private lateinit var items: RecyclerView.ViewHolder

    lateinit var historyCardView: MaterialCardView

    fun updateCardColor() {
        notifyDataSetChanged()
    }

    fun checkboxVisible() {
        checkBoxVisible = false
        if (AnimationData(context).loadRecyclerViewAnimation()) {
            notifyItemRangeChanged(0, dataList.size)
        } else {
            notifyDataSetChanged()
        }
        snackbarDeleteSelected.dismiss()
        snackbarDismissCheckBox.dismiss()
        selectedItemsList.clear()
    }

    fun isCheckBoxVisible(): Boolean {
        return checkBoxVisible
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var inTime: TextView = itemView.findViewById(R.id.row_in)
        var outTime: TextView = itemView.findViewById(R.id.row_out)
        var breakTime: TextView = itemView.findViewById(R.id.row_break)
        var totalTime: TextView = itemView.findViewById(R.id.row_total)
        var wages: TextView = itemView.findViewById(R.id.row_wages)
        var date: TextView = itemView.findViewById(R.id.row_day)
        var checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(position: Int) {
            historyCardView = itemView.findViewById(R.id.cardViewHistory)

            val imageViewOptions = itemView.findViewById<ImageButton>(R.id.imageViewOptions)
            val imageViewOptionsCardView =
                itemView.findViewById<MaterialCardView>(R.id.imageViewOptionsCardView)

            imageViewOptionsCardView.setCardBackgroundColor(
                Color.parseColor(
                    CustomColorGenerator(
                        context
                    ).generateTopAppBarColor()
                )
            )

            val color = Color.parseColor(CustomColorGenerator(context).generateMenuTintColor())
            imageViewOptions.setColorFilter(color)

            historyCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))

            val states = arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            )

            val colors = intArrayOf(
                Color.parseColor("#000000"),
                Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary())
            )

            itemView.findViewById<CheckBox>(R.id.checkbox).buttonTintList =
                ColorStateList(states, colors)

            if (!context.resources.getBoolean(R.bool.isTablet)) {
                if (dataList.count() == 1) {
                    historyCardView.shapeAppearanceModel = historyCardView.shapeAppearanceModel
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                        .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                        .setBottomRightCornerSize(28f)
                        .setBottomLeftCornerSize(28f)
                        .build()
                } else if (dataList.count() > 1) {
                    if (position == 0) {
                        historyCardView.shapeAppearanceModel = historyCardView.shapeAppearanceModel
                            .toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                            .setBottomRightCornerSize(0f)
                            .setBottomLeftCornerSize(0f)
                            .build()
                    } else if (position > 0 && position < dataList.count() - 1) {
                        historyCardView.shapeAppearanceModel = historyCardView.shapeAppearanceModel
                            .toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                            .setBottomRightCornerSize(0f)
                            .setBottomLeftCornerSize(0f)
                            .build()
                    } else if (position == dataList.count() - 1) {
                        historyCardView.shapeAppearanceModel = historyCardView.shapeAppearanceModel
                            .toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                            .setBottomRightCornerSize(28f)
                            .setBottomLeftCornerSize(28f)
                            .build()
                    }
                }
            } else {
                historyCardView.shapeAppearanceModel = historyCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 14f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 14f)
                    .setBottomRightCornerSize(14f)
                    .setBottomLeftCornerSize(14f)
                    .build()
            }

            val dataItem = dataList[position]

            val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
            val dateString = formatter.format(dataItem["date"]!!.toLong())

            inTime.text = context.getString(R.string.in_time_adapter, dataItem["inTime"])
            outTime.text = context.getString(R.string.out_time_adapter, dataItem["outTime"])

            if (ShowBreakTimeInDecimalData(context).loadShowBreakTimeInDecimal()) {
                try {
                    val decimal =
                        (dataItem["breakTime"]!!.toDouble() / 60).toBigDecimal()
                            .setScale(2, RoundingMode.HALF_EVEN)
                    breakTime.text = context.getString(R.string.break_time_hours, decimal)
                } catch (e: Exception) {
                    e.printStackTrace()
                    breakTime.text = context.getString(R.string.break_time_error)
                }
            } else {
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
            if (dataItem["totalHours"] == "1.0") {
                totalTime.text = context.getString(R.string.total_time_adapter_singular, "1")
            } else if (dataItem["totalHours"]!!.endsWith(".0")) {
                val total = dataItem["totalHours"]!!.substringBefore(".")
                totalTime.text = context.getString(R.string.total_time_adapter_plural, total)
            } else {
                totalTime.text =
                    context.getString(R.string.total_time_adapter_plural, dataItem["totalHours"])
            }
            date.text = context.getString(R.string.date_adapter, dateString.toString())

            if (checkBoxVisible) {
                checkBox.visibility = View.VISIBLE
            } else {
                checkBox.visibility = View.GONE
            }

            if (ShowWagesInHistoryData(context).loadShowWages()) {
                if (dataItem["totalHours"]!!.contains(":")) {
                    val (hours, minutes) = dataItem["totalHours"]!!.split(":")
                    val decimal =
                        (minutes.toDouble() / 60).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
                            .toString().drop(1)

                    try {
                        val wagesDecimal = "$hours$decimal".toDouble()
                        val wagesFormat = String.format("%.2f", wagesDecimal)
                        wages.text = context.getString(
                            R.string.wages_adapter, String.format(
                                "%,.2f",
                                wagesFormat.toDouble() * WagesData(context).loadWageAmount()!!
                                    .toDouble()
                            )
                        )
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        if (WagesData(context).loadWageAmount() == "") {
                            wages.text = context.getString(R.string.wages_must_set_wages)
                        } else {
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
                        } else {
                            wages.text = context.getString(R.string.wages_error)
                        }
                    }
                }
            } else {
                wages.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_row, parent, false))
    }

    class NoSwipeBehavior : BaseTransientBottomBar.Behavior() {
        override fun canSwipeDismissView(child: View): Boolean {
            return false
        }
    }

    fun getSelectedCount(): Int {
        return selectedItemsList.count()
    }

    fun getSelectedItems(): MutableList<Int> {
        return selectedItemsList
    }

    fun exportSelected() {
        val dbHandler = DBHelper(context, null)
        val timeCardDBHandler = TimeCardDBHelper(context, null)
        val timeCardItemDBHandler = TimeCardsItemDBHelper(context, null)

        val saveState = Runnable {
            (context as MainActivity).saveState()
        }
        MainActivity().runOnUiThread(saveState)

        val hideNavigationIcon = Runnable {
            (context as MainActivity).hideNavigationIcon()
        }
        MainActivity().runOnUiThread(hideNavigationIcon)

        val map = HashMap<String, String>()
        val cursor = dbHandler.getAllRow(context)

        var totalHours = 0.0
        val weekArray = arrayListOf<String>()

        val inTimeArray = arrayListOf<String>()
        val outTimeArray = arrayListOf<String>()
        val breakTimeArray = arrayListOf<String>()
        val totalHoursArray = arrayListOf<String>()
        val dateArray = arrayListOf<Long>()
        if (cursor!!.count > 0) {

            for (i in 0 until selectedItemsList.count()) {
                cursor.moveToPosition(selectedItemsList.elementAt(i))
                map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                map["inTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                map["outTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                map["breakTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                map["totalHours"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

                weekArray.add(map["date"].toString())
                totalHours += if (!map["totalHours"]!!.contains(":")) {
                    map["totalHours"]!!.toDouble()
                } else {
                    val (hours, minutes) = map["totalHours"]!!.split(":")
                    val decimal =
                        (minutes.toDouble() / 60).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
                            .toString().drop(2)
                    "$hours.$decimal".toDouble()
                }

                inTimeArray.add(map["inTime"]!!)
                outTimeArray.add(map["outTime"]!!)
                breakTimeArray.add(map["breakTime"]!!)
                totalHoursArray.add(map["totalHours"]!!)
                dateArray.add(map["date"]!!.toLong())

                dbHandler.deleteRow(map["id"].toString())
                dataList.removeAt(selectedItemsList.elementAt(i))
                if (AnimationData(context).loadRecyclerViewAnimation()) {
                    notifyItemRemoved(selectedItemsList.elementAt(i))
                } else {
                    notifyDataSetChanged()
                }
                checkBoxVisible = false
            }
            val sortedList = weekArray.sortedWith(compareBy { it })

            val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val firstDateString: String
            val lastDateString: String
            if (sortedList.count() > 1) {
                firstDateString = formatter.format(sortedList.first().toString().toLong())
                lastDateString = formatter.format(sortedList.last().toString().toLong())
                timeCardDBHandler.insertRow(
                    DefaultTimeCardNameData(context).loadDefaultName(),
                    "$firstDateString - $lastDateString",
                    totalHours.toString()
                )
            } else {
                firstDateString = formatter.format(sortedList.elementAt(0).toString().toLong())
                timeCardDBHandler.insertRow(
                    DefaultTimeCardNameData(context).loadDefaultName(),
                    firstDateString,
                    totalHours.toString()
                )
            }

            val timeCardLatestRowCursor = timeCardDBHandler.getLatestRowID()
            timeCardLatestRowCursor?.moveToFirst()
            for (i in 0 until inTimeArray.count()) {
                timeCardItemDBHandler.insertRow(
                    timeCardLatestRowCursor?.getString(
                        timeCardLatestRowCursor.getColumnIndex(
                            TimeCardDBHelper.COLUMN_ID
                        )
                    )!!,
                    inTimeArray.elementAt(i),
                    outTimeArray.elementAt(i),
                    totalHoursArray.elementAt(i),
                    dateArray.elementAt(i),
                    breakTimeArray.elementAt(i)
                )
            }
        }

        checkBoxVisible = false
        if (AnimationData(context).loadRecyclerViewAnimation()) {
            notifyItemRangeChanged(0, dataList.size)
        } else {
            notifyDataSetChanged()
        }
        val entriesDeleted: String = if (selectedItemsList.count() == 1) {
            selectedItemsList.count().toString() + " Entry Exported"
        } else {
            selectedItemsList.count().toString() + " Entries Exported"
        }
        val snackBar = Snackbar.make(
            snackbarDeleteSelected.view,
            entriesDeleted,
            Snackbar.LENGTH_LONG
        ).setDuration(5000)

        snackBar.apply {
            snackBar.view.background = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.snackbar_corners,
                context.theme
            )
        }
        snackBar.show()

        selectedItemsList.clear()
    }

    fun deleteSelected(view: View) {
        val dbHandler = DBHelper(context, null)

        val inTime = arrayOf<String>().toMutableList()
        val outTime = arrayOf<String>().toMutableList()
        val breakTime = arrayOf<String>().toMutableList()
        val totalHours = arrayOf<String>().toMutableList()
        val day = arrayOf<Long>().toMutableList()

        Vibrate().vibration(context)
        val saveState = Runnable {
            (context as MainActivity).saveState()
        }
        MainActivity().runOnUiThread(saveState)

        val hideNavigationIcon = Runnable {
            (context as MainActivity).hideNavigationIcon()
        }
        MainActivity().runOnUiThread(hideNavigationIcon)

        val map = HashMap<String, String>()
        val cursor = dbHandler.getAllRow(context)
        if (cursor!!.count > 0) {

            for (i in 0 until selectedItemsList.count()) {
                cursor.moveToPosition(selectedItemsList.elementAt(i))
                map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                map["inTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                map["outTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                map["breakTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                map["totalHours"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

                inTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN)))
                outTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT)))
                breakTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK)))
                totalHours.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)))
                day.add(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DAY)))

                dbHandler.deleteRow(map["id"].toString())
                dataList.removeAt(selectedItemsList.elementAt(i))
                if (AnimationData(context).loadRecyclerViewAnimation()) {
                    notifyItemRemoved(selectedItemsList.elementAt(i))
                } else {
                    notifyDataSetChanged()
                }

                checkBoxVisible = false
            }
        }

        checkBoxVisible = false
        if (AnimationData(context).loadRecyclerViewAnimation()) {
            notifyItemRangeChanged(0, dataList.size)
        } else {
            notifyDataSetChanged()
        }
        val entriesDeleted: String = if (selectedItemsList.count() == 1) {
            selectedItemsList.count().toString() + " Entry Deleted"
        } else {
            selectedItemsList.count().toString() + " Entries Deleted"
        }
        val snackBar = Snackbar.make(
            view,
            entriesDeleted,
            Snackbar.LENGTH_LONG
        ).setDuration(5000)

        snackBar.setActionTextColor(
            Color.parseColor(CustomColorGenerator(context).generateSnackbarActionTextColor())
        )

        snackBar.setAction(context.getString(R.string.undo)) {
            Vibrate().vibration(context)

            for (i in inTime.indices) {
                dbHandler.insertRow(
                    inTime.elementAt(i),
                    outTime.elementAt(i),
                    totalHours.elementAt(i),
                    day.elementAt(i),
                    breakTime.elementAt(i)
                )
            }

            dataList.clear()

            val cursor2 = dbHandler.getAllRow(context)
            cursor2!!.moveToFirst()
            if (cursor2.count > 0) {
                while (!cursor2.isAfterLast) {
                    val map2 = HashMap<String, String>()
                    map2["id"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_ID))
                    map2["inTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_IN))
                    map2["outTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_OUT))
                    map2["breakTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_BREAK))
                    map2["totalHours"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_TOTAL))
                    map2["date"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_DAY))
                    dataList.add(map2)
                    cursor2.moveToNext()
                }
            }

            for (i in 0 until selectedItemsList.count()) {
                if (AnimationData(context).loadRecyclerViewAnimation()) {
                    notifyItemInserted(selectedItemsList.elementAt(i))
                } else {
                    notifyDataSetChanged()
                }
            }

            selectedItemsList.clear()

            if (AnimationData(context).loadRecyclerViewAnimation()) {
                notifyItemRangeChanged(0, dataList.count())
            } else {
                notifyDataSetChanged()
            }
            val restoreState = Runnable {
                (context as MainActivity).restoreState()
            }
            MainActivity().runOnUiThread(restoreState)
        }
        snackBar.apply {
            snackBar.view.background = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.snackbar_corners,
                context.theme
            )
        }
        snackBar.show()

        selectedItemsList.clear()
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (AnimationData(context).loadHistoryScrollingAnimation()) {
            setAnimation(holder.itemView, position)
        }

        val dbHandler = DBHelper(context, null)
        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.checkbox)

        holder.itemView.findViewById<MaterialCardView>(R.id.imageViewOptionsCardView).setOnClickListener {
            Vibrate().vibration(context)
            showBlurredPopupMenu(it, holder, dbHandler)
        }

        items = holder

        checkBox.isChecked = selectedItemsList.contains(holder.adapterPosition)

        val inTime = arrayOf<String>().toMutableList()
        val outTime = arrayOf<String>().toMutableList()
        val breakTime = arrayOf<String>().toMutableList()
        val totalHours = arrayOf<String>().toMutableList()
        val day = arrayOf<Long>().toMutableList()

        if (!checkBoxVisible) {
            holder.itemView.findViewById<MaterialCardView>(R.id.cardViewHistory)
                .setOnLongClickListener {
                    Vibrate().vibrateOnLongClick(context)
                    longClick(inTime, outTime, breakTime, totalHours, day, checkBox, holder)
                    true
                }

            if (ClickableHistoryEntryData(context).loadHistoryItemClickable()) {
                holder.itemView.findViewById<MaterialCardView>(R.id.cardViewHistory)
                    .setOnClickListener {
                        Vibrate().vibration(context)
                        openHourForEditing(holder, dbHandler)
                    }
            }
        } else {
            holder.itemView.findViewById<MaterialCardView>(R.id.cardViewHistory)
                .setOnLongClickListener {
                    Vibrate().vibrateOnLongClick(context)
                    longClickSelectAll(holder)
                    return@setOnLongClickListener true
                }

            holder.itemView.findViewById<MaterialCardView>(R.id.cardViewHistory)
                .setOnClickListener {
                    clickEntryWhenCheckboxVisible(holder)
                }
        }

        holder.itemView.findViewById<MaterialCardView>(R.id.imageViewOptionsCardView).setOnClickListener {
            Vibrate().vibration(context)
            showBlurredPopupMenu(it, holder, dbHandler)
        }

        (holder as ViewHolder).bind(position)
    }

    private fun showBlurredPopupMenu(
        anchorView: View,
        holder: RecyclerView.ViewHolder,
        dbHandler: DBHelper
    ) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.dialog_history_popup_menu, null)
        val cardView = popupView.findViewById<com.google.android.material.card.MaterialCardView>(R.id.popupCardContainer)
        val blurView = popupView.findViewById<eightbitlab.com.blurview.BlurView>(R.id.blurViewContainer)

        cardView.strokeWidth = 0

        val activity = context as? AppCompatActivity
        val rootView = activity?.findViewById<ViewGroup>(android.R.id.content)

        // Detect dark mode
        val isDark = when (DarkThemeData(context).loadDarkModeState()) {
            1, 2 -> true
            0 -> false
            else -> {
                val nightMask = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
                nightMask == android.content.res.Configuration.UI_MODE_NIGHT_YES
            }
        }

        // --- 1. LIGHT THEME FROSTED TRANSLUCENCY ADJUSTMENT ---
        // Lowered light mode alpha from ~140 down to 85 (~33% opacity) for strong see-through glass
        val frostedOverlayColor = if (isDark) {
            Color.argb(125, 28, 30, 34) // Dark smoky slate
        } else {
            Color.argb(85, 255, 255, 255) // High-translucency milk glass
        }

        if (rootView != null) {
            val blurAlgorithm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                eightbitlab.com.blurview.RenderEffectBlur()
            } else {
                eightbitlab.com.blurview.RenderScriptBlur(context)
            }

            // Increased blur radius from 24f to 35f for an ultra-smooth frosted diffusion
            blurView.setupWith(rootView, blurAlgorithm)
                .setFrameClearDrawable(rootView.background)
                .setBlurRadius(35f)
                .setOverlayColor(frostedOverlayColor)
        }

        // Tint both text and start icons consistently
        val textColor = if (isDark) Color.parseColor("#F1F3F4") else Color.parseColor("#1F1F1F")
        val colorStateList = ColorStateList.valueOf(textColor)

        val itemEdit = popupView.findViewById<TextView>(R.id.menuItemEdit)
        val itemDelete = popupView.findViewById<TextView>(R.id.menuItemDelete)
        val itemDeleteAll = popupView.findViewById<TextView>(R.id.menuItemDeleteAll)

        listOf(itemEdit, itemDelete, itemDeleteAll).forEach { textView ->
            textView.setTextColor(textColor)
            textView.compoundDrawableTintList = colorStateList
        }

        popupView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val popupWidth = popupView.measuredWidth

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            elevation = 0f
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isOutsideTouchable = true
        }

        itemEdit.setOnClickListener {
            Vibrate().vibration(context)
            popupWindow.dismiss()
            openHourForEditing(holder, dbHandler)
        }

        itemDelete.setOnClickListener {
            popupWindow.dismiss()
            deleteSingleItem(holder, dbHandler)
        }

        itemDeleteAll.setOnClickListener {
            popupWindow.dismiss()
            showDeleteAllBottomSheet(dbHandler)
        }

        // --- 2. PUSH AWAY FROM RIGHT SCREEN EDGE ---
        // 12dp margin converted to physical pixels
        val rightMarginPx = (12 * context.resources.displayMetrics.density).toInt()
        val xOffset = -(popupWidth - anchorView.width + rightMarginPx)

        popupWindow.showAsDropDown(anchorView, xOffset, 6)
    }

    @SuppressLint("Range")
    private fun deleteSingleItem(holder: RecyclerView.ViewHolder, dbHandler: DBHelper) {
        Vibrate().vibration(context)
        try {
            var intimeDelete = ""
            var outtimeDelete = ""
            var breaktimeDelete = ""
            var totalhoursDelete = ""
            var dayDelete = 0L

            val map = HashMap<String, String>()
            val cursor = dbHandler.getAllRow(context)
            val itemPosition = holder.adapterPosition
            if (cursor!!.count > 0) {
                cursor.moveToPosition(itemPosition)
                if (cursor.position == itemPosition) {
                    map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                    intimeDelete = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                    outtimeDelete = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                    breaktimeDelete = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                    totalhoursDelete = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                    dayDelete = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

                    dbHandler.deleteRow(map["id"].toString())
                }
            }

            val saveState = Runnable { (context as MainActivity).saveState() }
            MainActivity().runOnUiThread(saveState)

            dataList.removeAt(itemPosition)
            if (AnimationData(context).loadRecyclerViewAnimation()) {
                notifyItemRemoved(itemPosition)
            } else {
                notifyDataSetChanged()
            }
            checkBoxVisible = false

            val snackbar = Snackbar.make(
                holder.itemView,
                context.getString(R.string.entry_deleted),
                Snackbar.LENGTH_LONG
            ).setDuration(5000)

            snackbar.setAction(context.getString(R.string.undo)) {
                Vibrate().vibration(context)
                dbHandler.insertRow(
                    intimeDelete,
                    outtimeDelete,
                    totalhoursDelete,
                    dayDelete,
                    breaktimeDelete
                )

                dataList.clear()
                val cursor2 = dbHandler.getAllRow(context)
                cursor2!!.moveToFirst()
                while (!cursor2.isAfterLast) {
                    val map2 = HashMap<String, String>()
                    map2["id"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_ID))
                    map2["inTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_IN))
                    map2["outTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_OUT))
                    map2["breakTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_BREAK))
                    map2["totalHours"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_TOTAL))
                    map2["date"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_DAY))
                    dataList.add(map2)
                    cursor2.moveToNext()
                }

                if (AnimationData(context).loadRecyclerViewAnimation()) {
                    notifyItemInserted(itemPosition)
                    notifyItemRangeChanged(0, dataList.count())
                } else {
                    notifyDataSetChanged()
                }

                val restoreState = Runnable { (context as MainActivity).restoreState() }
                MainActivity().runOnUiThread(restoreState)
            }

            snackbar.setActionTextColor(
                Color.parseColor(CustomColorGenerator(context).generateSnackbarActionTextColor())
            )
            snackbar.view.background = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.snackbar_corners,
                context.theme
            )
            snackbar.show()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.there_was_an_error_deleting_this_entry),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("Range")
    private fun showDeleteAllBottomSheet(dbHandler: DBHelper) {
        Vibrate().vibration(context)
        val inTimeDelete = arrayOf<String>().toMutableList()
        val outTimeDelete = arrayOf<String>().toMutableList()
        val breakTimeDelete = arrayOf<String>().toMutableList()
        val totalHoursDelete = arrayOf<String>().toMutableList()
        val dayDelete = arrayOf<Long>().toMutableList()

        val dialog = BottomSheetDialog(context)
        val deleteAllLayout = LayoutInflater.from(context).inflate(R.layout.delete_all_bottom_sheet, null)
        dialog.setContentView(deleteAllLayout)
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

        val infoCardView = deleteAllLayout.findViewById<MaterialCardView>(R.id.infoCardView)
        val yesButton = deleteAllLayout.findViewById<Button>(R.id.yesButton)
        val noButton = deleteAllLayout.findViewById<Button>(R.id.noButton)

        infoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCardColor()))
        yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary()))
        noButton.setTextColor(Color.parseColor(CustomColorGenerator(context).generateCustomColorPrimary()))

        yesButton.setOnClickListener {
            Vibrate().vibration(context)
            val cursor = dbHandler.getAllRow(context)
            cursor!!.moveToFirst()
            while (!cursor.isAfterLast) {
                val map = HashMap<String, String>()
                map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                inTimeDelete.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN)))
                outTimeDelete.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT)))
                breakTimeDelete.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK)))
                totalHoursDelete.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)))
                dayDelete.add(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DAY)))
                cursor.moveToNext()
            }

            dbHandler.deleteAll()
            try {
                val runnable = Runnable { (context as MainActivity).deleteAll() }
                MainActivity().runOnUiThread(runnable)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                Toast.makeText(context, "There was an error", Toast.LENGTH_SHORT).show()
            }

            val snackBar = Snackbar.make(
                items.itemView,
                context.getString(R.string.all_entries_deleted),
                Snackbar.LENGTH_LONG
            ).setDuration(5000)

            snackBar.setActionTextColor(
                Color.parseColor(CustomColorGenerator(context).generateSnackbarActionTextColor())
            )

            snackBar.setAction(context.getString(R.string.undo)) {
                Vibrate().vibration(context)
                GlobalScope.launch(Dispatchers.Main) {
                    for (i in inTimeDelete.indices) {
                        dbHandler.insertRow(
                            inTimeDelete.elementAt(i),
                            outTimeDelete.elementAt(i),
                            totalHoursDelete.elementAt(i),
                            dayDelete.elementAt(i),
                            breakTimeDelete.elementAt(i)
                        )
                    }

                    val runnable2 = Runnable { (context as MainActivity).undoDeleteAll() }
                    MainActivity().runOnUiThread(runnable2)
                }
            }
            snackBar.apply {
                snackBar.view.background = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.snackbar_corners,
                    context.theme
                )
            }
            snackBar.show()
            dialog.dismiss()
        }

        noButton.setOnClickListener {
            Vibrate().vibration(context)
            dialog.dismiss()
        }

        dialog.show()
        checkBoxVisible = false
    }

    private fun clickEntryWhenCheckboxVisible(holder: RecyclerView.ViewHolder) {
        if (checkBoxVisible) {
            Vibrate().vibration(context)
            snackbarDismissCheckBox = Snackbar.make(
                holder.itemView,
                context.getString(R.string.would_you_like_to_hide_check_boxes),
                Snackbar.LENGTH_INDEFINITE
            )

            if (selectedItemsList.isNotEmpty()) {
                var isInIt = false
                for (i in 0 until selectedItemsList.count()) {
                    if (selectedItemsList.elementAt(i) == holder.adapterPosition) {
                        isInIt = true
                    }
                }
                if (isInIt) {
                    val index = selectedItemsList.indexOf(holder.adapterPosition)
                    selectedItemsList.removeAt(index)
                } else {
                    selectedItemsList.add(holder.adapterPosition)
                }
            } else {
                selectedItemsList.add(holder.adapterPosition)
            }

            selectedItemsList.sortDescending()
            holder.itemView.findViewById<MaterialCheckBox>(R.id.checkbox).isChecked =
                holder.itemView.findViewById<MaterialCheckBox>(R.id.checkbox).isChecked != true

            if (selectedItemsList.isNotEmpty()) {
                if (selectedItemsList.count() <= 1) {
                    snackbarDeleteSelected.setText(
                        (selectedItemsList.count()).toString() + context.getString(
                            R.string.item_selected
                        )
                    )
                } else {
                    snackbarDeleteSelected.setText(
                        (selectedItemsList.count()).toString() + context.getString(
                            R.string.items_selected
                        )
                    )
                }
                snackbarDeleteSelected.show()
            } else if (selectedItemsList.isEmpty()) {
                snackbarDeleteSelected.dismiss()

                snackbarDismissCheckBox.setAction(context.getString(R.string.hide)) {
                    Vibrate().vibration(context)
                    checkBoxVisible = false
                    if (AnimationData(context).loadRecyclerViewAnimation()) {
                        notifyItemRangeChanged(0, dataList.size)
                    } else {
                        notifyDataSetChanged()
                    }

                    val hideNavigationIcon = Runnable {
                        (context as MainActivity).hideNavigationIcon()
                    }
                    MainActivity().runOnUiThread(hideNavigationIcon)
                }
                snackbarDismissCheckBox.apply {
                    snackbarDismissCheckBox.view.background = ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.snackbar_corners,
                        context.theme
                    )
                }

                snackbarDismissCheckBox.setActionTextColor(
                    Color.parseColor(CustomColorGenerator(context).generateSnackbarActionTextColor())
                )

                snackbarDismissCheckBox.show()
            }
        }
    }

    private fun longClickSelectAll(holder: RecyclerView.ViewHolder) {
        if (selectedItemsList.count() == dataList.count()) {
            selectedItemsList.clear()
            if (AnimationData(context).loadRecyclerViewAnimation()) {
                notifyItemRangeChanged(0, dataList.count())
            } else {
                notifyDataSetChanged()
            }

            snackbarDeleteSelected.dismiss()

            snackbarDismissCheckBox.setAction(context.getString(R.string.hide)) {
                Vibrate().vibration(context)
                checkBoxVisible = false

                if (AnimationData(context).loadRecyclerViewAnimation()) {
                    notifyItemRangeChanged(0, dataList.size)
                } else {
                    notifyDataSetChanged()
                }

                val hideNavigationIcon = Runnable {
                    (context as MainActivity).hideNavigationIcon()
                }
                MainActivity().runOnUiThread(hideNavigationIcon)
            }
            snackbarDismissCheckBox.apply {
                snackbarDismissCheckBox.view.background = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.snackbar_corners,
                    context.theme
                )
            }

            snackbarDismissCheckBox.setActionTextColor(
                Color.parseColor(CustomColorGenerator(context).generateSnackbarActionTextColor())
            )

            snackbarDismissCheckBox.show()
        } else {
            selectedItemsList.clear()
            selectedItemsList.add(holder.adapterPosition)
            for (i in 0 until dataList.count()) {
                selectedItemsList.add(i)
            }
            if (AnimationData(context).loadRecyclerViewAnimation()) {
                notifyItemRangeChanged(0, dataList.size)
            } else {
                notifyDataSetChanged()
            }

            if (selectedItemsList.isNotEmpty()) {
                var isInIt = false
                for (i in 0 until selectedItemsList.count()) {
                    isInIt = selectedItemsList.contains(holder.adapterPosition)
                }
                if (isInIt) {
                    if (selectedItemsList.contains(holder.adapterPosition)) {
                        val index = selectedItemsList.indexOf(holder.adapterPosition)
                        selectedItemsList.removeAt(index)
                    }
                } else {
                    selectedItemsList.add(holder.adapterPosition)
                }
            } else {
                selectedItemsList.add(holder.adapterPosition)
            }

            selectedItemsList.sortDescending()
            if (selectedItemsList.isNotEmpty()) {
                if (selectedItemsList.count() <= 1) {
                    snackbarDeleteSelected.setText((selectedItemsList.count()).toString() + " Item Selected")
                } else {
                    snackbarDeleteSelected.setText((selectedItemsList.count()).toString() + " Items Selected")
                }
                snackbarDeleteSelected.show()
            } else if (selectedItemsList.isEmpty()) {
                snackbarDeleteSelected.dismiss()

                snackbarDismissCheckBox.setAction(context.getString(R.string.hide)) {
                    Vibrate().vibration(context)
                    checkBoxVisible = false
                    if (AnimationData(context).loadRecyclerViewAnimation()) {
                        notifyItemRangeChanged(0, dataList.size)
                    } else {
                        notifyDataSetChanged()
                    }

                    val hideNavigationIcon = Runnable {
                        (context as MainActivity).hideNavigationIcon()
                    }
                    MainActivity().runOnUiThread(hideNavigationIcon)
                }
                snackbarDismissCheckBox.apply {
                    snackbarDismissCheckBox.view.background = ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.snackbar_corners,
                        context.theme
                    )
                }

                snackbarDismissCheckBox.setActionTextColor(
                    Color.parseColor(CustomColorGenerator(context).generateSnackbarActionTextColor())
                )

                snackbarDismissCheckBox.show()
            }
        }
    }

    @SuppressLint("Range")
    private fun openHourForEditing(
        holder: RecyclerView.ViewHolder,
        dbHandler: DBHelper
    ) {
        val itemPosition = holder.adapterPosition
        val map = HashMap<String, String>()
        dataList.clear()
        val cursor = dbHandler.getAllRow(context)
        cursor!!.moveToPosition(itemPosition)

        while (cursor.position == itemPosition) {
            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
            map["inTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
            map["outTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
            map["breakTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
            map["totalHours"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
            map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

            dataList.add(map)
            cursor.moveToNext()
        }
        if ((map["inTime"].toString()
                .contains(context.getString(R.string.AM)) || map["inTime"].toString()
                .contains(context.getString(R.string.PM))) &&
            (map["outTime"].toString()
                .contains(context.getString(R.string.AM)) || map["outTime"].toString()
                .contains(context.getString(R.string.PM)))
        ) {
            val bundle = Bundle()
            bundle.putInt("id", map["id"]!!.toInt())
            val editHoursFragment = EditHours()
            editHoursFragment.arguments = bundle
            ItemPositionData(context).setPosition(itemPosition)

            val manager =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            manager.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            manager.replace(R.id.fragment_container, editHoursFragment)
                .addToBackStack(null)
            manager.commit()

            try {
                val saveState = Runnable {
                    (context as MainActivity).saveState()
                }
                MainActivity().runOnUiThread(saveState)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.cant_edit_entry),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun longClick(
        inTime: MutableList<String>,
        outTime: MutableList<String>,
        breakTime: MutableList<String>,
        totalHours: MutableList<String>,
        day: MutableList<Long>,
        checkBox: CheckBox,
        holder: RecyclerView.ViewHolder
    ) {
        inTime.clear()
        outTime.clear()
        breakTime.clear()
        totalHours.clear()
        day.clear()
        selectedItemsList.clear()
        checkBoxVisible = true

        val historyCheckBox = Runnable {
            (context as MainActivity).checkBoxVisible(checkBoxVisible)
        }
        MainActivity().runOnUiThread(historyCheckBox)

        notifyItemRangeChanged(0, dataList.count())

        checkBox.isChecked = true
        selectedItemsList.add(holder.adapterPosition)

        snackbarDismissCheckBox = Snackbar.make(
            holder.itemView,
            context.getString(R.string.would_you_like_to_hide_checkboxes),
            Snackbar.LENGTH_INDEFINITE
        )

        snackbarDeleteSelected = Snackbar.make(
            holder.itemView,
            "Items selected",
            Snackbar.LENGTH_INDEFINITE
        )

        snackbarDeleteSelected.setActionTextColor(
            Color.parseColor(CustomColorGenerator(context).generateSnackbarActionTextColor())
        )

        snackbarDeleteSelected.apply {
            snackbarDeleteSelected.view.background = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.snackbar_corners,
                context.theme
            )
        }
        snackbarDeleteSelected.behavior = NoSwipeBehavior()

        if (selectedItemsList.count() <= 1) {
            snackbarDeleteSelected.setText((selectedItemsList.count()).toString() + " Item Selected")
        } else {
            snackbarDeleteSelected.setText((selectedItemsList.count()).toString() + " Items Selected")
        }
        snackbarDeleteSelected.show()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_recycler_view)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}
