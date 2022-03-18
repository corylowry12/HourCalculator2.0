package com.cory.hourcalculator.fragments

import android.animation.LayoutTransition
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.cory.hourcalculator.classes.FollowSystemVersion
import com.cory.hourcalculator.classes.Vibrate
import com.google.android.material.appbar.MaterialToolbar

class FAQFragment : Fragment() {

    var themeSelection = false

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
                        activity?.setTheme(AccentColor(requireContext()).followSystemTheme(requireContext()))
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
                }
                else {
                    if (themeSelection) {
                        activity?.theme?.applyStyle(R.style.system_accent_google, true)
                    }
                    else {
                        activity?.theme?.applyStyle(R.style.system_accent_google_light, true)
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_f_a_q, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.topAppBarFAQ)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val linearLayout = view.findViewById<LinearLayout>(R.id.linearLayout)
        linearLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        val sortMethodsSubtitle = view.findViewById<TextView>(R.id.sortMethodsSubtitle)
        val sortMethodChevron = view.findViewById<ImageView>(R.id.sortMethodChevronImage)
        val sortMethodsConstraint = view.findViewById<ConstraintLayout>(R.id.sortMethodsConstraint)

        sortMethodsConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (sortMethodsSubtitle.visibility == View.GONE) {
                sortMethodsSubtitle.visibility = View.VISIBLE
                sortMethodChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            } else {
                sortMethodsSubtitle.visibility = View.GONE
                sortMethodChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val systemThemingSubtitle = view.findViewById<TextView>(R.id.systemThemeSubtitle)
        val systemThemingChevron = view.findViewById<ImageView>(R.id.systemThemeChevronImage)
        val systemThemingConstraint =
            view.findViewById<ConstraintLayout>(R.id.systemThemeConstraint)

        systemThemingConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (systemThemingSubtitle.visibility == View.GONE) {
                systemThemingSubtitle.visibility = View.VISIBLE
                systemThemingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            } else {
                systemThemingSubtitle.visibility = View.GONE
                systemThemingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val bugReportingSubtitle = view.findViewById<TextView>(R.id.bugReportingSubtitle)
        val bugReportingChevron = view.findViewById<ImageView>(R.id.bugReportingChevronImage)
        val bugReportingConstraint =
            view.findViewById<ConstraintLayout>(R.id.bugReportingConstraint)

        bugReportingConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (bugReportingSubtitle.visibility == View.GONE) {
                bugReportingSubtitle.visibility = View.VISIBLE
                bugReportingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            } else {
                bugReportingSubtitle.visibility = View.GONE
                bugReportingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager?.popBackStack()
                }
            })
    }
}