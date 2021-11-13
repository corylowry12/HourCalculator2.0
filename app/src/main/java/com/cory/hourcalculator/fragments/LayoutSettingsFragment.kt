package com.cory.hourcalculator.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LayoutSettingsFragment : Fragment() {

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
            }
            accentColor.loadAccent() == 1 -> {
                activity?.theme?.applyStyle(R.style.pink_accent, true)
            }
            accentColor.loadAccent() == 2 -> {
                activity?.theme?.applyStyle(R.style.orange_accent, true)
            }
            accentColor.loadAccent() == 3 -> {
                activity?.theme?.applyStyle(R.style.red_accent, true)
            }
            accentColor.loadAccent() == 4 -> {
                activity?.theme?.applyStyle(R.style.system_accent, true)
            }
        }
        return inflater.inflate(R.layout.fragment_layout_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarLayoutSettings)

        topAppBar?.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        val vibrationData = VibrationData(requireContext())
        val enableVibration = activity?.findViewById<RadioButton>(R.id.enableVibration)
        val disableVibration = activity?.findViewById<RadioButton>(R.id.disableVibration)

        if (vibrationData.loadVibrationState()) {
            enableVibration?.isChecked = true
        } else if (!vibrationData.loadVibrationState()) {
            disableVibration?.isChecked = true
        }

        enableVibration?.setOnClickListener {
            //vibration(vibrationData)
            if (vibrationData.loadVibrationState()) {
                Toast.makeText(requireContext(), "Vibration Already Enabled", Toast.LENGTH_SHORT).show()
            } else {
                vibrationData.setVibrationState(true)
                Toast.makeText(requireContext(), "Vibration Enabled", Toast.LENGTH_SHORT).show()
            }
        }
        disableVibration?.setOnClickListener {
            //vibration(vibrationData)
            if (!vibrationData.loadVibrationState()) {
                Toast.makeText(requireContext(), "Vibration Already Disabled", Toast.LENGTH_SHORT).show()
            } else {
                vibrationData.setVibrationState(false)
                Toast.makeText(requireContext(), "Vibration Disabled", Toast.LENGTH_SHORT).show()
            }
        }

val historyToggleData = HistoryToggleData(requireContext())
        val enableHistory = activity?.findViewById<RadioButton>(R.id.enableHistory)
        val disableHistory = activity?.findViewById<RadioButton>(R.id.disableHistory)

        if (historyToggleData.loadHistoryState()) {
            enableHistory?.isChecked = true
        } else if (!historyToggleData.loadHistoryState()) {
            disableHistory?.isChecked = true
        }

        enableHistory?.setOnClickListener {
            //vibration(vibrationData)
            if (historyToggleData.loadHistoryState()) {
                Toast.makeText(requireContext(), "History Already Enabled", Toast.LENGTH_SHORT).show()
            } else {
                historyToggleData.setHistoryToggle(true)
                Toast.makeText(requireContext(), "History Is Enabled", Toast.LENGTH_SHORT).show()
            }
            val runnable = Runnable {
                (context as MainActivity).toggleHistory()
            }

            MainActivity().runOnUiThread(runnable)
        }
        disableHistory?.setOnClickListener {
            //vibration(vibrationData)
            if (!historyToggleData.loadHistoryState()) {
                Toast.makeText(requireContext(), "History is already disabled", Toast.LENGTH_SHORT).show()
            } else {
                historyToggleData.setHistoryToggle(false)
                val alertDialog = MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                alertDialog.setTitle("History")
                alertDialog.setMessage("What would you like to do with history?")
                alertDialog.setPositiveButton("Delete") { _, _ ->
                    //vibration(vibrationData)
                    val dbHandler = DBHelper(requireContext(), null)
                    dbHandler.deleteAll()
                    Toast.makeText(requireContext(), "History Deleted", Toast.LENGTH_SHORT).show()
                    val runnable = Runnable {
                        (context as MainActivity).changeBadgeNumber()
                    }

                    MainActivity().runOnUiThread(runnable)
                }
                alertDialog.setNeutralButton("Nothing") {_, _ ->
                    //vibration(vibrationData)
                }
                alertDialog.create().show()
                Toast.makeText(requireContext(), "History Disabled", Toast.LENGTH_SHORT).show()
            }
            val runnable = Runnable {
                (context as MainActivity).toggleHistory()
            }

            MainActivity().runOnUiThread(runnable)
        }

        val breakTextBoxVisiblityClass = BreakTextBoxVisiblityClass(requireContext())
        val enableBreakTextBox = activity?.findViewById<RadioButton>(R.id.showBreakTextBox)
        val disableBreakTextBox = activity?.findViewById<RadioButton>(R.id.hideBreakTextBox)

        if (breakTextBoxVisiblityClass.loadVisiblity() == 0) {
            enableBreakTextBox?.isChecked = true
        } else if (breakTextBoxVisiblityClass.loadVisiblity() == 1) {
            disableBreakTextBox?.isChecked = true
        }

        enableBreakTextBox?.setOnClickListener {
            //vibration(vibrationData)
            if (breakTextBoxVisiblityClass.loadVisiblity() == 0) {
                Toast.makeText(requireContext(), "Break Text Box Already Enabled", Toast.LENGTH_SHORT).show()
            } else {
                breakTextBoxVisiblityClass.setVisiblity(0)
                Toast.makeText(requireContext(), "Break Text Box Enabled", Toast.LENGTH_SHORT).show()
            }
        }
        disableBreakTextBox?.setOnClickListener {
            //vibration(vibrationData)
            if (breakTextBoxVisiblityClass.loadVisiblity() == 1) {
                Toast.makeText(requireContext(), "Break Text Box Already Disabled", Toast.LENGTH_SHORT).show()
            } else {
                breakTextBoxVisiblityClass.setVisiblity(1)
                Toast.makeText(requireContext(), "Break Text Box Disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}