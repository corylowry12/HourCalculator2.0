package com.cory.hourcalculator.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.DelicateCoroutinesApi
import java.lang.Exception
import java.lang.NumberFormatException
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@DelicateCoroutinesApi
class EditHours : Fragment() {

    private val dataList = ArrayList<HashMap<String, String>>()

    private lateinit var idMap: String
    private lateinit var day: String

    private lateinit var undoHoursData: UndoHoursData

    private lateinit var inTime: String
    private lateinit var outTime: String
    private lateinit var breakTime: String

    private var inTimeBool = false
    private var outTimeBool = false
    var breakTimeBool = false

    var themeSelection = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                activity?.setTheme(R.style.Theme_DarkTheme)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
                themeSelection = false
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                        themeSelection = false
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(AccentColor(requireContext()).followSystemTheme(requireContext()))
                        themeSelection = true
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                        themeSelection = true
                    }
                }
            }
        }

        val accentColor = AccentColor(requireContext())
        val followSystemVersion = FollowSystemVersion(requireContext())

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
                if (!followSystemVersion.loadSystemColor()) {
                    activity?.theme?.applyStyle(R.style.system_accent, true)
                }
                else {
                    if (themeSelection) {
                        activity?.theme?.applyStyle(R.style.system_accent_google, true)
                    }
                    else {
                        activity?.theme?.applyStyle(R.style.system_accent_google_light, true)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_edit_hours, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        undoHoursData = UndoHoursData(requireContext())
        val materialToolbarEdit =
            activity?.findViewById<MaterialToolbar>(R.id.materialToolBarEditFragment)

        materialToolbarEdit?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            exit()
        }

        materialToolbarEdit?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> {
                    Vibrate().vibration(requireContext())
                    val alert = MaterialAlertDialogBuilder(
                        requireContext(),
                        AccentColor(requireContext()).alertTheme()
                    )
                    alert.setCancelable(false)
                    alert.setTitle(getString(R.string.delete))
                    alert.setMessage(getString(R.string.would_you_like_to_delete_history))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                        Vibrate().vibration(requireContext())
                        val dbHandler = DBHelper(requireContext(), null)
                        dbHandler.deleteRow(idMap)

                        val runnable = Runnable {
                            (context as MainActivity).update()
                        }
                        MainActivity().runOnUiThread(runnable)
                        activity?.supportFragmentManager?.popBackStack()

                        Snackbar().snackbar(requireContext(), requireView())
                    }
                    alert.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                        Vibrate().vibration(requireContext())
                        dialog.dismiss()
                    }
                    alert.show()
                    true
                }
                else -> false
            }
        }

        try {
            main()

            val dateChip = activity?.findViewById<Chip>(R.id.dateChip)

            dateChip?.setOnClickListener {
                Vibrate().vibration(requireContext())
                val datePicker = DatePickerDialog(
                    requireContext(),
                    AccentColor(requireContext()).dateDialogTheme(requireContext())
                )
                datePicker.setCancelable(false)

                val (month1, day2, year1) = day.split("/")
                val year2 = year1.dropLast(12)

                val dateFormatter = SimpleDateFormat("mm")
                val month2 = month1.format(dateFormatter)

                datePicker.updateDate(year2.toInt(), month2.toInt() - 1, day2.toInt())

                datePicker.datePicker.maxDate = System.currentTimeMillis()

                datePicker.datePicker.setOnDateChangedListener { _, _, _, _ ->
                    Vibrate().vibration(requireContext())
                }

                datePicker.show()

                val positiveButton = datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
                val neutralButton = datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                positiveButton.setOnClickListener {
                    Vibrate().vibration(requireContext())

                    val year = datePicker.datePicker.year
                    val month = datePicker.datePicker.month
                    val day3 = datePicker.datePicker.dayOfMonth

                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, day3)
                    val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a")
                    val day4 = calendar.timeInMillis
                    calendar.timeInMillis

                    val date2 = simpleDateFormat.format(day4)
                    day = date2

                    dateChip.text = date2

                    datePicker.dismiss()
                }

                neutralButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                    datePicker.dismiss()
                }
            }

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

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    breakTimeBool = s.toString() != "" && s.toString() != breakTime
                }
            })

            inTimePickerEdit?.setOnTimeChangedListener { _, i, i2 ->
                Vibrate().vibration(requireContext())
                val inTimeMinutesNumbers: Int

                val (inTimeHours, inTimeMinutes) = inTime.split(":")

                var inTimeHoursInteger: Int = inTimeHours.toInt()

                if (inTime.contains(getString(R.string.PM))) {
                    inTimeMinutesNumbers =
                        inTimeMinutes.replace(getString(R.string.PM), "").trim().toInt()
                    inTimeHoursInteger += 12
                } else {
                    inTimeMinutesNumbers =
                        inTimeMinutes.replace(getString(R.string.AM), "").trim().toInt()
                    if (inTimeHours.toInt() == 12) {
                        inTimeHoursInteger -= 12
                    }
                }

                inTimeBool = inTimeHoursInteger != i || inTimeMinutesNumbers != i2
            }

            outTimePickerEdit?.setOnTimeChangedListener { _, i, i2 ->
                Vibrate().vibration(requireContext())
                val outTimeMinutesNumbers: Int
                val (outTimeHours, outTimeMinutes) = outTime.split(":")
                var outTimeHoursInteger: Int = outTimeHours.toInt()

                if (outTime.contains(getString(R.string.PM))) {
                    outTimeMinutesNumbers =
                        outTimeMinutes.replace(getString(R.string.PM), "").trim().toInt()
                    outTimeHoursInteger += 12
                } else {
                    outTimeMinutesNumbers =
                        outTimeMinutes.replace(getString(R.string.AM), "").trim().toInt()
                    if (outTimeHours.toInt() == 12) {
                        outTimeHoursInteger -= 12
                    }
                }
                outTimeBool = outTimeHoursInteger != i || outTimeMinutesNumbers != i2
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            activity?.supportFragmentManager?.popBackStack()
            Toast.makeText(requireContext(), getString(R.string.there_was_an_error), Toast.LENGTH_SHORT).show()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exit()
                }
            })
    }

    fun exit() {
        hideKeyboard(requireActivity().findViewById(R.id.breakTimeEdit))
        if (inTimeBool || outTimeBool || breakTimeBool) {
            val alert = MaterialAlertDialogBuilder(
                requireContext(),
                AccentColor(requireContext()).alertTheme()
            )
            alert.setCancelable(false)
            alert.setTitle(getString(R.string.pending_changes))
            alert.setMessage(getString(R.string.you_have_pending_changes))
            alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                Vibrate().vibration(requireContext())
                calculate(idMap, day)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.hour_is_updated),
                    Toast.LENGTH_SHORT
                ).show()
            }
            alert.setNegativeButton(getString(R.string.no)) { _, _ ->
                Vibrate().vibration(requireContext())
                activity?.supportFragmentManager?.popBackStack()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.hour_was_not_updated),
                    Toast.LENGTH_SHORT
                ).show()
            }
            alert.show()
        } else {
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
        val dateChip = activity?.findViewById<Chip>(R.id.dateChip)

        val id = IdData(requireContext()).loadID()

        dataList.clear()
        val cursor = dbHandler.getRow(id.toString())
        cursor.moveToFirst()
        val map = HashMap<String, String>()

        map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
        map["inTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
        map["outTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
        map["breakTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
        map["totalHours"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
        map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
        dataList.add(map)

        idMap = map["id"].toString()
        inTime = map["inTime"].toString()
        breakTime = map["breakTime"].toString()
        outTime = map["outTime"].toString()
        val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
        val dateString = formatter.format(map["date"]!!.toLong())
        day = dateString
        undoHoursData.setID(map["id"]!!.toInt())
        undoHoursData.setDate(map["date"]!!.toLong())
        undoHoursData.setInTime(inTime)
        undoHoursData.setOutTime(outTime)
        undoHoursData.setBreakTime(breakTime)
        undoHoursData.setTotalHours(map["totalHours"].toString())

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
            outTimeMinutesNumbers =
                outTimeMinutes.replace(getString(R.string.PM), "").trim().toInt()
            outTimeHoursInteger += 12
        } else {
            outTimeMinutesNumbers =
                outTimeMinutes.replace(getString(R.string.AM), "").trim().toInt()
            if (outTimeHours.toInt() == 12) {
                outTimeHoursInteger -= 12
            }
        }

        timePickerInTime?.hour = inTimeHoursInteger
        timePickerInTime?.minute = inTimeMinutesNumbers
        timePickerOutTime?.hour = outTimeHoursInteger
        timePickerOutTime?.minute = outTimeMinutesNumbers
        breakTimeEditText?.setText(breakTime)
        if (!DialogData(requireContext()).loadDateDialogState()) {
            Handler(Looper.getMainLooper()).postDelayed({

                dateChip?.text = day

            }, 5000)
            DialogData(requireContext()).setDateDialogState(true)
        } else {
            dateChip?.text = day
        }

        val calculationType = CalculationType(requireContext())

        saveButton?.setOnClickListener {
            Vibrate().vibration(requireContext())

            if (calculationType.loadCalculationState()) {
                calculate(idMap, day)
            } else {
                calculateTime(idMap, day)
            }
        }
    }

    private fun calculateTime(idMap: String, day: String) {

        val inTimeTotal: String
        val outTimeTotal: String

        val timePickerInTime = requireActivity().findViewById<TimePicker>(R.id.timePickerInTimeEdit)
        val timePickerOutTime =
            requireActivity().findViewById<TimePicker>(R.id.timePickerOutTimeEdit)
        val infoTextView1 = requireActivity().findViewById<TextView>(R.id.infoTextView1)

        var inTimeMinutesEdit = timePickerInTime.minute.toString()
        val inTimeHoursEdit = timePickerInTime.hour.toString()
        var outTimeMinutesEdit = timePickerOutTime.minute.toString()
        val outTimeHoursEdit = timePickerOutTime.hour.toString()

        if (inTimeMinutesEdit.length == 1) {
            inTimeMinutesEdit = "0$inTimeMinutesEdit"
        }

        if (outTimeMinutesEdit.length == 1) {
            outTimeMinutesEdit = "0$outTimeMinutesEdit"
        }

        var diffHours = outTimeHoursEdit.toInt() - inTimeHoursEdit.toInt()
        var diffMinutes = (outTimeMinutesEdit.toInt() - inTimeMinutesEdit.toInt()).toString()

        if (diffMinutes.length == 1) {
            diffMinutes = "0$diffMinutes"
        }

        if (diffMinutes.toInt() < 0) {
            diffMinutes = (60 + diffMinutes.toInt()).toString()
            diffHours -= 1
        }

        if (diffHours == 0 && diffMinutes.toInt() == 0) {
            infoTextView1?.text = getString(R.string.in_time_and_out_time_can_not_be_the_same)
        } else {
            if (diffHours < 0) {
                diffHours += 24
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

            val breakTime = activity?.findViewById<TextInputEditText>(R.id.breakTimeEdit)
            if (breakTime?.text != null && breakTime.text.toString() != "") {
                if (!breakTimeNumeric(breakTime)) {
                    infoTextView1!!.text = getString(R.string.error_with_break_time_must_be_numbers_only)
                }
                else {
                    var withBreak =
                        (diffMinutes.toInt() - breakTime.text.toString().toInt()).toString()
                    var hoursWithBreak = diffHours
                    if (withBreak.toInt() < 0) {
                        hoursWithBreak -= 1
                        withBreak = (withBreak.toInt() + 60).toString()
                    }

                    if (diffHours < 0) {
                        infoTextView1!!.text = getString(R.string.the_entered_break_time_is_too_big)
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
                    }
                }
            } else {
                savingHours(
                    idMap, "$diffHours:$diffMinutes",
                    inTimeTotal,
                    outTimeTotal,
                    "0", day
                )

            }
        }
    }

    private fun calculate(idMap: String, day: String) {

        val timePickerInTime = requireActivity().findViewById<TimePicker>(R.id.timePickerInTimeEdit)
        val timePickerOutTime =
            requireActivity().findViewById<TimePicker>(R.id.timePickerOutTimeEdit)
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
        var hoursDifference = outTimeHoursEdit.toInt() - inTimeHoursEdit.toInt()
        if ("$hoursDifference.$minutesWithoutFirstDecimal".toDouble() == 0.0) {
            infoTextView1?.text = getString(R.string.in_time_and_out_time_can_not_be_the_same)
        }
        else {
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

            val breakTimeNumber: Double
            val totalHours = "$hoursDifference.$minutesWithoutFirstDecimal".toDouble()

            val breakTime = activity?.findViewById<TextInputEditText>(R.id.breakTimeEdit)
            if (breakTime?.text != null && breakTime.text.toString() != "") {
                if (!breakTimeNumeric(breakTime)) {
                    infoTextView1!!.text = getString(R.string.error_with_break_time_must_be_numbers_only)
                }
                else {
                    breakTimeNumber = breakTime.text.toString().toDouble() / 60
                    val totalHoursWithBreak = (totalHours - breakTimeNumber).toBigDecimal()
                        .setScale(2, RoundingMode.HALF_EVEN).toString()

                    savingHours(
                        idMap,
                        totalHoursWithBreak,
                        inTimeTotal,
                        outTimeTotal,
                        breakTime.text.toString(),
                        day
                    )
                }
            } else {
                savingHours(idMap, totalHours.toString(), inTimeTotal, outTimeTotal, "0", day)
            }

        }
    }

    private fun savingHours(
        id: String,
        totalHours: String,
        inTimeTotal: String,
        outTimeTotal: String,
        breakTime: String,
        dayOfWeek: String
    ) {

        val dbHandler = DBHelper(requireContext(), null)

        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
        val date = formatter.parse(dayOfWeek)
        calendar.time = date!!
        val timeInMillis = calendar.timeInMillis

        dbHandler.update(id, inTimeTotal, outTimeTotal, totalHours, timeInMillis, breakTime)

        activity?.supportFragmentManager?.popBackStack()
    }

    private fun breakTimeNumeric(breakTime: TextInputEditText) : Boolean {
        return try {
            breakTime.text.toString().toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun hideKeyboard(breakEditText: TextInputEditText?) {
        val inputManager: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = activity?.currentFocus

        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(
                focusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            if (breakEditText!!.hasFocus()) {
                breakEditText.clearFocus()
            }
        }
    }
}