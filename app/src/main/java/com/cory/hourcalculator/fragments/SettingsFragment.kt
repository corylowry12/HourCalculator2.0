package com.cory.hourcalculator.fragments

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val accentColor = AccentColor(requireContext())
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
                activity?.theme?.applyStyle(R.style.system_accent, true)
            }
        }
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appearanceHeading = activity?.findViewById<TextView>(R.id.themeHeading)
        val appearanceSubtitle = activity?.findViewById<TextView>(R.id.themeSubtitle)
        val appearanceImage = activity?.findViewById<ImageView>(R.id.appearanceImage)
        val appearanceCardView = activity?.findViewById<CardView>(R.id.themeCardView)

        appearanceHeading?.setOnClickListener {
            openAppearanceFragment()
        }

        appearanceSubtitle?.setOnClickListener {
            openAppearanceFragment()
        }

        appearanceImage?.setOnClickListener {
            openAppearanceFragment()
        }

        appearanceCardView?.setOnClickListener {
            openAppearanceFragment()
        }

        val layoutSettingsHeading = activity?.findViewById<TextView>(R.id.layout)
        val layoutSettingsSubtitle = activity?.findViewById<TextView>(R.id.layoutSubtitle)
        val layoutSettingsCardView = activity?.findViewById<CardView>(R.id.layoutCardView)
        val layoutSettingsImageView = activity?.findViewById<ImageView>(R.id.layoutImage)

        layoutSettingsHeading?.setOnClickListener {
            openLayoutSettingsFragment()
        }
        layoutSettingsSubtitle?.setOnClickListener {
            openLayoutSettingsFragment()
        }
        layoutSettingsCardView?.setOnClickListener {
            openLayoutSettingsFragment()
        }
        layoutSettingsImageView?.setOnClickListener {

        }

        val automaticDeletionHeading = activity?.findViewById<TextView>(R.id.deletionHeading)

        automaticDeletionHeading?.setOnClickListener {
            openAutomaticDeletionFragment()
        }

        val patchNotesHeading = activity?.findViewById<TextView>(R.id.patchNotesHeading)
        val patchNotesSubtitle = activity?.findViewById<TextView>(R.id.patchNotesSubtitle)
        val patchNotesImage = activity?.findViewById<ImageView>(R.id.patchNotesImage)
        val patchNotesCardView = activity?.findViewById<CardView>(R.id.patchNotesCardView)

        patchNotesHeading?.setOnClickListener {
            openPatchNotesFragment()
        }
        patchNotesSubtitle?.setOnClickListener {
            openPatchNotesFragment()
        }
        patchNotesImage?.setOnClickListener {
            openPatchNotesFragment()
        }
        patchNotesCardView?.setOnClickListener {
            openPatchNotesFragment()
        }
    }

    fun openAppearanceFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()

        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction?.replace(R.id.fragment_container, AppearanceFragment())?.addToBackStack(null)
        transaction?.commit()
    }

    fun openLayoutSettingsFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()

        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction?.replace(R.id.fragment_container, LayoutSettingsFragment())?.addToBackStack(null)
        transaction?.commit()
    }

    fun openAutomaticDeletionFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()

        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction?.replace(R.id.fragment_container, AutomaticDeletionFragment())?.addToBackStack(null)
        transaction?.commit()
    }

    fun openPatchNotesFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()

        transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction?.replace(R.id.fragment_container, PatchNotesFragment())?.addToBackStack(null)
        transaction?.commit()
    }
}