package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class MoreColorfulBackgroundData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setMoreColorfulBackground(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("moreColorfulBackgroundData", state)
        editor.apply()
    }

    fun loadMoreColorfulBackground(): Boolean {
        val state = sharedPreferences.getBoolean("moreColorfulBackgroundData", false)
        return (state)
    }
}