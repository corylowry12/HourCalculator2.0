package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences
import android.os.Build

class MatchImageViewContentsBackgroundData(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setMatchImageViewContents(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("matchImageViewContents", state)
        editor.apply()
    }

    fun loadMatchImageViewContents(): Boolean {
        val state = sharedPreferences.getBoolean("matchImageViewContents", false)

        return (state)
    }
}