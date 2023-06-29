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
import android.widget.FrameLayout
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
import com.cory.hourcalculator.sharedprefs.VibrationData
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import java.lang.Exception

class VibrationSettingsFragment : Fragment() {

    private lateinit var dialog: BottomSheetDialog

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vibration_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateCustomColor()

        val toolbar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarVibrationSettings)

        toolbar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        toolbar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.reset -> {
                    resetMenuPress()
                    true
                }

                else -> true
            }
        }

        val vibrationOnClickCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnClick)
        val vibrationOnLongClickCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnLongClick)
        val vibrationOnTimePickerCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnTimePickerChange)
        val vibrationOnErrorCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnCalculationError)

        vibrationOnClickCardView.shapeAppearanceModel = vibrationOnClickCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        vibrationOnLongClickCardView.shapeAppearanceModel = vibrationOnLongClickCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        vibrationOnTimePickerCardView.shapeAppearanceModel = vibrationOnTimePickerCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        vibrationOnErrorCardView.shapeAppearanceModel = vibrationOnErrorCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val vibrationData = VibrationData(requireContext())
        val toggleVibration = activity?.findViewById<MaterialSwitch>(R.id.vibrationSwitch)

        if (vibrationData.loadVibrationOnClickState()) {
            toggleVibration?.isChecked = true
            toggleVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!vibrationData.loadVibrationOnClickState()) {
            toggleVibration?.isChecked = false
            toggleVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        vibrationOnClickCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleVibration!!.isChecked = !toggleVibration.isChecked
            vibrationData.setVibrationOnClickState(toggleVibration.isChecked)
            if (toggleVibration.isChecked) {
                toggleVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        vibrationOnClickCardView.setOnLongClickListener {
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
                "When enabled the app will vibrate every time a button is clicked.\n\n" +
                        "When disabled the app will not vibrate every time a button is clicked."
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

        val toggleVibrationOnLongClick = requireActivity().findViewById<MaterialSwitch>(R.id.vibrationOnLongClickSwitch)

        if (vibrationData.loadVibrationOnLongClickState()) {
            toggleVibrationOnLongClick?.isChecked = true
            toggleVibrationOnLongClick?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!vibrationData.loadVibrationOnLongClickState()) {
            toggleVibrationOnLongClick?.isChecked = false
            toggleVibrationOnLongClick?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        vibrationOnLongClickCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleVibrationOnLongClick!!.isChecked = !toggleVibrationOnLongClick.isChecked
            vibrationData.setVibrationOnLongClickState(toggleVibrationOnLongClick.isChecked)
            if (toggleVibrationOnLongClick.isChecked) {
                toggleVibrationOnLongClick.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleVibrationOnLongClick.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        vibrationOnLongClickCardView.setOnLongClickListener {
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
                "When enabled the app will vibrate every time a button is long clicked.\n\n" +
                        "When disabled the app will not vibrate every time a button is long clicked."
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

        val toggleTimePickerVibration = activity?.findViewById<MaterialSwitch>(R.id.vibrationOnTimePickerChangeSwitch)

        if (vibrationData.loadVibrationOnTimePickerChangeState()) {
            toggleTimePickerVibration?.isChecked = true
            toggleTimePickerVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!vibrationData.loadVibrationOnTimePickerChangeState()) {
            toggleTimePickerVibration?.isChecked = false
            toggleTimePickerVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        vibrationOnTimePickerCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleTimePickerVibration!!.isChecked = !toggleTimePickerVibration.isChecked
            vibrationData.setVibrationOnTimePickerChangeState(toggleTimePickerVibration.isChecked)
            if (toggleTimePickerVibration.isChecked) {
                toggleTimePickerVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleTimePickerVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        vibrationOnTimePickerCardView.setOnLongClickListener {
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
                "When enabled the app will vibrate every time the value for a time picker is changed.\n\n" +
                        "When disabled the app will not vibrate every time the value for a time picker is changed."
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

        val toggleCalculationErrorVibration = activity?.findViewById<MaterialSwitch>(R.id.vibrationOnCalculationErrorSwitch)

        if (vibrationData.loadVibrationOnErrorState()) {
            toggleCalculationErrorVibration?.isChecked = true
            toggleCalculationErrorVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!vibrationData.loadVibrationOnErrorState()) {
            toggleCalculationErrorVibration?.isChecked = false
            toggleCalculationErrorVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        vibrationOnErrorCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleCalculationErrorVibration!!.isChecked = !toggleCalculationErrorVibration.isChecked
            vibrationData.setVibrationOnErrorState(toggleCalculationErrorVibration.isChecked)
            if (toggleCalculationErrorVibration.isChecked) {
                toggleCalculationErrorVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleCalculationErrorVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        vibrationOnErrorCardView.setOnLongClickListener {
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
                "When enabled the app will vibrate every time there is an error with a calculation.\n\n" +
                        "When disabled the app will not vibrate every time there is an error with a calculation."
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

    fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.vibrationSettingsCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))

        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutVibrationSettings)
        collapsingToolbarLayout.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )

        val vibrateOnClickSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.vibrationSwitch)
        val vibrateOnLongClickSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.vibrationOnLongClickSwitch)
        val vibrateOnTimePickerChangeSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.vibrationOnTimePickerChangeSwitch)
        val vibrateOnCalculationErrorSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.vibrationOnCalculationErrorSwitch)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )
        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        vibrateOnClickSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        vibrateOnClickSwitch.trackTintList = ColorStateList(states, colors)
        vibrateOnLongClickSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        vibrateOnLongClickSwitch.trackTintList = ColorStateList(states, colors)
        vibrateOnTimePickerChangeSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        vibrateOnTimePickerChangeSwitch.trackTintList = ColorStateList(states, colors)
        vibrateOnCalculationErrorSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        vibrateOnCalculationErrorSwitch.trackTintList = ColorStateList(states, colors)

        val vibrationOnClickCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnClick)
        val vibrationOnLongClickCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnLongClick)
        val vibrationOnTimePickerChangeCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnTimePickerChange)
        val vibrationOnCalculationErrorCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibrationOnCalculationError)

        vibrationOnClickCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        vibrationOnLongClickCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        vibrationOnTimePickerChangeCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        vibrationOnCalculationErrorCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutVibrationSettings)
            ?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutVibrationSettings)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutVibrationSettings)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarVibrationSettings)

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

    private fun resetMenuPress() {

        dialog = BottomSheetDialog(requireContext())
        val resetSettingsLayout =
            layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
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
        resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text =
            "Would you like to reset Vibration settings?"
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
        VibrationData(requireContext()).setVibrationOnClickState(true)
        VibrationData(requireContext()).setVibrationOnLongClickState(true)
        VibrationData(requireContext()).setVibrationOnTimePickerChangeState(true)
        VibrationData(requireContext()).setVibrationOnErrorState(true)

        val vibrateOnClickSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.vibrationSwitch)
        val vibrateOnLongClickSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.vibrationOnLongClickSwitch)
        val vibrateOnTimePickerChangeSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.vibrationOnTimePickerChangeSwitch)
        val vibrateOnCalculationErrorSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.vibrationOnCalculationErrorSwitch)

        vibrateOnClickSwitch?.isChecked = true
        vibrateOnClickSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        vibrateOnLongClickSwitch?.isChecked = true
        vibrateOnLongClickSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        vibrateOnCalculationErrorSwitch?.isChecked = true
        vibrateOnCalculationErrorSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        vibrateOnTimePickerChangeSwitch?.isChecked = true
        vibrateOnTimePickerChangeSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
    }
}