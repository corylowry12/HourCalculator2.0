package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class UserAddedColors(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun addColor(state: MutableSet<String>) {
        val editor = sharedPreferences.edit()
        editor.putStringSet("userAddedColors", state)
        editor.apply()
    }

    fun loadColors(): MutableSet<String>? {
        val state = sharedPreferences.getStringSet("userAddedColors", emptySet())
        return (state)
    }

    fun clear() {
        sharedPreferences.edit().remove("userAddedColors").commit()
    }

    fun clearHash() {
        sharedPreferences.edit().remove("userAddedColorsMap").commit()
    }

    fun insert(jsonMap: ArrayList<HashMap<String, String>>) {
        val jsonString = Gson().toJson(jsonMap)
        val editor = sharedPreferences.edit()
        editor.putString("userAddedColorsMap", jsonString)
        editor.apply()
    }

    fun read(): ArrayList<HashMap<String, String>> {
        val defValue =
            Gson().toJson(ArrayList<HashMap<String, List<String>>>())
        val json = sharedPreferences.getString("userAddedColorsMap", defValue)
        val token: TypeToken<ArrayList<HashMap<String, String>>> = object : TypeToken<ArrayList<HashMap<String, String>>>() {}
        return Gson().fromJson(json, token.type)
    }
}