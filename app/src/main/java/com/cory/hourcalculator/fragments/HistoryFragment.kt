package com.cory.hourcalculator.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.cory.hourcalculator.classes.SortData
import com.cory.hourcalculator.classes.WagesData
import com.cory.hourcalculator.database.DBHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HistoryFragment : Fragment() {

    private var output : String = ""
    val dataList = ArrayList<HashMap<String, String>>()

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
        val accentColor = AccentColor(requireContext())
        val floatingActionButtonHistory = activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonHistory)
        when {

            accentColor.loadAccent() == 0 -> {
                floatingActionButtonHistory?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
            }
            accentColor.loadAccent() == 1 -> {
                floatingActionButtonHistory?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.pinkAccent)
            }
            accentColor.loadAccent() == 2 -> {
                floatingActionButtonHistory?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orangeAccent)
            }
            accentColor.loadAccent() == 3 -> {
                floatingActionButtonHistory?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.redAccent)
            }
            accentColor.loadAccent() == 4 -> {
                floatingActionButtonHistory?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.systemAccent)
            }
        }

        val sortData = SortData(requireContext())

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarHistory)

        when {
            sortData.loadSortState() == "day DESC" -> {
                topAppBar?.menu?.findItem(R.id.menuSortByLastEntered)?.title = getString(R.string.sort_by_last_entered_checked)
            }
            sortData.loadSortState() == "day ASC" -> {
                topAppBar?.menu?.findItem(R.id.menuSortByFirstEntered)?.title = getString(R.string.sort_by_first_entered_checked)
            }
            sortData.loadSortState() == "total ASC" -> {
                topAppBar?.menu?.findItem(R.id.menuSortByLeastHours)?.title = getString(R.string.sort_by_least_hours_checked)
            }
            sortData.loadSortState() == "total DESC" -> {
                topAppBar?.menu?.findItem(R.id.menuSortByMostHours)?.title = getString(R.string.sort_by_most_hours_checked)
            }
        }

        topAppBar?.setOnMenuItemClickListener { menuItem ->
            val dbHandler = DBHelper(requireContext(), null)

            when (menuItem.itemId) {
                R.id.info -> {
                    val wagesData = WagesData(requireContext())
                    // Handle edit text press
                    if (dbHandler.getCount() > 0) {
                        val alert = MaterialAlertDialogBuilder(requireActivity(), AccentColor(requireContext()).alertTheme())
                        alert.setTitle("Info")
                         if (wagesData.loadWageAmount() != "") {
                try {
                    val wages = output.toDouble() * wagesData.loadWageAmount().toString().toDouble()
                    val wagesrounded = String.format("%.2f", wages)
                    alert.setMessage("Total Hours: $output\nNumber of Entries: ${dbHandler.getCount()}\nWages: $wagesrounded")
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    alert.setMessage("Total Hours: $output\nNumber of Entries: ${dbHandler.getCount()}\nWages: There was an error")
                }
            }
                        alert.setPositiveButton("OK", null)
                        alert.show()
                    }
                    else {
                        Toast.makeText(requireContext(), "Can't show info, there aren't any hours stored", Toast.LENGTH_LONG).show()
                    }
                    true
                }

                R.id.menuSortByLastEntered -> {
                    if (dbHandler.getCount() == 0) {
                        Toast.makeText(requireContext(), "Cant sort, history is empty", Toast.LENGTH_SHORT).show()
                    } else {
                        sortData.setSortState("day DESC")
                        loadIntoList()
                        Toast.makeText(requireContext(), "Changed sort mode to last entered", Toast.LENGTH_SHORT).show()
                        topAppBar.menu?.findItem(R.id.menuSortByFirstEntered)?.title = getString(R.string.sort_by_first_entered)
                        topAppBar.menu?.findItem(R.id.menuSortByLeastHours)?.title = getString(R.string.sort_by_least_entered)
                        topAppBar.menu?.findItem(R.id.menuSortByMostHours)?.title = getString(R.string.sort_by_most_entered)
                        menuItem.title = getString(R.string.sort_by_last_entered_checked)
                    }
                    true
                }

                R.id.menuSortByFirstEntered -> {
                    if (dbHandler.getCount() == 0) {
                        Toast.makeText(requireContext(), "Cant sort, history is empty", Toast.LENGTH_SHORT).show()
                    } else {
                        sortData.setSortState("day ASC")
                        loadIntoList()
                        Toast.makeText(requireContext(), "Changed sort mode to first entered", Toast.LENGTH_SHORT).show()
                        topAppBar.menu?.findItem(R.id.menuSortByLastEntered)?.title = getString(R.string.sort_by_last_entered)
                        topAppBar.menu?.findItem(R.id.menuSortByLeastHours)?.title = getString(R.string.sort_by_least_entered)
                        topAppBar.menu?.findItem(R.id.menuSortByMostHours)?.title = getString(R.string.sort_by_most_entered)
                        menuItem.title = getString(R.string.sort_by_first_entered_checked)
                    }
                    true
                }

                R.id.menuSortByLeastHours -> {
                    if (dbHandler.getCount() == 0) {
                        Toast.makeText(requireContext(), "Cant sort, history is empty", Toast.LENGTH_SHORT).show()
                    } else {
                        sortData.setSortState(getString(R.string.total_asc))
                        loadIntoList()
                        Toast.makeText(requireContext(), "Changed sort mode to least entered", Toast.LENGTH_SHORT).show()
                        topAppBar.menu?.findItem(R.id.menuSortByLastEntered)?.title = getString(R.string.sort_by_last_entered)
                        topAppBar.menu?.findItem(R.id.menuSortByFirstEntered)?.title = getString(R.string.sort_by_first_entered)
                        topAppBar.menu?.findItem(R.id.menuSortByMostHours)?.title = getString(R.string.sort_by_most_entered)
                        menuItem.title = getString(R.string.sort_by_least_hours_checked)
                    }
                    true
                }

                R.id.menuSortByMostHours -> {
                    if (dbHandler.getCount() == 0) {
                        Toast.makeText(requireContext(), "Cant sort, history is empty", Toast.LENGTH_SHORT).show()
                    } else {
                        sortData.setSortState("total DESC")
                        loadIntoList()
                        Toast.makeText(requireContext(), "Changed sort mode to most entered", Toast.LENGTH_SHORT).show()
                        topAppBar.menu?.findItem(R.id.menuSortByLastEntered)?.title = getString(R.string.sort_by_last_entered)
                        topAppBar.menu?.findItem(R.id.menuSortByFirstEntered)?.title = getString(R.string.sort_by_first_entered)
                        topAppBar.menu?.findItem(R.id.menuSortByLeastHours)?.title = getString(R.string.sort_by_least_entered)
                        menuItem.title = getString(R.string.sort_by_most_hours_checked)
                    }
                    true
                }

                else -> false
            }
        }

        val listView = activity?.findViewById<ListView>(R.id.listView)
        listView?.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

                if (firstVisibleItem > 0) {
                    floatingActionButtonHistory?.show()
                } else {

                    floatingActionButtonHistory?.hide()
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}
        })

        floatingActionButtonHistory?.setOnClickListener {
            //vibration(vibrationData)
            listView?.smoothScrollToPosition(0)
        }

        loadIntoList()

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
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
        }
        else {
            val noHoursStoredTextView = activity?.findViewById<TextView>(R.id.noHoursStoredTextView)
            noHoursStoredTextView?.visibility = View.VISIBLE
        }

        var y = 0.0

        dataList.clear()
        val cursor = dbHandler.getAllRow(requireActivity())
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
            map["intime"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IN))
            map["out"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_OUT))
            map["break"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BREAK))
            map["total"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL))
            map["day"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY))
            dataList.add(map)

            val array = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL)).toString()

            y += array.toDouble()

            output = String.format("%.2f", y)
            //textViewTotalHours.text = getString(R.string.total_hours_history, output)

           /* if (wagesData.loadWageAmount() != "") {
                try {
                    val wages = output.toDouble() * wagesData.loadWageAmount().toString().toDouble()
                    val wagesrounded = String.format("%.2f", wages)
                    textViewWages.text = getString(R.string.total_wages, wagesrounded)
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    textViewWages.text = getString(R.string.there_is_a_problem_calculating_wages)
                }
            }*/
            cursor.moveToNext()

        }
        //textViewSize.text = getString(R.string.amount_of_hours_saved, dbHandler.getCount())
        val listView = activity?.findViewById<ListView>(R.id.listView)
        listView?.adapter = CustomAdapter(requireContext(), dataList)

    }

    fun update() {
        val listView = activity?.findViewById<ListView>(R.id.listView)
        val index = listView?.firstVisiblePosition
        val v = listView?.getChildAt(0)
        val top = if (v == null) 0 else v.top - listView.paddingTop

        if (HistoryFragment().isVisible) {
            loadIntoList()
        }
        listView?.setSelectionFromTop(index!!, top)

    }

}