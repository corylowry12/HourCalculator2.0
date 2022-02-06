package com.cory.hourcalculator.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var inTime: TextView = itemView.findViewById(R.id.row_in)
        var outTime: TextView = itemView.findViewById(R.id.row_out)
        var breakTime: TextView = itemView.findViewById(R.id.row_break)
        var totalTime: TextView = itemView.findViewById(R.id.row_total)
        var date: TextView = itemView.findViewById(R.id.row_day)

        fun bind(position: Int) {

            val dataItem = dataList[position]

            val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
            val dateString = formatter.format(dataItem["date"]!!.toLong())

            inTime.text = context.getString(R.string.in_time_adapter, dataItem["inTime"])
            outTime.text = context.getString(R.string.out_time_adapter, dataItem["outTime"])
            breakTime.text = context.getString(R.string.break_time_adapter, dataItem["breakTime"])
            totalTime.text = context.getString(R.string.total_time_adapter, dataItem["totalHours"])
            date.text = context.getString(R.string.date_adapter, dateString.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_row, parent, false))
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setAnimation(holder.itemView, position)
        val dbHandler = DBHelper(context, null)

        val imageView = holder.itemView.findViewById<ImageView>(R.id.imageViewOptions)
        imageView.setOnClickListener {
            Vibrate().vibration(context)
            val context12 =
                ContextThemeWrapper(context, AccentColor(context).menuTheme(context))
            val popup = PopupMenu(context12, imageView)
            popup.inflate(R.menu.menu_history_options)
            popup.setOnMenuItemClickListener { item ->
                Vibrate().vibration(context)
                when (item.itemId) {
                    R.id.menu2 -> {
                        try {
                            var inTime = ""
                            var outTime = ""
                            var breakTime = ""
                            var totalHours = ""
                            var day = 0L

                            val map = HashMap<String, String>()
                            val cursor = dbHandler.getAllRow(context)
                            if (cursor!!.count > 0) {
                                cursor.moveToPosition(position)

                                while (cursor.position == position) {

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

                                    inTime =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                                    outTime =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                                    breakTime =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                                    totalHours =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                                    day =
                                        cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

                                    dbHandler.deleteRow(map["id"].toString())

                                    cursor.moveToNext()


                                }
                            }

                            val saveState = Runnable {
                                (context as MainActivity).saveState()

                            }

                            MainActivity().runOnUiThread(saveState)

                            dataList.removeAt(position)
                            notifyItemRemoved(position)

                            val snackbar =
                                Snackbar.make(
                                    holder.itemView,
                                    context.getString(R.string.entry_deleted),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setDuration(5000)
                                    .setAnchorView(R.id.bottom_nav)
                            snackbar.setAction(context.getString(R.string.undo)) {
                                Vibrate().vibration(context)
                                dbHandler.insertRow(inTime, outTime, totalHours, day, breakTime)
                                dataList.add(map)
                                notifyItemInserted(position)
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
                            snackbar.show()

                        }
                        catch (e: Exception) {
                            Toast.makeText(context, "There was an error deleting this entry", Toast.LENGTH_SHORT).show()
                        }
                    }
                    R.id.menu4 -> {

                        val inTime = arrayOf<String>().toMutableList()
                        val outTime = arrayOf<String>().toMutableList()
                        val breakTime = arrayOf<String>().toMutableList()
                        val totalHours = arrayOf<String>().toMutableList()
                        val day = arrayOf<Long>().toMutableList()

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
                                inTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN)))

                                outTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT)))

                                breakTime.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK)))
                                totalHours.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)))

                                day.add(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DAY)))


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
                                .setAnchorView(R.id.bottom_nav)

                            snackBar.setActionTextColor(
                                ContextCompat.getColorStateList(
                                    context,
                                    AccentColor(context).snackbarActionTextColor()
                                )
                            )
                            snackBar.setAction(context.getString(R.string.undo)) {
                                Vibrate().vibration(context)
                                GlobalScope.launch(Dispatchers.Main) {
                                    for (i in inTime.indices) {
                                        dbHandler.insertRow(
                                            inTime.elementAt(i),
                                            outTime.elementAt(i),
                                            totalHours.elementAt(i),
                                            day.elementAt(i),
                                            breakTime.elementAt(i)
                                        )
                                    }

                                    val runnable2 = Runnable {
                                        (context as MainActivity).undoDeleteAll()
                                    }
                                    MainActivity().runOnUiThread(runnable2)
                                }
                            }
                            snackBar.show()
                        }
                            .setNeutralButton(context.getString(R.string.no)) { _, _ ->
                                Vibrate().vibration(context)
                            }
                        val alert = alertDialog.create()
                        alert.show()
                    }
                    R.id.menu5 -> {
                        val map = HashMap<String, String>()
                        dataList.clear()
                        val cursor = dbHandler.getAllRow(context)
                        cursor!!.moveToPosition(position)

                        while (cursor.position == position) {

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
                            itemPositionData.setPosition(position)
                            IdData(context).setID(map["id"]!!.toInt())
                            val manager =
                                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                            manager.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            manager.replace(R.id.fragment_container, EditHours())
                                .addToBackStack(null)
                            manager.commit()

                        } else {
                            Toast.makeText(context, context.getString(R.string.cant_edit_entry), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                true
            }
            popup.show()
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