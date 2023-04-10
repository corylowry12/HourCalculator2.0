package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class ShowPatchNotesOnAppLaunchData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setShowPatchNotesOnAppLaunch(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("showPatchNotesOnAppLaunch", state)
        editor.apply()
    }

    fun loadShowPatchNotesOnAppLaunch(): Boolean {
        val state = sharedPreferences.getBoolean("showPatchNotesOnAppLaunch", true)
        return (state)
    }
}