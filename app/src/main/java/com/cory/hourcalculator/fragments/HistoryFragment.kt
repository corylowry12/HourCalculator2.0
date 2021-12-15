package com.cory.hourcalculator.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.DelicateCoroutinesApi
import java.math.RoundingMode

@DelicateCoroutinesApi
class HistoryFragment : Fragment() {

    private var output: String = ""
    private val dataList = ArrayList<HashMap<String, String>>()

    private lateinit var linearLayoutManager : LinearLayoutManager

    private var containsColon = false

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

        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(requireContext())

        activity?.window?.setBackgroundDrawable(null)

        val listView = activity?.findViewById<RecyclerView>(R.id.listView)
        val animation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.listview_animation)
        listView?.layoutAnimation = animation

        val inputManager: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
                floatingActionButtonHistory?.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.systemAccent)
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
                                alert.setMessage("Total Hours: $output\nNumber of Entries: ${dbHandler.getCount()}\nWages: $$wagesRounded")
                            } catch (e: NumberFormatException) {
                                e.printStackTrace()
                                alert.setMessage("Total Hours: $output\nNumber of Entries: ${dbHandler.getCount()}\nWages: There was an error")
                            }
                        } else {
                            alert.setMessage("Total Hours: $output\nNumber of Entries: ${dbHandler.getCount()}\nWages: There was an error")
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
                    }
                    else {
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

                        var listItems : Array<String> = arrayOf()
                        listItems = if (containsColon) {
                            if ((sortData.loadSortState() == "totalHours DESC" || sortData.loadSortState() == "totalHours ASC") && containsColon) {
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

                        val alert = MaterialAlertDialogBuilder(
                            requireContext(),
                            AccentColor(requireContext()).alertTheme()
                        )
                        alert.setTitle("Sorting Method")
                        alert.setSingleChoiceItems(
                            listItems,
                            selectedItem
                        ) { dialog, i ->
                            Vibrate().vibration(requireContext())
                            when (i) {
                                0 -> {
                                    sortData.setSortState(getString(R.string.day_desc))
                                    //listView?.scheduleLayoutAnimation()
                                    changeSortMethod()
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(0, dataList.size)
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.changed_sort_mode_last_entered),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                1 -> {
                                    sortData.setSortState(getString(R.string.day_asc))
                                    //listView?.scheduleLayoutAnimation()
                                    changeSortMethod()
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(0, dataList.size)
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.changed_sort_mode_first_entered),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                2 -> {
                                    sortData.setSortState(getString(R.string.total_desc))
                                    listView?.scheduleLayoutAnimation()
                                    changeSortMethod()
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(0, dataList.size)
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.changed_sort_mode_most_entered),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                3 -> {
                                    sortData.setSortState(getString(R.string.total_asc))
                                    listView?.scheduleLayoutAnimation()
                                   changeSortMethod()
                                    view.findViewById<RecyclerView>(R.id.listView).adapter?.notifyItemRangeChanged(0, dataList.size)
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

        listView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                    val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                    if (pastVisibleItems > 0) {
                        floatingActionButtonHistory?.visibility = View.VISIBLE
                    }
                    else {
                        floatingActionButtonHistory?.visibility = View.INVISIBLE
                    }
            }
        })

       /* listView?.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (firstVisibleItem > 0) {
                    floatingActionButtonHistory?.visibility = View.VISIBLE

                } else {
                    floatingActionButtonHistory?.visibility = View.INVISIBLE
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}
        })*/

        floatingActionButtonHistory?.setOnClickListener {
            Vibrate().vibration(requireContext())
            listView?.smoothScrollToPosition(0)
        }

        loadIntoList()

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

            var decimalTime = 0.0
            if (array.contains(":")) {
                val (hours, minutes) = array.split(":")
                val decimal = (minutes.toDouble() / 60).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString().drop(2)
                decimalTime = "$hours.$decimal".toDouble()
                y += decimalTime
                    containsColon = true

            }
            else {
                y += array.toDouble()
            }

            output = String.format("%.2f", y)
            cursor.moveToNext()

        }

        val listView = activity?.findViewById<RecyclerView>(R.id.listView)
        listView?.layoutManager = linearLayoutManager
        listView?.adapter = CustomAdapter(requireContext(), dataList)

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

    fun undo() {
        val listView = activity?.findViewById<RecyclerView>(R.id.listView)

        containsColon = false

        val dbHandler = DBHelper(requireContext(), null)
        val undoHoursData = UndoHoursData(requireContext())

        val map = HashMap<String, String>()
        map["id"] = undoHoursData.loadID().toString()
        map["inTime"] =  undoHoursData.loadInTime()
        map["outTime"] = undoHoursData.loadOutTime()
        map["breakTime"] =   undoHoursData.loadBreakTime()
        map["totalHours"] = undoHoursData.loadTotalHours()
        map["date"] =  undoHoursData.loadDate().toString()
        dataList.add(map)

        dbHandler.insertRow(
            undoHoursData.loadInTime(),
            undoHoursData.loadOutTime(),
            undoHoursData.loadTotalHours(),
            undoHoursData.loadDate(),
            undoHoursData.loadBreakTime()
        )

        listView?.adapter?.notifyItemInserted(IdData(requireContext()).loadID())

        //loadIntoList()
    }
    private lateinit var recyclerViewState: Parcelable

    fun saveState() {
        /*val listView = activity?.findViewById<RecyclerView>(R.id.listView)
        val index = listView?.firstVisiblePosition
        val v = listView?.getChildAt(0)
        val top = if (v == null) 0 else v.top - listView.paddingTop

        listView?.setSelectionFromTop(index!!, top)

        containsColon = false

        loadIntoList()*/

        val recyclerView = view?.findViewById<RecyclerView>(R.id.listView)

        recyclerViewState = recyclerView?.layoutManager?.onSaveInstanceState()!!

    }

    fun restoreState() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.listView)
        recyclerView?.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    fun deleteAll() {
        val animation = AlphaAnimation(1f, 0f)
        animation.duration = 500
        val listView = view?.findViewById<RecyclerView>(R.id.listView)

        listView?.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            loadIntoList()
        }, 500)

    }

    fun undoDeleteAll() {
        val animation = AlphaAnimation(0f, 1f)
        animation.duration = 500
        val listView = view?.findViewById<RecyclerView>(R.id.listView)

        listView?.alpha = 0f
        loadIntoList()
        listView?.startAnimation(animation)
        listView?.alpha = 1f
    }
}