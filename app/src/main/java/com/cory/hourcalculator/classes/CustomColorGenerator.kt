package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.random.Random

class CustomColorGenerator(context: Context) {

    val insideContext = context

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setCustomHex(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("customColor", state)
        editor.apply()
    }

    fun setRandomGeneratedHex(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("randomHex", state)
        editor.apply()
    }

    fun loadRandomHex(): String {
        val state = sharedPreferences.getString("randomHex", "#53c8c8")
        return (state!!)
    }

    fun generateARandomColor() {
        val red = Random.nextInt(50,200)
        val green = Random.nextInt(50,200)
        val blue = Random.nextInt(50,200)
        val hex = String.format("#%02X%02X%02X", red, green, blue)
        setRandomGeneratedHex(hex)
    }

    fun loadCustomHex(): String {
        if (GenerateARandomColorData(insideContext).loadGenerateARandomColorOnAppLaunch()) {
            return loadRandomHex()
        }
        val state = sharedPreferences.getString("customColor", "#53c8c8")
        return (state!!)
    }

    fun generateBackgroundColor() : String {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#ffffff"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#000000"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#ffffff"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#000000"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#000000"
                    }
                }
            }
        }
        return "#000000"
    }

    fun generateTitleBarExpandedTextColor() : String {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#000000"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#ffffff"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#000000"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#ffffff"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#ffffff"
                        }
                    }
                }
            }
        return "#ffffff"
    }

    fun generateBottomNavBackgroundColor() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_100
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_800
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_800
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_800
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        else {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                    }
                }
            }
        }
        return "#${
            this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
        }"
    }

    fun generateNavBarColor() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_100
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_800
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_800
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_800
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        else {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                    }
                }
            }
        }
        return "#${
            this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
        }"
    }
    fun generateCardColor() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_50
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_300
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_50
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_300
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_300
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        else {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.8)}"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#${
                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                }"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.8)}"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#${
                            this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#${
                            this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                        }"
                    }
                }
            }
        }
        }
        return "#${lighten(Color.parseColor(this.loadCustomHex()), 0.8)}"
    }

    fun generatePatchNotesCardColor() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_50
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_200
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_50
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_200
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_200
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        return "#${lighten(Color.parseColor(this.generateCardColor()), 0.4)}"
    }

    fun generateBottomNavIconTintColor() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_600
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_100
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_600
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        else {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        this.darken(Color.parseColor(this.loadCustomHex()), 0.2)
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        this.lighten(Color.parseColor(this.loadCustomHex()), 0.7)
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                this.darken(Color.parseColor(this.loadCustomHex()), 0.2)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.7)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.7)
                            }"
                        }
                    }
                }
            }
        }
        return "#${
            this.lighten(Color.parseColor(this.loadCustomHex()), 0.7)
        }"
    }

    fun generateBottomNavIconIndicatorColor(): String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_300
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_600
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_300
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_600
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_600
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        else {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        this.lighten(Color.parseColor(this.loadCustomHex()), 0.3)
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        this.lighten(Color.parseColor(this.loadCustomHex()), 0.1)
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.3)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.1)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.1)
                            }"
                        }
                    }
                }
            }
        }
        return "#${
            this.lighten(Color.parseColor(this.loadCustomHex()), 0.1)
        }"
    }

    fun generateBottomNavTextColor() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_600
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_100
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_600
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        else {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        this.darken(Color.parseColor(this.loadCustomHex()), 0.2)
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        this.lighten(Color.parseColor(this.loadCustomHex()), 0.7)
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                this.darken(Color.parseColor(this.loadCustomHex()), 0.2)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.7)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.7)
                            }"
                        }
                    }
                }
            }
        }
        return "#${
            this.lighten(Color.parseColor(this.loadCustomHex()), 0.7)
        }"
    }

    fun generateCollapsedToolBarTextColor() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_700
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_100
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_700
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        else {
        val darkThemeData = DarkThemeData(insideContext)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                return "#${
                    this.darken(Color.parseColor(this.loadCustomHex()), 0.4)
                }"
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                return "#${
                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                }"
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.4)
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return "#${
                            this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                        }"
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return "#${
                            this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                        }"
                    }
                }
            }
        }
        }
        return "#${
            this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
        }"
    }

    fun generateMenuTintColor() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_700
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_100
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_700
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        else {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        this.darken(Color.parseColor(this.loadCustomHex()), 0.4)
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                this.darken(Color.parseColor(this.loadCustomHex()), 0.4)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                    }
                }
            }
        }
        return "#${
            this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
        }"
    }

    fun generateSnackbarActionTextColor() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_400
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_400
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_400
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_400
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_400
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        else {
            return "#${
                this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
            }"
        }
        return loadCustomHex()
    }

    fun generateCustomColorPrimary() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_200
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_800
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_200
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_800
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_800
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        return loadCustomHex()
    }

    fun generateTopAppBarColor() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_100
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_800
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_800
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_800
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        else {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                    }
                }
            }
        }
        return "#${
            this.darken(Color.parseColor(this.loadCustomHex()), 0.6)
        }"
    }

    fun generateChipBackgroundColor() : String {
        if (MaterialYouEnabled(insideContext).loadMaterialYou()) {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_100
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_800
                            ) and 0x00ffffff
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_800
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_800
                                    ) and 0x00ffffff
                                )
                            }"
                        }
                    }
                }
            }
        }
        else {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        this.darken(Color.parseColor(this.loadCustomHex()), 0.65)
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                this.lighten(Color.parseColor(this.loadCustomHex()), 0.6)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                this.darken(Color.parseColor(this.loadCustomHex()), 0.65)
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                this.darken(Color.parseColor(this.loadCustomHex()), 0.65)
                            }"
                        }
                    }
                }
            }
        }
        return "#${
            this.darken(Color.parseColor(this.loadCustomHex()), 0.8)
        }"
    }

    private fun lighten(color: Int, fraction: Double): String {

        val newRed: Float = color.red + ((255 - color.red) * fraction.toFloat())
        val newGreen: Float = color.green + ((255 - color.green) * fraction.toFloat())
        val newBlue: Float = color.blue + ((255 - color.blue) * fraction.toFloat())

        return String.format("#%02X%02X%02X", newRed.toInt(), newGreen.toInt(), newBlue.toInt())
            .drop(1)
    }

    private fun darken(color: Int, fraction: Double): String {

        val newRed: Float = color.red - ((0 + color.red) * fraction.toFloat())
        val newGreen: Float = color.green - ((0 + color.green) * fraction.toFloat())
        val newBlue: Float = color.blue - ((0 + color.blue) * fraction.toFloat())

        return String.format("#%02X%02X%02X", newRed.toInt(), newGreen.toInt(), newBlue.toInt())
            .drop(1)
    }
}