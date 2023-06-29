package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import android.os.Build

class NavigationRailMenuGravityData(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setNavigationRailPosition(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("navigationRailMenuGravity", state)
        editor.apply()
    }

    fun loadNavigationRailPosition(): Int {
        val state = sharedPreferences.getInt("navigationRailMenuGravity", 0)

        return (state)
    }
}