package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class AutomaticDeletionFragment : Fragment() {

    private var color: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                activity?.setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> activity?.setTheme(R.style.Theme_MyApplication)
                    Configuration.UI_MODE_NIGHT_YES -> activity?.setTheme(AccentColor(requireContext()).followSystemTheme(requireContext()))
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> activity?.setTheme(R.style.Theme_AMOLED)
                }
            }
        }

        val accentColor = AccentColor(requireContext())
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
                color = ContextCompat.getColor(requireContext(), R.color.systemAccent)
            }
        }
        return inflater.inflate(R.layout.fragment_automatic_deletion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val topAppBar =
            activity?.findViewById<MaterialToolbar>(R.id.materialToolBarAutomaticDeletion)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.reset -> {
                    reset()
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
            alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                Vibrate().vibration(requireContext())
                HistoryDeletion(requireContext()).deletion()

                val runnable = Runnable {
                    (context as MainActivity).changeBadgeNumber()
                }
                MainActivity().runOnUiThread(runnable)
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

        val alert = MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
        alert.setTitle(getString(R.string.warning))
        alert.setMessage(getString(R.string.reset_automatic_deletion_settings_warning))
        alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
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

            val historyDeletion = HistoryAutomaticDeletion(requireContext())

            historyDeletion.setHistoryDeletionState(false)

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
        alert.setNegativeButton(getString(R.string.no)) { _, _ ->
            Vibrate().vibration(requireContext())
        }
        alert.show()
    }
}