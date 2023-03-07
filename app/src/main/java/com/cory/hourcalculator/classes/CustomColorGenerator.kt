package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

class CustomColorGenerator(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setCustomHex(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("customColorHexCode", state)
        editor.apply()
    }

    fun loadCustomHex(): String {
        val state = sharedPreferences.getString("customColorHexCode", "#808080")
        return (state!!)
    }

    fun generateCardColor() : String {
        return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.7).toString()}"
    }

    fun generateBottomNavIconTintColor() : String {
        return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.7).toString()}"
    }

    fun generateBottomNavIconIndicatorColor(): String {
        return "#${lighten(Color.parseColor(loadCustomHex()), 0.5)}"
    }

    fun generateBottomNavTextColor() : String {
        return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.7).toString()}"
    }

    fun generateMenuTintColor() : String {
        return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()}"
    }

    fun generateCustomColorPrimary() : String {
        return loadCustomHex()
    }

    fun generateTopAppBarColor() : String {
        return loadCustomHex()
    }

    fun lighten(color: Int, fraction: Double): String {

        val newRed : Float = color.red + ((255 - color.red) * fraction.toFloat())
        val newGreen : Float = color.green + ((255 - color.green) * fraction.toFloat())
        val newBlue : Float = color.blue + ((255 - color.blue) * fraction.toFloat())
        val newColor = Color.rgb(newRed, newGreen, newBlue)
        val newColorFormatted = String.format("#%02X%02X%02X", newRed.toInt(), newGreen.toInt(), newBlue.toInt()).drop(1)

        return newColorFormatted
    }

    fun darken(color: Int, fraction: Double): String {

        val newRed : Float = color.red - ((0 + color.red) * fraction.toFloat())
        val newGreen : Float = color.green - ((0 + color.green) * fraction.toFloat())
        val newBlue : Float = color.blue - ((0 + color.blue) * fraction.toFloat())
        val newColor = Color.rgb(newRed, newGreen, newBlue)
        val newColorFormatted = String.format("#%02X%02X%02X", newRed.toInt(), newGreen.toInt(), newBlue.toInt()).drop(1)

        return newColorFormatted
    }
}