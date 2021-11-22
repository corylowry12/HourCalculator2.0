package com.cory.hourcalculator.fragments

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.math.abs
import android.graphics.drawable.TransitionDrawable

import android.graphics.drawable.ColorDrawable




class AppearanceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                activity?.setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> activity?.setTheme(R.style.Theme_MyApplication)
                    Configuration.UI_MODE_NIGHT_YES -> activity?.setTheme(R.style.Theme_AMOLED)
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> activity?.setTheme(R.style.Theme_AMOLED)
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
            activity?.supportFragmentManager?.popBackStack()
        }

        val darkThemeData = DarkThemeData(requireContext())

        val lightThemeButton = activity?.findViewById<RadioButton>(R.id.lightTheme)
        val darkThemeButton = activity?.findViewById<RadioButton>(R.id.darkTheme)
        val amoledThemeButton = activity?.findViewById<RadioButton>(R.id.blackTheme)
        val followSystemThemeButton = activity?.findViewById<RadioButton>(R.id.followSystem)

        if (darkThemeData.loadDarkModeState() == 1) {
            darkThemeButton?.isChecked = true
        } else if (darkThemeData.loadDarkModeState() == 0) {
            lightThemeButton?.isChecked = true
        }
        else if (darkThemeData.loadDarkModeState() == 2) {
            amoledThemeButton?.isChecked = true
        }
        else if (darkThemeData.loadDarkModeState() == 3) {
            followSystemThemeButton?.isChecked = true
        }

        lightThemeButton?.setOnClickListener {
            //vibration(vibrationData)
            if (darkThemeData.loadDarkModeState() == 0) {
                Toast.makeText(requireContext(), "Light theme is already enabled", Toast.LENGTH_SHORT).show()
            } else {
                darkThemeData.setDarkModeState(0)
                darkThemeButton?.isChecked = false
                amoledThemeButton?.isChecked = false
                followSystemThemeButton?.isChecked = false
                restartThemeChange()

            }
        }
        darkThemeButton?.setOnClickListener {
            //vibration(vibrationData)
            if (darkThemeData.loadDarkModeState() == 1) {
                Toast.makeText(requireContext(), "Dark mode is already enabled", Toast.LENGTH_SHORT).show()
            } else {
                darkThemeData.setDarkModeState(1)
                lightThemeButton?.isChecked = false
                amoledThemeButton?.isChecked = false
                followSystemThemeButton?.isChecked = false
                restartThemeChange()

            }
        }
        amoledThemeButton?.setOnClickListener {
            if (darkThemeData.loadDarkModeState() == 2) {
                Toast.makeText(requireContext(), "Black theme is already enabled", Toast.LENGTH_SHORT).show()
            } else {
                darkThemeData.setDarkModeState(2)
                lightThemeButton?.isChecked = false
                darkThemeButton?.isChecked = false
                followSystemThemeButton?.isChecked = false
                restartThemeChange()

            }
        }
        followSystemThemeButton?.setOnClickListener {
            if (darkThemeData.loadDarkModeState() == 3) {
                Toast.makeText(requireContext(), "System theme is already enabled", Toast.LENGTH_SHORT).show()
            } else {
                darkThemeData.setDarkModeState(3)
                lightThemeButton?.isChecked = false
                darkThemeButton?.isChecked = false
                amoledThemeButton?.isChecked = false
                restartThemeChange()

            }
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
            //vibration(vibrationData)
            if (accentColor.loadAccent() == 0) {
                Toast.makeText(requireContext(), "Teal already chosen", Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                alert.setTitle("Warning")
                alert.setMessage("This will restart the app, do you want to continue?")
                alert.setPositiveButton("Yes") { _, _ ->
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
                alert.setNegativeButton("No") { _, _ ->
                    tealAccentButton.isChecked = false
                }
                alert.show()
            }
        }
        pinkAccentButton?.setOnClickListener {
           // vibration(vibrationData)
            if (accentColor.loadAccent() == 1) {
                Toast.makeText(requireContext(), "Pink already chosen", Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                alert.setTitle("Warning")
                alert.setMessage("This will restart the app, do you want to continue?")
                alert.setPositiveButton("Yes") { _, _ ->
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
                alert.setNegativeButton("No") { _, _ ->
                    pinkAccentButton.isChecked = false
                }
                alert.show()
            }
        }
        orangeAccentButton?.setOnClickListener {
            //vibration(vibrationData)
            if (accentColor.loadAccent() == 2) {
                Toast.makeText(requireContext(), "orange already chosen", Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                alert.setTitle("Warning")
                alert.setMessage("This will restart the app, do you want to continue?")
                alert.setPositiveButton("Yes") { _, _ ->
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
                alert.setNegativeButton("No") { _, _ ->
                    orangeAccentButton.isChecked = false
                }
                alert.show()
            }
        }
        redAccentButton?.setOnClickListener {
            //vibration(vibrationData)
            if (accentColor.loadAccent() == 3) {
                Toast.makeText(requireContext(), "Red is already chosen", Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                alert.setTitle("Warning")
                alert.setMessage("This will restart the app, do you want to continue?")
                alert.setPositiveButton("Yes") { _, _ ->
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
                alert.setNegativeButton("No") { _, _ ->
                    redAccentButton.isChecked = false
                }
                alert.show()
            }
        }
        systemAccentButton?.setOnClickListener {
            //vibration(vibrationData)
            if (accentColor.loadAccent() == 4) {
                Toast.makeText(requireContext(), "Already chosen", Toast.LENGTH_SHORT).show()
            } else {
                val alert = MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                alert.setTitle("Warning")
                alert.setMessage("This will restart the app, do you want to continue?")
                alert.setPositiveButton("Yes") { _, _ ->
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
                alert.setNegativeButton("No") { _, _ ->
                    systemAccentButton.isChecked = false
                }
                alert.show()
            }
        }

       activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
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

    }

    private fun restartApplication() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }, 1000)
    }
}