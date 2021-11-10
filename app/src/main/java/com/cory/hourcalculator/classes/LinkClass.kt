package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class LinkClass(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    //this saves the name preference
    fun setLink(state: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("Link", state!!)
        editor.apply()
    }

    // this will load the name state
    fun loadLink(): String? {
        val state = sharedPreferences.getString("Link", "")
        return (state)
    }
}