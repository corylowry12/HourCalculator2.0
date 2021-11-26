package com.cory.hourcalculator.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.BoringLayout
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.fragments.AppearanceFragment
import com.cory.hourcalculator.fragments.EditHours
import com.cory.hourcalculator.fragments.HistoryFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.IdData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.classes.Vibrate


class CustomAdapter(
    private val context: Context,
    private val dataList: ArrayList<HashMap<String, String>>
) : BaseAdapter() {

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("Range")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val dbHandler = DBHelper(context, null)
        val dataitem = dataList[position]

        var rowView = convertView
        if (rowView == null) {
            val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            rowView = vi.inflate(R.layout.list_row, parent, false)

            //rowView = inflater.inflate(R.layout.list_row, parent, false)

            rowView.findViewById<TextView>(R.id.row_in).text =
                context.getString(R.string.in_time_adapter, dataitem["inTime"])
            rowView.findViewById<TextView>(R.id.row_out).text =
                context.getString(R.string.out_time_adapter, dataitem["outTime"])
            rowView.findViewById<TextView>(R.id.row_break).text =
                context.getString(R.string.break_time_adapter, dataitem["breakTime"])
            rowView.findViewById<TextView>(R.id.row_total).text =
                context.getString(R.string.total_time_adapter, dataitem["totalHours"])
            rowView.findViewById<TextView>(R.id.row_day).text =
                context.getString(R.string.date_adapter, dataitem["date"])

            val imageView = rowView.findViewById<ImageView>(R.id.imageViewOptions)
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

                            var inTime = ""
                            var outTime = ""
                            var breakTime = ""
                            var totalHours = ""
                            var day = ""

                            dataList.clear()
                            val cursor = dbHandler.getAllRow(context)
                            if (cursor!!.count > 0) {
                                cursor.moveToPosition(position)

                                val map = HashMap<String, String>()
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

                                    inTime =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                                    outTime =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                                    breakTime =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
                                    totalHours =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                                    day =
                                        cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))

                                    dbHandler.deleteRow(map["id"].toString())

                                    cursor.moveToNext()

                                }
                            }

                            val runnable = Runnable {
                                (context as MainActivity).update()

                            }

                            MainActivity().runOnUiThread(runnable)

                            val snackbar =
                                Snackbar.make(rowView, "Hour Deleted", Snackbar.LENGTH_LONG)
                                    .setDuration(5000)
                                    .setAnchorView(R.id.bottom_nav)
                            snackbar.setAction("UNDO") {
                                dbHandler.insertRow(inTime, outTime, totalHours, day, breakTime)
                                MainActivity().runOnUiThread(runnable)
                            }
                            snackbar.setActionTextColor(
                                ContextCompat.getColorStateList(context, AccentColor(context).snackbarActionTextColor())
                            )
                            snackbar.show()


                        }
                        R.id.menu4 -> {

                            val inTime = arrayOf<String>().toMutableList()
                            val outTime = arrayOf<String>().toMutableList()
                            val breakTime = arrayOf<String>().toMutableList()
                            val totalHours = arrayOf<String>().toMutableList()
                            val day = arrayOf<String>().toMutableList()

                            val alertDialog = MaterialAlertDialogBuilder(
                                context,
                                AccentColor(context).alertTheme()
                            )
                            alertDialog.setTitle("Delete All?")
                            alertDialog.setMessage("Would you like to delete all?")
                            alertDialog.setCancelable(false)
                            alertDialog.setPositiveButton("Yes") { _, _ ->
                                //vibration(vibrationData)

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

                                    day.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY)))


                                    cursor.moveToNext()
                                }

                                dbHandler.deleteAll()
                                val runnable = Runnable {
                                    (context as MainActivity).update()
                                }
                                MainActivity().runOnUiThread(runnable)

                                val snackBar = Snackbar.make(
                                    rowView,
                                    "All Hours Deleted",
                                    Snackbar.LENGTH_LONG
                                )
                                    .setDuration(5000)
                                    .setAnchorView(R.id.bottom_nav)

                                snackBar.setActionTextColor(
                                    ContextCompat.getColorStateList(context, AccentColor(context).snackbarActionTextColor())
                                )
                                snackBar.setAction("UNDO") {

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

                                        MainActivity().runOnUiThread(runnable)
                                    }
                                }
                                snackBar.show()
                            }
                                .setNeutralButton("No") { _, _ ->
                                    //vibration(vibrationData)
                                }
                            val alert = alertDialog.create()
                            alert.show()
                        }
                        R.id.menu5 -> {
                            dataList.clear()
                            val cursor = dbHandler.getAllRow(context)
                            cursor!!.moveToPosition(position)

                            val map = HashMap<String, String>()
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

                                val idData = IdData(context)
                                idData.setID(position)
                                val manager =
                                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                                manager.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                manager.replace(R.id.fragment_container, EditHours())
                                    .addToBackStack(null)
                                manager.commit()

                            } else {
                                Toast.makeText(context, "Cant edit", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    true
                }
                popup.show()
            }

            rowView.tag = position
            return rowView
        }
        return rowView
    }
}