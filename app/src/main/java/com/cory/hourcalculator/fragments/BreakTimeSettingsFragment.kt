package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.sharedprefs.BreakTextBoxVisibilityData
import com.cory.hourcalculator.sharedprefs.ClearBreakTextAutomaticallyData
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.sharedprefs.MenuTintData
import com.cory.hourcalculator.sharedprefs.ShowBreakTimeInDecimalData
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import java.lang.Exception

class BreakTimeSettingsFragment : Fragment() {

    private lateinit var dialog : BottomSheetDialog

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

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
                        activity?.setTheme(
                            R.style.Theme_AMOLED
                        )
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        updateCustomColor()

        try {
            if (dialog.isShowing) {
                dialog.dismiss()
                resetMenuPress()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
                        activity?.setTheme(
                            R.style.Theme_AMOLED
                        )
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_break_time_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarBreakTimeSettings)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.reset -> {
                    resetMenuPress()
                    true
                }

                else -> true
            }
        }

        updateCustomColor()

        val breakTextBoxCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewBreakTextBox)
        val clearBreakTextBoxCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewClearBreakTextBox)
        val showBreakTimeInDecimalCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewShowBreakInDecimal)

        breakTextBoxCardView.shapeAppearanceModel = breakTextBoxCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        clearBreakTextBoxCardView.shapeAppearanceModel =
            clearBreakTextBoxCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        showBreakTimeInDecimalCardView.shapeAppearanceModel =
            showBreakTimeInDecimalCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(28f)
                .setBottomLeftCornerSize(28f)
                .build()

        val breakTextBoxVisiblityClass = BreakTextBoxVisibilityData(requireContext())
        val toggleBreakTextBox = activity?.findViewById<MaterialSwitch>(R.id.breakTextBoxSwitch)

        if (breakTextBoxVisiblityClass.loadVisiblity() == 0) {
            toggleBreakTextBox?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            toggleBreakTextBox?.isChecked = true
        } else if (breakTextBoxVisiblityClass.loadVisiblity() == 1) {
            toggleBreakTextBox?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            toggleBreakTextBox?.isChecked = false
        }

        breakTextBoxCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleBreakTextBox!!.isChecked = !toggleBreakTextBox.isChecked
            if (toggleBreakTextBox.isChecked) {
                toggleBreakTextBox.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
                breakTextBoxVisiblityClass.setVisibility(0)
            } else {
                toggleBreakTextBox.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                breakTextBoxVisiblityClass.setVisibility(1)
            }
        }

        breakTextBoxCardView!!.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the break text box on the home screen will be shown.\n\n" +
                        "When disabled the break text box on the home screen will not be shown."
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

        val clearBreakTextAutomaticallyData = ClearBreakTextAutomaticallyData(requireContext())
        val clearBreakTextBoxSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.clearBreakTextBoxSwitch)

        if (clearBreakTextAutomaticallyData.loadClearAutomatically()) {
            clearBreakTextBoxSwitch.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            clearBreakTextBoxSwitch.isChecked = true
        } else if (!clearBreakTextAutomaticallyData.loadClearAutomatically()) {
            clearBreakTextBoxSwitch.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            clearBreakTextBoxSwitch.isChecked = false
        }

        clearBreakTextBoxCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            clearBreakTextBoxSwitch.isChecked = !clearBreakTextBoxSwitch.isChecked
            if (clearBreakTextBoxSwitch.isChecked) {
                clearBreakTextBoxSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                clearBreakTextBoxSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            clearBreakTextAutomaticallyData.setClearAutomatically(clearBreakTextBoxSwitch.isChecked)
        }

        clearBreakTextBoxCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the break time text box will automatically clear after calculation if there is not an error.\n\n" +
                        "When disabled you will have to manually clear the break text box each time."
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

        val showBreakTimeInDecimalData = ShowBreakTimeInDecimalData(requireContext())
        val showBreakTimeInDecimalSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.showBreakInDecimalSwitch)

        if (showBreakTimeInDecimalData.loadShowBreakTimeInDecimal()) {
            showBreakTimeInDecimalSwitch.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            showBreakTimeInDecimalSwitch.isChecked = true
        } else if (!showBreakTimeInDecimalData.loadShowBreakTimeInDecimal()) {
            showBreakTimeInDecimalSwitch.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            showBreakTimeInDecimalSwitch.isChecked = false
        }

        showBreakTimeInDecimalCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            showBreakTimeInDecimalSwitch.isChecked = !showBreakTimeInDecimalSwitch.isChecked
            if (showBreakTimeInDecimalSwitch.isChecked) {
                showBreakTimeInDecimalSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                showBreakTimeInDecimalSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            showBreakTimeInDecimalData.setShowBreakTimeInDecimal(showBreakTimeInDecimalSwitch.isChecked)
        }

        showBreakTimeInDecimalCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the break time will be shown in decimal format in History.\n\n" +
                        "When disabled the break time will be shown in minute format in History."
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

    private fun resetMenuPress() {

        dialog = BottomSheetDialog(requireContext())
        val resetSettingsLayout =
            layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
        dialog.setContentView(resetSettingsLayout)
        dialog.setCancelable(false)
        resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text =
            "Would you like to reset Break Time settings?"
        val yesResetButton =
            resetSettingsLayout.findViewById<Button>(R.id.yesResetButton)
        val cancelResetButton =
            resetSettingsLayout.findViewById<Button>(R.id.cancelResetButton)
        resetSettingsLayout.findViewById<MaterialCardView>(R.id.bodyCardView)
            .setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        yesResetButton.setBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCustomColorPrimary()
            )
        )
        cancelResetButton.setTextColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCustomColorPrimary()
            )
        )
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

    }

    private fun reset() {
        BreakTextBoxVisibilityData(requireContext()).setVisibility(0)
        ClearBreakTextAutomaticallyData(requireContext()).setClearAutomatically(true)
        ShowBreakTimeInDecimalData(requireContext()).setShowBreakTimeInDecimal(false)

        val breakTextBoxSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.breakTextBoxSwitch)
        val clearBreakTextBoxSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.clearBreakTextBoxSwitch)
        val showBreakTimeInDecimalSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.showBreakInDecimalSwitch)

        breakTextBoxSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        breakTextBoxSwitch?.isChecked = true
        clearBreakTextBoxSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        clearBreakTextBoxSwitch?.isChecked = true
        showBreakTimeInDecimalSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        showBreakTimeInDecimalSwitch?.isChecked = false
    }

    private fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.breakTimeSettingsCoordinatorLayout)
            .setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutBreakTimeSettings)
        collapsingToolbarLayout.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )

        val breakTextBoxCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewBreakTextBox)
        val clearBreakTextBoxCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewClearBreakTextBox)
        val showBreakTimeInDecimalCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewShowBreakInDecimal)

        breakTextBoxCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
        clearBreakTextBoxCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
        showBreakTimeInDecimalCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )

        val breakTextBoxSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.breakTextBoxSwitch)
        val clearBreakTextBoxSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.clearBreakTextBoxSwitch)
        val showBreakTimeInDecimalSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.showBreakInDecimalSwitch)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )
        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        breakTextBoxSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        breakTextBoxSwitch.trackTintList = ColorStateList(states, colors)
        clearBreakTextBoxSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        clearBreakTextBoxSwitch.trackTintList = ColorStateList(states, colors)
        showBreakTimeInDecimalSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        showBreakTimeInDecimalSwitch.trackTintList = ColorStateList(states, colors)

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutBreakTimeSettings)
            ?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutBreakTimeSettings)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutBreakTimeSettings)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarBreakTimeSettings)

        val resetDrawable = topAppBar?.menu?.findItem(R.id.reset)?.icon
        resetDrawable?.mutate()

        val navigationDrawable = topAppBar?.navigationIcon
        navigationDrawable?.mutate()

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
    }
}