package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
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
import android.widget.RadioButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.findFragment
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.ColoredNavBarData
import com.cory.hourcalculator.sharedprefs.ColoredTitleBarTextData
import com.cory.hourcalculator.sharedprefs.DarkThemeData
import com.cory.hourcalculator.sharedprefs.MenuTintData
import com.cory.hourcalculator.sharedprefs.NavigationRailMenuGravityData
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily

class NavigationRailSettings : Fragment() {

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
        return inflater.inflate(R.layout.fragment_navigation_rail_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateCustomColor()

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        val topAppBarNavigationRailSettingsFragment =
            view.findViewById<MaterialToolbar>(R.id.materialToolBarNavigationRailSettingsFragment)
        topAppBarNavigationRailSettingsFragment?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val topCardView =
            view.findViewById<MaterialCardView>(R.id.topNavigationRailCardViewNavigationRailSettings)
        val middleCardView =
            view.findViewById<MaterialCardView>(R.id.middleNavigationRailNavigationRailSettings)
        val bottomCardView =
            view.findViewById<MaterialCardView>(R.id.bottomNavigationRailCardViewNavigationRailSettings)

        topCardView.shapeAppearanceModel = topCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        middleCardView.shapeAppearanceModel = middleCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        bottomCardView.shapeAppearanceModel =
            bottomCardView.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                .setBottomRightCornerSize(28f)
                .setBottomLeftCornerSize(28f)
                .build()

        val topRadioButton = requireActivity().findViewById<RadioButton>(R.id.topPosition)
        val middleRadioButton = requireActivity().findViewById<RadioButton>(R.id.middlePosition)
        val bottomRadioButton = requireActivity().findViewById<RadioButton>(R.id.bottomPosition)

        val navigationRailMenuGravityData = NavigationRailMenuGravityData(requireContext())
        when {
            navigationRailMenuGravityData.loadNavigationRailPosition() == 0 -> {
                topRadioButton?.isChecked = true
            }
            navigationRailMenuGravityData.loadNavigationRailPosition() == 1 -> {
                middleRadioButton?.isChecked = true
            }
            navigationRailMenuGravityData.loadNavigationRailPosition() == 2 -> {
                bottomRadioButton?.isChecked = true
            }
        }

        topCardView.setOnClickListener {
            topRadioButton.isChecked = true
            middleRadioButton.isChecked = false
            bottomRadioButton.isChecked = false

            navigationRailMenuGravityData.setNavigationRailPosition(0)
            val runnable = Runnable {
                (context as MainActivity).setNavigationRailGravity()
            }
            MainActivity().runOnUiThread(runnable)
        }

        middleCardView.setOnClickListener {
            topRadioButton.isChecked = false
            middleRadioButton.isChecked = true
            bottomRadioButton.isChecked = false

            navigationRailMenuGravityData.setNavigationRailPosition(1)
            val runnable = Runnable {
                (context as MainActivity).setNavigationRailGravity()
            }
            MainActivity().runOnUiThread(runnable)
        }

        bottomCardView.setOnClickListener {
            topRadioButton.isChecked = false
            middleRadioButton.isChecked = false
            bottomRadioButton.isChecked = true

            navigationRailMenuGravityData.setNavigationRailPosition(2)
            val runnable = Runnable {
                (context as MainActivity).setNavigationRailGravity()
            }
            MainActivity().runOnUiThread(runnable)
        }
    }

    fun updateCustomColor() {
        requireView().findViewById<CoordinatorLayout>(R.id.NavigationRailSettingsCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val topCardView =
            requireView().findViewById<MaterialCardView>(R.id.topNavigationRailCardViewNavigationRailSettings)
        val middleCardView =
            requireView().findViewById<MaterialCardView>(R.id.middleNavigationRailNavigationRailSettings)
        val bottomCardView =
            requireView().findViewById<MaterialCardView>(R.id.bottomNavigationRailCardViewNavigationRailSettings)

        topCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        middleCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        bottomCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )

        val colors = intArrayOf(
            Color.parseColor("#000000"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        val topRadioButton = requireActivity().findViewById<RadioButton>(R.id.topPosition)
        val middleRadioButton = requireActivity().findViewById<RadioButton>(R.id.middlePosition)
        val bottomRadioButton = requireActivity().findViewById<RadioButton>(R.id.bottomPosition)

        topRadioButton.buttonTintList = ColorStateList(states, colors)
        middleRadioButton.buttonTintList = ColorStateList(states, colors)
        bottomRadioButton.buttonTintList = ColorStateList(states, colors)

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarNavigationRailSettingsFragment)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarNavigationRailSettingsFragment)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }

        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarNavigationRailSettingsFragment)
        collapsingToolbarLayout.setStatusBarScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )
        collapsingToolbarLayout.setContentScrimColor(
            Color.parseColor(
                CustomColorGenerator(
                    requireContext()
                ).generateTopAppBarColor()
            )
        )
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTitleBarExpandedTextColor()))

        val topAppBarTabletSettingsFragment =
            requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarNavigationRailSettingsFragment)
        val resetDrawable = topAppBarTabletSettingsFragment?.menu?.findItem(R.id.reset)?.icon
        val navigationDrawable = topAppBarTabletSettingsFragment?.navigationIcon
        resetDrawable?.mutate()
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            navigationDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                BlendMode.SRC_ATOP
            )
            resetDrawable?.colorFilter = BlendModeColorFilter(
                Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                BlendMode.SRC_ATOP
            )
        } else {
            val darkThemeData = DarkThemeData(requireContext())
            when {
                darkThemeData.loadDarkModeState() == 1 -> {
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#ffffff"),
                        BlendMode.SRC_ATOP
                    )
                    resetDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#ffffff"),
                        BlendMode.SRC_ATOP
                    )
                }
                darkThemeData.loadDarkModeState() == 0 -> {
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#000000"),
                        BlendMode.SRC_ATOP
                    )
                    resetDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#000000"),
                        BlendMode.SRC_ATOP
                    )
                }
                darkThemeData.loadDarkModeState() == 2 -> {
                    navigationDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#ffffff"),
                        BlendMode.SRC_ATOP
                    )
                    resetDrawable?.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#ffffff"),
                        BlendMode.SRC_ATOP
                    )
                }
                darkThemeData.loadDarkModeState() == 3 -> {
                    when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            navigationDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#000000"),
                                BlendMode.SRC_ATOP
                            )
                            resetDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#000000"),
                                BlendMode.SRC_ATOP
                            )
                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            navigationDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#ffffff"),
                                BlendMode.SRC_ATOP
                            )
                            resetDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#ffffff"),
                                BlendMode.SRC_ATOP
                            )
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            navigationDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#ffffff"),
                                BlendMode.SRC_ATOP
                            )
                            resetDrawable?.colorFilter = BlendModeColorFilter(
                                Color.parseColor("#ffffff"),
                                BlendMode.SRC_ATOP
                            )
                        }
                    }
                }
            }
        }
    }
}