package com.cory.hourcalculator.fragments

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.DelicateCoroutinesApi


@DelicateCoroutinesApi
class AppearanceFragment : Fragment() {

    private lateinit var followSystemImageViewDrawable: Drawable

    private fun setDrawable(): Drawable {
        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> {
                return ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.whitecircleimageview
                )!!
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                return if (FollowSystemThemeChoice(requireContext()).loadFollowSystemThemePreference() == 0) {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.blackcircleimageview
                    )!!
                } else {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.graycircleimageview
                    )!!
                }
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                return if (FollowSystemThemeChoice(requireContext()).loadFollowSystemThemePreference() == 0) {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.blackcircleimageview
                    )!!
                } else {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.graycircleimageview
                    )!!
                }
            }
        }

        return ContextCompat.getDrawable(requireContext(), R.drawable.blackcircleimageview)!!
    }

    private fun getCurrentNightTheme(): Boolean {

        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> {
                return false
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                return true
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                return true
            }
        }

        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                followSystemImageViewDrawable = setDrawable()
                activity?.setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                followSystemImageViewDrawable = setDrawable()
                activity?.setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                followSystemImageViewDrawable = setDrawable()
                activity?.setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                        followSystemImageViewDrawable = setDrawable()
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(AccentColor(requireContext()).followSystemTheme(requireContext()))
                        followSystemImageViewDrawable = setDrawable()
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(AccentColor(requireContext()).followSystemTheme(requireContext()))
                        followSystemImageViewDrawable = setDrawable()
                    }
                }
            }
        }

        val accentColor = AccentColor(requireContext())
        when {
            accentColor.loadAccent() == 0 -> {
                activity?.theme?.applyStyle(R.style.teal_accent, true)
            }
            accentColor.loadAccent() == 1 -> {
                activity?.theme?.applyStyle(R.style.pink_accent, true)
            }
            accentColor.loadAccent() == 2 -> {
                activity?.theme?.applyStyle(R.style.orange_accent, true)
            }
            accentColor.loadAccent() == 3 -> {
                activity?.theme?.applyStyle(R.style.red_accent, true)
            }
            accentColor.loadAccent() == 4 -> {
                activity?.theme?.applyStyle(R.style.system_accent, true)
            }
        }
        return inflater.inflate(R.layout.fragment_appearance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarAppearance)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.reset -> {
                    reset()
                    true
                }
                else -> true
            }
        }

        val nestedScrollViewAppearance =
            view.findViewById<NestedScrollView>(R.id.nestedScrollViewAppearance)

        nestedScrollViewAppearance.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            AppearanceScrollPosition(requireContext()).setScroll(scrollY.toFloat())
        }

        val followSystemImageView = activity?.findViewById<ImageView>(R.id.followSystemImageView)
        followSystemImageView?.setImageDrawable(followSystemImageViewDrawable)

        val darkThemeData = DarkThemeData(requireContext())

        val lightThemeButton = activity?.findViewById<RadioButton>(R.id.lightTheme)
        val darkThemeButton = activity?.findViewById<RadioButton>(R.id.darkTheme)
        val amoledThemeButton = activity?.findViewById<RadioButton>(R.id.blackTheme)
        val followSystemThemeButton = activity?.findViewById<RadioButton>(R.id.followSystem)

        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                darkThemeButton?.isChecked = true
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                lightThemeButton?.isChecked = true
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                amoledThemeButton?.isChecked = true
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                followSystemThemeButton?.isChecked = true
            }
        }

        lightThemeButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (darkThemeData.loadDarkModeState() == 0) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.light_theme_is_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                darkThemeData.setDarkModeState(0)
                darkThemeButton?.isChecked = false
                amoledThemeButton?.isChecked = false
                followSystemThemeButton?.isChecked = false
                restartThemeChange()

            }
        }
        darkThemeButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (darkThemeData.loadDarkModeState() == 1) {
                Toast.makeText(requireContext(), getString(R.string.dark_theme_already_enabled), Toast.LENGTH_SHORT)
                    .show()
            } else {
                darkThemeData.setDarkModeState(1)
                lightThemeButton?.isChecked = false
                amoledThemeButton?.isChecked = false
                followSystemThemeButton?.isChecked = false
                restartThemeChange()

            }
        }
        amoledThemeButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (darkThemeData.loadDarkModeState() == 2) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.black_theme_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                darkThemeData.setDarkModeState(2)
                lightThemeButton?.isChecked = false
                darkThemeButton?.isChecked = false
                followSystemThemeButton?.isChecked = false
                restartThemeChange()

            }
        }
        followSystemThemeButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (darkThemeData.loadDarkModeState() == 3) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.system_theme_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                darkThemeData.setDarkModeState(3)
                lightThemeButton?.isChecked = false
                darkThemeButton?.isChecked = false
                amoledThemeButton?.isChecked = false
                restartThemeChange()

            }
        }

        val followSystemThemeChoice = FollowSystemThemeChoice(requireContext())

        val blackTheme = activity?.findViewById<RadioButton>(R.id.amoledSystemTheme)
        val darkTheme = activity?.findViewById<RadioButton>(R.id.darkThemeSystem)

        if (followSystemThemeChoice.loadFollowSystemThemePreference() == 0) {
            blackTheme?.isChecked = true
        }
        else {
            darkTheme?.isChecked = true
        }

        blackTheme?.setOnClickListener {
            if (followSystemThemeChoice.loadFollowSystemThemePreference() == 0) {
                Toast.makeText(requireContext(), getString(R.string.follow_system_already_set_to_black_theme), Toast.LENGTH_SHORT).show()
            }
            else {
                followSystemThemeChoice.setFollowSystemThemePreference(0)
                darkTheme?.isChecked = false

                if (darkThemeData.loadDarkModeState() == 3) {
                    if (getCurrentNightTheme()) {
                        restartThemeChange()
                    }
                }
            }

            followSystemImageView?.setImageDrawable(setDrawable())
        }

        darkTheme?.setOnClickListener {
            if (followSystemThemeChoice.loadFollowSystemThemePreference() == 1) {
                Toast.makeText(requireContext(), getString(R.string.follow_system_already_set_to_dark_theme), Toast.LENGTH_SHORT).show()
            }
            else {
                followSystemThemeChoice.setFollowSystemThemePreference(1)
                blackTheme?.isChecked = false

                if (darkThemeData.loadDarkModeState() == 3) {
                    if (getCurrentNightTheme()) {
                        restartThemeChange()
                    }
                }
            }

            followSystemImageView?.setImageDrawable(setDrawable())
        }

        val accentColor = AccentColor(requireContext())

        val tealAccentButton = activity?.findViewById<RadioButton>(R.id.Teal)
        val pinkAccentButton = activity?.findViewById<RadioButton>(R.id.Pink)
        val orangeAccentButton = activity?.findViewById<RadioButton>(R.id.Orange)
        val redAccentButton = activity?.findViewById<RadioButton>(R.id.Red)
        val systemAccentButton = activity?.findViewById<RadioButton>(R.id.systemAccent)

        val systemAccentImage = activity?.findViewById<ImageView>(R.id.systemAccentImageView)

        if (Build.VERSION.RELEASE.toInt() < 12) {
            systemAccentButton?.visibility = View.GONE
            systemAccentImage?.visibility = View.GONE
        }

        when {
            accentColor.loadAccent() == 0 -> {
                tealAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 1 -> {
                pinkAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 2 -> {
                orangeAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 3 -> {
                redAccentButton?.isChecked = true
            }
            accentColor.loadAccent() == 4 -> {
                systemAccentButton?.isChecked = true
            }
        }

        tealAccentButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 0) {
                Toast.makeText(requireContext(), getString(R.string.teal_is_already_chosen), Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alert.setTitle(getString(R.string.warning))
                alert.setMessage(getString(R.string.restart_warning))
                alert.setCancelable(false)
                alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashOrange"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashRed"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashPink"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashScreenNoIcon"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
                    )
                    accentColor.setAccentState(0)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    tealAccentButton.isChecked = false
                }
                alert.show()
            }
        }
        pinkAccentButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 1) {
                Toast.makeText(requireContext(), getString(R.string.pink_is_already_chosen), Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alert.setTitle(getString(R.string.warning))
                alert.setMessage(getString(R.string.restart_warning))
                alert.setCancelable(false)
                alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashOrange"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashRed"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashPink"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashScreenNoIcon"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    accentColor.setAccentState(1)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    pinkAccentButton.isChecked = false
                }
                alert.show()
            }
        }
        orangeAccentButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 2) {
                Toast.makeText(requireContext(), getString(R.string.orange_is_already_chosen), Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alert.setTitle(getString(R.string.warning))
                alert.setMessage(getString(R.string.restart_warning))
                alert.setCancelable(false)
                alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashOrange"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashRed"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashPink"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashScreenNoIcon"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    accentColor.setAccentState(2)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    orangeAccentButton.isChecked = false
                }
                alert.show()
            }
        }
        redAccentButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 3) {
                Toast.makeText(requireContext(), getString(R.string.red_already_chosen), Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alert.setTitle(getString(R.string.warning))
                alert.setMessage(getString(R.string.restart_warning))
                alert.setCancelable(false)
                alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashOrange"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashRed"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashPink"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashScreenNoIcon"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    accentColor.setAccentState(3)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    redAccentButton.isChecked = false
                }
                alert.show()
            }
        }
        systemAccentButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 4) {
                Toast.makeText(requireContext(), getString(R.string.system_accent_color_is_already_enabled), Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alert.setTitle(getString(R.string.warning_experimental))
                alert.setMessage(getString(R.string.system_accent_warning))
                alert.setCancelable(false)
                alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashOrange"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashRed"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashPink"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashScreenNoIcon"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
                    )
                    accentColor.setAccentState(4)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    systemAccentButton.isChecked = false
                }
                alert.show()
            }
        }

        val enableColoredNavBar = view.findViewById<RadioButton>(R.id.enableColoredNavBar)
        val disableColoredNavBar = view.findViewById<RadioButton>(R.id.disableColoredNavBar)
        val coloredNavBarData = ColoredNavBarData(requireContext())

        if (!coloredNavBarData.loadNavBar()) {
            disableColoredNavBar.isChecked = true
        } else {
            enableColoredNavBar.isChecked = true
        }

        enableColoredNavBar.setOnClickListener {

            Vibrate().vibration(requireContext())
            if (coloredNavBarData.loadNavBar()) {
                Toast.makeText(requireContext(), getString(R.string.colored_nav_bar_is_already_enabled), Toast.LENGTH_SHORT).show()
            } else {
                disableColoredNavBar.isChecked = false
                val alert = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alert.setTitle(getString(R.string.warning))
                alert.setMessage(getString(R.string.may_or_may_not_work_warning))
                alert.setCancelable(false)
                alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    Vibrate().vibration(requireContext())

                    coloredNavBarData.setNavBar(true)

                    when {
                        accentColor.loadAccent() == 0 -> {
                            activity?.window?.navigationBarColor =
                                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                        }
                        accentColor.loadAccent() == 1 -> {
                            activity?.window?.navigationBarColor =
                                ContextCompat.getColor(requireContext(), R.color.pinkAccent)
                        }
                        accentColor.loadAccent() == 2 -> {
                            activity?.window?.navigationBarColor =
                                ContextCompat.getColor(requireContext(), R.color.orangeAccent)
                        }
                        accentColor.loadAccent() == 3 -> {
                            activity?.window?.navigationBarColor =
                                ContextCompat.getColor(requireContext(), R.color.redAccent)
                        }
                        accentColor.loadAccent() == 4 -> {
                            activity?.window?.navigationBarColor =
                                ContextCompat.getColor(requireContext(), R.color.systemAccent)
                        }
                    }

                    Toast.makeText(requireContext(), getString(R.string.colored_nav_bar_enabled), Toast.LENGTH_SHORT).show()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    enableColoredNavBar.isChecked = false
                    disableColoredNavBar.isChecked = true
                }
                alert.show()
            }
        }

        disableColoredNavBar.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (!coloredNavBarData.loadNavBar()) {
                Toast.makeText(requireContext(), getString(R.string.colored_navigation_bar_is_already_disabled), Toast.LENGTH_SHORT).show()
            } else {
                enableColoredNavBar.isChecked = false
                activity?.window?.navigationBarColor =
                    ContextCompat.getColor(requireContext(), R.color.black)
                coloredNavBarData.setNavBar(false)

                Toast.makeText(requireContext(), getString(R.string.colored_nav_bar_disabled), Toast.LENGTH_SHORT).show()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager?.popBackStack()
                }
            })
    }

    private fun restartThemeChange() {

        val runnable = Runnable {
            (context as MainActivity).setBackgroundColor()
        }
        MainActivity().runOnUiThread(runnable)

        activity?.supportFragmentManager?.beginTransaction()
            ?.detach(this)?.commitNow()
        activity?.supportFragmentManager?.beginTransaction()
            ?.attach(this)?.commitNow()

        view?.findViewById<NestedScrollView>(R.id.nestedScrollViewAppearance)
            ?.scrollTo(0, AppearanceScrollPosition(requireContext()).loadScroll().toInt())

    }

    private fun reset() {
        val alert =
            MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
        alert.setTitle(getString(R.string.warning))
        alert.setMessage(getString(R.string.would_you_like_to_reset_appearance_settings))
        alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
            Vibrate().vibration(requireContext())
            when {
                AccentColor(requireContext()).loadAccent() != 0 -> {
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashOrange"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashRed"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashPink"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                    activity?.packageManager?.setComponentEnabledSetting(
                        ComponentName(
                            BuildConfig.APPLICATION_ID,
                            "com.cory.hourcalculator.SplashScreenNoIcon"
                        ),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
                    )
                    AccentColor(requireContext()).setAccentState(0)
                    DarkThemeData(requireContext()).setDarkModeState(3)
                    restartApplication()
                }
                DarkThemeData(requireContext()).loadDarkModeState() != 3 -> {
                    view?.findViewById<RadioButton>(R.id.lightTheme)?.isChecked = false
                    view?.findViewById<RadioButton>(R.id.darkTheme)?.isChecked = false
                    view?.findViewById<RadioButton>(R.id.blackTheme)?.isChecked = false
                    view?.findViewById<RadioButton>(R.id.followSystem)?.isChecked = true
                    AccentColor(requireContext()).setAccentState(0)
                    DarkThemeData(requireContext()).setDarkModeState(3)
                    restartThemeChange()
                }
                else -> {
                    AccentColor(requireContext()).setAccentState(0)
                    DarkThemeData(requireContext()).setDarkModeState(3)
                }
            }
            ColoredNavBarData(requireContext()).setNavBar(false)
            view?.findViewById<RadioButton>(R.id.disableColoredNavBar)?.isChecked = true
            view?.findViewById<RadioButton>(R.id.enableColoredNavBar)?.isChecked = false
            activity?.window?.navigationBarColor =
                ContextCompat.getColor(requireContext(), R.color.black)

            if (FollowSystemThemeChoice(requireContext()).loadFollowSystemThemePreference() != 0) {
                FollowSystemThemeChoice(requireContext()).setFollowSystemThemePreference(0)
                view?.findViewById<RadioButton>(R.id.darkThemeSystem)?.isChecked = false
                view?.findViewById<RadioButton>(R.id.amoledSystemTheme)?.isChecked = true
                restartThemeChange()
            }
            Toast.makeText(requireContext(), getString(R.string.appearance_settings_reset), Toast.LENGTH_LONG).show()

        }
        alert.setNegativeButton(getString(R.string.no)) { _, _ ->
            Vibrate().vibration(requireContext())
        }
        alert.show()
    }

    private fun restartApplication() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent =
                requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }, 1000)
    }
}