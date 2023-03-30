package com.cory.hourcalculator.classes

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.sharedprefs.DaysWorkedPerWeekData
import com.cory.hourcalculator.sharedprefs.DeleteAllOnLimitReachedData

class HistoryDeletion(context: Context) {
    val insideContext = context

    private val dbHandler = DBHelper(context, null)

    private val daysWorkedPerWeekData = DaysWorkedPerWeekData(context)

    @SuppressLint("Range")
    fun deletion() : HashMap<String, String> {
        var numberToDelete = dbHandler.getCount() - daysWorkedPerWeekData.loadDaysWorked()
        if (DeleteAllOnLimitReachedData(insideContext).loadDeleteAllState()) {
            numberToDelete = dbHandler.getCount()
            Toast.makeText(insideContext, "All entries deleted automatically", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(insideContext, "Entry deleted automatically", Toast.LENGTH_SHORT).show()
        }
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