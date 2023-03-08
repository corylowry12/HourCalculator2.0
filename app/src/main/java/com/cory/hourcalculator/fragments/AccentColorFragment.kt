package com.cory.hourcalculator.fragments

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.*
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.intents.MainActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import com.google.android.material.slider.Slider
import kotlin.math.max
import kotlin.math.min

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
            accentColor.loadAccent() == 5 -> {
                activity?.theme?.applyStyle(R.style.transparent_accent, true)
            }
        }
        return inflater.inflate(R.layout.fragment_accent_color, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBarAccent = view.findViewById<MaterialToolbar>(R.id.materialToolBarAccentColorFragment)

        val resetDrawable = topAppBarAccent?.menu?.findItem(R.id.reset)?.icon
        resetDrawable?.mutate()

        val navigationDrawable = topAppBarAccent?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
            val id = typedValue.resourceId
            resetDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            resetDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

        topAppBarAccent?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        if (AccentColor(requireContext()).loadAccent() == 5) {
            updateCustomColorChange()
        }

        topAppBarAccent.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.reset -> {
                    Vibrate().vibration(requireContext())
                    val dialog = BottomSheetDialog(requireContext())
                    val resetSettingsLayout = layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
                    val yesResetButton = resetSettingsLayout.findViewById<Button>(R.id.yesResetButton)
                    val cancelResetButton = resetSettingsLayout.findViewById<Button>(R.id.cancelResetButton)
                    if (AccentColor(requireContext()).loadAccent() == 5) {
                        resetSettingsLayout.findViewById<MaterialCardView>(R.id.bodyCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                        yesResetButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                        cancelResetButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                    }
                    dialog.setContentView(resetSettingsLayout)
                    dialog.setCancelable(false)
                    resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text = "Would you like to reset Accent Color Settings?"
                    /*if (resources.getBoolean(R.bool.isTablet)) {
                        val bottomSheet =
                            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.isDraggable = false
                    }*/
                    yesResetButton.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        reset()
                        dialog.dismiss()
                    }
                    cancelResetButton.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        dialog.dismiss()
                    }
                    dialog.show()
                    true
                }
                else -> false
            }
        }

        val tealCardView = view.findViewById<MaterialCardView>(R.id.tealCardViewAccentColor)
        val pinkCardView = view.findViewById<MaterialCardView>(R.id.pinkCardViewAccentColor)
        val orangeCardView = view.findViewById<MaterialCardView>(R.id.orangeCardViewAccentColor)
        val redCardView = view.findViewById<MaterialCardView>(R.id.redCardViewAccentColor)
        val customCardView = view.findViewById<MaterialCardView>(R.id.customCardViewAccentColor)
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
        customCardView.shapeAppearanceModel = customCardView.shapeAppearanceModel
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
        val customRadioButton = view.findViewById<RadioButton>(R.id.customAccent)
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
            accentColor.loadAccent() == 5 -> {
                customRadioButton.isChecked = true
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
                if (AccentColor(requireContext()).loadAccent() == 5) {
                    restartAppLayout.findViewById<MaterialCardView>(R.id.bodyTextCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                    restartAppLayout.findViewById<Button>(R.id.yesButton).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                    restartAppLayout.findViewById<Button>(R.id.noButton).setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                }
                restartAppLayout.findViewById<Button>(R.id.yesButton).setOnClickListener {
                    Vibrate().vibration(requireContext())

                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "auto") {
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
                val dialog = BottomSheetDialog(requireContext())
                val restartAppLayout = layoutInflater.inflate(R.layout.restart_app_warning_bottom_sheet, null)
                dialog.setContentView(restartAppLayout)
                dialog.setCancelable(false)
                if (AccentColor(requireContext()).loadAccent() == 5) {
                    restartAppLayout.findViewById<MaterialCardView>(R.id.bodyTextCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                    restartAppLayout.findViewById<Button>(R.id.yesButton).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                    restartAppLayout.findViewById<Button>(R.id.noButton).setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                }
                restartAppLayout.findViewById<Button>(R.id.yesButton).setOnClickListener {
                    Vibrate().vibration(requireContext())
                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "auto") {
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
                    dialog.dismiss()
                    accentColor.setAccentState(1)
                    restartApplication()
                }
                restartAppLayout.findViewById<Button>(R.id.noButton).setOnClickListener {
                    Vibrate().vibration(requireContext())
                    pinkRadioButton.isChecked = false
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        orangeRadioButton.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 2) {
                Toast.makeText(requireContext(), getString(R.string.orange_is_already_chosen), Toast.LENGTH_SHORT).show()
            } else {
                val dialog = BottomSheetDialog(requireContext())
                val restartAppLayout = layoutInflater.inflate(R.layout.restart_app_warning_bottom_sheet, null)
                dialog.setContentView(restartAppLayout)
                dialog.setCancelable(false)
                if (AccentColor(requireContext()).loadAccent() == 5) {
                    restartAppLayout.findViewById<MaterialCardView>(R.id.bodyTextCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                    restartAppLayout.findViewById<Button>(R.id.yesButton).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                    restartAppLayout.findViewById<Button>(R.id.noButton).setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                }
                restartAppLayout.findViewById<Button>(R.id.yesButton).setOnClickListener {
                    Vibrate().vibration(requireContext())
                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "auto") {
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
                    dialog.dismiss()
                    accentColor.setAccentState(2)
                    restartApplication()
                }
                restartAppLayout.findViewById<Button>(R.id.noButton).setOnClickListener {
                    Vibrate().vibration(requireContext())
                    orangeRadioButton.isChecked = false
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        redRadioButton.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 3) {
                Toast.makeText(requireContext(), getString(R.string.red_already_chosen), Toast.LENGTH_SHORT).show()
            } else {
                val dialog = BottomSheetDialog(requireContext())
                val restartAppLayout = layoutInflater.inflate(R.layout.restart_app_warning_bottom_sheet, null)
                dialog.setContentView(restartAppLayout)
                dialog.setCancelable(false)
                if (AccentColor(requireContext()).loadAccent() == 5) {
                    restartAppLayout.findViewById<MaterialCardView>(R.id.bodyTextCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                    restartAppLayout.findViewById<Button>(R.id.yesButton).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                    restartAppLayout.findViewById<Button>(R.id.noButton).setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                }
                restartAppLayout.findViewById<Button>(R.id.yesButton).setOnClickListener {
                    Vibrate().vibration(requireContext())
                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "auto") {
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
                    dialog.dismiss()
                    accentColor.setAccentState(3)
                    restartApplication()
                }
                restartAppLayout.findViewById<Button>(R.id.noButton).setOnClickListener {
                    Vibrate().vibration(requireContext())
                    redRadioButton.isChecked = false
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        customRadioButton.setOnClickListener {
            Vibrate().vibration(requireContext())
            val customColorPickerDialog = BottomSheetDialog(requireContext())
            val customColorPickerLayout = layoutInflater.inflate(R.layout.custom_color_bottom_sheet, null)
            customColorPickerDialog.setContentView(customColorPickerLayout)
            customColorPickerDialog.setCancelable(false)

            val customColor = Color.parseColor(CustomColorGenerator(requireContext()).loadCustomHex())
            var redValue = customColor.red
            var greenValue = customColor.green
            var blueValue = customColor.blue

            val coloredCardView = customColorPickerLayout.findViewById<MaterialCardView>(R.id.coloredCardView)
            val redSlider = customColorPickerLayout.findViewById<Slider>(R.id.redSlider)
            val greenSlider = customColorPickerLayout.findViewById<Slider>(R.id.greenSlider)
            val blueSlider = customColorPickerLayout.findViewById<Slider>(R.id.blueSlider)
            val hashtagTextView = customColorPickerLayout.findViewById<TextView>(R.id.hashtagTextView)
            val hexadecimalTextView = customColorPickerLayout.findViewById<TextView>(R.id.hexadecimalTextView)

            val selectButton = customColorPickerLayout.findViewById<Button>(R.id.selectColorButton)
            val cancelSelectButton = customColorPickerLayout.findViewById<Button>(R.id.cancelSelectColorButton)

            if (AccentColor(requireContext()).loadAccent() == 5) {
                selectButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                cancelSelectButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
            }

            var hex = String.format("#%02X%02X%02X", redValue, greenValue, blueValue).drop(1)
            coloredCardView.setCardBackgroundColor(Color.parseColor("#$hex"))
            hexadecimalTextView.text = hex

            redSlider.value = redValue.toFloat()
            greenSlider.value = greenValue.toFloat()
            blueSlider.value = blueValue.toFloat()

            if (redValue < 100 || greenValue < 100 || blueValue < 100) {
                hashtagTextView.setTextColor(Color.parseColor("#ffffff"))
                hexadecimalTextView.setTextColor(Color.parseColor("#ffffff"))
            }
            else {
                hashtagTextView.setTextColor(Color.parseColor("#000000"))
                hexadecimalTextView.setTextColor(Color.parseColor("#000000"))
            }

            redSlider.addOnChangeListener { slider, value, fromUser ->
                redValue = slider.value.toInt()
                hex = String.format("#%02X%02X%02X", redValue, greenValue, blueValue).drop(1)
                hexadecimalTextView.text = hex
                coloredCardView.setCardBackgroundColor(Color.parseColor("#$hex"))
                if (redValue < 100 || greenValue < 100 || blueValue < 100) {
                    hashtagTextView.setTextColor(Color.parseColor("#ffffff"))
                    hexadecimalTextView.setTextColor(Color.parseColor("#ffffff"))
                }
                else {
                    hashtagTextView.setTextColor(Color.parseColor("#000000"))
                    hexadecimalTextView.setTextColor(Color.parseColor("#000000"))
                }
            }
            greenSlider.addOnChangeListener { slider, value, fromUser ->
                greenValue = slider.value.toInt()
                hex = String.format("#%02X%02X%02X", redValue, greenValue, blueValue).drop(1)
                hexadecimalTextView.text = hex
                coloredCardView.setCardBackgroundColor(Color.parseColor("#$hex"))
                if (redValue < 100 || greenValue < 100 || blueValue < 100) {
                    hashtagTextView.setTextColor(Color.parseColor("#ffffff"))
                    hexadecimalTextView.setTextColor(Color.parseColor("#ffffff"))
                }
                else {
                    hashtagTextView.setTextColor(Color.parseColor("#000000"))
                    hexadecimalTextView.setTextColor(Color.parseColor("#000000"))
                }
            }
            blueSlider.addOnChangeListener { slider, value, fromUser ->
                blueValue = slider.value.toInt()
                hex = String.format("#%02X%02X%02X", redValue, greenValue, blueValue).drop(1)
                hexadecimalTextView.text = hex
                coloredCardView.setCardBackgroundColor(Color.parseColor("#$hex"))
                if (redValue < 100 || greenValue < 100 || blueValue < 100) {
                    hashtagTextView.setTextColor(Color.parseColor("#ffffff"))
                    hexadecimalTextView.setTextColor(Color.parseColor("#ffffff"))
                }
                else {
                    hashtagTextView.setTextColor(Color.parseColor("#000000"))
                    hexadecimalTextView.setTextColor(Color.parseColor("#000000"))
                }
            }

            selectButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                customColorPickerDialog.dismiss()
                CustomColorGenerator(requireContext()).setCustomHex("#$hex")
                AccentColor(requireContext()).setAccentState(5)
                updateCustomColorChange()

                tealRadioButton.isChecked = false
                pinkRadioButton.isChecked = false
                orangeRadioButton.isChecked = false
                redRadioButton.isChecked = false
                materialYouRadioButton.isChecked = false
            }
            cancelSelectButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                if (AccentColor(requireContext()).loadAccent() != 5) {
                    customRadioButton.isChecked = false
                }
                customColorPickerDialog.dismiss()
            }

            customColorPickerDialog.show()
        }
        materialYouRadioButton.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (accentColor.loadAccent() == 4) {
                Toast.makeText(requireContext(), getString(R.string.system_accent_color_is_already_enabled), Toast.LENGTH_SHORT).show()
            } else {
                val dialog = BottomSheetDialog(requireContext())
                val restartAppLayout = layoutInflater.inflate(R.layout.restart_app_warning_bottom_sheet, null)
                dialog.setContentView(restartAppLayout)
                dialog.setCancelable(false)
                if (AccentColor(requireContext()).loadAccent() == 5) {
                    restartAppLayout.findViewById<MaterialCardView>(R.id.bodyTextCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                    restartAppLayout.findViewById<Button>(R.id.yesButton).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                    restartAppLayout.findViewById<Button>(R.id.noButton).setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                }
                restartAppLayout.findViewById<Button>(R.id.yesButton).setOnClickListener {
                    Vibrate().vibration(requireContext())
                    if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "auto") {
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
                    dialog.dismiss()
                    accentColor.setAccentState(4)
                    restartApplication()
                }
                restartAppLayout.findViewById<Button>(R.id.noButton).setOnClickListener {
                    Vibrate().vibration(requireContext())
                    materialYouRadioButton.isChecked = false
                    dialog.dismiss()
                }
                dialog.show()
            }
        }

        val followSystemVersion = FollowSystemVersion(requireContext())
        val materialYouStyle2Switch = view.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)
        val followGoogleAppsCardView = view.findViewById<MaterialCardView>(R.id.followGoogleAppsCardView)

        if (Build.VERSION.SDK_INT < 31) {
            materialYouCardView?.visibility = View.GONE
            followGoogleAppsCardView?.visibility = View.GONE
            customCardView.shapeAppearanceModel = customCardView.shapeAppearanceModel
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
                        val dialog = BottomSheetDialog(requireContext())
                        val restartAppLayout = layoutInflater.inflate(R.layout.restart_app_warning_bottom_sheet, null)
                        dialog.setContentView(restartAppLayout)
                        dialog.setCancelable(false)
                        if (AccentColor(requireContext()).loadAccent() == 5) {
                            restartAppLayout.findViewById<MaterialCardView>(R.id.bodyTextCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                            restartAppLayout.findViewById<Button>(R.id.yesButton).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                            restartAppLayout.findViewById<Button>(R.id.noButton).setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                        }
                        restartAppLayout.findViewById<Button>(R.id.yesButton).setOnClickListener {
                            Vibrate().vibration(requireContext())
                            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                            followSystemVersion.setSystemColor(false)
                            restartApplication()
                            dialog.dismiss()
                        }
                        restartAppLayout.findViewById<Button>(R.id.noButton).setOnClickListener {
                            Vibrate().vibration(requireContext())
                            materialYouStyle2Switch.isChecked = true
                            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                            dialog.dismiss()
                        }
                        dialog.show()
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
                        val dialog = BottomSheetDialog(requireContext())
                        val restartAppLayout = layoutInflater.inflate(R.layout.restart_app_warning_bottom_sheet, null)
                        dialog.setContentView(restartAppLayout)
                        dialog.setCancelable(false)
                        if (AccentColor(requireContext()).loadAccent() == 5) {
                            restartAppLayout.findViewById<MaterialCardView>(R.id.bodyTextCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                            restartAppLayout.findViewById<Button>(R.id.yesButton).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                            restartAppLayout.findViewById<Button>(R.id.noButton).setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                        }
                        restartAppLayout.findViewById<Button>(R.id.yesButton).setOnClickListener {
                            Vibrate().vibration(requireContext())
                            followSystemVersion.setSystemColor(true)
                            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                            restartApplication()
                            dialog.dismiss()
                        }
                        restartAppLayout.findViewById<Button>(R.id.noButton).setOnClickListener {
                            Vibrate().vibration(requireContext())
                            materialYouStyle2Switch.isChecked = false
                            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                            dialog.dismiss()
                        }
                        dialog.show()
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

    fun reset() {
        val tealRadioButton = view?.findViewById<RadioButton>(R.id.tealAccent)
        val pinkRadioButton = view?.findViewById<RadioButton>(R.id.pinkAccent)
        val orangeRadioButton = view?.findViewById<RadioButton>(R.id.orangeAccent)
        val redRadioButton = view?.findViewById<RadioButton>(R.id.redAccent)
        val materialYouRadioButton = view?.findViewById<RadioButton>(R.id.materialYouAccent)

        FollowSystemVersion(requireContext()).setSystemColor(false)

        view?.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)?.isChecked = FollowSystemVersion(requireContext()).loadSystemColor()

        if (Build.VERSION.SDK_INT >= 31) {
            tealRadioButton?.isChecked = false
            pinkRadioButton?.isChecked = false
            orangeRadioButton?.isChecked = false
            redRadioButton?.isChecked = false
            materialYouRadioButton?.isChecked = true

            AccentColor(requireContext()).setAccentState(4)
        }
        else {
            tealRadioButton?.isChecked = true
            pinkRadioButton?.isChecked = false
            orangeRadioButton?.isChecked = false
            redRadioButton?.isChecked = false
            materialYouRadioButton?.isChecked = false

            AccentColor(requireContext()).setAccentState(0)
        }

        if (ChosenAppIconData(requireContext()).loadChosenAppIcon() == "auto") {
            if (Build.VERSION.SDK_INT >= 33) {
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
            else {
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
            restartApplication()
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

    fun updateCustomColorChange() {
        val customColorGenerator = CustomColorGenerator(requireContext())

        val tealRadioButton = requireActivity().findViewById<RadioButton>(R.id.tealAccent)
        val pinkRadioButton = requireActivity().findViewById<RadioButton>(R.id.pinkAccent)
        val orangeRadioButton = requireActivity().findViewById<RadioButton>(R.id.orangeAccent)
        val redRadioButton = requireActivity().findViewById<RadioButton>(R.id.redAccent)
        val customRadioButton = requireActivity().findViewById<RadioButton>(R.id.customAccent)
        val materialYouRadioButton = requireActivity().findViewById<RadioButton>(R.id.materialYouAccent)

        val statesRadio = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )

        val colorsRadio = intArrayOf(
            Color.parseColor("#000000"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        tealRadioButton.buttonTintList = ColorStateList(statesRadio, colorsRadio)
        pinkRadioButton.buttonTintList = ColorStateList(statesRadio, colorsRadio)
        orangeRadioButton.buttonTintList = ColorStateList(statesRadio, colorsRadio)
        redRadioButton.buttonTintList = ColorStateList(statesRadio, colorsRadio)
        customRadioButton.buttonTintList = ColorStateList(statesRadio, colorsRadio)
        materialYouRadioButton.buttonTintList = ColorStateList(statesRadio, colorsRadio)

        val tealCardView = requireActivity().findViewById<MaterialCardView>(R.id.tealCardViewAccentColor)
        val pinkCardView = requireActivity().findViewById<MaterialCardView>(R.id.pinkCardViewAccentColor)
        val orangeCardView = requireActivity().findViewById<MaterialCardView>(R.id.orangeCardViewAccentColor)
        val redCardView = requireActivity().findViewById<MaterialCardView>(R.id.redCardViewAccentColor)
        val customCardView = requireActivity().findViewById<MaterialCardView>(R.id.customCardViewAccentColor)
        val materialYouCardView = requireActivity().findViewById<MaterialCardView>(R.id.materialYouCardViewAccentColor)
        val materialYouSwitchCardView = requireActivity().findViewById<MaterialCardView>(R.id.followGoogleAppsCardView)
        val material2Switch = requireActivity().findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)

        tealCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        pinkCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        orangeCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        redCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        customCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        materialYouCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        materialYouSwitchCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAccentColorFragment)?.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAccentColorFragment)?.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )
        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(customColorGenerator.generateCustomColorPrimary())
        )

        material2Switch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(customColorGenerator.generateCustomColorPrimary()))
        material2Switch.trackTintList = ColorStateList(states, colors)

        val topAppBarAccent = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarAccentColorFragment)

        val resetDrawable = topAppBarAccent?.menu?.findItem(R.id.reset)?.icon
        resetDrawable?.mutate()

        val navigationDrawable = topAppBarAccent?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            if (AccentColor(requireContext()).loadAccent() == 5) {
                resetDrawable?.colorFilter = BlendModeColorFilter(
                    Color.parseColor(customColorGenerator.generateMenuTintColor()),
                    BlendMode.SRC_ATOP
                )
                navigationDrawable?.colorFilter = BlendModeColorFilter(
                    Color.parseColor(customColorGenerator.generateMenuTintColor()),
                    BlendMode.SRC_ATOP
                )
            }
            else {
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
                val id = typedValue.resourceId
                resetDrawable?.colorFilter = BlendModeColorFilter(
                    id,
                    BlendMode.SRC_ATOP
                )
                navigationDrawable?.colorFilter = BlendModeColorFilter(
                    id,
                    BlendMode.SRC_ATOP
                )
            }
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            resetDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

        val runnable = Runnable {
            (context as MainActivity).updateBottomNavCustomColor()
        }
        MainActivity().runOnUiThread(runnable)

        if (ColoredNavBarData(requireContext()).loadNavBar()) {
            activity?.window?.navigationBarColor =
                Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        }
    }
}