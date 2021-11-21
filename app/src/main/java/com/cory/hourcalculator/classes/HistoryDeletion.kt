package com.cory.hourcalculator.classes

import android.annotation.SuppressLint
import android.content.Context
import com.cory.hourcalculator.database.DBHelper

class HistoryDeletion(context: Context) {

    private val dbHandler = DBHelper(context, null)

    private val dataList = ArrayList<HashMap<String, String>>()

    private val daysWorkedPerWeek = DaysWorkedPerWeek(context)

    @SuppressLint("Range")
    fun deletion() {
        val numberToDelete = dbHandler.getCount() - daysWorkedPerWeek.loadDaysWorked()
        dataList.clear()
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
            dataList.add(map)

            dbHandler.deleteRow(map["id"].toString())

            cursor.moveToNext()
        }
    }
}