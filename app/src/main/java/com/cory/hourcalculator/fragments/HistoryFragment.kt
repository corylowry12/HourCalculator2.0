package com.cory.hourcalculator.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import java.math.RoundingMode

@DelicateCoroutinesApi
class HistoryFragment : Fragment() {

    private var output: String = ""
    private val dataList = ArrayList<HashMap<String, String>>()

    private lateinit var linearLayoutManager: LinearLayoutManager

    private var containsColon = false

    var themeSelection = false

    private lateinit var customAdapter: CustomAdapter

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
                        activity?.setTheme(
                            AccentColor(requireContext()).followSystemTheme(
                                requireContext()
                            )
                        )
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
                } else {
                    if (themeSelection) {
                        activity?.theme?.applyStyle(R.style.system_accent_google, true)
                    } else {
                        activity?.theme?.applyStyle(R.style.system_accent_google_light, true)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customAdapter = CustomAdapter(requireContext(), dataList)

        linearLayoutManager = LinearLayoutManager(requireContext())

        activity?.window?.setBackgroundDrawable(null)

        val listView = activity?.findViewById<RecyclerView>(R.id.listView)
        val animation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.listview_animation)
        listView?.layoutAnimation = animation

        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)

        val accentColor = AccentColor(requireContext())
        val floatingActionButtonHistory =
            activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonHistory)
        when {

            accentColor.loadAccent() == 0 -> {
                floatingActionButtonHistory?.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
            }
            accentColor.loadAccent() == 1 -> {
                floatingActionButtonHistory?.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.pinkAccent)
            }
            accentColor.loadAccent() == 2 -> {
                floatingActionButtonHistory?.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.orangeAccent)
            }
            accentColor.loadAccent() == 3 -> {
                floatingActionButtonHistory?.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.redAccent)
            }
            accentColor.loadAccent() == 4 -> {
                val followSystemVersion = FollowSystemVersion(requireContext())
                if (!followSystemVersion.loadSystemColor()) {
                    floatingActionButtonHistory?.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.systemAccent)
                } else {
                    if (themeSelection) {
                        floatingActionButtonHistory?.backgroundTintList =
                            ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.systemAccentGoogleDark
                            )
                    } else {
                        floatingActionButtonHistory?.backgroundTintList =
                            ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.systemAccentGoogleDark_light
                            )
                    }
                }
            }
        }

        val sortData = SortData(requireContext())

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)

        topAppBar?.setOnMenuItemClickListener { menuItem ->
            val dbHandler = DBHelper(requireContext(), null)
            when (menuItem.itemId) {
                R.id.info -> {
                    Vibrate().vibration(requireContext())
                    val wagesData = WagesData(requireContext())

                    if (dbHandler.getCount() > 0) {
                        val alert = MaterialAlertDialogBuilder(
                            requireActivity(),
                            AccentColor(requireContext()).alertTheme()
                        )
                        alert.setTitle(getString(R.string.info))
                        if (wagesData.loadWageAmount() != "") {
                            try {
                                val wages =
                                    output.toDouble() * wagesData.loadWageAmount().toString()
                                        .toDouble()
                                val wagesRounded = String.format("%.2f", wages)
                                alert.setMessage(
                                    getString(
                                        R.string.history_info_dialog,
                                        output,
                                        dbHandler.getCount(),
                                        wagesRounded
                                    )
                                )
                            } catch (e: NumberFormatException) {
                                e.printStackTrace()
                                alert.setMessage(
                                    getString(
                                        R.string.history_info_dialog_error,
                                        output,
                                        dbHandler.getCount()
                                    )
                                )
                            }
                        } else {
                            alert.setMessage(
                                getString(
                                    R.string.history_info_dialog_error,
                                    output,
                                    dbHandler.getCount()
                                )
                            )
                        }
                        alert.setPositiveButton(getString(R.string.ok)) { _, _ ->
                            Vibrate().vibration(requireContext())
                        }
                        alert.show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.cant_show_info_history_empty),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    true
                }
                R.id.mnuSort -> {
                    Vibrate().vibration(requireContext())
                    if (dbHandler.getCount() == 0) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.cant_sort_history_is_empty),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        var selectedItem = -1

                        when {
                            sortData.loadSortState() == getString(R.string.day_desc) -> {
                                selectedItem = 0
                            }
                            sortData.loadSortState() == getString(R.string.day_asc) -> {
                                selectedItem = 1
                            }
                            sortData.loadSortState() == getString(R.string.total_desc) -> {
                                selectedItem = 2
                            }
                            sortData.loadSortState() == getString(R.string.total_asc) -> {
                                selectedItem = 3
                            }
                        }

                        val listItems: Array<String> = if (containsColon) {
                            if ((sortData.loadSortState() == getString(R.string.total_desc) || sortData.loadSortState() == getString(
                                    R.string.total_asc
                                )) && containsColon
                            ) {
                                selectedItem = 0
                                sortData.setSortState(getString(R.string.day_desc))
                            }
                            arrayOf(
                                getString(R.string.sort_by_last_entered),
                                getString(R.string.sort_by_first_entered)
                            )
                        } else {
                            arrayOf(
                                getString(R.string.sort_by_last_entered),
                                getString(R.string.sort_by_first_entered),
                                getString(R.string.sort_by_most_entered),
                                getString(R.string.sort_by_least_entered)
                            )
                        }

                        val collapsingToolbarLayout =
                            requireView().findViewById<AppBarLayout>(R.id.appBarLayoutHistory)

                        val alert = MaterialAlertDialogBuilder(
                            requireContext(),
                            AccentColor(requireContext()).alertTheme()
                        )
                        alert.setTitle(getString(R.string.sorting_method))
                        alert.setSingleChoiceItems(
                            listItems,
                            selectedItem
                        ) { dialog, i ->
                            Vibrate().vibration(requireContext())
                            when (i) {
                                0 -> {
                                    listView?.scrollToPosition(0)
                                    collapsingToolbarLayout.setExpanded(true, true)
                                    sortData.setSortState(getString(R.string.day_desc))
                                    changeSortMethod()
                                    customAdapter.checkBoxVisible = false
                                    try {
                                        topAppBar.navigationIcon = null
                                        customAdapter.snackbarDeleteSelected.dismiss()
                                        customAdapter.snackbarDismissCheckBox.dismiss()
                                    } catch (e: UninitializedPropertyAccessException) {
                                        e.printStackTrace()
                                    }
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                        0,
                                        dataList.size
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.changed_sort_mode_last_entered),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                1 -> {
                                    listView?.scrollToPosition(0)
                                    collapsingToolbarLayout.setExpanded(true, true)
                                    sortData.setSortState(getString(R.string.day_asc))
                                    changeSortMethod()
                                    customAdapter.checkBoxVisible = false
                                    try {
                                        topAppBar.navigationIcon = null
                                        customAdapter.snackbarDeleteSelected.dismiss()
                                        customAdapter.snackbarDismissCheckBox.dismiss()
                                    } catch (e: UninitializedPropertyAccessException) {
                                        e.printStackTrace()
                                    }
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                        0,
                                        dataList.size
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.changed_sort_mode_first_entered),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                2 -> {
                                    listView?.scrollToPosition(0)
                                    collapsingToolbarLayout.setExpanded(true, true)
                                    sortData.setSortState(getString(R.string.total_desc))
                                    changeSortMethod()
                                    customAdapter.checkBoxVisible = false
                                    try {
                                        topAppBar.navigationIcon = null
                                        customAdapter.snackbarDeleteSelected.dismiss()
                                        customAdapter.snackbarDismissCheckBox.dismiss()
                                    } catch (e: UninitializedPropertyAccessException) {
                                        e.printStackTrace()
                                    }
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                        0,
                                        dataList.size
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.changed_sort_mode_most_entered),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                3 -> {
                                    listView?.scrollToPosition(0)
                                    collapsingToolbarLayout.setExpanded(true, true)
                                    sortData.setSortState(getString(R.string.total_asc))
                                    changeSortMethod()
                                    customAdapter.checkBoxVisible = false
                                    try {
                                        topAppBar.navigationIcon = null
                                        customAdapter.snackbarDeleteSelected.dismiss()
                                        customAdapter.snackbarDismissCheckBox.dismiss()
                                    } catch (e: UninitializedPropertyAccessException) {
                                        e.printStackTrace()
                                    }
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(
                                        0,
                                        dataList.size
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.changed_sort_mode_least_entered),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            dialog.dismiss()
                        }
                        alert.show()
                    }
                    true
                }
                else -> false
            }
        }

        loadIntoList()

        if (customAdapter.isCheckBoxVisible()) {
            topAppBar?.navigationIcon = ResourcesCompat.getDrawable(
                requireContext().resources,
                R.drawable.ic_baseline_close_24,
                requireContext().theme
            )
        } else {
            topAppBar?.navigationIcon = null
        }

        listView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                if (pastVisibleItems > 0) {
                    floatingActionButtonHistory?.show()
                } else {
                    floatingActionButtonHistory?.hide()
                }
            }
        })

        floatingActionButtonHistory?.setOnClickListener {
            Vibrate().vibration(requireContext())
            val savedState = listView?.layoutManager?.onSaveInstanceState()
            listView?.scrollToPosition(0)
            val collapsingToolbarLayout =
                requireView().findViewById<AppBarLayout>(R.id.appBarLayoutHistory)
            collapsingToolbarLayout.setExpanded(true, true)
            val snackbar =
                Snackbar.make(view, getString(R.string.restore_position), Snackbar.LENGTH_LONG)
                    .setDuration(5000)

            snackbar.setAction(getString(R.string.restore)) {
                Vibrate().vibration(requireContext())

                listView?.layoutManager?.onRestoreInstanceState(savedState)
                collapsingToolbarLayout.setExpanded(false, false)

            }
            snackbar.setActionTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    AccentColor(requireContext()).snackbarActionTextColor()
                )
            )
            snackbar.apply {
                snackbar.view.background = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.snackbar_corners,
                    context.theme
                )
            }
            if (!customAdapter.isCheckBoxVisible()) {
                snackbar.show()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager?.popBackStack()
                }
            })
    }

    @SuppressLint("Range")
    private fun loadIntoList() {

        val dbHandler = DBHelper(requireActivity().applicationContext, null)

        if (dbHandler.getCount() > 0) {
            val noHoursStoredTextView = activity?.findViewById<TextView>(R.id.noHoursStoredTextView)
            noHoursStoredTextView?.visibility = View.GONE
        } else {
            val noHoursStoredTextView = activity?.findViewById<TextView>(R.id.noHoursStoredTextView)
            noHoursStoredTextView?.visibility = View.VISIBLE

        }

        var y = 0.0

        dataList.clear()
        val cursor = dbHandler.getAllRow(requireContext())
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
            map["inTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
            map["outTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
            map["breakTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
            map["totalHours"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
            map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
            dataList.add(map)

            val array = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)).toString()

            var decimalTime: Double
            if (array.contains(":")) {
                val (hours, minutes) = array.split(":")
                val decimal =
                    (minutes.toDouble() / 60).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
                        .toString().drop(2)
                try {
                    decimalTime = "$hours.$decimal".toDouble()
                    y += decimalTime

                } catch (e: java.lang.NumberFormatException) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_calculating_hours),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                containsColon = true

            } else {
                y += array.toDouble()
            }

            output = String.format("%.2f", y)
            cursor.moveToNext()

        }

        val listView = activity?.findViewById<RecyclerView>(R.id.listView)
        listView?.layoutManager = linearLayoutManager
        listView?.adapter = customAdapter

    }

    @SuppressLint("Range")
    private fun changeSortMethod() {

        val dbHandler = DBHelper(requireActivity().applicationContext, null)

        dataList.clear()
        val cursor = dbHandler.getAllRow(requireContext())
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
            map["inTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
            map["outTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
            map["breakTime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
            map["totalHours"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
            map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
            dataList.add(map)

            cursor.moveToNext()

        }
    }

    @SuppressLint("Range")
    fun undo() {
        val listView = activity?.findViewById<RecyclerView>(R.id.listView)

        containsColon = false

        val dbHandler = DBHelper(requireContext(), null)
        val undoHoursData = UndoHoursData(requireContext())

        dbHandler.insertRow(
            undoHoursData.loadInTime(),
            undoHoursData.loadOutTime(),
            undoHoursData.loadTotalHours(),
            undoHoursData.loadDate(),
            undoHoursData.loadBreakTime()
        )

        dataList.clear()
        val cursor2 = dbHandler.getAllRow(requireContext())
        cursor2!!.moveToFirst()

        while (!cursor2.isAfterLast) {
            val map2 = HashMap<String, String>()
            map2["id"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_ID))
            map2["inTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_IN))
            map2["outTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_OUT))
            map2["breakTime"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_BREAK))
            map2["totalHours"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_TOTAL))
            map2["date"] = cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_DAY))
            dataList.add(map2)

            cursor2.moveToNext()

        }

        listView?.adapter?.notifyItemInserted(ItemPosition(requireContext()).loadPosition())
    }

    private lateinit var recyclerViewState: Parcelable

    fun saveState() {

        val recyclerView = view?.findViewById<RecyclerView>(R.id.listView)

        recyclerViewState = recyclerView?.layoutManager?.onSaveInstanceState()!!

        textViewVisibility()

        calculateWages()
    }

    @SuppressLint("Range")
    private fun calculateWages() {

        val dbHandler = DBHelper(requireContext(), null)

        var y = 0.0

        val cursor = dbHandler.getAllRow(requireContext())
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {

            val array = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)).toString()

            var decimalTime: Double
            if (array.contains(":")) {
                val (hours, minutes) = array.split(":")
                val decimal =
                    (minutes.toDouble() / 60).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
                        .toString().drop(2)
                try {
                    decimalTime = "$hours.$decimal".toDouble()
                    y += decimalTime

                } catch (e: java.lang.NumberFormatException) {
                    e.printStackTrace()
                }

                containsColon = true

            } else {
                y += array.toDouble()
            }

            output = String.format("%.2f", y)
            cursor.moveToNext()

        }

    }

    private fun textViewVisibility() {
        val dbHandler = DBHelper(requireActivity().applicationContext, null)

        if (dbHandler.getCount() > 0) {
            val noHoursStoredTextView = activity?.findViewById<TextView>(R.id.noHoursStoredTextView)
            noHoursStoredTextView?.visibility = View.GONE
        } else {
            val noHoursStoredTextView = activity?.findViewById<TextView>(R.id.noHoursStoredTextView)
            noHoursStoredTextView?.visibility = View.VISIBLE
        }
    }

    fun restoreState() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.listView)
        recyclerView?.layoutManager?.onRestoreInstanceState(recyclerViewState)

        textViewVisibility()

        calculateWages()
    }

    fun deleteAll() {
        val animation = AlphaAnimation(1f, 0f)
        animation.duration = 500
        val listView = view?.findViewById<RecyclerView>(R.id.listView)
        val floatingActionButtonHistory =
            view?.findViewById<FloatingActionButton>(R.id.floatingActionButtonHistory)

        floatingActionButtonHistory?.visibility = View.INVISIBLE

        listView?.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            loadIntoList()
        }, 500)

        saveState()

    }

    fun checkBoxVisible(visible: Boolean) {

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)
        val recyclerView = activity?.findViewById<RecyclerView>(R.id.listView)

        if (visible) {
            topAppBar?.navigationIcon = ResourcesCompat.getDrawable(
                requireContext().resources,
                R.drawable.ic_baseline_close_24,
                requireContext().theme
            )
        } else {
            topAppBar?.navigationIcon = null
        }

        topAppBar?.setNavigationOnClickListener {
            customAdapter.checkboxVisible()
            topAppBar.navigationIcon = null
        }
    }

    fun hideNavigationIcon() {
        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)
        topAppBar?.navigationIcon = null
    }

    fun undoDeleteAll() {
        val animation = AlphaAnimation(0f, 1f)
        animation.duration = 500
        val listView = view?.findViewById<RecyclerView>(R.id.listView)

        listView?.alpha = 0f
        loadIntoList()
        listView?.startAnimation(animation)
        listView?.alpha = 1f

        restoreState()

    }
}