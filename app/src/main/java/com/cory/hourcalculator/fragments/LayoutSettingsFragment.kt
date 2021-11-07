package com.cory.hourcalculator.fragments

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
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.cory.hourcalculator.classes.HistoryToggleData
import com.cory.hourcalculator.classes.VibrationData
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class LayoutSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        if (darkThemeData.loadDarkModeState()) {
            activity?.setTheme(R.style.AMOLED)
        } else {
            activity?.setTheme(R.style.Theme_MyApplication)
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
                val alertDialog = MaterialAlertDialogBuilder(requireContext())
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
    }
}