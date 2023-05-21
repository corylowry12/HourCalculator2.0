package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.BreakTextBoxVisibilityData
import com.cory.hourcalculator.sharedprefs.CalculationTypeData
import com.cory.hourcalculator.sharedprefs.ClearBreakTextAutomaticallyData
import com.cory.hourcalculator.sharedprefs.ClickableHistoryEntryData
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.sharedprefs.HistoryToggleData
import com.cory.hourcalculator.sharedprefs.LongPressCalculateButtonData
import com.cory.hourcalculator.sharedprefs.MenuTintData
import com.cory.hourcalculator.sharedprefs.OutTimeData
import com.cory.hourcalculator.sharedprefs.ShowPatchNotesOnAppLaunchData
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import java.lang.Exception

class AppSettingsFragment : Fragment() {

    private lateinit var dialog : BottomSheetDialog

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

        val breakTimeSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewBreakTimeSettings)

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
        showPatchNotesOnAppLaunchCardView.shapeAppearanceModel =
            showPatchNotesOnAppLaunchCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(0f)
                .setBottomLeftCornerSize(0f)
                .build()
        breakTimeSettingsCardView.shapeAppearanceModel =
            breakTimeSettingsCardView.shapeAppearanceModel
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
            Vibrate().vibrateOnLongClick(requireContext())
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
            Vibrate().vibrateOnLongClick(requireContext())
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
            Vibrate().vibrateOnLongClick(requireContext())
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

        val showPatchNotesOnAppLaunchData = ShowPatchNotesOnAppLaunchData(requireContext())
        val showPatchNotesOnAppLaunchSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.showPatchNotesOnAppLaunchSwitch)

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
            showPatchNotesOnAppLaunchData.setShowPatchNotesOnAppLaunch(
                showPatchNotesOnAppLaunchSwitch.isChecked
            )
            if (showPatchNotesOnAppLaunchSwitch.isChecked) {
                showPatchNotesOnAppLaunchSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                showPatchNotesOnAppLaunchSwitch.thumbIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        showPatchNotesOnAppLaunchCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
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

        breakTimeSettingsCardView.setOnClickListener {
            Vibrate().vibration(requireContext())

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            transaction?.replace(R.id.fragment_container, BreakTimeSettingsFragment())
                ?.addToBackStack(null)
            transaction?.commit()
        }

        vibrationCardView.setOnClickListener {
            Vibrate().vibration(requireContext())

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            transaction?.replace(R.id.fragment_container, VibrationSettingsFragment())
                ?.addToBackStack(null)
            transaction?.commit()
        }
    }

    private fun resetMenuPress() {
        dialog = BottomSheetDialog(requireContext())
        val resetSettingsLayout =
            layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
        dialog.setContentView(resetSettingsLayout)
        dialog.setCancelable(false)
        resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text =
            getString(R.string.would_you_like_to_reset_app_settings)
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
        val setOutTimeSwitch = view?.findViewById<MaterialSwitch>(R.id.setOutTimeSwitch)
        val calculationTypeSwitch =
            view?.findViewById<MaterialSwitch>(R.id.setCalculationTypeSwitch)
        val longPressCalculateButton = requireActivity().findViewById<MaterialSwitch>(R.id.toggleLongPressingCalculateSwitch)
        val showPatchNotesOnAppLaunch = requireActivity().findViewById<MaterialSwitch>(R.id.showPatchNotesOnAppLaunchSwitch)

        CalculationTypeData(requireContext()).setCalculationState(true)
        OutTimeData(requireContext()).setOutTimeState(true)
        LongPressCalculateButtonData(requireContext()).setLongClick(true)
        ShowPatchNotesOnAppLaunchData(requireContext()).setShowPatchNotesOnAppLaunch(true)

        setOutTimeSwitch?.isChecked = true
        calculationTypeSwitch?.isChecked = false
        longPressCalculateButton?.isChecked = true
        showPatchNotesOnAppLaunch.isChecked = true

        setOutTimeSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        calculationTypeSwitch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        longPressCalculateButton?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        showPatchNotesOnAppLaunch?.thumbIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)

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
        val breakTimeSettingsCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewBreakTimeSettings)

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
        breakTimeSettingsCardView.setCardBackgroundColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateCardColor()
            )
        )

        val outTimeSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.setOutTimeSwitch)
        val setCalculationTypeSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.setCalculationTypeSwitch)
        val toggleLongPressingCalculateSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.toggleLongPressingCalculateSwitch)
        val showPatchNotesOnAppLaunchSwitch =
            requireActivity().findViewById<MaterialSwitch>(R.id.showPatchNotesOnAppLaunchSwitch)

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
        showPatchNotesOnAppLaunchSwitch.thumbIconTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        showPatchNotesOnAppLaunchSwitch.trackTintList = ColorStateList(states, colors)

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
}