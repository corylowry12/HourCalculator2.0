package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import com.google.android.material.slider.Slider
import kotlin.random.Random

class AccentColorFragment : Fragment() {

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateCustomColorChange()

        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                //activity?.setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                //activity?.setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_accent_color, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        val topAppBarAccent = view.findViewById<MaterialToolbar>(R.id.materialToolBarAccentColorFragment)

       // val resetDrawable = topAppBarAccent?.menu?.findItem(R.id.reset)?.icon
       // resetDrawable?.mutate()

        //val navigationDrawable = topAppBarAccent?.navigationIcon
       // navigationDrawable?.mutate()

        /*if (MenuTintData(requireContext()).loadMenuTint()) {
            resetDrawable?.colorFilter = BlendModeColorFilter(Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()), BlendMode.SRC_ATOP)
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            resetDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }*/

        topAppBarAccent?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

            updateCustomColorChange()

        topAppBarAccent.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.reset -> {
                    Vibrate().vibration(requireContext())
                    val dialog = BottomSheetDialog(requireContext())
                    val resetSettingsLayout = layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
                    val yesResetButton = resetSettingsLayout.findViewById<Button>(R.id.yesResetButton)
                    val cancelResetButton = resetSettingsLayout.findViewById<Button>(R.id.cancelResetButton)
                    resetSettingsLayout.findViewById<MaterialCardView>(R.id.bodyCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                    yesResetButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                    cancelResetButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

                    dialog.setContentView(resetSettingsLayout)
                    dialog.setCancelable(false)

                    if (resources.getBoolean(R.bool.isTablet)) {
                        val bottomSheet =
                            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.isDraggable = false
                    }
                    resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text = getString(R.string.would_you_like_to_reset_accent_color_settings)

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

        val customCardView = view.findViewById<MaterialCardView>(R.id.customCardViewAccentColor)
        val addColorCardView = requireActivity().findViewById<MaterialCardView>(R.id.addColorCardView)
        val viewSavedColorsCardView = requireActivity().findViewById<MaterialCardView>(R.id.viewSavedColorsCardView)
        val bottomNavBadgeColorCardView = view.findViewById<MaterialCardView>(R.id.bottomNavBadgeSettingCardView)
        val generateARandomColorCardView = view.findViewById<MaterialCardView>(R.id.generateARandomColorOnAppLaunchCardView)
        val followGoogleAppsCardView = view.findViewById<MaterialCardView>(R.id.followGoogleAppsCardView)

        customCardView.shapeAppearanceModel = customCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        addColorCardView.shapeAppearanceModel = addColorCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        viewSavedColorsCardView.shapeAppearanceModel = viewSavedColorsCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        bottomNavBadgeColorCardView.shapeAppearanceModel = bottomNavBadgeColorCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()

        if (Build.VERSION.SDK_INT < 31) {
            followGoogleAppsCardView?.visibility = View.GONE
            generateARandomColorCardView.shapeAppearanceModel = generateARandomColorCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(28f)
                .setBottomLeftCornerSize(28f)
                .build()
        }
        else {
            generateARandomColorCardView.shapeAppearanceModel = generateARandomColorCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
            followGoogleAppsCardView.shapeAppearanceModel = followGoogleAppsCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(28f)
                .setBottomLeftCornerSize(28f)
                .build()
        }

        val bottomNavBadgeColorSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.bottomNavBadgeSettingSwitch)

        if (BottomNavBadgeColorData(requireContext()).loadColor()) {
            bottomNavBadgeColorSwitch.isChecked = true
            bottomNavBadgeColorSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            bottomNavBadgeColorSwitch?.jumpDrawablesToCurrentState()
        }
        else {
            bottomNavBadgeColorSwitch.isChecked = false
            bottomNavBadgeColorSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            bottomNavBadgeColorSwitch?.jumpDrawablesToCurrentState()
        }

        bottomNavBadgeColorCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            bottomNavBadgeColorSwitch.isChecked = !bottomNavBadgeColorSwitch.isChecked
            BottomNavBadgeColorData(requireContext()).setColor(bottomNavBadgeColorSwitch.isChecked)
            if (bottomNavBadgeColorSwitch.isChecked) {
                bottomNavBadgeColorSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            }
            else {
                bottomNavBadgeColorSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            val updateBottomNavColor = Runnable {
                (context as MainActivity).updateBottomNavCustomColor()
            }
            MainActivity().runOnUiThread(updateBottomNavColor)
        }

        bottomNavBadgeColorCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)

            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    infoDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }

            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the badges in the bottom navigation view that show history/time card count will match the theme\n\n" +
                        "When disabled the badges in the bottom navigation view that show history/time card count will be red"
            val yesButton = infoAboutSettingLayout.findViewById<Button>(R.id.yesButton)

            infoAboutSettingLayout.findViewById<MaterialCardView>(R.id.bodyCardView)
                .setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

            yesButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                infoDialog.dismiss()
            }
            infoDialog.show()
            return@setOnLongClickListener true
        }

        val generateARandomColorOnAppLaunchSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.generateARandomColorOnAppLaunchSwitch)

        if (GenerateARandomColorData(requireContext()).loadGenerateARandomColorOnAppLaunch()) {
            generateARandomColorOnAppLaunchSwitch.isChecked = true
            generateARandomColorOnAppLaunchSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            generateARandomColorCardView?.jumpDrawablesToCurrentState()
        }
        else {
            generateARandomColorOnAppLaunchSwitch.isChecked = false
            generateARandomColorOnAppLaunchSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            generateARandomColorOnAppLaunchSwitch?.jumpDrawablesToCurrentState()
        }

        generateARandomColorCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            generateARandomColorOnAppLaunchSwitch.isChecked = !generateARandomColorOnAppLaunchSwitch.isChecked
            GenerateARandomColorData(requireContext()).setGenerateARandomColorOnAppLaunch(generateARandomColorOnAppLaunchSwitch.isChecked)
            if (generateARandomColorOnAppLaunchSwitch.isChecked) {
                generateARandomColorOnAppLaunchSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            }
            else {
                generateARandomColorOnAppLaunchSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }

            updateCustomColorChange()
        }

        generateARandomColorCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)

            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    infoDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }

            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the app will generate and set a random color on app launch\n\n" +
                        "When disabled the app will be set to whatever theme you have chosen"
            val yesButton = infoAboutSettingLayout.findViewById<Button>(R.id.yesButton)

            infoAboutSettingLayout.findViewById<MaterialCardView>(R.id.bodyCardView)
                .setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

            yesButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                infoDialog.dismiss()
            }
            infoDialog.show()
            return@setOnLongClickListener true
        }

        if (GenerateARandomColorMethodData(requireContext()).loadGenerateARandomColorMethod() == 0) {
            requireActivity().findViewById<TextView>(R.id.generateRandomColorTitle).text =
                getString(R.string.generate_a_random_color_on_app_launch)
        }
        else if (GenerateARandomColorMethodData(requireContext()).loadGenerateARandomColorMethod() == 1) {
            requireActivity().findViewById<TextView>(R.id.generateRandomColorTitle).text =
                getString(R.string.pick_a_random_color_on_app_launch_from_saved_colors)
        }
        val generateARandomColorOptionsCardView = requireActivity().findViewById<MaterialCardView>(R.id.imageViewRandomColorOptionsCardView)

        generateARandomColorOptionsCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            val optionsDialog = BottomSheetDialog(requireContext())
            val optionsLayout =
                layoutInflater.inflate(R.layout.generate_a_random_color_options_bottom_sheet, null)
            optionsDialog.setContentView(optionsLayout)
            optionsDialog.setCancelable(true)

            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    optionsDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }

            val randomColorCardView = optionsLayout.findViewById<MaterialCardView>(R.id.randomColorCardView)
            val randomColorFromSavedCardView = optionsLayout.findViewById<MaterialCardView>(R.id.fromSavedColorsCardView)
            val randomColorRadioButton = optionsLayout.findViewById<RadioButton>(R.id.randomColor)
            val fromSavedColorsRadioButton = optionsLayout.findViewById<RadioButton>(R.id.fromSavedColors)

            if (GenerateARandomColorMethodData(requireContext()).loadGenerateARandomColorMethod() == 0) {
                randomColorRadioButton.isChecked = true
            }
            else if (GenerateARandomColorMethodData(requireContext()).loadGenerateARandomColorMethod() == 1) {
                fromSavedColorsRadioButton.isChecked = true
            }

            randomColorCardView.shapeAppearanceModel =
                randomColorCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            randomColorFromSavedCardView.shapeAppearanceModel =
                randomColorFromSavedCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(28f)
                    .setBottomLeftCornerSize(28f)
                    .build()

            randomColorCardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            randomColorFromSavedCardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            val states = arrayOf(
                intArrayOf(-android.R.attr.state_checked), // unchecked
                intArrayOf(android.R.attr.state_checked)  // checked
            )

            val colors = intArrayOf(
                Color.parseColor("#000000"),
                Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
            )

            randomColorRadioButton.buttonTintList = ColorStateList(states, colors)
            randomColorRadioButton.buttonTintList = ColorStateList(states, colors)
            fromSavedColorsRadioButton.buttonTintList = ColorStateList(states, colors)
            fromSavedColorsRadioButton.buttonTintList = ColorStateList(states, colors)

            randomColorCardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                optionsDialog.dismiss()
                GenerateARandomColorMethodData(requireContext()).setGenerateARandomColorMethod(0)
                updateCustomColorChange()
                requireActivity().findViewById<TextView>(R.id.generateRandomColorTitle).text =
                    getString(R.string.generate_a_random_color_on_app_launch)
            }
            randomColorFromSavedCardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                optionsDialog.dismiss()
                if (UserAddedColorsData(requireContext()).read().count() >= 2) {
                    GenerateARandomColorMethodData(requireContext()).setGenerateARandomColorMethod(1)
                    val contains = UserAddedColorsData(requireContext()).read().filter { it.containsValue(CustomColorGenerator(requireContext()).loadRandomHex().drop(1)) }

                    if (CustomColorGenerator(requireContext()).loadRandomHex() == "" || contains.isEmpty()) {
                        CustomColorGenerator(requireContext()).generateARandomColor()
                    }
                    updateCustomColorChange()
                    requireActivity().findViewById<TextView>(R.id.generateRandomColorTitle).text =
                        getString(R.string.pick_a_random_color_on_app_launch_from_saved_colors)
                }
                else {
                    Toast.makeText(requireContext(), getString(R.string.must_have_more_than_two_colors_saved_warning), Toast.LENGTH_SHORT).show()
                }
            }

            optionsDialog.show()
        }

        val materialColorOptionsCardView = requireActivity().findViewById<MaterialCardView>(R.id.imageViewMaterialColorOptionsCardView)

        materialColorOptionsCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            val optionsDialog = BottomSheetDialog(requireContext())
            val optionsLayout =
                layoutInflater.inflate(R.layout.material_color_options_bottom_sheet, null)
            optionsDialog.setContentView(optionsLayout)
            optionsDialog.setCancelable(true)

            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    optionsDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }

            val accent1CardView = optionsLayout.findViewById<MaterialCardView>(R.id.accent1CardView)
            val accent1RadioButton = optionsLayout.findViewById<RadioButton>(R.id.accent1)
            val accent2CardView = optionsLayout.findViewById<MaterialCardView>(R.id.accent2CardView)
            val accent2RadioButton = optionsLayout.findViewById<RadioButton>(R.id.accent2)
            val accent3CardView = optionsLayout.findViewById<MaterialCardView>(R.id.accent3CardView)
            val accent3RadioButton = optionsLayout.findViewById<RadioButton>(R.id.accent3)
            val neutral1CardView = optionsLayout.findViewById<MaterialCardView>(R.id.neutral1CardView)
            val neutral1RadioButton = optionsLayout.findViewById<RadioButton>(R.id.neutral1)
            val neutral2CardView = optionsLayout.findViewById<MaterialCardView>(R.id.neutral2CardView)
            val neutral2RadioButton = optionsLayout.findViewById<RadioButton>(R.id.neutral2)
            val randomMaterialYouCardView = optionsLayout.findViewById<MaterialCardView>(R.id.randomMaterialYouCardView)
            val randomMaterialYouRadioButton = optionsLayout.findViewById<RadioButton>(R.id.randomMaterialYou)

            if (MaterialYouOptionData(requireContext()).loadMaterialOption() == 1) {
                accent1RadioButton.isChecked = true
            }
            else if (MaterialYouOptionData(requireContext()).loadMaterialOption() == 2) {
                accent2RadioButton.isChecked = true
            }
            else if (MaterialYouOptionData(requireContext()).loadMaterialOption() == 3) {
                accent3RadioButton.isChecked = true
            }
            else if (MaterialYouOptionData(requireContext()).loadMaterialOption() == 4) {
                neutral1RadioButton.isChecked = true
            }
            else if (MaterialYouOptionData(requireContext()).loadMaterialOption() == 5) {
                neutral2RadioButton.isChecked = true
            }
            else if (MaterialYouOptionData(requireContext()).loadMaterialOption() == 6) {
                randomMaterialYouRadioButton.isChecked = true
            }

            accent1CardView.shapeAppearanceModel =
                accent1CardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 28f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            accent2CardView.shapeAppearanceModel =
                accent2CardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            accent3CardView.shapeAppearanceModel =
                accent3CardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            neutral1CardView.shapeAppearanceModel =
                neutral1CardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            neutral2CardView.shapeAppearanceModel =
                neutral2CardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(0f)
                    .setBottomLeftCornerSize(0f)
                    .build()
            randomMaterialYouCardView.shapeAppearanceModel =
                randomMaterialYouCardView.shapeAppearanceModel
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCornerSize(28f)
                    .setBottomLeftCornerSize(28f)
                    .build()

            accent1CardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            accent2CardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            accent3CardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            neutral1CardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            neutral2CardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )
            randomMaterialYouCardView.setCardBackgroundColor(
                ColorStateList.valueOf(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor())
                )
            )

            optionsLayout.findViewById<MaterialCardView>(R.id.randomMaterialYouColorCardView).setCardBackgroundColor(Color.parseColor(GenerateMaterialYouColors(requireContext()).generate_random_500()))

            val states = arrayOf(
                intArrayOf(-android.R.attr.state_checked), // unchecked
                intArrayOf(android.R.attr.state_checked)  // checked
            )

            val colors = intArrayOf(
                Color.parseColor("#000000"),
                Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
            )

            accent1RadioButton.buttonTintList = ColorStateList(states, colors)
            accent2RadioButton.buttonTintList = ColorStateList(states, colors)
            accent3RadioButton.buttonTintList = ColorStateList(states, colors)
            neutral1RadioButton.buttonTintList = ColorStateList(states, colors)
            neutral2RadioButton.buttonTintList = ColorStateList(states, colors)
            randomMaterialYouRadioButton.buttonTintList = ColorStateList(states, colors)

            accent1CardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                accent1RadioButton.isChecked = true
                accent2RadioButton.isChecked = false
                accent3RadioButton.isChecked = false
                neutral1RadioButton.isChecked = false
                neutral2RadioButton.isChecked = false
                randomMaterialYouRadioButton.isChecked = false
                MaterialYouOptionData(requireContext()).setMaterialOption(1)
                updateCustomColorChange()
                optionsDialog.dismiss()
            }
            accent2CardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                accent1RadioButton.isChecked = false
                accent2RadioButton.isChecked = true
                accent3RadioButton.isChecked = false
                neutral1RadioButton.isChecked = false
                neutral2RadioButton.isChecked = false
                randomMaterialYouRadioButton.isChecked = false
                MaterialYouOptionData(requireContext()).setMaterialOption(2)
                updateCustomColorChange()
                optionsDialog.dismiss()
            }
            accent3CardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                accent1RadioButton.isChecked = false
                accent2RadioButton.isChecked = false
                accent3RadioButton.isChecked = true
                neutral1RadioButton.isChecked = false
                neutral2RadioButton.isChecked = false
                randomMaterialYouRadioButton.isChecked = false
                MaterialYouOptionData(requireContext()).setMaterialOption(3)
                updateCustomColorChange()
                optionsDialog.dismiss()
            }
            neutral1CardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                accent1RadioButton.isChecked = false
                accent2RadioButton.isChecked = false
                accent3RadioButton.isChecked = false
                neutral1RadioButton.isChecked = true
                neutral2RadioButton.isChecked = false
                randomMaterialYouRadioButton.isChecked = false
                MaterialYouOptionData(requireContext()).setMaterialOption(4)
                updateCustomColorChange()
                optionsDialog.dismiss()
            }
            neutral2CardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                accent1RadioButton.isChecked = false
                accent2RadioButton.isChecked = false
                accent3RadioButton.isChecked = false
                neutral1RadioButton.isChecked = false
                neutral2RadioButton.isChecked = true
                randomMaterialYouRadioButton.isChecked = false
                MaterialYouOptionData(requireContext()).setMaterialOption(5)
                updateCustomColorChange()
                optionsDialog.dismiss()
            }
            randomMaterialYouCardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                accent1RadioButton.isChecked = false
                accent2RadioButton.isChecked = false
                accent3RadioButton.isChecked = false
                neutral1RadioButton.isChecked = false
                neutral2RadioButton.isChecked = false
                randomMaterialYouRadioButton.isChecked = true
                MaterialYouOptionData(requireContext()).setMaterialOption(6)
                updateCustomColorChange()
                optionsDialog.dismiss()
            }

            optionsDialog.show()
        }

        customCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            val customColorPickerDialog = BottomSheetDialog(requireContext())
            val customColorPickerLayout = layoutInflater.inflate(R.layout.custom_color_bottom_sheet, null)
            customColorPickerDialog.setContentView(customColorPickerLayout)
            customColorPickerDialog.setCancelable(false)

            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    customColorPickerDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }

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
            val generateRandomColorButton = customColorPickerLayout.findViewById<Button>(R.id.generateRandomColorButton)

            selectButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
            cancelSelectButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
            generateRandomColorButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

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

            generateRandomColorButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                val red = Random.nextInt(50,200)
                val green = Random.nextInt(50,200)
                val blue = Random.nextInt(50,200)
                hex = String.format("#%02X%02X%02X", red, green, blue).drop(1)
                hexadecimalTextView.text = hex
                coloredCardView.setCardBackgroundColor(Color.parseColor("#$hex"))
                redSlider.value = red.toFloat()
                greenSlider.value = green.toFloat()
                blueSlider.value = blue.toFloat()
            }

            redSlider.addOnChangeListener { slider, _, _ ->
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
            greenSlider.addOnChangeListener { slider, _, _ ->
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
            blueSlider.addOnChangeListener { slider, _, _ ->
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
                MaterialYouData(requireContext()).setMaterialYouState(false)
                GenerateARandomColorData(requireContext()).setGenerateARandomColorOnAppLaunch(false)
                activity?.findViewById<MaterialSwitch>(R.id.generateARandomColorOnAppLaunchSwitch)?.isChecked = false
                activity?.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)?.isChecked = false
                activity?.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                activity?.findViewById<MaterialSwitch>(R.id.generateARandomColorOnAppLaunchSwitch)?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                updateCustomColorChange()
                requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text = "#$hex"
            }

            selectButton.setOnLongClickListener {
                Vibrate().vibrateOnLongClick(requireContext())
                customColorPickerDialog.dismiss()
                CustomColorGenerator(requireContext()).setCustomHex("#$hex")
                MaterialYouData(requireContext()).setMaterialYouState(false)
                GenerateARandomColorData(requireContext()).setGenerateARandomColorOnAppLaunch(false)
                activity?.findViewById<MaterialSwitch>(R.id.generateARandomColorOnAppLaunchSwitch)?.isChecked = false
                activity?.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)?.isChecked = false
                activity?.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                activity?.findViewById<MaterialSwitch>(R.id.generateARandomColorOnAppLaunchSwitch)?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                updateCustomColorChange()
                requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text = "#$hex"

                val addedColors = UserAddedColorsData(requireContext()).read()
                val dataList = ArrayList<HashMap<String, String>>()

                for (i in 0 until UserAddedColorsData(requireContext()).read().count()) {
                    val allColors = HashMap<String, String>()
                    try {
                        allColors["name"] = addedColors[i]["name"].toString()
                    } catch (e: java.lang.Exception) {
                        allColors["name"] = ""
                    }
                    allColors["hex"] = addedColors[i]["hex"].toString()
                    dataList.add(allColors)
                }
                val contains = dataList.filter { it.containsValue(CustomColorGenerator(requireContext()).loadCustomHex().drop(1)) }

                if (contains.isEmpty()) {
                    val newColor = HashMap<String, String>()
                    newColor["name"] = ""
                    newColor["hex"] =
                        requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text.drop(
                            1
                        ).toString()
                    dataList.add(newColor)
                    UserAddedColorsData(requireContext()).insert(dataList)
                }

                if (contains.isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.color_saved), Toast.LENGTH_SHORT).show()
                    requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text =
                        CustomColorGenerator(requireContext()).loadCustomHex() + " (Saved)"
                }
                else {
                    Toast.makeText(requireContext(), getString(R.string.color_already_saved), Toast.LENGTH_SHORT).show()
                    if (contains[0]["name"] == "") {
                        requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text =
                            CustomColorGenerator(requireContext()).loadCustomHex() + " (Saved)"
                    }
                    else {
                        requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text =
                            CustomColorGenerator(requireContext()).loadCustomHex() + " (${contains[0]["name"]})"
                    }
                }
                requireActivity().findViewById<TextView>(R.id.viewSavedColorsCountChip).text = UserAddedColorsData(requireContext()).read().count().toString()
                return@setOnLongClickListener true
            }
            cancelSelectButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                customColorPickerDialog.dismiss()
            }

            customColorPickerDialog.show()
        }

        requireActivity().findViewById<TextView>(R.id.viewSavedColorsCountChip).text = UserAddedColorsData(requireContext()).read().count().toString()

        addColorCardView.setOnClickListener {
            Vibrate().vibration(requireContext())

            val addedColors = UserAddedColorsData(requireContext()).read()
            val dataList = ArrayList<HashMap<String, String>>()

            for (i in 0 until UserAddedColorsData(requireContext()).read().count()) {
                val allColors = HashMap<String, String>()
                try {
                    allColors["name"] = addedColors[i]["name"].toString()
                } catch (e: java.lang.Exception) {
                    allColors["name"] = ""
                }
                allColors["hex"] = addedColors[i]["hex"].toString()
                dataList.add(allColors)
            }
            val contains = dataList.filter { it.containsValue(CustomColorGenerator(requireContext()).loadCustomHex().drop(1)) }

            if (contains.isEmpty()) {
                val newColor = HashMap<String, String>()
                newColor["name"] = ""
                newColor["hex"] =
                    requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text.drop(
                        1
                    ).toString()
                dataList.add(newColor)
                UserAddedColorsData(requireContext()).insert(dataList)
            }

            if (contains.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.color_saved), Toast.LENGTH_SHORT).show()
                requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text =
                    CustomColorGenerator(requireContext()).loadCustomHex() + " (Saved)"
            }
            else {
                Toast.makeText(requireContext(), getString(R.string.color_already_saved), Toast.LENGTH_SHORT).show()
                if (contains[0]["name"] == "") {
                    requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text =
                        CustomColorGenerator(requireContext()).loadCustomHex() + " (Saved)"
                }
                else {
                    requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text =
                        CustomColorGenerator(requireContext()).loadCustomHex() + " (${contains[0]["name"]})"
                }
            }
            requireActivity().findViewById<TextView>(R.id.viewSavedColorsCountChip).text = UserAddedColorsData(requireContext()).read().count().toString()

        }

        viewSavedColorsCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (UserAddedColorsData(requireContext()).read().isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.must_save_a_color_first), Toast.LENGTH_SHORT).show()
            }
            else {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                if (AnimationData(requireContext()).loadSettingsFragmentSwitchingAnimation()) {
                    transaction?.setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                }
                transaction?.replace(R.id.fragment_container, SavedColorsFragment())
                    ?.addToBackStack(null)
                transaction?.commit()
            }
        }

        val materialYouStyle2Switch = view.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)

        if (MaterialYouData(requireContext()).loadMaterialYou()) {
            materialYouStyle2Switch?.isChecked = true
            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!MaterialYouData(requireContext()).loadMaterialYou()) {
            materialYouStyle2Switch?.isChecked = false
            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        followGoogleAppsCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            materialYouStyle2Switch.isChecked = !materialYouStyle2Switch.isChecked
            MaterialYouData(requireContext()).setMaterialYouState(materialYouStyle2Switch.isChecked)
            updateCustomColorChange()
            if (MaterialYouData(requireContext()).loadMaterialYou()) {
                materialYouStyle2Switch?.isChecked = true
                materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                materialYouStyle2Switch?.jumpDrawablesToCurrentState()
            } else if (!MaterialYouData(requireContext()).loadMaterialYou()) {
                materialYouStyle2Switch?.isChecked = false
                materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                materialYouStyle2Switch?.jumpDrawablesToCurrentState()
            }
        }

        followGoogleAppsCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)

            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    infoDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }

            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the app will generate a color palette that matches your wallpaper\n\n" +
                        "When disabled the app will be set to whatever color you have chosen"
            val yesButton = infoAboutSettingLayout.findViewById<Button>(R.id.yesButton)

            infoAboutSettingLayout.findViewById<MaterialCardView>(R.id.bodyCardView)
                .setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

            yesButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                infoDialog.dismiss()
            }
            infoDialog.show()
            return@setOnLongClickListener true
        }
    }

    private fun reset() {

        if (Build.VERSION.SDK_INT >= 31) {
            MaterialYouData(requireContext()).setMaterialYouState(true)
            view?.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)?.isChecked = true
            view?.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else {
            MaterialYouData(requireContext()).setMaterialYouState(false)
            view?.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)?.isChecked = false
            view?.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        BottomNavBadgeColorData(requireContext()).setColor(true)
        view?.findViewById<MaterialSwitch>(R.id.bottomNavBadgeSettingSwitch)?.isChecked = true
        view?.findViewById<MaterialSwitch>(R.id.bottomNavBadgeSettingSwitch)?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)

        GenerateARandomColorData(requireContext()).setGenerateARandomColorOnAppLaunch(false)
        GenerateARandomColorMethodData(requireContext()).setGenerateARandomColorMethod(0)
        view?.findViewById<MaterialSwitch>(R.id.generateARandomColorOnAppLaunchSwitch)?.isChecked = false
        view?.findViewById<MaterialSwitch>(R.id.generateARandomColorOnAppLaunchSwitch)?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)

        requireActivity().findViewById<TextView>(R.id.generateRandomColorTitle).text =
            getString(R.string.generate_a_random_color_on_app_launch)

        CustomColorGenerator(requireContext()).setCustomHex("#53c8c8")

        UserAddedColorsData(requireContext()).clear()
        UserAddedColorsData(requireContext()).clearHash()
        requireActivity().findViewById<TextView>(R.id.viewSavedColorsCountChip).text = UserAddedColorsData(requireContext()).read().count().toString()
        MaterialYouOptionData(requireContext()).setMaterialOption(2)
        updateCustomColorChange()
    }

    private fun updateCustomColorChange() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.accentColorCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val customColorGenerator = CustomColorGenerator(requireContext())

        val customCardView = requireActivity().findViewById<MaterialCardView>(R.id.customCardViewAccentColor)
        val customColorCardView = requireActivity().findViewById<MaterialCardView>(R.id.customThemeColorThemeCardView)
        val addColorCardView = requireActivity().findViewById<MaterialCardView>(R.id.addColorCardView)
        val viewSavedColorsCardView = requireActivity().findViewById<MaterialCardView>(R.id.viewSavedColorsCardView)
        val bottomNavBadgeColorCardView = requireActivity().findViewById<MaterialCardView>(R.id.bottomNavBadgeSettingCardView)
        val generateARandomColorCardView = requireActivity().findViewById<MaterialCardView>(R.id.generateARandomColorOnAppLaunchCardView)
        val materialYouSwitchCardView = requireActivity().findViewById<MaterialCardView>(R.id.followGoogleAppsCardView)
        val bottomNavBadgeColorSettingSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.bottomNavBadgeSettingSwitch)
        val generateARandomColorSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.generateARandomColorOnAppLaunchSwitch)
        val material2Switch = requireActivity().findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)

        val color = Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor())

        val generateRandomColorOptionsCardView = requireActivity().findViewById<MaterialCardView>(R.id.imageViewRandomColorOptionsCardView)
        val generateARandomColorOptionsIcon = requireActivity().findViewById<ImageButton>(R.id.imageViewRandomColorOptions)
        generateRandomColorOptionsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        generateARandomColorOptionsIcon.setColorFilter(color)

        val materialColorOptionsCardView = requireActivity().findViewById<MaterialCardView>(R.id.imageViewMaterialColorOptionsCardView)
        val materialColorOptionsIcon = requireActivity().findViewById<ImageButton>(R.id.imageViewMaterialColorOptions)
        materialColorOptionsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        materialColorOptionsIcon.setColorFilter(color)

        customCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        customColorCardView.setCardBackgroundColor(Color.parseColor(customColorGenerator.loadCustomHex()))
        addColorCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        viewSavedColorsCardView.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        bottomNavBadgeColorCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        generateARandomColorCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        materialYouSwitchCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAccentColorFragment)?.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAccentColorFragment)?.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        val addedColors = UserAddedColorsData(requireContext()).read()

        val contains = addedColors.filter { it.containsValue(CustomColorGenerator(requireContext()).loadCustomHex().drop(1)) }

        if (contains.isEmpty()) {
            requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text =
                CustomColorGenerator(requireContext()).loadCustomHex()
        }
        else {
            if (contains[0]["name"].toString() == "") {
                requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text =
                    CustomColorGenerator(requireContext()).loadCustomHex() + " (Saved)"
            }
            else {
                requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text =
                    CustomColorGenerator(requireContext()).loadCustomHex() + " (${contains[0]["name"]?.trim()})"
            }
        }

        requireActivity().findViewById<Chip>(R.id.viewSavedColorsCountChip)
            .setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBottomNavTextColor()))
        requireActivity().findViewById<Chip>(R.id.viewSavedColorsCountChip).closeIconTint =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateBottomNavTextColor()))
        requireActivity().findViewById<Chip>(R.id.viewSavedColorsCountChip).chipBackgroundColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateChipBackgroundColor()))

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )
        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(customColorGenerator.generateCustomColorPrimary())
        )

        bottomNavBadgeColorSettingSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(customColorGenerator.generateCustomColorPrimary()))
        bottomNavBadgeColorSettingSwitch.trackTintList = ColorStateList(states, colors)
        generateARandomColorSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(customColorGenerator.generateCustomColorPrimary()))
        generateARandomColorSwitch.trackTintList = ColorStateList(states, colors)
        material2Switch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(customColorGenerator.generateCustomColorPrimary()))
        material2Switch.trackTintList = ColorStateList(states, colors)

        val runnable = Runnable {
            (context as MainActivity).updateCustomColor()
        }
        MainActivity().runOnUiThread(runnable)

        if (ColoredNavBarData(requireContext()).loadNavBar()) {
            activity?.window?.navigationBarColor =
                Color.parseColor(CustomColorGenerator(requireContext()).generateNavBarColor())
        }
        else {
            activity?.window?.navigationBarColor =
                Color.parseColor("#000000")
        }

        val topAppBarAccent = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarAccentColorFragment)

        val resetDrawable = topAppBarAccent?.menu?.findItem(R.id.reset)?.icon
        resetDrawable?.mutate()

        val navigationDrawable = topAppBarAccent?.navigationIcon
        navigationDrawable?.mutate()

        if (Build.VERSION.SDK_INT >= 29) {
            try {
                if (MenuTintData(requireContext()).loadMenuTint()) {

                    resetDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
                } else {
                    val typedValue = TypedValue()
                    activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                    val id = typedValue.resourceId
                    resetDrawable?.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), id),
                        BlendMode.SRC_ATOP
                    )
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), id),
                        BlendMode.SRC_ATOP
                    )
                }
            } catch (e: NoClassDefFoundError) {
                e.printStackTrace()
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                val id = typedValue.resourceId
                navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
                resetDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            }
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            resetDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
        }

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAccentColorFragment)?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAccentColorFragment)?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAccentColorFragment)?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }
    }

    override fun onResume() {
        super.onResume()
        val generateARandomColorOnAppLaunchSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.generateARandomColorOnAppLaunchSwitch)

        if (GenerateARandomColorData(requireContext()).loadGenerateARandomColorOnAppLaunch()) {
            generateARandomColorOnAppLaunchSwitch.isChecked = true
            generateARandomColorOnAppLaunchSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        }
        else {
            generateARandomColorOnAppLaunchSwitch.isChecked = false
            generateARandomColorOnAppLaunchSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        val materialYouStyle2Switch = requireActivity().findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)

        if (MaterialYouData(requireContext()).loadMaterialYou()) {
            materialYouStyle2Switch?.isChecked = true
            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!MaterialYouData(requireContext()).loadMaterialYou()) {
            materialYouStyle2Switch?.isChecked = false
            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }
    }
}