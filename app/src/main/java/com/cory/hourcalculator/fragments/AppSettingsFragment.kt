package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class AppSettingsFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_app_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarAppSettings)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.reset -> {
                    val alert = MaterialAlertDialogBuilder(
                        requireContext(),
                        AccentColor(requireContext()).alertTheme()
                    )
                    alert.setCancelable(false)
                    alert.setTitle(getString(R.string.yes))
                    alert.setMessage(getString(R.string.reset_app_settings_warning))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                        Vibrate().vibration(requireContext())
                        reset()
                    }
                    alert.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                        Vibrate().vibration(requireContext())
                        dialog.dismiss()
                    }
                    alert.show()
                    true
                }
                else -> true
            }
        }

        val calculationData = CalculationType(requireContext())
        val enableCalculation = activity?.findViewById<RadioButton>(R.id.enableCalculation)
        val disableCalculation = activity?.findViewById<RadioButton>(R.id.disableCalculation)

        if (calculationData.loadCalculationState()) {
            enableCalculation?.isChecked = true
        } else {
            disableCalculation?.isChecked = true
        }

        enableCalculation?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (calculationData.loadCalculationState()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.decimal_calculation_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                calculationData.setCalculationState(true)
                Toast.makeText(requireContext(), getString(R.string.decimal_calculation_enabled), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        disableCalculation?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (!calculationData.loadCalculationState()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.time_calculation_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                calculationData.setCalculationState(false)
                Toast.makeText(requireContext(), getString(R.string.time_calculation_enabled), Toast.LENGTH_SHORT)
                    .show()
            }
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
            Vibrate().vibration(requireContext())
            if (vibrationData.loadVibrationState()) {
                Toast.makeText(requireContext(), getString(R.string.vibration_already_enabled), Toast.LENGTH_SHORT)
                    .show()
            } else {
                vibrationData.setVibrationState(true)
                Toast.makeText(requireContext(), getString(R.string.vibration_enabled), Toast.LENGTH_SHORT).show()
            }
        }
        disableVibration?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (!vibrationData.loadVibrationState()) {
                Toast.makeText(requireContext(), getString(R.string.vibration_already_disabled), Toast.LENGTH_SHORT)
                    .show()
            } else {
                vibrationData.setVibrationState(false)
                Toast.makeText(requireContext(), getString(R.string.vibration_disabled), Toast.LENGTH_SHORT).show()
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
            Vibrate().vibration(requireContext())
            if (historyToggleData.loadHistoryState()) {
                Toast.makeText(requireContext(), getString(R.string.history_already_enabled), Toast.LENGTH_SHORT)
                    .show()
            } else {
                historyToggleData.setHistoryToggle(true)
                Toast.makeText(requireContext(), getString(R.string.history_is_enabled), Toast.LENGTH_SHORT).show()
            }
            val runnable = Runnable {
                (context as MainActivity).toggleHistory()
            }

            MainActivity().runOnUiThread(runnable)
        }
        disableHistory?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (!historyToggleData.loadHistoryState()) {
                Toast.makeText(requireContext(), getString(R.string.history_is_already_disabled), Toast.LENGTH_SHORT)
                    .show()
            } else {
                historyToggleData.setHistoryToggle(false)
                val alertDialog = MaterialAlertDialogBuilder(
                    requireContext(),
                    AccentColor(requireContext()).alertTheme()
                )
                alertDialog.setCancelable(false)
                alertDialog.setTitle(getString(R.string.history))
                alertDialog.setMessage(getString(R.string.what_would_you_like_to_do_with_history))
                alertDialog.setPositiveButton(getString(R.string.delete)) { _, _ ->
                    Vibrate().vibration(requireContext())
                    val dbHandler = DBHelper(requireContext(), null)
                    dbHandler.deleteAll()
                    Toast.makeText(requireContext(), getString(R.string.history_deleted), Toast.LENGTH_SHORT).show()
                    val runnable = Runnable {
                        (context as MainActivity).changeBadgeNumber()
                    }

                    MainActivity().runOnUiThread(runnable)
                }
                alertDialog.setNeutralButton(getString(R.string.nothing)) { _, _ ->
                    Vibrate().vibration(requireContext())
                }
                alertDialog.create().show()
                Toast.makeText(requireContext(), getString(R.string.history_disabled), Toast.LENGTH_SHORT).show()
            }
            val runnable = Runnable {
                (context as MainActivity).toggleHistory()
            }

            MainActivity().runOnUiThread(runnable)
        }

        val breakTextBoxVisiblityClass = BreakTextBoxVisibilityClass(requireContext())
        val enableBreakTextBox = activity?.findViewById<RadioButton>(R.id.showBreakTextBox)
        val disableBreakTextBox = activity?.findViewById<RadioButton>(R.id.hideBreakTextBox)

        if (breakTextBoxVisiblityClass.loadVisiblity() == 0) {
            enableBreakTextBox?.isChecked = true
        } else if (breakTextBoxVisiblityClass.loadVisiblity() == 1) {
            disableBreakTextBox?.isChecked = true
        }

        enableBreakTextBox?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (breakTextBoxVisiblityClass.loadVisiblity() == 0) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.break_text_box_already_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                breakTextBoxVisiblityClass.setVisibility(0)
                Toast.makeText(requireContext(), getString(R.string.break_text_box_enabled), Toast.LENGTH_SHORT)
                    .show()
            }
        }
        disableBreakTextBox?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (breakTextBoxVisiblityClass.loadVisiblity() == 1) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.break_text_box_already_disabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                breakTextBoxVisiblityClass.setVisibility(1)
                Toast.makeText(requireContext(), getString(R.string.break_text_box_disabled), Toast.LENGTH_SHORT)
                    .show()
            }
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
        val decimalFormat = view?.findViewById<RadioButton>(R.id.enableCalculation)
        val timeFormat = view?.findViewById<RadioButton>(R.id.disableCalculation)
        val enableVibration = view?.findViewById<RadioButton>(R.id.enableVibration)
        val disableVibration = view?.findViewById<RadioButton>(R.id.disableVibration)
        val enableHistory = view?.findViewById<RadioButton>(R.id.enableHistory)
        val disableHistory = view?.findViewById<RadioButton>(R.id.disableHistory)
        val enableBreakTextBox = view?.findViewById<RadioButton>(R.id.showBreakTextBox)
        val disableBreakTextBox = view?.findViewById<RadioButton>(R.id.hideBreakTextBox)
        val wagesEditText = view?.findViewById<TextInputEditText>(R.id.Wages)

        CalculationType(requireContext()).setCalculationState(true)
        VibrationData(requireContext()).setVibrationState(true)
        HistoryToggleData(requireContext()).setHistoryToggle(true)
        BreakTextBoxVisibilityClass(requireContext()).setVisibility(0)
        WagesData(requireContext()).setWageAmount(getString(R.string.wages_default))

        decimalFormat?.isChecked = true
        timeFormat?.isChecked = false
        enableVibration?.isChecked = true
        disableVibration?.isChecked = false
        enableHistory?.isChecked = true
        disableHistory?.isChecked = false
        enableBreakTextBox?.isChecked = true
        disableBreakTextBox?.isChecked = false
        wagesEditText?.setText(getString(R.string.wages_default))

        val runnable = Runnable {
            (context as MainActivity).toggleHistory()
        }

        MainActivity().runOnUiThread(runnable)

        Toast.makeText(requireContext(), getString(R.string.app_settings_reset_to_default), Toast.LENGTH_LONG)
            .show()
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