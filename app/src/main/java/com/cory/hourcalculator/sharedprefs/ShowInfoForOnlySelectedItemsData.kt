package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class ShowInfoForOnlySelectedItemsData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setShowInfoForOnlySelectedItems(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("showInfoForOnlySelectedItems", state)
        editor.apply()
    }

    fun loadShowInfoForOnlySelectedItems(): Boolean {
        val state = sharedPreferences.getBoolean("showInfoForOnlySelectedItems", false)
        return (state)
    }
}