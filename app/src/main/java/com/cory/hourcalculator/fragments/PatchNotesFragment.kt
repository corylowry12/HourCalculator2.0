package com.cory.hourcalculator.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.hourcalculator.BuildConfig
import com.cory.hourcalculator.intents.MainActivity
import com.cory.hourcalculator.R
import com.cory.hourcalculator.adapters.PatchNotesBugFixesAdapter
import com.cory.hourcalculator.adapters.PatchNotesEnhancementsAdapter
import com.cory.hourcalculator.adapters.PatchNotesNewFeaturesAdapter
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class PatchNotesFragment : Fragment() {

    private var bugFixesArray = arrayOf("Fixed issue where when resetting \'App Settings\' to default it would uncheck the Vibration switch even though vibration is enabled after resetting", "Fixed issue with there being no vibration when long clicking items in history view",
                                        "Fixed issue with there being no vibration when clicking \"Hide\" to hide checkboxes", "Fixed issue with there being no vibration when clicking \"Report A Bug\" in the about app view", "Fixed issue with the shape of the floating action button in the history view",
                                        "Fixed crashing when trying to leave the edit hour view if there were pending changes and you clicked yes to save them", "Fixed issue with title and menu buttons not being centered properly in title bars",
                                        "Fixed issue with github button in about view still using built in web view instead of custom tab", "Fixed issue when hitting back button and the bottom nav bar wouldn't change selected tab")

    private var newFeaturesArray = arrayOf("Added the ability to click on the history tab when the history view is active and scroll to the top", "Added the ability to calculate in time or decimal format depending on which is enabled by long pressing the calculate button (eg. if decimal format is enabled, long pressing will calculate in time format)",
                                            "Added the ability to click on an hour entry and open it to edit it (disabled by default, to enable go to Settings->App Settings->Open hour for editing on history item click)", "Added a toggle to change menu item tint to match the theme")

    private var enhancementsArray = arrayOf("Enabling and disabling history will now use a switch instead of radio buttons", "The switches in the settings will now have an icon based on their value", "Converted dialogs throughout the app to be Bottom Sheets",
                                            "Made the entire view clickable when selecting an app icon instead of just the check box", "Made the entire view clickable when selecting multiple hours to delete, it will function the same way as the checkbox (clicking will select and long clicking will select all)",
                                            "App will now show \"Error\" if there was a problem calculating wages", "App will now calculate hours via the info dialog in the history view to show HOURS:MINUTES if there is no decimal time stored",
                                            "Optimizations when launching the app", "Optimizations in switching tabs", "Improved the quality of the app icon logos in the Appearance Settings view", "Status bar will now match the color of the theme and will change color based on scroll position",
                                            "App icon radio buttons will no be checked if you click the icon but not the radio button", "App will no longer let you go to another tab when edit view is visible to prevent accidentally leaving and losing edited data",
                                            "Adjusted left and right margins for the output text view on the home view", "App will now only use custom tabs instead of the previous built in web view", "Custom tabs will now match the accent color of the app", "Changed red 1 logo when there was a new update in patch notes setting item",
                                            "Updated themed icon to match the other regular icons", "Changed the chip color in the patch notes and time cards view to match the theming better", "Delete menu item in the edit view is now an icon instead of a drop down menu",
                                            "Removed auto icon theming, you will now just have to manually pick an app icon, this way the app doesn't have to restart every time you change a theme")

    private var bugFixesArrayInternal = arrayOf("Fixed issue with app crashing when trying to view image in time card info view", "Fixed issue with date picker dialog in the edit hours view being the wrong theme", "Fixed issue with radio buttons in the background color view not matching the custom color",
                                                "Fixed issue with the sort bottom sheet in the history view not matching the custom color", "Fixed issue with the info bottom sheet in the history view not matching the custom color", "Fixed issue with the history options button sheet not matching the custom color",
                                                "Fixed issue with the outline of the radio buttons in the history view being the wrong color when not selected", "Fixed issue with the delete entry bottom sheet in the edit hour view not matching the custom theme", "Fixed issue with the restore scroll position button not matching " +
                                                "the custom color theme in history view", "Fixed issue with the restore scroll position button not matching the custom color theme in the time card view", "Fixed issue with the time card info view not matching the custom theme",
                                                "Fixed issue with background color view always setting to the custom color theme when another theme was selected", "Reset background theme setting, resulting in broken themes for some ui elements",
                                                "Fixed issue with the accent color circle in the appearance view being transparent when a custom theme was enabled", "Fixed issue where if you disabled the history in the app settings view and left the view and came back, the switch would be checked even tho it was disabled",
                                                "Fixed issue where if you cancelled the custom color picker dialog, the custom radio button would still be checked", "Fixed issue with there being no vibration when clicking the custom radio button", "Fixed issue with there being no vibration when click the select custom color button",
                                                "Fixed issue with restart app bottom sheet not matching custom theme when clicking the teal accent color button", "Fixed issue with switches being slightly purple if you long pressed on them", "Fixed issue with the About app view not matching custom theme",
                                                "Fixed issue with card views in the time card options (accessed by long pressing on a time card entry) not matching the custom theme", "Fixed issue with highlight color in the time card options bottom sheet not matching the curves of the card views",
                                                "Fixed issue with bottom sheet when changing app icon not matching the custom color")

    private var newFeaturesArrayInternal = arrayOf("No new features")

    private var enhancementsArrayInternal = arrayOf("Removed auto icon theming, you will now just have to manually pick an app icon, this way the app doesn't have to restart every time you change a theme", "Made stroke around the enabled app icon slightly smaller", "Added a cancel button to the custom color bottom sheet",
                                                    "Adjusted margin for the buttons in the custom color picker bottom sheet", "Converted the pink, orange, red, and material you accent color buttons to use a restart bottom sheet warning", "Converted the different material you theming switch to use a bottom sheet",
                                                    "Added icons to items in the Appearance view to let you know the item is clickable", "Tweaked design of the restart app warning bottom sheet view")

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
            accentColor.loadAccent() == 0 || accentColor.loadAccent() == 5 -> {
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

        if (AccentColor(requireContext()).loadAccent() == 5) {
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutPatchNotes).setContentScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutPatchNotes).setStatusBarScrimColor(Color.parseColor(CustomColorGenerator(requireContext()).generateTopAppBarColor()))
            requireActivity().findViewById<MaterialCardView>(R.id.bugFixesOuterCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            requireActivity().findViewById<MaterialCardView>(R.id.newFeaturesOuterCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            requireActivity().findViewById<MaterialCardView>(R.id.enhancementsOuterCardView).setCardBackgroundColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            requireActivity().findViewById<Chip>(R.id.bugFixesChip).setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            requireActivity().findViewById<Chip>(R.id.bugFixesChip).closeIconTint = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            requireActivity().findViewById<Chip>(R.id.bugFixesChip).chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
            requireActivity().findViewById<Chip>(R.id.newFeaturesChip).setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            requireActivity().findViewById<Chip>(R.id.newFeaturesChip).closeIconTint = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            requireActivity().findViewById<Chip>(R.id.newFeaturesChip).chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
            requireActivity().findViewById<Chip>(R.id.enhancementsChip).setTextColor(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            requireActivity().findViewById<Chip>(R.id.enhancementsChip).closeIconTint = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCardColor()))
            requireActivity().findViewById<Chip>(R.id.enhancementsChip).chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(CustomColorGenerator(requireContext()).generateCustomColorPrimary()))
        }

        val navigationDrawable = topAppBar?.navigationIcon
        navigationDrawable?.mutate()

        if (MenuTintData(requireContext()).loadMenuTint()) {
            if (AccentColor(requireContext()).loadAccent() == 5) {
                navigationDrawable?.colorFilter = BlendModeColorFilter(
                    Color.parseColor(CustomColorGenerator(requireContext()).generateMenuTintColor()),
                    BlendMode.SRC_ATOP
                )
            }
            else {
                val typedValue = TypedValue()
                activity?.theme?.resolveAttribute(R.attr.historyActionBarIconTint, typedValue, true)
                val id = typedValue.resourceId
                navigationDrawable?.colorFilter = BlendModeColorFilter(
                    ContextCompat.getColor(requireContext(), id),
                    BlendMode.SRC_ATOP
                )
            }
        }
        else {
            val typedValue = TypedValue()
            activity?.theme?.resolveAttribute(R.attr.textColor, typedValue, true)
            val id = typedValue.resourceId
            navigationDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), id), BlendMode.SRC_ATOP)
        }

        if (BuildConfig.FLAVOR == "Internal") {
            topAppBar?.title = "Patch Notes (Internal)"
        }

        topAppBar?.setNavigationOnClickListener {
            Vibrate().vibration(requireContext())
            activity?.supportFragmentManager?.popBackStack()
        }

        val bugFixesChip = requireView().findViewById<Chip>(R.id.bugFixesChip)
        if (BuildConfig.FLAVOR == "Internal") {
            bugFixesChip.text = bugFixesArrayInternal.count().toString()
        }
        else {
            bugFixesChip.text = bugFixesArray.count().toString()
        }

        val newFeaturesChip = requireView().findViewById<Chip>(R.id.newFeaturesChip)
        if (BuildConfig.FLAVOR == "Internal") {
            newFeaturesChip.text = newFeaturesArrayInternal.count().toString()
        }
        else {
            newFeaturesChip.text = newFeaturesArray.count().toString()
        }

        val enhancementsChip = requireView().findViewById<Chip>(R.id.enhancementsChip)
        if (BuildConfig.FLAVOR == "Internal") {
            enhancementsChip.text = enhancementsArrayInternal.count().toString()
        }
        else {
            enhancementsChip.text = enhancementsArray.count().toString()
        }

        val bugFixesConstraint = requireView().findViewById<ConstraintLayout>(R.id.bugFixesConstraint)

        bugFixesConstraint.setOnClickListener {
            Vibrate().vibration(requireContext())
            val bugFixesRecyclerView = requireView().findViewById<RecyclerView>(R.id.bugFixesRecyclerView)
            bugFixesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            if (BuildConfig.FLAVOR == "Internal") {
                bugFixesRecyclerView.adapter =
                    PatchNotesBugFixesAdapter(requireContext(), bugFixesArrayInternal)
            }
            else {
                bugFixesRecyclerView.adapter =
                    PatchNotesBugFixesAdapter(requireContext(), bugFixesArray)
            }
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
            if (BuildConfig.FLAVOR == "Internal") {
                newFeaturesRecyclerView.adapter =
                    PatchNotesNewFeaturesAdapter(requireContext(), newFeaturesArrayInternal)
            }
            else {
                newFeaturesRecyclerView.adapter =
                    PatchNotesNewFeaturesAdapter(requireContext(), newFeaturesArray)
            }

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
            if (BuildConfig.FLAVOR == "Internal") {
                enhancementsRecyclerView.adapter =
                    PatchNotesEnhancementsAdapter(requireContext(), enhancementsArrayInternal)
            }
            else {
                enhancementsRecyclerView.adapter =
                    PatchNotesEnhancementsAdapter(requireContext(), enhancementsArray)
            }

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

        Version(requireContext()).setVersion(BuildConfig.VERSION_NAME)

        val runnable = Runnable {
            (context as MainActivity).changeSettingsBadge()
        }
        MainActivity().runOnUiThread(runnable)
    }
}