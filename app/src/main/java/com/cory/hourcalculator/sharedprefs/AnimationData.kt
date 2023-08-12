package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class AnimationData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setHistoryRecyclerViewLoadingAnimation(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("historyRecyclerViewLoadingAnimation", state)
        editor.apply()
    }

    fun loadHistoryRecyclerViewLoadingAnimation(): Boolean {
        val state = sharedPreferences.getBoolean("historyRecyclerViewLoadingAnimation", false)
        return (state)
    }

    fun setTimeCardRecyclerViewLoadingAnimation(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("timeCardRecyclerViewLoadingAnimation", state)
        editor.apply()
    }

    fun loadTimeCardRecyclerViewLoadingAnimation(): Boolean {
        val state = sharedPreferences.getBoolean("timeCardRecyclerViewLoadingAnimation", false)
        return (state)
    }

    fun setHistoryScrollingLoadingAnimation(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("historyScrollingAnimation", state)
        editor.apply()
    }

    fun loadHistoryScrollingAnimation(): Boolean {
        val state = sharedPreferences.getBoolean("historyScrollingAnimation", false)
        return (state)
    }

    fun setTimeCardsScrollingLoadingAnimation(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("timeCardsScrollingAnimation", state)
        editor.apply()
    }

    fun loadTimeCardsScrollingAnimation(): Boolean {
        val state = sharedPreferences.getBoolean("timeCardsScrollingAnimation", false)
        return (state)
    }

    fun setImageAnimation(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("imageOpening/ClosingAnimation", state)
        editor.apply()
    }

    fun loadImageAnimation(): Boolean {
        val state = sharedPreferences.getBoolean("imageOpening/ClosingAnimation", true)
        return (state)
    }

    fun setTabSwitchingAnimation(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("tabSwitchingAnimation", state)
        editor.apply()
    }

    fun loadTabSwitchingAnimation(): Boolean {
        val state = sharedPreferences.getBoolean("tabSwitchingAnimation", true)
        return (state)
    }
}