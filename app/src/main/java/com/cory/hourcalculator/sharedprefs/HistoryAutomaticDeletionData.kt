package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class HistoryAutomaticDeletionData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setHistoryDeletionState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("History_Deletion_State", state)
        editor.apply()
    }

    fun loadHistoryDeletionState(): Boolean {
        val state = sharedPreferences.getBoolean("History_Deletion_State", false)
        return (state)
    }
}