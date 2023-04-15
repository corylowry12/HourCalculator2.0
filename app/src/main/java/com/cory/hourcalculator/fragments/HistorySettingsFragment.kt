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
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily
import com.google.android.material.snackbar.Snackbar

class HistorySettingsFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_history_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = BottomSheetDialog(requireContext())

        val topAppBar =
            activity?.findViewById<MaterialToolbar>(R.id.materialToolBarAutomaticDeletion)

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

        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            resetDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

            updateCustomColor()

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.reset -> {
                    val resetSettingsLayout = layoutInflater.inflate(R.layout.reset_settings_bottom_sheet, null)
                    dialog.setContentView(resetSettingsLayout)
                    dialog.setCancelable(false)
                    resetSettingsLayout.findViewById<TextView>(R.id.bodyTextView).text = "Would you like to reset History Settings?"
                    val infoCardView = resetSettingsLayout.findViewById<MaterialCardView>(R.id.bodyCardView)
                    val yesResetButton = resetSettingsLayout.findViewById<Button>(R.id.yesResetButton)
                    val cancelResetButton = resetSettingsLayout.findViewById<Button>(R.id.cancelResetButton)

                        infoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                        yesResetButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                        cancelResetButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

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

        main()
    }

    override fun onResume() {
        super.onResume()
        main()
    }

    private fun main() {

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        val toggleHistoryCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewHistory)
        val historyDeletionCardView = requireActivity().findViewById<MaterialCardView>(R.id.historyDeletionCardView)
        val historyDeleteAllOnLimitReachedCardView = requireActivity().findViewById<MaterialCardView>(R.id.historyDeleteAllOnLimitCardView)
        val numberOfDaysCardView = requireActivity().findViewById<MaterialCardView>(R.id.numberOfDaysCardView)
        val openHoursForEditingCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewHistoryClickable)
        val showWagesInHistoryCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewShowWagesInHistory)

        toggleHistoryCardView.shapeAppearanceModel = toggleHistoryCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        historyDeletionCardView.shapeAppearanceModel = historyDeletionCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        historyDeleteAllOnLimitReachedCardView.shapeAppearanceModel = historyDeleteAllOnLimitReachedCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        numberOfDaysCardView.shapeAppearanceModel = numberOfDaysCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        openHoursForEditingCardView.shapeAppearanceModel = openHoursForEditingCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        showWagesInHistoryCardView.shapeAppearanceModel = showWagesInHistoryCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val historyToggleData = HistoryToggleData(requireContext())
        val toggleHistory = requireActivity().findViewById<MaterialSwitch>(R.id.historySwitch)

        if (historyToggleData.loadHistoryState()) {
            toggleHistory?.isChecked = true
            toggleHistory?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!historyToggleData.loadHistoryState()) {
            toggleHistory?.isChecked = false
            toggleHistory?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        toggleHistoryCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleHistory.isChecked = !toggleHistory.isChecked
            if (toggleHistory.isChecked) {
                toggleHistory.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {

                val dialog = BottomSheetDialog(requireContext())
                val historySettingsWarningBottomSheet = LayoutInflater.from(context)
                    .inflate(R.layout.history_settings_warning_bottom_sheet, null)
                dialog.setContentView(historySettingsWarningBottomSheet)
                dialog.setCancelable(false)

                val title = historySettingsWarningBottomSheet.findViewById<TextView>(R.id.headingTextView)
                val body = historySettingsWarningBottomSheet.findViewById<TextView>(R.id.bodyTextView)
                val infoCardView = historySettingsWarningBottomSheet.findViewById<MaterialCardView>(R.id.bodyCardView)
                val yesButton = historySettingsWarningBottomSheet.findViewById<Button>(R.id.yesButton)
                val noButton =
                    historySettingsWarningBottomSheet.findViewById<Button>(R.id.cancelButton)

                title.text = "History"
                body.text = "Would you like to clear history?"

                infoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                noButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                yesButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                    dialog.dismiss()
                    val dbHandler = DBHelper(requireContext(), null)
                    dbHandler.deleteAll()
                    Toast.makeText(requireContext(), getString(R.string.history_deleted), Toast.LENGTH_SHORT).show()
                    val runnable = Runnable {
                        (context as MainActivity).changeBadgeNumber()
                    }

                    MainActivity().runOnUiThread(runnable)
                }
                noButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                    dialog.dismiss()
                }
                dialog.show()
                toggleHistory.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                HistoryAutomaticDeletionData(requireContext()).setHistoryDeletionState(false)
                requireActivity().findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch).isChecked = false
                activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)?.thumbIconDrawable =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_close_16
                    )
            }
            historyToggleData.setHistoryToggle(toggleHistory.isChecked)
            val runnable = Runnable {
                (context as MainActivity).toggleHistory()
            }

            MainActivity().runOnUiThread(runnable)
        }

        toggleHistoryCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the history tab in the bottom navigation view will be shown and history will be stored\n\n" +
                        "When disabled the history tab in the bottom navigation view will not be shown and history will not be stored"
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

        val historyDeletion = HistoryAutomaticDeletionData(requireContext())
        val daysWorked = DaysWorkedPerWeekData(requireContext())

        if (historyDeletion.loadHistoryDeletionState()) {
            activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)?.isChecked =
                true
            activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else {
            activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)?.isChecked =
                false
            activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        requireActivity().findViewById<TextView>(R.id.numberOfEntriesTextView).text =
            daysWorked.loadDaysWorked().toString()

        historyDeletionCardView
            ?.setOnClickListener {
                Vibrate().vibration(requireContext())
                if (HistoryToggleData(requireContext()).loadHistoryState()) {
                    requireActivity().findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch).isChecked =
                        !requireActivity().findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch).isChecked
                    if (activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)?.isChecked!!) {
                        activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)?.thumbIconDrawable =
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_baseline_check_16
                            )
                        historyDeletion.setHistoryDeletionState(true)
                        greaterThan()
                    } else {
                        activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)?.thumbIconDrawable =
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_baseline_close_16
                            )
                        historyDeletion.setHistoryDeletionState(false)
                    }
                }
                else {
                    Toast.makeText(requireContext(), "History must be enabled", Toast.LENGTH_SHORT).show()
                }
            }

        historyDeletionCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled history will automatically be deleted and not allow you to get over whatever limit you have set\n\n" +
                        "When disabled history will not be automatically be deleted"
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

        val deleteAllOnLimitReachedSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.historyDeleteAllOnLimitSwitch)

        if (DeleteAllOnLimitReachedData(requireContext()).loadDeleteAllState()) {
            deleteAllOnLimitReachedSwitch.isChecked = true
            deleteAllOnLimitReachedSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        }
        else {
            deleteAllOnLimitReachedSwitch.isChecked = false
            deleteAllOnLimitReachedSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        historyDeleteAllOnLimitReachedCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            deleteAllOnLimitReachedSwitch.isChecked = !deleteAllOnLimitReachedSwitch.isChecked
            DeleteAllOnLimitReachedData(requireContext()).setDeleteAllState(deleteAllOnLimitReachedSwitch.isChecked)

            if (deleteAllOnLimitReachedSwitch.isChecked) {
                deleteAllOnLimitReachedSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            }
            else {
                deleteAllOnLimitReachedSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        historyDeleteAllOnLimitReachedCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled the app will automatically delete all history when the limit is reached\n\n" +
                        "When disabled the app will not automatically delete history"
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

        numberOfDaysCardView.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (!historyDeletion.loadHistoryDeletionState()) {
                Toast.makeText(
                    requireContext(),
                    "History deletion must be enabled to change these settings",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                transaction?.replace(R.id.fragment_container, NumberOfEntriesBeforeDeletionFragment())?.addToBackStack(null)
                transaction?.commit()
            }
        }

        val historyClickable = ClickableHistoryEntryData(requireContext())
        val historyClickableSwitch = activity?.findViewById<MaterialSwitch>(R.id.clickableHistorySwitch)

        if (historyClickable.loadHistoryItemClickable()) {
            historyClickableSwitch?.isChecked = true
            historyClickableSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!historyClickable.loadHistoryItemClickable()) {
            historyClickableSwitch?.isChecked = false
            historyClickableSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        openHoursForEditingCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            historyClickableSwitch!!.isChecked = !historyClickableSwitch.isChecked
            historyClickable.setHistoryItemClickable(historyClickableSwitch.isChecked)
            if (historyClickableSwitch.isChecked) {
                historyClickableSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            }
            else {
                historyClickableSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        openHoursForEditingCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled each history item will be clickable to automatically open for editing\n\n" +
                        "When disabled each history item will not be clickable"
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

        val showWagesInHistoryData = ShowWagesInHistoryData(requireContext())
        val showWagesInHistorySwitch = requireActivity().findViewById<MaterialSwitch>(R.id.showWagesInHistorySwitch)

        if (showWagesInHistoryData.loadShowWages()) {
            showWagesInHistorySwitch?.isChecked = true
            showWagesInHistorySwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!showWagesInHistoryData.loadShowWages()) {
            showWagesInHistorySwitch?.isChecked = false
            showWagesInHistorySwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        showWagesInHistoryCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            showWagesInHistorySwitch.isChecked = !showWagesInHistorySwitch.isChecked
            showWagesInHistoryData.setShowWages(showWagesInHistorySwitch.isChecked)
            if (showWagesInHistorySwitch.isChecked) {
                showWagesInHistorySwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            }
            else {
                showWagesInHistorySwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
        }

        showWagesInHistoryCardView.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            val infoDialog = BottomSheetDialog(requireContext())
            val infoAboutSettingLayout =
                layoutInflater.inflate(R.layout.info_about_setting_bottom_sheet, null)
            infoDialog.setContentView(infoAboutSettingLayout)
            infoDialog.setCancelable(true)
            infoAboutSettingLayout.findViewById<TextView>(R.id.bodyTextView).text =
                "When enabled each history item will show wages based on the number of hours that is calculated for that entry and the wages that are stored\n\n" +
                        "When disabled each history item will not show wages"
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

    private fun greaterThan() {
        val toggleHistoryAutomaticDeletion =
            activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)

        val dbHandler = DBHelper(requireContext(), null)

        val historyDeletion = HistoryAutomaticDeletionData(requireContext())

        val greaterThan =
            dbHandler.getCount() - DaysWorkedPerWeekData(requireContext()).loadDaysWorked()
        if (dbHandler.getCount() > DaysWorkedPerWeekData(requireContext()).loadDaysWorked()) {

            val dialog = BottomSheetDialog(requireContext())
            val historySettingsWarningBottomSheet = LayoutInflater.from(context)
                .inflate(R.layout.history_settings_warning_bottom_sheet, null)
            dialog.setContentView(historySettingsWarningBottomSheet)
            dialog.setCancelable(false)

            val title = historySettingsWarningBottomSheet.findViewById<TextView>(R.id.headingTextView)
            val body = historySettingsWarningBottomSheet.findViewById<TextView>(R.id.bodyTextView)
            val infoCardView = historySettingsWarningBottomSheet.findViewById<MaterialCardView>(R.id.bodyCardView)
            val yesButton = historySettingsWarningBottomSheet.findViewById<Button>(R.id.yesButton)
            val noButton =
                historySettingsWarningBottomSheet.findViewById<Button>(R.id.cancelButton)

            title.text = "Warning"

            if (DeleteAllOnLimitReachedData(requireContext()).loadDeleteAllState()) {
                body.text = "The number of hours stored is greater than the number allowed to be stored. Would you like to delete all entries?"
            }
            else {
                if (greaterThan > 1) {
                    body.text = getString(R.string.history_deletion_multiple, greaterThan)
                } else {
                    body.text = getString(R.string.history_deletion_single, greaterThan)
                }
            }

            infoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
            noButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

            val historyDeletion2 = HistoryDeletion(requireContext())
            yesButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                dialog.dismiss()
                val undoValues = historyDeletion2.deletion()
                val runnable = Runnable {
                    (context as MainActivity).changeBadgeNumber()
                }
                MainActivity().runOnUiThread(runnable)

                val snackbar = Snackbar.make(requireView(), "$greaterThan Entries Deleted", Snackbar.LENGTH_LONG)
                snackbar.duration = 5000

                snackbar.setAction(getString(R.string.undo)) {
                    Vibrate().vibration(requireContext())
                    historyDeletion.setHistoryDeletionState(false)

                    for (i in 1..undoValues["count"]!!.toInt()) {
                        dbHandler.insertRow(undoValues["inTime"].toString(), undoValues["outTime"].toString(), undoValues["totalHours"].toString(), undoValues["date"].toString().toLong(), undoValues["breakTime"].toString())
                    }

                    MainActivity().runOnUiThread(runnable)

                    toggleHistoryAutomaticDeletion?.isChecked = false
                    toggleHistoryAutomaticDeletion?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                }

                snackbar.setActionTextColor(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateSnackbarActionTextColor())
                )
                snackbar.apply {
                    snackbar.view.background = ResourcesCompat.getDrawable(context.resources, R.drawable.snackbar_corners, context.theme)
                }
                snackbar.show()
            }
            noButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                dialog.dismiss()
                historyDeletion.setHistoryDeletionState(false)

                toggleHistoryAutomaticDeletion?.isChecked = false
                activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            dialog.show()
        }
    }

    private fun reset() {
        val historyDeletion = HistoryAutomaticDeletionData(requireContext())
        val daysWorked = DaysWorkedPerWeekData(requireContext())

        if (historyDeletion.loadHistoryDeletionState() || daysWorked.loadDaysWorked() != 7) {
                Vibrate().vibration(requireContext())
                val toggleHistoryAutomaticDeletion =
                    activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)
            activity?.findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)?.thumbIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
                historyDeletion.setHistoryDeletionState(false)
                daysWorked.setDaysWorked(7)
            requireActivity().findViewById<TextView>(R.id.numberOfEntriesTextView).text =
                daysWorked.loadDaysWorked().toString()
                toggleHistoryAutomaticDeletion?.isChecked = false

        }
        else {
            Toast.makeText(requireContext(), getString(R.string.already_default_settings), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.historySettingsCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val toggleHistoryCardView = requireView().findViewById<MaterialCardView>(R.id.cardViewHistory)
        val historyDeletionCardView = requireView().findViewById<MaterialCardView>(R.id.historyDeletionCardView)
        val historyDeleteAllOnLimitReached = requireView().findViewById<MaterialCardView>(R.id.historyDeleteAllOnLimitCardView)
        val numberOfDaysCardView = requireView().findViewById<MaterialCardView>(R.id.numberOfDaysCardView)
        val clickableHistoryCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewHistoryClickable)
        val showWagesInHistoryCardView = requireActivity().findViewById<MaterialCardView>(R.id.cardViewShowWagesInHistory)

        toggleHistoryCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        historyDeletionCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        historyDeleteAllOnLimitReached.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        numberOfDaysCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        clickableHistoryCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        showWagesInHistoryCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val toggleHistorySwitch = requireActivity().findViewById<MaterialSwitch>(R.id.historySwitch)
        val toggleHistoryDeletion = requireActivity().findViewById<MaterialSwitch>(R.id.toggleHistoryAutomaticDeletionSwitch)
        val toggleDeleteAllSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.historyDeleteAllOnLimitSwitch)
        val clickableHistorySwitch = requireActivity().findViewById<MaterialSwitch>(R.id.clickableHistorySwitch)
        val showWagesInHistorySwitch = requireActivity().findViewById<MaterialSwitch>(R.id.showWagesInHistorySwitch)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )

        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        toggleHistorySwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        toggleHistorySwitch.trackTintList = ColorStateList(states, colors)
        toggleHistoryDeletion.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        toggleHistoryDeletion.trackTintList = ColorStateList(states, colors)
        toggleDeleteAllSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        toggleDeleteAllSwitch.trackTintList = ColorStateList(states, colors)
        clickableHistorySwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        clickableHistorySwitch.trackTintList = ColorStateList(states, colors)
        showWagesInHistorySwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        showWagesInHistorySwitch.trackTintList = ColorStateList(states, colors)

        val collapsingToolbarHistorySettings = requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutHistorySettings)
        collapsingToolbarHistorySettings.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        collapsingToolbarHistorySettings.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        val topAppBar =
            activity?.findViewById<MaterialToolbar>(R.id.materialToolBarAutomaticDeletion)

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
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            resetDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutHistorySettings)?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutHistorySettings)?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }
    }
}