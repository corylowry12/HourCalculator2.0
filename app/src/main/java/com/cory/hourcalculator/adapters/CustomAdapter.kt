package com.cory.hourcalculator.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.fragments.HistoryFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CustomAdapter(
    private val context: Context,
    private val dataList: ArrayList<HashMap<String, String>>,
    private val historyFragment: HistoryFragment
) : BaseAdapter() {

    private val inflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val dbHandler = DBHelper(context, null)
        val dataitem = dataList[position]

        //val vibrationData = VibrationData(context)

        //val rowView = inflater.inflate(R.layout.list_row, parent, false)
        val rowView = inflater.inflate(R.layout.list_row, parent, false)

        rowView.findViewById<TextView>(R.id.row_in).text = context.getString(R.string.in_time_adapter, dataitem["intime"])
        rowView.findViewById<TextView>(R.id.row_out).text = context.getString(R.string.out_time_adapter, dataitem["out"])
        rowView.findViewById<TextView>(R.id.row_break).text = context.getString(R.string.total_time_adapter, dataitem["break"])
        rowView.findViewById<TextView>(R.id.row_total).text = context.getString(R.string.total_time_adapter, dataitem["total"])
        rowView.findViewById<TextView>(R.id.row_day).text = context.getString(R.string.date_adapter, dataitem["day"])

        val imageView = rowView.findViewById<ImageView>(R.id.imageViewOptions)
        imageView.setOnClickListener {
            //vibration(vibrationData)
            val popup = PopupMenu(context, imageView)
            popup.inflate(R.menu.menu_history_options)
            popup.setOnMenuItemClickListener { item ->
                //vibration(vibrationData)
                when (item.itemId) {
                    R.id.menu2 -> {

                        dataList.clear()
                        val cursor = dbHandler.getAllRow(context)
                        if (cursor!!.count > 0) {
                            cursor.moveToPosition(position)

                            val map = HashMap<String, String>()
                            while (cursor.position == position) {

                                map["id"] =
                                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                                map["intime"] =
                                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                                map["out"] =
                                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                                map["total"] =
                                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                                map["day"] =
                                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
                                dataList.add(map)

                                dbHandler.deleteRow(map["id"].toString())

                                cursor.moveToNext()

                            }
                        }

                        val runnable = Runnable {
                            (context as MainActivity).update()
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                        }

                        MainActivity().runOnUiThread(runnable)
                    }
                    R.id.menu4 -> {
                        val alertDialog = MaterialAlertDialogBuilder(context)
                        alertDialog.setTitle("Delete All?")
                        alertDialog.setMessage("Would you like to delete all?")
                        alertDialog.setPositiveButton("Yes") { _, _ ->
                            //vibration(vibrationData)
                            dbHandler.deleteAll()
                            val runnable = Runnable {
                                (context as MainActivity).update()
                                Toast.makeText(context, "All Items Deleted", Toast.LENGTH_SHORT).show()
                            }
                            MainActivity().runOnUiThread(runnable)
                        }
                            .setNeutralButton("No") {_, _ ->
                                //vibration(vibrationData)
                            }
                        val alert = alertDialog.create()
                        alert.show()
                    }
                    /*R.id.menu5 -> {
                        dataList.clear()
                        val cursor = dbHandler.getAllRow(context)
                        cursor!!.moveToPosition(position)

                        val map = HashMap<String, String>()
                        while (cursor.position == position) {

                            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
                            map["intime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
                            map["out"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
                            map["total"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
                            map["day"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
                            dataList.add(map)

                            cursor.moveToNext()

                        }
                        if ((map["intime"].toString().contains(context.getString(R.string.am)) || map["intime"].toString().contains(context.getString(R.string.pm))) &&
                            (map["out"].toString().contains(context.getString(R.string.am)) || map["out"].toString().contains(context.getString(R.string.pm)))
                        ) {
                            val intent = Intent(context, EditActivity::class.java)
                            intent.putExtra("id", position.toString())
                            (context as HistoryActivity).startActivity(intent)
                            (context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

                        } else {
                            Toast.makeText(context, context.getString(R.string.cant_edit), Toast.LENGTH_SHORT).show()
                        }
                    }*/
                }
               true
            }
            popup.show()
        }

        rowView.tag = position
        return rowView
    }

   /* private fun vibration(vibrationData: VibrationData) {
        if (vibrationData.loadVibrationState()) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(5, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }*/
}