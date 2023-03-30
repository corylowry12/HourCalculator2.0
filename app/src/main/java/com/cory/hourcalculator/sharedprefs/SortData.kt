package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class SortData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setSortState(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("SortMethod", state!!)
        editor.apply()
    }

    fun loadSortState(): String? {
        val state = sharedPreferences.getString("SortMethod", "date DESC")
        return (state)
    }
}