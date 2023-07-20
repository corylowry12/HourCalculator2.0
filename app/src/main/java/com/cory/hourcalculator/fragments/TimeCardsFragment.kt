package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.TimeCardCustomAdapter
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.database.TimeCardDBHelper
import com.cory.hourcalculator.database.TimeCardsItemDBHelper
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.AnimationData
import com.cory.hourcalculator.sharedprefs.TimeCardFABPositioningData
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class TimeCardsFragment : Fragment() {

    private val dataList = ArrayList<HashMap<String, String>>()
    private lateinit var linearLayoutManager: GridLayoutManager
    private lateinit var timeCardCustomAdapter: TimeCardCustomAdapter

    private lateinit var recyclerViewState: Parcelable

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

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
                        activity?.setTheme(
                            R.style.Theme_AMOLED
                        )
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        timeCardCustomAdapter.updateCardColor()
        updateCustomTheme()
    }

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
                        activity?.setTheme(
                            R.style.Theme_AMOLED
                        )
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_time_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       updateCustomTheme()

        val recyclerViewTimeCards = view.findViewById<RecyclerView>(R.id.timeCardsRecyclerView)

        if (AnimationData(requireContext()).loadTimeCardRecyclerViewLoadingAnimation()) {
            val animation =
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.listview_animation)
            recyclerViewTimeCards?.layoutAnimation = animation
        }

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 2
            (activity as MainActivity).setActiveTab(2)
        }

        MainActivity().runOnUiThread(runnable)

        timeCardCustomAdapter = TimeCardCustomAdapter(requireContext(), dataList)

        linearLayoutManager = if (resources.getBoolean(R.bool.isTablet)) {
            GridLayoutManager(requireContext(), 2)
        } else {
            GridLayoutManager(requireContext(), 1)
        }

        loadIntoList()

        val floatingActionButtonTimeCards =
            view.findViewById<FloatingActionButton>(R.id.floatingActionButtonTimeCards)

        val params = CoordinatorLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(30,0,30,30)

        if (TimeCardFABPositioningData(requireContext()).loadFABPosition() == 0) {
            params.gravity = Gravity.BOTTOM or Gravity.START
        }
        else if (TimeCardFABPositioningData(requireContext()).loadFABPosition() == 1) {
            params.gravity = Gravity.BOTTOM or Gravity.CENTER
        }
        else if (TimeCardFABPositioningData(requireContext()).loadFABPosition() == 2) {
            params.gravity = Gravity.BOTTOM or Gravity.END
        }

        floatingActionButtonTimeCards.layoutParams = params

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

            snackbar.setActionTextColor(
                Color.parseColor(CustomColorGenerator(requireContext()).generateSnackbarActionTextColor())
            )

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
        val cursor = dbHandler.getAllRow()
        cursor!!.moveToFirst()

        val noEntriesStoredTextView =
            view?.findViewById<TextView>(R.id.noEntriesStoredTextViewTimeCards)

        if (cursor.count > 0) {
            noEntriesStoredTextView?.visibility = View.GONE
        } else {
            noEntriesStoredTextView?.visibility = View.VISIBLE
        }

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_ID))
            try {
                map["name"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_NAME))
            } catch (e: java.lang.NullPointerException) {
                e.printStackTrace()
                map["name"] = ""
            }
            map["totalHours"] =
                cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_TOTAL))
            map["week"] = cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_WEEK))
            try {
                map["image"] =
                    cursor.getString(cursor.getColumnIndex(TimeCardDBHelper.COLUMN_IMAGE))
            } catch (e: NullPointerException) {
                e.printStackTrace()
                map["image"] = ""
            }
            map["count"] =
                TimeCardsItemDBHelper(requireActivity().applicationContext, null).getCountForItemID(
                    map["id"].toString().toInt()
                ).toString()

            if (map["week"]!!.contains("-") && !map["week"]!!.contains(" - ")) {
                TimeCardDBHelper(requireContext(), null).updateWeek(
                    map["week"]!!.replace(
                        "-",
                        " - "
                    ), map["id"]!!
                )
                map["week"] = map["week"]!!.replace("-", " - ")
            }

            //if (map["count"]!!.toInt() == 1) {
                //if (map["week"]!!.contains("-")) {
                    //val (first, last) = map["week"]!!.split("-")
                   // TimeCardDBHelper(requireContext(), null).updateWeek(first, map["id"]!!)
                    //map["week"] = first
                //}
            //}

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
            val noHoursStoredTextView =
                activity?.findViewById<TextView>(R.id.noEntriesStoredTextViewTimeCards)
            noHoursStoredTextView?.visibility = View.GONE
        } else {
            val noHoursStoredTextView =
                activity?.findViewById<TextView>(R.id.noEntriesStoredTextViewTimeCards)
            noHoursStoredTextView?.visibility = View.VISIBLE
        }
    }

    fun updateCustomTheme() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.timeCardsCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val collapsingToolbarLayout =
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarTimeCards)

        collapsingToolbarLayout?.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )


        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarTimeCards)
            ?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        val floatingActionButtonTimeCards =
            requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButtonTimeCards)
        floatingActionButtonTimeCards?.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateBottomNavBackgroundColor()))
        floatingActionButtonTimeCards?.imageTintList =
            ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarTimeCards)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarTimeCards)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }
    }
}