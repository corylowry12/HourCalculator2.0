package com.cory.hourcalculator.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.PatchNotesBugFixesAdapter
import com.cory.hourcalculator.adapters.PatchNotesEnhancementsAdapter
import com.cory.hourcalculator.adapters.PatchNotesNewFeaturesAdapter
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class PatchNotesFragment : Fragment() {

    private var bugFixesArray = arrayOf("Fixed issue with text color on splash screen not being set properly in light theme only")

    private var newFeaturesArray = arrayOf("Added a setting in Settings->App Settings to allow you to have out time automatically set to current time or previously stored time")

    private var enhancementsArray = arrayOf("Dependency Updates", "Various Optimizations")

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
        return inflater.inflate(R.layout.fragment_patch_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarPatchNotes)

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val bugFixesCounterTextView = requireView().findViewById<TextView>(R.id.bugFixesCounterTextView)
        bugFixesCounterTextView.text = bugFixesArray.count().toString()

        val newFeaturesCounterTextView = requireView().findViewById<TextView>(R.id.newFeaturesCounterTextView)
        newFeaturesCounterTextView.text = newFeaturesArray.count().toString()

        val enhancementsCounterTextView = requireView().findViewById<TextView>(R.id.enhancementsCounterTextView)
        enhancementsCounterTextView.text = enhancementsArray.count().toString()

        val bugFixesConstraint = requireView().findViewById<ConstraintLayout>(R.id.bugFixesConstraint)

        bugFixesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val bugFixesRecyclerView = requireView().findViewById<RecyclerView>(R.id.bugFixesRecyclerView)
            bugFixesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            bugFixesRecyclerView.adapter = PatchNotesBugFixesAdapter(requireContext(), bugFixesArray)
            val bugFixesChevron = requireView().findViewById<ImageView>(R.id.bugFixesChevronImage)

            if (bugFixesRecyclerView.visibility == View.GONE) {
                bugFixesRecyclerView.visibility = View.VISIBLE
                bugFixesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                bugFixesRecyclerView.visibility = View.GONE
                bugFixesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val newFeaturesConstraint = requireView().findViewById<ConstraintLayout>(R.id.newFeaturesConstraint)

        newFeaturesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val newFeaturesRecyclerView = requireView().findViewById<RecyclerView>(R.id.newFeaturesRecyclerView)
            newFeaturesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            newFeaturesRecyclerView.adapter = PatchNotesNewFeaturesAdapter(requireContext(), newFeaturesArray)
            val newFeaturesChevron = requireView().findViewById<ImageView>(R.id.newFeaturesChevronImage)

            if (newFeaturesRecyclerView.visibility == View.GONE) {
                newFeaturesRecyclerView.visibility = View.VISIBLE
                newFeaturesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                newFeaturesRecyclerView.visibility = View.GONE
                newFeaturesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val enhancementsConstraint = requireView().findViewById<ConstraintLayout>(R.id.enhancementsConstraint)

        enhancementsConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val enhancementsRecyclerView = requireView().findViewById<RecyclerView>(R.id.enhancementsRecyclerView)
            enhancementsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            enhancementsRecyclerView.adapter = PatchNotesEnhancementsAdapter(requireContext(), enhancementsArray)
            val enhancementsChevron = requireView().findViewById<ImageView>(R.id.enhancementsChevronImage)

            if (enhancementsRecyclerView.visibility == View.GONE) {
                enhancementsRecyclerView.visibility = View.VISIBLE
                enhancementsChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                enhancementsRecyclerView.visibility = View.GONE
                enhancementsChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.closeAll -> {

                    val bugFixesRecyclerView = requireView().findViewById<RecyclerView>(R.id.bugFixesRecyclerView)
                    val newFeaturesRecyclerView = requireView().findViewById<RecyclerView>(R.id.newFeaturesRecyclerView)
                    val enhancementsRecyclerView = requireView().findViewById<RecyclerView>(R.id.enhancementsRecyclerView)

                    val bugFixesChevron = requireView().findViewById<ImageView>(R.id.bugFixesChevronImage)
                    val newFeaturesChevron = requireView().findViewById<ImageView>(R.id.newFeaturesChevronImage)
                    val enhancementsChevron = requireView().findViewById<ImageView>(R.id.enhancementsChevronImage)

                    if (bugFixesRecyclerView.visibility == View.VISIBLE) {
                        bugFixesRecyclerView.visibility = View.GONE
                        bugFixesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }
                    if (newFeaturesRecyclerView.visibility == View.VISIBLE) {
                        newFeaturesRecyclerView.visibility = View.GONE
                        newFeaturesChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }

                    if (enhancementsRecyclerView.visibility == View.VISIBLE) {
                        enhancementsRecyclerView.visibility = View.GONE
                        enhancementsChevron.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }

                    val handler = Handler(Looper.getMainLooper())
                    val runnable = Runnable {
                        val collapsingToolbarLayoutPatchNotes = requireView().findViewById<AppBarLayout>(R.id.appBarLayoutPatchNotes)
                        collapsingToolbarLayoutPatchNotes.setExpanded(true, true)
                    }
                    handler.postDelayed(runnable, 50)
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
    }
}