package com.cory.hourcalculator.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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

        val runnable = Runnable {
            (activity as MainActivity).setActiveTab()
        }

        MainActivity().runOnUiThread(runnable)

        val breakTextBox = requireActivity().findViewById<TextInputEditText>(R.id.breakTime)
        val breakTextView = activity?.findViewById<TextView>(R.id.textViewBreak)
        val breakTextViewInput = activity?.findViewById<TextInputLayout>(R.id.outlinedTextField)
        val breakTextBoxVisiblityClass = BreakTextBoxVisiblityClass(requireContext())

        if (breakTextBoxVisiblityClass.loadVisiblity() == 1) {
            breakTextBox?.visibility = View.GONE
            breakTextView?.visibility = View.GONE
            breakTextViewInput?.visibility = View.GONE
        }
        else {
            breakTextBox?.visibility = View.VISIBLE
            breakTextView?.visibility = View.VISIBLE
            breakTextViewInput?.visibility = View.VISIBLE
        }

        val constraintLayout = activity?.findViewById<ConstraintLayout>(R.id.homeConstraintLayout)


        constraintLayout?.setOnClickListener {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.hideSoftInputFromWindow(constraintLayout.windowToken, 0)
                breakTextBox?.clearFocus()
            }
        }

       val calculateButton = activity?.findViewById<Button>(R.id.calculateButton1)

        calculateButton?.setOnClickListener {

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.hideSoftInputFromWindow(constraintLayout?.windowToken, 0)
                breakTextBox?.clearFocus()
            }

            calculate()
        }

        var doubleBackToExitPressedOnce = false

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (breakTextBox.hasFocus()) {
                    breakTextBox.clearFocus()
                }
                else {
                    if(doubleBackToExitPressedOnce) {
                        activity?.finishAffinity()
                    }
                    else {
                        doubleBackToExitPressedOnce = true
                        Toast.makeText(
                            requireContext(),
                            "Press BACK Again To Exit",
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
        minutesDecimal = minutesDecimal.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
        var minutesWithoutFirstDecimal = minutesDecimal.toString().substring(2)
        if (minutesDecimal < 0) {
            minutesWithoutFirstDecimal = (1.0 - minutesWithoutFirstDecimal.toDouble()).toString()
            minutesWithoutFirstDecimal = minutesWithoutFirstDecimal.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString()
            minutesWithoutFirstDecimal = minutesWithoutFirstDecimal.substring(2)
        }
        var hoursDifference = outTimeHours.toInt() - inTimeHours.toInt()
        if ("$hoursDifference.$minutesWithoutFirstDecimal".toDouble() == 0.0) {
            infoTextView1?.text = "In time and out time can not be the same"
        } else if (timePickerInTime!!.hour >= 0 && timePickerOutTime!!.hour <= 12 && hoursDifference < 0) {
            infoTextView1!!.text = "In Time Can Not Be greater than out time"
        } else if (timePickerInTime.hour >= 12 && timePickerOutTime!!.hour <= 24 && hoursDifference < 0) {
            infoTextView1!!.text = "In Time Can Not Be Greater Than Out Time"
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
                    val amOrPm = "pm"
                    inTimeTotal = "$inTime:$inTimeMinutes $amOrPm"
                }
                inTimeHours.toInt() == 0 -> {
                    val inTime = 12
                    val amOrPm = "am"
                    inTimeTotal = "$inTime:$inTimeMinutes $amOrPm"
                }
                else -> {
                    val amOrPm = "am"
                    inTimeTotal = "$inTimeHours:$inTimeMinutes $amOrPm"
                }
            }
            when {
                outTimeHours.toInt() > 12 -> {
                    val outTime = outTimeHours.toInt() - 12
                    val amOrPm = "pm"
                    outTimeTotal = "$outTime:$outTimeMinutes $amOrPm"
                }
                outTimeHours.toInt() == 0 -> {
                    val outTime = 12
                    val amOrPm = "am"
                    outTimeTotal = "$outTime:$outTimeMinutes $amOrPm"
                }
                else -> {
                    val amOrPm = "am"
                    outTimeTotal = "$outTimeHours:$outTimeMinutes $amOrPm"
                }
            }

            var breakTimeNumber = 0.0
            val totalHours = "$hoursDifference.$minutesWithoutFirstDecimal".toDouble()

            val breakTime = activity?.findViewById<TextInputEditText>(R.id.breakTime)
            if (breakTime?.text != null && breakTime.text.toString() != "") {

                Toast.makeText(requireActivity(), "break time not empty $totalHours", Toast.LENGTH_LONG).show()
                breakTimeNumber = breakTime.text.toString().toDouble() / 60
                val totalHoursWithBreak = (totalHours - breakTimeNumber).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString()
                Toast.makeText(requireActivity(), "break time not empty $totalHoursWithBreak", Toast.LENGTH_LONG).show()

                savingHours(totalHours, inTimeTotal, outTimeTotal, breakTime.text.toString())
                infoTextView1!!.text =  "Total Hours: " +"$hoursDifference.$minutesWithoutFirstDecimal\nTotal Hours With Break: $totalHoursWithBreak"
            }
            else {
                savingHours(totalHours, inTimeTotal, outTimeTotal, "0")
                infoTextView1!!.text =  "Total Hours: " +"$hoursDifference.$minutesWithoutFirstDecimal"
            }

            val daysWorked = DaysWorkedPerWeek(requireContext())
            val historyAutomaticDeletion = HistoryAutomaticDeletion(requireContext())
            val historyDeletion = HistoryDeletion(requireContext())
            val dbHandler = DBHelper(requireContext(), null)

            if (historyAutomaticDeletion.loadHistoryDeletionState() && dbHandler.getCount() > daysWorked.loadDaysWorked().toString().toInt()) {
                historyDeletion.deletion()
            }

            val runnable = Runnable {
                (context as MainActivity).changeBadgeNumber()
            }

            MainActivity().runOnUiThread(runnable)
        }
    }

    private fun savingHours(totalHours: Double, inTimeTotal: String, outTimeTotal: String, breakTime: String) {

        val dbHandler = DBHelper(requireContext(), null)

        val day = LocalDateTime.now()
        val day2 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val dayOfWeek = day.format(day2)
        dbHandler.insertRow(inTimeTotal, outTimeTotal, totalHours.toString(), dayOfWeek, breakTime)
    }
}