package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class LinkData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setLink(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("Link", state!!)
        editor.apply()
    }

    fun loadLink(): String? {
        val state = sharedPreferences.getString("Link", "")
        return (state)
    }
}