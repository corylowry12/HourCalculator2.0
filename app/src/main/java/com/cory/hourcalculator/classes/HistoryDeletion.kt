package com.cory.hourcalculator.classes

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.database.DBHelper
import com.google.protobuf.LazyStringArrayList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryDeletion(context: Context) {

    private val dbHandler = DBHelper(context, null)

    private val daysWorkedPerWeek = DaysWorkedPerWeek(context)

    @SuppressLint("Range")
    fun deletion() : HashMap<String, String> {
        val numberToDelete = dbHandler.getCount() - daysWorkedPerWeek.loadDaysWorked()
        val cursor = dbHandler.automaticDeletion(numberToDelete)

        cursor!!.moveToFirst()

        val map = HashMap<String, String>()
        while (!cursor.isAfterLast) {

            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
            map["inTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
            map["outTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
            map["breakTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
            map["totalHours"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
            map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
            map["count"] = numberToDelete.toString()

            dbHandler.deleteRow(map["id"].toString())

            cursor.moveToNext()
        }

        return map
    }
}