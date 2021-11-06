package com.cory.hourcalculator

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.database.DBHelper
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
        setContentView(R.layout.activity_main)

        replaceFragment(homeFragment)

        val bottom_nav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val badge = bottom_nav.getOrCreateBadge(R.id.ic_home1)
        badge.isVisible = true
        badge.number = dbHandler.getCount()

        bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_home1 -> replaceFragment(historyFragment)
                R.id.ic_home2 -> replaceFragment(settingsFragment)
            }
            true
        }

        bottom_nav.itemActiveIndicatorColor = ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryLightAppBar))
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    fun update() {


        val badge = findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.ic_home1)
        badge.isVisible = true
        badge.number = dbHandler.getCount()

        historyFragment.update()
    }
}