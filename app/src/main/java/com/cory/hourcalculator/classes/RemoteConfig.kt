package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class RemoteConfig(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setRemoteSystemColor(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("remoteSystemColor", state)
        editor.apply()
    }

    fun loadRemoteSystemColor(): String {
        val state = sharedPreferences.getString("remoteSystemColor", "0")
        return (state!!)
    }
}