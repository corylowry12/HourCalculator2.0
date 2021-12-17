package com.cory.hourcalculator.adapters

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.get
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
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@DelicateCoroutinesApi
/*class CustomAdapter(
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

        var rowView: View? = null

        if (rowView == null) {
            val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            rowView = vi.inflate(R.layout.list_row, parent, false)

            val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
            val dateString = formatter.format(dataitem["date"]!!.toLong())

            rowView.findViewById<TextView>(R.id.row_in).text =
                context.getString(R.string.in_time_adapter, dataitem["inTime"])
            rowView.findViewById<TextView>(R.id.row_out).text =
                context.getString(R.string.out_time_adapter, dataitem["outTime"])
            rowView.findViewById<TextView>(R.id.row_break).text =
                context.getString(R.string.break_time_adapter, dataitem["breakTime"])
            rowView.findViewById<TextView>(R.id.row_total).text =
                context.getString(R.string.total_time_adapter, dataitem["totalHours"])
            rowView.findViewById<TextView>(R.id.row_day).text =
                context.getString(R.string.date_adapter,  dateString.toString())

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

                            val runnable = Runnable {
                                (context as MainActivity).update()

                            }

                            MainActivity().runOnUiThread(runnable)

                            val snackbar =
                                Snackbar.make(rowView, "Hour Deleted", Snackbar.LENGTH_LONG)
                                    .setDuration(5000)
                                    .setAnchorView(R.id.bottom_nav)
                            snackbar.setAction("UNDO") {
                                Vibrate().vibration(context)
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
                            val day = arrayOf<Long>().toMutableList()

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

                                    day.add(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DAY)))


                                    cursor.moveToNext()
                                }

                                dbHandler.deleteAll()
                                val runnable = Runnable {
                                    (context as MainActivity).deleteAll()
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

    override fun hasStableIds(): Boolean {
        return true
    }
}*/

class CustomAdapter(private val context: Context, private val dataList: ArrayList<HashMap<String, String>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lastPosition = -1

    private inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var inTime: TextView = itemView.findViewById(R.id.row_in)
        var outTime: TextView = itemView.findViewById(R.id.row_out)
        var breakTime: TextView = itemView.findViewById(R.id.row_break)
        var totalTime: TextView = itemView.findViewById(R.id.row_total)
        var date: TextView = itemView.findViewById(R.id.row_day)

        fun bind(position: Int) {
            // This method will be called anytime a list item is created or update its data
            //Do your stuff here

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
                            Snackbar.make(holder.itemView, "Hour Deleted", Snackbar.LENGTH_LONG)
                                .setDuration(5000)
                                .setAnchorView(R.id.bottom_nav)
                        snackbar.setAction("UNDO") {
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
                                "All Hours Deleted",
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
                            snackBar.setAction("UNDO") {
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

                            val itemPositionData = ItemPosition(context)
                            itemPositionData.setPosition(position)
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