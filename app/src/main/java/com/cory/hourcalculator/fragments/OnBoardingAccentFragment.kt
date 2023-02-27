package com.cory.hourcalculator.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor

class OnBoardingAccentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTheme(R.style.Theme_AMOLED)

        activity?.theme?.applyStyle(R.style.teal_accent, true)
        return inflater.inflate(R.layout.fragment_on_boarding_accent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tealRadioButton = view.findViewById<RadioButton>(R.id.tealAccentOnBoarding)
        val pinkRadioButton = view.findViewById<RadioButton>(R.id.pinkAccentOnBoarding)
        val orangeRadioButton = view.findViewById<RadioButton>(R.id.orangeAccentOnBoarding)
        val redRadioButton = view.findViewById<RadioButton>(R.id.redAccentOnBoarding)
        val materialYouRadioButton = view.findViewById<RadioButton>(R.id.materialYouAccentOnBoarding)

        if (Build.VERSION.SDK_INT < 31) {
            materialYouRadioButton.visibility = View.GONE
        }

        if (AccentColor(requireContext()).loadAccent() == 0) {
            tealRadioButton.isChecked = true
        }
        else if (AccentColor(requireContext()).loadAccent() == 1) {
            pinkRadioButton.isChecked = true
        }
        else if (AccentColor(requireContext()).loadAccent() == 2) {
            orangeRadioButton.isChecked = true
        }
        else if (AccentColor(requireContext()).loadAccent() == 3) {
            redRadioButton.isChecked = true
        }
        else if (AccentColor(requireContext()).loadAccent() == 4) {
            materialYouRadioButton.isChecked = true
        }

        tealRadioButton.setOnClickListener {
            AccentColor(requireContext()).setAccentState(0)
        }
        pinkRadioButton.setOnClickListener {
            AccentColor(requireContext()).setAccentState(1)
        }
        orangeRadioButton.setOnClickListener {
            AccentColor(requireContext()).setAccentState(2)
        }
        redRadioButton.setOnClickListener {
            AccentColor(requireContext()).setAccentState(3)
        }
        materialYouRadioButton.setOnClickListener {
            AccentColor(requireContext()).setAccentState(4)
        }
    }
}