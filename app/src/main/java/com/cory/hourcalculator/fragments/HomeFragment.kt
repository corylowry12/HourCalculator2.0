package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.DelicateCoroutinesApi
import java.lang.NumberFormatException
import java.math.RoundingMode
import java.util.*

@DelicateCoroutinesApi
class HomeFragment : Fragment() {

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

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val breakTextBox = view.findViewById<TextInputEditText>(R.id.breakTime)

        activity?.window?.setBackgroundDrawable(null)

        val coloredNavBarData = ColoredNavBarData(requireContext())

        if (coloredNavBarData.loadNavBar()) {
            val accentColor = AccentColor(requireContext())
            when {
                accentColor.loadAccent() == 0 -> {
                    activity?.window?.navigationBarColor =
                        ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                }
                accentColor.loadAccent() == 1 -> {
                    activity?.window?.navigationBarColor =
                        ContextCompat.getColor(requireContext(), R.color.pinkAccent)
                }
                accentColor.loadAccent() == 2 -> {
                    activity?.window?.navigationBarColor =
                        ContextCompat.getColor(requireContext(), R.color.orangeAccent)
                }
                accentColor.loadAccent() == 3 -> {
                    activity?.window?.navigationBarColor =
                        ContextCompat.getColor(requireContext(), R.color.redAccent)
                }
                accentColor.loadAccent() == 4 -> {
                    activity?.window?.navigationBarColor =
                        ContextCompat.getColor(requireContext(), R.color.systemAccent)
                }
            }
        } else {
            activity?.window?.navigationBarColor =
                ContextCompat.getColor(requireContext(), R.color.black)
        }

        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        val runnable = Runnable {
            (activity as MainActivity).setActiveTab()
        }

        MainActivity().runOnUiThread(runnable)

        val breakTextView = activity?.findViewById<TextView>(R.id.textViewBreak)
        val breakTextViewInput = activity?.findViewById<TextInputLayout>(R.id.outlinedTextField)
        val breakTextBoxVisiblityClass = BreakTextBoxVisibilityClass(requireContext())

        if (breakTextBoxVisiblityClass.loadVisiblity() == 1) {
            breakTextBox?.visibility = View.GONE
            breakTextView?.visibility = View.GONE
            breakTextViewInput?.visibility = View.GONE
        } else {
            breakTextBox?.visibility = View.VISIBLE
            breakTextView?.visibility = View.VISIBLE
            breakTextViewInput?.visibility = View.VISIBLE
        }

        val timePickerInTime = requireActivity().findViewById<TimePicker>(R.id.timePickerInTime)
        val timePickerOutTime = requireActivity().findViewById<TimePicker>(R.id.timePickerOutTime)

        val dateData = DateData(requireContext())
        if (dateData.loadMinutes1() != "") {
            timePickerInTime.minute = dateData.loadMinutes1()!!.toInt()
        } else {
            dateData.setMinutes1(timePickerInTime.minute.toString())
        }
        if (dateData.loadHours1() != "") {
            timePickerInTime.hour = dateData.loadHours1()!!.toInt()
        } else {
            dateData.setHours1(timePickerInTime.hour.toString())
        }
        if (dateData.loadMinutes2() != "") {
            timePickerOutTime.minute = dateData.loadMinutes2()!!.toInt()
        } else {
            dateData.setMinutes2(timePickerOutTime.minute.toString())
        }
        if (dateData.loadHours2() != "") {
            timePickerOutTime.hour = dateData.loadHours2()!!.toInt()
        } else {
            dateData.setHours2(timePickerOutTime.hour.toString())
        }

        timePickerInTime.setOnTimeChangedListener { _, hourOfDay, minute ->
            Vibrate().vibration(requireContext())
            dateData.setMinutes1(minute.toString())
            dateData.setHours1(hourOfDay.toString())
        }

        timePickerOutTime?.setOnTimeChangedListener { _, hourOfDay, minute ->
            Vibrate().vibration(requireContext())
            dateData.setMinutes2(minute.toString())
            dateData.setHours2(hourOfDay.toString())
        }


        val constraintLayout = activity?.findViewById<ConstraintLayout>(R.id.homeConstraintLayout)

        constraintLayout?.setOnClickListener {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.hideSoftInputFromWindow(constraintLayout.windowToken, 0)
                breakTextBox?.clearFocus()
            }
        }

        val dialogData = DialogData(requireContext())
        val calculationType = CalculationType(requireContext())

        if (!dialogData.loadDialogState()) {
            val alert = MaterialAlertDialogBuilder(
                requireContext(),
                AccentColor(requireContext()).alertTheme()
            )
            alert.setCancelable(false)
            alert.setTitle(getString(R.string.calculation_type))
            alert.setMessage(getString(R.string.choose_the_calculation_method_you_would_prefer))
            alert.setPositiveButton(getString(R.string.decimal)) { _, _ ->
                Vibrate().vibration(requireContext())
                calculationType.setCalculationState(true)
                dialogData.setDialogState(true)
            }
            alert.setNeutralButton(getString(R.string.time)) { _, _ ->
                Vibrate().vibration(requireContext())
                calculationType.setCalculationState(false)
                dialogData.setDialogState(true)
            }
            alert.show()
        }

        val calculateButton = activity?.findViewById<Button>(R.id.calculateButton1)

        calculateButton?.setOnClickListener {

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.hideSoftInputFromWindow(constraintLayout?.windowToken, 0)
                breakTextBox?.clearFocus()
            }

            Vibrate().vibration(requireContext())

            if (calculationType.loadCalculationState()) {
                calculate()
            } else {
                calculateTime()
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

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (breakTextBox.hasFocus()) {
                        breakTextBox.clearFocus()
                    } else {
                        if (doubleBackToExitPressedOnce) {
                            activity?.finishAffinity()
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
    }

    private fun calculateTime() {

        val inTimeTotal: String
        val outTimeTotal: String

        val infoTextView = activity?.findViewById<TextView>(R.id.infoTextView1)

        val inTimePicker = view?.findViewById<TimePicker>(R.id.timePickerInTime)
        val outTimePicker = view?.findViewById<TimePicker>(R.id.timePickerOutTime)

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
        }

        if (diffHours == 0 && diffMinutes == "00") {
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

            val breakTime = activity?.findViewById<TextInputEditText>(R.id.breakTime)
            if (breakTime?.text != null && breakTime.text.toString() != "") {
                if (!breakTimeNumeric(breakTime)) {
                    infoTextView!!.text = getString(R.string.error_with_break_time_must_be_numbers_only)
                }
                else {
                    var withBreak =
                        (diffMinutes.toInt() - breakTime.text.toString().toInt()).toString()
                    var hoursWithBreak = diffHours
                    if (withBreak.toInt() < 0) {
                        hoursWithBreak -= 1
                        withBreak = (withBreak.toInt() + 60).toString()
                    }

                    if (hoursWithBreak < 0) {
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
            }

            val daysWorked = DaysWorkedPerWeek(requireContext())
            val historyAutomaticDeletion = HistoryAutomaticDeletion(requireContext())
            val historyDeletion = HistoryDeletion(requireContext())
            val dbHandler = DBHelper(requireContext(), null)

            if (historyAutomaticDeletion.loadHistoryDeletionState() && dbHandler.getCount() > daysWorked.loadDaysWorked()
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

    private fun calculate() {

        val timePickerInTime = activity?.findViewById<TimePicker>(R.id.timePickerInTime)
        val timePickerOutTime = activity?.findViewById<TimePicker>(R.id.timePickerOutTime)
        val infoTextView1 = activity?.findViewById<TextView>(R.id.infoTextView1)

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

            val breakTime = activity?.findViewById<TextInputEditText>(R.id.breakTime)
            if (breakTime?.text != null && breakTime.text.toString() != "") {
                if (!breakTimeNumeric(breakTime)) {
                    infoTextView1!!.text = getString(R.string.error_with_break_time_must_be_numbers_only)
                }
                else {
                    breakTimeNumber = breakTime.text.toString().toDouble() / 60
                    val totalHoursWithBreak = (totalHours - breakTimeNumber).toBigDecimal()
                        .setScale(2, RoundingMode.HALF_EVEN)

                    if (totalHoursWithBreak.toDouble() < 0.0) {
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
                    }
                }
            } else {
                savingHours(totalHours.toString(), inTimeTotal, outTimeTotal, "0")
                infoTextView1?.text = getString(
                    R.string.total_hours_decimal_format,
                    hoursDifference,
                    minutesWithoutFirstDecimal
                )
            }

            val daysWorked = DaysWorkedPerWeek(requireContext())
            val historyAutomaticDeletion = HistoryAutomaticDeletion(requireContext())
            val historyDeletion = HistoryDeletion(requireContext())
            val dbHandler = DBHelper(requireContext(), null)

            if (historyAutomaticDeletion.loadHistoryDeletionState() && dbHandler.getCount() > daysWorked.loadDaysWorked()
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
    }

    private fun breakTimeNumeric(breakTime: TextInputEditText) : Boolean {
        return try {
            breakTime.text.toString().toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
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