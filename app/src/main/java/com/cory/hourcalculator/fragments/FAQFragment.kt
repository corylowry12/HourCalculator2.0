package com.cory.hourcalculator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
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

        val sortMethodsCardView = view.findViewById<CardView>(R.id.sortMethodsCardView)
        val sortMethodsHeading = view.findViewById<TextView>(R.id.sortMethodsHeading)
        val sortMethodsSubtitle = view.findViewById<TextView>(R.id.sortMethodsSubtitle)
        val sortMethodChevron = view.findViewById<ImageView>(R.id.sortMethodChevronImage)

        sortMethodsCardView.setOnClickListener {
            if (sortMethodsSubtitle.visibility == View.GONE) {
                sortMethodsSubtitle.visibility = View.VISIBLE
                sortMethodChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                sortMethodsSubtitle.visibility = View.GONE
                sortMethodChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        sortMethodsSubtitle.setOnClickListener {
            if (sortMethodsSubtitle.visibility == View.GONE) {
                sortMethodsSubtitle.visibility = View.VISIBLE
                sortMethodChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                sortMethodsSubtitle.visibility = View.GONE
                sortMethodChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        sortMethodChevron.setOnClickListener {
            if (sortMethodsSubtitle.visibility == View.GONE) {
                sortMethodsSubtitle.visibility = View.VISIBLE
                sortMethodChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                sortMethodsSubtitle.visibility = View.GONE
                sortMethodChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        sortMethodsHeading.setOnClickListener {
            if (sortMethodsSubtitle.visibility == View.GONE) {
                sortMethodsSubtitle.visibility = View.VISIBLE
                sortMethodChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                sortMethodsSubtitle.visibility = View.GONE
                sortMethodChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager?.popBackStack()
            }
        })
    }
}