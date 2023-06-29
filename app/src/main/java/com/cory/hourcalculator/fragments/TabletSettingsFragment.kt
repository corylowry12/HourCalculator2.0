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

class TabletSettingsFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_tablet_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateCustomColor()

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        val topAppBarTabletSettingsFragment =
            view.findViewById<MaterialToolbar>(R.id.materialToolBarTabletSettingsFragment)
        topAppBarTabletSettingsFragment?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val navigationSettingsCardView =
            view.findViewById<MaterialCardView>(R.id.navigationSettingsCardView)

        navigationSettingsCardView.shapeAppearanceModel = navigationSettingsCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()


        navigationSettingsCardView.setOnClickListener {
            openFragment(NavigationRailSettings())
        }
    }

    fun updateCustomColor() {
        requireView().findViewById<CoordinatorLayout>(R.id.tabletSettingsCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))
        val navigationRailSettingsCardView =
            requireView().findViewById<MaterialCardView>(R.id.navigationSettingsCardView)

        navigationRailSettingsCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarTabletSettingsFragment)
                ?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        } else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarTabletSettingsFragment)
                ?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }

        val collapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarTabletSettingsFragment)
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
            requireActivity().findViewById<MaterialToolbar>(R.id.materialToolBarTabletSettingsFragment)
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
    private fun openFragment(fragment: Fragment) {
        Vibrate().vibration(requireContext())

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        transaction?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)
        transaction?.commit()
    }
}