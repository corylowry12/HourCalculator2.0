package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class HistoryRecyclerViewLoadingAnimationData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setHistoryRecyclerViewLoadingAnimation(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("historyRecyclerViewLoadingAnimation", state)
        editor.apply()
    }

    fun loadHistoryRecyclerViewLoadingAnimation(): Boolean {
        val state = sharedPreferences.getBoolean("historyRecyclerViewLoadingAnimation", false)
        return (state)
    }
}