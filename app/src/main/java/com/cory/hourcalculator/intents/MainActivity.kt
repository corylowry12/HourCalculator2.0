package com.cory.hourcalculator.intents

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ComponentInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.red
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.fragments.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.TypeVariable
import java.util.*
import kotlin.collections.ArrayList


@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val historyFragment = HistoryFragment()
    private val settingsFragment = SettingsFragment()
    private val editHours = EditHours()
    private val timeCards = TimeCardsFragment()

    var currentTab = 0
    var currentSettingsItem = -1

    private val dbHandler = DBHelper(this, null)

    var themeSelection = false

    fun setStatusBarColor() {
        val accentColor = AccentColor(this)
        val followSystemVersion = FollowSystemVersion(this)
        when {
            accentColor.loadAccent() == 0 || accentColor.loadAccent() == 5 -> {
                window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
            }
            accentColor.loadAccent() == 1 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.pinkAccent)
            }
            accentColor.loadAccent() == 2 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.orangeAccent)
            }
            accentColor.loadAccent() == 3 -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.redAccent)
            }
            accentColor.loadAccent() == 4 -> {
                if (!followSystemVersion.loadSystemColor()) {
                    window.statusBarColor = ContextCompat.getColor(this, R.color.systemAccent)
                } else {
                    if (themeSelection) {
                        window.statusBarColor = ContextCompat.getColor(this,
                            R.color.systemAccentGoogleDark
                        )
                    } else {
                        window.statusBarColor = ContextCompat.getColor(this,
                            R.color.systemAccentGoogleDark_light
                        )
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }
        }

    }

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val darkThemeData = DarkThemeData(this)
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                setTheme(R.style.Theme_DarkTheme)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                setTheme(R.style.Theme_MyApplication)
                themeSelection = false
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                setTheme(R.style.Theme_AMOLED)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        setTheme(R.style.Theme_MyApplication)
                        themeSelection = false
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        setTheme(AccentColor(this).followSystemTheme(this))
                        themeSelection = true
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        setTheme(R.style.Theme_AMOLED)
                        themeSelection = true
                    }
                }
            }
        }
        val accentColor = AccentColor(this)
        val followSystemVersion = FollowSystemVersion(this)

        when {
            accentColor.loadAccent() == 0 -> {
                theme!!.applyStyle(R.style.teal_accent, true)
            }
            accentColor.loadAccent() == 1 -> {
                theme?.applyStyle(R.style.pink_accent, true)
            }
            accentColor.loadAccent() == 2 -> {
                theme?.applyStyle(R.style.orange_accent, true)
            }
            accentColor.loadAccent() == 3 -> {
                theme?.applyStyle(R.style.red_accent, true)
            }
            accentColor.loadAccent() == 4 -> {
                if (!followSystemVersion.loadSystemColor()) {
                    theme!!.applyStyle(R.style.system_accent, true)
                }
                else {
                    if (themeSelection) {
                        theme!!.applyStyle(R.style.system_accent_google, true)
                    }
                    else {
                        theme?.applyStyle(R.style.system_accent_google_light, true)
                    }
                }
            }
            accentColor.loadAccent() == 5 -> {
                theme!!.applyStyle(R.style.transparent_accent, true)
            }
        }
        setContentView(R.layout.activity_main)

        replaceFragment(homeFragment, 0)

        //setActiveTab()

        //setStatusBarColor()

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("file", Context.MODE_PRIVATE)
        val state = sharedPreferences.getInt("chosenAppIcon", 0)

        if (isComponentEnabled("com.cory.hourcalculator.SplashScreenNoIcon") == 1 && state == 1) {
            ChosenAppIconData(this).setChosenAppIcon("teal")
        }
        else if (isComponentEnabled("com.cory.hourcalculator.SplashPink") == 1 && state == 2) {
            ChosenAppIconData(this).setChosenAppIcon("pink")
        }
        else if (isComponentEnabled("com.cory.hourcalculator.SplashOrange") == 1 && state == 3) {
            ChosenAppIconData(this).setChosenAppIcon("orange")
        }
        else if (isComponentEnabled("com.cory.hourcalculator.SplashRed") == 1 && state == 4) {
            ChosenAppIconData(this).setChosenAppIcon("red")
            Toast.makeText(this, "red", Toast.LENGTH_SHORT).show()
        }
        sharedPreferences.edit().remove("chosenAppIcon").commit()

        if (ChosenAppIconData(this).loadChosenAppIcon() == "auto") {
            if (isComponentEnabled("com.cory.hourcalculator.SplashScreenNoIcon") == 1) {
                ChosenAppIconData(this).setChosenAppIcon("teal")
                Toast.makeText(this, "teal", Toast.LENGTH_SHORT).show()
            }
            else if (isComponentEnabled("com.cory.hourcalculator.SplashPink") == 1) {
                ChosenAppIconData(this).setChosenAppIcon("pink")
                Toast.makeText(this, "pink", Toast.LENGTH_SHORT).show()
            }
            else if (isComponentEnabled("com.cory.hourcalculator.SplashOrange") == 1) {
                ChosenAppIconData(this).setChosenAppIcon("orange")
                Toast.makeText(this, "orange", Toast.LENGTH_SHORT).show()
            }
            else if (isComponentEnabled("com.cory.hourcalculator.SplashRed") == 1) {
                ChosenAppIconData(this).setChosenAppIcon("red")
                Toast.makeText(this, "red", Toast.LENGTH_SHORT).show()
            }
            else if (isComponentEnabled("com.cory.hourcalculator.MaterialYou") == 1) {
                ChosenAppIconData(this).setChosenAppIcon("material you")
                Toast.makeText(this, "material you", Toast.LENGTH_SHORT).show()
            }
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

        //val intent = Intent(this, OnboardingActivity::class.java)
        //startActivity(intent)
        
        val context = this
        GlobalScope.launch(Dispatchers.Main) {
            MobileAds.initialize(context)
            val adView = AdView(context)
            adView.adUnitId = "ca-app-pub-4546055219731501/5171269817"
            val mAdView = findViewById<AdView>(R.id.adView)
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        if (AccentColor(this).loadAccent() == 5) {
            updateBottomNavCustomColor()
        }

        changeBadgeNumber()
        changeSettingsBadge()

        toggleHistory()

        bottomNav.setOnItemSelectedListener {
            Vibrate().vibration(this)
            when (it.itemId) {
                R.id.ic_home -> {
                    val currentFragment = supportFragmentManager.fragments.last()
                    if (currentFragment.toString().startsWith("EditHours", true)) {
                        bottomNav.menu.findItem(R.id.ic_home).isChecked = false
                        bottomNav.menu.findItem(R.id.history).isChecked = true
                        Toast.makeText(this, "You must save and exit editing to leave the view", Toast.LENGTH_SHORT).show()
                        return@setOnItemSelectedListener false
                    }
                    else {
                        replaceFragment(homeFragment, 0)
                        currentTab = 0
                        return@setOnItemSelectedListener true
                    }
                }
                R.id.history -> {
                    try {
                        val currentFragment = supportFragmentManager.fragments.last()

                        if (currentFragment.toString().startsWith("EditHours", true)) {
                            editHours.historyTabClicked(this)
                            return@setOnItemSelectedListener false
                        } else if (currentFragment.toString().startsWith("History", true) && !currentFragment.toString().contains("Settings", true)) {
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
                        Toast.makeText(this, "You must save and exit editing to leave the view", Toast.LENGTH_SHORT).show()
                        return@setOnItemSelectedListener false
                    }
                    else if (currentFragment.toString().startsWith("Time", true)) {
                        timeCards.scrollToTop()
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
                        Toast.makeText(this, "You must save and exit editing to leave the view", Toast.LENGTH_SHORT).show()
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

        when {
            accentColor.loadAccent() == 0 -> {
                bottomNav.itemActiveIndicatorColor =
                    ContextCompat.getColorStateList(this, R.color.colorPrimaryLightAppBar)
            }
            accentColor.loadAccent() == 1 -> {
                bottomNav.itemActiveIndicatorColor =
                    ContextCompat.getColorStateList(this, R.color.colorPrimaryPinkAppBar)
            }
            accentColor.loadAccent() == 2 -> {
                bottomNav.itemActiveIndicatorColor =
                    ContextCompat.getColorStateList(this, R.color.colorPrimaryOrangeAppBar)
            }
            accentColor.loadAccent() == 3 -> {
                bottomNav.itemActiveIndicatorColor =
                    ContextCompat.getColorStateList(this, R.color.colorPrimaryRedAppBar)
            }
            accentColor.loadAccent() == 4 -> {
                if (!FollowSystemVersion(this).loadSystemColor()) {
                    bottomNav.itemActiveIndicatorColor =
                        ContextCompat.getColorStateList(this, R.color.colorPrimaryPixelAppBar)
                } else {
                    if (themeSelection) {
                        bottomNav.itemActiveIndicatorColor =
                            ContextCompat.getColorStateList(this, R.color.googleItemIndicatorColor)
                    } else {
                        bottomNav.itemActiveIndicatorColor =
                            ContextCompat.getColorStateList(
                                this,
                                R.color.googleItemIndicatorColor_light
                            )
                    }
                }
            }
            accentColor.loadAccent() == 5 -> {
                bottomNav.itemActiveIndicatorColor =
                    ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavIconIndicatorColor()))
            }
        }
    }

    fun updateBottomNavCustomColor() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.itemIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavIconTintColor()))
        bottomNav.setBackgroundColor(Color.parseColor(CustomColorGenerator(this).generateCustomColorPrimary()))
        bottomNav.itemTextColor = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavTextColor()))
        bottomNav.itemRippleColor = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavIconTintColor()))
        bottomNav.itemActiveIndicatorColor = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(this).generateBottomNavIconIndicatorColor()))
    }

    private fun replaceFragment(fragment: Fragment, goingToTab: Int) {

        val transaction = supportFragmentManager.beginTransaction()
        val bottomNav = this.findViewById<BottomNavigationView>(R.id.bottom_nav)

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

    fun undo() {
        historyFragment.undo()
        changeBadgeNumber()
    }

    fun saveState() {

        changeBadgeNumber()
        historyFragment.saveState()
    }

    fun saveTimeCardState() {

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
        val badge =
            findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.history)
        badge.isVisible = true
        badge.number = dbHandler.getCount()
        if (AccentColor(this).loadAccent() != 3) {
            badge.backgroundColor = ContextCompat.getColor(this, R.color.redBadgeColor)
        } else {
            badge.backgroundColor = ContextCompat.getColor(this, R.color.lightRedBadgeColor)
        }
    }

    fun changeSettingsBadge() {
        val badge =
            findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.settings)
        if (Version(this).loadVersion() != BuildConfig.VERSION_NAME) {
            badge.isVisible = true

            if (AccentColor(this).loadAccent() != 3) {
                badge.backgroundColor = ContextCompat.getColor(this, R.color.redBadgeColor)
            } else {
                badge.backgroundColor = ContextCompat.getColor(this, R.color.lightRedBadgeColor)
            }
        } else {
            badge.isVisible = false
        }
    }

    fun toggleHistory() {
        val historyToggleData = HistoryToggleData(this)

        findViewById<BottomNavigationView>(R.id.bottom_nav).menu.findItem(R.id.history).isVisible =
            historyToggleData.loadHistoryState()
    }

    fun setActiveTab(view: Int) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        if (view == 0) {
            bottomNav.menu.findItem(R.id.ic_home).isChecked = true
        }
        else if (view == 1) {
            bottomNav.menu.findItem(R.id.history).isChecked = true
        }
        else if (view == 2) {
            bottomNav.menu.findItem(R.id.timeCards).isChecked = true
        }
        else if (view == 3) {
            bottomNav.menu.findItem(R.id.settings).isChecked = true
        }
    }

    @SuppressLint("CutPasteId")
    fun setBackgroundColor() {
        val mainConstraint = findViewById<ConstraintLayout>(R.id.mainConstraint)
        val badge =
            findViewById<BottomNavigationView>(R.id.bottom_nav).getOrCreateBadge(R.id.history)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val darkThemeData = DarkThemeData(this)
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                mainConstraint.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.darkThemeBackground
                    )
                )
                badge.badgeTextColor = ContextCompat.getColor(this, R.color.black)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                mainConstraint.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                badge.badgeTextColor = ContextCompat.getColor(this, R.color.white)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                mainConstraint.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
                badge.badgeTextColor = ContextCompat.getColor(this, R.color.black)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        mainConstraint.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.white
                            )
                        )
                        badge.badgeTextColor = ContextCompat.getColor(this, R.color.white)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        if (FollowSystemThemeChoice(this).loadFollowSystemThemePreference() == 0) {
                            mainConstraint.setBackgroundColor(
                                ContextCompat.getColor(
                                    this,
                                    R.color.black
                                )
                            )
                            badge.badgeTextColor = ContextCompat.getColor(this, R.color.black)
                        }
                        else {
                            mainConstraint.setBackgroundColor(
                                ContextCompat.getColor(
                                    this,
                                    R.color.darkThemeBackground
                                )
                            )
                            badge.badgeTextColor = ContextCompat.getColor(this,
                                R.color.darkThemeBackground
                            )
                        }
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        mainConstraint.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.black
                            )
                        )
                        badge.badgeTextColor = ContextCompat.getColor(this, R.color.black)
                    }
                }
            }
        }

        when {
            darkThemeData.loadDarkModeState() == 0 -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                if (window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
                    var flags: Int = this.window.decorView
                        .systemUiVisibility // get current flag

                    flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    this.window.decorView.systemUiVisibility = flags;
                }
            }
            darkThemeData.loadDarkModeState() == 3 -> {

                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        if (window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
                            var flags: Int = this.getWindow().getDecorView()
                                .getSystemUiVisibility() // get current flag

                            flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            this.getWindow().getDecorView().setSystemUiVisibility(flags);
                        }
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        if (window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
                            var flags: Int = this.getWindow().getDecorView()
                                .getSystemUiVisibility() // get current flag

                            flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            this.getWindow().getDecorView().setSystemUiVisibility(flags);
                        }
                    }
                }
            }
        }

        if (FollowSystemVersion(this).loadSystemColor() && AccentColor(this).loadAccent() == 4) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    /*override fun onBackPressed() {
        super.onBackPressed()

        Toast.makeText(this, "called", Toast.LENGTH_SHORT).show()


    }*/

    fun isComponentEnabled(componentName: String) : Int {
        return this.packageManager.getComponentEnabledSetting(ComponentName(
            BuildConfig.APPLICATION_ID,
            componentName
        ))
    }
}