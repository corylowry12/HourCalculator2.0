package com.cory.hourcalculator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.IdData
import com.cory.hourcalculator.database.DBHelper
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_hours, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main()
    }

    fun main() {

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
            map["intime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
            map["out"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
            map["break"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
            map["total"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
            map["day"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
            dataList.add(map)

            cursor.moveToNext()

        }
        idMap = map["id"].toString()
        inTime = map["intime"].toString()
        breakTime = map["break"].toString()
        outTime = map["out"].toString()
        day = map["day"].toString()

        val (inTimeHours, inTimeMinutes) = map["intime"].toString().split(":")
        val (outTimeHours, outTimeMinutes) = map["out"].toString().split(":")

        var inTimeHoursInteger: Int = inTimeHours.toInt()
        var outTimeHoursInteger: Int = outTimeHours.toInt()

        val inTimeMinutesNumbers: Int
        val outTimeMinutesNumbers: Int

        if (map["intime"].toString().contains("pm")) {
            inTimeMinutesNumbers = inTimeMinutes.replace("pm", "").trim().toInt()
            inTimeHoursInteger += 12
        } else {
            inTimeMinutesNumbers = inTimeMinutes.replace("am", "").trim().toInt()
            if (inTimeHours.toInt() == 12) {
                inTimeHoursInteger -= 12
            }
        }

        if (map["out"].toString().contains("pm")) {
            outTimeMinutesNumbers = outTimeMinutes.replace("pm", "").trim().toInt()
            outTimeHoursInteger += 12
        } else {
            outTimeMinutesNumbers = outTimeMinutes.replace("am", "").trim().toInt()
            if (outTimeHours.toInt() == 12) {
                outTimeHoursInteger -= 12
            }
        }

        timePickerInTime?.hour = inTimeHoursInteger
        timePickerInTime?.minute = inTimeMinutesNumbers
        timePickerOutTime?.hour = outTimeHoursInteger
        timePickerOutTime?.minute = outTimeMinutesNumbers
        breakTimeEditText?.setText(breakTime)

        saveButton?.setOnClickListener {
            //vibration(vibrationData)
            calculate(idMap, day)
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
                inTimeHoursEdit.toInt() > 12 -> {
                    val inTime = inTimeHoursEdit.toInt() - 12
                    val amOrPm = "pm"
                    inTimeTotal = "$inTime:$inTimeMinutesEdit $amOrPm"
                }
                inTimeHoursEdit.toInt() == 0 -> {
                    val inTime = 12
                    val amOrPm = "am"
                    inTimeTotal = "$inTime:$inTimeMinutesEdit $amOrPm"
                }
                else -> {
                    val amOrPm = "am"
                    inTimeTotal = "$inTimeHoursEdit:$inTimeMinutesEdit $amOrPm"
                }
            }
            when {
                outTimeHoursEdit.toInt() > 12 -> {
                    val outTime = outTimeHoursEdit.toInt() - 12
                    val amOrPm = "pm"
                    outTimeTotal = "$outTime:$outTimeMinutesEdit $amOrPm"
                }
                outTimeHoursEdit.toInt() == 0 -> {
                    val outTime = 12
                    val amOrPm = "am"
                    outTimeTotal = "$outTime:$outTimeMinutesEdit $amOrPm"
                }
                else -> {
                    val amOrPm = "am"
                    outTimeTotal = "$outTimeHoursEdit:$outTimeMinutesEdit $amOrPm"
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

                savingHours(idMap, totalHours, inTimeTotal, outTimeTotal, breakTime.text.toString(), day)
                infoTextView1!!.text =  "Total Hours: " +"$hoursDifference.$minutesWithoutFirstDecimal\nTotal Hours With Break: $totalHoursWithBreak"
            }
            else {
                savingHours(idMap, totalHours, inTimeTotal, outTimeTotal, "0", day)
                infoTextView1!!.text =  "Total Hours: " +"$hoursDifference.$minutesWithoutFirstDecimal"
            }

        }
    }

    private fun savingHours(id: String, totalHours: Double, inTimeTotal: String, outTimeTotal: String, breakTime: String, dayOfWeek: String) {

        val dbHandler = DBHelper(requireContext(), null)

        dbHandler.update(id, inTimeTotal, outTimeTotal, totalHours.toString(), dayOfWeek, breakTime)

        activity?.supportFragmentManager?.popBackStack()
    }
}