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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CustomColorGenerator
import com.cory.hourcalculator.classes.Vibrate
import com.cory.hourcalculator.database.DBHelper
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.sharedprefs.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.CornerFamily

class AnimationSettingsFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_animation_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runnable = Runnable {
            (activity as MainActivity).currentTab = 3
            (activity as MainActivity).setActiveTab(3)
        }
        MainActivity().runOnUiThread(runnable)

        val topAppBar =
            activity?.findViewById<MaterialToolbar>(R.id.materialToolbarAnimationSettings)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        updateCustomColor()

        val historyRecyclerViewLoadingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.historyListLoadingAnimationCardView)
        val timeCardRecyclerViewLoadingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.timeCardListLoadingAnimationCardView)

        historyRecyclerViewLoadingAnimationCardView.shapeAppearanceModel = historyRecyclerViewLoadingAnimationCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 28f)
            .setTopRightCorner(CornerFamily.ROUNDED, 28f)
            .setBottomRightCornerSize(0f)
            .setBottomLeftCornerSize(0f)
            .build()
        timeCardRecyclerViewLoadingAnimationCardView.shapeAppearanceModel = timeCardRecyclerViewLoadingAnimationCardView.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCornerSize(28f)
            .setBottomLeftCornerSize(28f)
            .build()

        val historyLoadingAnimation = HistoryRecyclerViewLoadingAnimationData(requireContext())
        val toggleHistoryLoadingAnimation = requireActivity().findViewById<MaterialSwitch>(R.id.historyListLoadingAnimationSwitch)

        if (historyLoadingAnimation.loadHistoryRecyclerViewLoadingAnimation()) {
            toggleHistoryLoadingAnimation?.isChecked = true
            toggleHistoryLoadingAnimation?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!historyLoadingAnimation.loadHistoryRecyclerViewLoadingAnimation()) {
            toggleHistoryLoadingAnimation?.isChecked = false
            toggleHistoryLoadingAnimation?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        historyRecyclerViewLoadingAnimationCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleHistoryLoadingAnimation.isChecked = !toggleHistoryLoadingAnimation.isChecked
            if (toggleHistoryLoadingAnimation.isChecked) {
                toggleHistoryLoadingAnimation.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleHistoryLoadingAnimation?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            historyLoadingAnimation.setHistoryRecyclerViewLoadingAnimation(toggleHistoryLoadingAnimation.isChecked)
        }

        val timeCardLoadingAnimation = TimeCardRecyclerViewLoadingAnimationData(requireContext())
        val toggleTimeCardLoadingAnimationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.timeCardListLoadingAnimationSwitch)

        if (timeCardLoadingAnimation.loadTimeCardRecyclerViewLoadingAnimation()) {
            toggleTimeCardLoadingAnimationSwitch?.isChecked = true
            toggleTimeCardLoadingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
        } else if (!timeCardLoadingAnimation.loadTimeCardRecyclerViewLoadingAnimation()) {
            toggleTimeCardLoadingAnimationSwitch?.isChecked = false
            toggleTimeCardLoadingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
        }

        timeCardRecyclerViewLoadingAnimationCardView?.setOnClickListener {
            Vibrate().vibration(requireContext())
            toggleTimeCardLoadingAnimationSwitch.isChecked = !toggleTimeCardLoadingAnimationSwitch.isChecked
            if (toggleTimeCardLoadingAnimationSwitch.isChecked) {
                toggleTimeCardLoadingAnimationSwitch.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_16)
            } else {
                toggleTimeCardLoadingAnimationSwitch?.thumbIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_16)
            }
            timeCardLoadingAnimation.setTimeCardRecyclerViewLoadingAnimation(toggleTimeCardLoadingAnimationSwitch.isChecked)
        }
    }

    fun updateCustomColor() {
        requireActivity().findViewById<CoordinatorLayout>(R.id.animationSettingsCoordinatorLayout).setBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateBackgroundColor()))

        val historyRecyclerViewLoadingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.historyListLoadingAnimationCardView)
        historyRecyclerViewLoadingAnimationCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
        val timeCardRecyclerViewLoadingAnimationCardView = requireActivity().findViewById<MaterialCardView>(R.id.timeCardListLoadingAnimationCardView)
        timeCardRecyclerViewLoadingAnimationCardView.setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))

        val toggleHistoryLoadingAnimationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.historyListLoadingAnimationSwitch)
        val timeCardLoadingAnimationSwitch = requireActivity().findViewById<MaterialSwitch>(R.id.timeCardListLoadingAnimationSwitch)
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        )

        val colors = intArrayOf(
            Color.parseColor("#e7dfec"),
            Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary())
        )

        toggleHistoryLoadingAnimationSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        toggleHistoryLoadingAnimationSwitch.trackTintList = ColorStateList(states, colors)
        timeCardLoadingAnimationSwitch.thumbIconTintList = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        timeCardLoadingAnimationSwitch.trackTintList = ColorStateList(states, colors)

        val collapsingToolbarAnimationSettings = requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAnimationSettings)
        collapsingToolbarAnimationSettings.setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
        collapsingToolbarAnimationSettings.setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))

        val topAppBar =
            activity?.findViewById<MaterialToolbar>(R.id.materialToolbarAnimationSettings)

        val navigationDrawable = topAppBar?.navigationIcon
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

        if (ColoredTitleBarTextData(requireContext()).loadTitleBarTextState()) {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAnimationSettings)?.setCollapsedTitleTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCollapsedToolBarTextColor()))
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAnimationSettings)?.setCollapsedTitleTextColor(ContextCompat.getColor(requireContext(), id))
        }
    }
}