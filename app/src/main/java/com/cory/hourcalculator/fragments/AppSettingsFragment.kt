package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AppSettingsFragment : Fragment() {

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateCustomColor()

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
        return inflater.inflate(R.layout.fragment_app_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        updateCustomColor()

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarAppSettings)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val dialog = BottomSheetDialog(requireContext())

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.reset -> {

                    val resetSettingsLayout =
                        layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
                    dialog.setContentView(resetSettingsLayout)
                    dialog.setCancelable(false)
                    resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text =
                        "Would you like to reset App Settings?"
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
                    true
                }
                else -> true
            }
        }

        val outTimeCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewSetOutTime)
        val calculationTypeCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewCalculation)
        val longClickEnabledCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewDisableLongPress)
        val vibrationCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibration)
        val showPatchNotesOnAppLaunchCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewShowPatchNotesOnAppLaunch)
        val breakTextBoxCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewBreakTextBox)
        val clearBreakTextBoxCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewClearBreakTextBox)
        val wagesCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewWages)

        outTimeCardView.shapeAppearanceModel = outTimeCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        calculationTypeCardView.shapeAppearanceModel = calculationTypeCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        longClickEnabledCardView.shapeAppearanceModel =
            longClickEnabledCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        vibrationCardView.shapeAppearanceModel = vibrationCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        showPatchNotesOnAppLaunchCardView.shapeAppearanceModel = showPatchNotesOnAppLaunchCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        breakTextBoxCardView.shapeAppearanceModel = breakTextBoxCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        clearBreakTextBoxCardView.shapeAppearanceModel = clearBreakTextBoxCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        wagesCardView.shapeAppearanceModel = wagesCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val outTimeData = OutTimeData(requireContext())

        val outTimeSwitch = activity?.findViewById<MaterialSwitch>(R.id.setOutTimeSwitch)

        if (!outTimeData.loadOutTimeState()) {
            outTimeSwitch?.isChecked = true
            outTimeSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else {
            outTimeSwitch?.isChecked = false
            outTimeSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        outTimeCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            outTimeSwitch?.isChecked = !outTimeSwitch!!.isChecked
            outTimeData.setOutTimeState(!outTimeSwitch.isChecked)
            if (outTimeSwitch.isChecked) {
                outTimeSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                outTimeSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        outTimeCardView.setOnLongClickListener {
            Vibrate().vibration(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the out time time picker on the home screen will match the device clock.\n\n" +
                        "When disabled the out time time picker on the home screen will match what out time was previously entered."
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

        val calculationData = CalculationTypeData(requireContext())

        val calculationTypeSwitch =
            activity?.findViewById<MaterialSwitch>(R.id.setCalculationTypeSwitch)

        if (calculationData.loadCalculationState()) {
            calculationTypeSwitch?.isChecked = false
            calculationTypeSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        } else {
            calculationTypeSwitch?.isChecked = true
            calculationTypeSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        }

        calculationTypeCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            calculationTypeSwitch!!.isChecked = !calculationTypeSwitch.isChecked
            calculationData.setCalculationState(!calculationTypeSwitch.isChecked)
            if (calculationTypeSwitch.isChecked) {
                calculationTypeSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                calculationTypeSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        calculationTypeCardView.setOnLongClickListener {
            Vibrate().vibration(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the app will calculate time in time format. (eg. Total Hours: 6:45)\n\n" +
                        "When disabled the app will calculate time in decimal format. (eg. Total Hours: 6.45)"
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

        val longClickEnabled = LongPressCalculateButtonData(requireContext())

        val longClickEnabledSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.toggleLongPressingCalculateSwitch)

        if (longClickEnabled.loadLongClick()) {
            longClickEnabledSwitch.isChecked = true
            longClickEnabledSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else {
            longClickEnabledSwitch.isChecked = false
            longClickEnabledSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        longClickEnabledCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            longClickEnabledSwitch.isChecked = !longClickEnabledSwitch.isChecked
            longClickEnabled.setLongClick(longClickEnabledSwitch.isChecked)
            if (longClickEnabledSwitch.isChecked) {
                longClickEnabledSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                longClickEnabledSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        longClickEnabledCardView.setOnLongClickListener {
            Vibrate().vibration(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled you can long click on the calculate button and calculate time format in the opposite format of what is enabled.\n\n" +
                        "When disabled you will not be able to long click the calculate button to calculate time in another format."
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

        val vibrationData = VibrationData(requireContext())
        val toggleVibration = activity?.findViewById<MaterialSwitch>(R.id.vibrationSwitch)

        if (vibrationData.loadVibrationState()) {
            toggleVibration?.isChecked = true
            toggleVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!vibrationData.loadVibrationState()) {
            toggleVibration?.isChecked = false
            toggleVibration?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        vibrationCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleVibration!!.isChecked = !toggleVibration.isChecked
            vibrationData.setVibrationState(toggleVibration.isChecked)
            if (toggleVibration.isChecked) {
                toggleVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleVibration.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        vibrationCardView.setOnLongClickListener {
            Vibrate().vibration(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
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

        val showPatchNotesOnAppLaunchData = ShowPatchNotesOnAppLaunchData(requireContext())
        val showPatchNotesOnAppLaunchSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.showPatchNotesOnAppLaunchSwitch)

        if (showPatchNotesOnAppLaunchData.loadShowPatchNotesOnAppLaunch()) {
            showPatchNotesOnAppLaunchSwitch?.isChecked = true
            showPatchNotesOnAppLaunchSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!showPatchNotesOnAppLaunchData.loadShowPatchNotesOnAppLaunch()) {
            showPatchNotesOnAppLaunchSwitch?.isChecked = false
            showPatchNotesOnAppLaunchSwitch?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }


        showPatchNotesOnAppLaunchCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            showPatchNotesOnAppLaunchSwitch!!.isChecked = !showPatchNotesOnAppLaunchSwitch.isChecked
            showPatchNotesOnAppLaunchData.setShowPatchNotesOnAppLaunch(showPatchNotesOnAppLaunchSwitch.isChecked)
            if (showPatchNotesOnAppLaunchSwitch.isChecked) {
                showPatchNotesOnAppLaunchSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                showPatchNotesOnAppLaunchSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        showPatchNotesOnAppLaunchCardView.setOnLongClickListener {
            Vibrate().vibration(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the update patch notes will show on app launch after an update.\n\n" +
                        "When disabled the update patch notes will not show on app launch after an update."
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
            Vibrate().vibration(requireContext())
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
        val clearBreakTextBoxSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.clearBreakTextBoxSwitch)

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
            Vibrate().vibration(requireContext())
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

        val wagesData = WagesData(requireContext())
        val wagesEditText = activity?.findViewById<TextInputEditText>(R.id.Wages)

        val editable =
            Editable.Factory.getInstance().newEditable(wagesData.loadWageAmount().toString())
        wagesEditText?.text = editable

        wagesEditText?.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard(wagesEditText)
                return@OnKeyListener true
            }
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                wagesData.setWageAmount(wagesEditText.text.toString())
                hideKeyboard(wagesEditText)
                return@OnKeyListener true
            }
            false
        })

        wagesEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    wagesData.setWageAmount(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wagesData.setWageAmount(s.toString())
            }
        })
    }

    private fun reset() {
        val setOutTimeSwitch = view?.findViewById<MaterialSwitch>(R.id.setOutTimeSwitch)
        val calculationTypeSwitch =
            view?.findViewById<MaterialSwitch>(R.id.setCalculationTypeSwitch)
        val vibrationSwitch = view?.findViewById<MaterialSwitch>(R.id.vibrationSwitch)
        val toggleHistory = view?.findViewById<MaterialSwitch>(R.id.historySwitch)
        val toggleBreakTextBox = view?.findViewById<MaterialSwitch>(R.id.breakTextBoxSwitch)
        val wagesEditText = view?.findViewById<TextInputEditText>(R.id.Wages)

        CalculationTypeData(requireContext()).setCalculationState(true)
        VibrationData(requireContext()).setVibrationState(true)
        HistoryToggleData(requireContext()).setHistoryToggle(true)
        BreakTextBoxVisibilityData(requireContext()).setVisibility(0)
        WagesData(requireContext()).setWageAmount(getString(R.string.wages_default))
        OutTimeData(requireContext()).setOutTimeState(true)
        ClickableHistoryEntryData(requireContext()).setHistoryItemClickable(true)

        setOutTimeSwitch?.isChecked = true
        calculationTypeSwitch?.isChecked = true
        vibrationSwitch?.isChecked = true
        toggleHistory?.isChecked = true
        toggleBreakTextBox?.isChecked = true
        wagesEditText?.setText(getString(R.string.wages_default))

        setOutTimeSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        calculationTypeSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        vibrationSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        toggleHistory?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        toggleBreakTextBox?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)

        val runnable = Runnable {
            (context as MainActivity).toggleHistory()
        }

        MainActivity().runOnUiThread(runnable)

        Toast.makeText(
            requireContext(),
            getString(R.string.app_settings_reset_to_default),
            Toast.LENGTH_LONG
        )
            .show()
    }

    private fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.appSettingsCoordinatorLayout)
            .setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAppSettings)
        collapsingToolbarLayout.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )

        val outTimeCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewSetOutTime)
        val calculationTypeCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewCalculation)
        val longClickEnabledCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewDisableLongPress)
        val vibrationCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewVibration)
        val showPatchNotesOnAppLaunchCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewShowPatchNotesOnAppLaunch)
        val breakTextBoxCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewBreakTextBox)
        val clearBreakTextBoxCardView =
            requireActivity().findViewById<MaterialCardView>(R.id.cardViewClearBreakTextBox)
        val wagesCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewWages)

        outTimeCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        calculationTypeCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
        longClickEnabledCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
        vibrationCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
        showPatchNotesOnAppLaunchCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )
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
        wagesCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val outTimeSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.setOutTimeSwitch)
        val setCalculationTypeSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.setCalculationTypeSwitch)
        val toggleLongPressingCalculateSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.toggleLongPressingCalculateSwitch)
        val vibrationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.vibrationSwitch)
        val showPatchNotesOnAppLaunchSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.showPatchNotesOnAppLaunchSwitch)
        val breakTextBoxSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.breakTextBoxSwitch)
        val clearBreakTextBoxSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.clearBreakTextBoxSwitch)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )
        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        outTimeSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        outTimeSwitch.trackTintList = ColorStateList(states, colors)
        setCalculationTypeSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        setCalculationTypeSwitch.trackTintList = ColorStateList(states, colors)
        toggleLongPressingCalculateSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        toggleLongPressingCalculateSwitch.trackTintList = ColorStateList(states, colors)
        vibrationSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        vibrationSwitch.trackTintList = ColorStateList(states, colors)
        showPatchNotesOnAppLaunchSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        showPatchNotesOnAppLaunchSwitch.trackTintList = ColorStateList(states, colors)
        breakTextBoxSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        breakTextBoxSwitch.trackTintList = ColorStateList(states, colors)
        clearBreakTextBoxSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        clearBreakTextBoxSwitch.trackTintList = ColorStateList(states, colors)

        val wagesEditText = requireActivity().findViewById<TextInputEditText>(R.id.Wages)
        val wagesTextInputEditText =
            activity?.findViewById<TextInputLayout>(R.id.outlinedTextFieldWages)

        wagesTextInputEditText?.boxStrokeColor = Color.parseColor("#000000")
        wagesTextInputEditText?.hintTextColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        wagesEditText.textCursorDrawable = null
        wagesTextInputEditText?.defaultHintTextColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        wagesEditText.highlightColor =
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        wagesEditText.setTextIsSelectable(false)

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAppSettings)
            ?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAppSettings)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAppSettings)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarAppSettings)

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

    private fun hideKeyboard(wagesEditText: TextInputEditText) {
        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = activity?.currentFocus

        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(
                focusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            if (wagesEditText.hasFocus()) {
                wagesEditText.clearFocus()
            }
        }
    }
}