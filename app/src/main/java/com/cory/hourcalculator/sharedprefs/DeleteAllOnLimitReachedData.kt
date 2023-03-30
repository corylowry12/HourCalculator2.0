package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class DeleteAllOnLimitReachedData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setDeleteAllState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("deleteAllOnLimitReached", state)
        editor.apply()
    }

    fun loadDeleteAllState(): Boolean {
        val state = sharedPreferences.getBoolean("deleteAllOnLimitReached", false)
        return (state)
    }
}