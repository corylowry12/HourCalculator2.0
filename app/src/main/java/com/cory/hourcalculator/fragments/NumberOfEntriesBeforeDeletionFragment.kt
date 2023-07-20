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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.snackbar.Snackbar

class NumberOfEntriesBeforeDeletionFragment : Fragment() {

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
        return inflater.inflate(
            R.layout.fragment_number_of_entries_before_deletion,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        val topAppBar =
            requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarNumberOfDaysBeforeDeletion)
        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.reset -> {
                    Vibrate().vibration(requireContext())
                    val dialog = BottomSheetDialog(requireContext())
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
                        "Would you like to reset Number of Days Worked Settings?"
                    val infoCardView =
                        resetSettingsLayout.findViewById<MaterialCardView>(R.id.bodyCardView)
                    val yesResetButton =
                        resetSettingsLayout.findViewById<Button>(R.id.yesResetButton)
                    val cancelResetButton =
                        resetSettingsLayout.findViewById<Button>(R.id.cancelResetButton)

                    infoCardView.setCardBackgroundColor(
                        Color.parseColor(
                            CustomColorGenerator(
                                requireContext()
                            ).generateCardColor()
                        )
                    )
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

        updateCustomColor()

        val oneCardView = requireActivity().findViewById<MaterialCardView>(R.id.oneCardView)
        val twoCardView = requireActivity().findViewById<MaterialCardView>(R.id.twoCardView)
        val threeCardView = requireActivity().findViewById<MaterialCardView>(R.id.threeCardView)
        val fourCardView = requireActivity().findViewById<MaterialCardView>(R.id.fourCardView)
        val fiveCardView = requireActivity().findViewById<MaterialCardView>(R.id.fiveCardView)
        val sixCardView = requireActivity().findViewById<MaterialCardView>(R.id.sixCardView)
        val sevenCardView = requireActivity().findViewById<MaterialCardView>(R.id.sevenCardView)

        oneCardView.shapeAppearanceModel = oneCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        twoCardView.shapeAppearanceModel = twoCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        threeCardView.shapeAppearanceModel = threeCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        fourCardView.shapeAppearanceModel = fourCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        fiveCardView.shapeAppearanceModel = fiveCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        sixCardView.shapeAppearanceModel = sixCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        sevenCardView.shapeAppearanceModel = sevenCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val one = requireActivity().findViewById<RadioButton>(R.id.one)
        val two = requireActivity().findViewById<RadioButton>(R.id.two)
        val three = requireActivity().findViewById<RadioButton>(R.id.three)
        val four = requireActivity().findViewById<RadioButton>(R.id.four)
        val five = requireActivity().findViewById<RadioButton>(R.id.five)
        val six = requireActivity().findViewById<RadioButton>(R.id.six)
        val seven = requireActivity().findViewById<RadioButton>(R.id.seven)

        val daysWorked = DaysWorkedPerWeekData(requireContext())

        when {
            daysWorked.loadDaysWorked() == 1 -> {
                one?.isChecked = true
            }

            daysWorked.loadDaysWorked() == 2 -> {
                two?.isChecked = true
            }

            daysWorked.loadDaysWorked() == 3 -> {
                three?.isChecked = true
            }

            daysWorked.loadDaysWorked() == 4 -> {
                four?.isChecked = true
            }

            daysWorked.loadDaysWorked() == 5 -> {
                five?.isChecked = true
            }

            daysWorked.loadDaysWorked() == 6 -> {
                six?.isChecked = true
            }

            daysWorked.loadDaysWorked() == 7 -> {
                seven?.isChecked = true
            }
        }

        oneCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 1) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.one_entry_already_enabled),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                greaterThan(1)
            }
        }

        twoCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 2) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.two_entries_already_enabled),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                greaterThan(2)
            }
        }

        threeCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 3) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.three_entries_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                greaterThan(3)
            }
        }

        fourCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 4) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.four_entries_already_enabled),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                greaterThan(4)
            }
        }

        fiveCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 5) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.five_entries_already_enabled),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                greaterThan(5)
            }
        }

        sixCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 6) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.six_entries_already_enabled),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                greaterThan(6)
            }
        }

        sevenCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 7) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.seven_entries_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                greaterThan(7)
            }
        }
    }

    private fun greaterThan(number: Int) {

        val one = requireActivity().findViewById<RadioButton>(R.id.one)
        val two = requireActivity().findViewById<RadioButton>(R.id.two)
        val three = requireActivity().findViewById<RadioButton>(R.id.three)
        val four = requireActivity().findViewById<RadioButton>(R.id.four)
        val five = requireActivity().findViewById<RadioButton>(R.id.five)
        val six = requireActivity().findViewById<RadioButton>(R.id.six)
        val seven = requireActivity().findViewById<RadioButton>(R.id.seven)

        val dbHandler = DBHelper(requireContext(), null)

        val greaterThan =
            dbHandler.getCount() - number
        if (dbHandler.getCount() > number) {

            val dialog = BottomSheetDialog(requireContext())
            val historySettingsWarningBottomSheet = LayoutInflater.from(context)
                .inflate(R.layout.history_settings_warning_bottom_sheet, null)
            dialog.setContentView(historySettingsWarningBottomSheet)
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
            val title =
                historySettingsWarningBottomSheet.findViewById<TextView>(R.id.headingTextView)
            val body = historySettingsWarningBottomSheet.findViewById<TextView>(R.id.bodyTextView)
            val infoCardView =
                historySettingsWarningBottomSheet.findViewById<MaterialCardView>(R.id.bodyCardView)
            val yesButton = historySettingsWarningBottomSheet.findViewById<Button>(R.id.yesButton)
            val noButton =
                historySettingsWarningBottomSheet.findViewById<Button>(R.id.cancelButton)

            title.text = "Warning"

            if (DeleteAllOnLimitReachedData(requireContext()).loadDeleteAllState()) {
                body.text =
                    "The number of hours stored is greater than the number allowed to be stored. Would you like to delete all entries?"
            } else {
                if (greaterThan > 1) {
                    body.text = getString(R.string.history_deletion_multiple, greaterThan)
                } else {
                    body.text = getString(R.string.history_deletion_single, greaterThan)
                }
            }

            infoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
            noButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

            val historyDeletion = HistoryDeletion(requireContext())
            yesButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                dialog.dismiss()
                DaysWorkedPerWeekData(requireContext()).setDaysWorked(number)

                when (number) {
                    1 -> {
                        one.isChecked = true
                        two.isChecked = false
                        three.isChecked = false
                        four.isChecked = false
                        five.isChecked = false
                        six.isChecked = false
                        seven.isChecked = false
                    }

                    2 -> {
                        one.isChecked = false
                        two.isChecked = true
                        three.isChecked = false
                        four.isChecked = false
                        five.isChecked = false
                        six.isChecked = false
                        seven.isChecked = false
                    }

                    3 -> {
                        one.isChecked = false
                        two.isChecked = false
                        three.isChecked = true
                        four.isChecked = false
                        five.isChecked = false
                        six.isChecked = false
                        seven.isChecked = false
                    }

                    4 -> {
                        one.isChecked = false
                        two.isChecked = false
                        three.isChecked = false
                        four.isChecked = true
                        five.isChecked = false
                        six.isChecked = false
                        seven.isChecked = false
                    }

                    5 -> {
                        one.isChecked = false
                        two.isChecked = false
                        three.isChecked = false
                        four.isChecked = false
                        five.isChecked = true
                        six.isChecked = false
                        seven.isChecked = false
                    }

                    6 -> {
                        one.isChecked = false
                        two.isChecked = false
                        three.isChecked = false
                        four.isChecked = false
                        five.isChecked = false
                        six.isChecked = true
                        seven.isChecked = false
                    }

                    7 -> {
                        one.isChecked = false
                        two.isChecked = false
                        three.isChecked = false
                        four.isChecked = false
                        five.isChecked = false
                        six.isChecked = false
                        seven.isChecked = true
                    }
                }

                val undoValues = historyDeletion.deletion()
                val runnable = Runnable {
                    (context as MainActivity).changeBadgeNumber()
                }
                MainActivity().runOnUiThread(runnable)

                val snackbar = Snackbar.make(
                    requireView(),
                    "$greaterThan Entries Deleted",
                    Snackbar.LENGTH_LONG
                )
                snackbar.duration = 5000

                snackbar.setAction(getString(R.string.undo)) {
                    Vibrate().vibration(requireContext())
                    HistoryAutomaticDeletionData(requireContext()).setHistoryDeletionState(false)
                    DaysWorkedPerWeekData(requireContext()).setDaysWorked(7)

                    for (i in 1..undoValues["count"]!!.toInt()) {
                        dbHandler.insertRow(
                            undoValues["inTime"].toString(),
                            undoValues["outTime"].toString(),
                            undoValues["totalHours"].toString(),
                            undoValues["date"].toString().toLong(),
                            undoValues["breakTime"].toString()
                        )
                    }

                    MainActivity().runOnUiThread(runnable)

                    activity?.supportFragmentManager?.popBackStack()
                }

                snackbar.setActionTextColor(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateSnackbarActionTextColor())
                )
                snackbar.apply {
                    snackbar.view.background = ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.snackbar_corners,
                        context.theme
                    )
                }
                snackbar.show()
            }
            noButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                dialog.dismiss()
                DaysWorkedPerWeekData(requireContext()).setDaysWorked(
                    DaysWorkedPerWeekData(
                        requireContext()
                    ).loadDaysWorked()
                )

                when (DaysWorkedPerWeekData(requireContext()).loadDaysWorked()) {
                    1 -> {
                        one.isChecked = true
                        two.isChecked = false
                        three.isChecked = false
                        four.isChecked = false
                        five.isChecked = false
                        six.isChecked = false
                        seven.isChecked = false
                    }

                    2 -> {
                        one.isChecked = false
                        two.isChecked = true
                        three.isChecked = false
                        four.isChecked = false
                        five.isChecked = false
                        six.isChecked = false
                        seven.isChecked = false
                    }

                    3 -> {
                        one.isChecked = false
                        two.isChecked = false
                        three.isChecked = true
                        four.isChecked = false
                        five.isChecked = false
                        six.isChecked = false
                        seven.isChecked = false
                    }

                    4 -> {
                        one.isChecked = false
                        two.isChecked = false
                        three.isChecked = false
                        four.isChecked = true
                        five.isChecked = false
                        six.isChecked = false
                        seven.isChecked = false
                    }

                    5 -> {
                        one.isChecked = false
                        two.isChecked = false
                        three.isChecked = false
                        four.isChecked = false
                        five.isChecked = true
                        six.isChecked = false
                        seven.isChecked = false
                    }

                    6 -> {
                        one.isChecked = false
                        two.isChecked = false
                        three.isChecked = false
                        four.isChecked = false
                        five.isChecked = false
                        six.isChecked = true
                        seven.isChecked = false
                    }

                    7 -> {
                        one.isChecked = false
                        two.isChecked = false
                        three.isChecked = false
                        four.isChecked = false
                        five.isChecked = false
                        six.isChecked = false
                        seven.isChecked = true
                    }
                }

                activity?.supportFragmentManager?.popBackStack()
            }
            dialog.show()
        } else {
            DaysWorkedPerWeekData(requireContext()).setDaysWorked(number)
            when (number) {
                1 -> {
                    one.isChecked = true
                    two.isChecked = false
                    three.isChecked = false
                    four.isChecked = false
                    five.isChecked = false
                    six.isChecked = false
                    seven.isChecked = false
                }

                2 -> {
                    one.isChecked = false
                    two.isChecked = true
                    three.isChecked = false
                    four.isChecked = false
                    five.isChecked = false
                    six.isChecked = false
                    seven.isChecked = false
                }

                3 -> {
                    one.isChecked = false
                    two.isChecked = false
                    three.isChecked = true
                    four.isChecked = false
                    five.isChecked = false
                    six.isChecked = false
                    seven.isChecked = false
                }

                4 -> {
                    one.isChecked = false
                    two.isChecked = false
                    three.isChecked = false
                    four.isChecked = true
                    five.isChecked = false
                    six.isChecked = false
                    seven.isChecked = false
                }

                5 -> {
                    one.isChecked = false
                    two.isChecked = false
                    three.isChecked = false
                    four.isChecked = false
                    five.isChecked = true
                    six.isChecked = false
                    seven.isChecked = false
                }

                6 -> {
                    one.isChecked = false
                    two.isChecked = false
                    three.isChecked = false
                    four.isChecked = false
                    five.isChecked = false
                    six.isChecked = true
                    seven.isChecked = false
                }

                7 -> {
                    one.isChecked = false
                    two.isChecked = false
                    three.isChecked = false
                    four.isChecked = false
                    five.isChecked = false
                    six.isChecked = false
                    seven.isChecked = true
                }
            }
        }
    }

    private fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.numberOfEntriesBeforeDeletionCoordinatorLayout)
            .setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val oneCardView = requireView().findViewById<MaterialCardView>(R.id.oneCardView)
        oneCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        val twoCardView = requireView().findViewById<MaterialCardView>(R.id.twoCardView)
        twoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        val threeCardView = requireView().findViewById<MaterialCardView>(R.id.threeCardView)
        threeCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        val fourCardView = requireView().findViewById<MaterialCardView>(R.id.fourCardView)
        fourCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        val fiveCardView = requireView().findViewById<MaterialCardView>(R.id.fiveCardView)
        fiveCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        val sixCardView = requireView().findViewById<MaterialCardView>(R.id.sixCardView)
        sixCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        val sevenCardView = requireView().findViewById<MaterialCardView>(R.id.sevenCardView)
        sevenCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutNumberOfDaysBeforeDeletion)
        collapsingToolbarLayout.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )
        collapsingToolbarLayout.setStatusBarScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )

        val one = requireActivity().findViewById<RadioButton>(R.id.one)
        val two = requireActivity().findViewById<RadioButton>(R.id.two)
        val three = requireActivity().findViewById<RadioButton>(R.id.three)
        val four = requireActivity().findViewById<RadioButton>(R.id.four)
        val five = requireActivity().findViewById<RadioButton>(R.id.five)
        val six = requireActivity().findViewById<RadioButton>(R.id.six)
        val seven = requireActivity().findViewById<RadioButton>(R.id.seven)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )

        val colors = intArrayOf(
            Color.parseColor("#000000"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        one.buttonTintList = ColorStateList(states, colors)
        two.buttonTintList = ColorStateList(states, colors)
        three.buttonTintList = ColorStateList(states, colors)
        four.buttonTintList = ColorStateList(states, colors)
        five.buttonTintList = ColorStateList(states, colors)
        six.buttonTintList = ColorStateList(states, colors)
        seven.buttonTintList = ColorStateList(states, colors)

        val topAppBar =
            activity?.findViewById<MaterialToolbar>(R.id.materialToolBarNumberOfDaysBeforeDeletion)

        val resetDrawable = topAppBar?.menu?.findItem(R.id.reset)?.icon
        resetDrawable?.mutate()

        val navigationDrawable = topAppBar?.navigationIcon
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

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutNumberOfDaysBeforeDeletion)
            ?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutNumberOfDaysBeforeDeletion)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutNumberOfDaysBeforeDeletion)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }
    }

    private fun reset() {
        val daysWorked = DaysWorkedPerWeekData(requireContext())

        daysWorked.setDaysWorked(7)

        val one = requireActivity().findViewById<RadioButton>(R.id.one)
        val two = requireActivity().findViewById<RadioButton>(R.id.two)
        val three = requireActivity().findViewById<RadioButton>(R.id.three)
        val four = requireActivity().findViewById<RadioButton>(R.id.four)
        val five = requireActivity().findViewById<RadioButton>(R.id.five)
        val six = requireActivity().findViewById<RadioButton>(R.id.six)
        val seven = requireActivity().findViewById<RadioButton>(R.id.seven)

        one.isChecked = false
        two.isChecked = false
        three.isChecked = false
        four.isChecked = false
        five.isChecked = false
        six.isChecked = false
        seven.isChecked = true
    }
}