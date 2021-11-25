package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.MaterialToolbar

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
                    Configuration.UI_MODE_NIGHT_YES -> activity?.setTheme(R.style.Theme_AMOLED)
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> activity?.setTheme(R.style.Theme_AMOLED)
                }
            }
        }

        val accentColor = AccentColor(requireContext())
        when {
            accentColor.loadAccent() == 0 -> {
                activity?.theme?.applyStyle(R.style.teal_accent, true)
                color = resources.getColor(R.color.colorPrimary)
            }
            accentColor.loadAccent() == 1 -> {
                activity?.theme?.applyStyle(R.style.pink_accent, true)
                color = resources.getColor(R.color.pinkAccent)
            }
            accentColor.loadAccent() == 2 -> {
                activity?.theme?.applyStyle(R.style.orange_accent, true)
                color = resources.getColor(R.color.orangeAccent)
            }
            accentColor.loadAccent() == 3 -> {
                activity?.theme?.applyStyle(R.style.red_accent, true)
                color = resources.getColor(R.color.redAccent)
            }
            accentColor.loadAccent() == 4 -> {
                activity?.theme?.applyStyle(R.style.system_accent, true)
                color = resources.getColor(R.color.systemAccent)
            }
        }
        return inflater.inflate(R.layout.fragment_automatic_deletion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarAutomaticDeletion)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val enableHistoryAutomaticDeletion = activity?.findViewById<RadioButton>(R.id.enableHistoryDeletion)
        val disableHistoryAutomaticDeletion = activity?.findViewById<RadioButton>(R.id.disableHistoryDeletion)

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

            one?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
            two?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
            three?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
            four?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
            five?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
            six?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
            seven?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
        }
        else {
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
                Toast.makeText(requireContext(), "History Automatic Deletion Already Enabled", Toast.LENGTH_SHORT).show()
            } else {
                historyDeletion.setHistoryDeletionState(true)
                Toast.makeText(requireContext(), "History Automatic Deletion Enabled", Toast.LENGTH_SHORT).show()

                one?.isEnabled = true
                two?.isEnabled = true
                three?.isEnabled = true
                four?.isEnabled = true
                five?.isEnabled = true
                six?.isEnabled = true
                seven?.isEnabled = true

                one?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
                two?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
                three?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
                four?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
                five?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
                six?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
                seven?.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.black)))
            }
        }
        disableHistoryAutomaticDeletion?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (!historyDeletion.loadHistoryDeletionState()) {
                Toast.makeText(requireContext(), "History Automatic Deletion Already Disabled", Toast.LENGTH_SHORT).show()
            } else {
                historyDeletion.setHistoryDeletionState(false)
                Toast.makeText(requireContext(), "History Automatic Deletion Disabled", Toast.LENGTH_SHORT).show()

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
                Toast.makeText(requireContext(), "One Entry Already Enabled", Toast.LENGTH_SHORT).show()
            } else {
                daysWorked.setDaysWorked(1)
                Toast.makeText(requireContext(), "One Entry Enabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        two?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 2) {
                Toast.makeText(requireContext(), "Two Entries Already Enabled", Toast.LENGTH_SHORT).show()
            } else {
                daysWorked.setDaysWorked(2)
                Toast.makeText(requireContext(), "Two Entries Enabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        three?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 3) {
                Toast.makeText(requireContext(), "Three Entries Already Enabled", Toast.LENGTH_SHORT).show()
            } else {
                daysWorked.setDaysWorked(3)
                Toast.makeText(requireContext(), "Three Entries Enabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        four?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 4) {
                Toast.makeText(requireContext(), "Four Entries Already Enabled", Toast.LENGTH_SHORT).show()
            } else {
                daysWorked.setDaysWorked(4)
                Toast.makeText(requireContext(), "Four Entries Enabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        five?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 5) {
                Toast.makeText(requireContext(), "Five Entries Already Enabled", Toast.LENGTH_SHORT).show()
            } else {
                daysWorked.setDaysWorked(5)
                Toast.makeText(requireContext(), "Five Entries Enabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        six?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 6) {
                Toast.makeText(requireContext(), "Six Entries Already Enabled", Toast.LENGTH_SHORT).show()
            } else {
                daysWorked.setDaysWorked(6)
                Toast.makeText(requireContext(), "Six Entries Enabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        seven?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (daysWorked.loadDaysWorked() == 7) {
                Toast.makeText(requireContext(), "Seven Entries Already Enabled", Toast.LENGTH_SHORT).show()
            } else {
                daysWorked.setDaysWorked(7)
                Toast.makeText(requireContext(), "Seven Entries Enabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}