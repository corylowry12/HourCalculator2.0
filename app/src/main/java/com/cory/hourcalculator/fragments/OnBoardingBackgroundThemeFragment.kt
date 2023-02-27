package com.cory.hourcalculator.fragments

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.cory.hourcalculator.classes.FollowSystemThemeChoice
import com.cory.hourcalculator.classes.FollowSystemVersion

class OnBoardingBackgroundThemeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTheme(R.style.Theme_AMOLED)

        activity?.theme?.applyStyle(R.style.teal_accent, true)
        return inflater.inflate(R.layout.fragment_on_boarding_background_theme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lightThemeRadioButton = view.findViewById<RadioButton>(R.id.lightThemeOnBoarding)
        val darkThemeRadioButton = view.findViewById<RadioButton>(R.id.darkThemeOnBoarding)
        val blackThemeRadioButton = view.findViewById<RadioButton>(R.id.blackThemeOnBoarding)
        val followSystemRadioButton = view.findViewById<RadioButton>(R.id.followSystemOnBoarding)

        if (DarkThemeData(requireContext()).loadDarkModeState() == 0) {
            lightThemeRadioButton.isChecked = true
        }
        else if (DarkThemeData(requireContext()).loadDarkModeState() == 1) {
            darkThemeRadioButton.isChecked = true
        }
        else if (DarkThemeData(requireContext()).loadDarkModeState() == 2) {
            blackThemeRadioButton.isChecked = true
        }
        else if (DarkThemeData(requireContext()).loadDarkModeState() == 3) {
            followSystemRadioButton.isChecked = true
        }

        lightThemeRadioButton.setOnClickListener {
            DarkThemeData(requireContext()).setDarkModeState(0)
        }
        darkThemeRadioButton.setOnClickListener {
            DarkThemeData(requireContext()).setDarkModeState(1)
        }
        blackThemeRadioButton.setOnClickListener {
            DarkThemeData(requireContext()).setDarkModeState(2)
        }
        followSystemRadioButton.setOnClickListener {
            DarkThemeData(requireContext()).setDarkModeState(3)
        }
    }
}