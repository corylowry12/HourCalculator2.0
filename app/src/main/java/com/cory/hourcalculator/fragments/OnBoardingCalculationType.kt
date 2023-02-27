package com.cory.hourcalculator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.CalculationType

class OnBoardingCalculationType : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTheme(R.style.Theme_AMOLED)
        activity?.theme?.applyStyle(R.style.teal_accent, true)
        return inflater.inflate(R.layout.fragment_on_boarding_calculation_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val decimalFormatRadioButton = view.findViewById<RadioButton>(R.id.decimalFormat)
        val timeFormatRadioButton = view.findViewById<RadioButton>(R.id.timeFormat)

        if (CalculationType(requireContext()).loadCalculationState()) {
            decimalFormatRadioButton.isChecked = true
        }
        else {
            timeFormatRadioButton.isChecked = true
        }

        decimalFormatRadioButton.setOnClickListener {
            CalculationType(requireContext()).setCalculationState(true)
        }
        timeFormatRadioButton.setOnClickListener {
            CalculationType(requireContext()).setCalculationState(false)
        }
    }
}