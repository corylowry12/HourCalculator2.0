package com.cory.hourcalculator.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.AccentColor
import com.cory.hourcalculator.classes.DarkThemeData
import com.cory.hourcalculator.classes.Version
import com.cory.hourcalculator.classes.Vibrate
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class PatchNotesFragment : Fragment() {

    private var newFeaturesBool = false
    private var enhancementsBool = false
    private var bugFixesBool = false

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
                    Configuration.UI_MODE_NIGHT_NO -> activity?.setTheme(R.style.Theme_MyApplication)
                    Configuration.UI_MODE_NIGHT_YES -> activity?.setTheme(R.style.Theme_AMOLED)
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> activity?.setTheme(R.style.Theme_AMOLED)
                }
            }
        }

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
        return inflater.inflate(R.layout.fragment_patch_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarPatchNotes)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.closeAll -> {
                    newFeaturesBool = true
                    enhancementsBool = true
                    bugFixesBool = true
                    showBugFixes()
                    showEnhancements()
                    showNewFeatures()
                    true
                }
                else -> true
            }
        }

        Version(requireContext()).setVersion(getString(R.string.version_number))

        val runnable = Runnable {
            (context as MainActivity).changeSettingsBadge()
        }
        MainActivity().runOnUiThread(runnable)

        val constraintBugFixes = view.findViewById<ConstraintLayout>(R.id.bugFixesConstraint)

        constraintBugFixes.setOnClickListener {
            Vibrate().vibration(requireContext())
            showBugFixes()
        }

        val newFeaturesConstraint = view.findViewById<ConstraintLayout>(R.id.newFeaturesConstraint)

        newFeaturesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            showNewFeatures()
        }

        val enhancementsConstraintLayout =
            view.findViewById<ConstraintLayout>(R.id.enhancementsConstraint)

        enhancementsConstraintLayout.setOnClickListener {
            Vibrate().vibration(requireContext())
            showEnhancements()
        }
    }

    private fun showBugFixes() {
        bugFixesBool = !bugFixesBool
        val enhancementsChevronImage = view?.findViewById<ImageView>(R.id.bugFixesChevronImage)
        val enhancementsArray = arrayOf(
            R.id.bugFixesCardView1,
            R.id.bugFixesCardView2,
            R.id.bugFixesCardView3,
            R.id.bugFixesCardView4,
        )
        if (bugFixesBool) {
            for (i in 0 until enhancementsArray.count()) {
                view?.findViewById<CardView>(enhancementsArray.elementAt(i))?.visibility =
                    View.VISIBLE
            }
            enhancementsChevronImage?.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
        } else {
            for (i in 0 until enhancementsArray.count()) {
                view?.findViewById<CardView>(enhancementsArray.elementAt(i))?.visibility = View.GONE
            }
            enhancementsChevronImage?.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        }
    }

    private fun showNewFeatures() {
        newFeaturesBool = !newFeaturesBool
        val newFeaturesChevronImage = view?.findViewById<ImageView>(R.id.newFeaturesChevronImage)
        val bugFixesArray = arrayOf(
            R.id.newFeaturesCardView1
        )
        if (newFeaturesBool) {
            for (i in 0 until bugFixesArray.count()) {
                view?.findViewById<CardView>(bugFixesArray.elementAt(i))?.visibility = View.VISIBLE
            }
            newFeaturesChevronImage?.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
        } else {
            for (i in 0 until bugFixesArray.count()) {
                view?.findViewById<CardView>(bugFixesArray.elementAt(i))?.visibility = View.GONE
            }
            newFeaturesChevronImage?.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        }
    }

    private fun showEnhancements() {
        enhancementsBool = !enhancementsBool
        val enhancementsChevronImage = view?.findViewById<ImageView>(R.id.enhancementsChevronImage)
        val enhancementsArray = arrayOf(
            R.id.enhancementsCardView1
        )
        if (enhancementsBool) {
            for (i in 0 until enhancementsArray.count()) {
                view?.findViewById<CardView>(enhancementsArray.elementAt(i))?.visibility =
                    View.VISIBLE
            }
            enhancementsChevronImage?.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
        } else {
            for (i in 0 until enhancementsArray.count()) {
                view?.findViewById<CardView>(enhancementsArray.elementAt(i))?.visibility = View.GONE
            }
            enhancementsChevronImage?.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        }
    }
}