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
import androidx.core.content.ContextCompat
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
import com.google.android.material.chip.Chip
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class PatchNotesFragment : Fragment() {

    private var bugFixesArray = arrayOf("Fixed issue where when resetting \'App Settings\' to default it would uncheck the Vibration switch even though vibration is enabled after resetting", "Fixed issue with there being no vibration when long clicking items in history view",
                                        "Fixed issue with there being no vibration when clicking \"Hide\" to hide checkboxes", "Fixed issue with there being no vibration when clicking \"Report A Bug\" in the about app view", "Fixed issue with the shape of the floating action button in the history view",
                                        "Fixed crashing when trying to leave the edit hour view if there were pending changes and you clicked yes to save them", "Fixed issue with title and menu buttons not being centered properly in title bars")

    private var newFeaturesArray = arrayOf("Added the ability to click on the history tab when the history view is active and scroll to the top", "Added the ability to long press the calculate button and calculate in time format in the home view and edit hour view without having it enabled in App Settings")

    private var enhancementsArray = arrayOf("Enabling and disabling history will now use a switch instead of radio buttons", "The switches in the settings will now have an icon based on their value", "Converted dialogs throughout the app to be Bottom Sheets",
                                            "Made the entire view clickable when selecting an app icon instead of just the check box", "Made the entire view clickable when selecting multiple hours to delete, it will function the same way as the checkbox (clicking will select and long clicking will select all)",
                                            "App will now show \"Error\" if there was a problem calculating wages", "App will now calculate hours via the info dialog in the history view to show HOURS:MINUTES if there is no decimal time stored",
                                            "Optimizations when launching the app", "Optimizations in switching tabs", "Improved the quality of the app icon logos in the Appearance Settings view", "Status bar will now match the color of the theme and will change color based on scroll position",
                                            "App icon radio buttons will no be checked if you click the icon but not the radio button", "App will no longer let you go to another tab when edit view is visible to prevent accidentally leaving and losing edited data",
                                            "Adjusted left and right margins for the output text view on the home view")

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

        val bugFixesChip = requireView().findViewById<Chip>(R.id.bugFixesChip)
        bugFixesChip.text = bugFixesArray.count().toString()

        val newFeaturesChip = requireView().findViewById<Chip>(R.id.newFeaturesChip)
        newFeaturesChip.text = newFeaturesArray.count().toString()

        val enhancementsChip = requireView().findViewById<Chip>(R.id.enhancementsChip)
        enhancementsChip.text = enhancementsArray.count().toString()

        val bugFixesConstraint = requireView().findViewById<ConstraintLayout>(R.id.bugFixesConstraint)

        bugFixesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val bugFixesRecyclerView = requireView().findViewById<RecyclerView>(R.id.bugFixesRecyclerView)
            bugFixesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            bugFixesRecyclerView.adapter = PatchNotesBugFixesAdapter(requireContext(), bugFixesArray)

            if (bugFixesRecyclerView.visibility == View.GONE) {
                bugFixesRecyclerView.visibility = View.VISIBLE
                bugFixesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                bugFixesRecyclerView.visibility = View.GONE
                bugFixesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val newFeaturesConstraint = requireView().findViewById<ConstraintLayout>(R.id.newFeaturesConstraint)

        newFeaturesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val newFeaturesRecyclerView = requireView().findViewById<RecyclerView>(R.id.newFeaturesRecyclerView)
            newFeaturesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            newFeaturesRecyclerView.adapter = PatchNotesNewFeaturesAdapter(requireContext(), newFeaturesArray)

            if (newFeaturesRecyclerView.visibility == View.GONE) {
                newFeaturesRecyclerView.visibility = View.VISIBLE
                newFeaturesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                newFeaturesRecyclerView.visibility = View.GONE
                newFeaturesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        val enhancementsConstraint = requireView().findViewById<ConstraintLayout>(R.id.enhancementsConstraint)

        enhancementsConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val enhancementsRecyclerView = requireView().findViewById<RecyclerView>(R.id.enhancementsRecyclerView)
            enhancementsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            enhancementsRecyclerView.adapter = PatchNotesEnhancementsAdapter(requireContext(), enhancementsArray)

            if (enhancementsRecyclerView.visibility == View.GONE) {
                enhancementsRecyclerView.visibility = View.VISIBLE
                enhancementsChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                enhancementsRecyclerView.visibility = View.GONE
                enhancementsChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        topAppBar?.setOnMenuItemClickListener {
            Vibrate().vibration(requireContext())
            when (it.itemId) {
                R.id.closeAll -> {

                    val bugFixesRecyclerView = requireView().findViewById<RecyclerView>(R.id.bugFixesRecyclerView)
                    val newFeaturesRecyclerView = requireView().findViewById<RecyclerView>(R.id.newFeaturesRecyclerView)
                    val enhancementsRecyclerView = requireView().findViewById<RecyclerView>(R.id.enhancementsRecyclerView)

                    if (bugFixesRecyclerView.visibility == View.VISIBLE) {
                        bugFixesRecyclerView.visibility = View.GONE
                        bugFixesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }
                    if (newFeaturesRecyclerView.visibility == View.VISIBLE) {
                        newFeaturesRecyclerView.visibility = View.GONE
                        newFeaturesChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }

                    if (enhancementsRecyclerView.visibility == View.VISIBLE) {
                        enhancementsRecyclerView.visibility = View.GONE
                        enhancementsChip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24)
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