package com.cory.hourcalculator.classes

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.snackbar.Snackbar

class Snackbar {

    fun snackbar(context: Context, view: View) {

        val dbHandler = DBHelper(context, null)

        val undoHoursData = UndoHoursData(context)

        val snackbar =
            Snackbar.make(view, "Hour Deleted", Snackbar.LENGTH_LONG)
                .setDuration(5000)
                .setAnchorView(R.id.bottom_nav)
        snackbar.setAction("UNDO") {
            Vibrate().vibration(context)
            dbHandler.insertRow(
                undoHoursData.loadInTime(),
                undoHoursData.loadOutTime(),
                undoHoursData.loadTotalHours(),
                undoHoursData.loadDate(),
                undoHoursData.loadBreakTime()
            )
            val runnable = Runnable {
                (context as MainActivity).update()
            }
            MainActivity().runOnUiThread(runnable)
        }
        snackbar.setActionTextColor(
            ContextCompat.getColorStateList(
                context,
                AccentColor(context).snackbarActionTextColor()
            )
        )
        snackbar.show()
    }
}