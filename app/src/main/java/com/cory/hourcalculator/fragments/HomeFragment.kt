package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.math.RoundingMode

class HomeFragment : Fragment() {

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        activity?.supportFragmentManager?.beginTransaction()
            ?.detach(this)?.commitNow()
        activity?.supportFragmentManager?.beginTransaction()
            ?.attach(this)?.commitNow()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                //requireActivity().setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                requireActivity().setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                requireActivity().setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        requireActivity().setTheme(R.style.Theme_MyApplication)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        requireActivity().setTheme(R.style.Theme_AMOLED)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        requireActivity().setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val runnable = Runnable {
                (activity as MainActivity).currentTab = 0
                (activity as MainActivity).setActiveTab(0)
            }

            MainActivity().runOnUiThread(runnable)
        } catch (e : Exception) {
            e.printStackTrace()
        }
        main()
    }

    private fun main() {
        val breakTextBox = requireActivity().findViewById<TextInputEditText>(R.id.breakTimeEditTextHomeFragment)
        val calculateButton =
            requireActivity().findViewById<Button>(R.id.calculateButtonHomeFragment)
        val breakTextViewInput =
            requireActivity().findViewById<TextInputLayout>(R.id.outlinedTextFieldBreakTime)
        val timePickerInTime =
            requireActivity().findViewById<TimePicker>(R.id.timePickerInTimeHomeFragment)
        val timePickerOutTime =
            requireActivity().findViewById<TimePicker>(R.id.timePickerOutTimeHomeFragment)

        val inputManager: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(requireView().windowToken, 0)


        val breakTextView = requireActivity().findViewById<TextView>(R.id.textViewBreakHomeFragment)
        val breakTextBoxVisiblityClass = BreakTextBoxVisibilityData(requireContext())

        if (breakTextBoxVisiblityClass.loadVisiblity() == 1) {
            breakTextBox?.visibility = View.GONE
            breakTextView?.visibility = View.GONE
            breakTextViewInput?.visibility = View.GONE
        } else {
            breakTextBox?.visibility = View.VISIBLE
            breakTextView?.visibility = View.VISIBLE
            breakTextViewInput?.visibility = View.VISIBLE
        }

        val timePickersData = TimePickersData(requireContext())
        if (timePickersData.loadMinutes1() != "") {
            timePickerInTime.minute = timePickersData.loadMinutes1()!!.toInt()
        } else {
            timePickersData.setMinutes1(timePickerInTime.minute.toString())
        }
        if (timePickersData.loadHours1() != "") {
            timePickerInTime.hour = timePickersData.loadHours1()!!.toInt()
        } else {
            timePickersData.setHours1(timePickerInTime.hour.toString())
        }

        if (OutTimeData(requireContext()).loadOutTimeState()) {
            if (timePickersData.loadMinutes2() != "") {
                timePickerOutTime.minute = timePickersData.loadMinutes2()!!.toInt()
            } else {
                timePickersData.setMinutes2(timePickerOutTime.minute.toString())
            }
            if (timePickersData.loadHours2() != "") {
                timePickerOutTime.hour = timePickersData.loadHours2()!!.toInt()
            } else {
                timePickersData.setHours2(timePickerOutTime.hour.toString())
            }
        }

        timePickerInTime.setOnTimeChangedListener { _, hourOfDay, minute ->
            Vibrate().vibrationTimePickers(requireContext())
            timePickersData.setMinutes1(minute.toString())
            timePickersData.setHours1(hourOfDay.toString())
        }

        timePickerOutTime?.setOnTimeChangedListener { _, hourOfDay, minute ->
            Vibrate().vibrationTimePickers(requireContext())
            timePickersData.setMinutes2(minute.toString())
            timePickersData.setHours2(hourOfDay.toString())
        }

        val constraintLayout =
            requireActivity().findViewById<CoordinatorLayout>(R.id.homeFragmentCoordinatorLayout)

        constraintLayout?.setOnClickListener {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.hideSoftInputFromWindow(constraintLayout.windowToken, 0)
                breakTextBox?.clearFocus()
            }
        }

        val calculationTypeData = CalculationTypeData(requireContext())

        calculateButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.hideSoftInputFromWindow(constraintLayout?.windowToken, 0)
                breakTextBox?.clearFocus()
            }

            if (calculationTypeData.loadCalculationState()) {
                calculate(0)
            } else {
                calculateTime(0)
            }
        }

        calculateButton?.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            if (LongPressCalculateButtonData(requireContext()).loadLongClick()) {
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm.isAcceptingText) {
                    imm.hideSoftInputFromWindow(constraintLayout?.windowToken, 0)
                    breakTextBox?.clearFocus()
                }
                if (calculationTypeData.loadCalculationState()) {
                    calculateTime(1)
                } else {
                    calculate(1)
                }
                return@setOnLongClickListener true
            } else {
                return@setOnLongClickListener false
            }
        }

        breakTextBox?.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard(breakTextBox)
                return@OnKeyListener true
            }
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard(breakTextBox)
                return@OnKeyListener true
            }
            false
        })

        var doubleBackToExitPressedOnce = false

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (breakTextBox.hasFocus()) {
                        breakTextBox.clearFocus()
                    } else {
                        if (doubleBackToExitPressedOnce) {
                            requireActivity().finishAffinity()
                        } else {
                            doubleBackToExitPressedOnce = true
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.press_back_again_to_exit),
                                Toast.LENGTH_SHORT
                            ).show()

                            Handler(Looper.getMainLooper()).postDelayed({
                                doubleBackToExitPressedOnce = false
                            }, 2000)
                        }
                    }
                }
            })
        updateCustomTheme()
    }

    private fun updateCustomTheme() {

        requireActivity().findViewById<CoordinatorLayout>(R.id.homeFragmentCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))

        val breakTextBox = requireActivity().findViewById<TextInputEditText>(R.id.breakTimeEditTextHomeFragment)

        val calculateButton = requireActivity().findViewById<Button>(R.id.calculateButtonHomeFragment)
        val breakTextViewInput =
            requireActivity().findViewById<TextInputLayout>(R.id.outlinedTextFieldBreakTime)

        requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarHomeFragment)
            .setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        calculateButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        breakTextViewInput.boxStrokeColor =
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        breakTextViewInput.hintTextColor =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        try {
            breakTextBox.textCursorDrawable = null
        } catch (e: NoSuchMethodError) {
            e.printStackTrace()
        }
        breakTextBox.highlightColor =
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        breakTextBox.setTextIsSelectable(false)

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarHomeFragment)
                .setTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            requireActivity().theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarHomeFragment)
                .setTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }

        val coloredNavBarData = ColoredNavBarData(requireContext())

        if (coloredNavBarData.loadNavBar()) {
            requireActivity().window?.navigationBarColor =
                Color.parseColor(CustomColorGenerator(requireContext()).generateNavBarColor())
        } else {
            requireActivity().window?.navigationBarColor =
                ContextCompat.getColor(requireContext(), R.color.black)
        }
    }

    private fun calculateTime(method: Int) {

        val inTimeTotal: String
        val outTimeTotal: String

        val infoTextView = requireActivity().findViewById<TextView>(R.id.outputTextViewHomeFragment)

        val inTimePicker = view?.findViewById<TimePicker>(R.id.timePickerInTimeHomeFragment)
        val outTimePicker = view?.findViewById<TimePicker>(R.id.timePickerOutTimeHomeFragment)

        inTimePicker?.requestLayout()

        val inTimeHours = inTimePicker?.hour
        var inTimeMinutes = inTimePicker?.minute.toString()
        val outTimeHours = outTimePicker?.hour
        var outTimeMinutes = outTimePicker?.minute.toString()

        if (inTimeMinutes.length == 1) {
            inTimeMinutes = "0$inTimeMinutes"
        }

        if (outTimeMinutes.length == 1) {
            outTimeMinutes = "0$outTimeMinutes"
        }

        var diffHours = outTimeHours!!.toInt() - inTimeHours!!.toInt()
        var diffMinutes = (outTimeMinutes.toInt() - inTimeMinutes.toInt()).toString()

        if (diffMinutes.length == 1) {
            diffMinutes = "0$diffMinutes"
        }

        if (diffMinutes.toInt() < 0) {

            diffMinutes = (60 + diffMinutes.toInt()).toString()
            diffHours -= 1
            if (diffMinutes.length == 1) {
                Toast.makeText(requireContext(), "called", Toast.LENGTH_SHORT).show()
                diffMinutes = "0$diffMinutes"
            }
        }

        if (diffHours == 0 && diffMinutes == "00") {
            Vibrate().vibrateOnError(requireContext())
            infoTextView?.text = getString(R.string.in_time_and_out_time_can_not_be_the_same)
        } else {
            if (diffHours < 0) {
                diffHours += 24
            }
            when {
                inTimeHours.toInt() > 12 -> {
                    val inTime = inTimeHours.toInt() - 12
                    val amOrPm = getString(R.string.PM)
                    inTimeTotal = "$inTime:$inTimeMinutes $amOrPm"
                }
                inTimeHours.toInt() == 0 -> {
                    val inTime = 12
                    val amOrPm = getString(R.string.AM)
                    inTimeTotal = "$inTime:$inTimeMinutes $amOrPm"
                }
                else -> {
                    val amOrPm = getString(R.string.AM)
                    inTimeTotal = "$inTimeHours:$inTimeMinutes $amOrPm"
                }
            }
            when {
                outTimeHours.toInt() > 12 -> {
                    val outTime = outTimeHours.toInt() - 12
                    val amOrPm = getString(R.string.PM)
                    outTimeTotal = "$outTime:$outTimeMinutes $amOrPm"
                }
                outTimeHours.toInt() == 0 -> {
                    val outTime = 12
                    val amOrPm = getString(R.string.AM)
                    outTimeTotal = "$outTime:$outTimeMinutes $amOrPm"
                }
                else -> {
                    val amOrPm = getString(R.string.AM)
                    outTimeTotal = "$outTimeHours:$outTimeMinutes $amOrPm"
                }
            }

            val breakTime = requireActivity().findViewById<TextInputEditText>(R.id.breakTimeEditTextHomeFragment)
            if (breakTime?.text != null && breakTime.text.toString() != "") {
                if (!breakTimeNumeric(breakTime)) {
                    Vibrate().vibrateOnError(requireContext())
                    infoTextView!!.text =
                        getString(R.string.error_with_break_time_must_be_numbers_only)
                } else if (breakTime.text.toString().toInt() > 0) {
                    var withBreak =
                        (diffMinutes.toInt() - breakTime.text.toString().toInt()).toString()
                    var hoursWithBreak = diffHours
                    if (withBreak.toInt() < 0) {
                        hoursWithBreak -= 1
                        withBreak = (withBreak.toInt() + 60).toString()
                        if (withBreak.toInt() < 0) {
                            hoursWithBreak -= 1
                            withBreak = (60 + withBreak.toInt()).toString()
                        }
                    }

                    if (hoursWithBreak < 0) {
                        Vibrate().vibrateOnError(requireContext())
                        infoTextView!!.text = getString(R.string.the_entered_break_time_is_too_big)
                    } else {
                        if (withBreak.length == 1) {
                            withBreak = "0$withBreak"
                        }

                        savingHours(
                            "$hoursWithBreak:$withBreak",
                            inTimeTotal,
                            outTimeTotal,
                            breakTime.text.toString()
                        )

                        infoTextView!!.text = getString(
                            R.string.total_hours_with_break_time_format,
                            diffHours,
                            diffMinutes,
                            hoursWithBreak,
                            withBreak
                        )

                        if (method == 1) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.hour_calculated_in_time_format),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                else {
                    savingHours(
                        "$diffHours:$diffMinutes",
                        inTimeTotal,
                        outTimeTotal,
                        "0"
                    )
                    infoTextView?.text =
                        getString(R.string.total_hours_time_format, diffHours, diffMinutes)

                    if (method == 1) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.hour_calculated_in_time_format),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                savingHours(
                    "$diffHours:$diffMinutes",
                    inTimeTotal,
                    outTimeTotal,
                    "0"
                )
                infoTextView?.text =
                    getString(R.string.total_hours_time_format, diffHours, diffMinutes)

                if (method == 1) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.hour_calculated_in_time_format),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            val daysWorked = DaysWorkedPerWeekData(requireContext())
            val historyAutomaticDeletionData = HistoryAutomaticDeletionData(requireContext())
            val historyDeletion = HistoryDeletion(requireContext())
            val dbHandler = DBHelper(requireContext(), null)

            if (historyAutomaticDeletionData.loadHistoryDeletionState() && dbHandler.getCount() > daysWorked.loadDaysWorked()
                    .toString().toInt()
            ) {
                historyDeletion.deletion()
            }

            val runnable = Runnable {
                (context as MainActivity).changeBadgeNumber()
            }

            MainActivity().runOnUiThread(runnable)
        }
    }

    private fun calculate(method: Int) {

        val timePickerInTime = requireActivity().findViewById<TimePicker>(R.id.timePickerInTimeHomeFragment)
        val timePickerOutTime = requireActivity().findViewById<TimePicker>(R.id.timePickerOutTimeHomeFragment)
        val infoTextView1 = requireActivity().findViewById<TextView>(R.id.outputTextViewHomeFragment)

        var inTimeMinutes = timePickerInTime?.minute.toString()
        val inTimeHours = timePickerInTime?.hour.toString()
        var outTimeMinutes = timePickerOutTime?.minute.toString()
        val outTimeHours = timePickerOutTime?.hour.toString()

        if (inTimeMinutes.length == 1) {
            inTimeMinutes = "0$inTimeMinutes"
        }

        if (outTimeMinutes.length == 1) {
            outTimeMinutes = "0$outTimeMinutes"
        }
        val inTimeTotal: String
        val outTimeTotal: String

        var minutesDecimal: Double = (outTimeMinutes.toInt() - inTimeMinutes.toInt()) / 60.0
        minutesDecimal =
            minutesDecimal.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
        var minutesWithoutFirstDecimal = minutesDecimal.toString().substring(2)
        if (minutesDecimal < 0) {
            minutesWithoutFirstDecimal = (1.0 - minutesWithoutFirstDecimal.toDouble()).toString()
            minutesWithoutFirstDecimal =
                minutesWithoutFirstDecimal.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
                    .toString()
            minutesWithoutFirstDecimal = minutesWithoutFirstDecimal.substring(2)
        }
        var hoursDifference = outTimeHours.toInt() - inTimeHours.toInt()

        if ("$hoursDifference.$minutesWithoutFirstDecimal".toDouble() == 0.0) {
            Vibrate().vibrateOnError(requireContext())
            infoTextView1?.text = getString(R.string.in_time_and_out_time_can_not_be_the_same)
        } else {
            if (minutesDecimal < 0) {
                hoursDifference -= 1
            }
            if (hoursDifference < 0) {
                hoursDifference += 24
            }
            when {
                inTimeHours.toInt() > 12 -> {
                    val inTime = inTimeHours.toInt() - 12
                    val amOrPm = getString(R.string.PM)
                    inTimeTotal = "$inTime:$inTimeMinutes $amOrPm"
                }
                inTimeHours.toInt() == 12 -> {
                    val inTime = 12
                    val amOrPm = getString(R.string.PM)
                    inTimeTotal = "$inTime:$inTimeMinutes $amOrPm"
                }
                inTimeHours.toInt() == 0 -> {
                    val inTime = 12
                    val amOrPm = getString(R.string.AM)
                    inTimeTotal = "$inTime:$inTimeMinutes $amOrPm"
                }
                else -> {
                    val amOrPm = getString(R.string.AM)
                    inTimeTotal = "$inTimeHours:$inTimeMinutes $amOrPm"
                }
            }
            when {
                outTimeHours.toInt() > 12 -> {
                    val outTime = outTimeHours.toInt() - 12
                    val amOrPm = getString(R.string.PM)
                    outTimeTotal = "$outTime:$outTimeMinutes $amOrPm"
                }
                outTimeHours.toInt() == 12 -> {
                    val outTime = 12
                    val amOrPm = getString(R.string.PM)
                    outTimeTotal = "$outTime:$outTimeMinutes $amOrPm"
                }
                outTimeHours.toInt() == 0 -> {
                    val outTime = 12
                    val amOrPm = getString(R.string.AM)
                    outTimeTotal = "$outTime:$outTimeMinutes $amOrPm"
                }
                else -> {
                    val amOrPm = getString(R.string.AM)
                    outTimeTotal = "$outTimeHours:$outTimeMinutes $amOrPm"
                }
            }

            val breakTimeNumber: Double
            val totalHours = "$hoursDifference.$minutesWithoutFirstDecimal".toDouble()

            val breakTime = requireActivity().findViewById<TextInputEditText>(R.id.breakTimeEditTextHomeFragment)
            if (breakTime?.text != null && breakTime.text.toString() != "") {
                if (!breakTimeNumeric(breakTime)) {
                    Vibrate().vibrateOnError(requireContext())
                    infoTextView1!!.text =
                        getString(R.string.error_with_break_time_must_be_numbers_only)
                } else if (breakTime.text.toString().toInt() > 0) {
                    breakTimeNumber = breakTime.text.toString().toDouble() / 60
                    val totalHoursWithBreak = (totalHours - breakTimeNumber).toBigDecimal()
                        .setScale(2, RoundingMode.HALF_EVEN)

                    if (totalHoursWithBreak.toDouble() < 0.0) {
                        Vibrate().vibrateOnError(requireContext())
                        infoTextView1!!.text = getString(R.string.the_entered_break_time_is_too_big)
                    } else {
                        savingHours(
                            totalHoursWithBreak.toString(),
                            inTimeTotal,
                            outTimeTotal,
                            breakTime.text.toString()
                        )

                        infoTextView1?.text = getString(
                            R.string.total_hours_with_break_decimal_format,
                            hoursDifference,
                            minutesWithoutFirstDecimal,
                            totalHoursWithBreak.toString()
                        )
                        if (method == 1) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.hour_calculated_in_decimal_format),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                else {
                    savingHours(totalHours.toString(), inTimeTotal, outTimeTotal, "0")
                    infoTextView1?.text = getString(
                        R.string.total_hours_decimal_format,
                        hoursDifference,
                        minutesWithoutFirstDecimal
                    )
                    if (method == 1) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.hour_calculated_in_decimal_format),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                savingHours(totalHours.toString(), inTimeTotal, outTimeTotal, "0")
                infoTextView1?.text = getString(
                    R.string.total_hours_decimal_format,
                    hoursDifference,
                    minutesWithoutFirstDecimal
                )
                if (method == 1) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.hour_calculated_in_decimal_format),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            val daysWorked = DaysWorkedPerWeekData(requireContext())
            val historyAutomaticDeletionData = HistoryAutomaticDeletionData(requireContext())
            val historyDeletion = HistoryDeletion(requireContext())
            val dbHandler = DBHelper(requireContext(), null)

            if (historyAutomaticDeletionData.loadHistoryDeletionState() && dbHandler.getCount() > daysWorked.loadDaysWorked()
                    .toString().toInt()
            ) {
                historyDeletion.deletion()
            }

            val runnable = Runnable {
                (context as MainActivity).changeBadgeNumber()
            }

            MainActivity().runOnUiThread(runnable)
        }
    }

    private fun savingHours(
        totalHours: String,
        inTimeTotal: String,
        outTimeTotal: String,
        breakTime: String
    ) {

        if (HistoryToggleData(requireContext()).loadHistoryState()) {
            val dbHandler = DBHelper(requireContext(), null)

            val date = System.currentTimeMillis()
            dbHandler.insertRow(inTimeTotal, outTimeTotal, totalHours, date, breakTime)
        }

        if (ClearBreakTextAutomaticallyData(requireContext()).loadClearAutomatically()) {
            requireActivity().findViewById<TextInputEditText>(R.id.breakTimeEditTextHomeFragment).text = null
        }
    }

    private fun breakTimeNumeric(breakTime: TextInputEditText): Boolean {
        return try {
            breakTime.text.toString().toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun hideKeyboard(wagesEditText: TextInputEditText) {
        val inputManager: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = requireActivity().currentFocus

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