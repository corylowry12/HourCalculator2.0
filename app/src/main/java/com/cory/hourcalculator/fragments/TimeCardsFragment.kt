package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.TimeCardCustomAdapter
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

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

        val collapsingToolbarLayout = activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarTimeCards)

        if (AccentColor(requireContext()).loadAccent() == 5) {
            collapsingToolbarLayout?.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
            collapsingToolbarLayout?.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        }

        val recyclerViewTimeCards = view.findViewById<RecyclerView>(R.id.timeCardsRecyclerView)

        val animation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.listview_animation)
        recyclerViewTimeCards?.layoutAnimation = animation

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 2
            (activity as MainActivity).setActiveTab(2)
        }

        MainActivity().runOnUiThread(runnable)

        timeCardCustomAdapter = TimeCardCustomAdapter(requireContext(), dataList)
        linearLayoutManager = LinearLayoutManager(requireContext())

        activity?.window?.setBackgroundDrawable(null)

        loadIntoList()

        val floatingActionButtonTimeCards = view.findViewById<FloatingActionButton>(R.id.floatingActionButtonTimeCards)
        val accentColor = AccentColor(requireContext())
        when {

            accentColor.loadAccent() == 0 -> {
                floatingActionButtonTimeCards?.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
            }
            accentColor.loadAccent() == 1 -> {
                floatingActionButtonTimeCards?.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.pinkAccent)
            }
            accentColor.loadAccent() == 2 -> {
                floatingActionButtonTimeCards?.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.orangeAccent)
            }
            accentColor.loadAccent() == 3 -> {
                floatingActionButtonTimeCards?.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.redAccent)
            }
            accentColor.loadAccent() == 4 -> {
                val followSystemVersion = FollowSystemVersion(requireContext())
                if (!followSystemVersion.loadSystemColor()) {
                    floatingActionButtonTimeCards?.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.systemAccent)
                } else {
                    if (themeSelection) {
                        floatingActionButtonTimeCards?.backgroundTintList =
                            ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.systemAccentGoogleDark
                            )
                    } else {
                        floatingActionButtonTimeCards?.backgroundTintList =
                            ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.systemAccentGoogleDark_light
                            )
                    }
                }
            }
            accentColor.loadAccent() == 5 -> {
                floatingActionButtonTimeCards?.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
                floatingActionButtonTimeCards?.imageTintList =  ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()))
            }
        }

        recyclerViewTimeCards?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                if (pastVisibleItems > 0) {
                    floatingActionButtonTimeCards?.show()
                } else {
                    floatingActionButtonTimeCards?.hide()
                }
            }
        })

        floatingActionButtonTimeCards?.setOnClickListener {
            scrollToTop()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager?.popBackStack()
                }
            })
    }

    fun scrollToTop() {
        val listView = view?.findViewById<RecyclerView>(R.id.timeCardsRecyclerView)
        Vibrate().vibration(requireContext())

        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0) {

            val savedState = listView?.layoutManager?.onSaveInstanceState()
            listView?.scrollToPosition(0)
            val collapsingToolbarLayout =
                requireView().findViewById<AppBarLayout>(R.id.appBarLayoutTimeCards)
            collapsingToolbarLayout.setExpanded(true, true)
            val snackbar =
                Snackbar.make(
                    requireView(),
                    getString(R.string.restore_position),
                    Snackbar.LENGTH_LONG
                )
                    .setDuration(5000)

            snackbar.setAction(getString(R.string.restore)) {
                Vibrate().vibration(requireContext())

                listView?.layoutManager?.onRestoreInstanceState(savedState)
                collapsingToolbarLayout.setExpanded(false, false)

            }
            if (AccentColor(requireContext()).loadAccent() == 5) {
                snackbar.setActionTextColor(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
                )
            }
            else {
                snackbar.setActionTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        AccentColor(requireContext()).snackbarActionTextColor()
                    )
                )
            }
            snackbar.apply {
                snackbar.view.background = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.snackbar_corners,
                    context.theme
                )
            }
            snackbar.show()
        }
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

            if (map["week"]!!.contains("-") && !map["week"]!!.contains(" - ")) {
                TimeCardDBHelper(requireContext(), null).updateWeek(map["week"]!!.replace("-", " - "), map["id"]!!)
                map["week"] = map["week"]!!.replace("-", " - ")
            }

            if (map["count"]!!.toInt() == 1) {
                if (map["week"]!!.contains("-")) {
                    val (first, last) = map["week"]!!.split("-")
                    TimeCardDBHelper(requireContext(), null).updateWeek(first, map["id"]!!)
                    map["week"] = first
                }
            }

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
        val floatingActionButtonHistory =
            view?.findViewById<FloatingActionButton>(R.id.floatingActionButtonTimeCards)

        floatingActionButtonHistory?.visibility = View.INVISIBLE

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