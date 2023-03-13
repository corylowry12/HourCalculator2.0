package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.intents.MainActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import com.google.android.material.slider.Slider


class AccentColorFragment : Fragment() {

    var themeSelection = false
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
            resetDrawable?.colorFilter = BlendModeColorFilter(Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()), BlendMode.SRC_ATOP)
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

        val customCardView = view.findViewById<MaterialCardView>(R.id.customCardViewAccentColor)
        val generateARandomColorCardView = view.findViewById<MaterialCardView>(R.id.generateARandomColorOnAppLaunchCardView)
        val followGoogleAppsCardView = view.findViewById<MaterialCardView>(R.id.followGoogleAppsCardView)

        customCardView.shapeAppearanceModel = customCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
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

        val generateARandomColorOnAppLaunchSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.generateARandomColorOnAppLaunchSwitch)

        if (GenerateARandomColorData(requireContext()).loadGenerateARandomColorOnAppLaunch()) {
            generateARandomColorOnAppLaunchSwitch.isChecked = true
            generateARandomColorOnAppLaunchSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        }
        else {
            generateARandomColorOnAppLaunchSwitch.isChecked = false
            generateARandomColorOnAppLaunchSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        generateARandomColorOnAppLaunchSwitch.setOnClickListener {
            Vibrate().vibration(requireContext())
            GenerateARandomColorData(requireContext()).setGenerateARandomColorOnAppLaunch(generateARandomColorOnAppLaunchSwitch.isChecked)
            if (generateARandomColorOnAppLaunchSwitch.isChecked) {
                generateARandomColorOnAppLaunchSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            }
            else {
                generateARandomColorOnAppLaunchSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text = CustomColorGenerator(requireContext()).loadCustomHex()
        customCardView.setOnClickListener {
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
            val generateRandomButton = customColorPickerLayout.findViewById<ImageButton>(R.id.generateRandomButton)
            val generateRandomButtonCardView = customColorPickerLayout.findViewById<MaterialCardView>(R.id.generateRandomButtonCardView)

            selectButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
            cancelSelectButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
            generateRandomButtonCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

            val color = Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()) //The color u want
            generateRandomButton.setColorFilter(color)

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

            generateRandomButtonCardView.setOnClickListener {
                Vibrate().vibration(requireContext())
                val red = (50..200).random()
                val green = (50..200).random()
                val blue = (50..200).random()
                hex = String.format("#%02X%02X%02X", red, green, blue).drop(1)
                hexadecimalTextView.text = hex
                coloredCardView.setCardBackgroundColor(Color.parseColor("#$hex"))
                redSlider.value = red.toFloat()
                greenSlider.value = green.toFloat()
                blueSlider.value = blue.toFloat()
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
                updateCustomColorChange()
                requireActivity().findViewById<TextView>(R.id.customTextViewSubtitle).text = "#$hex"
            }
            cancelSelectButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                customColorPickerDialog.dismiss()
            }

            customColorPickerDialog.show()
        }

        val materialYouStyle2Switch = view.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)

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

        if (MaterialYouEnabled(requireContext()).loadMaterialYou()) {
            materialYouStyle2Switch?.isChecked = true
            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!MaterialYouEnabled(requireContext()).loadMaterialYou()) {
            materialYouStyle2Switch?.isChecked = false
            materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        materialYouStyle2Switch.setOnCheckedChangeListener { buttonView, isChecked ->
            Vibrate().vibration(requireContext())
            MaterialYouEnabled(requireContext()).setMaterialYouState(materialYouStyle2Switch.isChecked)
            updateCustomColorChange()
            if (MaterialYouEnabled(requireContext()).loadMaterialYou()) {
                materialYouStyle2Switch?.isChecked = true
                materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else if (!MaterialYouEnabled(requireContext()).loadMaterialYou()) {
                materialYouStyle2Switch?.isChecked = false
                materialYouStyle2Switch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }
    }

    private fun reset() {

        if (Build.VERSION.SDK_INT >= 31) {
            MaterialYouEnabled(requireContext()).setMaterialYouState(true)
            view?.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)?.isChecked = true
        } else {
            MaterialYouEnabled(requireContext()).setMaterialYouState(false)
            view?.findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)?.isChecked = false
        }
    }

    private fun updateCustomColorChange() {
        val customColorGenerator = CustomColorGenerator(requireContext())

        val customCardView = requireActivity().findViewById<MaterialCardView>(R.id.customCardViewAccentColor)
        val generateARandomColorCardView = requireActivity().findViewById<MaterialCardView>(R.id.generateARandomColorOnAppLaunchCardView)
        val materialYouSwitchCardView = requireActivity().findViewById<MaterialCardView>(R.id.followGoogleAppsCardView)
        val generateARandomColorSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.generateARandomColorOnAppLaunchSwitch)
        val material2Switch = requireActivity().findViewById<MaterialSwitch>(R.id.followGoogleAppsSwitch)

        customCardView?.setCardBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
        generateARandomColorCardView?.setBackgroundColor(Color.parseColor(customColorGenerator.generateCardColor()))
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

        generateARandomColorSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(customColorGenerator.generateCustomColorPrimary()))
        generateARandomColorSwitch.trackTintList = ColorStateList(states, colors)
        material2Switch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(customColorGenerator.generateCustomColorPrimary()))
        material2Switch.trackTintList = ColorStateList(states, colors)

        val topAppBarAccent = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarAccentColorFragment)

        val resetDrawable = topAppBarAccent?.menu?.findItem(R.id.reset)?.icon
        resetDrawable?.mutate()

        val navigationDrawable = topAppBarAccent?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
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
                Color.parseColor(CustomColorGenerator(requireContext()).generateNavBarColor())
        }

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
}