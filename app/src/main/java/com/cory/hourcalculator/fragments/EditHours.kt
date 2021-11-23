package com.cory.hourcalculator.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.CalculationType
import com.cory.hourcalculator.classes.IdData
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class EditHours : Fragment() {

    private val dataList = ArrayList<HashMap<String, String>>()

    private lateinit var idMap: String
    private lateinit var day: String

    private lateinit var inTime: String
    private lateinit var outTime: String
    private lateinit var breakTime: String

    private var inTimeBool = false
    private var outTimeBool = false
    private var breakTimeBool = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_hours, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val materialToolbarEdit = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarEditFragment)

        materialToolbarEdit?.setNavigationOnClickListener {
            exit()
        }

        materialToolbarEdit?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> {
                    val alert = MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
                    alert.setTitle(getString(R.string.delete))
                    alert.setMessage(getString(R.string.would_you_like_to_delete_history))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                        val dbHandler = DBHelper(requireContext(), null)
                        dbHandler.deleteRow(idMap)
                        Toast.makeText(requireContext(), getString(R.string.hour_deleted), Toast.LENGTH_SHORT).show()
                        val runnable = Runnable {
                            (context as MainActivity).changeBadgeNumber()
                        }
                        MainActivity().runOnUiThread(runnable)
                        activity?.supportFragmentManager?.popBackStack()
                    }
                    alert.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    alert.show()
                    true
                }
                else -> false
            }
        }

        main()

        val inTimePickerEdit = activity?.findViewById<TimePicker>(R.id.timePickerInTimeEdit)
        val outTimePickerEdit = activity?.findViewById<TimePicker>(R.id.timePickerOutTimeEdit)
        val breakTimeEditText = activity?.findViewById<TextInputEditText>(R.id.breakTimeEdit)

        breakTimeEditText?.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard(breakTimeEditText)
                return@OnKeyListener true
            }
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard(breakTimeEditText)
                return@OnKeyListener true
            }
            false
        })

        breakTimeEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                breakTimeBool = s.toString() != "" && s.toString() != breakTime
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                breakTimeBool = s.toString() != "" && s.toString() != breakTime
            }
        })

        inTimePickerEdit?.setOnTimeChangedListener { _, i, i2 ->
            val inTimeMinutesNumbers: Int

            val (inTimeHours, inTimeMinutes) = inTime.split(":")

            var inTimeHoursInteger: Int = inTimeHours.toInt()

            if (inTime.contains(getString(R.string.PM))) {
                inTimeMinutesNumbers = inTimeMinutes.replace(getString(R.string.PM), "").trim().toInt()
                inTimeHoursInteger += 12
            } else {
                inTimeMinutesNumbers = inTimeMinutes.replace(getString(R.string.AM), "").trim().toInt()
                if (inTimeHours.toInt() == 12) {
                    inTimeHoursInteger -= 12
                }
            }

            inTimeBool = inTimeHoursInteger != i || inTimeMinutesNumbers != i2
        }

        outTimePickerEdit?.setOnTimeChangedListener { _, i, i2 ->
            val outTimeMinutesNumbers: Int
            val (outTimeHours, outTimeMinutes) = outTime.split(":")
            var outTimeHoursInteger: Int = outTimeHours.toInt()

            if (outTime.contains(getString(R.string.PM))) {
                outTimeMinutesNumbers = outTimeMinutes.replace(getString(R.string.PM), "").trim().toInt()
                outTimeHoursInteger += 12
            } else {
                outTimeMinutesNumbers = outTimeMinutes.replace(getString(R.string.AM), "").trim().toInt()
                if (outTimeHours.toInt() == 12) {
                    outTimeHoursInteger -= 12
                }
            }
            outTimeBool = outTimeHoursInteger != i || outTimeMinutesNumbers != i2
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exit()
            }
        })
    }

    fun exit() {
        hideKeyboard(requireActivity().findViewById(R.id.breakTimeEdit))
        if (inTimeBool || outTimeBool || breakTimeBool) {
            val alert = MaterialAlertDialogBuilder(requireContext(), AccentColor(requireContext()).alertTheme())
            alert.setTitle(getString(R.string.pending_changes))
            alert.setMessage(getString(R.string.you_have_pending_changes))
            alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                calculate(idMap, day)
                Toast.makeText(requireContext(), getString(R.string.hour_is_updated), Toast.LENGTH_SHORT).show()
            }
            alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                activity?.supportFragmentManager?.popBackStack()
                Toast.makeText(requireContext(), getString(R.string.hour_was_not_updated), Toast.LENGTH_SHORT).show()
            }
            alert.show()
        }
        else {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    @SuppressLint("Range")
    private fun main() {

        val dbHandler = DBHelper(requireContext(), null)

        val timePickerInTime = activity?.findViewById<TimePicker>(R.id.timePickerInTimeEdit)
        val timePickerOutTime = activity?.findViewById<TimePicker>(R.id.timePickerOutTimeEdit)
        val breakTimeEditText = activity?.findViewById<EditText>(R.id.breakTimeEdit)
        val saveButton = activity?.findViewById<Button>(R.id.saveButton)

        val id = IdData(requireContext()).loadID()

        dataList.clear()
        val cursor = dbHandler.getAllRow(requireContext())
        cursor!!.moveToPosition(id.toInt())

        val map = HashMap<String, String>()
        while (cursor.position == id.toInt()) {

            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
            map["inTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
            map["outTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
            map["breakTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
            map["totalHours"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
            map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
            dataList.add(map)

            cursor.moveToNext()

        }
        idMap = map["id"].toString()
        inTime = map["inTime"].toString()
        breakTime = map["breakTime"].toString()
        outTime = map["outTime"].toString()
        day = map["date"].toString()

        val (inTimeHours, inTimeMinutes) = map["inTime"].toString().split(":")
        val (outTimeHours, outTimeMinutes) = map["outTime"].toString().split(":")

        var inTimeHoursInteger: Int = inTimeHours.toInt()
        var outTimeHoursInteger: Int = outTimeHours.toInt()

        val inTimeMinutesNumbers: Int
        val outTimeMinutesNumbers: Int

        if (map["inTime"].toString().contains(getString(R.string.PM))) {
            inTimeMinutesNumbers = inTimeMinutes.replace(getString(R.string.PM), "").trim().toInt()
            inTimeHoursInteger += 12
        } else {
            inTimeMinutesNumbers = inTimeMinutes.replace(getString(R.string.AM), "").trim().toInt()
            if (inTimeHours.toInt() == 12) {
                inTimeHoursInteger -= 12
            }
        }

        if (map["outTime"].toString().contains(getString(R.string.PM))) {
            outTimeMinutesNumbers = outTimeMinutes.replace(getString(R.string.PM), "").trim().toInt()
            outTimeHoursInteger += 12
        } else {
            outTimeMinutesNumbers = outTimeMinutes.replace(getString(R.string.AM), "").trim().toInt()
            if (outTimeHours.toInt() == 12) {
                outTimeHoursInteger -= 12
            }
        }

        timePickerInTime?.hour = inTimeHoursInteger
        timePickerInTime?.minute = inTimeMinutesNumbers
        timePickerOutTime?.hour = outTimeHoursInteger
        timePickerOutTime?.minute = outTimeMinutesNumbers
        breakTimeEditText?.setText(breakTime)

        val calculationType = CalculationType(requireContext())

        saveButton?.setOnClickListener {
            //vibration(vibrationData)

            if (calculationType.loadCalculationState()) {
                calculate(idMap, day)
            }
            else {
                calculateTime(idMap, day)
            }
        }
    }

    private fun calculateTime(idMap: String, day: String) {

        var inTimeTotal: String = ""
        var outTimeTotal : String = ""

        val timePickerInTime = requireActivity().findViewById<TimePicker>(R.id.timePickerInTimeEdit)
        val timePickerOutTime = requireActivity().findViewById<TimePicker>(R.id.timePickerOutTimeEdit)
        val infoTextView1 = requireActivity().findViewById<TextView>(R.id.infoTextView1)

        var inTimeMinutesEdit = timePickerInTime.minute.toString()
        val inTimeHoursEdit = timePickerInTime.hour.toString()
        var outTimeMinutesEdit = timePickerOutTime.minute.toString()
        val outTimeHoursEdit = timePickerOutTime.hour.toString()

        var diffHours = outTimeHoursEdit.toInt() - inTimeHoursEdit.toInt()
        var diffMinutes = (outTimeMinutesEdit.toInt() - inTimeMinutesEdit.toInt()).toString()

        if (diffMinutes.length == 1) {
            diffMinutes = "0$diffMinutes"
        }

        if (diffMinutes.toInt() < 0) {
            diffMinutes = (60 + diffMinutes.toInt()).toString()
            diffHours -= 1
        }

        if (diffHours < 0) {
            infoTextView1?.text = "In time can not be later than out time"
        }
        else if (diffHours == 0 && diffMinutes.toInt() == 0) {
            infoTextView1?.text = "In time and out time can not be the same"
        }
        else {

            when {
                inTimeHoursEdit.toInt() > 12 -> {
                    val inTime = inTimeHoursEdit.toInt() - 12
                    val amOrPm = getString(R.string.PM)
                    inTimeTotal = "$inTime:$inTimeMinutesEdit $amOrPm"
                }
                inTimeHoursEdit.toInt() == 0 -> {
                    val inTime = 12
                    val amOrPm = getString(R.string.AM)
                    inTimeTotal = "$inTime:$inTimeMinutesEdit $amOrPm"
                }
                else -> {
                    val amOrPm = getString(R.string.AM)
                    inTimeTotal = "$inTimeHoursEdit:$inTimeMinutesEdit $amOrPm"
                }
            }
            when {
                outTimeHoursEdit.toInt() > 12 -> {
                    val outTime = outTimeHoursEdit.toInt() - 12
                    val amOrPm = getString(R.string.PM)
                    outTimeTotal = "$outTime:$outTimeMinutesEdit $amOrPm"
                }
                outTimeHoursEdit.toInt() == 0 -> {
                    val outTime = 12
                    val amOrPm = getString(R.string.AM)
                    outTimeTotal = "$outTime:$outTimeMinutesEdit $amOrPm"
                }
                else -> {
                    val amOrPm = getString(R.string.AM)
                    outTimeTotal = "$outTimeHoursEdit:$outTimeMinutesEdit $amOrPm"
                }
            }

            val breakTime = activity?.findViewById<TextInputEditText>(R.id.breakTimeEdit)
            if (breakTime?.text != null && breakTime.text.toString() != "") {

                var withBreak = (diffMinutes.toInt() - breakTime.text.toString().toInt()).toString()
                var hoursWithBreak = diffHours
                if (withBreak.toInt() < 0) {
                    hoursWithBreak -= 1
                    withBreak = (withBreak.toInt() + 60).toString()
                }

                Toast.makeText(requireContext(), withBreak, Toast.LENGTH_SHORT).show()

                if (diffHours < 0) {
                    infoTextView1!!.text = "The entered break time is too big"
                } else {
                    if (withBreak.length == 1) {
                        withBreak = "0$withBreak"
                    }

                    savingHours(
                        idMap, "$hoursWithBreak:$withBreak",
                        inTimeTotal,
                        outTimeTotal,
                        breakTime.text.toString(),
                        day
                    )
                    infoTextView1!!.text =
                        "Total Hours: " + "$diffHours:$diffMinutes\nTotal Hours With Break: $hoursWithBreak:$withBreak"
                }
            }
            else {
                savingHours(
                    idMap, "$diffHours:$diffMinutes",
                    inTimeTotal,
                    outTimeTotal,
                    "0", day)
                infoTextView1?.text = "Total Hours: " + diffHours.toString() + ":" + diffMinutes
            }
        }
    }

    private fun calculate(idMap: String, day: String) {

        val timePickerInTime = requireActivity().findViewById<TimePicker>(R.id.timePickerInTimeEdit)
        val timePickerOutTime = requireActivity().findViewById<TimePicker>(R.id.timePickerOutTimeEdit)
        val infoTextView1 = requireActivity().findViewById<TextView>(R.id.infoTextView1)

        var inTimeMinutesEdit = timePickerInTime.minute.toString()
        val inTimeHoursEdit = timePickerInTime.hour.toString()
        var outTimeMinutesEdit = timePickerOutTime.minute.toString()
        val outTimeHoursEdit = timePickerOutTime.hour.toString()

        if (inTimeMinutesEdit.length == 1) {
            inTimeMinutesEdit = "0${inTimeMinutesEdit.toInt()}"
        }

        if (outTimeMinutesEdit.length == 1) {
            outTimeMinutesEdit = "0$outTimeMinutesEdit"
        }
        val inTimeTotal: String
        val outTimeTotal: String

        var minutesDecimal: Double = (outTimeMinutesEdit.toInt() - inTimeMinutesEdit.toInt()) / 60.0
        minutesDecimal = minutesDecimal.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
        var minutesWithoutFirstDecimal = minutesDecimal.toString().substring(2)
        if (minutesDecimal < 0) {
            minutesWithoutFirstDecimal = (1.0 - minutesWithoutFirstDecimal.toDouble()).toString()
            minutesWithoutFirstDecimal = minutesWithoutFirstDecimal.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString()
            minutesWithoutFirstDecimal = minutesWithoutFirstDecimal.substring(2)
        }
        var hoursDifference = outTimeHoursEdit.toInt() - inTimeHoursEdit.toInt()
        if ("$hoursDifference.$minutesWithoutFirstDecimal".toDouble() == 0.0) {
            infoTextView1?.text = getString(R.string.in_time_and_out_time_cant_be_the_same)
        } else if (timePickerInTime!!.hour >= 0 && timePickerOutTime!!.hour <= 12 && hoursDifference < 0) {
            infoTextView1!!.text = getString(R.string.in_time_can_not_be_greater_than_out_time)
        } else if (timePickerInTime.hour >= 12 && timePickerOutTime!!.hour <= 24 && hoursDifference < 0) {
            infoTextView1!!.text = getString(R.string.in_time_can_not_be_greater_than_out_time)
        } else {
            if (minutesDecimal < 0) {
                hoursDifference -= 1
            }
            if (hoursDifference < 0) {
                hoursDifference += 24
            }
            when {
                inTimeHoursEdit.toInt() > 12 -> {
                    val inTime = inTimeHoursEdit.toInt() - 12
                    val amOrPm = getString(R.string.PM)
                    inTimeTotal = "$inTime:$inTimeMinutesEdit $amOrPm"
                }
                inTimeHoursEdit.toInt() == 0 -> {
                    val inTime = 12
                    val amOrPm = getString(R.string.AM)
                    inTimeTotal = "$inTime:$inTimeMinutesEdit $amOrPm"
                }
                else -> {
                    val amOrPm = getString(R.string.AM)
                    inTimeTotal = "$inTimeHoursEdit:$inTimeMinutesEdit $amOrPm"
                }
            }
            when {
                outTimeHoursEdit.toInt() > 12 -> {
                    val outTime = outTimeHoursEdit.toInt() - 12
                    val amOrPm = getString(R.string.PM)
                    outTimeTotal = "$outTime:$outTimeMinutesEdit $amOrPm"
                }
                outTimeHoursEdit.toInt() == 0 -> {
                    val outTime = 12
                    val amOrPm = getString(R.string.AM)
                    outTimeTotal = "$outTime:$outTimeMinutesEdit $amOrPm"
                }
                else -> {
                    val amOrPm = getString(R.string.AM)
                    outTimeTotal = "$outTimeHoursEdit:$outTimeMinutesEdit $amOrPm"
                }
            }

            var breakTimeNumber = 0.0
            val totalHours = "$hoursDifference.$minutesWithoutFirstDecimal".toDouble()

            val breakTime = activity?.findViewById<TextInputEditText>(R.id.breakTimeEdit)
            if (breakTime?.text != null && breakTime.text.toString() != "") {

                breakTimeNumber = breakTime.text.toString().toDouble() / 60
                val totalHoursWithBreak = (totalHours - breakTimeNumber).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString()

                savingHours(idMap, totalHours.toString(), inTimeTotal, outTimeTotal, breakTime.text.toString(), day)
                infoTextView1!!.text =  "Total Hours: " +"$hoursDifference.$minutesWithoutFirstDecimal\nTotal Hours With Break: $totalHoursWithBreak"
            }
            else {
                savingHours(idMap, totalHours.toString(), inTimeTotal, outTimeTotal, "0", day)
                infoTextView1!!.text =  "Total Hours: " +"$hoursDifference.$minutesWithoutFirstDecimal"
            }

        }
    }

    private fun savingHours(id: String, totalHours: String, inTimeTotal: String, outTimeTotal: String, breakTime: String, dayOfWeek: String) {

        val dbHandler = DBHelper(requireContext(), null)

        dbHandler.update(id, inTimeTotal, outTimeTotal, totalHours.toString(), dayOfWeek, breakTime)

        activity?.supportFragmentManager?.popBackStack()
    }

    private fun hideKeyboard(breakEditText: TextInputEditText?) {
        val inputManager: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = activity?.currentFocus

        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            if (breakEditText!!.hasFocus()) {
                breakEditText.clearFocus()
            }
        }
    }
}