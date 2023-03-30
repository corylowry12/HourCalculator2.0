package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class VersionData(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setVersion(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("VersionData", state)
        editor.apply()
    }

    fun loadVersion(): String {
        val state = sharedPreferences.getString("VersionData", "")
        return (state.toString())
    }
}