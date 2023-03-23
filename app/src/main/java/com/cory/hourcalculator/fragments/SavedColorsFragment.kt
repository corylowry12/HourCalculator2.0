package com.cory.hourcalculator.fragments

import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.PatchNotesAdapter
import com.cory.hourcalculator.adapters.SavedColorsAdapter
import com.cory.hourcalculator.classes.*
import com.cory.hourcalculator.intents.MainActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar

class SavedColorsFragment : Fragment() {

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
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_saved_colors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBarSavedColors = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarSavedColorFragment)
        topAppBarSavedColors.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        updateCustomColor()

        val savedColorsRecyclerView = requireActivity().findViewById<RecyclerView>(R.id.savedColorRecyclerView)
        savedColorsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        savedColorsRecyclerView.adapter =
            SavedColorsAdapter(requireContext(), UserAddedColors(requireContext()).loadColors()!!, this)
    }

    fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.savedColorCoordinatorLayout).setBackgroundColor(
            Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))

        val savedColorsCollapsingToolbarLayout = requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarSavedColorFragment)
        savedColorsCollapsingToolbarLayout.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        savedColorsCollapsingToolbarLayout.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        val topAppBarSavedColors = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarSavedColorFragment)

        val navigationDrawable = topAppBarSavedColors?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                BlendMode.SRC_ATOP
            )
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

        val runnable = Runnable {
            (context as MainActivity).updateBottomNavCustomColor()
        }
        MainActivity().runOnUiThread(runnable)

        if (ColoredNavBarData(requireContext()).loadNavBar()) {
            activity?.window?.navigationBarColor =
                Color.parseColor(CustomColorGenerator(requireContext()).generateNavBarColor())
        }

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            savedColorsCollapsingToolbarLayout?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            savedColorsCollapsingToolbarLayout?.setCollapsedTitleTextColor(
                ContextCompat.getColor(requireContext(), id))
        }
    }

    fun itemCountZero() {
        activity?.supportFragmentManager?.popBackStack()
    }
}