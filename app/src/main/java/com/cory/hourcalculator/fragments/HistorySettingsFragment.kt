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
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class HistorySettingsFragment : Fragment() {

    private var color: Int = 0
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

        val accentColor = AccentColor(requireContext())
        val followSystemVersion = FollowSystemVersion(requireContext())

        when {
            accentColor.loadAccent() == 0 -> {
                activity?.theme?.applyStyle(R.style.teal_accent, true)
                color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            }
            accentColor.loadAccent() == 1 -> {
                activity?.theme?.applyStyle(R.style.pink_accent, true)
                color = ContextCompat.getColor(requireContext(), R.color.pinkAccent)
            }
            accentColor.loadAccent() == 2 -> {
                activity?.theme?.applyStyle(R.style.orange_accent, true)
                color = ContextCompat.getColor(requireContext(), R.color.orangeAccent)
            }
            accentColor.loadAccent() == 3 -> {
                activity?.theme?.applyStyle(R.style.red_accent, true)
                color = ContextCompat.getColor(requireContext(), R.color.redAccent)
            }
            accentColor.loadAccent() == 4 -> {
                activity?.theme?.applyStyle(R.style.system_accent, true)
                color = if (!followSystemVersion.loadSystemColor()) {
                    activity?.theme?.applyStyle(R.style.system_accent, true)
                    ContextCompat.getColor(requireContext(), R.color.systemAccent)
                } else {
                    if (themeSelection) {
                        activity?.theme?.applyStyle(R.style.system_accent_google, true)
                        ContextCompat.getColor(requireContext(), R.color.systemAccentGoogleDark)
                    } else {
                        activity?.theme?.applyStyle(R.style.system_accent_google_light, true)
                        ContextCompat.getColor(requireContext(), R.color.systemAccentGoogleDark_light)
                    }
                }
            }
            accentColor.loadAccent() == 5 -> {
                activity?.theme?.applyStyle(R.style.system_accent_google_light, true)
                color = Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor())
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
            if (AccentColor(requireContext()).loadAccent() == 5) {
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
                activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
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
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            resetDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

        if (AccentColor(requireContext()).loadAccent() == 5) {
            updateCustomColor()
        }

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
                    if (AccentColor(requireContext()).loadAccent() == 5) {
                        infoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
                        yesResetButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                        cancelResetButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                    }
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

        val enableHistoryAutomaticDeletion =
            activity?.findViewById<RadioButton>(R.id.enableHistoryDeletion)
        val disableHistoryAutomaticDeletion =
            activity?.findViewById<RadioButton>(R.id.disableHistoryDeletion)

        val one = activity?.findViewById<RadioButton>(R.id.one)
        val two = activity?.findViewById<RadioButton>(R.id.two)
        val three = activity?.findViewById<RadioButton>(R.id.three)
        val four = activity?.findViewById<RadioButton>(R.id.four)
        val five = activity?.findViewById<RadioButton>(R.id.five)
        val six = activity?.findViewById<RadioButton>(R.id.six)
        val seven = activity?.findViewById<RadioButton>(R.id.seven)

        val historyDeletion = HistoryAutomaticDeletion(requireContext())
        val daysWorked = DaysWorkedPerWeek(requireContext())

        if (historyDeletion.loadHistoryDeletionState()) {
            enableHistoryAutomaticDeletion?.isChecked = true
            one?.isEnabled = true
            two?.isEnabled = true
            three?.isEnabled = true
            four?.isEnabled = true
            five?.isEnabled = true
            six?.isEnabled = true
            seven?.isEnabled = true

            one?.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            )
            two?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
            three?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
            four?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
            five?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
            six?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
            seven?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
        } else {
            disableHistoryAutomaticDeletion?.isChecked = true
            one?.isEnabled = false
            two?.isEnabled = false
            three?.isEnabled = false
            four?.isEnabled = false
            five?.isEnabled = false
            six?.isEnabled = false
            seven?.isEnabled = false

            one?.setTextColor(color)
            two?.setTextColor(color)
            three?.setTextColor(color)
            four?.setTextColor(color)
            five?.setTextColor(color)
            six?.setTextColor(color)
            seven?.setTextColor(color)
        }

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

        enableHistoryAutomaticDeletion?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (historyDeletion.loadHistoryDeletionState()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.history_automatic_deletion_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                historyDeletion.setHistoryDeletionState(true)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.history_automatic_deletion_enabled),
                    Toast.LENGTH_SHORT
                ).show()

                one?.isEnabled = true
                two?.isEnabled = true
                three?.isEnabled = true
                four?.isEnabled = true
                five?.isEnabled = true
                six?.isEnabled = true
                seven?.isEnabled = true

                one?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
                two?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
                three?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
                four?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
                five?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
                six?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
                seven?.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)))
            }

            greaterThan()
        }
        disableHistoryAutomaticDeletion?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (!historyDeletion.loadHistoryDeletionState()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.history_automatic_deletion_already_disabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                historyDeletion.setHistoryDeletionState(false)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.history_automatic_deletion_disabled),
                    Toast.LENGTH_SHORT
                ).show()

                one?.isEnabled = false
                two?.isEnabled = false
                three?.isEnabled = false
                four?.isEnabled = false
                five?.isEnabled = false
                six?.isEnabled = false
                seven?.isEnabled = false

                one?.setTextColor(color)
                two?.setTextColor(color)
                three?.setTextColor(color)
                four?.setTextColor(color)
                five?.setTextColor(color)
                six?.setTextColor(color)
                seven?.setTextColor(color)
            }
        }

        one?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 1) {
                Toast.makeText(requireContext(), getString(R.string.one_entry_already_enabled), Toast.LENGTH_SHORT)
                    .show()
            } else {
                daysWorked.setDaysWorked(1)
                Toast.makeText(requireContext(), getString(R.string.one_entry_enabled), Toast.LENGTH_SHORT)
                    .show()
                greaterThan()
            }
        }

        two?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 2) {
                Toast.makeText(requireContext(), getString(R.string.two_entries_already_enabled), Toast.LENGTH_SHORT)
                    .show()
            } else {
                daysWorked.setDaysWorked(2)
                Toast.makeText(requireContext(), getString(R.string.two_entries_enabled), Toast.LENGTH_SHORT)
                    .show()
                greaterThan()
            }
        }

        three?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 3) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.three_entries_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                daysWorked.setDaysWorked(3)
                Toast.makeText(requireContext(), getString(R.string.three_entries_enabled), Toast.LENGTH_SHORT)
                    .show()
                greaterThan()
            }
        }

        four?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 4) {
                Toast.makeText(requireContext(), getString(R.string.four_entries_already_enabled), Toast.LENGTH_SHORT)
                    .show()
            } else {
                daysWorked.setDaysWorked(4)
                Toast.makeText(requireContext(), getString(R.string.four_entries_enabled), Toast.LENGTH_SHORT)
                    .show()
                greaterThan()
            }
        }

        five?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 5) {
                Toast.makeText(requireContext(), getString(R.string.five_entries_already_enabled), Toast.LENGTH_SHORT)
                    .show()
            } else {
                daysWorked.setDaysWorked(5)
                Toast.makeText(requireContext(), getString(R.string.five_entries_enabled), Toast.LENGTH_SHORT)
                    .show()
                greaterThan()
            }
        }

        six?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 6) {
                Toast.makeText(requireContext(), getString(R.string.six_entries_already_enabled), Toast.LENGTH_SHORT)
                    .show()
            } else {
                daysWorked.setDaysWorked(6)
                Toast.makeText(requireContext(), getString(R.string.six_entries_enabled), Toast.LENGTH_SHORT)
                    .show()
                greaterThan()
            }
        }

        seven?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 7) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.seven_entries_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                daysWorked.setDaysWorked(7)
                Toast.makeText(requireContext(), getString(R.string.seven_entries_enabled), Toast.LENGTH_SHORT)
                    .show()
                greaterThan()
            }
        }
    }

    private fun greaterThan() {
        val enableHistoryAutomaticDeletion =
            activity?.findViewById<RadioButton>(R.id.enableHistoryDeletion)
        val disableHistoryAutomaticDeletion =
            activity?.findViewById<RadioButton>(R.id.disableHistoryDeletion)

        val one = activity?.findViewById<RadioButton>(R.id.one)
        val two = activity?.findViewById<RadioButton>(R.id.two)
        val three = activity?.findViewById<RadioButton>(R.id.three)
        val four = activity?.findViewById<RadioButton>(R.id.four)
        val five = activity?.findViewById<RadioButton>(R.id.five)
        val six = activity?.findViewById<RadioButton>(R.id.six)
        val seven = activity?.findViewById<RadioButton>(R.id.seven)

        val dbHandler = DBHelper(requireContext(), null)

        val historyDeletion = HistoryAutomaticDeletion(requireContext())

        val greaterThan =
            dbHandler.getCount() - DaysWorkedPerWeek(requireContext()).loadDaysWorked()
        if (dbHandler.getCount() > DaysWorkedPerWeek(requireContext()).loadDaysWorked()) {
            val alert = MaterialAlertDialogBuilder(
                requireContext(),
                AccentColor(requireContext()).alertTheme()
            )
            alert.setCancelable(false)
            alert.setTitle(getString(R.string.warning))
            if (greaterThan > 1) {
                alert.setMessage(getString(R.string.history_deletion_multiple, greaterThan))
            } else {
                alert.setMessage(getString(R.string.history_deletion_single, greaterThan))
            }

            val historyDeletion2 = HistoryDeletion(requireContext())
            alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                Vibrate().vibration(requireContext())

                val undoValues = historyDeletion2.deletion()
                val runnable = Runnable {
                    (context as MainActivity).changeBadgeNumber()
                }
                MainActivity().runOnUiThread(runnable)

                val snackbar = Snackbar.make(requireView(), greaterThan.toString() + " Entries Deleted", Snackbar.LENGTH_LONG)
                snackbar.duration = 5000

                snackbar.setAction(getString(R.string.undo)) {

                    historyDeletion.setHistoryDeletionState(false)

                    for (i in 1..undoValues["count"]!!.toInt()) {
                        dbHandler.insertRow(undoValues["inTime"].toString(), undoValues["outTime"].toString(), undoValues["totalHours"].toString(), undoValues["breakTime"].toString().toLong(), undoValues["breakTime"].toString())
                    }

                    MainActivity().runOnUiThread(runnable)

                    enableHistoryAutomaticDeletion?.isChecked = false
                    disableHistoryAutomaticDeletion?.isChecked = true

                    one?.isEnabled = false
                    two?.isEnabled = false
                    three?.isEnabled = false
                    four?.isEnabled = false
                    five?.isEnabled = false
                    six?.isEnabled = false
                    seven?.isEnabled = false

                    one?.isChecked = false
                    two?.isChecked = false
                    three?.isChecked = false
                    four?.isChecked = false
                    five?.isChecked = false
                    six?.isChecked = false
                    seven?.isChecked = true

                    one?.setTextColor(color)
                    two?.setTextColor(color)
                    three?.setTextColor(color)
                    four?.setTextColor(color)
                    five?.setTextColor(color)
                    six?.setTextColor(color)
                    seven?.setTextColor(color)
                }

                snackbar.setActionTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        AccentColor(requireContext()).snackbarActionTextColor()
                    )
                )
                snackbar.apply {
                    snackbar.view.background = ResourcesCompat.getDrawable(context.resources, R.drawable.snackbar_corners, context.theme)
                }
                snackbar.show()
            }
            alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                Vibrate().vibration(requireContext())
                historyDeletion.setHistoryDeletionState(false)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.history_automatic_deletion_disabled),
                    Toast.LENGTH_SHORT
                ).show()

                enableHistoryAutomaticDeletion?.isChecked = false
                disableHistoryAutomaticDeletion?.isChecked = true

                one?.isEnabled = false
                two?.isEnabled = false
                three?.isEnabled = false
                four?.isEnabled = false
                five?.isEnabled = false
                six?.isEnabled = false
                seven?.isEnabled = false

                one?.setTextColor(color)
                two?.setTextColor(color)
                three?.setTextColor(color)
                four?.setTextColor(color)
                five?.setTextColor(color)
                six?.setTextColor(color)
                seven?.setTextColor(color)
            }
            alert.show()
        }
    }

    private fun reset() {
        val historyDeletion = HistoryAutomaticDeletion(requireContext())
        val daysWorked = DaysWorkedPerWeek(requireContext())

        if (historyDeletion.loadHistoryDeletionState() || daysWorked.loadDaysWorked() != 7) {
                Vibrate().vibration(requireContext())
                val enableHistoryAutomaticDeletion =
                    activity?.findViewById<RadioButton>(R.id.enableHistoryDeletion)
                val disableHistoryAutomaticDeletion =
                    activity?.findViewById<RadioButton>(R.id.disableHistoryDeletion)

                val one = activity?.findViewById<RadioButton>(R.id.one)
                val two = activity?.findViewById<RadioButton>(R.id.two)
                val three = activity?.findViewById<RadioButton>(R.id.three)
                val four = activity?.findViewById<RadioButton>(R.id.four)
                val five = activity?.findViewById<RadioButton>(R.id.five)
                val six = activity?.findViewById<RadioButton>(R.id.six)
                val seven = activity?.findViewById<RadioButton>(R.id.seven)

                historyDeletion.setHistoryDeletionState(false)
                daysWorked.setDaysWorked(7)

                enableHistoryAutomaticDeletion?.isChecked = false
                disableHistoryAutomaticDeletion?.isChecked = true

                one?.isEnabled = false
                two?.isEnabled = false
                three?.isEnabled = false
                four?.isEnabled = false
                five?.isEnabled = false
                six?.isEnabled = false
                seven?.isEnabled = false

                one?.isChecked = false
                two?.isChecked = false
                three?.isChecked = false
                four?.isChecked = false
                five?.isChecked = false
                six?.isChecked = false
                seven?.isChecked = true

                one?.setTextColor(color)
                two?.setTextColor(color)
                three?.setTextColor(color)
                four?.setTextColor(color)
                five?.setTextColor(color)
                six?.setTextColor(color)
                seven?.setTextColor(color)
        }
        else {
            Toast.makeText(requireContext(), getString(R.string.already_default_settings), Toast.LENGTH_SHORT).show()
        }
    }

    fun updateCustomColor() {
        val historyDeletionCardView = requireView().findViewById<MaterialCardView>(R.id.historyDeletionCardView)
        historyDeletionCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val daysWorkedCardView = requireView().findViewById<MaterialCardView>(R.id.daysWorkedCardView)
        daysWorkedCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

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
            if (AccentColor(requireContext()).loadAccent() == 5) {
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
                activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
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
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            resetDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }
    }
}