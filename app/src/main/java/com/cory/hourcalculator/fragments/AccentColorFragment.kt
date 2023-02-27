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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily

class AccentColorFragment : Fragment() {

    var themeSelection = false

    private fun materialYouDrawable() : Drawable {

        val darkThemeData = DarkThemeData(requireContext())
        val followSystemVersion = FollowSystemVersion(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 && followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.materialyouimageview)!!
            }
            darkThemeData.loadDarkModeState() == 1 && !followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.followsystemcircleimageview)!!
            }
            darkThemeData.loadDarkModeState() == 0 && followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.materialyouimageview)!!
            }
            darkThemeData.loadDarkModeState() == 0 && !followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.followsystemcircleimageview)!!
            }
            darkThemeData.loadDarkModeState() == 2 && followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.materialyouimageview)!!
            }
            darkThemeData.loadDarkModeState() == 2 && !followSystemVersion.loadSystemColor() -> {
                return ContextCompat.getDrawable(requireContext(), R.drawable.followsystemcircleimageview)!!
            }
            darkThemeData.loadDarkModeState() == 3 -> {

                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        return if (followSystemVersion.loadSystemColor()) {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.materialyouimageview
                            )!!
                        } else {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.followsystemcircleimageview
                            )!!
                        }
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        return if (followSystemVersion.loadSystemColor()) {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.materialyouimageview
                            )!!
                        } else {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.followsystemcircleimageview
                            )!!
                        }
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        return if (followSystemVersion.loadSystemColor()) {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.materialyouimageview
                            )!!
                        } else {
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.followsystemcircleimageview
                            )!!
                        }
                    }
                }
            }
        }

        return ContextCompat.getDrawable(requireContext(), R.drawable.followsystemcircleimageview)!!
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                activity?.setTheme(R.style.Theme_DarkTheme)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
                themeSelection = false
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                        themeSelection = false
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(AccentColor(requireContext()).followSystemTheme(requireContext()))
                        themeSelection = true
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                        themeSelection = true
                    }
                }
            }
        }

        val accentColor = AccentColor(requireContext())
        val followSystemVersion = FollowSystemVersion(requireContext())

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
                if (!followSystemVersion.loadSystemColor()) {
                    activity?.theme?.applyStyle(R.style.system_accent, true)
                }
                else {
                    if (themeSelection) {
                        activity?.theme?.applyStyle(R.style.system_accent_google, true)
                    }
                    else {
                        activity?.theme?.applyStyle(R.style.system_accent_google_light, true)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_accent_color, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tealCardView = view.findViewById<MaterialCardView>(R.id.tealCardViewAccentColor)
        val pinkCardView = view.findViewById<MaterialCardView>(R.id.pinkCardViewAccentColor)
        val orangeCardView = view.findViewById<MaterialCardView>(R.id.orangeCardViewAccentColor)
        val redCardView = view.findViewById<MaterialCardView>(R.id.redCardViewAccentColor)
        val materialYouCardView = view.findViewById<MaterialCardView>(R.id.materialYouCardViewAccentColor)
        val materialYouImageView = view.findViewById<ImageView>(R.id.materialYouImageView)

        materialYouImageView.setImageDrawable(materialYouDrawable())

        tealCardView.shapeAppearanceModel = tealCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        pinkCardView.shapeAppearanceModel = pinkCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        orangeCardView.shapeAppearanceModel = orangeCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        redCardView.shapeAppearanceModel = redCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        materialYouCardView.shapeAppearanceModel = materialYouCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val tealRadioButton = view.findViewById<RadioButton>(R.id.tealAccent)
        val pinkRadioButton = view.findViewById<RadioButton>(R.id.pinkAccent)
        val orangeRadioButton = view.findViewById<RadioButton>(R.id.orangeAccent)
        val redRadioButton = view.findViewById<RadioButton>(R.id.redAccent)
        val materialYouRadioButton = view.findViewById<RadioButton>(R.id.materialYouAccent)

        val accentColor = AccentColor(requireContext())

        when {
            accentColor.loadAccent() == 0 -> {
                tealRadioButton.isChecked = true
            }
            accentColor.loadAccent() == 1 -> {
                pinkRadioButton.isChecked = true
            }
            accentColor.loadAccent() == 2 -> {
                orangeRadioButton.isChecked = true
            }
            accentColor.loadAccent() == 3 -> {
                redRadioButton.isChecked = true
            }
            accentColor.loadAccent() == 4 -> {
                materialYouRadioButton.isChecked = true
            }
        }

        tealRadioButton.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 0) {
                Toast.makeText(requireContext(), getString(R.string.teal_is_already_chosen), Toast.LENGTH_SHORT).show()
            } else {
                val dialog = BottomSheetDialog(requireContext())
                val restartAppLayout = layoutInflater.inflate(R.layout.restart_app_warning_bottom_sheet, null)
                dialog.setContentView(restartAppLayout)
                dialog.setCancelable(false)
                restartAppLayout.findViewById<Button>(R.id.yesButton).setOnClickListener {
                    Vibrate().vibration(requireContext())

                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 0) {
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
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.MaterialYou"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                    }
                    dialog.dismiss()
                    accentColor.setAccentState(0)
                    restartApplication()
                }
                restartAppLayout.findViewById<Button>(R.id.noButton).setOnClickListener {
                    Vibrate().vibration(requireContext())
                    tealRadioButton.isChecked = false
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        pinkRadioButton.setOnClickListener {
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
                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 0) {
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
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
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
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.MaterialYou"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                    }
                    accentColor.setAccentState(1)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    pinkRadioButton.isChecked = false
                }
                alert.show()
            }
        }
        orangeRadioButton.setOnClickListener {
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
                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 0) {
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.SplashOrange"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
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
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.MaterialYou"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                    }
                    accentColor.setAccentState(2)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    orangeRadioButton.isChecked = false
                }
                alert.show()
            }
        }
        redRadioButton.setOnClickListener {
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
                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 0) {
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
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
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
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.MaterialYou"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                    }
                    accentColor.setAccentState(3)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    redRadioButton.isChecked = false
                }
                alert.show()
            }
        }
        materialYouRadioButton.setOnClickListener {
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
                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == 0) {
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
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        activity?.packageManager?.setComponentEnabledSetting(
                            ComponentName(
                                BuildConfig.APPLICATION_ID,
                                "com.cory.hourcalculator.MaterialYou"
                            ),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                    }
                    accentColor.setAccentState(4)
                    restartApplication()
                }
                alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    materialYouRadioButton.isChecked = false
                }
                alert.show()
            }
        }

        val followSystemVersion = FollowSystemVersion(requireContext())
        val materialYouStyle2Switch = view.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)
        val followGoogleAppsCardView = view.findViewById<MaterialCardView>(R.id.followGoogleAppsCardView)

        if (Build.VERSION.SDK_INT < 31) {
            materialYouCardView?.visibility = View.GONE
            followGoogleAppsCardView?.visibility = View.GONE
            redCardView.shapeAppearanceModel = redCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(28f)
                .setBottomLeftCornerSize(28f)
                .build()
        }

        materialYouStyle2Switch.isChecked = followSystemVersion.loadSystemColor()

        if (followSystemVersion.loadSystemColor()) {
            materialYouStyle2Switch?.isChecked = true
            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!followSystemVersion.loadSystemColor()) {
            materialYouStyle2Switch?.isChecked = false
            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        materialYouStyle2Switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                Vibrate().vibration(requireContext())
                if (followSystemVersion.loadSystemColor()) {
                    if (accentColor.loadAccent() == 4) {
                        val alert = MaterialAlertDialogBuilder(
                            requireContext(),
                            AccentColor(requireContext()).alertTheme()
                        )
                        alert.setTitle(requireContext().getString(R.string.warning_experimental))
                        alert.setMessage(getString(R.string.may_produce_unwanted_results))
                        alert.setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                            Vibrate().vibration(requireContext())
                            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                            followSystemVersion.setSystemColor(false)
                            restartApplication()
                        }
                        alert.setNegativeButton(requireContext().getString(R.string.no)) { _, _ ->
                            Vibrate().vibration(requireContext())
                            materialYouStyle2Switch.isChecked = true
                            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                        }
                        alert.show()
                    } else {
                        materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                        materialYouStyle2Switch.isChecked = false
                        followSystemVersion.setSystemColor(false)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.app_theme_will_now_match_wallpaper),
                            Toast.LENGTH_SHORT
                        ).show()
                        materialYouImageView.setImageDrawable(materialYouDrawable())
                    }
                }
            }
            else {
                Vibrate().vibration(requireContext())
                materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                if (!followSystemVersion.loadSystemColor()) {
                    if (accentColor.loadAccent() == 4) {
                        val alert = MaterialAlertDialogBuilder(
                            requireContext(),
                            AccentColor(requireContext()).alertTheme()
                        )
                        alert.setTitle(requireContext().getString(R.string.warning_experimental))
                        alert.setMessage(getString(R.string.this_option_may_require_the_app_to_restart_desc))
                        alert.setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                            Vibrate().vibration(requireContext())
                            followSystemVersion.setSystemColor(true)
                            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                            restartApplication()
                        }
                        alert.setNegativeButton(requireContext().getString(R.string.no)) { _, _ ->
                            Vibrate().vibration(requireContext())
                            materialYouStyle2Switch.isChecked = false
                            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                        }
                        alert.show()
                    } else {
                        materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                        materialYouStyle2Switch.isChecked = true
                        followSystemVersion.setSystemColor(true)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.app_will_now_match_google_apps),
                            Toast.LENGTH_SHORT
                        ).show()
                        materialYouImageView.setImageDrawable(materialYouDrawable())
                    }
                }
            }
        }
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