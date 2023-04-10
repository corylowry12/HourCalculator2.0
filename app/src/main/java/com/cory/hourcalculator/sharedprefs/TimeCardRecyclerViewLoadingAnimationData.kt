package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class TimeCardRecyclerViewLoadingAnimationData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setTimeCardRecyclerViewLoadingAnimation(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("timeCardRecyclerViewLoadingAnimation", state)
        editor.apply()
    }

    fun loadTimeCardRecyclerViewLoadingAnimation(): Boolean {
        val state = sharedPreferences.getBoolean("timeCardRecyclerViewLoadingAnimation", false)
        return (state)
    }
}