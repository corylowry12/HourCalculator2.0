package com.cory.hourcalculator.intents

import android.R.attr.enabled
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.fragments.*
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigationrail.NavigationRailView
import com.google.android.material.snackbar.Snackbar
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: NavigationBarView

    private val homeFragment = HomeFragment()
    private val historyFragment = HistoryFragment()
    private val settingsFragment = SettingsFragment()
    private val editHours = EditHours()
    private val timeCards = TimeCardsFragment()

    var currentTab = 0
    var currentSettingsItem = -1

    private val dbHandler = DBHelper(this, null)

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateCustomColor()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val darkThemeData = DarkThemeData(this)
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                //setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                setTheme(R.style.Theme_MyApplication)
            }

            darkThemeData.loadDarkModeState() == 2 -> {
                setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        setTheme(R.style.Theme_MyApplication)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        setTheme(R.style.Theme_AMOLED)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        setContentView(R.layout.activity_main)

        if (!resources.getBoolean(R.bool.isTablet)) {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        updateCustomColor()

        setNavigationRailGravity()

            if (isComponentEnabled("com.cory.hourcalculator.SplashScreenNoIcon") == 1) {
                ChosenAppIconData(this).setChosenAppIcon("teal")
            }
            else if (isComponentEnabled("com.cory.hourcalculator.SplashPink") == 1) {
                ChosenAppIconData(this).setChosenAppIcon("pink")
            }
            else if (isComponentEnabled("com.cory.hourcalculator.SplashOrange") == 1) {
                ChosenAppIconData(this).setChosenAppIcon("orange")
            }
            else if (isComponentEnabled("com.cory.hourcalculator.SplashRed") == 1) {
                ChosenAppIconData(this).setChosenAppIcon("red")
            }
            else if (isComponentEnabled("com.cory.hourcalculator.MaterialYou") == 1) {
                ChosenAppIconData(this).setChosenAppIcon("material you")
            }

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }
        }

        val runnable = Runnable {
                MobileAds.initialize(this)
                val adView = AdView(this)
                adView.adUnitId = GoogleAdsKey().API_KEY
                val mAdView = findViewById<AdView>(R.id.adView)
                val adRequest = AdRequest.Builder().build()
                mAdView.loadAd(adRequest)
        }
        runOnUiThread(runnable)

        bottomNav = if (resources.getBoolean(R.bool.isTablet)) {
            findViewById<BottomNavigationView>(R.id.bottom_nav)
        } else {
            findViewById<NavigationRailView>(R.id.bottom_nav)
        }

        changeBadgeNumber()
        changeSettingsBadge()
        openPatchNotesOnAppLaunch()

        toggleHistory()

        bottomNav.setOnItemSelectedListener {
            Vibrate().vibration(this)
            when (it.itemId) {
                R.id.ic_home -> {
                    try {
                        val currentFragment = supportFragmentManager.fragments.last()
                        if (currentFragment.toString().startsWith("EditHours", true)) {
                            bottomNav.menu.findItem(R.id.ic_home).isChecked = false
                            bottomNav.menu.findItem(R.id.history).isChecked = true
                            Toast.makeText(
                                this,
                                getString(R.string.you_must_save_and_exit_editing_to_leave_the_view),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnItemSelectedListener false
                        } else {
                            replaceFragment(homeFragment, 0)
                            currentTab = 0
                            return@setOnItemSelectedListener true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        replaceFragment(homeFragment, 0)
                        currentTab = 0
                    }
                }
                R.id.history -> {
                    try {
                        val currentFragment = supportFragmentManager.fragments.last()

                        if (currentFragment.toString().startsWith("EditHours", true)) {
                            editHours.historyTabClicked(this)
                            return@setOnItemSelectedListener false
                        } else if (currentFragment.toString().startsWith("History", true) && !currentFragment.toString().contains("Settings", true) && !currentFragment.toString().contains("fab", true)) {
                            historyFragment.scrollToTop()
                            return@setOnItemSelectedListener true
                        }
                        else {
                            replaceFragment(historyFragment, 1)
                            currentTab = 1
                            return@setOnItemSelectedListener true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                R.id.timeCards -> {
                    val currentFragment = supportFragmentManager.fragments.last()

                    if (currentFragment.toString().startsWith("EditHours", true)) {
                        bottomNav.menu.findItem(R.id.timeCards).isChecked = false
                        bottomNav.menu.findItem(R.id.history).isChecked = true
                        Toast.makeText(this,     getString(R.string.you_must_save_and_exit_editing_to_leave_the_view), Toast.LENGTH_SHORT).show()
                        return@setOnItemSelectedListener false
                    }
                    else if (currentFragment.toString().startsWith("Time", true) && (!currentFragment.toString().contains("Info") && !currentFragment.toString().contains("Settings")) && !currentFragment.toString().contains("fab", true)) {
                        timeCards.scrollToTop()
                        return@setOnItemSelectedListener true
                    }
                    else if (currentFragment.toString().startsWith("Time", true) && (currentFragment.toString().contains("Info") && !currentFragment.toString().contains("Settings")) || currentFragment.toString().startsWith("supportrequestmanager", true)) {
                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)

                        transaction.replace(R.id.fragment_container, timeCards).addToBackStack(null)

                        transaction.commit()
                        currentTab = 2
                        return@setOnItemSelectedListener true
                    }
                    else {
                        replaceFragment(timeCards, 2)
                        currentTab = 2
                        return@setOnItemSelectedListener true
                    }
                }
                R.id.settings -> {
                    /*val currentFragment = supportFragmentManager.fragments.last()
                    if (currentSettingsItem == 0 && !currentFragment.toString().startsWith("Appearance", true)) {
                        replaceFragment(AppearanceFragment(), 3)
                    }
                    else if (currentSettingsItem == 1 && !currentFragment.toString().startsWith("AppSetting", true)) {
                        replaceFragment(AppSettingsFragment(), 3)
                    }
                    else if (currentSettingsItem == 2 && !currentFragment.toString().startsWith("Automatic", true)) {
                        replaceFragment(AutomaticDeletionFragment(), 3)
                    }
                    else if (currentSettingsItem == 3 && !currentFragment.toString().startsWith("Patch", true)) {
                        replaceFragment(PatchNotesFragment(), 3)
                    }
                    else if (currentSettingsItem == 4 && !currentFragment.toString().startsWith("Update", true)) {
                        replaceFragment(UpdateFragment(), 3)
                    }
                    else if (currentSettingsItem == 5 && !currentFragment.toString().startsWith("FAQ", true)) {
                        replaceFragment(FAQFragment(), 3)
                    }
                    else if (currentSettingsItem == 6 && !currentFragment.toString().startsWith("About", true)) {
                        replaceFragment(AboutAppFragment(), 3)
                    }
                    else {*/
                        //replaceFragment(settingsFragment, 3)
                    //}
                    val currentFragment = supportFragmentManager.fragments.last()
                    if (currentFragment.toString().startsWith("EditHours", true)) {
                        bottomNav.menu.findItem(R.id.ic_home).isChecked = false
                        bottomNav.menu.findItem(R.id.history).isChecked = true
                        Toast.makeText(this,     getString(R.string.you_must_save_and_exit_editing_to_leave_the_view), Toast.LENGTH_SHORT).show()
                        return@setOnItemSelectedListener false
                    }
                    else {
                        replaceFragment(settingsFragment, 3)
                        currentTab = 3
                        return@setOnItemSelectedListener true
                    }
                }
            }

            true
        }

        bottomNav.itemActiveIndicatorColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavIconIndicatorColor()))

        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, homeFragment)
        transaction.commitNow()
    }

    fun updateBottomNavCustomColor() {
        if (!resources.getBoolean(R.bool.isTablet)) {
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav.itemIconTintList =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavIconTintColor()))
            bottomNav.setBackgroundColor(Color.parseColor(CustomColorGenerator(this).generateBottomNavBackgroundColor()))
            bottomNav.itemTextColor =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavTextColor()))
            bottomNav.itemRippleColor =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavIconTintColor()))
            bottomNav.itemActiveIndicatorColor =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavIconIndicatorColor()))

            val historyBadge =
                findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.history)
            val timeCardBadge =
                findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.timeCards)
            val settingsBadge = findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.settings)

            historyBadge.backgroundColor = Color.parseColor(CustomColorGenerator(this).generateBottomNavBadgeBackgroundColor())
            historyBadge.badgeTextColor = Color.parseColor("#ffffff")
            timeCardBadge.backgroundColor = Color.parseColor(CustomColorGenerator(this).generateBottomNavBadgeBackgroundColor())
            timeCardBadge.badgeTextColor = Color.parseColor("#ffffff")
            settingsBadge.backgroundColor = Color.parseColor(CustomColorGenerator(this).generateBottomNavBadgeBackgroundColor())
        }
        else {
            val bottomNav = findViewById<NavigationRailView>(R.id.bottom_nav)
            bottomNav.itemIconTintList =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavIconTintColor()))
            bottomNav.setBackgroundColor(Color.parseColor(CustomColorGenerator(this).generateBottomNavBackgroundColor()))
            bottomNav.itemTextColor =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavTextColor()))
            bottomNav.itemRippleColor =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavIconTintColor()))
            bottomNav.itemActiveIndicatorColor =
                ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavIconIndicatorColor()))

            val historyBadge =
                findViewById<NavigationRailView>(R.id.bottom_nav).getOrCreateBadge(R.id.history)
            val timeCardBadge =
                findViewById<NavigationRailView>(R.id.bottom_nav).getOrCreateBadge(R.id.timeCards)
            val settingsBadge = findViewById<NavigationRailView>(R.id.bottom_nav).getOrCreateBadge(R.id.settings)

            historyBadge.backgroundColor = Color.parseColor(CustomColorGenerator(this).generateBottomNavBadgeBackgroundColor())
            historyBadge.badgeTextColor = Color.parseColor("#ffffff")
            timeCardBadge.backgroundColor = Color.parseColor(CustomColorGenerator(this).generateBottomNavBadgeBackgroundColor())
            timeCardBadge.badgeTextColor = Color.parseColor("#ffffff")
            settingsBadge.backgroundColor = Color.parseColor(CustomColorGenerator(this).generateBottomNavBadgeBackgroundColor())
        }
    }

    private fun replaceFragment(fragment: Fragment, goingToTab: Int) {

        val transaction = supportFragmentManager.beginTransaction()

        /*if (homeFragment.isVisible) {
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
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)*/

        if (currentTab < goingToTab) {
            transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        }
        else {
            transaction.setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            )
        }

        transaction.replace(R.id.fragment_container, fragment).addToBackStack(null)

        transaction.commit()
    }

    fun update() {
        changeBadgeNumber()
    }

    fun showSnackbar() {
        val snackbar =
            Snackbar.make(findViewById(R.id.bottom_nav), getString(R.string.entry_deleted), Snackbar.LENGTH_LONG)
                .setDuration(5000)
                .setAnchorView(findViewById(R.id.adView))

        snackbar.setAction(getString(R.string.undo)) {
            Vibrate().vibration(this)

            undo()

            restoreState()
        }
        snackbar.setActionTextColor(
            Color.parseColor(CustomColorGenerator(this).generateSnackbarActionTextColor())
        )
        snackbar.apply {
            snackbar.view.background = ResourcesCompat.getDrawable(context.resources, R.drawable.snackbar_corners, context.theme)
        }
        snackbar.show()
    }

    private fun undo() {
        historyFragment.undo()
        changeBadgeNumber()
    }

    fun saveState() {
        changeBadgeNumber()
        historyFragment.saveState()
    }

    fun saveTimeCardState() {
        changeBadgeNumber()
        timeCards.saveState()
    }

    fun restoreState() {
        changeBadgeNumber()
        historyFragment.restoreState()
    }

    fun deleteAll() {
        changeBadgeNumber()
        historyFragment.deleteAll()
    }

    fun deleteAllTimeCards() {
        update()
        timeCards.deleteAll()
    }

    fun checkBoxVisible(visible: Boolean) {
        historyFragment.checkBoxVisible(visible)
    }

    fun hideNavigationIcon() {
        historyFragment.hideNavigationIcon()
    }

    fun undoDeleteAll() {

        changeBadgeNumber()
        historyFragment.undoDeleteAll()
    }

    fun changeBadgeNumber() {
        if (!resources.getBoolean(R.bool.isTablet)) {
            val historyBadge =
                findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.history)
            val timeCardBadge =
                findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.timeCards)
            historyBadge.isVisible = true
            timeCardBadge.isVisible = true
            historyBadge.number = dbHandler.getCount()
            timeCardBadge.number = TimeCardDBHelper(this, null).getCount()
        }
        else {
            val historyBadge =
                findViewById<NavigationRailView>(R.id.bottom_nav).getOrCreateBadge(R.id.history)
            val timeCardBadge =
                findViewById<NavigationRailView>(R.id.bottom_nav).getOrCreateBadge(R.id.timeCards)
            historyBadge.isVisible = true
            timeCardBadge.isVisible = true
            historyBadge.number = dbHandler.getCount()
            timeCardBadge.number = TimeCardDBHelper(this, null).getCount()
        }
    }

    fun changeSettingsBadge() {
        if (!resources.getBoolean(R.bool.isTablet)) {
            val badge =
                findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.settings)
            badge.isVisible = VersionData(this).loadVersion() != BuildConfig.VERSION_NAME
        }
        else {
            val badge =
                findViewById<NavigationRailView>(R.id.bottom_nav).getOrCreateBadge(R.id.settings)
            badge.isVisible = VersionData(this).loadVersion() != BuildConfig.VERSION_NAME
        }
    }

    private fun openPatchNotesOnAppLaunch() {
        if (VersionData(this).loadVersion() != BuildConfig.VERSION_NAME) {
            if (ShowPatchNotesOnAppLaunchData(this).loadShowPatchNotesOnAppLaunch()) {
                replaceFragment(PatchNotesFragment(), 3)
            }
        }
    }

    fun toggleHistory() {
        val historyToggleData = HistoryToggleData(this)

        if (!resources.getBoolean(R.bool.isTablet)) {
            findViewById<BottomNavigationView>(R.id.bottom_nav).menu.findItem(R.id.history).isVisible =
                historyToggleData.loadHistoryState()
        }
        else {
            findViewById<NavigationRailView>(R.id.bottom_nav).menu.findItem(R.id.history).isVisible =
                historyToggleData.loadHistoryState()
        }
    }

    fun setActiveTab(view: Int) {
        try {
            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomNav = findViewById<NavigationRailView>(R.id.bottom_nav)
                when (view) {
                    0 -> {
                        bottomNav.menu.findItem(R.id.ic_home).isChecked = true
                    }

                    1 -> {
                        bottomNav.menu.findItem(R.id.history).isChecked = true
                    }

                    2 -> {
                        bottomNav.menu.findItem(R.id.timeCards).isChecked = true
                    }

                    3 -> {
                        bottomNav.menu.findItem(R.id.settings).isChecked = true
                    }
                }
            } else {
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
                when (view) {
                    0 -> {
                        bottomNav.menu.findItem(R.id.ic_home).isChecked = true
                    }

                    1 -> {
                        bottomNav.menu.findItem(R.id.history).isChecked = true
                    }

                    2 -> {
                        bottomNav.menu.findItem(R.id.timeCards).isChecked = true
                    }

                    3 -> {
                        bottomNav.menu.findItem(R.id.settings).isChecked = true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("CutPasteId")
    fun updateCustomColor() {
        val mainConstraint = findViewById<ConstraintLayout>(R.id.mainConstraint)
        mainConstraint.setBackgroundColor(Color.parseColor(CustomColorGenerator(this).generateBackgroundColor()))

        if (ColoredNavBarData(this).loadNavBar()) {
            window?.navigationBarColor =
                Color.parseColor(CustomColorGenerator(this).generateNavBarColor())
        }
        else {
            window?.navigationBarColor =
                Color.parseColor("#000000")
        }

        updateBottomNavCustomColor()

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        mainConstraint.setBackgroundColor(Color.parseColor(CustomColorGenerator(this).generateBackgroundColor()))

        val darkThemeData = DarkThemeData(this)
        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                if (window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
                    var flags: Int = this.window.decorView
                        .systemUiVisibility // get current flag

                    flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    this.window.decorView.systemUiVisibility = flags
                }
            }
            darkThemeData.loadDarkModeState() == 3 -> {

                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        if (window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
                            var flags: Int = this.window.decorView
                                .systemUiVisibility // get current flag

                            flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            this.window.decorView.systemUiVisibility = flags
                        }
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        if (window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
                            var flags: Int = this.window.decorView
                                .systemUiVisibility // get current flag

                            flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            this.window.decorView.systemUiVisibility = flags
                        }
                    }
                }
            }
        }
    }

    fun setNavigationRailGravity() {

        if (resources.getBoolean(R.bool.isTablet)) {
            if (NavigationRailMenuGravityData(this).loadNavigationRailPosition() == 0) {
                val navigationRail = findViewById<NavigationRailView>(R.id.bottom_nav)
                navigationRail.menuGravity = Gravity.TOP
            }
            if (NavigationRailMenuGravityData(this).loadNavigationRailPosition() == 1) {
                val navigationRail = findViewById<NavigationRailView>(R.id.bottom_nav)
                navigationRail.menuGravity = Gravity.CENTER
            }
            if (NavigationRailMenuGravityData(this).loadNavigationRailPosition() == 2) {
                val navigationRail = findViewById<NavigationRailView>(R.id.bottom_nav)
                navigationRail.menuGravity = Gravity.BOTTOM
            }
        }
    }

    fun openTimeCardInfoView(id: String, name: String) {
        val timeCardInfoFragment = TimeCardItemInfoFragment()
        timeCardInfoFragment.arguments = Bundle().apply {
            putString("id", id)
            putString("name", name)
        }
        val manager = supportFragmentManager.beginTransaction()
        manager.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        manager.replace(R.id.fragment_container, timeCardInfoFragment)
            .addToBackStack(null)
        manager.commit()
    }

    private fun isComponentEnabled(componentName: String) : Int {
        return this.packageManager.getComponentEnabledSetting(ComponentName(
            BuildConfig.APPLICATION_ID,
            componentName
        ))
    }
}