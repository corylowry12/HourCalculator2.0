package com.cory.hourcalculator.fragments

import android.animation.LayoutTransition
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.Vibrate
import com.google.android.material.appbar.MaterialToolbar

class FAQFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
            }
            else {
                sortMethodsSubtitle.visibility = View.GONE
                sortMethodChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val systemThemingSubtitle = view.findViewById<TextView>(R.id.systemThemeSubtitle)
        val systemThemingChevron = view.findViewById<ImageView>(R.id.systemThemeChevronImage)
        val systemThemingConstraint = view.findViewById<ConstraintLayout>(R.id.systemThemeConstraint)

        systemThemingConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (systemThemingSubtitle.visibility == View.GONE) {
                systemThemingSubtitle.visibility = View.VISIBLE
                systemThemingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                systemThemingSubtitle.visibility = View.GONE
                systemThemingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val bugReportingSubtitle = view.findViewById<TextView>(R.id.bugReportingSubtitle)
        val bugReportingChevron = view.findViewById<ImageView>(R.id.bugReportingChevronImage)
        val bugReportingConstraint = view.findViewById<ConstraintLayout>(R.id.bugReportingConstraint)

        bugReportingConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            if (bugReportingSubtitle.visibility == View.GONE) {
                bugReportingSubtitle.visibility = View.VISIBLE
                bugReportingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                bugReportingSubtitle.visibility = View.GONE
                bugReportingChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager?.popBackStack()
            }
        })
    }
}