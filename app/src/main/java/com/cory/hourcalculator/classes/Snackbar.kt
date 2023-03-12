package com.cory.hourcalculator.classes

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.cory.hourcalculator.R
import com.cory.hourcalculator.intents.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Snackbar {

    fun snackbar(context: Context, view: View) {



        val snackbar =
            Snackbar.make(view, context.getString(R.string.entry_deleted), Snackbar.LENGTH_LONG)
                .setDuration(5000)
                .setAnchorView(R.id.bottom_nav)

        snackbar.setAction(context.getString(R.string.undo)) {
            Vibrate().vibration(context)

            val runnable = Runnable {
                (context as MainActivity).undo()
            }
            MainActivity().runOnUiThread(runnable)

            val restoreState = Runnable {
                (context as MainActivity).restoreState()

            }

            MainActivity().runOnUiThread(restoreState)
        }
        snackbar.setActionTextColor(
            Color.parseColor(CustomColorGenerator(context).generateSnackbarActionTextColor())
        )
        snackbar.apply {
            snackbar.view.background = ResourcesCompat.getDrawable(context.resources, R.drawable.snackbar_corners, context.theme)
        }
        snackbar.show()
    }
}