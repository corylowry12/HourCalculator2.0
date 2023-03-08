package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.cory.hourcalculator.R

class CustomColorGenerator(context: Context) {

    val insideContext = context

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

    fun generateBottomNavBackgroundColor() : String {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#${
                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#${
                    this.darken(Color.parseColor(this.loadCustomHex()), 0.8).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#${
                            this.lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                }
            }
        }
        return generateCustomColorPrimary()
    }

    fun generateNavBarColor() : String {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#${
                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#${
                    this.darken(Color.parseColor(this.loadCustomHex()), 0.8).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#${
                            this.lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                }
            }
        }
        return generateCustomColorPrimary()
    }
    fun generateCardColor() : String {
        return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.5).toString()}"
    }

    fun generatePatchNotesCardColor() : String {
        return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.7).toString()}"
    }

    fun generateBottomNavIconTintColor() : String {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#${
                    this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#${
                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                }
            }
        }
    return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.7).toString()}"
    }

    fun generateBottomNavIconIndicatorColor(): String {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#${
                    this.darken(Color.parseColor(this.loadCustomHex()), 0.05).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#${
                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.1).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.05).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#${
                            this.lighten(Color.parseColor(this.loadCustomHex()), 0.1).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.1).toString()
                        }"
                    }
                }
            }
        }
        return "#${
            this.darken(Color.parseColor(this.loadCustomHex()), 0.05).toString()
        }"
    }

    fun generateBottomNavTextColor() : String {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#${
                    this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#${
                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                }
            }
        }
        return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.7).toString()}"
    }

    fun generateCollapsedToolBarTextColor() : String {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#${
                    this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#${
                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                }
            }
        }
        return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()}"
    }

    fun generateMenuTintColor() : String {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#${
                    this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#${
                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                }
            }
        }
        return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()}"
    }

    fun generateCustomColorPrimary() : String {
        return loadCustomHex()
    }

    fun generateTopAppBarColor() : String {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#${
                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#${
                    this.darken(Color.parseColor(this.loadCustomHex()), 0.8).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#${
                            this.lighten(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                }
            }
        }
        return generateCustomColorPrimary()
    }

    fun generateChipBackgroundColor() : String {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#${
                    this.darken(Color.parseColor(this.loadCustomHex()), 0.05).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#${
                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.1).toString()
                }"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#${
                            this.lighten(Color.parseColor(this.loadCustomHex()), 0.1).toString()
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.6).toString()
                        }"
                    }
                }
            }
        }
        return "#${lighten(Color.parseColor(loadCustomHex()), 0.5)}"
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