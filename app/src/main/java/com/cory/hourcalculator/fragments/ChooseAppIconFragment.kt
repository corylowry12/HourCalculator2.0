package com.cory.hourcalculator.fragments

import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.AppIconAdapter
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.sharedprefs.MenuTintData
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar

class ChooseAppIconFragment : Fragment() {

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        customColorChange()

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
    }

    private val appIconDataList = ArrayList<HashMap<String, String>>()

    private lateinit var gridLayoutManager: GridLayoutManager

    private lateinit var appIconAdapter: AppIconAdapter

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
        return inflater.inflate(R.layout.fragment_choose_app_icon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        customColorChange()

        gridLayoutManager = GridLayoutManager(requireContext(), 1)

        val appIconRecyclerView = activity?.findViewById<RecyclerView>(R.id.appIconRecyclerView)
        appIconRecyclerView?.layoutManager = gridLayoutManager
        val iconArray = arrayOf(
            R.drawable.hourcalclogoteal.toString(),
            R.drawable.hourcalclogopink.toString(),
            R.drawable.hourcalclogoorange.toString(),
            R.drawable.hourcalclogored.toString(),
            R.drawable.hourcalclogoblue.toString(),
            R.drawable.hourcalculatorlogoyellowgradient.toString(),
            R.drawable.hourcalclogo_christmas.toString(),
            R.drawable.hourcalclogoteal.toString()
        )
        val iconNameArray =
            arrayOf("Teal", "Pink", "Orange", "Red", "Blue", "OG", "Snow Falling", "Material You")
        for (i in 0 until iconArray.count()) {
            val map = HashMap<String, String>()
            map["icon"] = iconArray.elementAt(i)
            map["name"] = iconNameArray.elementAt(i)
            appIconDataList.add(map)
        }
        appIconAdapter = AppIconAdapter(requireContext(), appIconDataList)

        appIconRecyclerView?.adapter = appIconAdapter
    }

    private fun customColorChange() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.chooseAppIconCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val appIconToolBar =
            activity?.findViewById<MaterialToolbar>(R.id.materialToolBarAppIconsFragment)

        appIconToolBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val collapsingToolbarLayout =
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppIconFragment)

        collapsingToolbarLayout?.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )
        collapsingToolbarLayout?.setStatusBarScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )

        activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppIconFragment)
            ?.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppIconFragment)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarAppIconFragment)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }


        val navigationDrawable = appIconToolBar?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                BlendMode.SRC_ATOP
            )
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                ContextCompat.getColor(requireContext(), id),
                BlendMode.SRC_ATOP
            )
        }
    }
}