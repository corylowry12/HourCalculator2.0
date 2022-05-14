package com.cory.hourcalculator.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.IdData
import com.cory.hourcalculator.classes.ItemPosition
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.fragments.EditHours
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.elementAt
import kotlin.collections.indices
import kotlin.collections.set
import kotlin.collections.toMutableList

@DelicateCoroutinesApi
class CustomAdapter(
    private val context: Context,
    private val dataList: ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lastPosition = -1
    var checkBoxVisible = false
    private var selectedItems  = arrayOf<Int>()
    private var selectedItemsList = selectedItems.toMutableList()

    private lateinit var snackbarDeleteSelected : Snackbar
    private lateinit var snackbarDismissCheckBox : Snackbar

    fun checkboxVisible() {
        checkBoxVisible = false
        notifyItemRangeChanged(0, dataList.size)
        snackbarDeleteSelected.dismiss()
        snackbarDismissCheckBox.dismiss()
    }

    fun isCheckBoxVisible() : Boolean {
        return checkBoxVisible
    }

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var inTime: TextView = itemView.findViewById(R.id.row_in)
        var outTime: TextView = itemView.findViewById(R.id.row_out)
        var breakTime: TextView = itemView.findViewById(R.id.row_break)
        var totalTime: TextView = itemView.findViewById(R.id.row_total)
        var date: TextView = itemView.findViewById(R.id.row_day)
        var checkBox : CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(position: Int) {

            val dataItem = dataList[position]

            val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
            val dateString = formatter.format(dataItem["date"]!!.toLong())

            inTime.text = context.getString(R.string.in_time_adapter, dataItem["inTime"])
            outTime.text = context.getString(R.string.out_time_adapter, dataItem["outTime"])
            breakTime.text = context.getString(R.string.break_time_adapter, dataItem["breakTime"])
            totalTime.text = context.getString(R.string.total_time_adapter, dataItem["totalHours"])
            date.text = context.getString(R.string.date_adapter, dateString.toString())

            if (checkBoxVisible) {
                checkBox.visibility = View.VISIBLE
            }
            else {
                checkBox.visibility = View.GONE
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

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setAnimation(holder.itemView, position)
        var isInflated = false
        val dbHandler = DBHelper(context, null)
        val listItems = arrayOf(context.getString(R.string.edit), context.getString(R.string.delete), context.getString(R.string.delete_all))
        val imageView = holder.itemView.findViewById<ImageView>(R.id.imageViewOptions)
        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.checkbox)

        checkBox.isChecked = selectedItemsList.contains(holder.adapterPosition)

        val inTime = arrayOf<String>().toMutableList()
        val outTime = arrayOf<String>().toMutableList()
        val breakTime = arrayOf<String>().toMutableList()
        val totalHours = arrayOf<String>().toMutableList()
        val day = arrayOf<Long>().toMutableList()

        if (!checkBoxVisible) {
            holder.itemView.setOnLongClickListener {
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

                snackbarDismissCheckBox = Snackbar.make(holder.itemView, "Would you like to hide checkboxes?", Snackbar.LENGTH_INDEFINITE)

                snackbarDeleteSelected =
                    Snackbar.make(
                        holder.itemView,
                        "Items selected",
                        Snackbar.LENGTH_INDEFINITE
                    )
                snackbarDeleteSelected.setActionTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        AccentColor(context).snackbarActionTextColor()
                    )
                )
                snackbarDeleteSelected.apply {
                    snackbarDeleteSelected.view.background = ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.snackbar_corners,
                        context.theme
                    )
                }
                snackbarDeleteSelected.behavior = NoSwipeBehavior()
                snackbarDeleteSelected.setAction("Delete") {
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
                            map["id"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                            map["inTime"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                            map["outTime"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                            map["breakTime"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                            map["totalHours"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                            map["date"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

                            inTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN)))

                            outTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT)))

                            breakTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK)))
                            totalHours.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)))

                            day.add(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DAY)))

                            dbHandler.deleteRow(map["id"].toString())
                            dataList.removeAt(selectedItemsList.elementAt(i))
                            notifyItemRemoved(selectedItemsList.elementAt(i))
                            checkBoxVisible = false
                            checkBox.isChecked = false
                        }

                    }

                    checkBoxVisible = false
                    notifyItemRangeChanged(0, dataList.size)
                    val entriesDeleted: String = if (selectedItemsList.count() == 1) {
                        selectedItemsList.count().toString() + " Entry Deleted"
                    } else {
                        selectedItemsList.count().toString() + " Entries Deleted"
                    }
                        val snackBar = Snackbar.make(
                            snackbarDeleteSelected.view,
                            entriesDeleted,
                            Snackbar.LENGTH_LONG
                        )
                            .setDuration(5000)

                        snackBar.setActionTextColor(
                            ContextCompat.getColorStateList(
                                context,
                                AccentColor(context).snackbarActionTextColor()
                            )
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
                                        map2["id"] =
                                            cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_ID))
                                        map2["inTime"] =
                                            cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_IN))
                                        map2["outTime"] =
                                            cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_OUT))
                                        map2["breakTime"] =
                                            cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_BREAK))
                                        map2["totalHours"] =
                                            cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_TOTAL))
                                        map2["date"] =
                                            cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_DAY))
                                        dataList.add(map2)

                                        cursor2.moveToNext()
                                    }
                                }

                            for (i in 0 until selectedItemsList.count()) {
                                notifyItemInserted(selectedItemsList.elementAt(i))
                            }

                            selectedItemsList.clear()

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

                    val handler = android.os.Handler(Looper.getMainLooper())
                    val runnable = Runnable {
                        selectedItemsList.clear()
                    }
                    handler.postDelayed(runnable, 5000)
                }
                if (selectedItemsList.count() <= 1) {
                    snackbarDeleteSelected.setText((selectedItemsList.count()).toString() + " Item Selected")
                }
                else {
                    snackbarDeleteSelected.setText((selectedItemsList.count()).toString() + " Items Selected")
                }
                snackbarDeleteSelected.show()
                true
            }
        }

            checkBox.setOnLongClickListener {

                selectedItemsList.clear()
                selectedItemsList.add(holder.adapterPosition)
                for (i in 0 until dataList.count()) {
                    selectedItemsList.add(i)
                }
                notifyItemRangeChanged(0, dataList.size)

                snackbarDeleteSelected.setAction("Delete") {
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
                            map["id"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                            map["inTime"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                            map["outTime"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                            map["breakTime"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                            map["totalHours"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                            map["date"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

                            inTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN)))

                            outTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT)))

                            breakTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK)))
                            totalHours.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)))

                            day.add(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DAY)))

                            dbHandler.deleteRow(map["id"].toString())
                            dataList.removeAt(selectedItemsList.elementAt(i))
                            notifyItemRemoved(selectedItemsList.elementAt(i))

                        }
                        checkBoxVisible = false
                        notifyItemRangeChanged(0, dataList.size)
                    }

                    val entriesDeleted: String = if (selectedItemsList.count() == 1) {
                        selectedItemsList.count().toString() + " Entry Deleted"
                    } else {
                        selectedItemsList.count().toString() + " Entries Deleted"
                    }
                    val snackBar = Snackbar.make(
                        snackbarDeleteSelected.view,
                        entriesDeleted,
                        Snackbar.LENGTH_LONG
                    )
                        .setDuration(5000)

                    snackBar.setActionTextColor(
                        ContextCompat.getColorStateList(
                            context,
                            AccentColor(context).snackbarActionTextColor()
                        )
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
                                map2["id"] =
                                    cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_ID))
                                map2["inTime"] =
                                    cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_IN))
                                map2["outTime"] =
                                    cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_OUT))
                                map2["breakTime"] =
                                    cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_BREAK))
                                map2["totalHours"] =
                                    cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_TOTAL))
                                map2["date"] =
                                    cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_DAY))
                                dataList.add(map2)

                                cursor2.moveToNext()
                            }
                        }

                        for (i in 0 until selectedItemsList.count()) {
                            notifyItemInserted(selectedItemsList.elementAt(i))
                        }
                        selectedItemsList.clear()

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

                    val handler = android.os.Handler(Looper.getMainLooper())
                    val runnable = Runnable {
                        selectedItemsList.clear()
                    }
                    handler.postDelayed(runnable, 5000)
                }

                if (selectedItemsList.count() > 0) {
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
                if (selectedItemsList.count() > 0) {
                    if (selectedItemsList.count() <= 1) {
                        snackbarDeleteSelected.setText((selectedItemsList.count()).toString() + " Item Selected")
                    } else {
                        snackbarDeleteSelected.setText((selectedItemsList.count()).toString() + " Items Selected")
                    }
                    snackbarDeleteSelected.show()
                } else if (selectedItemsList.count() == 0) {
                    snackbarDeleteSelected.dismiss()

                    snackbarDismissCheckBox.setAction("Hide") {
                        checkBoxVisible = false
                        notifyItemRangeChanged(0, dataList.size)
                    }
                    snackbarDismissCheckBox.apply {
                        snackbarDismissCheckBox.view.background = ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.snackbar_corners,
                            context.theme
                        )
                    }
                    snackbarDismissCheckBox.setActionTextColor(
                        ContextCompat.getColorStateList(
                            context,
                            AccentColor(context).snackbarActionTextColor()
                        )
                    )
                    snackbarDismissCheckBox.show()
                }

                return@setOnLongClickListener true
            }

        checkBox.setOnClickListener {
            Vibrate().vibration(context)
            snackbarDismissCheckBox = Snackbar.make(holder.itemView, context.getString(R.string.would_you_like_to_hide_check_boxes), Snackbar.LENGTH_INDEFINITE)

            snackbarDeleteSelected.setAction(context.getString(R.string.delete)) {
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
                        map["id"] =
                            cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                        map["inTime"] =
                            cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                        map["outTime"] =
                            cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                        map["breakTime"] =
                            cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                        map["totalHours"] =
                            cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                        map["date"] =
                            cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

                        inTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN)))

                        outTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT)))

                        breakTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK)))
                        totalHours.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)))

                        day.add(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DAY)))

                        dbHandler.deleteRow(map["id"].toString())
                        dataList.removeAt(selectedItemsList.elementAt(i))
                        notifyItemRemoved(selectedItemsList.elementAt(i))

                    }
                    checkBoxVisible = false
                    notifyItemRangeChanged(0, dataList.size)
                }

                val entriesDeleted: String = if (selectedItemsList.count() == 1) {
                    selectedItemsList.count().toString() + " Entry Deleted"
                } else {
                    selectedItemsList.count().toString() + " Entries Deleted"
                }
                val snackBar = Snackbar.make(
                    snackbarDeleteSelected.view,
                    entriesDeleted,
                    Snackbar.LENGTH_LONG
                )
                    .setDuration(5000)

                snackBar.setActionTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        AccentColor(context).snackbarActionTextColor()
                    )
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
                            map2["id"] =
                                cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_ID))
                            map2["inTime"] =
                                cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_IN))
                            map2["outTime"] =
                                cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_OUT))
                            map2["breakTime"] =
                                cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_BREAK))
                            map2["totalHours"] =
                                cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_TOTAL))
                            map2["date"] =
                                cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_DAY))
                            dataList.add(map2)

                            cursor2.moveToNext()
                        }
                    }

                    for (i in 0 until selectedItemsList.count()) {
                        notifyItemInserted(selectedItemsList.elementAt(i))
                    }
                    selectedItemsList.clear()

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

                val handler = android.os.Handler(Looper.getMainLooper())
                val runnable = Runnable {
                    selectedItemsList.clear()
                }
                handler.postDelayed(runnable, 5000)
            }

                if (selectedItemsList.count() > 0) {
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
                if (selectedItemsList.count() > 0) {
                    if (selectedItemsList.count() <= 1) {
                        snackbarDeleteSelected.setText((selectedItemsList.count()).toString() + " Item Selected")
                    }
                    else {
                        snackbarDeleteSelected.setText((selectedItemsList.count()).toString() + " Items Selected")
                    }
                    snackbarDeleteSelected.show()
                } else if (selectedItemsList.count() == 0) {
                    snackbarDeleteSelected.dismiss()

                    snackbarDismissCheckBox.setAction("Hide") {
                        checkBoxVisible = false
                        notifyItemRangeChanged(0, dataList.size)
                    }
                    snackbarDismissCheckBox.apply {
                        snackbarDismissCheckBox.view.background = ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.snackbar_corners,
                            context.theme
                        )
                    }
                    snackbarDismissCheckBox.setActionTextColor(
                        ContextCompat.getColorStateList(
                            context,
                            AccentColor(context).snackbarActionTextColor()
                        )
                    )
                    snackbarDismissCheckBox.show()
                }
        }

        imageView.setOnClickListener {
            Vibrate().vibration(context)

            val popupWindowAdapter = ArrayAdapter(context, R.layout.historypopupwindow, R.id.details, listItems)
            val listPopupWindow = ListPopupWindow(context)
            listPopupWindow.setAdapter(popupWindowAdapter)
            listPopupWindow.anchorView = imageView
          listPopupWindow.width = holder.itemView.width / 2
            listPopupWindow.height = ListPopupWindow.WRAP_CONTENT
            listPopupWindow.setDropDownGravity(Gravity.NO_GRAVITY)
            if (isInflated) {
                listPopupWindow.dismiss()
                isInflated = false
            }
            else {
                listPopupWindow.setOnItemClickListener { _, _, p2, _ ->
                    val itemPosition = holder.adapterPosition
                    if (p2 == 0) {
                        Vibrate().vibration(context)
                        listPopupWindow.dismiss()
                        val map = HashMap<String, String>()
                        dataList.clear()
                        val cursor = dbHandler.getAllRow(context)
                        cursor!!.moveToPosition(itemPosition)

                        while (cursor.position == itemPosition) {

                            map["id"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                            map["inTime"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                            map["outTime"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                            map["breakTime"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                            map["totalHours"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                            map["date"] =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

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

                            val itemPositionData = ItemPosition(context)
                            itemPositionData.setPosition(itemPosition)
                            IdData(context).setID(map["id"]!!.toInt())
                            val manager =
                                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                            manager.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            manager.replace(R.id.fragment_container, EditHours())
                                .addToBackStack(null)
                            manager.commit()

                            val saveState = Runnable {
                                (context as MainActivity).saveState()

                            }

                            MainActivity().runOnUiThread(saveState)

                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.cant_edit_entry),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (p2 == 1) {
                        listPopupWindow.dismiss()
                        Vibrate().vibration(context)
                        try {
                            var intimeDelete = ""
                            var outtimeDelete = ""
                            var breaktimeDelete = ""
                            var totalhoursDelete = ""
                            var dayDelete = 0L

                            val map = HashMap<String, String>()
                            val cursor = dbHandler.getAllRow(context)
                            if (cursor!!.count > 0) {
                                cursor.moveToPosition(holder.adapterPosition)

                                while (cursor.position == holder.adapterPosition) {

                                    map["id"] =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                                    map["inTime"] =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                                    map["outTime"] =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                                    map["breakTime"] =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                                    map["totalHours"] =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                                    map["date"] =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

                                    intimeDelete =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                                    outtimeDelete =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                                    breaktimeDelete =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                                    totalhoursDelete =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                                    dayDelete =
                                        cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

                                    dbHandler.deleteRow(map["id"].toString())
                                    cursor.moveToNext()
                                }
                            }

                            val saveState = Runnable {
                                (context as MainActivity).saveState()

                            }

                            MainActivity().runOnUiThread(saveState)

                            dataList.removeAt(holder.adapterPosition)
                            notifyItemRemoved(holder.adapterPosition)
                            checkBoxVisible = false

                            val snackbar =
                                Snackbar.make(
                                    holder.itemView,
                                    context.getString(R.string.entry_deleted),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setDuration(5000)

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
                                    map2["id"] =
                                        cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_ID))
                                    map2["inTime"] =
                                        cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_IN))
                                    map2["outTime"] =
                                        cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_OUT))
                                    map2["breakTime"] =
                                        cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_BREAK))
                                    map2["totalHours"] =
                                        cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_TOTAL))
                                    map2["date"] =
                                        cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_DAY))
                                    dataList.add(map2)

                                    cursor2.moveToNext()

                                }

                                notifyItemInserted(itemPosition)

                                val restoreState = Runnable {
                                    (context as MainActivity).restoreState()

                                }

                                MainActivity().runOnUiThread(restoreState)

                            }
                            snackbar.setActionTextColor(
                                ContextCompat.getColorStateList(
                                    context,
                                    AccentColor(context).snackbarActionTextColor()
                                )
                            )
                            snackbar.apply {
                                snackbar.view.background = ResourcesCompat.getDrawable(
                                    context.resources,
                                    R.drawable.snackbar_corners,
                                    context.theme
                                )
                            }
                            snackbar.show()
                            notifyItemRangeChanged(0, dataList.size)

                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.there_was_an_error_deleting_this_entry),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (p2 == 2) {
                        listPopupWindow.dismiss()
                        Vibrate().vibration(context)
                        val inTimeDelete = arrayOf<String>().toMutableList()
                        val outTimeDelete = arrayOf<String>().toMutableList()
                        val breakTimeDelete = arrayOf<String>().toMutableList()
                        val totalHoursDelete = arrayOf<String>().toMutableList()
                        val dayDelete = arrayOf<Long>().toMutableList()

                        val alertDialog = MaterialAlertDialogBuilder(
                            context,
                            AccentColor(context).alertTheme()
                        )
                        alertDialog.setTitle(context.getString(R.string.warning))
                        alertDialog.setMessage(context.getString(R.string.would_you_like_to_delete_all))
                        alertDialog.setCancelable(false)
                        alertDialog.setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                            Vibrate().vibration(context)

                            val cursor = dbHandler.getAllRow(context)
                            cursor!!.moveToFirst()

                            while (!cursor.isAfterLast) {
                                val map = HashMap<String, String>()
                                map["id"] =
                                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                                inTimeDelete.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN)))

                                outTimeDelete.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT)))

                                breakTimeDelete.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK)))
                                totalHoursDelete.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)))

                                dayDelete.add(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DAY)))


                                cursor.moveToNext()
                            }

                            dbHandler.deleteAll()
                            val runnable = Runnable {
                                (context as MainActivity).deleteAll()
                            }
                            MainActivity().runOnUiThread(runnable)

                            val snackBar = Snackbar.make(
                                holder.itemView,
                                context.getString(R.string.all_entries_deleted),
                                Snackbar.LENGTH_LONG
                            )
                                .setDuration(5000)

                            snackBar.setActionTextColor(
                                ContextCompat.getColorStateList(
                                    context,
                                    AccentColor(context).snackbarActionTextColor()
                                )
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

                                    val runnable2 = Runnable {
                                        (context as MainActivity).undoDeleteAll()
                                    }
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
                        }
                            .setNeutralButton(context.getString(R.string.no)) { _, _ ->
                                Vibrate().vibration(context)
                            }
                        val alert = alertDialog.create()
                        alert.show()

                        checkBoxVisible = false
                    }
                }
                listPopupWindow.show()
                isInflated = true
            }
        }
        (holder as ViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {

        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_recycler_view)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}