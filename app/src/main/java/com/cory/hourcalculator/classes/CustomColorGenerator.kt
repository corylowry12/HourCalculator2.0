package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.cory.hourcalculator.sharedprefs.*
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

    fun setRandomSavedGeneratedHex(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("randomSavedHex", state)
        editor.apply()
    }

    fun loadRandomHex(): String {
        if (GenerateARandomColorMethodData(insideContext).loadGenerateARandomColorMethod() == 1) {
            val savedState = sharedPreferences.getString("randomSavedHex", "")
            return (savedState!!)
        }
        val state = sharedPreferences.getString("randomHex", "#53c8c8")
        return (state!!)
    }

    fun generateARandomColor() {
        if (GenerateARandomColorMethodData(insideContext).loadGenerateARandomColorMethod() == 0) {
            val red = Random.nextInt(50, 200)
            val green = Random.nextInt(50, 200)
            val blue = Random.nextInt(50, 200)
            val hex = String.format("#%02X%02X%02X", red, green, blue)
            setRandomGeneratedHex(hex)
        }
        else if (GenerateARandomColorMethodData(insideContext).loadGenerateARandomColorMethod() == 1) {
            val dataList = UserAddedColorsData(insideContext).read()
            val random = Random.nextInt(0, dataList.count())
            setRandomSavedGeneratedHex("#${dataList[random]["hex"]}")
        }
    }

    fun loadCustomHex(): String {
        if (GenerateARandomColorData(insideContext).loadGenerateARandomColorOnAppLaunch()) {
            return loadRandomHex()
        }
        val state = sharedPreferences.getString("customColor", "#53c8c8")
        return (state!!)
    }

    fun generateBackgroundColor() : String {
        //DARK GRAY COLOR #383B3C
        val darkTheme = DarkThemeData(insideContext)
        if (MoreColorfulBackgroundData(insideContext).loadMoreColorfulBackground()) {
            if (MaterialYouData(insideContext).loadMaterialYou()) {
                when {
                    darkTheme.loadDarkModeState() == 0 -> {
                        return "#${
                            this.lighten(
                                ContextCompat.getColor(
                                    insideContext, android.R.color.system_accent3_50
                                ), 0.8)
                        }"
                    }
                    darkTheme.loadDarkModeState() == 2 -> {
                        return "#${
                            this.darken(
                                ContextCompat.getColor(
                                    insideContext, android.R.color.system_accent3_900
                                ), 0.4)
                        }"
                    }
                    darkTheme.loadDarkModeState() == 3 -> {
                        when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                            Configuration.UI_MODE_NIGHT_NO -> {
                                return "#${
                                    this.lighten(
                                        ContextCompat.getColor(
                                            insideContext, android.R.color.system_accent3_50
                                        ), 0.8)
                                }"
                            }
                            Configuration.UI_MODE_NIGHT_YES -> {
                                return "#${
                                    this.darken(
                                        ContextCompat.getColor(
                                            insideContext, android.R.color.system_accent3_900
                                        ), 0.4)
                                }"
                            }
                            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                                return "#${
                                    this.darken(
                                        ContextCompat.getColor(
                                            insideContext, android.R.color.system_accent3_900
                                        ), 0.4)
                                }"
                            }
                        }
                    }
                }
            }
            else {
                when {
                    darkTheme.loadDarkModeState() == 0 -> {
                        return "#${
                            this.lighten(Color.parseColor(this.loadCustomHex()), 0.9)
                        }"
                    }
                    darkTheme.loadDarkModeState() == 2 -> {
                        return "#${
                            this.darken(Color.parseColor(this.loadCustomHex()), 0.85)
                        }"
                    }
                    darkTheme.loadDarkModeState() == 3 -> {
                        when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                            Configuration.UI_MODE_NIGHT_NO -> {
                                return "#${
                                    this.lighten(Color.parseColor(this.loadCustomHex()), 0.9)
                                }"
                            }
                            Configuration.UI_MODE_NIGHT_YES -> {
                                return "#${
                                    this.darken(Color.parseColor(this.loadCustomHex()), 0.85)
                                }"
                            }
                            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                                return "#${
                                    this.darken(Color.parseColor(this.loadCustomHex()), 0.85)
                                }"
                            }
                        }
                    }
                }
            }
        }
        else {
            when {
                darkTheme.loadDarkModeState() == 0 -> {
                    return "#ffffff"
                }
                darkTheme.loadDarkModeState() == 2 -> {
                    return "#000000"
                }
                darkTheme.loadDarkModeState() == 3 -> {
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
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_100
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_800
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_100
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_800
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_800
                                    ) 
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
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_100
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_800
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_100
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_800
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_800
                                    ) 
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
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_50
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_300
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_50
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_300
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_300
                                    ) 
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
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_50
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_200
                            ) 
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
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_200
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_200
                                    ) 
                                )
                            }"
                        }
                    }
                }
            }
        }
        return "#${lighten(Color.parseColor(this.generateCardColor()), 0.1)}"
    }

    fun generateBottomNavIconTintColor() : String {
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_600
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_100
                            ) 
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
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_100
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    ) 
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
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_200
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_600
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_200
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_600
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_600
                                    ) 
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
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_600
                            )
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_100
                            )
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
                                    )
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_100
                                    )
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    )
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
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_600
                            )
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_100
                            )
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
                                    )
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_100
                                    )
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    )
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

    fun generateMenuTintColor() : String {
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_600
                            )
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_neutral2_100
                            )
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
                                    )
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_100
                                    )
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_100
                                    )
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

    fun generateSnackbarActionTextColor() : String {
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_400
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent2_400
                            ) 
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
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_400
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent2_400
                                    ) 
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
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_200
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_800
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_200
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_800
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_800
                                    ) 
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
        if (MaterialYouData(insideContext).loadMaterialYou()) {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_100
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_800
                            ) 
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_100
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_800
                                    ) 
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_800
                                    ) 
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

    fun generateBottomNavBadgeBackgroundColor() : String {
        if (BottomNavBadgeColorData(insideContext).loadColor()) {
            if (MaterialYouData(insideContext).loadMaterialYou()) {

                val darkThemeData = DarkThemeData(insideContext)
                when {
                    darkThemeData.loadDarkModeState() == 0 -> {
                        return "#${
                            Integer.toHexString(
                                ContextCompat.getColor(
                                    insideContext, android.R.color.system_accent3_200
                                ) 
                            )
                        }"
                    }
                    darkThemeData.loadDarkModeState() == 2 -> {
                        return "#${
                            Integer.toHexString(
                                ContextCompat.getColor(
                                    insideContext, android.R.color.system_accent2_500
                                ) 
                            )
                        }"
                    }
                    darkThemeData.loadDarkModeState() == 3 -> {
                        when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                            Configuration.UI_MODE_NIGHT_NO -> {
                                return "#${
                                    Integer.toHexString(
                                        ContextCompat.getColor(
                                            insideContext, android.R.color.system_accent3_200
                                        ) 
                                    )
                                }"
                            }
                            Configuration.UI_MODE_NIGHT_YES -> {
                                return "#${
                                    Integer.toHexString(
                                        ContextCompat.getColor(
                                            insideContext, android.R.color.system_accent2_500
                                        ) 
                                    )
                                }"
                            }
                            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                                return "#${
                                    Integer.toHexString(
                                        ContextCompat.getColor(
                                            insideContext, android.R.color.system_accent2_500
                                        ) 
                                    )
                                }"
                            }
                        }
                    }
                }
            }
            else {
                return generateCustomColorPrimary()
            }
        }
        return "#D36C6C"
    }

    fun generateBottomNavBadgeTextColor() : String {
        if (BottomNavBadgeColorData(insideContext).loadColor()) {
            return generateCardColor()
        }
        else {
            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 1 -> {

                }
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#000000"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#FFFFFF"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#000000"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#FFFFFF"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#FFFFFF"
                        }
                    }
                }
            }
        }
        return "#000000"
    }

    fun generateChipBackgroundColor() : String {
        if (MaterialYouData(insideContext).loadMaterialYou()) {

            val darkThemeData = DarkThemeData(insideContext)
            when {
                darkThemeData.loadDarkModeState() == 0 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_100
                            )
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    return "#${
                        Integer.toHexString(
                            ContextCompat.getColor(
                                insideContext, android.R.color.system_accent3_800
                            )
                        )
                    }"
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (insideContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_100
                                    )
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_accent3_800
                                    )
                                )
                            }"
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            return "#${
                                Integer.toHexString(
                                    ContextCompat.getColor(
                                        insideContext, android.R.color.system_neutral2_800
                                    )
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