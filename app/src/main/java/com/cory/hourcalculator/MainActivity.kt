package com.cory.hourcalculator

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.fragments.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val historyFragment = HistoryFragment()
    private val settingsFragment = SettingsFragment()

    val dbHandler = DBHelper(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val darkThemeData = DarkThemeData(this)
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> setTheme(R.style.Theme_MyApplication)
                    Configuration.UI_MODE_NIGHT_YES -> setTheme(R.style.Theme_AMOLED)
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> setTheme(R.style.Theme_AMOLED)
                }
            }
        }
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

        MobileAds.initialize(this)
        val adView = AdView(this)

        GlobalScope.launch(Dispatchers.Main) {

            adView.adSize = AdSize.BANNER
            adView.adUnitId = "ca-app-pub-4546055219731501/5171269817"
            val mAdView = findViewById<AdView>(R.id.adView)
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        changeBadgeNumber()
        changeSettingsBadge()

        toggleHistory()

        bottomNav.setOnItemSelectedListener {
            Vibrate().vibration(this)
                when (it.itemId) {
                    R.id.ic_home -> replaceFragment(homeFragment)
                    R.id.history -> replaceFragment(historyFragment)
                    R.id.settings -> replaceFragment(settingsFragment)
                }

            true
        }

        when {
            accentColor.loadAccent() == 0 -> {
                bottomNav.itemActiveIndicatorColor = ContextCompat.getColorStateList(this, R.color.colorPrimaryLightAppBar)
            }
            accentColor.loadAccent() == 1 -> {
                bottomNav.itemActiveIndicatorColor = ContextCompat.getColorStateList(this, R.color.colorPrimaryPinkAppBar)
            }
            accentColor.loadAccent() == 2 -> {
                bottomNav.itemActiveIndicatorColor = ContextCompat.getColorStateList(this, R.color.colorPrimaryOrangeAppBar)
            }
            accentColor.loadAccent() == 3 -> {
                bottomNav.itemActiveIndicatorColor = ContextCompat.getColorStateList(this, R.color.colorPrimaryRedAppBar)
            }
            accentColor.loadAccent() == 4 -> {
                bottomNav.itemActiveIndicatorColor = ContextCompat.getColorStateList(this, R.color.colorPrimaryPixelAppBar)
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        val bottomNav = this.findViewById<BottomNavigationView>(R.id.bottom_nav)

            if (homeFragment.isVisible) {
                transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
            } else if (historyFragment.isVisible && fragment == homeFragment) {
                transaction.setCustomAnimations(
                    R.anim.enter_from_left,
                    R.anim.exit_to_right,
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
                )
            } else if (historyFragment.isVisible && fragment == settingsFragment) {
                transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
            } else if (bottomNav.menu.findItem(R.id.history).isChecked && fragment == settingsFragment) {
                transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
            } else if (bottomNav.menu.findItem(R.id.history).isChecked && fragment == homeFragment) {
                transaction.setCustomAnimations(
                    R.anim.enter_from_left,
                    R.anim.exit_to_right,
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
                )
            } else if (bottomNav.menu.findItem(R.id.history).isChecked && fragment == historyFragment) {
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            } else if (settingsFragment.isVisible) {
                transaction.setCustomAnimations(
                    R.anim.enter_from_left,
                    R.anim.exit_to_right,
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
                )
            } else {
                transaction.setCustomAnimations(
                    R.anim.enter_from_left,
                    R.anim.exit_to_right,
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
                )
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

            transaction.replace(R.id.fragment_container, fragment).addToBackStack(null)
            transaction.commit()
    }

    fun update() {

       changeBadgeNumber()
        historyFragment.update()
    }

    fun deleteAll() {

        changeBadgeNumber()
        historyFragment.deleteAll()
    }

    fun undoDeleteAll() {

        changeBadgeNumber()
        historyFragment.undoDeleteAll()
    }

    fun changeBadgeNumber() {
        val badge = findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.history)
        badge.isVisible = true
        badge.number = dbHandler.getCount()
        if (AccentColor(this).loadAccent() != 3) {
            badge.backgroundColor = ContextCompat.getColor(this, R.color.redBadgeColor)
        }
        else {
            badge.backgroundColor = ContextCompat.getColor(this, R.color.lightRedBadgeColor)
        }
    }

    fun changeSettingsBadge() {
        val badge = findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.settings)
        if (Version(this).loadVersion() != "9.0.0") {
            badge.isVisible = true

            if (AccentColor(this).loadAccent() != 3) {
                badge.backgroundColor = ContextCompat.getColor(this, R.color.redBadgeColor)
            } else {
                badge.backgroundColor = ContextCompat.getColor(this, R.color.lightRedBadgeColor)
            }
        }
        else {
            badge.isVisible = false
        }
    }

    fun toggleHistory() {
        val historyToggleData = HistoryToggleData(this)

        findViewById<BottomNavigationView>(R.id.bottom_nav).menu.findItem(R.id.history).isVisible = historyToggleData.loadHistoryState()
    }

    fun setActiveTab() {

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.menu.findItem(R.id.ic_home).isChecked = true
    }

    @SuppressLint("CutPasteId")
    fun setBackgroundColor() {
        val mainConstraint = findViewById<ConstraintLayout>(R.id.mainConstraint)
        val badge = findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.history)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val darkThemeData = DarkThemeData(this)
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                mainConstraint.setBackgroundColor(ContextCompat.getColor(this, R.color.darkThemeBackground))
                badge.badgeTextColor = ContextCompat.getColor(this, R.color.black)
                bottomNavigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.black)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                mainConstraint.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                badge.badgeTextColor = ContextCompat.getColor(this, R.color.white)
                bottomNavigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.white)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                mainConstraint.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
                badge.badgeTextColor = ContextCompat.getColor(this, R.color.black)
                bottomNavigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.black)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        mainConstraint.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        badge.badgeTextColor = ContextCompat.getColor(this, R.color.white)
                        bottomNavigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.white)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        mainConstraint.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
                        badge.badgeTextColor = ContextCompat.getColor(this, R.color.black)
                        bottomNavigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.black)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        mainConstraint.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
                        badge.badgeTextColor = ContextCompat.getColor(this, R.color.black)
                        bottomNavigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.black)
                    }
                }
            }
        }
    }
}