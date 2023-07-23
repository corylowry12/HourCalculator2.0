package com.cory.hourcalculator.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class EditHours : Fragment() {

    private lateinit var timePickerInTime : TimePicker
    private lateinit var timePickerOutTime : TimePicker
    private lateinit var infoTextView1 : TextView

    var historyDateEntry : Long = 0

    private val dataList = ArrayList<HashMap<String, String>>()

    private lateinit var idMap: String
    private lateinit var day: String

    private lateinit var undoHoursData: UndoHoursData

    private lateinit var inTime: String
    private lateinit var outTime: String
    lateinit var breakTime: String

    private var inTimeBool = false
    private var outTimeBool = false
    var breakTimeBool = false
    private var dateChangedBool = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                //activity?.setTheme(R.style.Theme_DarkTheme)
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_edit_hours, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val breakTimeEditText = activity?.findViewById<TextInputEditText>(R.id.breakTimeEdit)

        val constraintLayout =
            requireActivity().findViewById<CoordinatorLayout>(R.id.editHoursCoordinatorLayout)

        constraintLayout?.setOnClickListener {
            Vibrate().vibration(requireContext())
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.hideSoftInputFromWindow(constraintLayout.windowToken, 0)
                breakTimeEditText?.clearFocus()
            }
        }

        activity?.window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        when {
            DarkThemeData(requireContext()).loadDarkModeState() == 0 -> {
                activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            DarkThemeData(requireContext()).loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }
        }

        undoHoursData = UndoHoursData(requireContext())
        val materialToolbarEdit =
            activity?.findViewById<MaterialToolbar>(R.id.materialToolBarEditFragment)

        updateCustomColor()

        materialToolbarEdit?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            exit()
        }

        materialToolbarEdit?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> {
                    Vibrate().vibration(requireContext())
                    val dialog = BottomSheetDialog(requireContext())
                    val deleteAllLayout = layoutInflater.inflate(R.layout.delete_entry_bottom_sheet, null)

                    val yesButton = deleteAllLayout.findViewById<Button>(R.id.yesButton)
                    val noButton = deleteAllLayout.findViewById<Button>(R.id.noButton)
                    val infoCardView = deleteAllLayout.findViewById<MaterialCardView>(R.id.infoCardView)

                        yesButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                        noButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                        infoCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

                    dialog.setContentView(deleteAllLayout)
                    dialog.setCancelable(true)

                    if (resources.getBoolean(R.bool.isTablet)) {
                        val bottomSheet =
                            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.isDraggable = false
                    }
                    yesButton.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        val dbHandler = DBHelper(requireContext(), null)
                        dbHandler.deleteRow(idMap)

                        val runnable = Runnable {
                            (context as MainActivity).update()
                        }
                        MainActivity().runOnUiThread(runnable)
                        activity?.supportFragmentManager?.popBackStack()

                        val showSnackbar = Runnable {
                            (context as MainActivity).showSnackbar()

                        }

                        MainActivity().runOnUiThread(showSnackbar)

                        dialog.dismiss()
                    }
                    noButton.setOnClickListener {
                        Vibrate().vibration(requireContext())
                        dialog.dismiss()
                    }

                    dialog.show()
                    true
                }
                else -> false
            }
        }

        timePickerInTime = view.findViewById(R.id.timePickerInTimeEdit)
        timePickerOutTime =
            view.findViewById(R.id.timePickerOutTimeEdit)
        infoTextView1 = view.findViewById(R.id.outputTextViewEdit)

        val timeEditHourData = TimeEditHourData(requireContext())
        timeEditHourData.setInTimeBool(false)
        timeEditHourData.setOutTimeBool(false)
        timeEditHourData.setBreakTimeBool(false)
        timeEditHourData.setDateBool(false)

        try {
            main()

            val dateChip = activity?.findViewById<Chip>(R.id.dateChipEdit)

            dateChip?.setOnLongClickListener {
                Vibrate().vibrateOnLongClick(requireContext())
                val calendar = Calendar.getInstance()
                val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a")
                val day4 = calendar.timeInMillis
                calendar.timeInMillis

                val date2 = simpleDateFormat.format(day4)
                day = date2

                dateChip.text = date2
                dateChangedBool = true
                return@setOnLongClickListener true
            }

            dateChip?.setOnClickListener {
                Vibrate().vibration(requireContext())
                val datePicker = DatePickerDialog(
                    requireContext(),
                    DateDialogTheme().dateDialogTheme(requireContext())
                )
                datePicker.setCancelable(false)

                val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US)
                val dateString = formatter.format(historyDateEntry)

                val (month1, day2, year1) = day.split("/")
                val year2 = year1.dropLast(12)

                val dateFormatter = SimpleDateFormat("mm")
                val month2 = month1.format(dateFormatter)

                datePicker.updateDate(year2.toInt(), month2.toInt() - 1, day2.toInt())

                datePicker.datePicker.maxDate = System.currentTimeMillis()

                datePicker.datePicker.setOnDateChangedListener { _, _, _, _ ->
                    Vibrate().vibration(requireContext())
                    if (datePicker.datePicker.month == month2.toInt() || datePicker.datePicker.dayOfMonth != day2.toInt() || datePicker.datePicker.year != year2.toInt()) {
                        dateChangedBool = true
                        timeEditHourData.setDateBool(true)
                    }
                    else {
                        dateChangedBool = false
                        timeEditHourData.setDateBool(false)
                    }
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
                    dateChangedBool = true

                    datePicker.dismiss()
                }

                neutralButton.setOnClickListener {
                    Vibrate().vibration(requireContext())
                    if (dateChip.text == dateString) {
                        dateChangedBool = false
                        timeEditHourData.setDateBool(false)
                    }
                    else {
                        dateChangedBool = true
                        timeEditHourData.setDateBool(true)
                    }
                    datePicker.dismiss()
                }
            }

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
                    TimeEditHourData(requireContext()).setBreakTimeBool(s.toString() != "" && s.toString() != breakTime)
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
                    TimeEditHourData(requireContext()).setBreakTimeBool(s.toString() != "" && s.toString() != breakTime)
                }
            })

            timePickerInTime.setOnTimeChangedListener { _, i, i2 ->
                Vibrate().vibrationTimePickers(requireContext())
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
                TimeEditHourData(requireContext()).setInTimeBool(inTimeHoursInteger != i || inTimeMinutesNumbers != i2)
            }

            timePickerOutTime.setOnTimeChangedListener { _, i, i2 ->
                Vibrate().vibrationTimePickers(requireContext())
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
                TimeEditHourData(requireContext()).setOutTimeBool(outTimeHoursInteger != i || outTimeMinutesNumbers != i2)
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
                    if (breakTimeEditText!!.hasFocus()) {
                        breakTimeEditText.clearFocus()
                    } else {
                        exit()
                    }
                }
            })
    }

    fun historyTabClicked(context: Context) {
        val timeEditHourData = TimeEditHourData(context)

        if (timeEditHourData.loadInTimeBool() || timeEditHourData.loadOutTimeBool() || timeEditHourData.loadBreakTimeBool() || timeEditHourData.loadDateBool()) {
            val dialog = BottomSheetDialog(requireContext())
            val deleteAllLayout = layoutInflater.inflate(R.layout.pending_changes_bottom_sheet, null)
            dialog.setContentView(deleteAllLayout)
            dialog.setCancelable(false)

            val infoCardView =
                deleteAllLayout.findViewById<MaterialCardView>(R.id.infoCardView)
            val yesButton = deleteAllLayout.findViewById<Button>(R.id.yesButton)
            val noButton = deleteAllLayout.findViewById<Button>(R.id.noButton)


            infoCardView.setCardBackgroundColor(
                Color.parseColor(
                    CustomColorGenerator(requireContext()).generateCardColor()
                )
            )
            yesButton.setBackgroundColor(
                Color.parseColor(
                    CustomColorGenerator(
                        requireContext()
                    ).generateCustomColorPrimary()
                )
            )
            noButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

            yesButton.setOnClickListener {
                Vibrate().vibration(context)
                dialog.dismiss()
                val timePickerInTime2 = (context as AppCompatActivity).findViewById<TimePicker>(R.id.timePickerInTimeEdit)
                val timePickerOutTime2 = (context).findViewById<TimePicker>(R.id.timePickerOutTimeEdit)

                var inTimeMinutesEdit = timePickerInTime2?.minute.toString()
                val inTimeHoursEdit = timePickerInTime2?.hour.toString()
                var outTimeMinutesEdit = timePickerOutTime2?.minute.toString()
                val outTimeHoursEdit = timePickerOutTime2?.hour.toString()

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
                    infoTextView1.text = getString(R.string.in_time_and_out_time_can_not_be_the_same)
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
                            val amOrPm = context.getString(R.string.PM)
                            inTimeTotal = "$inTime:$inTimeMinutesEdit $amOrPm"
                        }
                        inTimeHoursEdit.toInt() == 0 -> {
                            val inTime = 12
                            val amOrPm = context.getString(R.string.AM)
                            inTimeTotal = "$inTime:$inTimeMinutesEdit $amOrPm"
                        }
                        else -> {
                            val amOrPm = context.getString(R.string.AM)
                            inTimeTotal = "$inTimeHoursEdit:$inTimeMinutesEdit $amOrPm"
                        }
                    }
                    when {
                        outTimeHoursEdit.toInt() > 12 -> {
                            val outTime = outTimeHoursEdit.toInt() - 12
                            val amOrPm = context.getString(R.string.PM)
                            outTimeTotal = "$outTime:$outTimeMinutesEdit $amOrPm"
                        }
                        outTimeHoursEdit.toInt() == 0 -> {
                            val outTime = 12
                            val amOrPm = context.getString(R.string.AM)
                            outTimeTotal = "$outTime:$outTimeMinutesEdit $amOrPm"
                        }
                        else -> {
                            val amOrPm = context.getString(R.string.AM)
                            outTimeTotal = "$outTimeHoursEdit:$outTimeMinutesEdit $amOrPm"
                        }
                    }

                    val breakTimeNumber: Double
                    val totalHours = "$hoursDifference.$minutesWithoutFirstDecimal".toDouble()

                    val breakTime = (context).findViewById<TextInputEditText>(R.id.breakTimeEdit)
                    if (breakTime?.text != null && breakTime.text.toString() != "") {
                        if (!breakTimeNumeric(breakTime)) {
                            infoTextView1.text = context.getString(R.string.error_with_break_time_must_be_numbers_only)
                        }
                        else {
                            breakTimeNumber = breakTime.text.toString().toDouble() / 60
                            val totalHoursWithBreak = (totalHours - breakTimeNumber).toBigDecimal()
                                .setScale(2, RoundingMode.HALF_EVEN).toString()

                            savingHoursHistoryTab(context,
                                timeEditHourData.loadIdMap(),
                                totalHoursWithBreak,
                                inTimeTotal,
                                outTimeTotal,
                                breakTime.text.toString(),
                                timeEditHourData.loadDate()
                            )
                        }
                    } else {
                        savingHoursHistoryTab(context, timeEditHourData.loadIdMap(), totalHours.toString(), inTimeTotal, outTimeTotal, "0", timeEditHourData.loadDate())
                    }

                }

            }
            noButton.setOnClickListener {
                Vibrate().vibration(context)
                dialog.dismiss()
                (context as AppCompatActivity).supportFragmentManager.popBackStack()
            }
            dialog.show()
        }
        else {
            (context as AppCompatActivity).supportFragmentManager.popBackStack()
        }
    }
    fun exit() {
        try {
            hideKeyboard(requireActivity().findViewById(R.id.breakTimeEdit))
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        if (inTimeBool || outTimeBool || breakTimeBool || dateChangedBool) {
            val dialog = BottomSheetDialog(requireContext())
            val deleteAllLayout = layoutInflater.inflate(R.layout.pending_changes_bottom_sheet, null)
            dialog.setContentView(deleteAllLayout)
            dialog.setCancelable(false)

            if (resources.getBoolean(R.bool.isTablet)) {
                val bottomSheet =
                    dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = false
            }
            val infoCardView =
                deleteAllLayout.findViewById<MaterialCardView>(R.id.infoCardView)
            val yesButton = deleteAllLayout.findViewById<Button>(R.id.yesButton)
            val noButton = deleteAllLayout.findViewById<Button>(R.id.noButton)


            infoCardView.setCardBackgroundColor(
                Color.parseColor(
                    CustomColorGenerator(requireContext()).generateCardColor()
                )
            )
            yesButton.setBackgroundColor(
                Color.parseColor(
                    CustomColorGenerator(
                        requireContext()
                    ).generateCustomColorPrimary()
                )
            )
            noButton.setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

            yesButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                dialog.dismiss()
                calculate(idMap, day, 0)
            }
            noButton.setOnClickListener {
                Vibrate().vibration(requireContext())
                dialog.dismiss()
                activity?.supportFragmentManager?.popBackStack()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.hour_was_not_updated),
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialog.show()
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
        val dateChip = activity?.findViewById<Chip>(R.id.dateChipEdit)

        val id = requireArguments().getInt("id") //IdData(requireContext()).loadID()

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
        historyDateEntry = map["date"]!!.toLong()
        dataList.add(map)

        idMap = map["id"].toString()
        TimeEditHourData(requireContext()).setIdMap(idMap)
        inTime = map["inTime"].toString()
        breakTime = map["breakTime"].toString()
        outTime = map["outTime"].toString()
        val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
        val dateString = formatter.format(map["date"]!!.toLong())
        day = dateString
        TimeEditHourData(requireContext()).setDate(day)
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
            if (inTimeHoursInteger != 12) {
                inTimeHoursInteger += 12
            }
        } else {
            inTimeMinutesNumbers = inTimeMinutes.replace(getString(R.string.AM), "").trim().toInt()
            if (inTimeHours.toInt() == 12) {
                inTimeHoursInteger -= 12
            }
        }

        if (map["outTime"].toString().contains(getString(R.string.PM))) {
            outTimeMinutesNumbers =
                outTimeMinutes.replace(getString(R.string.PM), "").trim().toInt()
            if (outTimeHoursInteger != 12) {
                outTimeHoursInteger += 12
            }
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

        val calculationTypeData = CalculationTypeData(requireContext())

        saveButton?.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (calculationTypeData.loadCalculationState()) {
                calculate(idMap, day, 0)
            } else {
                calculateTime(idMap, day, 0)
            }
        }

        saveButton?.setOnLongClickListener {
            Vibrate().vibrateOnLongClick(requireContext())
            if (LongPressCalculateButtonData(requireContext()).loadLongClick()) {
                if (calculationTypeData.loadCalculationState()) {
                    calculateTime(idMap, day, 1)
                } else {
                    calculate(idMap, day, 1)
                }
                return@setOnLongClickListener true
            }
            else {
                return@setOnLongClickListener false
            }
        }
    }

    private fun calculateTime(idMap: String, day: String, method: Int) {

        val inTimeTotal: String
        val outTimeTotal: String

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
            Vibrate().vibrateOnError(requireContext())
            infoTextView1.text = getString(R.string.in_time_and_out_time_can_not_be_the_same)
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
                    Vibrate().vibrateOnError(requireContext())
                    infoTextView1.text = getString(R.string.error_with_break_time_must_be_numbers_only)
                }
                else {
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

                    if (diffHours < 0) {
                        Vibrate().vibrateOnError(requireContext())
                        infoTextView1.text = getString(R.string.the_entered_break_time_is_too_big)
                    } else {
                        if (withBreak.length == 1) {
                            withBreak = "0$withBreak"
                        }

                        if (method == 1) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.hour_calculated_in_time_format),
                                Toast.LENGTH_SHORT
                            ).show()
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
                if (method == 1) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.hour_calculated_in_time_format),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                savingHours(
                    idMap, "$diffHours:$diffMinutes",
                    inTimeTotal,
                    outTimeTotal,
                    "0", day
                )

            }
        }
    }

    private fun calculate(idMap: String, day: String, method: Int) {

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
            Vibrate().vibrateOnError(requireContext())
            infoTextView1.text = getString(R.string.in_time_and_out_time_can_not_be_the_same)
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
                    Vibrate().vibrateOnError(requireContext())
                    infoTextView1.text = getString(R.string.error_with_break_time_must_be_numbers_only)
                }
                else {
                    breakTimeNumber = breakTime.text.toString().toDouble() / 60
                    val totalHoursWithBreak = (totalHours - breakTimeNumber).toBigDecimal()
                        .setScale(2, RoundingMode.HALF_EVEN).toString()

                    if (method == 1) {
                        Toast.makeText(requireContext(), requireContext().getString(R.string.hour_calculated_in_time_format), Toast.LENGTH_SHORT).show()
                    }

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
                if (method == 1) {
                    Toast.makeText(requireContext(), requireContext().getString(R.string.hour_calculated_in_time_format), Toast.LENGTH_SHORT).show()
                }

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

    private fun savingHoursHistoryTab(
        context: Context,
        id: String,
        totalHours: String,
        inTimeTotal: String,
        outTimeTotal: String,
        breakTime: String,
        dayOfWeek: String
    ) {
        Vibrate().vibration(context)
        val dbHandler = DBHelper(context, null)

        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
        val date = formatter.parse(dayOfWeek)
        calendar.time = date!!
        val timeInMillis = calendar.timeInMillis

        dbHandler.update(id, inTimeTotal, outTimeTotal, totalHours, timeInMillis, breakTime)

        (context as AppCompatActivity).supportFragmentManager.popBackStack()
    }

    private fun breakTimeNumeric(breakTime: TextInputEditText) : Boolean {
        return try {
            breakTime.text.toString().toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.editHoursCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val materialToolbarEdit = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarEditFragment)
        materialToolbarEdit.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        val saveButton = requireActivity().findViewById<Button>(R.id.saveButton)
        saveButton.setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

        val dateChip = requireActivity().findViewById<Chip>(R.id.dateChipEdit)
        dateChip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))

        val deleteDrawable = materialToolbarEdit?.menu?.findItem(R.id.delete)?.icon
        deleteDrawable?.mutate()

        val navigationDrawable = materialToolbarEdit?.navigationIcon
        navigationDrawable?.mutate()

        val breakTextInputEditText = requireActivity().findViewById<TextInputLayout>(R.id.outlinedTextFieldEdit)
        val breakTextBox = requireActivity().findViewById<TextInputEditText>(R.id.breakTimeEdit)

        breakTextInputEditText?.boxStrokeColor = Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        breakTextInputEditText?.hintTextColor = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        try {
            breakTextBox.textCursorDrawable = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        breakTextBox.highlightColor = Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        breakTextBox.setTextIsSelectable(false)

        if (Build.VERSION.SDK_INT >= 29) {
            try {
                if (MenuTintData(requireContext()).loadMenuTint()) {

                    deleteDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                        BlendMode.SRC_ATOP
                    )
                } else {
                    val typedValue = TypedValue()
                    activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                    val id = typedValue.resourceId
                    deleteDrawable?.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), id),
                        BlendMode.SRC_ATOP
                    )
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), id),
                        BlendMode.SRC_ATOP
                    )
                }
            } catch (e: NoClassDefFoundError) {
                e.printStackTrace()
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
                val id = typedValue.resourceId
                navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP);
                deleteDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
            }
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP);
            deleteDrawable?.setColorFilter(ContextCompat.getColor(requireContext(), id), PorterDuff.Mode.SRC_ATOP)
        }

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<MaterialToolbar>(R.id.materialToolBarEditFragment)?.setTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<MaterialToolbar>(R.id.materialToolBarEditFragment)?.setTitleTextColor(ContextCompat.getColor(requireContext(), id))
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