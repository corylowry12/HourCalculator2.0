package com.cory.hourcalculator

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.HistoryToggleData
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.fragments.AppearanceFragment
import com.cory.hourcalculator.fragments.HistoryFragment
import com.cory.hourcalculator.fragments.HomeFragment
import com.cory.hourcalculator.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val historyFragment = HistoryFragment()
    private val settingsFragment = SettingsFragment()

    val dbHandler = DBHelper(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val accentColor = AccentColor(this)
        when {
            accentColor.loadAccent() == 0 -> {
                theme.applyStyle(R.style.teal_accent, true)
            }
            accentColor.loadAccent() == 1 -> {
                theme.applyStyle(R.style.pink_accent, true)
            }
            accentColor.loadAccent() == 2 -> {
                theme.applyStyle(R.style.orange_accent, true)
            }
            accentColor.loadAccent() == 3 -> {
                theme.applyStyle(R.style.red_accent, true)
            }
            accentColor.loadAccent() == 4 -> {
                theme.applyStyle(R.style.system_accent, true)
            }
        }
        setContentView(R.layout.activity_main)

        replaceFragment(homeFragment)

        val bottom_nav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        changeBadgeNumber()

        toggleHistory()

        bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_home1 -> replaceFragment(historyFragment)
                R.id.ic_home2 -> replaceFragment(settingsFragment)
            }
            true
        }

        when {
            accentColor.loadAccent() == 0 -> {
                bottom_nav.itemActiveIndicatorColor = ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryLightAppBar))
            }
            accentColor.loadAccent() == 1 -> {
                bottom_nav.itemActiveIndicatorColor = ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryPinkAppBar))
            }
            accentColor.loadAccent() == 2 -> {
                bottom_nav.itemActiveIndicatorColor = ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryOrangeAppBar))
            }
            accentColor.loadAccent() == 3 -> {
                bottom_nav.itemActiveIndicatorColor = ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryRedAppBar))
            }
            accentColor.loadAccent() == 4 -> {
                bottom_nav.itemActiveIndicatorColor = ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryPixelAppBar))
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {

            val transaction = supportFragmentManager.beginTransaction()

            if (homeFragment.isVisible) {
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            }
            else if (historyFragment.isVisible && fragment == homeFragment) {
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
            }
            else if (historyFragment.isVisible && fragment == settingsFragment) {
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            }
            else if (settingsFragment.isVisible) {
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
            }
            else {
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    fun update() {

       changeBadgeNumber()

        historyFragment.update()
    }

    fun changeBadgeNumber() {
        val badge = findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.ic_home1)
        badge.isVisible = true
        badge.number = dbHandler.getCount()
    }

    fun toggleHistory() {
        val historyToggleData = HistoryToggleData(this)

        findViewById<BottomNavigationView>(R.id.bottom_nav).menu.findItem(R.id.ic_home1).isVisible = historyToggleData.loadHistoryState()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (AppearanceFragment().isVisible) {
            Toast.makeText(this, "Visible", Toast.LENGTH_LONG).show()
        }
    }
}