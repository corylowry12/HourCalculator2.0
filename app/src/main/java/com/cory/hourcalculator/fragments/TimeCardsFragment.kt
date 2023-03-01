package com.cory.hourcalculator.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.CustomAdapter
import com.cory.hourcalculator.adapters.TimeCardCustomAdapter
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.cory.hourcalculator.classes.FollowSystemVersion
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.math.RoundingMode

class TimeCardsFragment : Fragment() {

    private val dataList = ArrayList<HashMap<String, String>>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var timeCardCustomAdapter: TimeCardCustomAdapter

    var themeSelection = false

    private lateinit var recyclerViewState: Parcelable

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
        return inflater.inflate(R.layout.fragment_time_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeCardCustomAdapter = TimeCardCustomAdapter(requireContext(), dataList)
        linearLayoutManager = LinearLayoutManager(requireContext())

        activity?.window?.setBackgroundDrawable(null)

        loadIntoList()
    }

    private fun loadIntoList() {

        val dbHandler = TimeCardDBHelper(requireActivity().applicationContext, null)

        dataList.clear()
        val cursor = dbHandler.getAllRow(requireContext())
        cursor!!.moveToFirst()

        val noEntriesStoredTextView = view?.findViewById<TextView>(R.id.noEntriesStoredTextViewTimeCards)

        if (cursor.count > 0) {
            noEntriesStoredTextView?.visibility = View.GONE
        }
        else {
            noEntriesStoredTextView?.visibility = View.VISIBLE
        }

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_ID))
            try {
                map["name"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_NAME))
            } catch (e : java.lang.NullPointerException) {
                e.printStackTrace()
                map["name"] = ""
            }
            map["totalHours"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_TOTAL))
            map["week"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_WEEK))
            map["count"] = TimeCardsItemDBHelper(requireActivity().applicationContext, null).getCountForItemID(map["id"].toString().toInt()).toString()
            dataList.add(map)

            cursor.moveToNext()
        }


        val timeCardRecyclerView = activity?.findViewById<RecyclerView>(R.id.timeCardsRecyclerView)
        timeCardRecyclerView?.layoutManager = linearLayoutManager
        timeCardRecyclerView?.adapter = timeCardCustomAdapter

    }

    fun deleteAll() {
        val animation = AlphaAnimation(1f, 0f)
        animation.duration = 500
        val listView = view?.findViewById<RecyclerView>(R.id.timeCardsRecyclerView)
        //val floatingActionButtonHistory =
            //view?.findViewById<FloatingActionButton>(R.id.floatingActionButtonHistory)

        //floatingActionButtonHistory?.visibility = View.INVISIBLE

        listView?.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            loadIntoList()
        }, 500)

        saveState()

    }

    fun saveState() {

        val recyclerView = view?.findViewById<RecyclerView>(R.id.timeCardsRecyclerView)

        recyclerViewState = recyclerView?.layoutManager?.onSaveInstanceState()!!

        textViewVisibility()
    }

    private fun textViewVisibility() {
        val dbHandler = TimeCardDBHelper(requireActivity().applicationContext, null)

        if (dbHandler.getCount() > 0) {
            val noHoursStoredTextView = activity?.findViewById<TextView>(R.id.noEntriesStoredTextViewTimeCards)
            noHoursStoredTextView?.visibility = View.GONE
        } else {
            val noHoursStoredTextView = activity?.findViewById<TextView>(R.id.noEntriesStoredTextViewTimeCards)
            noHoursStoredTextView?.visibility = View.VISIBLE
        }
    }
}